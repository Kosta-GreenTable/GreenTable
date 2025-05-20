package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 고객센터(Customer Service) 관련 요청을 처리하는 컨트롤러
 */
public class CsController implements Controller {
    
    /**
     * 클라이언트의 요청을 처리하는 메인 메소드
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String methodName = request.getParameter("methodName");
        
        // methodName 파라미터 값에 따라 적절한 메소드 호출
        if ("main".equals(methodName)) {
            return main(request, response);
        } else if ("noticeDetail".equals(methodName)) {
            return noticeDetail(request, response);
        } else if ("faqDetail".equals(methodName)) {
            return faqDetail(request, response);
        }
        
        // 기본적으로 메인 페이지로 이동
        return main(request, response);
    }
    
    /**
     * 고객센터 메인 페이지를 표시
     * - 공지사항과 FAQ 목록을 보여줌
     * - 실제 데이터는 cs.js에서 클라이언트 측에서 로드됨
     */
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // cs.jsp 페이지로 포워딩
        return new ModelAndView("/cs/cs.jsp");
    }
    
    /**
     * 공지사항 상세보기
     * - 현재는 클라이언트 측(cs.js)에서 처리하므로 기본 페이지로 리다이렉션
     * - 추후 서버 측 데이터베이스 구현 시 사용 예정
     */
    public ModelAndView noticeDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String noticeId = request.getParameter("id");
        
        // 여기서 noticeId를 사용하여 데이터베이스에서 공지사항 정보를 조회할 수 있음
        // 현재는 클라이언트 측에서 처리하므로 기본 페이지로 이동
        
        return new ModelAndView("/cs/cs.jsp");
    }
    
    /**
     * FAQ 상세보기
     * - 현재는 클라이언트 측(cs.js)에서 처리하므로 기본 페이지로 리다이렉션
     * - 추후 서버 측 데이터베이스 구현 시 사용 예정
     */
    public ModelAndView faqDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String faqId = request.getParameter("id");
        
        // 여기서 faqId를 사용하여 데이터베이스에서 FAQ 정보를 조회할 수 있음
        // 현재는 클라이언트 측에서 처리하므로 기본 페이지로 이동
        
        return new ModelAndView("/cs/cs.jsp");
    }
}