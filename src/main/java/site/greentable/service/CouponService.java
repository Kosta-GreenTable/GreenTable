package site.greentable.service;

import java.util.List;
import site.greentable.dto.CouponDTO;

public interface CouponService {

    /**
     * 사용자가 보유한 사용 가능한 쿠폰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 사용 가능한 쿠폰 리스트
     */
    List<CouponDTO> getAvailableCouponsByUserId(int userId);

    /**
     * 사용자가 사용한 쿠폰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 사용한 쿠폰 리스트
     */
    List<CouponDTO> getUsedCouponsByUserId(int userId);

    /**
     * 사용자의 만료된 쿠폰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 만료된 쿠폰 리스트
     */
    List<CouponDTO> getExpiredCouponsByUserId(int userId);

    /**
     * 쿠폰을 사용 처리합니다.
     * 
     * @param issuedCouponId 발행된 쿠폰 ID
     * @param orderNo        주문번호
     * @return 사용 처리 성공 여부
     */
    boolean useCoupon(int issuedCouponId, String orderNo);

    /**
     * 사용자에게 쿠폰을 발행합니다.
     * 
     * @param userId   사용자 ID
     * @param couponId 쿠폰 ID
     * @return 발행 성공 여부
     */
    boolean issueCouponToUser(int userId, int couponId);

    /**
     * 쿠폰 코드로 쿠폰을 등록합니다.
     * 
     * @param userId     사용자 ID
     * @param couponCode 쿠폰 코드
     * @return 등록 성공 여부
     */
    boolean registerCouponByCode(int userId, String couponCode);
}
