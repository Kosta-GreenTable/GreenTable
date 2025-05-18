package site.greentable.service;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.Farm;
import site.greentable.dto.Product;

/**
 * 농가 관련 서비스 인터페이스
 */
public interface FarmService {
    /**
     * 모든 농가 목록 조회
     */
    List<Farm> getAllFarms() throws SQLException;

    // 활성 상태 농가만 조회하는 메소드 추가
    List<Farm> getActiveFarms() throws Exception;

    /**
     * 전체 농가 수를 조회
     */
    int getTotalFarmCount() throws Exception;

    /**
     * 페이징 처리된 농가 목록 조회
     * 
     * @param startIndex 시작 인덱스
     * @param count      가져올 아이템 수
     */
    List<Farm> getFarmsByPage(int startIndex, int count) throws Exception;

    /**
     * 농가 삭제
     */
    boolean deleteFarm(int farmId) throws Exception;

    // getFarmById 메서드 추가
    Farm getFarmById(int farmId) throws Exception;

    /**
     * 농가 상세 정보 조회
     */
    Farm getFarmDetail(int farmId) throws SQLException;

    /**
     * 농가에서 생산하는 상품 목록 조회
     */
    List<Product> getFarmProducts(int farmId) throws SQLException;

    /**
     * 새 농가 등록
     */
    int registerFarm(Farm farm) throws SQLException;

    /**
     * 농가 정보 수정
     */
    int updateFarm(Farm farm) throws SQLException;

    /**
     * 농가 계약 상태 변경
     */
    int updateFarmStatus(int farmId, String status) throws SQLException;

    // FarmService.java에 아래 메소드 추가
    boolean reorderFarmIds() throws Exception;
}
