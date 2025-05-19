package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

	ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
