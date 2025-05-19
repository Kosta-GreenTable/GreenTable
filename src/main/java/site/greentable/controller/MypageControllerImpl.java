package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.dto.UserDTO;
import site.greentable.exception.UnAuthorizedException;



public class MypageControllerImpl implements Controller {
	
	 public ModelAndView mypage(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");
	        System.out.println("MypageControllerImpl - loginUser in session: " + loginUser);
	        if (loginUser == null) {
	            throw new UnAuthorizedException("로그인 후 이용 가능합니다.");
	        }

	        request.setAttribute("userName", loginUser.getUserInfoDto().getUserName());
	        return new ModelAndView("/user/mypage.jsp");
	    }

//	@Override
//	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		return mypage(request, response);
//	}
}
