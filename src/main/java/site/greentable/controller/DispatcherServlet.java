package site.greentable.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.coyote.BadRequestException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.exception.ForbiddenException;
import site.greentable.exception.MethodNotAllowedException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.UnAuthorizedException;

/**
 * 모든 요청을 중앙집중적으로 관리해줄 진입점(FrontController) Controller이다.
 */
@MultipartConfig( // 어노테이션을 통해 서블릿이 파일 업로드 기능을 할 수 있도록 웹 컨테이너에 지시
		maxFileSize = 1024 * 1024 * 5, // 5M - 한 번에 업로드 할 수 있는 파일 크기 제한
		maxRequestSize = 1024 * 1024 * 50 // 50M -전체 요청의 크기 제한. 기본값은 무제한
)
@WebServlet(urlPatterns = "/front", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Controller> map;

	@Override
	public void init() throws ServletException {
		System.out.println("DispatcherServlet init....");
		ServletContext application = super.getServletContext();
		map = (Map<String, Controller>) application.getAttribute("map");

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String key = request.getParameter("key");
		String methodName = request.getParameter("methodName");

		try {

			Controller con = map.get(key);

			if (con == null) {
				throw new NotFoundException("잘못된 경로입니다");
			}
			Method method = null;
			try {
				method = con.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			} catch (NoSuchMethodException e) {
				throw new NotFoundException("잘못된 경로입니다");
			}
			// 공통의 메소드 호출 그 결과 받아서 이동 시킨다.
			ModelAndView mv = (ModelAndView) method.invoke(con, request, response);

			if (mv.isRedirect()) { // redirect방식으로 이동하자.
				response.sendRedirect(mv.getViewName());
			} else {
				// forward방식 이동하자
				request.getRequestDispatcher(mv.getViewName()).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getCause());
			if (e.getCause() instanceof BadRequestException) {
				request.getRequestDispatcher("/error/400.jsp").forward(request, response);
			} else if (e.getCause() instanceof UnAuthorizedException) {
				request.getRequestDispatcher("/error/401.jsp").forward(request, response);
			} else if (e.getCause() instanceof ForbiddenException) {
				request.getRequestDispatcher("/error/403.jsp").forward(request, response);
			} else if (e.getCause() instanceof NotFoundException) {
				request.getRequestDispatcher("/error/404.jsp").forward(request, response);
			} else if (e.getCause() instanceof MethodNotAllowedException) {
				request.getRequestDispatcher("/error/405.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/error/500.jsp").forward(request, response);
			}
		}
	}

}
