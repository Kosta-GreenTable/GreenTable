package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.exception.EmailVerifyException;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;

public class AjaxUserControllerImpl implements AjaxUserController {

	private UserService userService = new UserServiceImpl();

	@Override
	public Object verifyEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");
		String verifyCode = userService.verifyEmail(email);
		request.getSession().setAttribute("verifyCode", verifyCode);
		return 1;
	}

	@Override
	public Object verifyEmailOk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (!(session.getAttribute("verifyCode") != null
				&& request.getParameter("code").equals(session.getAttribute("verifyCode")))) {
			throw new EmailVerifyException("인증번호가 잘못되었습니다");
		}
		return 1;
	}

}
