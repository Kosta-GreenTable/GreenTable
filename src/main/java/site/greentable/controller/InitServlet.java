//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.io.FileInputStream;
//
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//
//@WebServlet(urlPatterns = "/init", loadOnStartup = 1)
//public class InitServlet extends HttpServlet {
//
//    @Override
//    public void init() throws ServletException {
//        System.out.println("=== InitServlet 초기화 시작 ===");
//        
//        ServletContext application = super.getServletContext();
//        
//        // 컨트롤러 등록
//        Map<String, Controller> map = new HashMap<>();
//        
//        try {
//            // properties 파일에서 컨트롤러 정보 로드
//            Properties prop = new Properties();
//            prop.load(new FileInputStream(application.getRealPath("/WEB-INF/classes/actionMapping.properties")));
//            
//            // 각 컨트롤러 객체 생성하여 맵에 저장
//            for(Object key : prop.keySet()) {
//                String keyName = (String)key;
//                String className = prop.getProperty(keyName);
//                
//                if(className == null || className.trim().isEmpty() || className.startsWith("#")) 
//                    continue;
//                
//                // 컨트롤러 인스턴스 생성
//                Class<?> cls = Class.forName(className);
//                Controller controller = (Controller)cls.getDeclaredConstructor().newInstance();
//                
//                map.put(keyName, controller);
//                System.out.println("Controller key: " + keyName + " = " + className);
//            }
//            
//            // 서블릿 컨텍스트에 맵 저장 - 중요!
//            application.setAttribute("controllerMap", map);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ServletException("컨트롤러 초기화 오류", e);
//        }
//        
//        System.out.println("=== Controller Map 등록 완료 ===");
//    }
//}