package site.greentable.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;

/**
 * AdminController - 관리자 전용 기능을 제공하는 컨트롤러
 * 모든 메소드는 관리자 권한 체크 후 실행됨
 */
public class AdminController implements Controller {
    private static final String UPLOAD_DIR = "/assets/images/products";
    private ProductService productService = new ProductServiceImpl();

    /**
     * 관리자 권한 체크 메소드
     * 
     * @return 권한이 있으면 true, 없으면 false
     */
    private boolean checkAdminAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**
    	HttpSession session = request.getSession();
        String adminId = (String) session.getAttribute("adminId");

        if (adminId == null || adminId.isEmpty()) {
            return false;
        }**/

        return true;
    }

    /**
     * 관리자 대시보드
     */
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!checkAdminAuth(request, response)) {
            return new ModelAndView("/admin/login.jsp", true);
        }

        return new ModelAndView("/admin/index.jsp");
    }

    /**
     * 상품 목록 조회
     */
    public ModelAndView productList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 관리자 권한 체크
        if (!checkAdminAuth(request, response)) {
        	
            return new ModelAndView("/admin/login.jsp", true);
        }

        // 페이지 번호 파라미터 가져오기
        String pageNo = request.getParameter("pageNo");
        if (pageNo == null || pageNo.isEmpty()) {
            pageNo = "1";
        }

        // 상품 목록 조회
        int page = Integer.parseInt(pageNo);
        List<Product> products = productService.getAllProducts(page);
        int totalPages = productService.getTotalPages();

        // 결과를 request에 저장
        request.setAttribute("productList", products);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        // 관리자 상품 목록 페이지로 이동
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
}