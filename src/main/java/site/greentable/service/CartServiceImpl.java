package site.greentable.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import site.greentable.dao.CartDAO;
import site.greentable.dao.CartDaoImpl;
import site.greentable.dto.CartDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.SelectException;

public class CartServiceImpl implements CartService {
	CartDAO cartDao = new CartDaoImpl();

	@Override
	public List<CartDTO> selectCartByUserId(int userId) throws SQLException, SelectException {
		List<CartDTO> list = cartDao.selectCartByUserId(userId);
		if(list == null) throw new SelectException("장바구니 정보를 가져오는데 실패했습니다.");
		return list;
	}

	@Override
	public int insertCart(CartDTO cartdto) throws SQLException, AddException {
		int result = cartDao.insertCart(cartdto);
		if(result == 0) throw new AddException("장바구니 상품 추가에 실패했습니다.");
		return result;
	}

	@Override
	public Map<String, Object> updateCart(int userId, int productId, int quantity)
			throws SQLException, ModifyException, SelectException {
		Map<String, Object> resultMap = new HashMap<>();
        
        // 수량 업데이트
        CartDTO cart = new CartDTO(quantity, productId, userId);
        int updateResult = cartDao.updateQuantity(cart);
        
        if(updateResult == 0) throw new ModifyException("상품 수량 변경에 실패했습니다.");
        
        // 업데이트 성공 시 가격 정보 계산
        Map<String, Object> priceMap = calculateCartPrices(userId);
        resultMap.putAll(priceMap);
        
        // 업데이트된 개별 상품 가격 계산
        List<CartDTO> cartList = (List<CartDTO>) priceMap.get("cartList");
        int itemTotal = 0;
        
        for(CartDTO item : cartList) {
            if(item.getProductId() == productId) {
                itemTotal = item.getPrice() * item.getQuantity();
                break;
            }
        }
        resultMap.put("itemTotal", itemTotal);
        resultMap.put("success", true);
        
        return resultMap;
	}
	
	/**
     * 장바구니 가격 정보 계산
     * 총 상품금액, 총 할인금액, 배송비, 결제금액 등을 계산하여 반환
     */
	@Override
	public Map<String, Object> calculateCartPrices(int userId) throws SQLException, SelectException {
		Map<String, Object> priceMap = new HashMap<>();
		
		List<CartDTO> cartList = selectCartByUserId(userId);
		
		int totalProductPrice = 0; //총 상품금액
		int totalDiscount = 0;     //총 할인금액
		
	    for (CartDTO item : cartList) {
	    	int price = item.getPrice();
	    	int quantity = item.getQuantity();
	        int itemTotal = price * quantity;
	        
	        totalProductPrice += itemTotal;
	    	// 할인율 적용
	        if(item.getDiscountRate() > 0) {
                int discountAmount = (int)(itemTotal * (item.getDiscountRate() / 100.0));
                totalDiscount += discountAmount;
            }
	    }
	    // 조건부 무료 배송 (5만원 이상 무료, 기본 3500원)
        int deliveryFee = totalProductPrice >= 50000 ? 0 : 3500;
        
        // 결제 금액 (총상품금액 - 총할인금액 + 배송비)
        int totalPayPrice = totalProductPrice - totalDiscount + deliveryFee;
        
        priceMap.put("totalProductPrice", totalProductPrice);
        priceMap.put("totalDiscount", totalDiscount);
        priceMap.put("deliveryFee", deliveryFee);
        priceMap.put("totalPayPrice", totalPayPrice);
        priceMap.put("cartList", cartList);
        
        return priceMap;
	}

	@Override
	public Map<String, Object> deleteCart(int userId, int productId)
			throws SQLException, DeleteException {
		Map<String, Object> resultMap = new HashMap<>();
        
        int deleteResult = cartDao.deleteCart(userId, productId);
        
        if(deleteResult == 0) throw new DeleteException("상품 삭제에 실패했습니다.");

        // 삭제 성공 시 장바구니 정보 조회
        try {
            Map<String, Object> priceMap = calculateCartPrices(userId);
            resultMap.putAll(priceMap);
            resultMap.put("success", true);
            resultMap.put("isEmpty", ((List<?>)priceMap.get("cartList")).isEmpty());
        } catch(SelectException e) {
            // 장바구니가 비어있는 경우
        	resultMap.put("success", true);
            resultMap.put("isEmpty", true);
        }   
        return resultMap;
    }

	@Override
	public Map<String, Object> calculateSelectedProducts(int userId, List<Integer> productIds)
			throws SQLException, SelectException {
		 Map<String, Object> priceMap = new HashMap<>();
	        
	        List<CartDTO> cartItems = selectCartByUserId(userId);
	        
	        // 선택된 상품만 필터링
	        List<CartDTO> selectedItems = cartItems.stream()
	                .filter(item -> productIds.contains(item.getProductId()))
	                .collect(Collectors.toList());
	        
	        int totalProductPrice = 0; // 총 상품금액
	        int totalDiscount = 0;     // 총 할인금액
	        
	        for (CartDTO item : selectedItems) {
	            int price = item.getPrice();
	            int quantity = item.getQuantity();
	            int itemTotal = price * quantity;
	            
	            totalProductPrice += itemTotal;
	            // 할인율 적용
	            if(item.getDiscountRate() > 0) {
	                int discountAmount = (int)(itemTotal * (item.getDiscountRate() / 100.0));
	                totalDiscount += discountAmount;
	            }
	        }
	        
	        // 조건부 무료 배송 (5만원 이상 무료, 기본 3500원)
	        int deliveryFee = totalProductPrice >= 50000 ? 0 : 3500;
	        
	        // 결제 금액 (총상품금액 - 총할인금액 + 배송비)
	        int totalPayPrice = totalProductPrice - totalDiscount + deliveryFee;
	        
	        priceMap.put("totalProductPrice", totalProductPrice);
	        priceMap.put("totalDiscount", totalDiscount);
	        priceMap.put("deliveryFee", deliveryFee);
	        priceMap.put("totalPayPrice", totalPayPrice);
	        
	        return priceMap;
	    }

	@Override
	public boolean migrateGuestCart(List<CartDTO> cartItems) throws SQLException, AddException {
		// TODO Auto-generated method stub
		return false;
	}
	

}
