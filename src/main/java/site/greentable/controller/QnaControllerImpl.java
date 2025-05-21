package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.Product;
import site.greentable.dto.QnaDTO;
import site.greentable.dto.UserDTO;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;
import site.greentable.service.QnaService;
import site.greentable.service.QnaServiceImpl;

import java.util.List;

public class QnaControllerImpl implements QnaController {
    private QnaService qnaService;

    public QnaControllerImpl() {
        this.qnaService = new QnaServiceImpl();
    }

    @Override
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 직접 리다이렉트
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return null;
        }

        // 상품 ID 가져오기
        String productIdStr = request.getParameter("productId");
        int productId = Integer.parseInt(productIdStr);
        request.setAttribute("productId", productIdStr);

        // 상품 서비스를 통해 상품 정보 가져오기
        ProductService productService = new ProductServiceImpl();
        Product product = productService.getProductDetail(productId);

        // 상품 정보를 request에 저장
        request.setAttribute("product", product);

        return new ModelAndView("/qna/writeForm.jsp");
    }

    @Override
    public ModelAndView writeQna(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 직접 리다이렉트
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return null;
        }

        // 파라미터 받기
        int productId = Integer.parseInt(request.getParameter("productId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // QnA 객체 생성
        QnaDTO qna = new QnaDTO();
        qna.setProductId(productId);
        qna.setUserId(user.getUserId());
        qna.setTitle(title);
        qna.setContent(content);

        // QnA 등록
        qnaService.writeQna(qna);

        // 완료 후 상품 상세 페이지로 리다이렉트
        ModelAndView mv = new ModelAndView();
        mv.setRedirect(true);
        mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        return mv;
    }

    @Override
    public ModelAndView myQnas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 필터링 파라미터
        String period = request.getParameter("period");
        String status = request.getParameter("status");

        if (period == null || period.isEmpty()) {
            period = "1"; // 기본값 1개월
        }

        if (status == null || status.isEmpty()) {
            status = "all"; // 기본값 전체
        }

        // 유저의 QnA 목록 가져오기
        List<QnaDTO> qnaList;
        if ("all".equals(status) && "1".equals(period)) {
            // 기본 조회 - 필터 없음
            qnaList = qnaService.getUserQnas(user.getUserId());
        } else {
            // 필터 적용 조회
            qnaList = qnaService.getUserQnasWithFilter(user.getUserId(), Integer.parseInt(period), status);
        }
        request.setAttribute("qnaList", qnaList);
        request.setAttribute("period", period);
        request.setAttribute("status", status);

        // 상품 목록 가져오기 (문의 작성 모달에서 사용)
        List<QnaDTO> productList = qnaService.getProductList(user.getUserId());
        request.setAttribute("productList", productList);

        return new ModelAndView("/user/myqna.jsp");
    }

    @Override
    public ModelAndView myQnasWithFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // myQnas 메소드를 재사용
        return myQnas(request, response);
    }

    @Override
    public ModelAndView deleteQna(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 파라미터 받기
        int qnaId = Integer.parseInt(request.getParameter("qnaId"));
        int productId = Integer.parseInt(request.getParameter("productId"));

        // QnA 정보 가져오기
        QnaDTO qna = qnaService.getQna(qnaId);

        // QnA 작성자가 현재 로그인한 사용자와 같은지 확인
        if (qna.getUserId() != user.getUserId()) {
            // 권한 없음 - 상품 상세 페이지로 리다이렉트하고 경고 메시지 표시
            request.getSession().setAttribute("alertMessage", "문의 삭제 권한이 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
            return mv;
        }

        // 이미 답변이 등록된 문의인지 확인
        if ("Y".equals(qna.getIsAnswered())) {
            // 답변이 등록된 문의는 삭제 불가
            request.getSession().setAttribute("alertMessage", "답변이 등록된 문의는 삭제할 수 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
            return mv;
        }

        // QnA 삭제
        qnaService.deleteQna(qnaId, user.getUserId());

        // 리다이렉트 경로 결정 (마이페이지에서 왔는지, 상품 상세에서 왔는지)
        String referer = request.getHeader("Referer");
        ModelAndView mv = new ModelAndView();

        if (referer != null && referer.contains("myqna")) {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=qna&methodName=myQnas");
        } else {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        }
        return mv;
    }

    @Override
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 직접 리다이렉트
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return null;
        }

        // 파라미터 받기
        int qnaId = Integer.parseInt(request.getParameter("qnaId"));

        // QnA 정보 가져오기
        QnaDTO qna = qnaService.getQna(qnaId);

        // QnA 작성자가 현재 로그인한 사용자와 같은지 확인
        if (qna.getUserId() != user.getUserId()) {
            // 권한 없음
            request.getSession().setAttribute("alertMessage", "문의 수정 권한이 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(
                    request.getContextPath() + "/front?key=product&methodName=detail&productId=" + qna.getProductId());
            return mv;
        }

        // 이미 답변이 등록된 문의인지 확인
        if ("Y".equals(qna.getIsAnswered())) {
            // 답변이 등록된 문의는 수정 불가
            request.getSession().setAttribute("alertMessage", "답변이 등록된 문의는 수정할 수 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(
                    request.getContextPath() + "/front?key=product&methodName=detail&productId=" + qna.getProductId());
            return mv;
        }

        request.setAttribute("qna", qna);

        return new ModelAndView("/qna/updateForm.jsp");
    }

    @Override
    public ModelAndView updateQna(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 파라미터 받기
        int qnaId = Integer.parseInt(request.getParameter("qnaId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 기존 QnA 정보 가져오기
        QnaDTO originalQna = qnaService.getQna(qnaId);

        // QnA 작성자가 현재 로그인한 사용자와 같은지 확인
        if (originalQna.getUserId() != user.getUserId()) {
            // 권한 없음
            request.getSession().setAttribute("alertMessage", "문의 수정 권한이 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
            return mv;
        }

        // 이미 답변이 등록된 문의인지 확인
        if ("Y".equals(originalQna.getIsAnswered())) {
            // 답변이 등록된 문의는 수정 불가
            request.getSession().setAttribute("alertMessage", "답변이 등록된 문의는 수정할 수 없습니다.");
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
            return mv;
        }

        // QnA 객체 생성
        QnaDTO qna = new QnaDTO();
        qna.setQnaId(qnaId);
        qna.setProductId(productId);
        qna.setUserId(user.getUserId());
        qna.setTitle(title);
        qna.setContent(content);

        // QnA 수정
        qnaService.updateQna(qna);

        // 리다이렉트 경로 결정 (마이페이지에서 왔는지, 상품 상세에서 왔는지)
        String referer = request.getHeader("Referer");
        ModelAndView mv = new ModelAndView();

        if (referer != null && referer.contains("myqna")) {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=qna&methodName=myQnas");
        } else {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        }
        return mv;
    }

    @Override
    public ModelAndView getProductQnas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int page = 1;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<QnaDTO> qnaList = qnaService.getProductQnas(productId, page);
        request.setAttribute("qnaList", qnaList);

        // 상품 상세 페이지로 리다이렉트
        ModelAndView mv = new ModelAndView();
        mv.setRedirect(true);
        mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        return mv;
    }
}