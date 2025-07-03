package site.greentable.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;

import com.google.gson.Gson;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.exception.ForbiddenException;
import site.greentable.exception.MethodNotAllowedException;
import site.greentable.exception.NotFoundException;
import site.greentable.exception.UnAuthorizedException;

/**
 * 사용자의 모든 요청을 처리할 진입점 Controller이다(FrontController의 역할한다)
 */
//@WebServlet(urlPatterns = "/ajax", loadOnStartup = 1)
public class AjaxDispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Map<String, RestController> ajaxMap;
	Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		
		ServletContext application = config.getServletContext();
		Object obj = application.getAttribute("ajaxMap");
		ajaxMap = (Map<String, RestController>) obj;
		System.out.println(ajaxMap);
		gson = new Gson();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter("key"); 
		String methodName = request.getParameter("methodName"); 
		response.setContentType("application/json;charset=utf-8");
		
		try {

			RestController controller = ajaxMap.get(key);

			if (controller == null) {
				System.out.println("여기 오니??");
				throw new NotFoundException("잘못된 경로입니다");
			}
			Method method = null;
			try {
				method = controller.getClass().getMethod(methodName, HttpServletRequest.class,
						HttpServletResponse.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new NotFoundException("잘못된 경로입니다");
			}

			System.out.println("ajax 요청 key = " + key + " | 요청 method = " + methodName);
			System.out.println("ajax 응답 컨트롤러 = " + controller + " | 응답 method = " + method);
			
			Object obj = method.invoke(controller, request, response);

			String data = gson.toJson(obj);
//			System.out.println("data = " + data);
			
			response.getWriter().print(data);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("errorMsg", e.getCause().getMessage());
			if (e.getCause() instanceof BadRequestException) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else if (e.getCause() instanceof UnAuthorizedException) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else if (e.getCause() instanceof ForbiddenException) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else if (e.getCause() instanceof NotFoundException) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (e.getCause() instanceof MethodNotAllowedException) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			response.getWriter().print(gson.toJson(jsonMap));
		}
	}// service 메소드 끝

}
