package site.greentable.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.UserDTO;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.UnAuthorizedException;
import site.greentable.service.CartService;
import site.greentable.service.CartServiceImpl;
import site.greentable.service.OrderService;
import site.greentable.service.OrderServiceImpl;
import site.greentable.service.ProductService;
import site.greentable.service.ProductServiceImpl;

public class OrderController implements Controller {
	private OrderService orderService = new OrderServiceImpl();
	private CartService cartService = new CartServiceImpl();
	
	/** 장바구니 -> 주문 페이지 이동 */
	public ModelAndView goOrderPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
	    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
	    int userId = loginUser != null ? loginUser.getUserId() : 0;

	    String selected = request.getParameter("selected");
	    List<CartDTO> orderList = new ArrayList<>();

	    if ("true".equals(selected)) {
	        // 선택 상품 주문 처리
	        String rawIds = request.getParameter("productIds");
	        String rawQtys = request.getParameter("quantities");

	        if (rawIds != null && rawQtys != null) {
	            String[] productIds = rawIds.split("\\s*,\\s*");
	            String[] quantities = rawQtys.split("\\s*,\\s*");

	            for (int i = 0; i < productIds.length; i++) {
	                int pid = Integer.parseInt(productIds[i].trim());
	                int qty = Integer.parseInt(quantities[i].trim());

	                CartDTO fullInfo = orderService.getProductDetail(pid);
	                if (fullInfo == null) {
	                    throw new NotFoundException("상품 ID " + pid + " 가 존재하지 않습니다.");
	                }

	                CartDTO cart = new CartDTO();
	                cart.setProductId(pid);
	                cart.setQuantity(qty);
	                cart.setProductName(fullInfo.getProductName());
	                cart.setPrice(fullInfo.getPrice());
	                cart.setDiscountRate(fullInfo.getDiscountRate());
	                cart.setImageName(fullInfo.getImageName());

	                orderList.add(cart);
	            }
	        } else {
	            // 비회원의 경우 guestProducts 처리
	            String guestProductsJson = request.getParameter("guestProducts");
	            if (guestProductsJson != null) {
	                Gson gson = new Gson();
	                List<CartDTO> guestCart = gson.fromJson(guestProductsJson, new TypeToken<List<CartDTO>>() {}.getType());

	                for (CartDTO item : guestCart) {
	                    CartDTO fullInfo = orderService.getProductDetail(item.getProductId());
	                    item.setProductName(fullInfo.getProductName());
	                    item.setPrice(fullInfo.getPrice());
	                    item.setDiscountRate(fullInfo.getDiscountRate());
	                    item.setImageName(fullInfo.getImageName());
	                    orderList.add(item);
	                }
	            }
	        }
	    } else {
	        // 전체 상품 주문 처리
	        if (userId != 0) {
	            orderList = cartService.selectCartByUserId(userId);
	        } else {
	            String guestProductsJson = request.getParameter("guestProducts");
	            if (guestProductsJson != null) {
	                Gson gson = new Gson();
	                List<CartDTO> guestCart = gson.fromJson(guestProductsJson, new TypeToken<List<CartDTO>>() {}.getType());

	                for (CartDTO item : guestCart) {
	                    CartDTO fullInfo = orderService.getProductDetail(item.getProductId());
	                    item.setProductName(fullInfo.getProductName());
	                    item.setPrice(fullInfo.getPrice());
	                    item.setDiscountRate(fullInfo.getDiscountRate());
	                    item.setImageName(fullInfo.getImageName());
	                    orderList.add(item);
	                }
	            }
	        }
	    }

	    // 계산된 OrderList를 세션에 저장
	    session.setAttribute("orderItems", orderList);

	    Map<String, Object> priceMap;
	    if (userId != 0) {
	        priceMap = cartService.calculateCartPrices(userId);
	    } else {
	        priceMap = cartService.calculateGuestCartPrices(orderList);
	    }

	    session.setAttribute("totalProductPrice", priceMap.get("totalProductPrice"));
	    session.setAttribute("totalDiscount", priceMap.get("totalDiscount"));
	    session.setAttribute("deliveryFee", priceMap.get("deliveryFee"));
	    session.setAttribute("totalPayPrice", priceMap.get("totalPayPrice"));

	    request.setAttribute("orderList", orderList);
	    request.setAttribute("totalProductPrice", priceMap.get("totalProductPrice"));
	    request.setAttribute("totalDiscount", priceMap.get("totalDiscount"));
	    request.setAttribute("deliveryFee", priceMap.get("deliveryFee"));
	    request.setAttribute("totalPayPrice", priceMap.get("totalPayPrice"));

	    String merchantUid = generateMerchantUid();
	    session.setAttribute("merchantUid", merchantUid);
	    request.setAttribute("merchantUid", merchantUid);

