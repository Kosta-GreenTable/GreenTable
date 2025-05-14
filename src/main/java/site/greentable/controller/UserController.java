package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserController extends Controller {

	ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView join(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView findUserEmail(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView findUserPassword(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView loginKakao(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView loginGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView loginKakaoCallback(HttpServletRequest request, HttpServletResponse response) throws Exception;

	ModelAndView loginGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
