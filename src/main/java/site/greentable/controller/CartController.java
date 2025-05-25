package site.greentable.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.dto.Product;
import site.greentable.dto.UserDTO;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.UnAuthorizedException;
import site.greentable.service.CartService;
import site.greentable.service.CartServiceImpl;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;

public class CartController implements Controller {
	private CartService cartService = new CartServiceImpl();
	private ProductService productService = new ProductServiceImpl();
	
	/**
	 * 장바구니 목록 조회
	 * */
	public ModelAndView selectCartByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
			HttpSession session = request.getSession();
//		    Integer userId = (Integer) session.getAttribute("userId");
			UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
//		    if (loginUser == null) {
//		        throw new UnAuthorizedException("로그인이 필요합니다.");
//		    }
//		    int userId = loginUser.getUserId();
		    
			if (loginUser != null) {
			int userId = loginUser.getUserId();
			Map<String, Object> priceMap = cartService.calculateCartPrices(userId);
	        
	        // 장바구니 목록과 가격 정보 저장
	        request.setAttribute("cartList", priceMap.get("cartList"));
	        request.setAttribute("totalProductPrice", priceMap.get("totalProductPrice"));
	        request.setAttribute("totalDiscount", priceMap.get("totalDiscount"));
	        request.setAttribute("deliveryFee", priceMap.get("deliveryFee"));
	        request.setAttribute("totalPayPrice", priceMap.get("totalPayPrice"));
		    }
	        return new ModelAndView("order/cart.jsp");
	}
	
	/**
	 * 장바구니 상품 등록
	 * 같은 상품 담을 시 수량만 수정 (쿼리문에서 처리)
	 * */
	public ModelAndView insertCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			HttpSession session = request.getSession();
		    Integer userId = (Integer) session.getAttribute("userId");
		    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
			System.out.println("inserCart;" + request.getParameter("userId"));
			int productId = Integer.parseInt(request.getParameter("productId"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			
			// 상품 DB에서 정확한 할인율 다시 조회
		    Product product = productService.getProductDetail(productId);
			
			if (product == null) throw new NotFoundException("상품 정보를 찾을 수 없습니다.");
			
			CartDTO cart = new CartDTO(quantity, productId, userId);
			cart.setPrice(product.getPrice());
			cart.setDiscountRate(product.getDiscountRate()); //할인율 반영
			cart.setProductName(product.getName());
			cart.setImageName(product.getMainImageName());
			
			cartService.insertCart(cart);
			
			// AJAX 요청이라면 JSON 으로 성공 여부 내보내기
			  if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			    response.setContentType("application/json;charset=UTF-8");
			    response.getWriter().write("{\"success\":true}");
			    return null;  
			  }
				
			return new ModelAndView(request.getContextPath() + "/front?key=cart&methodName=selectCartByUserId&userId=" + userId, true);
		}
	
}
