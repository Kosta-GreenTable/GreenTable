package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MypageController extends Controller {
    ModelAndView mypage(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myCancelList(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView getCancelDetail(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myPointHistory(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myCoupons(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
