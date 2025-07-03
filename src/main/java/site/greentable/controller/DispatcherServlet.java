package site.greentable.controller;

import java.io.IOException;
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
 * 
 * MultipartConfig는 web.xml에서 무제한으로 설정되어 있음
 */
// @WebServlet(urlPatterns = "/front", loadOnStartup = 2)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Controller> map;

	@Override
	public void init() throws ServletException {
		System.out.println("DispatcherServlet init....");
		ServletContext application = super.getServletContext();
		map = (Map<String, Controller>) application.getAttribute("map");

		System.out.println("======= DispatcherServlet init(), map: " + map);

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String key = null;
		String methodName = null;

		// 멀티파트 요청인지 확인
		String contentType = request.getContentType();
		boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");

		if (isMultipart) {
			// 멀티파트 요청의 경우 쿼리 스트링에서 파라미터 추출
			String queryString = request.getQueryString();
			if (queryString != null) {
				String[] params = queryString.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					if (keyValue.length == 2) {
						if ("key".equals(keyValue[0])) {
							key = keyValue[1];
						} else if ("methodName".equals(keyValue[0])) {
							methodName = keyValue[1];
						}
					}
				}
			}
		} else {
			// 일반 요청의 경우 기존 방식 사용
			key = request.getParameter("key");
			methodName = request.getParameter("methodName");
		}

		Controller controller = map.get(key);

		System.out.println("==================DispatcherServlet.service() called  ================");
		System.out.println("key=" + key + ", methodName=" + methodName);
		System.out.println("Controller instance: " + controller);

		try {

			Controller con = map.get(key);

			if (con == null) {
				throw new NotFoundException("잘못된 경로입니다");
			}
			Method method = null;
			try {
				method = con.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
				System.out.println("찾은 메서드 = " + method);
			} catch (NoSuchMethodException e) {
				throw new NotFoundException("잘못된 경로입니다");
			}

			Object result = method.invoke(con, request, response);

			// Ajax 등에서 이미 직접 출력 처리한 경우
			if (response.isCommitted()) {
				return; // // 이미 응답 끝냈으면 아무것도 하지 말고 종료
			}

			if (result == null) {
				return; // 응답은 커밋되지 않았지만, 처리할 것도 없음
			}

			if (!(result instanceof ModelAndView)) {
				throw new NotFoundException("Controller 메서드가 ModelAndView를 반환하지 않았습니다: "
						+ con.getClass().getName() + "#" + methodName);
			}

			ModelAndView mv = (ModelAndView) result;

			if (mv.isRedirect()) {
				response.sendRedirect(mv.getViewName());
			} else {
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