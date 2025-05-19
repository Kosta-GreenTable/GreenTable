package site.greentable.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.PaymentDTO;

public interface OrderDAO {
	
	/** 주문 시퀀스 생성 */
	int createOrderSeq(Connection con) throws SQLException;
	
	/** 주문 정보 등록 */
	void insertOrder(Connection con, Map<String, Object> orderData) throws SQLException;
	
	/** 주문 상세 정보 등록 */
    void insertOrderDetail(Connection con, int orderId, CartDTO item) throws SQLException;
    
    /** 결제 정보 등록 */
    void insertPayment(Connection con, PaymentDTO paymentDTO) throws SQLException;
    
    /** 사용자 주문횟수, 누적 주문금액 업데이트 */
    void updateUserOrderInfo(Connection con, int userId, int orderAmount) throws SQLException; 
    
    /** 포인트 사용 시 사용자 보유 포인트 업데이트*/
    void updateUserPoint(Connection con, int userId, int usedPoint) throws SQLException; 
	
	/** 주문 상세 조회 */
	OrderDTO selectOrderById(int orderId) throws SQLException;
	
	/** 특정 유저의 주문 내역 조회 */
	List<OrderDTO> selectOrdersByUser(int userId) throws SQLException;
	 
}
