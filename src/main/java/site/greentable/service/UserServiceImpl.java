package site.greentable.service;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import site.greentable.dao.UserDAO;
import site.greentable.dao.UserDAOImpl;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;
import site.greentable.util.DbUtil;

public class UserServiceImpl implements UserService {

	private UserDAO userDao = new UserDAOImpl();

	@Override
	public UserDTO login(String userEmail, String userPwd) throws NotFoundException, ServerException {
		// 먼저 이메일로 사용자 존재 여부 확인
		UserDTO existingUser = userDao.selectUserByEmailOnly(userEmail);

		if (existingUser == null) {
			// 아이디(이메일)가 존재하지 않는 경우
			throw new NotFoundException("존재하지 않는 아이디입니다.");
		}

		// 이메일과 비밀번호로 사용자 조회
		UserDTO userDto = userDao.selectUserByEmail(userEmail, userPwd);
		if (userDto != null) {
			UserInfoDTO userInfo = userDao.findUserInfoByUserId(userDto.getUserId());
			userDto.setUserInfoDto(userInfo);
		} else {
			// 아이디는 존재하지만 비밀번호가 틀린 경우
			throw new NotFoundException("비밀번호가 틀렸습니다.");
		}

		return userDto;
	}

	@Override
	public void register(UserDTO userDto) throws AddException {
		Connection con = null;
		System.out.println("==== register 진입 ====");

		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false); // 트랜잭션 시작

			System.out.println("*************** con 객체 상태: " + con);

			UserDAO userDAO = new UserDAOImpl();

			// 1. 기본 사용자 정보 insert -> user_id 생성
			int userId = userDAO.insertUser(userDto, con);

			// 2. userInfoDto 가져와서 userId 세팅
			UserInfoDTO userInfoDto = userDto.getUserInfoDto();
			if (userInfoDto == null) {
				throw new AddException("UserInfoDTO가 null입니다. 회원 상세정보가 필요합니다.");
			}
			userInfoDto.setUserId(userId);

			// 3. 상세 정보 insert
			userDAO.insertUserInfo(userInfoDto, con);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (con != null) {
				try {
					con.rollback();
					System.out.println("회원가입 트랜잭션 롤백");
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			throw new AddException("회원가입 실패: " + e.getMessage());
		} finally {
			DbUtil.dbClose(con, null);
			System.out.println("DB 연결 종료");
		}
	}

	@Override
	public String verifyEmail(String email) throws EmailVerifyException {
		// TODO: 이메일 발송 기능 구현 필요 (javax.mail 의존성 추가 후)
		// 현재는 임시로 6자리 인증코드만 생성
		SecureRandom random = new SecureRandom();
		int number = random.nextInt(1000000); // 0 ~ 999999
		String verifyCode = String.format("%06d", number);

		System.out.println("임시 이메일 인증코드: " + verifyCode + " (실제로는 " + email + "로 발송되어야 함)");
		return verifyCode;
	}

	@Override
	public boolean checkEmailDuplicate(String email) {
		return userDao.isEmailExists(email);
	}

	@Override
	public String findUserEmail(String name, String phone) throws NotFoundException {
		UserDTO dto = userDao.selectUserByName(name, phone);
		return dto.getEmail();
	}

	@Override
	public void findUserPwd(String name, String email) throws NotFoundException {
		UserDTO dto = userDao.selectUserByEmailAndName(email, name);
		if (dto == null) {
			throw new NotFoundException("해당 사용자 정보를 찾을 수 없습니다.");
		}

		// 보안상 실제 비밀번호를 보내면 안되므로, 비밀번호 재설정 링크를 이메일로 보내는 방식 권장
		// 여기에 이메일 발송 로직이 들어갈 수 있음

	}

	@Override
	public UserDTO loginKakao(String code) throws AddException {
		// 걷어내기
		return null;
	}

	@Override
	public void kakaoJoin(UserDTO userDto) throws AddException {
		// 걷어내기

	}

	@Override
	public UserDTO loginGoogle(String code) throws AddException {
		// 걷어내기
		return null;
	}

	@Override
	public void googleJoin(UserDTO userDto) throws AddException {
		// 걷어내기

	}

	@Override
	public UserInfoDTO getUserInfoByUserId(int userId) {
		return userDao.findUserInfoByUserId(userId);
	}

	@Override
	public int updateUser(UserDTO userDto) throws ModifyException {
		return userDao.updateUser(userDto);
	}

	@Override
	public boolean withdrawUser(String userId) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	// Admin methods implementation
	@Override
	public List<UserDTO> getAdminUserList(String searchType, String searchKeyword, String status, int offset,
			int pageSize) {
		return userDao.getAdminUserList(searchType, searchKeyword, status, offset, pageSize);
	}

	@Override
	public int getAdminUserCount(String searchType, String searchKeyword, String status) {
		return userDao.getAdminUserCount(searchType, searchKeyword, status);
	}

	@Override
	public List<OrderDTO> getAdminOrderList(String searchType, String searchKeyword, String status, String startDate,
			String endDate, int offset, int pageSize) {
		return userDao.getAdminOrderList(searchType, searchKeyword, status, startDate, endDate, offset, pageSize);
	}

	@Override
	public int getAdminOrderCount(String searchType, String searchKeyword, String status, String startDate,
			String endDate) {
		return userDao.getAdminOrderCount(searchType, searchKeyword, status, startDate, endDate);
	}

	@Override
	public boolean suspendUser(String email) {
		return userDao.suspendUser(email);
	}

	@Override
	public boolean activateUser(String email) {
		return userDao.activateUser(email);
	}

	@Override
	public UserDTO getUserDetail(String email) {
		return userDao.getUserDetailByEmail(email);
	}

	@Override
	public List<OrderDTO> getOrderDetail(String orderNo) {
		return userDao.getOrderDetailByOrderNo(orderNo);
	}

	@Override
	public boolean updateOrderStatus(String orderNo, String status) {
		return userDao.updateOrderStatus(orderNo, status);
	}

	@Override
	public int getTotalUsers() {
		return userDao.getTotalUsers();
	}

	@Override
	public int getActiveUsers() {
		return userDao.getActiveUsers();
	}

	@Override
	public int getSuspendedUsers() {
		return userDao.getSuspendedUsers();
	}

	@Override
	public int getTotalOrders() {
		return userDao.getTotalOrders();
	}

	@Override
	public int getPendingOrders() {
		return userDao.getPendingOrders();
	}

	@Override
	public int getCompletedOrders() {
		return userDao.getCompletedOrders();
	}

	@Override
	public int getCancelledOrders() {
		return userDao.getCancelledOrders();
	}

	// 통계 관련 메서드들 구현
	@Override
	public List<Object[]> getDailySalesStats(String startDate, String endDate) {
		return userDao.getDailySalesStats(startDate, endDate);
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit) {
		return userDao.getTopSellingProducts(limit);
	}

	@Override
	public List<Object[]> getMonthlySalesStats(int year) {
		return userDao.getMonthlySalesStats(year);
	}

	@Override
	public List<Object[]> getCategoryStats() {
		return userDao.getCategoryStats();
	}

	@Override
	public List<Object[]> getUserRegistrationStats(String startDate, String endDate) {
		return userDao.getUserRegistrationStats(startDate, endDate);
	}

	@Override
	public int getTotalRevenue() {
		return userDao.getTotalRevenue();
	}

	@Override
	public List<Object[]> getOrderStatusStats() {
		return userDao.getOrderStatusStats();
	}

	@Override
	public List<Object[]> getProductSalesRanking(int limit) {
		return userDao.getProductSalesRanking(limit);
	}

}