	    return new ModelAndView("/order/order.jsp");
    }
	
	
	/** merchantUid(고유 주문번호) 생성 함수 */
	private String generateMerchantUid() {
	    return "ord_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);  // 타임스탬프 + 랜덤 문자열 8자리
	}
	
	/**
	 * 결제 성공 후 데이터 저장
	 * */
	public ModelAndView completeOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // 세션에서 주문 상품 정보 가져오기
        @SuppressWarnings("unchecked")
        List<CartDTO> orderItems = (List<CartDTO>) session.getAttribute("orderItems");
        if (orderItems == null || orderItems.isEmpty()) {
            throw new NotFoundException("주문할 상품이 없습니다.");
        }

        // 1. 주문 정보 수집용 Map 선언
        Map<String, Object> orderData = new HashMap<>();
        
        // 2. 주문자 정보 가져오기
        orderData.put("userId", userId);
        orderData.put("name", request.getParameter("name"));
        
        String email1 = request.getParameter("email1");
        String email2 = request.getParameter("email2");
        orderData.put("email", email1 + "@" + email2);

        String phonePrefix = request.getParameter("phonePrefix");
        String phone1 = request.getParameter("phone1");
        String phone2 = request.getParameter("phone2");
        orderData.put("phone", phonePrefix + "-" + phone1 + "-" + phone2);
        
        // 3. 배송지 정보 가져오기
        orderData.put("recipient", request.getParameter("recipient"));
        orderData.put("zipCode", request.getParameter("zipCode"));
        orderData.put("address", request.getParameter("address"));
        orderData.put("addressDetail", request.getParameter("addressDetail"));

        String recipientPhonePrefix = request.getParameter("recipientPhonePrefix");
        String recipientPhone1 = request.getParameter("recipientPhone1");
        String recipientPhone2 = request.getParameter("recipientPhone2");
        orderData.put("recipientPhone", recipientPhonePrefix + "-" + recipientPhone1 + "-" + recipientPhone2);
        
        // 4. 적립금 처리
        int usedPoint = 0;
        String pointParam = request.getParameter("point");
        if (pointParam != null && !pointParam.isEmpty()) {
            usedPoint = Integer.parseInt(pointParam);
            orderData.put("usedPoint", usedPoint);
        }
        
        System.out.println("결제 성공후 컨트롤러 - merchantUid : " + request.getParameter("merchantUid"));
        System.out.println("결제 성공후 컨트롤러 - impUid : " + request.getParameter("impUid"));
        System.out.println("결제 성공후 컨트롤러 - totalAmount : " + request.getParameter("totalAmount"));
        System.out.println("결제 성공후 컨트롤러 - paymentMethod : " + request.getParameter("paymentMethod"));
        System.out.println("결제 성공후 컨트롤러 - paymentStatus : " + request.getParameter("paymentStatus"));
        
        // 5. 결제 정보 넣기
        orderData.put("merchantUid", request.getParameter("merchantUid"));
        orderData.put("impUid", request.getParameter("impUid"));
        orderData.put("paymentMethod", request.getParameter("paymentMethod"));
        orderData.put("totalAmount", Integer.parseInt(request.getParameter("totalAmount")));
        orderData.put("paymentStatus", request.getParameter("paymentStatus"));
        
        // 6. 비회원 - 주문 비밀번호 검증
        if (userId == null) {
            String password = request.getParameter("password");
            String passwordConfirm = request.getParameter("passwordConfirm");          
            //비밀번호 확인 로직 (js에서도 이중 검증)
            if (!password.equals(passwordConfirm)) { 
                throw new Exception("주문 비밀번호가 일치하지 않습니다.");
            }
            orderData.put("guestPassword", password);
        }
        System.out.println("Order컨트롤러 orderData - " + orderData);

        try {
            // 7. 서비스 호출하여 트랜잭션 처리
            boolean result = orderService.processOrder(orderItems, orderData);
            
            if (result) {
                // 8. 성공 시 세션에서 주문 정보 제거 -> 9. 장바구니 데이터 삭제 (추후구현)
                session.removeAttribute("orderItems");
                request.setAttribute("merchantUid", request.getParameter("merchantUid"));
                return new ModelAndView("order/orderSuccess.jsp");
            } else {
                throw new Exception("주문 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("/error/500.jsp", false);
        }
	}// completeOrder 메소드 끝
	
	/** 비회원 주문 조회 */
	public ModelAndView getGuestOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String merchantUid = request.getParameter("merchantUid");
        String guestPassword = request.getParameter("guestPassword");

        try {
	        OrderDTO order = orderService.getGuestOrder(merchantUid, guestPassword);
	
	        request.getSession().setAttribute("order", order);
	        request.setAttribute("order", order);
	        return new ModelAndView("/order/guestOrderDetail.jsp");
        } catch (NotFoundException e) {
            // 검증 실패 시 에러 메시지 반환 (json으로 메세지 보내서 비동기 처리)
        	request.setAttribute("errorMessage", e.getMessage());
        	return new ModelAndView("/user/login.jsp", false);
        }
    }
	
	
	
}
