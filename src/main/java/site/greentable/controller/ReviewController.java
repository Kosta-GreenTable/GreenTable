package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ReviewController extends Controller {
    ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView writeReview(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView myReviews(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView deleteReview(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView updateReview(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView getProductReviews(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ModelAndView downloadReviewImage(HttpServletRequest request, HttpServletResponse response) throws Exception;
}