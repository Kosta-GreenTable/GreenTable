package site.greentable.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import site.greentable.dto.UserDTO;
import site.greentable.dto.UserInfoDTO;
import site.greentable.exception.AddException;
import site.greentable.service.UserService;
import site.greentable.service.UserServiceImpl;

@WebServlet("/register") 
public class UserRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 현재 시간 문자열 얻는 메서드 (예시)
    private String getCurrentTimeString() {
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return java.time.LocalDateTime.now().format(formatter);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. 요청 파라미터에서 데이터 꺼내서 DTO에 세팅
        UserDTO userDto = new UserDTO();
        userDto.setEmail(request.getParameter("email"));
        userDto.setPassword(request.getParameter("password"));
        userDto.setLastLogin(getCurrentTimeString());

        UserInfoDTO userInfoDto = new UserInfoDTO();
        userInfoDto.setUserName(request.getParameter("userName"));
        userInfoDto.setPhone(request.getParameter("phone"));
        userInfoDto.setZipCode(Integer.parseInt(request.getParameter("zipCode")));
        userInfoDto.setAddress(request.getParameter("address"));
        userInfoDto.setDetailAddress(request.getParameter("detailAddress"));
        userInfoDto.setOrderCount(0);
        userInfoDto.setTotalAmount(0);
        userInfoDto.setUserGrade("브론즈");
        userInfoDto.setPoint(0);

        userDto.setUserInfoDto(userInfoDto);

        // 2. 서비스 호출해서 회원가입 처리
        UserService userService = new UserServiceImpl();

        try {
            userService.register(userDto);
            // 성공 시 이동할 페이지
            response.sendRedirect("registerSuccess.jsp");
        } catch (AddException e) {
            // 실패 시 에러 메시지 전달 후 가입 폼으로 포워딩
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("registerForm.jsp").forward(request, response);
        }
    }
}
