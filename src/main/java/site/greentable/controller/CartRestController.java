package site.greentable.controller;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.CartDTO;
import site.greentable.exception.UnAuthorizedException;
import site.greentable.service.CartService;
import site.greentable.service.CartServiceImpl;

public class CartRestController implements RestController {
	CartService cartService = new CartServiceImpl();
	
	 /**
     * 상품 수량 수정
     * 장바구니 페이지 -> -, + 버튼으로 수량 변경
     * */
    public Map<String, Object> updateCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
        	HttpSession session = request.getSession();
		    Integer userId = (Integer) session.getAttribute("userId");
		    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            resultMap = cartService.updateCart(userId, productId, quantity);
           
        } catch(Exception e) {
        	resultMap.put("success", false);
        	resultMap.put("message", "오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
    
    /**
     * 상품 삭제
     * */
    public Map<String, Object> deleteCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
        	HttpSession session = request.getSession();
		    Integer userId = (Integer) session.getAttribute("userId");
		    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            resultMap = cartService.deleteCart(userId, productId);
            
        }catch(Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "오류 발생: " + e.getMessage());
            e.printStackTrace();
        } 
        return resultMap;   
    }
    
    /**
     * 선택한 상품만 가격 계산
     */
    public Map<String, Object> calculateSelected(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
        	HttpSession session = request.getSession();
		    Integer userId = (Integer) session.getAttribute("userId");
		    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
            
            String productIdsJson = request.getParameter("productIds");
            
            Gson gson = new Gson();
            List<Integer> productIds = gson.fromJson(productIdsJson, new TypeToken<List<Integer>>(){}.getType());
            
            resultMap = cartService.calculateSelectedProducts(userId, productIds);
            resultMap.put("success", true);
            
        } catch(Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultMap;
    }
    
    /**
     * 비회원 장바구니 가격 계산
     * */
    public Map<String, Object> calculateGuestCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 요청 본문에서 JSON 데이터 읽기
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String requestJson = jsonBuilder.toString();

            // JSON 데이터를 Java 객체로 변환
            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(requestJson, 
                    new TypeToken<Map<String, Object>>(){}.getType());
            
            // items 필드에서 장바구니 아이템 추출
            List<Map<String, Object>> itemsList = (List<Map<String, Object>>) requestMap.get("items");

            // CartDTO 리스트로 변환
            List<CartDTO> guestCartItems = new ArrayList<>();
            for (Map<String, Object> item : itemsList) {
                CartDTO cartItem = new CartDTO();
                cartItem.setProductId(((Number)item.get("productId")).intValue());
                cartItem.setQuantity(((Number)item.get("quantity")).intValue());
                guestCartItems.add(cartItem);
            }
            
            // 상품 정보를 조회하여 장바구니 데이터 완성
            List<CartDTO> cartItems = cartService.getGuestCartItems(guestCartItems);
            
            // 서비스 계층에서 가격 계산
            Map<String, Object> priceMap = cartService.calculateGuestCartPrices(cartItems);
            
            resultMap.put("success", true);
            resultMap.put("cartItems", cartItems);
            resultMap.putAll(priceMap);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
    
    /**
     * 비회원 장바구니 이관
     */
    public Map<String, Object> migrateGuestCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
        	HttpSession session = request.getSession();
		    Integer userId = (Integer) session.getAttribute("userId");
		    if (userId == null) throw new UnAuthorizedException("로그인이 필요합니다.");
		    
            String guestCartJson = request.getParameter("guestCart");
            
            Gson gson = new Gson();
            List<Map<String, Object>> guestCartItems = gson.fromJson(guestCartJson, 
                    new TypeToken<List<Map<String, Object>>>(){}.getType());
            
            List<CartDTO> cartItems = new ArrayList<>();
            for (Map<String, Object> item : guestCartItems) {
                int productId = ((Double)item.get("productId")).intValue();
                int quantity = ((Double)item.get("quantity")).intValue();
                
                CartDTO cartDTO = new CartDTO(quantity, productId, userId);
                cartItems.add(cartDTO);
            }
            
            boolean success = cartService.migrateGuestCart(cartItems);
            resultMap.put("success", success);
            
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultMap;
    }
}
