package site.greentable.controller;

import java.io.IOException;
import java.lang.reflect.Method;
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

	Map<String, RestController> map;
	Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {

		ServletContext application = config.getServletContext();
		Object obj = application.getAttribute("ajaxMap");
		map = (Map<String, RestController>) obj;
		gson = new Gson();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter("key"); // customer
		String methodName = request.getParameter("methodName"); // idCheck , insert , selectAll

		try {
			RestController controller = map.get(key);
			Method method = controller.getClass().getMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);

			Object obj = method.invoke(controller, request, response);

			String data = gson.toJson(obj);
//			System.out.println("data = " + data);

			response.getWriter().print(data);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", e.getCause().getMessage());
			if (e instanceof BadRequestException) {
				request.getRequestDispatcher("/error/400.jsp").forward(request, response);
			} else if (e instanceof UnAuthorizedException) {
				request.getRequestDispatcher("/error/401.jsp").forward(request, response);
			} else if (e instanceof ForbiddenException) {
				request.getRequestDispatcher("/error/403.jsp").forward(request, response);
			} else if (e instanceof NotFoundException) {
				request.getRequestDispatcher("/error/404.jsp").forward(request, response);
			} else if (e instanceof MethodNotAllowedException) {
				request.getRequestDispatcher("/error/405.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/error/500.jsp").forward(request, response);
			}

		}
	}// service 메소드 끝

}
