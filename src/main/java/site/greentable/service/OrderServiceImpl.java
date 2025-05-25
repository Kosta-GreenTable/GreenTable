package site.greentable.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import site.greentable.dao.OrderDAO;
import site.greentable.dao.OrderDAOImpl;
import site.greentable.dao.ProductDAO;
import site.greentable.dao.ProductDAOImpl;
import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.PaymentDTO;
import site.greentable.dto.Product;
import site.greentable.exception.NotFoundException;
import site.greentable.util.DbUtil;

public class OrderServiceImpl implements OrderService {
	OrderDAO orderDao = new OrderDAOImpl();
	ProductDAO productDao = new ProductDAOImpl();

	@Override
	public boolean processOrder(List<CartDTO> orderItems, Map<String, Object> orderData)
			throws SQLException {
		Connection con = null;
        boolean result = false;
        
        try {
            // 1. 트랜잭션 시작
            con = DbUtil.getConnection();
            con.setAutoCommit(false);
            
            System.out.println("orderService - orderData : " + orderData);
            
            // 2. 주문 시퀀스 생성
            int orderId = orderDao.createOrderSeq(con);
            
            // 3. 주문 정보 저장
            orderDao.insertOrder(con, orderId,orderData);
            
            // 4. 주문 상세 정보 저장
            for (CartDTO item : orderItems) {
                orderDao.insertOrderDetail(con, orderId, item);
            }
            
            // 5. 결제 정보 저장
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setOrderId(orderId);
            paymentDTO.setPayMethod((String) orderData.get("paymentMethod"));
            paymentDTO.setPaidAmount((Integer) orderData.get("totalAmount"));
            paymentDTO.setPaymentStatus((String) orderData.get("paymentStatus"));
            paymentDTO.setImpUid((String) orderData.get("impUid"));
            paymentDTO.setMerchantUid((String) orderData.get("merchantUid"));
            
            orderDao.insertPayment(con, paymentDTO);
            
            
            // 6. 회원인 경우 사용자 정보 업데이트
            Integer userId = (Integer) orderData.get("userId");
            if (userId != null && userId > 0) {
                // 누적 주문금액, 주문횟수 업데이트
                orderDao.updateUserOrderInfo(con, userId, (Integer) orderData.get("totalAmount"));
                
                // 포인트 사용 처리
                Integer usedPoint = (Integer) orderData.getOrDefault("usedPoint", 0);
                if (usedPoint > 0) {
                	orderDao.updateUserPoint(con, userId, usedPoint);
                }
            }
            
            // 7. 트랜잭션 커밋
            con.commit();
            result = true;
            
        } catch (Exception e) {
            // 예외 발생 시 롤백
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }       
        return result;
	}

	
	@Override
	public CartDTO getProductDetail(int productId) throws SQLException {
		Product product = productDao.selectProductDetail(productId);
		if (product != null) {
			CartDTO cart = new CartDTO(product.getName(), productId, product.getPrice(), 
										product.getDiscountRate(), product.getMainImageName());
			return cart;
		} else {
			return null;
		}
	}


	@Override
	public List<OrderDTO> getOrdersByUserId(int userId) throws SQLException {
		return orderDao.selectOrdersByUser(userId);
	}


	@Override
	public OrderDTO getGuestOrder(String merchantUid, String guestPassword) throws SQLException, NotFoundException {
		OrderDTO order = orderDao.selectGuestOrder(merchantUid, guestPassword);
		if(order == null) throw new NotFoundException("주문번호 또는 비밀번호가 일치하지 않습니다.");
		return order;
	}
	

}
