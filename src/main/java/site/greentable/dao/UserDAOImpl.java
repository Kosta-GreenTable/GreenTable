package site.greentable.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
			ps.setInt(10, userInfoDto.getPoint() > 0 ? userInfoDto.getPoint() : 0);

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
		PreparedStatement ps = null;

		String sql = proFile.getProperty("query.updateUser");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userDto.getStatus());
			ps.setString(2, userDto.getUserType());
			ps.setString(3, userDto.getProvider());
			ps.setString(4, userDto.getOauthId());
			ps.setInt(5, userDto.getUserId());

			int result = ps.executeUpdate();
			if (result == 0)
				throw new ModifyException("회원 정보 수정 실패");
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException("회원 정보 수정 중 오류 발생");
		} finally {
			DbUtil.dbClose(con, ps);
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

}
