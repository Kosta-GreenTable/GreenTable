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

/**
 * AdminController - 관리자 전용 기능을 제공하는 컨트롤러
 * 모든 메소드는 관리자 권한 체크 후 실행됨
 */
public class AdminController implements Controller {
    private static final String UPLOAD_DIR = "/assets/images/products";
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

            response.sendRedirect(request.getContextPath() + "/admin/index.jsp");
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
        response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
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
     * 상품 등록 처리
     */
    public ModelAndView productInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        try {
            // 파일 업로드를 위한 경로 설정
            String uploadPath = request.getServletContext().getRealPath(UPLOAD_DIR);
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 상품 기본 정보 추출
            String name = request.getParameter("name");
            String subName = request.getParameter("subName");
            int price = Integer.parseInt(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String category = request.getParameter("category");
            int discountRate = 0;

            if (request.getParameter("discountRate") != null && !request.getParameter("discountRate").isEmpty()) {
                discountRate = Integer.parseInt(request.getParameter("discountRate"));
            }

            // Product 객체 생성
            Product product = new Product();
            product.setName(name);
            product.setSubName(subName);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);
            product.setDiscountRate(discountRate);

            // 상품 상세 정보 추출
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");
            int kcal = 0;
            if (request.getParameter("kcal") != null && !request.getParameter("kcal").isEmpty()) {
                kcal = Integer.parseInt(request.getParameter("kcal"));
            }

            int amount = 0;
            if (request.getParameter("amount") != null && !request.getParameter("amount").isEmpty()) {
                amount = Integer.parseInt(request.getParameter("amount"));
            }

            String nutrition = request.getParameter("nutrition");

            // 현재 시간을 날짜로 설정
            Date currentDate = new Date();

            // ProductDetail 객체 생성
            ProductDetail detail = new ProductDetail();
            detail.setDescription(description);
            detail.setIngredients(ingredients);
            detail.setKcal(kcal);
            detail.setAmount(amount);
            detail.setNutrition(nutrition);
            detail.setCreatedDate(currentDate);
            detail.setUpdatedDate(currentDate);

            // 상품 이미지 처리
            List<ProductImage> images = new ArrayList<>();

            // 파일 입력 필드는 productImage0, productImage1, ... 형식으로 네이밍
            for (int i = 0; i < 4; i++) { // 최대 4개 이미지 처리 (메인 + 추가 3개)
                Part filePart = request.getPart("productImage" + i);
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = UUID.randomUUID().toString() + getExtension(filePart);
                    File file = new File(uploadPath, fileName);

                    // 파일의 상위 디렉토리 존재 여부 확인 및 생성
                    File parentDir = file.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    // 파일 경로 추출 및 저장
                    String filePath = file.getPath();

                    // 파일 저장
                    filePart.write(filePath);

                    // 이미지 정보 생성
                    ProductImage image = new ProductImage();
                    image.setImageName(fileName);

                    // 첫 번째 이미지는 메인 이미지로 설정
                    if (i == 0) {
                        image.setMain(true);
                    } else {
                        image.setMain(false);
                    }

                    images.add(image);
                }
            } // 상품 등록 서비스 호출
            int productId = productService.registerProduct(product, detail, images);

