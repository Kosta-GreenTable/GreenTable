package site.greentable.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import site.greentable.controller.Controller;
import site.greentable.controller.RestController;

/**
 * Application Lifecycle Listener implementation class HandlerMappingListener
 * 서버가 start될떄 필요한 각 Controller를 미리 생성해서 map저장하고 application영역에 저장한다. - 모든 영역에서
 * 사용할수 있도록....
 */
@WebListener
public class AjaxHandlerMappingListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		System.out.println("AjaxHandlerMappingLister 초기화");

		// actionMapping.properties파일 로딩
		ResourceBundle rb = ResourceBundle.getBundle("ajaxMapping"); // resources/actionMapping.properties
		
		// 반복문 안헤서 key와 value를 분리해서

		Map<String, RestController> ajaxMap = new HashMap<String, RestController>();

		try {
			for (String key : rb.keySet()) {
				String value = rb.getString(key);

				// String은 value를 Reflection API를 적용해서 객체로 만든다.
				Class<?> className = Class.forName(value);

				// 생성 - value를 객체로 만들고
				RestController con = (RestController) className.getDeclaredConstructor().newInstance();
				System.out.println("key" + " = " + value + "con" + " = " + con);

				// Map에 저장한다.
				ajaxMap.put(key, con);

			} // for문끝
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 마지막에 map을 application영역에 저장한다.
		ServletContext application = event.getServletContext();
		application.setAttribute("ajaxMap", ajaxMap);
	}

}
