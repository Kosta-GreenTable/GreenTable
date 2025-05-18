package site.greentable.dao;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.Farm;
import site.greentable.dto.Product;

/**
 * 농가 관련 데이터 액세스 인터페이스
 */
public interface FarmDAO {
    // 모든 농가 목록 조회
    List<Farm> selectAll() throws SQLException;
    
    // 활성 상태 농가만 조회
    List<Farm> selectActiveFarms() throws SQLException;
    
    // ID로 농가 조회
    Farm selectFarmDetail(int farmId) throws SQLException;
    
    // 농가별 상품 조회
    List<Product> selectFarmProducts(int farmId) throws SQLException;
    
    // 농가 등록
    int insertFarm(Farm farm) throws SQLException;
    
    // 농가 수정
    int updateFarm(Farm farm) throws SQLException;
    
    // 농가 상태 변경
    int updateFarmStatus(int farmId, String status) throws SQLException;
    
    // 농가 ID로 조회
    Farm getFarmById(int farmId) throws Exception;
    
    // 전체 농가 수 조회
    int getTotalFarmCount() throws Exception;
    
    // 페이징 처리된 농가 목록 조회
    List<Farm> getFarmsByPage(int startIndex, int count) throws Exception;
    
    // 농가 삭제
    boolean deleteFarm(int farmId) throws Exception;

    // FarmDAO.java에 아래 메소드 추가
    boolean reorderFarmIds() throws Exception;
}