package site.greentable.service;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dao.FarmDAO;
import site.greentable.dao.FarmDAOImpl;
import site.greentable.dto.Farm;
import site.greentable.dto.Product;

/**
 * 농가 관련 서비스 구현 클래스
 */
public class FarmServiceImpl implements FarmService {
    private FarmDAO farmDAO;

    public FarmServiceImpl() {
        farmDAO = new FarmDAOImpl();
    }

    @Override
    public List<Farm> getAllFarms() throws SQLException {
        return farmDAO.selectAll();
    }

    // getFarmById 메서드 구현
    @Override
    public Farm getFarmById(int farmId) throws Exception {
        return farmDAO.getFarmById(farmId);
    }

    @Override
    public int getTotalFarmCount() throws Exception {
        return farmDAO.getTotalFarmCount();
    }

    @Override
    public List<Farm> getFarmsByPage(int startIndex, int count) throws Exception {
        return farmDAO.getFarmsByPage(startIndex, count);
    }

    @Override
    public boolean deleteFarm(int farmId) throws Exception {
        return farmDAO.deleteFarm(farmId);
    }

    @Override
    public Farm getFarmDetail(int farmId) throws SQLException {
        return farmDAO.selectFarmDetail(farmId);
    }

    @Override
    public List<Farm> getActiveFarms() throws Exception {
        return farmDAO.selectActiveFarms();
    }

    @Override
    public List<Product> getFarmProducts(int farmId) throws SQLException {
        return farmDAO.selectFarmProducts(farmId);
    }

    @Override
    public int registerFarm(Farm farm) throws SQLException {
        return farmDAO.insertFarm(farm);
    }

    @Override
    public int updateFarm(Farm farm) throws SQLException {
        return farmDAO.updateFarm(farm);
    }

    @Override
    public int updateFarmStatus(int farmId, String status) throws SQLException {
        return farmDAO.updateFarmStatus(farmId, status);
    }

    @Override
    public boolean reorderFarmIds() throws Exception {
        return farmDAO.reorderFarmIds();
    }
}
