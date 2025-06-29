package site.greentable.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;

/**
 * ProductController - 일반 사용자용 상품 관련 컨트롤러
 * 관리자 기능은 AdminController로 이동됨
 */
public class ProductController implements Controller {
    private ProductService productService = new ProductServiceImpl();

    /**
     * 상품 목록 조회
     */
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageNo = request.getParameter("pageNo");

        if (pageNo == null || pageNo.isEmpty()) {
            pageNo = "1";
        }

        List<Product> list = productService.getAllProducts(Integer.parseInt(pageNo));
        int totalPages = productService.getTotalPages();

        request.setAttribute("productList", list);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        return new ModelAndView("/products/product-list.jsp");
    }

    /**
     * 카테고리별 상품 목록 조회
     */
    public ModelAndView category(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String category = request.getParameter("category");
        String pageNo = request.getParameter("pageNo");

        if (pageNo == null || pageNo.isEmpty()) {
            pageNo = "1";
        }

        int pageNoInt = Integer.parseInt(pageNo);
        List<Product> list;
        int totalPages;

        // 카테고리에 따라 다른 메서드 호출
        switch (category.toLowerCase()) {
            case "best":
                list = productService.getBestProducts(pageNoInt);
                totalPages = productService.getBestTotalPages();
                break;
            case "regular":
                list = productService.getRegularProducts(pageNoInt);
                totalPages = productService.getRegularTotalPages();
                break;
            case "lunchbox":
                list = productService.getLunchboxProducts(pageNoInt);
                totalPages = productService.getLunchboxTotalPages();
                break;
            case "salad":
                list = productService.getSaladProducts(pageNoInt);
                totalPages = productService.getSaladTotalPages();
                break;
            default:
                list = productService.getProductsByCategory(category, pageNoInt);
                totalPages = productService.getCategoryTotalPages(category);
        }

        request.setAttribute("productList", list);
        request.setAttribute("category", category);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        // 카테고리에 따라 다른 JSP 페이지로 이동
        String viewPath;
        switch (category.toLowerCase()) {
            case "best":
                viewPath = "/best/best.jsp";
                break;
            case "regular":
                viewPath = "/regular/regular.jsp";
                break;
            case "lunchbox":
                viewPath = "/lunchbox/lunchbox.jsp";
                break;
            case "salad":
                viewPath = "/salad/salad.jsp";
                break;
            default:
                viewPath = "/products/products.jsp";
        }

        return new ModelAndView(viewPath);
    }

    /**
     * 상품 재고 확인 (AJAX 요청용)
     */
    public void checkStock(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String productIdStr = request.getParameter("productId");

        response.setContentType("application/json;charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();

        try {
            if (productIdStr == null || productIdStr.isEmpty()) {
                out.print("{\"success\": false, \"stock\": 0, \"message\": \"상품 ID가 없습니다.\"}");
                return;
            }

            int productId = Integer.parseInt(productIdStr);
            Product product = productService.getProductDetail(productId);

            if (product == null) {
                out.print("{\"success\": false, \"stock\": 0, \"message\": \"상품 정보를 찾을 수 없습니다.\"}");
            } else {
                out.print("{\"success\": true, \"stock\": " + product.getStock() + "}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"stock\": 0, \"message\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    /**
     * 상품 상세 정보 조회
     */
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String productIdStr = request.getParameter("productId");

        if (productIdStr == null || productIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "상품 정보를 찾을 수 없습니다.");
            return new ModelAndView("/shopping/error.jsp");
        }

        int productId = Integer.parseInt(productIdStr);
        Product product = productService.getProductDetail(productId);
        ProductDetail productDetail = productService.getProductDetailInfo(productId);
        List<ProductImage> productImages = productService.getProductImages(productId);

        if (product == null) {
            System.out.println("상품디테일오류");
            System.out.println(product);
            request.setAttribute("errorMessage", "상품 정보를 찾을 수 없습니다.");
            return new ModelAndView("/shopping/error.jsp");
        }
        request.setAttribute("product", product);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("productImages", productImages);

        // 추천 상품 가져오기 (4개까지 제한)
        List<Product> recommendedProducts = productService.getRecommendedProducts(productId, 4);
        request.setAttribute("recommendedProducts", recommendedProducts);

        return new ModelAndView("/products/product-detail.jsp");

    }

    /**
     * 메인 페이지용 카테고리 컨텐츠 로드
     */
    public ModelAndView categoryContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String category = request.getParameter("category");

        if (category == null || category.isEmpty()) {
            request.setAttribute("errorMessage", "카테고리 정보가 없습니다.");
            return new ModelAndView("/error.jsp");
        }

        List<Product> list;

        // 카테고리에 따라 다른 메서드 호출 (첫 페이지만)
        switch (category.toLowerCase()) {
            case "best":
                list = productService.getBestProducts(1);
                break;
            case "regular":
                list = productService.getRegularProducts(1);
                break;
            case "lunchbox":
                list = productService.getLunchboxProducts(1);
                break;
            case "salad":
                list = productService.getSaladProducts(1);
                break;
            default:
                list = productService.getProductsByCategory(category, 1);
        }

        // 최대 4개만 표시
        if (list.size() > 4) {
            list = list.subList(0, 4);
        }

        request.setAttribute("productList", list);
        request.setAttribute("category", category);

        String viewPath = "/" + category + "/" + category + ".jsp";

        return new ModelAndView(viewPath);
    }

    /**
     * 검색 기능 - 검색 결과 페이지로 이동
     */
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String query = request.getParameter("query");
        String pageNo = request.getParameter("pageNo");
        String sortOption = request.getParameter("sort");

        if (pageNo == null || pageNo.isEmpty()) {
            pageNo = "1";
        }

        if (query == null || query.isEmpty()) {
            return new ModelAndView("/index.jsp");
        }

        int pageNoInt = Integer.parseInt(pageNo);
        List<Product> list = productService.searchProducts(query, pageNoInt);
        int totalPages = productService.getSearchTotalPages(query);

        // 정렬 옵션 처리
        if (sortOption != null && !sortOption.isEmpty()) {
            switch (sortOption) {
                case "newest":
                    list = productService.sortProductsByNewest(list);
                    break;
                case "price-asc":
                    list = productService.sortProductsByPriceAsc(list);
                    break;
                case "price-desc":
                    list = productService.sortProductsByPriceDesc(list);
                    break;
                case "discount":
                    list = productService.sortProductsByDiscount(list);
                    break;
                default:
                    // 기본 정렬(관련도순)은 그대로 유지
                    break;
            }
        }

        // 검색 결과가 없을 경우 추천 상품 제공
        if (list.isEmpty()) {
            List<Product> recommendedProducts = productService.getBestProducts(1);
            // 최대 8개까지 제한
            if (recommendedProducts.size() > 8) {
                recommendedProducts = recommendedProducts.subList(0, 8);
            }
            request.setAttribute("recommendedProducts", recommendedProducts);
        }

        request.setAttribute("productList", list);
        request.setAttribute("query", query);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("category", "검색 결과: " + query);
        request.setAttribute("sortOption", sortOption);

        return new ModelAndView("/products/search-results.jsp");
    }

    /**
     * AJAX 검색 기능 - 실시간 검색 결과 JSON 반환
     */
    public void searchAjax(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String query = request.getParameter("query");

        response.setContentType("application/json;charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();

        try {
            if (query == null || query.isEmpty()) {
                out.print("[]");
                return;
            }

            // 최대 5개의 검색 결과만 반환
            List<Product> products = productService.searchProducts(query, 1);
            if (products.size() > 5) {
                products = products.subList(0, 5);
            }

            // JSON 형식으로 변환
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                json.append("{");
                json.append("\"productId\":").append(product.getProductId()).append(",");
                json.append("\"name\":\"").append(escapeJson(product.getName())).append("\",");
                json.append("\"category\":\"").append(escapeJson(product.getCategory())).append("\",");
                json.append("\"price\":").append(product.getPrice()).append(",");
                json.append("\"mainImageName\":\"").append(escapeJson(product.getMainImageName())).append("\"");
                json.append("}");
                if (i < products.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            out.print(json.toString());
        } catch (Exception e) {
            out.print("[]");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }

    // JSON 문자열 이스케이프 처리
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
