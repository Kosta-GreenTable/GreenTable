package site.greentable.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.greentable.dto.Farm;
import site.greentable.exception.BadRequestException;
import site.greentable.service.FarmService;
import site.greentable.service.FarmServiceImpl;

/**
 * 농가 관련 기능을 처리하는 컨트롤러
 */
public class FarmController implements Controller {

    private FarmService farmService;

    public FarmController() {
        farmService = new FarmServiceImpl();
    }

    /**
     * 모든 농가 목록을 조회하는 메소드 (일반 사용자용 - 활성 농가만)
     */
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 활성 상태인 농가만 조회
        List<Farm> farmList = farmService.getActiveFarms();

        // HTML 형식으로 응답
        request.setAttribute("farmList", farmList);
        return new ModelAndView("farm/farm.jsp");
    }

    /**
     * 농가 상세 정보를 조회하는 메소드
     */
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // URL 파라미터에서 farmId 가져오기
        String farmIdParam = request.getParameter("farmId");
        if (farmIdParam == null || farmIdParam.trim().isEmpty()) {
            throw new BadRequestException("farmId 파라미터가 필요합니다.");
        }

        int farmId;
        try {
            farmId = Integer.parseInt(farmIdParam);
        } catch (NumberFormatException e) {
            throw new BadRequestException("farmId는 숫자여야 합니다.");
        }

        // 농가 상세 정보 조회
        Farm farm = farmService.getFarmDetail(farmId);
        if (farm == null) {
            throw new BadRequestException("존재하지 않는 농가 ID입니다: " + farmId);
        }

        // 농가에서 생산하는 상품 목록 조회
        request.setAttribute("farm", farm);
        request.setAttribute("farmProducts", farmService.getFarmProducts(farmId));

        return new ModelAndView("farm/farm-detail.jsp");
    }

    /**
     * 관리자 페이지 - 농가 목록 조회 (모든 농가) - 페이징 기능 추가
     */
    public ModelAndView adminList(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 페이지 파라미터 가져오기
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1)
                    currentPage = 1;
            } catch (NumberFormatException e) {
                // 잘못된 페이지 숫자는 무시하고 1페이지로
            }
        }

        // 페이지당 항목 수와 페이지 블록 크기 설정
        int itemsPerPage = 10; // 한 페이지에 표시할 농가 수
        int blockCount = 5; // 페이지 네비게이션에 표시할 페이지 번호 수

        // 전체 농가 수 조회
        int totalItems = farmService.getTotalFarmCount();

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages; // 요청 페이지가 전체 페이지보다 크면 마지막 페이지로 조정
        }

        // 시작 인덱스 계산
        int startIndex = (currentPage - 1) * itemsPerPage;

        // 페이징 처리된 농가 목록 조회
        List<Farm> farmList = farmService.getFarmsByPage(startIndex, itemsPerPage);

        // 페이지 정보를 request에 저장
        request.setAttribute("farmList", farmList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // 페이지 블록 정보를 맵으로 저장
        Map<String, Integer> pageCnt = new HashMap<>();
        pageCnt.put("blockcount", blockCount);
        request.setAttribute("pageCnt", pageCnt);

        return new ModelAndView("admin/farm-list.jsp");
    }

    /**
     * 관리자 페이지 - 농가 등록 폼 표시
     */
    public ModelAndView registForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return new ModelAndView("admin/farm-form.jsp");
    }

    /**
     * 관리자 페이지 - 농가 등록 처리
     */
    public ModelAndView regist(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 폼에서 전달된 데이터로 Farm 객체 생성
        Farm farm = new Farm();
        farm.setName(request.getParameter("name"));
        farm.setDescription(request.getParameter("description"));
        farm.setAddress(request.getParameter("address"));
        farm.setFarmImg(request.getParameter("farmImg"));
        farm.setCategory(request.getParameter("category")); // 카테고리 추가

        // 위도와 경도가 비어있지 않을 경우에만 설정
        String latitudeStr = request.getParameter("latitude");
        String longitudeStr = request.getParameter("longitude");

        if (latitudeStr != null && !latitudeStr.trim().isEmpty()) {
            double latitude = Double.parseDouble(latitudeStr);
            // 위도 범위 검사: -90 ~ 90
            if (latitude < -90 || latitude > 90) {
                throw new BadRequestException("위도는 -90에서 90 사이의 값이어야 합니다.");
            }
            farm.setLatitude(latitude);
        }

        if (longitudeStr != null && !longitudeStr.trim().isEmpty()) {
            double longitude = Double.parseDouble(longitudeStr);
            // 경도 범위 검사: -180 ~ 180
            if (longitude < -180 || longitude > 180) {
                throw new BadRequestException("경도는 -180에서 180 사이의 값이어야 합니다.");
            }
            farm.setLongitude(longitude);
        }

        // 기본적으로 계약 상태는 '활성'으로 설정
        farm.setContractStatus("활성");

        // 농가 등록 처리
        int farmId = farmService.registerFarm(farm);

        // 등록 결과 메시지 설정
        if (farmId > 0) {
            request.setAttribute("message", "농가 등록이 성공적으로 완료되었습니다.");
        } else {
            request.setAttribute("message", "농가 등록에 실패했습니다.");
        }

        // 농가 목록 페이지로 리다이렉트
        return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
    }

    /**
     * 관리자 페이지 - 농가 수정 폼 표시
     */
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String farmIdParam = request.getParameter("farmId");

        if (farmIdParam == null || farmIdParam.trim().isEmpty()) {
            throw new BadRequestException("farmId 파라미터가 필요합니다.");
        }

        int farmId = Integer.parseInt(farmIdParam);

        // 농가 정보 조회
        Farm farm = farmService.getFarmDetail(farmId);

        if (farm == null) {
            throw new BadRequestException("존재하지 않는 농가 ID입니다: " + farmId);
        }

        request.setAttribute("farm", farm);

        return new ModelAndView("admin/farm-update-form.jsp");
    }

    /**
     * 관리자 페이지 - 농가 수정 처리
     */
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 폼에서 전달된 데이터로 Farm 객체 생성
        Farm farm = new Farm();

        String farmIdStr = request.getParameter("farmId");
        int farmId = Integer.parseInt(farmIdStr);
        farm.setFarmId(farmId);

        farm.setName(request.getParameter("name"));
        farm.setDescription(request.getParameter("description"));
        farm.setAddress(request.getParameter("address"));
        farm.setFarmImg(request.getParameter("farmImg"));
        farm.setCategory(request.getParameter("category")); // 카테고리 추가

        String latitudeStr = request.getParameter("latitude");
        String longitudeStr = request.getParameter("longitude");

        if (latitudeStr != null && !latitudeStr.trim().isEmpty()) {
            double latitude = Double.parseDouble(latitudeStr);
            // 위도 범위 검사 추가
            if (latitude < -90 || latitude > 90) {
                throw new BadRequestException("위도는 -90에서 90 사이의 값이어야 합니다.");
            }
            farm.setLatitude(latitude);
        }

        if (longitudeStr != null && !longitudeStr.trim().isEmpty()) {
            double longitude = Double.parseDouble(longitudeStr);
            // 경도 범위 검사 추가
            if (longitude < -180 || longitude > 180) {
                throw new BadRequestException("경도는 -180에서 180 사이의 값이어야 합니다.");
            }
            farm.setLongitude(longitude);
        }

        farm.setContractStatus(request.getParameter("contractStatus"));

        int result = farmService.updateFarm(farm);

        if (result > 0) {
            request.setAttribute("message", "농가 정보가 성공적으로 수정되었습니다.");
        } else {
            request.setAttribute("message", "농가 정보 수정에 실패했습니다.");
        }

        // 리다이렉트 방식 수정
        return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
    }

    /**
     * 관리자 페이지 - 농가 상태 변경 (활성/종료)
     */
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 디버그 로깅 추가
            System.out.println("updateStatus 메소드 호출됨");
            System.out.println("파라미터 farmId: " + request.getParameter("farmId"));
            System.out.println("파라미터 status: " + request.getParameter("status"));

            String farmIdStr = request.getParameter("farmId");
            String status = request.getParameter("status");

            // farmId가 null이거나 빈 문자열인 경우 오류 메시지 출력 후 페이지 리다이렉트
            if (farmIdStr == null || farmIdStr.trim().isEmpty()) {
                request.setAttribute("message", "농가 ID가 제공되지 않았습니다.");
                return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
            }

            // status가 null이거나 빈 문자열인 경우 기본값 설정
            if (status == null || status.trim().isEmpty()) {
                status = "종료"; // 기본값 설정
            }

            int farmId = Integer.parseInt(farmIdStr);

            // 실제 DB 업데이트 수행
            int result = farmService.updateFarmStatus(farmId, status);

            if (result > 0) {
                request.setAttribute("message", "농가 상태가 성공적으로 변경되었습니다.");
            } else {
                request.setAttribute("message", "농가 상태 변경에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("숫자 변환 오류: " + e.getMessage());
            request.setAttribute("message", "올바르지 않은 농가 ID 형식입니다.");
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "농가 상태 변경 중 오류가 발생했습니다.");
        }

        // 관리자 목록 페이지로 리다이렉트
        return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
    }

    /**
     * 농가 삭제 메소드 (계약 종료된 농가만 삭제 가능)
     */
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String farmIdStr = request.getParameter("farmId");

        try {
            // 디버깅 로그
            System.out.println("Delete 메서드 호출됨, farmId: " + farmIdStr);

            if (farmIdStr == null || farmIdStr.trim().isEmpty()) {
                request.setAttribute("message", "농가 ID가 제공되지 않았습니다.");
                return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
            }

            int farmId = Integer.parseInt(farmIdStr);

            // 해당 농가가 실제로 존재하는지 확인
            Farm farm = farmService.getFarmById(farmId);
            if (farm == null) {
                request.setAttribute("message", "해당 ID의 농가를 찾을 수 없습니다: " + farmId);
                return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
            }

            boolean result = farmService.deleteFarm(farmId);
            
            if (result) {
                // 삭제 성공 시 ID 재정렬 실행
                boolean reorderResult = farmService.reorderFarmIds();
                if (reorderResult) {
                    request.setAttribute("message", "농가가 성공적으로 삭제되고 ID가 재정렬되었습니다.");
                } else {
                    request.setAttribute("message", "농가는 삭제되었으나 ID 재정렬 중 문제가 발생했습니다.");
                }
            } else {
                request.setAttribute("message", "농가 삭제에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("숫자 변환 오류: " + e.getMessage());
            request.setAttribute("message", "올바르지 않은 농가 ID 형식입니다.");
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "농가 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return new ModelAndView(request.getContextPath() + "/front?key=farm&methodName=adminList", true);
    }

    /**
     * 메인 페이지 농가 소개 섹션을 표시하는 메서드
     * 메서드명을 category로 변경 (ProductController와 동일한 패턴 적용)
     */
    public ModelAndView category(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 활성 상태인 농가만 조회
            List<Farm> farmList = farmService.getActiveFarms();

            // 결과를 request에 저장
            request.setAttribute("farmList", farmList);

            // 카테고리 이름과 타이틀 설정 (ProductController와 유사하게)
            request.setAttribute("category", "farm");
            request.setAttribute("categoryTitle", "농가 소개");

            // 농가 소개 JSP 반환
            return new ModelAndView("farm/farm-section.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "농가 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return new ModelAndView("farm/error-section.jsp");
        }
    }
}