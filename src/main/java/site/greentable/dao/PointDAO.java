package site.greentable.dao;

import java.util.List;
import site.greentable.dto.PointHistoryDTO;

public interface PointDAO {

    /**
     * 사용자의 포인트 내역을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 포인트 내역 리스트 (최신순)
     */
    List<PointHistoryDTO> getPointHistoryByUserId(int userId) throws Exception;

    /**
     * 포인트 내역을 추가합니다.
     * 
     * @param pointHistory 포인트 내역 정보
     * @return 추가 성공 여부
     */
    boolean insertPointHistory(PointHistoryDTO pointHistory) throws Exception;

    /**
     * 특정 주문의 포인트 내역을 조회합니다.
     * 
     * @param orderNo 주문번호
     * @return 포인트 내역 리스트
     */
    List<PointHistoryDTO> getPointHistoryByOrderNo(String orderNo) throws Exception;
}
