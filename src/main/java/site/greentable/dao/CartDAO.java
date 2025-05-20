package site.greentable.dao;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.CartDTO;

public interface CartDAO {
	
	/**
	 * 장바구니 목록 조회
	 * */
	List<CartDTO> selectCartByUserId(int userId) throws SQLException;
	
	
	/**
	 * 장바구니 상품 등록
	 * 상품 상세 페이지 -> 장바구니 추가 할 경우
	 * 같은 상품 담을 시, 담은 수량 만큼 추가 (쿼리문에서 처리)
	 * */
	int insertCart(CartDTO cartdto) throws SQLException;
	
		
	/**
	 * 상품 수량 수정
	 * 장바구니 페이지 -> -, + 버튼으로 수량 변경
	 * */	
	int updateQuantity(CartDTO cartdto) throws SQLException;
	
	
	/**
	 * 상품 삭제
	 * */
	int deleteCart(int userId, int productId) throws SQLException;
	
	
}
