package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface QnaController extends Controller {
    ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView writeQna(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myQnas(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myQnasWithFilter(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView deleteQna(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView updateQna(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView getProductQnas(HttpServletRequest request, HttpServletResponse response) throws Exception;
}