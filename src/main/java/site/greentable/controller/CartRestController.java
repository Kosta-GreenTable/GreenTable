package site.greentable.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.dto.CartDTO;
import site.greentable.service.CartService;
import site.greentable.service.CartServiceImpl;

public class CartRestController implements RestController {
	CartService cartService = new CartServiceImpl();
	
	 /**
     * 상품 수량 수정
     * 장바구니 페이지 -> -, + 버튼으로 수량 변경
     * */
    public Map<String, Object> updateQuantity(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
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
            int userId = Integer.parseInt(request.getParameter("userId"));
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
            int userId = Integer.parseInt(request.getParameter("userId"));
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
     * 비회원 장바구니 이관
     */
    public Map<String, Object> migrateGuestCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
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
