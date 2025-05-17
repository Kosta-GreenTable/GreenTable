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
            request.setAttribute("errorMessage", "상품 정보를 찾을 수 없습니다.");
            return new ModelAndView("/shopping/error.jsp");
        }
        request.setAttribute("product", product);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("productImages", productImages);

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
}
