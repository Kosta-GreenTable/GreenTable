package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.exception.EmailVerifyException;

public interface AjaxUserController extends RestController {

	//이메일 인증번호 전송
	Object verifyEmail(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//이메일 인증번호 확인
	Object verifyEmailOk(HttpServletRequest request, HttpServletResponse response) throws Exception;

	Object register(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	Object updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
