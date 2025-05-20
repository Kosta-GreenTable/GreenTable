package site.greentable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 이벤트(Event) 관련 요청을 처리하는 컨트롤러
 */
public class EventController implements Controller {
    
    /**
     * 클라이언트의 요청을 처리하는 메인 메소드
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String methodName = request.getParameter("methodName");
        
        // methodName 파라미터 값에 따라 적절한 메소드 호출
        if ("list".equals(methodName)) {
            return list(request, response);
        } else if ("detail".equals(methodName)) {
            return detail(request, response);
        }
        
        // 기본적으로 이벤트 목록 페이지로 이동
        return list(request, response);
    }
    
    /**
     * 이벤트 목록 페이지를 표시
     * - 진행중/종료된 이벤트 목록을 보여줌
     * - 실제 데이터는 event.js에서 클라이언트 측에서 로드됨
     */
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // event.jsp 페이지로 포워딩
        return new ModelAndView("/event/event.jsp");
    }
    
    /**
     * 이벤트 상세 페이지를 표시
     * - 현재는 클라이언트 측(event.js)에서 처리하므로 기본 페이지로 리다이렉션
     * - 추후 서버 측 데이터베이스 구현 시 사용 예정
     */
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String eventId = request.getParameter("id");
        
        // 여기서 eventId를 사용하여 데이터베이스에서 이벤트 정보를 조회할 수 있음
        // 현재는 클라이언트 측에서 처리하므로 기본 페이지로 이동
        
        return new ModelAndView("/event/event.jsp");
    }
}