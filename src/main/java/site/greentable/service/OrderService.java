package site.greentable.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.exception.NotFoundException;

public interface OrderService {

    /**
     * 결제 성공 시 주문 정보 저장 | 트랜잭션 |
     * 1. 주문 시퀀스 생성 2. 주문 정보 등록
     * 3. 주문 상세 정보 등록 4. 결제 정보 등록
     * 5. 사용자 누적주문금액, 주문횟수 업데이트 (주문금액 일정 달성시 유저등급 업데이트?)
     * 6. 포인트 사용시 사용자정보 테이블에 point 사용한만큼 감소
     */
    boolean processOrder(List<CartDTO> orderItems, Map<String, Object> orderData) throws SQLException;

    /** 조작 위험있는 상품 가격, 할인율 정보 DB로 꺼내기 */
    CartDTO getProductDetail(int productId) throws SQLException;

    /** 회원 주문 내역 조회 */
    List<OrderDTO> getOrdersByUserId(int userId) throws SQLException;

    /** 비회원 주문 조회 */
    OrderDTO getGuestOrder(String merchantUid, String guestPassword) throws SQLException, NotFoundException;

    /** 회원 취소/환불 주문 내역 조회 (페이징) */
    List<OrderDTO> getCancelledOrdersByUserId(Integer userId, String period, String status, String search, int page)
            throws SQLException;

    /** 회원 취소/환불 주문 총 개수 조회 */
    int getCancelledOrdersCount(Integer userId, String period, String status, String search) throws SQLException;

    /** 주문 상세 정보 조회 (취소/환불 상세) */
    OrderDTO getOrderDetailById(int orderId) throws SQLException;

}
