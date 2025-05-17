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
public class HandlerMappingListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        ServletContext application = event.getServletContext();
        
        // 일반 Controller 매핑 처리
        Map<String, Controller> map = loadControllers("actionMapping");
        application.setAttribute("map", map);
        
        // REST Controller 매핑 처리
        Map<String, RestController> ajaxMap = loadRestControllers("ajaxMapping");
        application.setAttribute("ajaxMap", ajaxMap);
        
        // 컨텍스트 경로 저장
        application.setAttribute("path", application.getContextPath()); // ${path}
    }
    
    /**
     * 일반 Controller 객체를 로딩하는 메소드
     */
    private Map<String, Controller> loadControllers(String resourceName) {
        Map<String, Controller> map = new HashMap<>();
        
        try {
            // properties 파일 로딩
            ResourceBundle rb = ResourceBundle.getBundle(resourceName); // resources/actionMapping.properties
            
            for (String key : rb.keySet()) {
                String value = rb.getString(key);
                
                // Reflection API를 사용하여 객체 생성
                Class<?> className = Class.forName(value);
                Controller controller = (Controller) className.getDeclaredConstructor().newInstance();
                System.out.println("Controller key: " + key + " = " + value);
                
                map.put(key, controller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return map;
    }
    
    /**
     * RestController 객체를 로딩하는 메소드
     */
    private Map<String, RestController> loadRestControllers(String resourceName) {
        Map<String, RestController> map = new HashMap<>();
        
        try {
            // properties 파일 로딩 시도
            ResourceBundle rb = ResourceBundle.getBundle(resourceName); // resources/ajaxMapping.properties
            
            for (String key : rb.keySet()) {
                String value = rb.getString(key);
                
                // Reflection API를 사용하여 객체 생성
                Class<?> className = Class.forName(value);
                RestController controller = (RestController) className.getDeclaredConstructor().newInstance();
                System.out.println("RestController key: " + key + " = " + value);
                
                map.put(key, controller);
            }
        } catch (Exception e) {
            System.out.println("REST 컨트롤러 로딩 중 오류: " + e.getMessage());
            // 리소스 번들을 찾지 못해도 프로그램은 계속 실행됨
        }
        
        return map;
    }
}