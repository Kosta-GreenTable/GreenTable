package site.greentable.service;

import site.greentable.dao.UserDAO;
import site.greentable.dto.UserDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.NotFoundException;

public class UserServiceImpl implements UserService {

	private UserDAO userDao;

	@Override
	public UserDTO login(String userEmail, String userPwd) throws NotFoundException {
		UserDTO userDto = userDao.selectUserByEmail(userEmail, userPwd);
		if (userDto == null)
			throw new NotFoundException("사용자를 찾을 수 없습니다");

		return userDto;
	}

	@Override
	public void join(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

	@Override
	public String findUserEmail(String name, String phone) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findUserPwd(String name, String email) throws NotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO loginKakao(String code) throws AddException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kakaoJoin(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO loginGoogle(String code) throws AddException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void googleJoin(UserDTO userDto) throws AddException {
		// TODO Auto-generated method stub

	}

}
