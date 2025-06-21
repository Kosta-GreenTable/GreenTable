package site.greentable.service;

import java.util.List;
import site.greentable.dto.PointHistoryDTO;

public interface PointService {

    /**
     * 사용자의 포인트 내역을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 포인트 내역 리스트
     */
    List<PointHistoryDTO> getPointHistoryByUserId(int userId);

    /**
     * 포인트를 적립합니다.
     * 
     * @param userId      사용자 ID
     * @param pointAmount 적립할 포인트 금액
     * @param description 적립 사유
     * @param orderNo     관련 주문번호 (선택)
     * @return 적립 성공 여부
     */
    boolean earnPoints(int userId, int pointAmount, String description, String orderNo);

    /**
     * 포인트를 사용합니다.
     * 
     * @param userId      사용자 ID
     * @param pointAmount 사용할 포인트 금액
     * @param description 사용 사유
     * @param orderNo     관련 주문번호 (선택)
     * @return 사용 성공 여부
     */
    boolean usePoints(int userId, int pointAmount, String description, String orderNo);

    /**
     * 사용자의 현재 포인트 잔액을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 현재 포인트 잔액
     */
    int getCurrentPointBalance(int userId);
}
