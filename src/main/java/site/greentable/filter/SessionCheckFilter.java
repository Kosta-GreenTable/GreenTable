package site.greentable.filter;

import java.io.IOException;

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
@WebFilter(urlPatterns = "/front")
public class SessionCheckFilter implements Filter {

	public SessionCheckFilter() {
		System.out.println("SessionCheckFilter 생성됨...");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String key = request.getParameter("key");
		String methodName = request.getParameter("methodName");
		// key나 methodName이 null이거나 빈문자열일경우 에러발생
		if ("".equals(key) || key == null || "".equals(methodName) || methodName == null) {
			res.sendError(400);
			return;
		}

		if (key.equals("mypage")) {
			// 인증된 사용자만 해라...

			HttpSession session = req.getSession();

			if (session.getAttribute("userId") == null) {
				req.setAttribute("errorMsg", "로그인하고 이용해주세요.^^");
				req.getRequestDispatcher("error/401.jsp").forward(request, response);
				return;// 함수를 빠져나가라
			}
		}

		chain.doFilter(request, response);
	}

}
