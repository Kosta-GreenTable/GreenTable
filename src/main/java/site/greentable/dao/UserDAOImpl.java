package site.greentable.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			proFile.load(this.getClass().getClassLoader().getResourceAsStream("dbQuery.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//로그인시 사용하는 메서드
	@Override
	public UserDTO selectUserByEmail(String email, String password) throws ServerException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserDTO userDto = null;

		String sql = proFile.getProperty("query.userlogin");// select * from users where user_id=? and pwd=?
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
	public int insertUser(UserDTO userDto) throws AddException {
		return 0;
	}

	@Override
	public UserDTO selectUserByName(String name, String phone) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO selectUserByEmailAndName(String email, String name) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateUser(UserDTO userDto) throws ModifyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteUser(int userId) throws DeleteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertUserInfo(UserInfoDTO userInfoDto, Connection conn) throws AddException {
		// TODO Auto-generated method stub
		return 0;
	}

}
