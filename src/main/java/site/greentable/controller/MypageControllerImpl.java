package site.greentable.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.OrderDTO;
import site.greentable.dto.UserDTO;
import site.greentable.exception.UnAuthorizedException;
import site.greentable.service.OrderService;
import site.greentable.service.OrderServiceImpl;



public class MypageControllerImpl implements Controller {
	private OrderService orderService = new OrderServiceImpl();
	
	 public ModelAndView mypage(HttpServletRequest request, HttpServletResponse response) throws Exception {
			HttpSession session = request.getSession();   
			UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
	        Integer userId = (Integer) session.getAttribute("userId");
	        System.out.println("MypageControllerImpl - loginUser in session: " + loginUser);
	        
	        if (loginUser == null) {
	            return new ModelAndView("/user/login.jsp", true); // 로그인 페이지로 리다이렉트
	        }

	        // 주문 내역 조회
	        List<OrderDTO> orderList = orderService.getOrdersByUserId(userId);

	        // JSP로 데이터 전달
	        request.setAttribute("orderList", orderList);

	        request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());
	        return new ModelAndView("/user/mypage.jsp");
	    }

	 
}
