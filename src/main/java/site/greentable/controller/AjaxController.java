package site.greentable.controller;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.dto.Product;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;

/**
 * Ajax 호출을 처리하는 컨트롤러
 */
public class AjaxController implements RestController {

    private ProductService productService = new ProductServiceImpl();

    /**
     * 상품 재고를 확인하는 메소드
     */
    public void checkStock(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 상품 ID 파라미터 가져오기
        String productIdParam = request.getParameter("productId");
        int productId = Integer.parseInt(productIdParam);

        // 상품 정보 조회
        Product product = productService.getProductDetail(productId);

        // JSON 형식으로 응답 생성
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 상품이 없거나 재고가 없는 경우
        if (product == null) {
            out.print("{\"success\": false, \"stock\": 0, \"message\": \"상품 정보를 찾을 수 없습니다.\"}");
        } else {
            out.print("{\"success\": true, \"stock\": " + product.getStock() + "}");
        }

        out.flush();
    }
}
