package site.greentable.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import site.greentable.dto.CartDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.SelectException;

public interface CartService {
	
	/** 장바구니 목록 조회 */
	List<CartDTO> selectCartByUserId(int userId) throws SQLException, SelectException;
	
	/**
	 * 장바구니에 상품 등록(추가)
	 * | 같은 상품 담을 시 수량만 변경 (쿼리문에서 처리)
	 * */
	int insertCart(CartDTO cartdto) throws SQLException, AddException;
			
	/** 장바구니 수량 업데이트 및 가격 재계산 */
    Map<String, Object> updateCart(int userId, int productId, int quantity) 
        throws SQLException, ModifyException, SelectException;

	/**
     * 회원의 장바구니 가격 정보 계산
     * 총 상품금액, 총 할인금액, 배송비, 결제금액 등을 계산하여 반환
     * */
    Map<String, Object> calculateCartPrices(int userId) throws SQLException, SelectException;
    
    /** 회원 선택 상품 가격 계산 */
    Map<String, Object> calculateSelectedProducts(int userId, List<Integer> productIds) 
        throws SQLException, SelectException;
    
    /** 장바구니 상품 삭제 및 가격 재계산 */
    Map<String, Object> deleteCart(int userId, int productId) throws SQLException, DeleteException;
    
    /** 비회원 장바구니의 상품id로 정보 꺼내기 */
    List<CartDTO> getGuestCartItems(List<CartDTO> guestCartItems) throws SQLException;
    
    /** 비회원 장바구니 가격 계산 */
    public Map<String, Object> calculateGuestCartPrices(List<CartDTO> guestCartItems) throws SQLException;
    
    /** 비회원 로그인시 장바구니 이관 */
    boolean migrateGuestCart(List<CartDTO> cartItems) throws SQLException, AddException;
	
}
