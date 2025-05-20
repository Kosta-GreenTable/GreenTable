package site.greentable.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.dto.UserDTO;
import site.greentable.exception.NotFoundException;
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
            // 선택 상품 주문 (회원 or 비회원 모두)
        	String rawIds  = request.getParameter("productIds");
            String rawQtys = request.getParameter("quantities");
            
//            System.out.println("goOrder productIds raw = " + rawIds);
//            System.out.println("goOrder quantities raw = " + rawQtys);
            String[] productIds  = rawIds .split("\\s*,\\s*");
            String[] quantities  = rawQtys.split("\\s*,\\s*");

            for (int i = 0; i < productIds.length; i++) {
                int pid = Integer.parseInt(productIds[i].trim());
                int qty = Integer.parseInt(quantities[i].trim());

//                System.out.println("goOrder loop["+ i +"] pid=" + pid + ", qty=" + qty);

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
            // 전체 상품 주문
            if (userId != 0) {
                // 회원의 경우 DB에서 userId로 cart 전체 조회
                orderList = cartService.selectCartByUserId(userId);
            } else {
                // 비회원의 경우 localStorage 데이터를 통해 구성 → 전달받은 파라미터 이용
                String[] productIds = request.getParameter("productIds").split(",");
                String[] quantities = request.getParameter("quantities").split(",");

                for (int i = 0; i < productIds.length; i++) {
                    CartDTO cart = new CartDTO();
                    cart.setProductId(Integer.parseInt(productIds[i]));
                    cart.setQuantity(Integer.parseInt(quantities[i]));

                    CartDTO fullInfo = orderService.getProductDetail(cart.getProductId());
                    cart.setProductName(fullInfo.getProductName());
                    cart.setPrice(fullInfo.getPrice());
                    cart.setDiscountRate(fullInfo.getDiscountRate());
                    cart.setImageName(fullInfo.getImageName());

                    orderList.add(cart);
                }
            }
        }
        // 계산된 OrderList를 세션에 저장
        session.setAttribute("orderItems", orderList);
        
        Map<String, Object> priceMap;
        if (userId != 0) { // 회원
            if ("true".equals(selected)) {
                List<Integer> selectedIds = orderList.stream()
                    .map(CartDTO::getProductId)
                    .collect(Collectors.toList());
                priceMap = cartService.calculateSelectedProducts(userId, selectedIds);
            } else {
                priceMap = cartService.calculateCartPrices(userId);
            }
        } else { // 비회원
            priceMap = cartService.calculateGuestCartPrices(orderList);
        }
        
        // 계산 결과 JSP에 넘기기
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
	
	/** merchantUid 생성 함수 */
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

        // 1. 주문 정보 수집
        Map<String, Object> orderData = new HashMap<>();
        
        // 주문자 정보 가져오기
        orderData.put("userId", userId);
        orderData.put("name", request.getParameter("name"));
        
        String email1 = request.getParameter("email1");
        String email2 = request.getParameter("email2");
        orderData.put("email", email1 + "@" + email2);

        String phonePrefix = request.getParameter("phonePrefix");
        String phone1 = request.getParameter("phone1");
        String phone2 = request.getParameter("phone2");
        orderData.put("phone", phonePrefix + "-" + phone1 + "-" + phone2);
        
        // 배송지 정보
        orderData.put("recipient", request.getParameter("recipient"));
        orderData.put("zipCode", request.getParameter("zipCode"));
        orderData.put("address", request.getParameter("address"));
        orderData.put("addressDetail", request.getParameter("addressDetail"));

        String recipientPhonePrefix = request.getParameter("recipientPhonePrefix");
        String recipientPhone1 = request.getParameter("recipientPhone1");
        String recipientPhone2 = request.getParameter("recipientPhone2");
        orderData.put("recipientPhone", recipientPhonePrefix + "-" + recipientPhone1 + "-" + recipientPhone2);
        
        // 적립금 처리
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
        
        // 결제 정보
        orderData.put("merchantUid", request.getParameter("merchantUid"));
        orderData.put("impUid", request.getParameter("impUid"));
        orderData.put("paymentMethod", request.getParameter("paymentMethod"));
        orderData.put("totalAmount", Integer.parseInt(request.getParameter("totalAmount")));
        orderData.put("paymentStatus", request.getParameter("paymentStatus"));
        
        // 비회원 처리
        if (userId == null) {
            String password = request.getParameter("password");
            String passwordConfirm = request.getParameter("passwordConfirm");          
            //비밀번호 확인로직 js에서도 처리
            if (!password.equals(passwordConfirm)) { 
                throw new Exception("주문 비밀번호가 일치하지 않습니다.");
            }
            orderData.put("guestPassword", password);
        }
        System.out.println("Order컨트롤러 orderData - " + orderData);

        try {
            // 2. 서비스 호출하여 트랜잭션 처리
            boolean result = orderService.processOrder(orderItems, orderData);
            
            if (result) {
                // 성공 시 세션에서 주문 정보 제거
                session.removeAttribute("orderItems");
                return new ModelAndView("order/orderSuccess.jsp", true);
            } else {
                throw new Exception("주문 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("error/error.jsp", false);
        }
	}// completeOrder 메소드 끝
}
