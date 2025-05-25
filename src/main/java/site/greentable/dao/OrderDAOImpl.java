package site.greentable.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import site.greentable.dto.CartDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.OrderDetailDTO;
import site.greentable.dto.PaymentDTO;
import site.greentable.util.DbUtil;

public class OrderDAOImpl implements OrderDAO {
	private Properties proFile = new Properties();

	public OrderDAOImpl() {
		try {
			// properties 파일 로딩
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int createOrderSeq(Connection con) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int orderId = 0;
		String sql = proFile.getProperty("query.createSeq");
		try {
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "일반");
			ps.executeUpdate();

			// 생성된 주문 ID 가져오기
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
		} finally {
			DbUtil.dbClose(null, ps, rs);
		}
		return orderId;
	}

	@Override
	public void insertOrder(Connection con, int orderId, Map<String, Object> orderData) throws SQLException {
		PreparedStatement ps = null;
		String sql = proFile.getProperty("query.insertOrder");
		try {
			ps = con.prepareStatement(sql);

			// 주문 데이터 매핑
			ps.setInt(1, orderId);

			// userId가 null일 경우(비회원)
			Integer userId = (Integer) orderData.get("userId");
			if (userId != null) {
				ps.setInt(2, userId);
			} else {
				ps.setNull(2, java.sql.Types.INTEGER);
			}
			ps.setString(3, (String) orderData.get("merchantUid"));
			ps.setString(4, (String) orderData.get("name"));
			ps.setString(5, (String) orderData.get("phone"));
			ps.setString(6, (String) orderData.get("email"));
			ps.setString(7, (String) orderData.get("recipient"));
			ps.setString(8, (String) orderData.get("recipientPhone"));
			ps.setString(9, (String) orderData.get("zipCode"));
			ps.setString(10, (String) orderData.get("address"));
			ps.setString(11, (String) orderData.get("addressDetail"));
			ps.setInt(12, (Integer) orderData.get("totalAmount"));
			ps.setInt(13, (Integer) orderData.getOrDefault("usedPoint", 0));

			// 비회원 주문 비밀번호
			String guestPassword = (String) orderData.get("guestPassword");
			if (guestPassword != null) {
				ps.setString(14, guestPassword);
			} else {
				ps.setNull(14, java.sql.Types.VARCHAR);
			}

			ps.executeUpdate();
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public void insertOrderDetail(Connection con, int orderId, CartDTO item) throws SQLException {
		PreparedStatement ps = null;
		String sql = proFile.getProperty("query.insertOrderDetail");

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, orderId);
			ps.setInt(2, item.getProductId());
			ps.setInt(3, item.getQuantity());
			ps.setInt(4, item.getPrice());
			// ps.setInt(5, item.getDiscountRate());
			ps.executeUpdate();
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public void insertPayment(Connection con, PaymentDTO paymentDTO) throws SQLException {
		PreparedStatement ps = null;
		String sql = proFile.getProperty("query.insertPayment");

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, paymentDTO.getOrderId());
			ps.setString(2, paymentDTO.getPayMethod());
			ps.setInt(3, paymentDTO.getPaidAmount());
			ps.setString(4, paymentDTO.getPaymentStatus());
			ps.setString(5, paymentDTO.getImpUid());
			ps.setString(6, paymentDTO.getMerchantUid());
			ps.executeUpdate();
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public void updateUserOrderInfo(Connection con, int userId, int orderAmount) throws SQLException {
		PreparedStatement ps = null;
		String sql = proFile.getProperty("query.updateUserOrderInfo");

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, orderAmount); // 누적 주문금액 증가
			ps.setInt(2, userId); // 사용자 ID
			ps.executeUpdate();
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public void updateUserPoint(Connection con, int userId, int usedPoint) throws SQLException {
		PreparedStatement ps = null;
		String sql = proFile.getProperty("query.updateUserPoint");

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, usedPoint); // 포인트 사용량
			ps.setInt(2, userId); // 사용자 ID
			ps.executeUpdate();
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public OrderDTO selectOrderById(int orderId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderDTO order = null;

		String sql = proFile.getProperty("query.getOrderDetail");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, orderId);
			rs = ps.executeQuery();

			if (rs.next()) {
				order = new OrderDTO();
				order.setOrderId(rs.getInt("order_id"));
				order.setUserId(rs.getInt("user_id"));
				order.setMerchantUid(rs.getString("merchant_uid"));
				order.setCustomerName(rs.getString("customer_name"));
				order.setCustomerPhone(rs.getString("customer_phone"));
				order.setCustomerEmail(rs.getString("customer_email"));
				order.setRecipient(rs.getString("recipient"));
				order.setRecipientPhone(rs.getString("recipient_phone"));
				order.setZipCode(rs.getString("zip_code"));
				order.setAddress(rs.getString("address"));
				order.setAddressDetail(rs.getString("address_detail"));
				order.setTotalAmount(rs.getInt("total_amount"));
				order.setUsedPoint(rs.getInt("used_point"));
				order.setOrderStatus(rs.getString("order_status"));
				order.setOrderAt(rs.getDate("order_at")); // 주문시간 매핑
			}
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return order;
	}

	@Override
	public List<OrderDTO> selectOrdersByUser(int userId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDTO> orderList = new ArrayList<>();

		String sql = proFile.getProperty("query.getOrderInfo");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			// 주문 ID별로 OrderDTO를 관리하기 위한 Map
			Map<Integer, OrderDTO> orderMap = new HashMap<>();

			while (rs.next()) {
				int orderId = rs.getInt("order_id");

				// 주문 ID로 OrderDTO를 찾거나 새로 생성
				OrderDTO order = orderMap.get(orderId);
				if (order == null) {
					order = new OrderDTO();
					order.setOrderId(orderId);
					order.setOrderAt(rs.getDate("order_at"));
					order.setTotalAmount(rs.getInt("total_amount"));
					order.setOrderStatus(rs.getString("order_status"));
					order.setMerchantUid(rs.getString("merchant_uid"));
					order.setMainImageName(rs.getString("image_name"));
					order.setOrderDetails(new ArrayList<>()); // 주문 상세 리스트 초기화

					orderMap.put(orderId, order);
				}

				// 주문 상세 정보 생성 및 추가
				OrderDetailDTO orderDetail = new OrderDetailDTO(rs.getInt("order_detail_id"), rs.getInt("product_id"),
						rs.getString("name"), rs.getInt("quantity"), rs.getInt("price"));
				order.getOrderDetails().add(orderDetail);
			}

			// Map의 값을 List로 변환
			orderList.addAll(orderMap.values());
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return orderList;
	}

	@Override
	public OrderDTO selectGuestOrder(String merchantUid, String guestPassword) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderDTO order = null;

		String sql = proFile.getProperty("query.getGuestOrder");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, merchantUid);
			ps.setString(2, guestPassword);
			rs = ps.executeQuery();

