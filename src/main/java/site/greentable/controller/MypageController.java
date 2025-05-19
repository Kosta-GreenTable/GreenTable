package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MypageController {
    ModelAndView mypage(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
