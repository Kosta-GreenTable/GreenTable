package site.greentable.service;

import java.util.List;
import site.greentable.dao.CouponDAO;
import site.greentable.dao.CouponDAOImpl;
import site.greentable.dto.CouponDTO;

public class CouponServiceImpl implements CouponService {
    private CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    public List<CouponDTO> getAvailableCouponsByUserId(int userId) {
        try {
            return couponDAO.getAvailableCouponsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CouponDTO> getUsedCouponsByUserId(int userId) {
        try {
            return couponDAO.getUsedCouponsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CouponDTO> getExpiredCouponsByUserId(int userId) {
        try {
            return couponDAO.getExpiredCouponsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean useCoupon(int issuedCouponId, String orderNo) {
        try {
            return couponDAO.useCoupon(issuedCouponId, orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean issueCouponToUser(int userId, int couponId) {
        try {
            return couponDAO.issueCouponToUser(userId, couponId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registerCouponByCode(int userId, String couponCode) {
        try {
            // 쿠폰 코드로 쿠폰 ID 조회 후 발행
            int couponId = couponDAO.getCouponIdByCode(couponCode);
            if (couponId > 0) {
                return couponDAO.issueCouponToUser(userId, couponId);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
