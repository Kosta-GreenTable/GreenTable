package site.greentable.service;

import site.greentable.dto.UserDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.NotFoundException;

public interface UserService {
	UserDTO login(String userEmail, String userPwd) throws NotFoundException;

	void join(UserDTO userDto) throws AddException;

	String findUserEmail(String name, String phone) throws NotFoundException;

	void findUserPwd(String name, String email) throws NotFoundException;

	UserDTO loginKakao(String code) throws AddException;

	void kakaoJoin(UserDTO userDto) throws AddException;

	UserDTO loginGoogle(String code) throws AddException;

	void googleJoin(UserDTO userDto) throws AddException;

}
