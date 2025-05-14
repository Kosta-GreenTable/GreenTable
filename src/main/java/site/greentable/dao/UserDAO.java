package site.greentable.dao;

import site.greentable.dto.UserDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.DeleteException;
import site.greentable.exception.ModifyException;
import site.greentable.exception.NotFoundException;

public interface UserDAO {

	UserDTO selectUserByEmail(String email, String password) throws NotFoundException;

	int insertUser(UserDTO userDto) throws AddException;

	UserDTO selectUserByName(String name, String phone) throws NotFoundException;

	UserDTO selectUserByEmailAndName(String email, String name) throws NotFoundException;

	int updateUser(UserDTO userDto) throws ModifyException;

	int deleteUser(int userId) throws DeleteException;

}
