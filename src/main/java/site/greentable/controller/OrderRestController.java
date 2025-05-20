package site.greentable.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.exception.UnAuthorizedException;
import site.greentable.util.Env;

public class OrderRestController implements RestController {
	
	/** 주문할 상품 정보 세션에 저장 */
	public Map<String, Object> processOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> resultMap = new HashMap<>();
	    HttpSession session = request.getSession();
	    Integer userId = (Integer) session.getAttribute("userId");
	    
	    try {
	    	// 가격 정보를 세션에 저장
	    	int totalProductPrice = Integer.parseInt(request.getParameter("totalProductPrice"));
	        int totalDiscount = Integer.parseInt(request.getParameter("totalDiscount"));
	        int deliveryFee = Integer.parseInt(request.getParameter("deliveryFee"));
	        int totalPayPrice = Integer.parseInt(request.getParameter("totalPayPrice"));
	        
	        session.setAttribute("totalProductPrice", totalProductPrice);
	        session.setAttribute("totalDiscount", totalDiscount);
	        session.setAttribute("deliveryFee", deliveryFee);
	        session.setAttribute("totalPayPrice", totalPayPrice);
	    	 
	        if (userId == null || userId == 0) {
	            // 비회원 주문 데이터 세션에 저장
	            saveGuestOrder(request, session);
	        } else {
	            // 회원 주문 데이터 세션에 저장
	            saveMemberOrder(request, session, userId);
	        }

	        // merchantUid 생성 및 세션에 저장
	        String merchantUid = generateMerchantUid();
	        session.setAttribute("merchantUid", merchantUid);

	        // 클라이언트에 전달 (order.jsp에서 사용 가능하도록)
	        resultMap.put("merchantUid", merchantUid);
	        
	        resultMap.put("success", true);
	        resultMap.put("redirectUrl",request.getContextPath() + "/order/order.jsp");
	        resultMap.put("message", "주문 페이지로 이동합니다.");
	    } catch (Exception e) {
	        resultMap.put("success", false);
	        resultMap.put("message", "주문 데이터 처리 중 오류 발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resultMap;
	}

	
	/** 비회원 주문 데이터 세션 저장 */
	private void saveGuestOrder(HttpServletRequest request, HttpSession session) throws Exception {
	    String guestProductsJson = request.getParameter("guestProducts");
	    Gson gson = new Gson();
	    List<CartDTO> guestCart = gson.fromJson(guestProductsJson, new TypeToken<List<CartDTO>>() {}.getType());

	    // 세션에 저장
	    session.setAttribute("orderItems", guestCart);
	}
	
	/** 회원 주문 데이터 세션 저장 */
	private void saveMemberOrder(HttpServletRequest request, HttpSession session, Integer userId) throws Exception {

	    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
	    
	    String productIdsJson = request.getParameter("productIds");
	    String quantitiesJson = request.getParameter("quantity");

	    Gson gson = new Gson();
	    List<Integer> productIds = gson.fromJson(productIdsJson, new TypeToken<List<Integer>>() {}.getType());
	    List<Integer> quantities = gson.fromJson(quantitiesJson, new TypeToken<List<Integer>>() {}.getType());

	    // DB에서 회원 장바구니 데이터 가져오기
	    List<CartDTO> orderItems = new ArrayList<>();
	    for (int i = 0; i < productIds.size(); i++) {
	        orderItems.add(new CartDTO(quantities.get(i), productIds.get(i), userId));
	    }

	    // 세션에 저장
	    session.setAttribute("orderItems", orderItems);
	}
	///////////////////////////////
	/** 주문 번호 랜덤 생성 */
	private String generateMerchantUid() {
	    return "ord_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);  // 타임스탬프 + 랜덤 문자열 8자리
	}
	
	/**
     * 클라이언트에게 결제 초기화 정보 제공
     */
    public Map<String, Object> getPaymentInitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        // 어플리케이션에 저장된 merchantUid 가져오기
        String merchantUid = (String) request.getSession().getAttribute("merchantUid");
        
        // 클라이언트에게 필요한 정보만 반환
        resultMap.put("merchantUid", merchantUid);
        resultMap.put("success", true);
        
        return resultMap;
    }
    
    /**
     * 결제 검증 (서버에서 API 키 사용)
     */
    public Map<String, Object> verifyPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        // 클라이언트에서 전송된 결제 정보
        String impUid = request.getParameter("impUid");
        String merchantUid = request.getParameter("merchantUid");
        
        // 세션에서 주문 정보 조회
        HttpSession session = request.getSession();
        Object payPriceObj = session.getAttribute("totalPayPrice");
        //int totalPayPrice = (Integer) request.getSession().getAttribute("totalPayPrice");
        if (payPriceObj == null) throw new IllegalStateException("세션에 결제 금액이 없습니다.");
        int totalPayPrice = (Integer) payPriceObj;
       
        // 1. 결제 금액 검증 - 세션의 주문금액과 결제금액 비교
        int paidAmount = Integer.parseInt(request.getParameter("amount"));
        if (paidAmount != totalPayPrice) {
            resultMap.put("success", false);
            resultMap.put("message", "결제 금액이 일치하지 않습니다.");
            return resultMap;
        }
        
        // 2. 포트원 API 결제 검증
        String apiKey = Env.pr.getProperty("portone.apiKey");
        String apiSecret = Env.pr.getProperty("portone.apiSecret");
        if (apiKey == null || apiSecret == null) {
            throw new IllegalStateException("포트원 API 키 설정이 누락되었습니다.");
        }
            
        if (impUid == null || impUid.isBlank() || merchantUid == null || merchantUid.isBlank()) {
        	resultMap.put("success", false);
            resultMap.put("message", "결제 정보가 올바르지 않습니다.");
        } else {
        	resultMap.put("success", true);
            resultMap.put("message", "결제가 검증되었습니다");
        }
        return resultMap;
    }
	
	
}
