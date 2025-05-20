package site.greentable.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
		 response.setContentType("application/json; charset=UTF-8");
		    Map<String, Object> result = new HashMap<>();

		    try {
		        String email = request.getParameter("email");
		        String verifyCode = userService.verifyEmail(email);
		        request.getSession().setAttribute("verifyCode", verifyCode);
		        result.put("result", true);
		    } catch (EmailVerifyException e) {
		        result.put("result", false);
		        result.put("message", e.getMessage());
		    }

		    PrintWriter out = response.getWriter();
		    String jsonStr = new Gson().toJson(result);
		    
		    out.write(jsonStr);
		    out.flush();
		    out.close();
		    return null;
	}

	@Override
	public Object verifyEmailOk(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> result = new HashMap<>();
	    HttpSession session = request.getSession();
	    String inputCode = request.getParameter("code");
	    String sessionCode = (String) session.getAttribute("verifyCode");

	    if (sessionCode == null || !inputCode.equals(sessionCode)) {
	        result.put("result", false);
	        result.put("message", "인증번호가 잘못되었습니다");
	    } else {
	        result.put("result", true);
	    }
	    return result;  // 직접 출력하지 말고 반환만 한다.
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
	    } finally {
		    out.flush(); 
		    out.close();	    }
	    return null;
	}


	@Override
	public Object updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 response.setContentType("application/json; charset=UTF-8");
		    Map<String, Object> result = new HashMap<>();
		    PrintWriter out = response.getWriter();

		    try {
		        BufferedReader reader = request.getReader();
		        StringBuilder jsonBuilder = new StringBuilder();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            jsonBuilder.append(line);
		        }

		        Gson gson = new Gson();
		        JsonObject json = gson.fromJson(jsonBuilder.toString(), JsonObject.class);

		        int userId = json.has("userId") ? json.get("userId").getAsInt() : 0;
		        String status = json.has("status") ? json.get("status").getAsString() : null;
		        String userType = json.has("userType") ? json.get("userType").getAsString() : null;
		        String provider = json.has("provider") ? json.get("provider").getAsString() : null;
		        String oauthId = json.has("oauthId") ? json.get("oauthId").getAsString() : null;

		        UserDTO userDto = new UserDTO();
		        userDto.setUserId(userId);
		        userDto.setStatus(status);
		        userDto.setUserType(userType);
		        userDto.setProvider(provider);
		        userDto.setOauthId(oauthId);

		        int updateCount = userService.updateUser(userDto);

		        result.put("success", true);
		        result.put("message", "회원 정보가 수정되었습니다.");
		        result.put("updated", updateCount);
		    } catch (Exception e) {
		        result.put("success", false);
		        result.put("message", "회원 정보 수정 실패: " + e.getMessage());
		    }

		    out.write(new Gson().toJson(result));
		    out.flush();
		    out.close();
		    return null;
	}

	

}
