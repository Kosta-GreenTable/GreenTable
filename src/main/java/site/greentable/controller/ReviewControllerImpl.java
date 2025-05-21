package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import site.greentable.dto.ReviewDTO;
import site.greentable.dto.ReviewImageDTO;
import site.greentable.dto.UserDTO;
import site.greentable.service.ReviewService;
import site.greentable.service.ReviewServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024, // 5MB
        maxRequestSize = 25 * 1024 * 1024 // 25MB
)
public class ReviewControllerImpl implements ReviewController {
    private ReviewService reviewService;

    public ReviewControllerImpl() {
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDTO user = (UserDTO) request.getSession().getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 상품 ID와 주문 상세 ID 가져오기
        String productId = request.getParameter("productId");
        String orderDetailId = request.getParameter("orderDetailId");

        // orderDetailId가 null인 경우 처리 추가
        if (orderDetailId == null) {
            // 사용자가 구매한 이 상품의 가장 최근 주문 상세 ID 가져오기
            orderDetailId = String
                    .valueOf(reviewService.getLatestOrderDetailId(user.getUserId(), Integer.parseInt(productId)));

            // 구매 기록이 없는 경우
            if (orderDetailId.equals("0")) {
                // 에러 페이지 대신 상품 상세 페이지로 돌려보내고 경고 메시지 표시
                request.getSession().setAttribute("alertMessage", "이 상품을 구매한 이력이 없거나 이미 리뷰를 작성했습니다.");

                ModelAndView mv = new ModelAndView();
                mv.setRedirect(true);
                mv.setViewName(
                        request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
                return mv;
            }
        }

        /**
        // 리뷰 작성 가능 여부 확인
        if (!reviewService.isReviewable(user.getUserId(), Integer.parseInt(productId),
                Integer.parseInt(orderDetailId))) {
            // 에러 페이지 대신 상품 상세 페이지로 돌려보내고 경고 메시지 표시
            request.getSession().setAttribute("alertMessage", "이미 리뷰를 작성했거나 구매하지 않은 상품입니다.");

            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
            return mv;
        }
        **/

        request.setAttribute("productId", productId);
        request.setAttribute("orderDetailId", orderDetailId);

        return new ModelAndView("/review/writeForm.jsp");
    }

    @Override
    public ModelAndView writeReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크 - loginUser로 세션 속성 이름 변경
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 파라미터 기본값 설정
        int productId;
        int orderDetailId;
        int rating;
        String content;
        List<ReviewImageDTO> images = new ArrayList<>();

        // 멀티파트 요청인지 확인
        boolean isMultipart = request.getContentType() != null
                && request.getContentType().startsWith("multipart/form-data");

        if (isMultipart) {
            // 파일 저장 경로
            String uploadDir = request.getServletContext().getRealPath("/uploads/reviews");
            File uploadDirObj = new File(uploadDir);
            if (!uploadDirObj.exists()) {
                uploadDirObj.mkdirs();
            }

            // 기본 파라미터 가져오기
            productId = Integer.parseInt(request.getParameter("productId"));
            orderDetailId = Integer.parseInt(request.getParameter("orderDetailId"));
            rating = Integer.parseInt(request.getParameter("rating"));
            content = request.getParameter("content");

            // 파일 처리
            try {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    String fieldName = part.getName();
                    if (fieldName.equals("photos") && part.getSize() > 0) {
                        String fileName = part.getSubmittedFileName();
                        if (fileName != null && !fileName.isEmpty()) {
                            // 파일 이름 중복 방지를 위해 고유한 이름 생성
                            String uniqueName = System.currentTimeMillis() + "_" + fileName;
                            String filePath = uploadDir + File.separator + uniqueName;

                            // 파일 저장
                            part.write(filePath);

                            // 이미지 정보 저장
                            ReviewImageDTO image = new ReviewImageDTO();
                            image.setRealName(uniqueName);
                            image.setOriginalName(fileName);
                            image.setMain(images.isEmpty()); // 첫 번째 이미지를 대표 이미지로 설정

                            images.add(image);
                        }
                    }
                }
            } catch (IOException | IllegalStateException e) {
                request.setAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
                return new ModelAndView("/error.jsp");
            }
        } else {
            // 일반 폼인 경우
            productId = Integer.parseInt(request.getParameter("productId"));
            orderDetailId = Integer.parseInt(request.getParameter("orderDetailId"));
            rating = Integer.parseInt(request.getParameter("rating"));
            content = request.getParameter("content");
        }

