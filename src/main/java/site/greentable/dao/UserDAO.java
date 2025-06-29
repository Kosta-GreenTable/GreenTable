package site.greentable.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.OrderDTO;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;

public interface UserDAO { // 로그인시 사용하는 메서드
	UserDTO selectUserByEmail(String email, String password) throws ServerException;

	// 이메일만으로 사용자 존재 여부 확인 (로그인 에러 메시지 구분용)
	UserDTO selectUserByEmailOnly(String email) throws ServerException;

	// 이메일 중복 확인
	boolean isEmailExists(String email);

	// 사용자 추가(회원가입)
	int insertUser(UserDTO userDto, Connection con) throws AddException;

	// 사용자 추가시 user_infos에도 추가해야 하므로 해당 메서드(트랜잭션 유지)
	int insertUserInfo(UserInfoDTO userInfoDto, Connection conn) throws AddException;

	// 이메일(로그인아이디) 찾기
	UserDTO selectUserByName(String name, String phone) throws NotFoundException;

	// 패스워드 재설정시 사용
	UserDTO selectUserByEmailAndName(String email, String name) throws NotFoundException;

	// 사용자 정보 수정
	int updateUser(UserDTO userDto) throws ModifyException;

	// 사용자 탈퇴(회원탈퇴)
	int deleteUser(int userId) throws DeleteException;

	UserInfoDTO findUserInfoByUserId(int userId);

	boolean withdrawUser(String userId) throws SQLException;

	// Admin 관련 메서드들
	// 사용자 관리
	List<UserDTO> getAdminUserList(String searchType, String searchKeyword, String status, int offset, int pageSize);

	int getAdminUserCount(String searchType, String searchKeyword, String status);

	boolean suspendUser(String email);

	boolean activateUser(String email);

	UserDTO getUserDetailByEmail(String email);

	// 주문 관리
	List<OrderDTO> getAdminOrderList(String searchType, String searchKeyword, String status, String startDate,
			String endDate, int offset, int pageSize);

	int getAdminOrderCount(String searchType, String searchKeyword, String status, String startDate, String endDate);

	List<OrderDTO> getOrderDetailByOrderNo(String orderNo);

	boolean updateOrderStatus(String orderNo, String status);

	// 통계 관련
	int getTotalUsers();

	int getActiveUsers();

	int getSuspendedUsers();

	int getTotalOrders();

	int getPendingOrders();

	int getCompletedOrders();

	int getCancelledOrders();

	// 통계 관련 메서드들
	List<Object[]> getDailySalesStats(String startDate, String endDate);

	List<Object[]> getTopSellingProducts(int limit);

	List<Object[]> getMonthlySalesStats(int year);

	List<Object[]> getCategoryStats();

	List<Object[]> getUserRegistrationStats(String startDate, String endDate);

	int getTotalRevenue();

	List<Object[]> getOrderStatusStats();

	List<Object[]> getProductSalesRanking(int limit);

	// Point related methods
	/**
	 * 사용자의 현재 포인트 잔액을 조회합니다.
	 * 
	 * @param userId 사용자 ID
	 * @return 현재 포인트 잔액
	 */
	int getUserPointBalance(int userId) throws Exception;

	/**
	 * 사용자의 포인트 잔액을 업데이트합니다.
	 * 
	 * @param userId     사용자 ID
	 * @param newBalance 새로운 포인트 잔액
	 * @return 업데이트 성공 여부
	 */
	boolean updateUserPoint(int userId, int newBalance) throws Exception;

}
