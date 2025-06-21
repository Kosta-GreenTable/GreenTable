package site.greentable.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.OrderDTO;
import site.greentable.dto.OrderDetailDTO;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;
import site.greentable.util.DbUtil;

public class UserDAOImpl implements UserDAO {
	private Properties proFile = new Properties();

	public UserDAOImpl() {
		System.out.println("UserDAOImpl 생성자 호출됨....");

		try {
			String fileName = "dbQuery.properties";
			ClassLoader classLoader = this.getClass().getClassLoader();
			if (classLoader == null) {
				System.out.println("ClassLoader가 null입니다.");
				return;
			}

			InputStream is = classLoader.getResourceAsStream(fileName);

			if (is == null) {
				System.out.println("⚠️ " + fileName + " 파일을 찾을 수 없습니다. 경로 확인 필요.");
			} else {
				proFile.load(is);
				System.out.println("✅ " + fileName + " 로딩 성공");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("proFile 내용 확인: " + proFile);

		String sql = proFile.getProperty("query.insertUser");
		System.out.println("SQL : " + sql);
	}

	// 로그인시 사용하는 메서드
	@Override
	public UserDTO selectUserByEmail(String email, String password) throws ServerException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO userDto = null;

		String sql = proFile.getProperty("query.userlogin");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);

			rs = ps.executeQuery();
			if (rs.next()) {
				userDto = new UserDTO();
				userDto.setUserId(rs.getInt("user_id"));
				userDto.setEmail(rs.getString("email"));
				userDto.setPassword(rs.getString("password"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException("서버 오류입니다. 문제가 지속되면 관리자에게 문의해주세요");
		} finally {

			DbUtil.dbClose(con, ps, rs);
		}
		return userDto;
	}

	@Override
	public UserDTO selectUserByEmailOnly(String email) throws ServerException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO userDto = null;

		String sql = "SELECT user_id, email, password FROM user_login WHERE email = ?";
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);

			rs = ps.executeQuery();
			if (rs.next()) {
				userDto = new UserDTO();
				userDto.setUserId(rs.getInt("user_id"));
				userDto.setEmail(rs.getString("email"));
				userDto.setPassword(rs.getString("password"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException("서버 오류입니다. 문제가 지속되면 관리자에게 문의해주세요");
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return userDto;
	}

	@Override
	public boolean isEmailExists(String email) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(*) FROM user_login WHERE email = ?";
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);

			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return false;
	}

	@Override
	public int insertUser(UserDTO userDto, Connection con) throws AddException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("query.insertUser"); // INSERT INTO user_login(email, password, last_login)
																// VALUES (?, ?, ?)
		System.out.println("SQL : " + sql);

		try {

			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, userDto.getEmail());
			ps.setString(2, userDto.getPassword());
			ps.setString(3, userDto.getLastLogin());

			int result = ps.executeUpdate();
			if (result == 0)
				throw new AddException("회원가입에 실패했습니다.");

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1); // user_id를 반환 (자동 생성된 값)
			} else {
				throw new AddException("user_id 생성 실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("회원가입 중 오류가 발생했습니다.");
		} finally {
			DbUtil.dbClose(null, ps, rs);
		}
	}

	@Override
	public int insertUserInfo(UserInfoDTO userInfoDto, Connection con) throws AddException {
		PreparedStatement ps = null;

		if (con == null) {
			throw new AddException("insertUserInfo 메서드에 전달된 Connection이 null입니다.");
		}

		String sql = proFile.getProperty("query.insertUserInfo"); // INSERT INTO user_infos(user_id, user_name, phone,
																	// zip_code, address, detail_address, order_count,
																	// total_amount, user_grade, point) VALUES (?, ?, ?,
																	// ?, ?, ?, ?, ?, ?, ?)
		System.out.println("insertUserInfo 쿼리: " + sql);

		try {

			ps = con.prepareStatement(sql);
			ps.setInt(1, userInfoDto.getUserId());
			ps.setString(2, userInfoDto.getUserName());
			ps.setString(3, userInfoDto.getPhone());
			ps.setInt(4, userInfoDto.getZipCode());
			ps.setString(5, userInfoDto.getAddress());
			ps.setString(6, userInfoDto.getDetailAddress());
			ps.setInt(7, userInfoDto.getOrderCount() > 0 ? userInfoDto.getOrderCount() : 0);
			ps.setInt(8, userInfoDto.getTotalAmount() > 0 ? userInfoDto.getTotalAmount() : 0);
			ps.setString(9, userInfoDto.getUserGrade() != null ? userInfoDto.getUserGrade() : "브론즈");
			ps.setInt(10, userInfoDto.getPoint() > 0 ? userInfoDto.getPoint() : 2000);

			int result = ps.executeUpdate();
			if (result == 0)
				throw new AddException("부가정보 입력 실패");
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("부가정보 저장 중 오류 발생");
		} finally {
			DbUtil.dbClose(null, ps);
		}
	}

	@Override
	public UserDTO selectUserByName(String name, String phone) throws NotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("query.findEmailByNamePhone");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, phone);

			rs = ps.executeQuery();
			if (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setEmail(rs.getString("email"));
				return dto;
			} else {
				throw new NotFoundException("해당 이름과 전화번호로 가입된 이메일을 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NotFoundException("이메일 찾기 중 오류가 발생했습니다.");
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
	}

	@Override
	public UserDTO selectUserByEmailAndName(String email, String name) throws NotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("query.findUserByEmailAndName");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, name);

			rs = ps.executeQuery();
			if (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setUserId(rs.getInt("user_id"));
				dto.setEmail(email);
				return dto;
			} else {
				throw new NotFoundException("해당 이메일과 이름으로 가입된 사용자를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NotFoundException("비밀번호 재설정 확인 중 오류 발생");
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
	}

	@Override
	public int updateUser(UserDTO userDto) throws ModifyException {
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		String sql1 = proFile.getProperty("query.updateUserPassword"); // UPDATE user_login SET password=? WHERE
																		// user_id=?
		String sql2 = proFile.getProperty("query.updateUserInfo"); // UPDATE user_info SET user_name=?, phone=?,
																	// zip_code=?, address=?, detail_address=? WHERE
																	// user_id=?

		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false); // 트랜잭션 시작

			// 1. user_login 테이블의 password 업데이트
			ps1 = con.prepareStatement(sql1);
			ps1.setString(1, userDto.getPassword());
			ps1.setInt(2, userDto.getUserId());
			int result1 = ps1.executeUpdate();

			// 2. user_info 테이블의 상세 정보 업데이트
			UserInfoDTO info = userDto.getUserInfoDto();
			ps2 = con.prepareStatement(sql2);
			ps2.setString(1, info.getUserName());
			ps2.setString(2, info.getPhone());
			ps2.setInt(3, info.getZipCode());
			ps2.setString(4, info.getAddress());
			ps2.setString(5, info.getDetailAddress());
			ps2.setInt(6, userDto.getUserId());
			int result2 = ps2.executeUpdate();

			if (result1 == 0 && result2 == 0) {
				con.rollback();
				throw new ModifyException("회원 정보 수정 실패");
			}

			con.commit();
			return result1 + result2;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			throw new ModifyException("회원 정보 수정 중 오류 발생");
		} finally {
			DbUtil.dbClose(null, ps2);
			DbUtil.dbClose(con, ps1);
		}
	}

	@Override
	public int deleteUser(int userId) throws DeleteException {
		Connection con = null;
		PreparedStatement ps = null;

		String sql = proFile.getProperty("query.deleteUser");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);

			int result = ps.executeUpdate();
			if (result == 0)
				throw new DeleteException("탈퇴할 사용자를 찾을 수 없습니다.");
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DeleteException("회원 탈퇴 중 오류 발생");
		} finally {
			DbUtil.dbClose(con, ps);
		}
	}

	@Override
	public UserInfoDTO findUserInfoByUserId(int userId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("query.findUserInfo");
		UserInfoDTO dto = null;
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);

			rs = ps.executeQuery();

			if (rs.next()) {
				dto = new UserInfoDTO();
				dto.setUserId(rs.getInt("user_id"));
				dto.setUserName(rs.getString("user_name"));
				dto.setPhone(rs.getString("phone"));
				dto.setZipCode(rs.getInt("zip_code"));
				dto.setAddress(rs.getString("address"));
				dto.setDetailAddress(rs.getString("detail_address"));
				dto.setOrderCount(rs.getInt("order_count"));
				dto.setTotalAmount(rs.getInt("total_amount"));
				dto.setUserGrade(rs.getString("user_grade"));
				dto.setPoint(rs.getInt("point"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return dto;
	}

	@Override
	public boolean withdrawUser(String userId) throws SQLException {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = proFile.getProperty("query.withdrawUser");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps);
		}

		return false;
	}

	// ==================== ADMIN METHODS ====================
	@Override
	public List<UserDTO> getAdminUserList(String searchType, String searchKeyword, String status, int offset,
			int pageSize) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<UserDTO> userList = new ArrayList<>();

		String sql = proFile.getProperty("admin.getUserList");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			// Set parameters for search conditions
			ps.setString(1, searchKeyword == null ? "" : searchKeyword);
			ps.setString(2, searchType == null ? "" : searchType);
			ps.setString(3, searchKeyword == null ? "" : searchKeyword);
			ps.setString(4, searchType == null ? "" : searchType);
			ps.setString(5, searchKeyword == null ? "" : searchKeyword);
			ps.setString(6, searchType == null ? "" : searchType);
			ps.setString(7, searchKeyword == null ? "" : searchKeyword);
			ps.setString(8, status == null ? "" : status);
			ps.setString(9, status == null ? "" : status);
			ps.setInt(10, offset);
			ps.setInt(11, pageSize);

			rs = ps.executeQuery();

			while (rs.next()) {
				UserDTO user = new UserDTO();
				user.setUserId(rs.getInt("user_id"));
				user.setEmail(rs.getString("email"));
				user.setStatus(rs.getString("state"));
				user.setCreatedAt(
						rs.getTimestamp("insert_date") != null ? rs.getTimestamp("insert_date").toString() : null);

				// Create UserInfoDTO for additional info
				UserInfoDTO userInfo = new UserInfoDTO();
				userInfo.setUserName(rs.getString("name"));
				userInfo.setPhone(rs.getString("phone"));
				user.setUserInfoDto(userInfo);

				userList.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return userList;
	}

	@Override
	public int getAdminUserCount(String searchType, String searchKeyword, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getUserCount");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			// Set parameters for search conditions
			ps.setString(1, searchKeyword == null ? "" : searchKeyword);
			ps.setString(2, searchType == null ? "" : searchType);
			ps.setString(3, searchKeyword == null ? "" : searchKeyword);
			ps.setString(4, searchType == null ? "" : searchType);
			ps.setString(5, searchKeyword == null ? "" : searchKeyword);
			ps.setString(6, searchType == null ? "" : searchType);
			ps.setString(7, searchKeyword == null ? "" : searchKeyword);
			ps.setString(8, status == null ? "" : status);
			ps.setString(9, status == null ? "" : status);

			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public boolean suspendUser(String email) {
		Connection con = null;
		PreparedStatement ps = null;

		String sql = proFile.getProperty("admin.suspendUser");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps);
		}

		return false;
	}

	@Override
	public boolean activateUser(String email) {
		Connection con = null;
		PreparedStatement ps = null;

		String sql = proFile.getProperty("admin.activateUser");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps);
		}

		return false;
	}

	@Override
	public UserDTO getUserDetailByEmail(String email) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO user = null;

		String sql = proFile.getProperty("admin.getUserDetail");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, email);

			rs = ps.executeQuery();

			if (rs.next()) {
				user = new UserDTO();
				user.setUserId(rs.getInt("user_id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setStatus(rs.getString("state"));
				user.setCreatedAt(
						rs.getTimestamp("insert_date") != null ? rs.getTimestamp("insert_date").toString() : null);
				user.setLastLogin(
						rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toString() : null);

				// Set user info details if available
				if (rs.getString("user_name") != null) {
					UserInfoDTO userInfo = new UserInfoDTO();
					userInfo.setUserName(rs.getString("user_name"));
					userInfo.setPhone(rs.getString("phone"));
					userInfo.setZipCode(rs.getInt("zip_code"));
					userInfo.setAddress(rs.getString("address"));
					userInfo.setDetailAddress(rs.getString("detail_address"));
					userInfo.setOrderCount(rs.getInt("order_count"));
					userInfo.setTotalAmount(rs.getInt("total_amount"));
					userInfo.setUserGrade(rs.getString("user_grade"));
					userInfo.setPoint(rs.getInt("point"));
					user.setUserInfoDto(userInfo);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return user;
	}

	@Override
	public List<OrderDTO> getAdminOrderList(String searchType, String searchKeyword, String status, String startDate,
			String endDate, int offset, int pageSize) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDTO> orderList = new ArrayList<>();

		String sql = proFile.getProperty("admin.getOrderList");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			// Set parameters for search conditions
			ps.setString(1, searchKeyword == null ? "" : searchKeyword);
			ps.setString(2, searchType == null ? "" : searchType);
			ps.setString(3, searchKeyword == null ? "" : searchKeyword);
			ps.setString(4, searchType == null ? "" : searchType);
			ps.setString(5, searchKeyword == null ? "" : searchKeyword);
			ps.setString(6, searchType == null ? "" : searchType);
			ps.setString(7, searchKeyword == null ? "" : searchKeyword);
			ps.setString(8, status == null ? "" : status);
			ps.setString(9, status == null ? "" : status);
			ps.setString(10, startDate == null ? "" : startDate);
			ps.setString(11, startDate == null ? "" : startDate);
			ps.setString(12, endDate == null ? "" : endDate);
			ps.setString(13, endDate == null ? "" : endDate);
			ps.setInt(14, offset);
			ps.setInt(15, pageSize);

			rs = ps.executeQuery();

			while (rs.next()) {
				OrderDTO order = new OrderDTO();
				order.setOrderId(rs.getInt("order_id"));
				order.setMerchantUid(rs.getString("orderNo"));
				order.setCustomerName(rs.getString("userName"));
				order.setCustomerEmail(rs.getString("userEmail"));
				order.setOrderAt(rs.getDate("orderDate"));
				order.setTotalAmount(rs.getInt("total_amount"));
				order.setOrderStatus(rs.getString("status"));
				orderList.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return orderList;
	}

	@Override
	public int getAdminOrderCount(String searchType, String searchKeyword, String status, String startDate,
			String endDate) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getOrderCount");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			// Set parameters for search conditions
			ps.setString(1, searchKeyword == null ? "" : searchKeyword);
			ps.setString(2, searchType == null ? "" : searchType);
			ps.setString(3, searchKeyword == null ? "" : searchKeyword);
			ps.setString(4, searchType == null ? "" : searchType);
			ps.setString(5, searchKeyword == null ? "" : searchKeyword);
			ps.setString(6, searchType == null ? "" : searchType);
			ps.setString(7, searchKeyword == null ? "" : searchKeyword);
			ps.setString(8, status == null ? "" : status);
			ps.setString(9, status == null ? "" : status);
			ps.setString(10, startDate == null ? "" : startDate);
			ps.setString(11, startDate == null ? "" : startDate);
			ps.setString(12, endDate == null ? "" : endDate);
			ps.setString(13, endDate == null ? "" : endDate);

			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public List<OrderDTO> getOrderDetailByOrderNo(String orderNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDTO> orderDetails = new ArrayList<>();

		String sql = proFile.getProperty("admin.getOrderDetail");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, orderNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				OrderDTO orderDetail = new OrderDTO();
				orderDetail.setOrderId(rs.getInt("order_id"));
				orderDetail.setMerchantUid(rs.getString("merchant_uid"));
				orderDetail.setCustomerName(rs.getString("customer_name"));
				orderDetail.setCustomerEmail(rs.getString("customer_email"));
				orderDetail.setOrderAt(rs.getDate("order_at"));
				orderDetail.setOrderStatus(rs.getString("order_status"));
				orderDetail.setTotalAmount(rs.getInt("total_amount"));

				// Create OrderDetailDTO for product information
				OrderDetailDTO productDetail = new OrderDetailDTO();
				productDetail.setProductId(rs.getInt("product_id"));
				productDetail.setProductName(rs.getString("productName"));
				productDetail.setQuantity(rs.getInt("quantity"));
				productDetail.setPrice(rs.getInt("price"));

				// Add product detail to order
				List<OrderDetailDTO> details = new ArrayList<>();
				details.add(productDetail);
				orderDetail.setOrderDetails(details);

				orderDetails.add(orderDetail);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return orderDetails;
	}

	@Override
	public boolean updateOrderStatus(String orderNo, String status) {
		Connection con = null;
		PreparedStatement ps = null;

		String sql = proFile.getProperty("admin.updateOrderStatus");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, orderNo);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps);
		}

		return false;
	}

	@Override
	public int getTotalUsers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getTotalUsers");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getActiveUsers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getActiveUsers");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getSuspendedUsers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getSuspendedUsers");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getTotalOrders() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getTotalOrders");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getPendingOrders() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getPendingOrders");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getCompletedOrders() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getCompletedOrders");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	@Override
	public int getCancelledOrders() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		String sql = proFile.getProperty("admin.getCancelledOrders");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return count;
	}

	// 통계 관련 메서드들 구현
	@Override
	public List<Object[]> getDailySalesStats(String startDate, String endDate) {
		List<Object[]> stats = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.daily.sales");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, startDate);
			ps.setString(2, endDate);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getString("order_date");
				row[1] = rs.getInt("order_count");
				row[2] = rs.getInt("total_amount");
				stats.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return stats;
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit) {
		List<Object[]> products = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.top.products");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, limit);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getString("product_name");
				row[1] = rs.getInt("total_quantity");
				row[2] = rs.getInt("total_revenue");
				products.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return products;
	}

	@Override
	public List<Object[]> getMonthlySalesStats(int year) {
		List<Object[]> stats = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.monthly.sales");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, year);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("month");
				row[1] = rs.getInt("order_count");
				row[2] = rs.getInt("total_amount");
				stats.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return stats;
	}

	@Override
	public List<Object[]> getCategoryStats() {
		List<Object[]> stats = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.category");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getString("category");
				row[1] = rs.getInt("product_count");
				row[2] = rs.getInt("total_sales");
				stats.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return stats;
	}

	@Override
	public List<Object[]> getUserRegistrationStats(String startDate, String endDate) {
		List<Object[]> stats = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.user.registration");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, startDate);
			ps.setString(2, endDate);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[2];
				row[0] = rs.getString("registration_date");
				row[1] = rs.getInt("user_count");
				stats.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return stats;
	}

	@Override
	public int getTotalRevenue() {
		int revenue = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.total.revenue");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			if (rs.next()) {
				revenue = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return revenue;
	}

	@Override
	public List<Object[]> getOrderStatusStats() {
		List<Object[]> stats = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.order.status");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[2];
				row[0] = rs.getString("status");
				row[1] = rs.getInt("count");
				stats.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return stats;
	}

	@Override
	public List<Object[]> getProductSalesRanking(int limit) {
		List<Object[]> ranking = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = proFile.getProperty("stats.product.ranking");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, limit);

			rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getString("product_name");
				row[1] = rs.getInt("total_quantity");
				row[2] = rs.getInt("total_revenue");
				row[3] = rs.getString("category");
				ranking.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}

		return ranking;
	}

	@Override
	public int getUserPointBalance(int userId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT point FROM user_infos WHERE user_id = ?";

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("point");
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("포인트 잔액 조회 중 오류가 발생했습니다.", e);
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
	}

	@Override
	public boolean updateUserPoint(int userId, int newBalance) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;

		String sql = "UPDATE user_infos SET point = ? WHERE user_id = ?";

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, newBalance);
			ps.setInt(2, userId);

			int result = ps.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("포인트 업데이트 중 오류가 발생했습니다.", e);
		} finally {
			DbUtil.dbClose(con, ps);
		}
	}

}
