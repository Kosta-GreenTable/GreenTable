package site.greentable.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;

public interface OrderService {
	
	/**
	 * 결제 성공 시 주문 정보 저장 | 트랜잭션 | 
	 * 1. 주문 시퀀스 생성  2. 주문 정보 등록
	 * 3. 주문 상세 정보 등록 4. 결제 정보 등록
	 * 5. 사용자 누적주문금액, 주문횟수 업데이트 (주문금액 일정 달성시 유저등급 업데이트?)
	 * 6. 포인트 사용시 사용자정보 테이블에 point 사용한만큼 감소
	 * */
    boolean processOrder(List<CartDTO> orderItems, Map<String, Object> orderData) throws SQLException;
    
    CartDTO getProductDetail(int productId) throws SQLException;
    
    List<OrderDTO> getOrdersByUserId(int userId) throws SQLException;
    
}
