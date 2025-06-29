package site.greentable.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.exception.EmailVerifyException;
import site.greentable.service.EmailService;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;

public class AjaxUserControllerImpl implements AjaxUserController {

	private UserService userService = new UserServiceImpl();
	private EmailService emailService = new EmailService();

	@Override
	public Object verifyEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> result = new HashMap<>();

		try {
			String email = request.getParameter("email");
			String verifyCode = userService.verifyEmail(email);
			request.getSession().setAttribute("verifyCode", verifyCode);
			result.put("result", true);
			result.put("message", "인증번호가 발송되었습니다.");
		} catch (EmailVerifyException e) {
			result.put("result", false);
			result.put("message", e.getMessage());
		}

		return result;
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
		return result; // 직접 출력하지 말고 반환만 한다.
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
			out.write(result.toString()); // 여기서 out 사용
		} catch (AddException e) {
			e.printStackTrace();
			JsonObject result = new JsonObject();
			result.addProperty("success", false);
			result.addProperty("message", "회원가입 실패: " + e.getMessage());
			out.write(result.toString()); // 여기서도 out 사용
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	@Override
	public Object updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> result = new HashMap<>();
		PrintWriter out = response.getWriter();

		try {
			System.out.println("요청이 들어왔습니다. request.getContentType(): " + request.getContentType());

			BufferedReader reader = request.getReader();
			StringBuilder jsonBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuilder.append(line);
			}

			System.out.println("받은 JSON 데이터: " + jsonBuilder.toString());

			JsonObject json = JsonParser.parseString(jsonBuilder.toString()).getAsJsonObject();

			String email = json.has("email") ? json.get("email").getAsString() : null;
			String password = json.has("password") ? json.get("password").getAsString() : null;

			// ✅ userInfoDto 내부 JSON 객체 가져오기
			JsonObject userInfoJson = json.getAsJsonObject("userInfoDto");

			String userName = userInfoJson.has("userName") ? userInfoJson.get("userName").getAsString() : null;
			String phone = userInfoJson.has("phone") ? userInfoJson.get("phone").getAsString() : null;
			int zipCode = userInfoJson.has("zipCode") ? userInfoJson.get("zipCode").getAsInt() : 0;
			String address1 = userInfoJson.has("address") ? userInfoJson.get("address").getAsString() : null;
			String address2 = userInfoJson.has("detailAddress") ? userInfoJson.get("detailAddress").getAsString()
					: null;

			// DTO 구성
			UserDTO userDto = new UserDTO();
			int userId = json.has("userId") ? json.get("userId").getAsInt() : 0;
			userDto.setUserId(userId);
			userDto.setEmail(email);
			if (password != null && !password.isBlank()) {
				userDto.setPassword(password);
			}

			UserInfoDTO info = new UserInfoDTO();
			info.setUserName(userName);
			info.setPhone(phone);
			info.setZipCode(zipCode);
			info.setAddress(address1);
			info.setDetailAddress(address2);
			userDto.setUserInfoDto(info);

			int updateCount = userService.updateUser(userDto);

			result.put("success", true);
			result.put("message", "회원 정보가 수정되었습니다.");
			result.put("updated", updateCount);
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "회원 정보 수정 실패: " + e.getMessage());
			e.printStackTrace();
		}

		out.write(new Gson().toJson(result));
		out.flush();
		out.close();
		return null;
	}

	@Override
	public void withdrawUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		System.out.println("withdrawUser() 호출됨");

		PrintWriter out = response.getWriter();
		JsonObject json = new JsonObject(); // Gson의 JsonObject 사용

		try {
			// 세션에서 로그인된 사용자 ID 가져오기
			HttpSession session = request.getSession(false);
			System.out.println("Session = " + session);
			if (session == null || session.getAttribute("loginUser") == null) {
				json.addProperty("status", "fail");
				json.addProperty("message", "로그인이 필요합니다.");
				out.print(new Gson().toJson(json)); // Gson으로 JSON 출력
				return;
			}
			UserDTO user = (UserDTO) session.getAttribute("loginUser");
			String userId = (String) session.getAttribute("user_id");

			System.out.println("Login User = " + user);

			// 회원 탈퇴 처리 (DB에서 상태 변경)
			boolean result = userService.withdrawUser(userId); // true: 성공, false: 실패

			if (result) {
				session.invalidate();
				json.addProperty("status", "success");
				json.addProperty("message", "회원 탈퇴가 완료되었습니다.");
			} else {
				json.addProperty("status", "fail");
				json.addProperty("message", "회원 탈퇴에 실패했습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("status", "error");
			json.addProperty("message", "서버 오류가 발생했습니다.");
		}

		out.print(new Gson().toJson(json));

	}

	/**
	 * 아이디(이메일) 중복확인
	 */
	@Override
	public Object checkDuplicate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<>();

		try {
			String email = request.getParameter("email");

			if (email == null || email.trim().isEmpty()) {
				result.put("available", false);
				result.put("message", "이메일을 입력해주세요.");
				return result;
			}

			// 이메일 중복 체크
			boolean isDuplicate = userService.checkEmailDuplicate(email);

			result.put("available", !isDuplicate);
			if (isDuplicate) {
				result.put("message", "이미 사용중인 이메일입니다.");
			} else {
				result.put("message", "사용 가능한 이메일입니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("available", false);
			result.put("message", "중복확인 중 오류가 발생했습니다.");
		}

		return result;
	}

	/**
	 * 이메일 인증번호 발송
	 */
	@Override
	public Object sendVerification(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<>();

		try {
			String email = request.getParameter("email");

			if (email == null || email.trim().isEmpty()) {
				result.put("success", false);
				result.put("message", "이메일을 입력해주세요.");
				return result;
			}

			// 인증번호 생성 및 발송
			String verificationCode = generateVerificationCode();

			// 세션에 인증번호 저장 (실제로는 Redis나 DB에 저장하는 것이 좋음)
			request.getSession().setAttribute("verificationCode_" + email, verificationCode);
			request.getSession().setAttribute("verificationTime_" + email, System.currentTimeMillis());

			// 실제 이메일 발송
			boolean emailSent = false;
			try {
				EmailService emailService = new EmailService();
				emailSent = emailService.sendVerificationEmail(email, verificationCode);
			} catch (Exception e) {
				System.err.println("EmailService 사용 중 오류: " + e.getMessage());
				emailSent = false;
			}

			if (emailSent) {
				System.out.println("이메일 발송 성공: " + email + " -> " + verificationCode);
				result.put("success", true);
				result.put("message", "인증번호가 이메일로 발송되었습니다.");
			} else {
				// 이메일 발송 실패 시에도 개발 단계에서는 성공으로 처리
				System.out.println("이메일 발송 실패, 콘솔 출력: " + email + " -> " + verificationCode);
				result.put("success", true);
				result.put("message", "인증번호가 발송되었습니다. (개발 모드: 콘솔 확인)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "인증번호 발송에 실패했습니다.");
		}

		return result;
	}

	/**
	 * 인증번호 확인
	 */
	@Override
	public Object verifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<>();

		try {
			String email = request.getParameter("email");
			String inputCode = request.getParameter("code");

			if (email == null || inputCode == null) {
				result.put("verified", false);
				result.put("message", "필수 정보가 누락되었습니다.");
				return result;
			}

			// 세션에서 저장된 인증번호와 시간 확인
			String savedCode = (String) request.getSession().getAttribute("verificationCode_" + email);
			Long savedTime = (Long) request.getSession().getAttribute("verificationTime_" + email);

			if (savedCode == null || savedTime == null) {
				result.put("verified", false);
				result.put("message", "인증번호를 먼저 요청해주세요.");
				return result;
			}

			// 5분 시간 제한 체크
			long currentTime = System.currentTimeMillis();
			long timeDiff = (currentTime - savedTime) / 1000; // 초 단위

			if (timeDiff > 300) { // 5분 = 300초
				request.getSession().removeAttribute("verificationCode_" + email);
				request.getSession().removeAttribute("verificationTime_" + email);
				result.put("verified", false);
				result.put("message", "인증번호가 만료되었습니다. 다시 요청해주세요.");
				return result;
			}

			// 인증번호 일치 확인
			if (savedCode.equals(inputCode)) {
				// 인증 성공 시 세션에서 제거
				request.getSession().removeAttribute("verificationCode_" + email);
				request.getSession().removeAttribute("verificationTime_" + email);
				// 인증 완료 표시
				request.getSession().setAttribute("emailVerified_" + email, true);

				result.put("verified", true);
				result.put("message", "이메일 인증이 완료되었습니다.");
			} else {
				result.put("verified", false);
				result.put("message", "인증번호가 일치하지 않습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("verified", false);
			result.put("message", "인증 확인 중 오류가 발생했습니다.");
		}

		return result;
	}

	/**
	 * 6자리 랜덤 인증번호 생성
	 */
	private String generateVerificationCode() {
		return String.format("%06d", (int) (Math.random() * 1000000));
	}
}
