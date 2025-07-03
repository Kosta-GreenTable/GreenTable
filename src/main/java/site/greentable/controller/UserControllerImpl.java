package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.MethodNotAllowedException;
import site.greentable.exception.NotFoundException;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;

public class UserControllerImpl implements UserController {

	private UserService userService = new UserServiceImpl();

	// login 컨트롤러
	@Override
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("UserControllerImpl.login() 진입");
		if (request.getMethod().equals("GET")) {
			return new ModelAndView("user/login.jsp");
		} else if (request.getMethod().equals("POST")) {

			System.out.println("POST 요청 처리 시작");

			String email = request.getParameter("email");
			String password = request.getParameter("password");

			try {
				// 로그인 서비스 호출
				UserDTO userDto = userService.login(email, password);

				// UserInfoDTO 조회 후 세팅
				UserInfoDTO userInfoDto = userService.getUserInfoByUserId(userDto.getUserId());
				userDto.setUserInfoDto(userInfoDto);

				// 세션에 저장
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", userDto);
				session.setAttribute("userId", userDto.getUserId());

				System.out.println("==========userDTO = " + userDto);
				System.out.println("userInfoDto: " + userDto.getUserInfoDto()); // null이면 문제 있음

				return new ModelAndView("index.jsp");

			} catch (Exception e) {
				// 로그인 실패 시 에러 메시지 설정
				String errorMessage = "로그인에 실패했습니다.";
				if (e.getMessage() != null) {
					if (e.getMessage().contains("아이디") || e.getMessage().contains("email")
							|| e.getMessage().contains("존재하지 않는")) {
						errorMessage = "존재하지 않는 아이디입니다.";
					} else if (e.getMessage().contains("비밀번호") || e.getMessage().contains("password")
							|| e.getMessage().contains("틀렸")) {
						errorMessage = "비밀번호가 틀렸습니다.";
					}
				}

				request.setAttribute("errorMessage", errorMessage);
				request.setAttribute("userId", email); // 입력했던 이메일 유지
				return new ModelAndView("user/login.jsp");
			}

		} else {
			throw new MethodNotAllowedException("허용된 메소드가 아닙니다");
		}

	}

	// 로그아웃 컨트롤러
	@Override
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.invalidate();
		return new ModelAndView("index.jsp", true);
	}

	// 약관동의 컨트롤러
	@Override
	public ModelAndView terms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("user/terms.jsp");
	}

	// 회원가입 컨트롤러
	@Override
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("==== register 메소드 진입 ====");
		if (request.getMethod().equals("GET")) {
			return new ModelAndView("user/register.jsp");
		} else if (request.getMethod().equals("POST")) {
			System.out.println("==== POST 요청 확인 ====");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String userName = request.getParameter("name"); // HTML form에서는 name="name"으로 되어있음
			String phone = request.getParameter("phone");
			String zipCode = request.getParameter("zipCode");
			String address = request.getParameter("address1"); // HTML form에서는 name="address1"
			String detailAddress = request.getParameter("address2"); // HTML form에서는 name="address2"

			System.out.println("이메일: " + email);
			System.out.println("비밀번호: " + password);
			System.out.println("이름: " + userName);

			try {
				UserDTO userDto = new UserDTO();
				userDto.setEmail(email);
				userDto.setPassword(password);
				userDto.setLastLogin(java.time.LocalDateTime.now()
						.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

				// UserInfoDTO 설정
				UserInfoDTO userInfoDto = new UserInfoDTO();
				userInfoDto.setUserName(userName != null ? userName : "");
				userInfoDto.setPhone(phone != null ? phone : "");
				userInfoDto.setZipCode(zipCode != null && !zipCode.isEmpty() ? Integer.parseInt(zipCode) : 0);
				userInfoDto.setAddress(address != null ? address : "");
				userInfoDto.setDetailAddress(detailAddress != null ? detailAddress : "");

				userDto.setUserInfoDto(userInfoDto);

				userService.register(userDto);
				System.out.println("==== 회원가입 성공 ====");
				return new ModelAndView("user/login.jsp", true);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("==== 회원가입 실패 ====");
				return new ModelAndView("user/register.jsp");
			}
		} else {
			throw new MethodNotAllowedException("허용된 메소드가 아닙니다");
		}

	}

	@Override
	public ModelAndView loginKakao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		throw new NotFoundException("아직 구현되지 않은 기능입니다: loginKakao");
	}

	@Override
	public ModelAndView loginGoogle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		throw new NotFoundException("아직 구현되지 않은 기능입니다: loginGoogle");
	}

	@Override
	public ModelAndView loginKakaoCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		throw new NotFoundException("아직 구현되지 않은 기능입니다: loginKakaoCallback");
	}

	@Override
	public ModelAndView loginGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		throw new NotFoundException("아직 구현되지 않은 기능입니다: loginGoogleCallback");
	}

	@Override
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");

		request.setAttribute("user", null);
		return new ModelAndView("/user/mypage.jsp");

	}

}
