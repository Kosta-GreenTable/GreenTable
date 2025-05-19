package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.exception.NotFoundException;

public interface UserController extends Controller {

	//로그인
	ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//로그아웃
	ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//회원가입
	ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//약관페이지
	ModelAndView terms(HttpServletRequest request, HttpServletResponse response) throws Exception;

//	ModelAndView findUserEmail(HttpServletRequest request, HttpServletResponse response) throws Exception;
//
//	ModelAndView findUserPassword(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//카카오 로그인
	ModelAndView loginKakao(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//구글 로그인
	ModelAndView loginGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//카카오 로그인 콜백
	ModelAndView loginKakaoCallback(HttpServletRequest request, HttpServletResponse response) throws Exception;

	//구글 로그인 콜백 
	ModelAndView loginGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
