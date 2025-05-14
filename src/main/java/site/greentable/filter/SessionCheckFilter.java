package site.greentable.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class SessionCheckFilter
 */
@WebFilter(urlPatterns = { "/front", "/ajax" })
public class SessionCheckFilter implements Filter {
	private Gson gson = new Gson();

	public SessionCheckFilter() {
		System.out.println("SessionCheckFilter 생성됨...");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String key = request.getParameter("key");
		String methodName = request.getParameter("methodName");
		String requestURI = req.getRequestURI();

		// key나 methodName이 null이거나 빈문자열일경우 에러발생
		if ("".equals(key) || key == null || "".equals(methodName) || methodName == null) {
			// 동기 요청일 경우 에러 메시지 세팅 후 400에러페이지로 포워딩한다
			if (requestURI.contains("front")) {
				req.setAttribute("error", new Exception("잘못된 경로입니다"));
				req.getRequestDispatcher("error/400.jsp").forward(request, response);
				// 아닐경우 그냥 상태코드 세팅하고 에러메시지 전송
			} else {
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				Map<String, String> jsonMap = new HashMap<>();
				jsonMap.put("errorMsg", "잘못된 경로입니다");
				res.getWriter().print(gson.toJson(jsonMap));
			}
			return;
		}

		if (key.equals("mypage")) {

			HttpSession session = req.getSession();

			if (session.getAttribute("userId") == null) {
				// 동기 요청일 경우 에러 메시지 세팅 후 401에러페이지로 포워딩한다
				if (requestURI.contains("front")) {
					req.setAttribute("error", new Exception("로그인하고 이용해주세요^^."));
					req.getRequestDispatcher("error/401.jsp").forward(request, response);
					// 아닐경우 그냥 상태코드 세팅하고 에러메시지 전송
				} else {
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Map<String, String> jsonMap = new HashMap<>();
					jsonMap.put("errorMsg", "로그인하고 이용해주세요^^.");
					res.getWriter().print(gson.toJson(jsonMap));
				}
				return;
			}
		}

		chain.doFilter(request, response);
	}

}
