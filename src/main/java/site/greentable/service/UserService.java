package site.greentable.service;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;

public interface UserService {
	UserDTO login(String userEmail, String userPwd) throws NotFoundException, ServerException;

	void register(UserDTO userDto) throws AddException;

	String verifyEmail(String email) throws EmailVerifyException;

	boolean checkEmailDuplicate(String email);

	String findUserEmail(String name, String phone) throws NotFoundException;

	void findUserPwd(String name, String email) throws NotFoundException;

	UserDTO loginKakao(String code) throws AddException;

	void kakaoJoin(UserDTO userDto) throws AddException;

	UserDTO loginGoogle(String code) throws AddException;

	void googleJoin(UserDTO userDto) throws AddException;

	UserInfoDTO getUserInfoByUserId(int userId);

	int updateUser(UserDTO userDto) throws ModifyException;

	boolean withdrawUser(String userId) throws SQLException;

	// 관리자 기능 메서드들
	List<UserDTO> getAdminUserList(String searchType, String searchKeyword, String status, int offset, int pageSize);

	int getAdminUserCount(String searchType, String searchKeyword, String status);

	int getTotalUsers();

	int getActiveUsers();

	int getSuspendedUsers();

	boolean suspendUser(String email);

	boolean activateUser(String email);

	UserDTO getUserDetail(String email);

	List<OrderDTO> getAdminOrderList(String searchType, String searchKeyword, String status, String startDate,
			String endDate, int offset, int pageSize);

	int getAdminOrderCount(String searchType, String searchKeyword, String status, String startDate, String endDate);

	int getTotalOrders();

	int getPendingOrders();

	int getCompletedOrders();

	int getCancelledOrders();

	boolean updateOrderStatus(String orderNo, String status);

	List<OrderDTO> getOrderDetail(String orderNo);

	// 통계 관련 메서드들
	List<Object[]> getDailySalesStats(String startDate, String endDate);

	List<Object[]> getTopSellingProducts(int limit);

	List<Object[]> getMonthlySalesStats(int year);

	List<Object[]> getCategoryStats();

	List<Object[]> getUserRegistrationStats(String startDate, String endDate);

	int getTotalRevenue();

	List<Object[]> getOrderStatusStats();

	List<Object[]> getProductSalesRanking(int limit);
}
