package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.UserDTO;
import site.greentable.exception.MethodNotAllowedException;
import site.greentable.service.UserService;

public class UserControllerImpl implements UserController {
	
	private UserService userService;
	
	
	@Override
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getMethod().equals("GET")) {
			return new ModelAndView("user/login.jsp");
		} else if (request.getMethod().equals("POST")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			UserDTO userDto = userService.login(email, password);
			HttpSession session = request.getSession();
			session.setAttribute("userId", userDto.getUserId());
			session.setAttribute("email", userDto.getEmail());
			return new ModelAndView("/", true);
			
		} else {
			throw new MethodNotAllowedException("허용된 메소드가 아닙니다");
		}
	

	}

	@Override
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.invalidate();
		return new ModelAndView("index.jsp", true);
	}

	@Override
	public ModelAndView join(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ModelAndView findUserEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView findUserPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public ModelAndView loginKakao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView loginGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView loginKakaoCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView loginGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