            if (productId > 0) {
                return new ModelAndView(
                        request.getContextPath() + "/front?key=admin&methodName=productDetail&productId=" + productId,
                        true);
            } else {
                request.setAttribute("errorMessage", "상품 등록에 실패했습니다.");
                return new ModelAndView("/admin/product-form.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
            return new ModelAndView("/admin/product-form.jsp");
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
     * 상품 수정 처리
     */
    public ModelAndView productUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            // 상품 기본 정보 추출
            String name = request.getParameter("name");
            String subName = request.getParameter("subName");
            int price = Integer.parseInt(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String category = request.getParameter("category");
            int discountRate = 0;

            if (request.getParameter("discountRate") != null && !request.getParameter("discountRate").isEmpty()) {
                discountRate = Integer.parseInt(request.getParameter("discountRate"));
            }

            Product product = new Product();
            product.setProductId(productId);
            product.setName(name);
            product.setSubName(subName);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);
            product.setDiscountRate(discountRate);

            // 상품 상세 정보 추출
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");
            int kcal = 0;
            if (request.getParameter("kcal") != null && !request.getParameter("kcal").isEmpty()) {
                kcal = Integer.parseInt(request.getParameter("kcal"));
            }

            int amount = 0;
            if (request.getParameter("amount") != null && !request.getParameter("amount").isEmpty()) {
                amount = Integer.parseInt(request.getParameter("amount"));
            }

            String nutrition = request.getParameter("nutrition");

            // 현재 시간을 업데이트 날짜로 설정
            Date currentDate = new Date();

            ProductDetail detail = new ProductDetail();
            detail.setProductId(productId);
            detail.setDescription(description);
            detail.setIngredients(ingredients);
            detail.setKcal(kcal);
            detail.setAmount(amount);
            detail.setNutrition(nutrition);
            detail.setUpdatedDate(currentDate);

            // 이미지 업데이트 여부 확인
            String updateImages = request.getParameter("updateImages");
            List<ProductImage> images = new ArrayList<>();

            if ("true".equals(updateImages)) {
                // 기존 이미지 삭제 후 새 이미지 등록
                String uploadPath = request.getServletContext().getRealPath(UPLOAD_DIR);

                // 기존 이미지 파일 삭제
                List<ProductImage> oldImages = productService.getProductImages(productId);
                for (ProductImage oldImage : oldImages) {
                    File oldFile = new File(uploadPath, oldImage.getImageName());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                } // 업로드 디렉토리 확인 및 생성
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 새 이미지 업로드
                for (int i = 0; i < 4; i++) {
                    Part filePart = request.getPart("productImage" + i);

                    if (filePart != null && filePart.getSize() > 0) { // 파일명 생성
                        String fileName = UUID.randomUUID().toString() + getExtension(filePart);

                        // 이미지 저장 경로 확인 및 디렉토리 생성
                        File targetDir = new File(uploadPath);
                        if (!targetDir.exists()) {
                            // 모든 상위 디렉토리를 포함하여 생성
                            boolean created = targetDir.mkdirs();
                            System.out.println("디렉토리 생성 결과: " + created + " - 경로: " + targetDir.getAbsolutePath());
                        }

                        // 저장할 파일 경로 생성
                        String filePath = new File(targetDir, fileName).getAbsolutePath();
                        System.out.println("파일 저장 경로: " + filePath);

                        // 파일 저장
                        filePart.write(filePath);

                        ProductImage image = new ProductImage();
                        image.setProductId(productId);
                        image.setImageName(fileName);

                        if (i == 0) {
                            image.setMain(true);
                        } else {
                            image.setMain(false);
                        }

                        images.add(image);
                    }
                }
            }

            int result = productService.updateProduct(product, detail, images, "true".equals(updateImages));
            if (result > 0) {
                return new ModelAndView(
                        request.getContextPath() + "/front?key=admin&methodName=productDetail&productId=" + productId,
                        true);
            } else {
                request.setAttribute("errorMessage", "상품 수정에 실패했습니다.");
                request.setAttribute("product", product);
                request.setAttribute("productDetail", detail);
                // 실패 시 컨트롤러를 통해 돌아가도록 수정
                return new ModelAndView(request.getContextPath()
                        + "/front?key=admin&methodName=productUpdateForm&productId=" + productId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "상품 수정 중 오류가 발생했습니다: " + e.getMessage());
            // 오류 발생 시 컨트롤러를 통해 폼으로 돌아가도록 수정
            return new ModelAndView(request.getContextPath()
                    + "/front?key=admin&methodName=productUpdateForm&productId=" + request.getParameter("productId"),
                    true);
        }
    }

    /**
     * 상품 삭제 처리
     */
    public ModelAndView productDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return null;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            // 이미지 파일 삭제
            String uploadPath = request.getServletContext().getRealPath(UPLOAD_DIR);
            List<ProductImage> images = productService.getProductImages(productId);

            for (ProductImage image : images) {
                File imageFile = new File(uploadPath, image.getImageName());
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }

            // 상품 삭제 서비스 호출
            int result = productService.deleteProduct(productId);
            if (result > 0) {
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
     * 파일 확장자 추출 메소드
     */
    private String getExtension(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");

        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                String filename = element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
                int lastDot = filename.lastIndexOf('.');
                if (lastDot > 0) {
                    return filename.substring(lastDot);
                }
            }
        }
        return "";
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
}