			if (rs.next()) {
				order = new OrderDTO();
				order.setOrderId(rs.getInt("order_id"));
				order.setMerchantUid(rs.getString("merchant_uid"));
				order.setCustomerName(rs.getString("customer_name"));
				order.setCustomerEmail(rs.getString("customer_email"));
				order.setCustomerPhone(rs.getString("customer_phone"));
				order.setRecipient(rs.getString("recipient"));
				order.setRecipientPhone(rs.getString("recipient_phone"));
				order.setZipCode(rs.getString("zip_code"));
				order.setAddress(rs.getString("address"));
				order.setAddressDetail(rs.getString("address_detail"));
				order.setTotalAmount(rs.getInt("total_amount"));
				order.setUsedPoint(rs.getInt("used_point"));
				order.setOrderStatus(rs.getString("order_status"));
				order.setOrderAt(rs.getDate("order_at"));
				order.setMainImageName(rs.getString("image_name"));
				order.setOrderDetails(new ArrayList<>());

				// 주문 상세 정보 추가
				do {
					OrderDetailDTO detail = new OrderDetailDTO();
					detail.setProductId(rs.getInt("product_id"));
					detail.setProductName(rs.getString("name"));
					detail.setQuantity(rs.getInt("quantity"));
					detail.setPrice(rs.getInt("price"));
					order.getOrderDetails().add(detail);
				} while (rs.next());
			}
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return order;
	}

}
