package site.greentable.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

// 중요: 서버 시작 시 자동 호출
@WebServlet(urlPatterns = "/init", loadOnStartup = 1)
public class InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("=== InitServlet 초기화 시작 ===");

        try {
            // DispatcherServlet에서 사용할 Map 생성
            Map<String, Controller> map = new HashMap<>();
            System.out.println("==========DispatcherServlet 1 init(), map: " + map);

            // Controller 인스턴스 등록
            map.put("user", new UserControllerImpl());
            map.put("ajaxUser", new AjaxUserControllerImpl());
            map.put("mypage", new MypageControllerImpl());


            // ServletContext에 등록
            ServletContext application = config.getServletContext();
            application.setAttribute("map", map);
            
            System.out.println("==========DispatcherServlet 2 init(), map: " + map);
            

            System.out.println("=== Controller Map 등록 완료 ===");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("InitServlet 초기화 중 오류 발생", e);
        }
    }
}
