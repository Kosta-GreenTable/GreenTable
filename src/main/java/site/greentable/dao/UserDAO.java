package site.greentable.dao;

import java.sql.Connection;

import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.ServerException;

public interface UserDAO {
	
	//로그인시 사용하는 메서드
	UserDTO selectUserByEmail(String email, String password) throws ServerException;

	//사용자 추가(회원가입)
	int insertUser(UserDTO userDto) throws AddException;

	//사용자 추가시 user_infos에도 추가해야 하므로 해당 메서드(트랜잭션 유지)
	int insertUserInfo(UserInfoDTO userInfoDto, Connection conn) throws AddException;

	
	//이메일(로그인아이디) 찾기
	UserDTO selectUserByName(String name, String phone) throws NotFoundException;

	//패스워드 재설정시 사용
	UserDTO selectUserByEmailAndName(String email, String name) throws NotFoundException;

	//사용자 정보 수정
	int updateUser(UserDTO userDto) throws ModifyException;

	//사용자 탈퇴(회원탈퇴)
	int deleteUser(int userId) throws DeleteException;

}
