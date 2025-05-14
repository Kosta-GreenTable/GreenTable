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
@WebServlet(urlPatterns = "/ajax", loadOnStartup = 1)
public class AjaxDispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Map<String, RestController> ajaxMap;
	Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {

		ServletContext application = config.getServletContext();
		Object obj = application.getAttribute("ajaxMap");
		ajaxMap = (Map<String, RestController>) obj;
		gson = new Gson();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter("key"); // customer
		String methodName = request.getParameter("methodName"); // idCheck , insert , selectAll
		response.setContentType("application/json;charset=utf-8");

		try {
			RestController controller = ajaxMap.get(key);
			Method method = controller.getClass().getMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);

			Object obj = method.invoke(controller, request, response);

			String data = gson.toJson(obj);
//			System.out.println("data = " + data);

			response.getWriter().print(data);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("errorMsg", e.getMessage());
			if (e instanceof BadRequestException) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else if (e instanceof UnAuthorizedException) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else if (e instanceof ForbiddenException) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else if (e instanceof NotFoundException) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (e instanceof MethodNotAllowedException) {
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			response.getWriter().print(gson.toJson(jsonMap));
		}
	}// service 메소드 끝

}
