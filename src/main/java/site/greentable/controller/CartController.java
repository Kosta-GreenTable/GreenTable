package site.greentable.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.dto.CartDTO;
import site.greentable.exception.NotFoundException;
import site.greentable.service.CartService;
import site.greentable.service.CartServiceImpl;

public class CartController implements Controller {
	CartService cartService = new CartServiceImpl();
	public CartController() {
		System.out.println("cartController 호출");
	}
	
	/**
	 * 장바구니 목록 조회
	 * */
	public ModelAndView selectCartByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {

			int userId = Integer.parseInt(request.getParameter("userId"));

			Map<String, Object> priceMap = cartService.calculateCartPrices(userId);
	        
	        // 장바구니 목록과 가격 정보 저장
	        request.setAttribute("cartList", priceMap.get("cartList"));
	        request.setAttribute("totalProductPrice", priceMap.get("totalProductPrice"));
	        request.setAttribute("totalDiscount", priceMap.get("totalDiscount"));
	        request.setAttribute("deliveryFee", priceMap.get("deliveryFee"));
	        request.setAttribute("totalPayPrice", priceMap.get("totalPayPrice"));
	        
	        return new ModelAndView("order/cart.jsp");
	}
	
	/**
	 * 장바구니 상품 등록
	 * 같은 상품 담을 시 수량만 수정 (쿼리문에서 처리)
	 * */
	public ModelAndView insertCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			int userId = Integer.parseInt(request.getParameter("userId"));
			int productId = Integer.parseInt(request.getParameter("productId"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			
			// 상품 DB에서 정확한 할인율 다시 조회
//		    ProductDTO product = productService.getProductById(productId);
			
			// 예외 처리
			//if (product == null) throw new NotFoundException("상품 정보를 찾을 수 없습니다.");
			
			CartDTO cart = new CartDTO(quantity, productId, userId);
			//cart.setPrice(product.getPrice());
			//cart.setDiscountRate(product.getDiscountRate()); //할인율 반영
			//cart.setProductName(product.getName());
			//cart.setImageName(product.getMainImageName());
			
			cartService.insertCart(cart);
				
			return new ModelAndView(request.getContextPath() + "/front?key=cart&methodName=selectCartByUserId&userId=" + userId, true);
		}
	
}