        // 리뷰 객체 생성
        ReviewDTO review = new ReviewDTO();
        review.setProductId(productId);
        review.setOrderDetailId(orderDetailId);
        review.setUserId(user.getUserId());
        review.setRating(rating);
        review.setContent(content);
        review.setImages(images);

        // 리뷰 작성 가능 여부 확인
        if (!reviewService.isReviewable(user.getUserId(), productId, orderDetailId)) {
            request.setAttribute("errorMsg", "이미 리뷰를 작성했거나 구매하지 않은 상품입니다.");
            return new ModelAndView("/error.jsp");
        }

        // 리뷰 등록
        reviewService.writeReview(review);

        // 완료 후 상품 상세 페이지로 리다이렉트
        ModelAndView mv = new ModelAndView();
        mv.setRedirect(true);
        mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        return mv;
    }

    @Override
    public ModelAndView myReviews(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크 - loginUser로 세션 속성 이름 변경
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 사용자의 리뷰 목록 가져오기
        List<ReviewDTO> writtenReviews = reviewService.getUserReviews(user.getUserId());
        request.setAttribute("writtenReviews", writtenReviews);
        request.setAttribute("writtenReviewsCount", writtenReviews.size());

        // 작성 가능한 리뷰 목록 가져오기
        List<ReviewDTO> writableReviews = reviewService.getWritableReviews(user.getUserId());
        request.setAttribute("writableReviews", writableReviews);
        request.setAttribute("writableReviewsCount", writableReviews.size());

        // 포토 리뷰 수 계산
        int photoReviewsCount = 0;
        for (ReviewDTO review : writtenReviews) {
            if (review.getReviewImagesCount() > 0) {
                photoReviewsCount++;
            }
        }
        request.setAttribute("photoReviewsCount", photoReviewsCount);

        return new ModelAndView("/user/myreview.jsp");
    }

    @Override
    public ModelAndView deleteReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크 - loginUser로 세션 속성 이름 변경
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 리뷰 ID 가져오기
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        // 리뷰 정보 가져오기
        ReviewDTO review = reviewService.getReview(reviewId);

        // 리뷰 작성자가 현재 로그인한 사용자와 같은지 확인
        if (review.getUserId() != user.getUserId()) {
            // 권한 없음
            request.setAttribute("errorMsg", "리뷰 삭제 권한이 없습니다.");
            return new ModelAndView("/error.jsp");
        }

        int productId = review.getProductId();

        // 리뷰 삭제
        reviewService.deleteReview(reviewId);

        // 리다이렉트 경로 결정 (마이페이지에서 왔는지, 상품 상세에서 왔는지)
        String referer = request.getHeader("Referer");
        ModelAndView mv = new ModelAndView();

        if (referer != null && referer.contains("myreview")) {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=review&methodName=myReviews");
        } else {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        }
        return mv;
    }

    @Override
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크 - loginUser로 세션 속성 이름 변경
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 리뷰 ID 가져오기
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        // 리뷰 정보 가져오기
        ReviewDTO review = reviewService.getReview(reviewId);

        // 리뷰 작성자가 현재 로그인한 사용자와 같은지 확인
        if (review.getUserId() != user.getUserId()) {
            // 권한 없음
            request.setAttribute("errorMsg", "리뷰 수정 권한이 없습니다.");
            return new ModelAndView("/error.jsp");
        }

        request.setAttribute("review", review);

        return new ModelAndView("/review/updateForm.jsp");
    }

    @Override
    public ModelAndView updateReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 체크 - loginUser로 세션 속성 이름 변경
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            // 로그인 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/user/login.jsp");
            return mv;
        }

        // 파라미터 기본값 설정
        int reviewId;
        int rating;
        String content;
        List<ReviewImageDTO> images = new ArrayList<>();
        boolean imageChanged = false;

        // 멀티파트 요청인지 확인
        boolean isMultipart = request.getContentType() != null
                && request.getContentType().startsWith("multipart/form-data");

        if (isMultipart) {
            // 파일 저장 경로
            String uploadDir = request.getServletContext().getRealPath("/uploads/reviews");
            File uploadDirObj = new File(uploadDir);
            if (!uploadDirObj.exists()) {
                uploadDirObj.mkdirs();
            }

            // 기본 파라미터 가져오기
            reviewId = Integer.parseInt(request.getParameter("reviewId"));
            rating = Integer.parseInt(request.getParameter("rating"));
            content = request.getParameter("content");
            imageChanged = Boolean.parseBoolean(request.getParameter("imageChanged"));

            // 파일 처리
            try {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    String fieldName = part.getName();
                    if (fieldName.equals("photos") && part.getSize() > 0) {
                        String fileName = part.getSubmittedFileName();
                        if (fileName != null && !fileName.isEmpty()) {
                            // 이미지가 변경되었음을 표시
                            imageChanged = true;

                            // 파일 이름 중복 방지를 위해 고유한 이름 생성
                            String uniqueName = System.currentTimeMillis() + "_" + fileName;
                            String filePath = uploadDir + File.separator + uniqueName;

                            // 파일 저장
                            part.write(filePath);

                            // 이미지 정보 저장
                            ReviewImageDTO image = new ReviewImageDTO();
                            image.setRealName(uniqueName);
                            image.setOriginalName(fileName);
                            image.setMain(images.isEmpty()); // 첫 번째 이미지를 대표 이미지로 설정

                            images.add(image);
                        }
                    }
                }
            } catch (IOException | IllegalStateException e) {
                request.setAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
                return new ModelAndView("/error.jsp");
            }
        } else {
            // 일반 폼인 경우
            reviewId = Integer.parseInt(request.getParameter("reviewId"));
            rating = Integer.parseInt(request.getParameter("rating"));
            content = request.getParameter("content");
            imageChanged = Boolean.parseBoolean(request.getParameter("imageChanged"));
        }

        // 기존 리뷰 정보 가져오기
        ReviewDTO originalReview = reviewService.getReview(reviewId);

        // 리뷰 작성자가 현재 로그인한 사용자와 같은지 확인
        if (originalReview.getUserId() != user.getUserId()) {
            // 권한 없음
            request.setAttribute("errorMsg", "리뷰 수정 권한이 없습니다.");
            return new ModelAndView("/error.jsp");
        }

        // 리뷰 객체 생성
        ReviewDTO review = new ReviewDTO();
        review.setReviewId(reviewId);
        review.setRating(rating);
        review.setContent(content);
        review.setImages(images);
        review.setImageChanged(imageChanged);

        // 리뷰 수정
        reviewService.updateReview(review);

        // 리다이렉트 경로 결정 (마이페이지에서 왔는지, 상품 상세에서 왔는지)
        String referer = request.getHeader("Referer");
        ModelAndView mv = new ModelAndView();

        if (referer != null && referer.contains("myreview")) {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=review&methodName=myReviews");
        } else {
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId="
                    + originalReview.getProductId());
        }
        return mv;
    }

    @Override
    public ModelAndView getProductReviews(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int page = 1;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<ReviewDTO> reviewList = reviewService.getProductReviews(productId, page);
        request.setAttribute("reviewList", reviewList);

        // 상품 상세 페이지로 리다이렉트
        ModelAndView mv = new ModelAndView();
        mv.setRedirect(true);
        mv.setViewName(request.getContextPath() + "/front?key=product&methodName=detail&productId=" + productId);
        return mv;
    }

    @Override
    public ModelAndView downloadReviewImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName"); // 실제 저장된 파일명
        String originalFileName = request.getParameter("originalFileName"); // 원본 파일명

        // 파일 경로 설정
        String saveDir = request.getServletContext().getRealPath("/uploads/reviews");
        String filePath = saveDir + File.separator + fileName;

        // 파일이 존재하는지 확인
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            // 파일이 없는 경우 에러 페이지로 리다이렉트
            ModelAndView mv = new ModelAndView();
            mv.setRedirect(true);
            mv.setViewName(request.getContextPath() + "/error.jsp");
            return mv;
        }

        // 파일 정보 가져오기
        String mimeType = Files.probeContentType(Paths.get(filePath));
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // 브라우저에 따른 파일명 인코딩 처리
        String userAgent = request.getHeader("User-Agent");
        if (originalFileName == null)
            originalFileName = fileName;

        String downloadFileName;
        if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 || userAgent.indexOf("Edge") > -1) {
            downloadFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (userAgent.indexOf("Firefox") > -1) {
            downloadFileName = "\"" + new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1") + "\"";
        } else if (userAgent.indexOf("Chrome") > -1) {
            downloadFileName = new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");
        } else {
            downloadFileName = "\"" + new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1") + "\"";
        }

        // 응답 헤더 설정
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

        try (
                FileInputStream fis = new FileInputStream(filePath);
                OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int byteRead = 0;

            while ((byteRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 다운로드는 특별 처리
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/blank.jsp");
        return mv;
    }
}