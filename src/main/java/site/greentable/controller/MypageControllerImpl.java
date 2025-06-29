package site.greentable.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import site.greentable.dto.CouponDTO;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.PointHistoryDTO;
import site.greentable.dto.UserDTO;
import site.greentable.service.CouponService;
import site.greentable.service.CouponServiceImpl;
import site.greentable.service.OrderService;
import site.greentable.service.OrderServiceImpl;
import site.greentable.service.PointService;
import site.greentable.service.PointServiceImpl;

public class MypageControllerImpl implements MypageController {
	private OrderService orderService = new OrderServiceImpl();
	private PointService pointService = new PointServiceImpl();
	private CouponService couponService = new CouponServiceImpl();

	/**
	 * 메인 마이페이지 표시
	 */
	@Override
	public ModelAndView mypage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		Integer userId = (Integer) session.getAttribute("userId");

		if (loginUser == null) {
			return new ModelAndView("/user/auth-required.jsp");
		}

		// 주문 내역 조회
		List<OrderDTO> orderList = orderService.getOrdersByUserId(userId);

		// JSP로 데이터 전달
		request.setAttribute("orderList", orderList);
		request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());

		return new ModelAndView("/user/mypage.jsp");
	}

	/**
	 * 취소/환불 내역 페이지 표시
	 */
	@Override
	public ModelAndView myCancelList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		Integer userId = (Integer) session.getAttribute("userId");

		if (loginUser == null) {
			return new ModelAndView("/user/auth-required.jsp");
		}

		// 파라미터 가져오기
		String period = request.getParameter("period");
		if (period == null || period.isEmpty()) {
			period = "1"; // 기본값: 1개월
		}

		String status = request.getParameter("status");
		if (status == null || status.isEmpty()) {
			status = "all"; // 기본값: 전체
		}

		String search = request.getParameter("search");
		if (search == null) {
			search = "";
		}

		String pageParam = request.getParameter("page");
		int page = 1;
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		// 취소/환불 주문 목록 조회
		List<OrderDTO> cancelledOrders = orderService.getCancelledOrdersByUserId(
				userId, period, status, search, page);

		// 전체 개수 조회 (페이지네이션용)
		int totalCount = orderService.getCancelledOrdersCount(
				userId, period, status, search);

		// 페이지네이션 계산
		int pageSize = 10; // 페이지당 10개
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		// JSP에 데이터 전달
		request.setAttribute("cancelledOrders", cancelledOrders);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("period", period);
		request.setAttribute("status", status);
		request.setAttribute("search", search);
		request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());

		return new ModelAndView("/user/mycancel.jsp");
	}

	/**
	 * 취소/환불 상세 정보 조회 (AJAX)
	 */
	@Override
	public ModelAndView getCancelDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		Integer userId = (Integer) session.getAttribute("userId");

		// JSON 응답 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();

			// 로그인 체크
			if (loginUser == null) {
				out.write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
				out.flush();
				return null;
			}

			// 주문 ID 파라미터 가져오기
			String orderIdParam = request.getParameter("orderId");
			if (orderIdParam == null || orderIdParam.isEmpty()) {
				out.write("{\"success\": false, \"message\": \"주문 ID가 필요합니다.\"}");
				out.flush();
				return null;
			}

			int orderId;
			try {
				orderId = Integer.parseInt(orderIdParam);
			} catch (NumberFormatException e) {
				out.write("{\"success\": false, \"message\": \"올바르지 않은 주문 ID입니다.\"}");
				out.flush();
				return null;
			}

			// 주문 상세 정보 조회
			OrderDTO orderDetail = orderService.getOrderDetailById(orderId);

			if (orderDetail == null) {
				out.write("{\"success\": false, \"message\": \"주문 정보를 찾을 수 없습니다.\"}");
				out.flush();
				return null;
			}

			// 본인 주문인지 확인
			if (orderDetail.getUserId() != userId) {
				out.write("{\"success\": false, \"message\": \"접근 권한이 없습니다.\"}");
				out.flush();
				return null;
			}
			// JSON 응답 생성 (Gson 없이 직접 생성)
			String jsonResponse = String.format(
					"{\"success\": true, \"message\": \"성공\", \"order\": " +
							"{\"orderId\": %d, \"orderNo\": \"%s\", \"orderDate\": \"%s\", " +
							"\"totalAmount\": %d, \"orderStatus\": \"%s\"}}",
					orderDetail.getOrderId(),
					orderDetail.getOrderNo() != null ? orderDetail.getOrderNo() : "",
					orderDetail.getOrderDate() != null ? orderDetail.getOrderDate().toString() : "",
					orderDetail.getTotalAmount(),
					orderDetail.getOrderStatus() != null ? orderDetail.getOrderStatus() : "");
			out.write(jsonResponse);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				PrintWriter out = response.getWriter();
				out.write("{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}");
				out.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null; // AJAX 응답이므로 JSP 반환하지 않음
	}

	/**
	 * 포인트 내역 페이지 표시
	 */
	@Override
	public ModelAndView myPointHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		Integer userId = (Integer) session.getAttribute("userId");

		if (loginUser == null) {
			return new ModelAndView("/user/auth-required.jsp");
		}

		// 파라미터 가져오기
		String period = request.getParameter("period");
		if (period == null || period.isEmpty()) {
			period = "3"; // 기본값: 3개월
		}

		String pageParam = request.getParameter("page");
		int page = 1;
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		// 포인트 내역 조회
		List<PointHistoryDTO> pointHistory = pointService.getPointHistoryByUserId(userId);

		// 현재 포인트 잔액 조회 (UserService를 통해 조회)
		int currentPoints = loginUser.getUserInfoDto().getPoint();

		// JSP에 데이터 전달
		request.setAttribute("pointHistory", pointHistory);
		request.setAttribute("currentPage", page);
		request.setAttribute("period", period);
		request.setAttribute("currentPoints", currentPoints);
		request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());

		return new ModelAndView("/user/mypoint.jsp");
	}

	/**
	 * 쿠폰 관리 페이지 표시
	 */
	@Override
	public ModelAndView myCoupons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		Integer userId = (Integer) session.getAttribute("userId");

		if (loginUser == null) {
			return new ModelAndView("/user/auth-required.jsp");
		}

		// 파라미터 가져오기
		String status = request.getParameter("status");
		if (status == null || status.isEmpty()) {
			status = "available"; // 기본값: 사용 가능한 쿠폰
		}

		String pageParam = request.getParameter("page");
		int page = 1;
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		// 쿠폰 목록 조회
		List<CouponDTO> coupons;

		switch (status) {
			case "used":
				coupons = couponService.getUsedCouponsByUserId(userId);
				break;
			case "expired":
				coupons = couponService.getExpiredCouponsByUserId(userId);
				break;
			default: // available
				coupons = couponService.getAvailableCouponsByUserId(userId);
				break;
		}

		// JSP에 데이터 전달
		request.setAttribute("coupons", coupons);
		request.setAttribute("currentPage", page);
		request.setAttribute("status", status);
		request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());

		return new ModelAndView("/user/mycoupon.jsp");
	}
}
