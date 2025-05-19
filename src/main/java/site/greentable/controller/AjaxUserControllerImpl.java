package site.greentable.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;

public class AjaxUserControllerImpl implements AjaxUserController {

	private UserService userService = new UserServiceImpl();

	@Override
	public Object verifyEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");
		String verifyCode = userService.verifyEmail(email);
		request.getSession().setAttribute("verifyCode", verifyCode);
		return 1;
	}

	@Override
	public Object verifyEmailOk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (!(session.getAttribute("verifyCode") != null
				&& request.getParameter("code").equals(session.getAttribute("verifyCode")))) {
			throw new EmailVerifyException("인증번호가 잘못되었습니다");
		}
		return 1;
	}
	
	@Override
	public Object register(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    BufferedReader reader = request.getReader();
	    StringBuilder json = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        json.append(line);
	    }

	    Gson gson = new Gson();
	    JsonObject jsonObject = gson.fromJson(json.toString(), JsonObject.class);

	    String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
	    String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : null;
	    String userName = jsonObject.has("userName") ? jsonObject.get("userName").getAsString() : null;
	    String phone = jsonObject.has("phone") ? jsonObject.get("phone").getAsString() : null;
	    int zipCode = jsonObject.has("zipCode") ? jsonObject.get("zipCode").getAsInt() : 0;
	    String address = jsonObject.has("address") ? jsonObject.get("address").getAsString() : null;
	    String detailAddress = jsonObject.has("detailAddress") ? jsonObject.get("detailAddress").getAsString() : null;

	    UserDTO userDto = new UserDTO();
	    userDto.setEmail(email);
	    userDto.setPassword(password);
	    userDto.setLastLogin(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

	    UserInfoDTO userInfoDto = new UserInfoDTO();
	    userInfoDto.setUserName(userName);
	    userInfoDto.setPhone(phone);
	    userInfoDto.setZipCode(zipCode);
	    userInfoDto.setAddress(address);
	    userInfoDto.setDetailAddress(detailAddress);

	    userDto.setUserInfoDto(userInfoDto);

	    response.setContentType("application/json;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    
	    try {
	        userService.register(userDto);
	        JsonObject result = new JsonObject();
	        result.addProperty("success", true);
	        result.addProperty("message", "회원가입 성공");
	        out.write(result.toString());   // 여기서 out 사용
	    } catch (AddException e) {
	        e.printStackTrace();
	        JsonObject result = new JsonObject();
	        result.addProperty("success", false);
	        result.addProperty("message", "회원가입 실패: " + e.getMessage());
	        out.write(result.toString());   // 여기서도 out 사용
	    }
	    out.flush(); // flush 해주기
	    return null;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}





}
