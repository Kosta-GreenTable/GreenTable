package site.greentable.service;

import java.util.List;
import site.greentable.dao.PointDAO;
import site.greentable.dao.PointDAOImpl;
import site.greentable.dao.UserDAO;
import site.greentable.dao.UserDAOImpl;
import site.greentable.dto.PointHistoryDTO;

public class PointServiceImpl implements PointService {
    private PointDAO pointDAO = new PointDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public List<PointHistoryDTO> getPointHistoryByUserId(int userId) {
        try {
            return pointDAO.getPointHistoryByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean earnPoints(int userId, int pointAmount, String description, String orderNo) {
        try {
            // 현재 포인트 잔액 조회
            int currentBalance = getCurrentPointBalance(userId);
            int newBalance = currentBalance + pointAmount;

            // 포인트 내역 추가
            PointHistoryDTO pointHistory = new PointHistoryDTO(userId, "EARN", pointAmount,
                    description, newBalance, orderNo);
            boolean historyAdded = pointDAO.insertPointHistory(pointHistory);

            if (historyAdded) {
                // 사용자 포인트 잔액 업데이트
                return userDAO.updateUserPoint(userId, newBalance);
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean usePoints(int userId, int pointAmount, String description, String orderNo) {
        try {
            // 현재 포인트 잔액 조회
            int currentBalance = getCurrentPointBalance(userId);

            // 포인트 부족 체크
            if (currentBalance < pointAmount) {
                return false; // 포인트 부족
            }

            int newBalance = currentBalance - pointAmount;

            // 포인트 내역 추가
            PointHistoryDTO pointHistory = new PointHistoryDTO(userId, "USE", pointAmount,
                    description, newBalance, orderNo);
            boolean historyAdded = pointDAO.insertPointHistory(pointHistory);

            if (historyAdded) {
                // 사용자 포인트 잔액 업데이트
                return userDAO.updateUserPoint(userId, newBalance);
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getCurrentPointBalance(int userId) {
        try {
            return userDAO.getUserPointBalance(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
