package site.greentable.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.exception.NotFoundException;
import site.greentable.service.OrderService;
import site.greentable.service.OrderServiceImpl;

public class OrderController implements Controller {
	OrderService orderService = new OrderServiceImpl();
	
	/**
	 * 결제 성공 후 데이터 저장
	 * */
	public ModelAndView completeOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // 세션에서 주문 상품 정보 가져오기
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
        
        // 결제 정보
        orderData.put("paymentMethod", request.getParameter("paymentMethod"));
        orderData.put("totalAmount", Integer.parseInt(request.getParameter("totalAmount")));
        orderData.put("paymentStatus", "결제 성공");
        
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
        

        try {
            // 2. 서비스 호출하여 트랜잭션 처리
            boolean result = orderService.processOrder(orderItems, orderData);
            
            if (result) {
                // 성공 시 세션에서 주문 정보 제거
                session.removeAttribute("orderItems");
                return new ModelAndView("order/confirmation.jsp", true);
            } else {
                throw new Exception("주문 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            return new ModelAndView("error/error.jsp", false);
        }
	}
}
