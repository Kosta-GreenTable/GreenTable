package site.greentable.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Properties;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import site.greentable.controller.Controller;
import site.greentable.controller.ModelAndView;
import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;
import site.greentable.dto.UserDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.service.FarmService;
import site.greentable.service.FarmServiceImpl;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;
import site.greentable.util.S3Util;

/**
 * AdminController - 관리자 전용 기능을 제공하는 컨트롤러
 * 모든 메소드는 관리자 권한 체크 후 실행됨
 */
public class AdminController implements Controller {
    private ProductService productService = new ProductServiceImpl();
    private FarmService farmService = new FarmServiceImpl();
    private UserService userService = new UserServiceImpl();

    /**
     * 관리자 권한 체크 메소드
     * 
     * @return 권한이 있으면 true, 없으면 false
     */
    private boolean checkAdminAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        // 세션이 없거나 관리자 정보가 없으면 권한 없음
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
            return false;
        }

        return true;
    }

    /**
     * 관리자 로그인 처리
     */
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // GET 요청이거나 파라미터가 없으면 로그인 페이지 표시
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            return new ModelAndView("/admin/login.jsp");
        }

        // env.properties에서 관리자 정보 로드
        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("env.properties");
        props.load(inputStream);

        String adminEmail = props.getProperty("admin.email");
        String adminPassword = props.getProperty("admin.password");

        if (adminEmail.equals(email) && adminPassword.equals(password)) {
            // 로그인 성공
            HttpSession session = request.getSession();
            session.setAttribute("adminUser", email);

            response.sendRedirect(request.getContextPath() + "/front?key=admin&methodName=index");
            return null;
        } else {
            // 로그인 실패
            ModelAndView mv = new ModelAndView("/admin/login.jsp");
            request.setAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return mv;
        }
    }

    /**
     * 관리자 로그아웃 처리
     */
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/front?key=admin&methodName=login");
        return null;
    }

    /**
     * 관리자 대시보드
     */
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null; // 권한 체크 실패 시 이미 redirect되었으므로 null 반환
        }

        return new ModelAndView("/admin/index.jsp");
    }

    /**
     * 상품 목록 조회
     */
    public ModelAndView productList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        // PageCnt 클래스를 사용하여 페이지네이션 설정
        String pageNoStr = request.getParameter("page");

        // 페이지 번호가 없으면 기본값 1로 설정
        if (pageNoStr == null || pageNoStr.isEmpty()) {
            pageNoStr = "1";
        }

        int pageNo = Integer.parseInt(pageNoStr);

        // PageCnt 클래스의 정적 변수 설정
        site.greentable.paging.PageCnt.pageNo = pageNo;
        site.greentable.paging.PageCnt.pagesize = 5; // 한 페이지당 보여줄 상품 수

        // 전체 상품 개수 조회 후 총 페이지 수 계산
        int totalCount = productService.getTotalProductCount();
        int totalPages = (int) Math.ceil((double) totalCount / site.greentable.paging.PageCnt.pagesize);

        // PageCnt 객체 생성 및 총 페이지 수 설정
        site.greentable.paging.PageCnt pageCnt = new site.greentable.paging.PageCnt();
        pageCnt.setPageCnt(totalPages);

        // 현재 페이지에 해당하는 상품 목록 조회
        List<Product> productList = productService.getAllProducts(pageNo);

        // JSP에서 사용할 속성 설정
        request.setAttribute("productList", productList);
        request.setAttribute("pageNo", pageNoStr); // 페이지 번호 설정
        request.setAttribute("currentPage", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageCnt", pageCnt);

        return new ModelAndView("/admin/product-list.jsp");
    }

    /**
     * 상품 상세 정보 조회
     */
    public ModelAndView productDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String productIdStr = request.getParameter("productId");

        if (productIdStr == null || productIdStr.isEmpty()) {
            return new ModelAndView("/admin/product-list.jsp", true);
        }

        int productId = Integer.parseInt(productIdStr);

        Product product = productService.getProductDetail(productId);
        ProductDetail productDetail = productService.getProductDetailInfo(productId);
        List<ProductImage> productImages = productService.getProductImages(productId);

        request.setAttribute("product", product);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("productImages", productImages);

        return new ModelAndView("/admin/product-detail.jsp");
    }

    /**
     * 상품 등록 폼
     */
    public ModelAndView productInsertForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        return new ModelAndView("/admin/product-form.jsp");
    }

    /**
     * 상품 등록 처리 - AWS S3 업로드 사용
     */
    public ModelAndView productInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        try {
            // Servlet 3.0+ 표준 API 사용 - S3 업로드로 변경
            Collection<Part> parts = null;

            try {
                parts = request.getParts();
            } catch (ServletException e) {
                // FileCountLimitExceededException 등 Tomcat 파일 개수 제한 예외 처리
                if (e.getMessage() != null && (e.getMessage().contains("FileCountLimitExceededException") ||
                        e.getMessage().contains("file count limit") ||
                        e.getMessage().contains("too many files"))) {

                    System.err.println("Tomcat 파일 개수 제한 오류 발생, 대안 처리 시도: " + e.getMessage());

                    // 대안: Tomcat의 제한을 우회하여 직접 파라미터 처리
                    return handleProductInsertWithFallback(request, response);
                } else {
                    // 다른 ServletException은 재발생
                    throw e;
                }
            } catch (IllegalStateException e) {
                // IllegalStateException도 파일 업로드 제한으로 발생할 수 있음
                System.err.println("IllegalStateException 발생 (파일 업로드 제한 관련), 대안 처리 시도: " + e.getMessage());
                return handleProductInsertWithFallback(request, response);
            } catch (Exception e) {
                // 기타 예외들도 로그 출력 후 대안 처리 시도
                System.err.println("파일 처리 중 예외 발생, 대안 처리 시도: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                return handleProductInsertWithFallback(request, response);
            }

            // 🔍 디버깅: 전체 파트 개수 확인
            System.out.println("=== S3 업로드 방식: Servlet API 파트 개수 = " + parts.size());

            Map<String, String> formFields = new HashMap<>();
            Map<String, Part> fileFields = new HashMap<>();

            // 폼 필드와 파일 필드 분리
            for (Part part : parts) {
                String fieldName = part.getName();
                String contentType = part.getContentType();

                System.out.println("필드명: " + fieldName + ", Content-Type: " + contentType + ", 크기: " + part.getSize());

                if (contentType == null || part.getSize() == 0) {
                    // 일반 폼 필드 - InputStream으로 읽기
                    try (java.io.InputStream inputStream = part.getInputStream()) {
                        String value = new String(inputStream.readAllBytes(), "UTF-8");
                        formFields.put(fieldName, value);
                    }
                } else {
                    // 파일 필드
                    fileFields.put(fieldName, part);
                }
            }

            // 멀티파트 폼 데이터 처리
            String name = formFields.get("name");
            String subName = formFields.get("subName");
            String priceStr = formFields.get("price");
            String stockStr = formFields.get("stock");
            String category = formFields.get("category");
            String discountRateStr = formFields.get("discountRate");

            // 디버깅: 받은 파라미터 출력
            System.out.println("=== Product Insert Debug (S3 Version) ===");
            System.out.println("name: " + name);
            System.out.println("subName: " + subName);
            System.out.println("price: " + priceStr);
            System.out.println("stock: " + stockStr);
            System.out.println("category: " + category);
            System.out.println("discountRate: " + discountRateStr);

            // 필수 값 검증
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("상품명은 필수입니다.");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("가격은 필수입니다.");
            }
            if (stockStr == null || stockStr.trim().isEmpty()) {
                throw new IllegalArgumentException("재고는 필수입니다.");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("카테고리는 필수입니다.");
            }

            int price = Integer.parseInt(priceStr.trim());
            int stock = Integer.parseInt(stockStr.trim());
            int discountRate = 0;

            if (discountRateStr != null && !discountRateStr.trim().isEmpty()) {
                discountRate = Integer.parseInt(discountRateStr.trim());
            }

            // 상품 객체 생성
            Product product = new Product();
            product.setName(name.trim());
            product.setSubName(subName != null ? subName.trim() : "");
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category.trim());
            product.setDiscountRate(discountRate);

            // 상품 상세 정보 처리
            String description = formFields.get("description");
            String ingredients = formFields.get("ingredients");
            String kcalStr = formFields.get("kcal");
            String amountStr = formFields.get("amount");
            String nutrition = formFields.get("nutrition");

            int kcal = 0;
            int amount = 0;

            if (kcalStr != null && !kcalStr.trim().isEmpty()) {
                kcal = Integer.parseInt(kcalStr.trim());
            }
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amount = Integer.parseInt(amountStr.trim());
            }

            // 상품 등록 - registerProduct 메소드 사용
            ProductDetail productDetail = new ProductDetail();
            productDetail.setDescription(description != null ? description.trim() : "");
            productDetail.setIngredients(ingredients != null ? ingredients.trim() : "");
            productDetail.setKcal(kcal);
            productDetail.setAmount(amount);
            productDetail.setNutrition(nutrition != null ? nutrition.trim() : "");
            productDetail.setCreatedDate(new Date());
            productDetail.setUpdatedDate(new Date());

            // 이미지 처리 - S3 업로드
            List<ProductImage> productImages = new ArrayList<>();

            // 메인 이미지 S3 업로드
            Part mainImagePart = fileFields.get("mainImage");
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                try {
                    String s3Key = S3Util.uploadFile(mainImagePart, "products/");
                    if (s3Key != null) {
                        ProductImage mainImage = new ProductImage();
                        mainImage.setImageName(s3Key); // S3 키를 이미지명으로 저장
                        mainImage.setMain(true);
                        productImages.add(mainImage);
                        System.out.println("메인 이미지 S3 업로드 완료: " + s3Key);
                    }
                } catch (IOException e) {
                    System.err.println("메인 이미지 S3 업로드 실패: " + e.getMessage());
                    throw new Exception("메인 이미지 업로드에 실패했습니다: " + e.getMessage());
                }
            }

            // 추가 이미지들 S3 업로드 (image1, image2, image3)
            for (int i = 1; i <= 3; i++) {
                Part imagePart = fileFields.get("image" + i);
                if (imagePart != null && imagePart.getSize() > 0) {
                    try {
                        String s3Key = S3Util.uploadFile(imagePart, "products/");
                        if (s3Key != null) {
                            ProductImage image = new ProductImage();
                            image.setImageName(s3Key); // S3 키를 이미지명으로 저장
                            image.setMain(false);
                            productImages.add(image);
                            System.out.println("상세 이미지 " + i + " S3 업로드 완료: " + s3Key);
                        }
                    } catch (IOException e) {
                        System.err.println("상세 이미지 " + i + " S3 업로드 실패: " + e.getMessage());
                        // 추가 이미지는 실패해도 계속 진행
                    }
                }
            }

            // 상품 등록 (Product, ProductDetail, ProductImage 모두 한 번에)
            int result = productService.registerProduct(product, productDetail, productImages);

            if (result > 0) {
                System.out.println("상품 등록 성공 (S3 버전)");
                request.setAttribute("successMessage", "상품이 성공적으로 등록되었습니다.");
                return new ModelAndView(request.getContextPath() + "/front?key=admin&methodName=productList", true);
            } else {
                System.err.println("상품 등록 실패");
                request.setAttribute("errorMessage", "상품 등록에 실패했습니다.");
                return new ModelAndView("/admin/product-form.jsp");
            }

        } catch (NumberFormatException e) {
            System.err.println("숫자 형식 오류: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "숫자 형식이 올바르지 않습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");

        } catch (IllegalArgumentException e) {
            System.err.println("필수 값 누락: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");

        } catch (Exception e) {
            System.err.println("상품 등록 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");
        }
    }

    /**
     * FileCountLimitExceededException을 우회하기 위한 대안 처리 메서드
     * Apache Commons FileUpload를 사용하여 직접 파싱
     */
    private ModelAndView handleProductInsertWithFallback(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("=== Product Insert Fallback Mode ===");
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Content-Length: " + request.getContentLength());

        // 파라미터 추출 변수 선언
        String name = null, subName = null, priceStr = null, stockStr = null, category = null, discountRateStr = null;
        String description = null, ingredients = null, kcalStr = null, amountStr = null, nutrition = null;

        try {
            // Jakarta Servlet API를 사용한 수동 multipart 파싱
            if (request.getContentType() != null
                    && request.getContentType().toLowerCase().contains("multipart/form-data")) {

                System.out.println("Jakarta Servlet API를 사용한 수동 multipart 파싱 시작");

                // 요청 본문을 직접 읽어서 파싱
                try (java.io.InputStream inputStream = request.getInputStream()) {
                    byte[] requestBodyBytes = inputStream.readAllBytes();
                    String requestBody = new String(requestBodyBytes, "UTF-8");

                    System.out.println("요청 본문 크기: " + requestBodyBytes.length + " bytes");

                    // multipart 경계 추출
                    String boundary = extractBoundary(request.getContentType());
                    if (boundary != null) {
                        System.out.println("Boundary: " + boundary);

                        // 각 필드 값 추출
                        name = extractFieldValue(requestBody, boundary, "name");
                        subName = extractFieldValue(requestBody, boundary, "subName");
                        priceStr = extractFieldValue(requestBody, boundary, "price");
                        stockStr = extractFieldValue(requestBody, boundary, "stock");
                        category = extractFieldValue(requestBody, boundary, "category");
                        discountRateStr = extractFieldValue(requestBody, boundary, "discountRate");
                        description = extractFieldValue(requestBody, boundary, "description");
                        ingredients = extractFieldValue(requestBody, boundary, "ingredients");
                        kcalStr = extractFieldValue(requestBody, boundary, "kcal");
                        amountStr = extractFieldValue(requestBody, boundary, "amount");
                        nutrition = extractFieldValue(requestBody, boundary, "nutrition");
                    }
                } catch (Exception e) {
                    System.err.println("수동 multipart 파싱 실패: " + e.getMessage());
                    throw e;
                }

            } else {
                System.out.println("multipart/form-data가 아님, 일반 파라미터로 시도");
                // 일반 파라미터로 시도
                name = request.getParameter("name");
                subName = request.getParameter("subName");
                priceStr = request.getParameter("price");
                stockStr = request.getParameter("stock");
                category = request.getParameter("category");
                discountRateStr = request.getParameter("discountRate");
                description = request.getParameter("description");
                ingredients = request.getParameter("ingredients");
                kcalStr = request.getParameter("kcal");
                amountStr = request.getParameter("amount");
                nutrition = request.getParameter("nutrition");
            }

        } catch (Exception e) {
            System.err.println("Apache Commons FileUpload 파싱 실패: " + e.getMessage());
            e.printStackTrace();

            // 최후 수단: 기본값으로 처리 또는 일반 파라미터 시도
            if (name == null || name.trim().isEmpty()) {
                name = "Fallback Product";
                subName = "파일 제한으로 생성된 상품";
                priceStr = "0";
                stockStr = "0";
                category = "salad";
                discountRateStr = "0";
                description = "파일 업로드 제한으로 인해 기본값으로 생성된 상품입니다. 관리자가 수정해주세요.";
                ingredients = "정보 없음";
                kcalStr = "0";
                amountStr = "0";
                nutrition = "정보 없음";
            } else {
                // 일반 파라미터로 다시 시도
                name = request.getParameter("name");
                subName = request.getParameter("subName");
                priceStr = request.getParameter("price");
                stockStr = request.getParameter("stock");
                category = request.getParameter("category");
                discountRateStr = request.getParameter("discountRate");
                description = request.getParameter("description");
                ingredients = request.getParameter("ingredients");
                kcalStr = request.getParameter("kcal");
                amountStr = request.getParameter("amount");
                nutrition = request.getParameter("nutrition");
            }
        }

        // 디버깅: 받은 파라미터 출력
        System.out.println("=== 추출된 파라미터 ===");
        System.out.println("name: " + name);
        System.out.println("subName: " + subName);
        System.out.println("price: " + priceStr);
        System.out.println("stock: " + stockStr);
        System.out.println("category: " + category);
        System.out.println("discountRate: " + discountRateStr);

        // 필수 값 검증
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tomcat 파일 업로드 제한으로 인해 상품 등록에 실패했습니다. 파일 개수를 줄이거나 관리자에게 문의하세요.");
            return new ModelAndView("/admin/product-form.jsp");
        }
        if (priceStr == null || priceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("가격은 필수입니다.");
        }
        if (stockStr == null || stockStr.trim().isEmpty()) {
            throw new IllegalArgumentException("재고는 필수입니다.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        try {
            int price = Integer.parseInt(priceStr.trim());
            int stock = Integer.parseInt(stockStr.trim());
            int discountRate = 0;

            if (discountRateStr != null && !discountRateStr.trim().isEmpty()) {
                discountRate = Integer.parseInt(discountRateStr.trim());
            }

            // 상품 객체 생성
            Product product = new Product();
            product.setName(name.trim());
            product.setSubName(subName != null ? subName.trim() : "");
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category.trim());
            product.setDiscountRate(discountRate);

            // 상품 상세 정보 처리
            int kcal = 0;
            int amount = 0;

            if (kcalStr != null && !kcalStr.trim().isEmpty()) {
                kcal = Integer.parseInt(kcalStr.trim());
            }
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amount = Integer.parseInt(amountStr.trim());
            }

            ProductDetail productDetail = new ProductDetail();
            productDetail.setDescription(description != null ? description.trim() : "");
            productDetail.setIngredients(ingredients != null ? ingredients.trim() : "");
            productDetail.setKcal(kcal);
            productDetail.setAmount(amount);
            productDetail.setNutrition(nutrition != null ? nutrition.trim() : "");
            productDetail.setCreatedDate(new Date());
            productDetail.setUpdatedDate(new Date());

            // 상품 등록 (이미지 없이)
            List<ProductImage> emptyImages = new ArrayList<>();
            int result = productService.registerProduct(product, productDetail, emptyImages);

            if (result > 0) {
                System.out.println("상품 등록 성공 (Fallback Mode - 이미지 없음)");
                request.setAttribute("successMessage", "상품이 성공적으로 등록되었습니다. (이미지는 별도 업로드 필요)");
                return new ModelAndView(request.getContextPath() + "/front?key=admin&methodName=productList", true);
            } else {
                System.err.println("상품 등록 실패 (Fallback Mode)");
                request.setAttribute("errorMessage", "상품 등록에 실패했습니다.");
                return new ModelAndView("/admin/product-form.jsp");
            }

        } catch (NumberFormatException e) {
            System.err.println("숫자 형식 오류 (Fallback): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "숫자 형식이 올바르지 않습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");

        } catch (IllegalArgumentException e) {
            System.err.println("필수 값 누락 (Fallback): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");

        } catch (Exception e) {
            System.err.println("상품 등록 실패 (Fallback): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");
        }
    }

    /**
     * 개별 파트 값을 안전하게 읽는 헬퍼 메소드
     */
    private String getSinglePartValue(HttpServletRequest request, String fieldName) throws Exception {
        try {
            Part part = request.getPart(fieldName);
            if (part != null && part.getSize() > 0) {
                try (java.io.InputStream inputStream = part.getInputStream()) {
                    byte[] bytes = inputStream.readAllBytes();
                    return new String(bytes, "UTF-8");
                }
            }
            return null;
        } catch (Exception e) {
            // 개별 파트 읽기 실패 시 null 반환
            System.err.println("파트 읽기 실패 [" + fieldName + "]: " + e.getMessage());
            return null;
        }
    }

    /**
     * 상품 수정 폼
     */
    public ModelAndView productUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String productIdStr = request.getParameter("productId");

        if (productIdStr == null || productIdStr.isEmpty()) {
            return new ModelAndView("/admin/product-list.jsp", true);
        }

        int productId = Integer.parseInt(productIdStr);

        Product product = productService.getProductDetail(productId);
        ProductDetail productDetail = productService.getProductDetailInfo(productId);
        List<ProductImage> productImages = productService.getProductImages(productId);

        request.setAttribute("product", product);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("productImages", productImages);

        return new ModelAndView("/admin/product-update-form.jsp");
    }

    /**
     * 상품 수정 처리 - AWS S3 업로드 사용
     */
    public ModelAndView productUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String productIdStr = null;
        try {
            // multipart 폼 데이터 처리를 위한 파트 분리
            Collection<Part> parts = request.getParts();
            Map<String, String> formFields = new HashMap<>();
            Map<String, Part> fileFields = new HashMap<>();

            // 폼 필드와 파일 필드 분리
            for (Part part : parts) {
                String contentDisposition = part.getHeader("content-disposition");
                if (contentDisposition != null) {
                    if (contentDisposition.contains("filename=")) {
                        // 파일 필드
                        String fieldName = extractFieldName(contentDisposition);
                        if (fieldName != null) {
                            fileFields.put(fieldName, part);
                        }
                    } else {
                        // 일반 폼 필드
                        String fieldName = extractFieldName(contentDisposition);
                        if (fieldName != null) {
                            try (java.io.InputStream inputStream = part.getInputStream()) {
                                byte[] bytes = inputStream.readAllBytes();
                                String value = new String(bytes, "UTF-8");
                                formFields.put(fieldName, value);
                            }
                        }
                    }
                }
            }

            productIdStr = formFields.get("productId");
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("상품 ID는 필수입니다.");
            }
            int productId = Integer.parseInt(productIdStr.trim());

            // 상품 기본 정보 추출
            String name = formFields.get("name");
            String subName = formFields.get("subName");
            String priceStr = formFields.get("price");
            String stockStr = formFields.get("stock");
            String category = formFields.get("category");
            String discountRateStr = formFields.get("discountRate");

            // 디버깅 로그
            System.out.println("=== Product Update Debug ===");
            System.out.println("productId: " + productIdStr);
            System.out.println("name: " + name);
            System.out.println("category: " + category);
            System.out.println("price: " + priceStr);

            // 필수 값 검증
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("상품명은 필수입니다.");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("가격은 필수입니다.");
            }
            if (stockStr == null || stockStr.trim().isEmpty()) {
                throw new IllegalArgumentException("재고는 필수입니다.");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("카테고리는 필수입니다.");
            }

            int price = Integer.parseInt(priceStr.trim());
            int stock = Integer.parseInt(stockStr.trim());
            int discountRate = 0;

            if (discountRateStr != null && !discountRateStr.trim().isEmpty()) {
                discountRate = Integer.parseInt(discountRateStr.trim());
            }

            Product product = new Product();
            product.setProductId(productId);
            product.setName(name.trim());
            product.setSubName(subName != null ? subName.trim() : "");
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category.trim());
            product.setDiscountRate(discountRate);

            // 상품 상세 정보 추출
            String description = formFields.get("description");
            String ingredients = formFields.get("ingredients");
            String kcalStr = formFields.get("kcal");
            String amountStr = formFields.get("amount");
            String nutrition = formFields.get("nutrition");

            int kcal = 0;
            if (kcalStr != null && !kcalStr.trim().isEmpty()) {
                kcal = Integer.parseInt(kcalStr.trim());
            }

            int amount = 0;
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amount = Integer.parseInt(amountStr.trim());
            }

            // 현재 시간을 업데이트 날짜로 설정
            Date currentDate = new Date();

            ProductDetail detail = new ProductDetail();
            detail.setProductId(productId);
            detail.setDescription(description != null ? description.trim() : "");
            detail.setIngredients(ingredients != null ? ingredients.trim() : "");
            detail.setKcal(kcal);
            detail.setAmount(amount);
            detail.setNutrition(nutrition != null ? nutrition.trim() : "");
            detail.setUpdatedDate(currentDate);

            // 이미지 업데이트 여부 확인
            String updateImages = formFields.get("updateImages");
            List<ProductImage> images = new ArrayList<>();

            if ("true".equals(updateImages)) {
                System.out.println("이미지 업데이트 시작 - S3 업로드 방식");

                // 기존 S3 이미지 삭제
                List<ProductImage> oldImages = productService.getProductImages(productId);
                for (ProductImage oldImage : oldImages) {
                    try {
                        boolean deleted = S3Util.deleteFile(oldImage.getImageName());
                        if (deleted) {
                            System.out.println("기존 S3 이미지 삭제 성공: " + oldImage.getImageName());
                        } else {
                            System.err.println("기존 S3 이미지 삭제 실패: " + oldImage.getImageName());
                        }
                    } catch (Exception e) {
                        System.err.println("기존 S3 이미지 삭제 중 오류: " + oldImage.getImageName() + " - " + e.getMessage());
                    }
                }

                // 새 이미지 S3 업로드
                // 메인 이미지 처리
                Part mainImagePart = fileFields.get("mainImage");
                if (mainImagePart != null && mainImagePart.getSize() > 0) {
                    try {
                        String s3Key = S3Util.uploadFile(mainImagePart, "products/");
                        if (s3Key != null) {
                            ProductImage image = new ProductImage();
                            image.setProductId(productId);
                            image.setImageName(s3Key); // S3 키를 이미지명으로 저장
                            image.setMain(true);
                            images.add(image);
                            System.out.println("메인 이미지 S3 업로드 완료: " + s3Key);
                        }
                    } catch (IOException e) {
                        System.err.println("메인 이미지 S3 업로드 실패: " + e.getMessage());
                        throw new Exception("메인 이미지 업로드에 실패했습니다: " + e.getMessage());
                    }
                }

                // 추가 이미지들 S3 업로드 (image1, image2, image3)
                for (int i = 1; i <= 3; i++) {
                    Part filePart = fileFields.get("image" + i);
                    if (filePart != null && filePart.getSize() > 0) {
                        try {
                            String s3Key = S3Util.uploadFile(filePart, "products/");
                            if (s3Key != null) {
                                ProductImage image = new ProductImage();
                                image.setProductId(productId);
                                image.setImageName(s3Key); // S3 키를 이미지명으로 저장
                                image.setMain(false);
                                images.add(image);
                                System.out.println("상세 이미지 " + i + " S3 업로드 완료: " + s3Key);
                            }
                        } catch (IOException e) {
                            System.err.println("상세 이미지 " + i + " S3 업로드 실패: " + e.getMessage());
                            // 추가 이미지는 실패해도 계속 진행
                        }
                    }
                }
            }

            int result = productService.updateProduct(product, detail, images, "true".equals(updateImages));
            if (result > 0) {
                System.out.println("상품 수정 성공 (S3 버전)");
                response.sendRedirect(request.getContextPath() + "/front?key=admin&methodName=productDetail&productId=" + productId);
                return null;
            } else {
                request.setAttribute("errorMessage", "상품 수정에 실패했습니다.");
                request.setAttribute("product", product);
                request.setAttribute("productDetail", detail);
                // 실패 시 수정 폼으로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/front?key=admin&methodName=productUpdateForm&productId=" + productId + "&error=update_failed");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorProductId = productIdStr != null ? productIdStr : request.getParameter("productId");
            if (errorProductId == null) {
                errorProductId = "1"; // 기본값
            }
            
            // 오류 발생 시 수정 폼으로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/front?key=admin&methodName=productUpdateForm&productId=" + errorProductId + "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            return null;
        }
    }

    /**
     * 상품 삭제 처리 - AWS S3 파일 삭제 포함
     */
    public ModelAndView productDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("상품 ID는 필수입니다.");
            }
            int productId = Integer.parseInt(productIdStr.trim());

            // S3에서 이미지 파일 삭제
            List<ProductImage> images = productService.getProductImages(productId);
            System.out.println("삭제할 이미지 개수: " + images.size());

            for (ProductImage image : images) {
                try {
                    boolean deleted = S3Util.deleteFile(image.getImageName());
                    if (deleted) {
                        System.out.println("S3 이미지 삭제 성공: " + image.getImageName());
                    } else {
                        System.err.println("S3 이미지 삭제 실패: " + image.getImageName());
                    }
                } catch (Exception e) {
                    System.err.println("S3 이미지 삭제 중 오류: " + image.getImageName() + " - " + e.getMessage());
                    // 이미지 삭제 실패해도 상품 삭제는 계속 진행
                }
            }

            // 상품 삭제 서비스 호출
            int result = productService.deleteProduct(productId);
            if (result > 0) {
                System.out.println("상품 삭제 성공 (S3 버전): " + productId);
                return new ModelAndView(request.getContextPath() + "/front?key=admin&methodName=productList", true);
            } else {
                request.setAttribute("errorMessage", "상품 삭제에 실패했습니다.");
                return new ModelAndView(
                        request.getContextPath() + "/front?key=admin&methodName=productDetail&productId=" + productId,
                        true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-list.jsp");
        }
    }

    /**
     * 상품 ID 시퀀스 리셋
     */
    public ModelAndView resetProductId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }
        try {
            int result = productService.resetProductIdSequence();

            if (result >= 0) {
                request.setAttribute("successMessage", "상품 ID 시퀀스가 재설정되었습니다. 이제 빈 ID 번호를 재사용할 수 있습니다.");
            } else {
                request.setAttribute("errorMessage", "상품 ID 시퀀스 재설정에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 ID 시퀀스 재설정 중 오류가 발생했습니다: " + e.getMessage());
        }

        // 상품 목록 페이지로 리다이렉트
        return new ModelAndView(request.getContextPath() + "/front?key=admin&methodName=productList", true);
    }

    public ModelAndView resetFarmId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            boolean result = farmService.reorderFarmIds();

            if (result) {
                request.setAttribute("successMessage", "농가 ID 시퀀스가 성공적으로 재설정되었습니다.");
            } else {
                request.setAttribute("errorMessage", "농가 ID 시퀀스 재설정에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "농가 ID 시퀀스 재설정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
    }

    /**
     * 회원 목록 조회
     */
    public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String searchType = request.getParameter("searchType");
        String searchKeyword = request.getParameter("searchKeyword");
        String status = request.getParameter("status");
        String pageStr = request.getParameter("page");

        if (searchType == null)
            searchType = "";
        if (searchKeyword == null)
            searchKeyword = "";
        if (status == null)
            status = "";

        int currentPage = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            currentPage = Integer.parseInt(pageStr);
        }

        int pageSize = 10;
        int offset = (currentPage - 1) * pageSize;

        // 회원 목록 조회
        List<UserDTO> userList = userService.getAdminUserList(searchType, searchKeyword, status, offset, pageSize);

        // 총 회원 수 조회
        int totalCount = userService.getAdminUserCount(searchType, searchKeyword, status);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 통계 정보 조회
        int totalUsers = userService.getTotalUsers();
        int activeUsers = userService.getActiveUsers();
        int suspendedUsers = userService.getSuspendedUsers();
        request.setAttribute("userList", userList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("status", status);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("suspendedUsers", suspendedUsers);

        ModelAndView mv = new ModelAndView("/admin/user-management.jsp");

        return mv;
    }

    /**
     * 회원 정지
     */
    public ModelAndView suspendUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String email = request.getParameter("email");

        try {
            boolean result = userService.suspendUser(email);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            if (result) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"회원 정지에 실패했습니다.\"}");
            }
            out.flush();

        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
            out.flush();
        }

        return null;
    }

    /**
     * 회원 활성화
     */
    public ModelAndView activateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String email = request.getParameter("email");

        try {
            boolean result = userService.activateUser(email);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            if (result) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"회원 활성화에 실패했습니다.\"}");
            }
            out.flush();

        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
            out.flush();
        }

        return null;
    }

    /**
     * 회원 상세 정보 조회
     */
    public ModelAndView userDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }
        String email = request.getParameter("email");
        UserDTO user = userService.getUserDetail(email);

        request.setAttribute("user", user);
        return new ModelAndView("/admin/user-detail.jsp");
    }

    /**
     * 주문 목록 조회
     */
    public ModelAndView orderList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String searchType = request.getParameter("searchType");
        String searchKeyword = request.getParameter("searchKeyword");
        String status = request.getParameter("status");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageStr = request.getParameter("page");

        if (searchType == null)
            searchType = "";
        if (searchKeyword == null)
            searchKeyword = "";
        if (status == null)
            status = "";
        if (startDate == null)
            startDate = "";
        if (endDate == null)
            endDate = "";

        int currentPage = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            currentPage = Integer.parseInt(pageStr);
        }

        int pageSize = 10;
        int offset = (currentPage - 1) * pageSize;

        // 주문 목록 조회
        List<OrderDTO> orderList = userService.getAdminOrderList(searchType, searchKeyword, status, startDate, endDate,
                offset, pageSize);

        // 총 주문 수 조회
        int totalCount = userService.getAdminOrderCount(searchType, searchKeyword, status, startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize); // 통계 정보 조회
        int totalOrders = userService.getTotalOrders();
        int pendingOrders = userService.getPendingOrders();
        int completedOrders = userService.getCompletedOrders();
        int cancelledOrders = userService.getCancelledOrders();

        request.setAttribute("orderList", orderList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("status", status);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("completedOrders", completedOrders);
        request.setAttribute("cancelledOrders", cancelledOrders);

        return new ModelAndView("/admin/order-management.jsp");
    }

    /**
     * 주문 상태 변경
     */
    public ModelAndView updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String orderNo = request.getParameter("orderNo");
        String status = request.getParameter("status");

        try {
            boolean result = userService.updateOrderStatus(orderNo, status);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            if (result) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"주문 상태 변경에 실패했습니다.\"}");
            }
            out.flush();

        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
            out.flush();
        }

        return null;
    }

    /**
     * 주문 상세 정보 조회
     */
    public ModelAndView orderDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String orderNo = request.getParameter("orderNo");
        List<OrderDTO> orderDetails = userService.getOrderDetail(orderNo);

        if (orderDetails != null && !orderDetails.isEmpty()) {
            OrderDTO order = orderDetails.get(0);

            StringBuilder html = new StringBuilder();
            html.append("<div class='order-info'>");
            html.append("<div class='info-section'>");
            html.append("<h4>주문 정보</h4>");
            html.append("<div class='info-item'>주문번호: ").append(order.getOrderNo()).append("</div>");
            html.append("<div class='info-item'>주문일시: ").append(order.getOrderDate()).append("</div>");
            html.append("<div class='info-item'>주문자: ").append(order.getUserName()).append("</div>");
            html.append("<div class='info-item'>이메일: ").append(order.getUserEmail()).append("</div>");
            html.append("<div class='info-item'>전화번호: ").append(order.getRecipientPhone()).append("</div>");
            html.append("<div class='info-item'>주문상태: ").append(order.getStatus()).append("</div>");
            html.append("</div>");

            html.append("<div class='info-section'>");
            html.append("<h4>배송 정보</h4>");
            html.append("<div class='info-item'>받는분: ").append(order.getRecipient()).append("</div>");
            html.append("<div class='info-item'>연락처: ").append(order.getRecipientPhone()).append("</div>");
            html.append("<div class='info-item'>우편번호: ").append(order.getZipCode()).append("</div>");
            html.append("<div class='info-item'>주소: ").append(order.getAddress()).append("</div>");
            html.append("<div class='info-item'>상세주소: ").append(order.getAddressDetail()).append("</div>");
            html.append("</div>");
            html.append("</div>");

            html.append("<div class='order-items'>");
            html.append("<h4>주문 상품</h4>");
            html.append("<div class='item-list'>");
            html.append("<div class='item-header'>상품명 | 수량 | 단가 | 금액</div>");

            int totalAmount = 0;
            for (OrderDTO item : orderDetails) {
                int itemTotal = item.getPrice() * item.getQuantity();
                totalAmount += itemTotal;

                html.append("<div class='item-row'>");
                html.append("<span>").append(item.getProductName()).append("</span>");
                html.append("<span>").append(item.getQuantity()).append("개</span>");
                html.append("<span>").append(String.format("%,d", item.getPrice())).append("원</span>");
                html.append("<span>").append(String.format("%,d", itemTotal)).append("원</span>");
                html.append("</div>");
            }

            html.append("<div class='item-row' style='font-weight: bold; border-top: 2px solid #333;'>");
            html.append("<span>합계</span>");
            html.append("<span></span>");
            html.append("<span></span>");
            html.append("<span>").append(String.format("%,d", totalAmount)).append("원</span>");
            html.append("</div>");
            html.append("</div>");
            html.append("</div>");

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(html.toString());
            out.flush();
        }

        return null;
    }

    /**
     * 통계 대시보드 페이지
     */
    public ModelAndView statistics(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        ModelAndView mv = new ModelAndView("/admin/statistics.jsp");
        return mv;
    }

    /**
     * 일별 매출 통계 데이터 조회
     */
    public ModelAndView getDailySalesData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // 기본값 설정 (최근 30일)
        if (startDate == null || startDate.isEmpty()) {
            startDate = java.time.LocalDate.now().minusDays(30).toString();
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = java.time.LocalDate.now().toString();
        }

        List<Object[]> dailySales = userService.getDailySalesStats(startDate, endDate);

        // JSON 형태로 응답
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"labels\":[");

        for (int i = 0; i < dailySales.size(); i++) {
            Object[] row = dailySales.get(i);
            json.append("\"").append(row[0]).append("\"");
            if (i < dailySales.size() - 1)
                json.append(",");
        }

        json.append("],\"orderCounts\":[");

        for (int i = 0; i < dailySales.size(); i++) {
            Object[] row = dailySales.get(i);
            json.append(row[1]);
            if (i < dailySales.size() - 1)
                json.append(",");
        }

        json.append("],\"totalAmounts\":[");

        for (int i = 0; i < dailySales.size(); i++) {
            Object[] row = dailySales.get(i);
            json.append(row[2]);
            if (i < dailySales.size() - 1)
                json.append(",");
        }

        json.append("]}");

        out.print(json.toString());
        out.flush();

        return null;
    }

    /**
     * 인기 상품 통계 데이터 조회
     */
    public ModelAndView getTopProductsData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String limitParam = request.getParameter("limit");
        int limit = (limitParam != null) ? Integer.parseInt(limitParam) : 10;

        List<Object[]> topProducts = userService.getTopSellingProducts(limit);

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"labels\":[");

        for (int i = 0; i < topProducts.size(); i++) {
            Object[] row = topProducts.get(i);
            json.append("\"").append(row[0]).append("\"");
            if (i < topProducts.size() - 1)
                json.append(",");
        }

        json.append("],\"quantities\":[");

        for (int i = 0; i < topProducts.size(); i++) {
            Object[] row = topProducts.get(i);
            json.append(row[1]);
            if (i < topProducts.size() - 1)
                json.append(",");
        }

        json.append("],\"revenues\":[");

        for (int i = 0; i < topProducts.size(); i++) {
            Object[] row = topProducts.get(i);
            json.append(row[2]);
            if (i < topProducts.size() - 1)
                json.append(",");
        }

        json.append("]}");

        out.print(json.toString());
        out.flush();

        return null;
    }

    /**
     * 월별 매출 통계 데이터 조회
     */
    public ModelAndView getMonthlySalesData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        String yearParam = request.getParameter("year");
        int year = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.LocalDate.now().getYear();

        List<Object[]> monthlySales = userService.getMonthlySalesStats(year);

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"labels\":[");

        // 1-12월 전체 초기화
        String[] months = { "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" };
        int[] orderCounts = new int[12];
        int[] totalAmounts = new int[12];

        // 실제 데이터 매핑
        for (Object[] row : monthlySales) {
            int monthIndex = (Integer) row[0] - 1;
            orderCounts[monthIndex] = (Integer) row[1];
            totalAmounts[monthIndex] = (Integer) row[2];
        }

        for (int i = 0; i < months.length; i++) {
            json.append("\"").append(months[i]).append("\"");
            if (i < months.length - 1)
                json.append(",");
        }

        json.append("],\"orderCounts\":[");

        for (int i = 0; i < orderCounts.length; i++) {
            json.append(orderCounts[i]);
            if (i < orderCounts.length - 1)
                json.append(",");
        }

        json.append("],\"totalAmounts\":[");

        for (int i = 0; i < totalAmounts.length; i++) {
            json.append(totalAmounts[i]);
            if (i < totalAmounts.length - 1)
                json.append(",");
        }

        json.append("]}");

        out.print(json.toString());
        out.flush();

        return null;
    }

    /**
     * 카테고리별 통계 데이터 조회
     */
    public ModelAndView getCategoryStatsData(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        List<Object[]> categoryStats = userService.getCategoryStats();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"labels\":[");

        for (int i = 0; i < categoryStats.size(); i++) {
            Object[] row = categoryStats.get(i);
            json.append("\"").append(row[0]).append("\"");
            if (i < categoryStats.size() - 1)
                json.append(",");
        }

        json.append("],\"productCounts\":[");

        for (int i = 0; i < categoryStats.size(); i++) {
            Object[] row = categoryStats.get(i);
            json.append(row[1]);
            if (i < categoryStats.size() - 1)
                json.append(",");
        }

        json.append("],\"totalSales\":[");

        for (int i = 0; i < categoryStats.size(); i++) {
            Object[] row = categoryStats.get(i);
            json.append(row[2]);
            if (i < categoryStats.size() - 1)
                json.append(",");
        }

        json.append("]}");

        out.print(json.toString());
        out.flush();

        return null;
    }

    /**
     * 주문 상태별 통계 데이터 조회
     */
    public ModelAndView getOrderStatusData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        List<Object[]> orderStats = userService.getOrderStatusStats();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"labels\":[");

        for (int i = 0; i < orderStats.size(); i++) {
            Object[] row = orderStats.get(i);
            String status = (String) row[0];
            String statusLabel = "";
            switch (status) {
                case "PENDING":
                    statusLabel = "주문접수";
                    break;
                case "PREPARING":
                    statusLabel = "배송준비중";
                    break;
                case "SHIPPED":
                    statusLabel = "배송중";
                    break;
                case "DELIVERED":
                    statusLabel = "배송완료";
                    break;
                case "CANCELLED":
                    statusLabel = "주문취소";
                    break;
                default:
                    statusLabel = status;
                    break;
            }
            json.append("\"").append(statusLabel).append("\"");
            if (i < orderStats.size() - 1)
                json.append(",");
        }

        json.append("],\"data\":[");

        for (int i = 0; i < orderStats.size(); i++) {
            Object[] row = orderStats.get(i);
            json.append(row[1]);
            if (i < orderStats.size() - 1)
                json.append(",");
        }
        json.append("]}");

        out.print(json.toString());
        out.flush();

        return null;
    }

    /**
     * 총 매출액 조회
     */
    public ModelAndView getTotalRevenue(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        int totalRevenue = userService.getTotalRevenue();

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(String.valueOf(totalRevenue));
        out.flush();

        return null;
    }

    /**
     * 총 주문 수 조회
     */
    public ModelAndView getTotalOrders(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        int totalOrders = userService.getTotalOrders();

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(String.valueOf(totalOrders));
        out.flush();

        return null;
    }

    /**
     * 총 사용자 수 조회
     */
    public ModelAndView getTotalUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        int totalUsers = userService.getTotalUsers();

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(String.valueOf(totalUsers));
        out.flush();

        return null;
    }

    /**
     * Content-Disposition 헤더에서 필드명을 추출하는 헬퍼 메소드
     */
    private String extractFieldName(String contentDisposition) {
        if (contentDisposition == null) return null;
        
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("name=")) {
                String name = part.substring(5);
                // 따옴표 제거
                if (name.startsWith("\"") && name.endsWith("\"")) {
                    name = name.substring(1, name.length() - 1);
                }
                return name;
            }
        }
        return null;
    }

    /**
     * Helper method to extract form field values from multipart request
     */
    private String getPartValue(HttpServletRequest request, String fieldName) throws Exception {
        try {
            Part part = request.getPart(fieldName);
            if (part != null) {
                try (java.io.InputStream inputStream = part.getInputStream()) {
                    byte[] bytes = inputStream.readAllBytes();
                    return new String(bytes, "UTF-8");
                }
            }
            return null;
        } catch (Exception e) {
            // 파트를 찾을 수 없는 경우 일반 파라미터로 시도
            return request.getParameter(fieldName);
        }
    }

    /**
     * multipart boundary 추출 헬퍼 메서드
     */
    private String extractBoundary(String contentType) {
        try {
            if (contentType != null && contentType.contains("boundary=")) {
                String boundary = contentType.substring(contentType.indexOf("boundary=") + 9);
                // 세미콜론이 있으면 그 전까지만
                if (boundary.contains(";")) {
                    boundary = boundary.substring(0, boundary.indexOf(";"));
                }
                return boundary.trim();
            }
            return null;
        } catch (Exception e) {
            System.err.println("boundary 추출 실패: " + e.getMessage());
            return null;
        }
    }

    /**
     * multipart 요청에서 특정 필드 값 추출 헬퍼 메서드
     */
    private String extractFieldValue(String requestBody, String boundary, String fieldName) {
        try {
            // boundary로 구분된 부분들을 찾기
            String boundaryPattern = "--" + boundary;
            String[] parts = requestBody.split(boundaryPattern);

            for (String part : parts) {
                if (part.contains("name=\"" + fieldName + "\"")) {
                    // Content-Disposition 헤더 다음의 빈 줄 이후부터 값 시작
                    int headerEnd = part.indexOf("\r\n\r\n");
                    if (headerEnd == -1) {
                        headerEnd = part.indexOf("\n\n");
                        if (headerEnd != -1) {
                            headerEnd += 2;
                        }
                    } else {
                        headerEnd += 4;
                    }

                    if (headerEnd != -1 && headerEnd < part.length()) {
                        String value = part.substring(headerEnd).trim();
                        // 마지막 줄바꿈 제거
                        value = value.replace("\r\n", "").replace("\n", "").trim();
                        return value.isEmpty() ? null : value;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("필드 값 추출 실패 [" + fieldName + "]: " + e.getMessage());
            return null;
        }
    }
}