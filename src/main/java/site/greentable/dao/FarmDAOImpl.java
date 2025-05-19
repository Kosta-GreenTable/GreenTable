package site.greentable.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.Farm;
import site.greentable.dto.Product;
import site.greentable.util.DbUtil;

public class FarmDAOImpl implements FarmDAO {
    private Properties proFile = new Properties();

    public FarmDAOImpl() {
        try {
            // properties 파일 로딩
            InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
            if (is != null) {
                proFile.load(is);
                System.out.println("FarmDAOImpl: dbQuery.properties 로드 성공");
            } else {
                System.out.println("경고: dbQuery.properties 파일을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Farm> selectAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Farm> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            // 관리자용 목록 조회일 때는 모든 농가를 가져오기 위해 다른 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getAdminList"));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Farm farm = new Farm();
                farm.setFarmId(rs.getInt("farm_id"));
                farm.setName(rs.getString("name"));
                farm.setDescription(rs.getString("description"));
                farm.setAddress(rs.getString("address"));
                farm.setFarmImg(rs.getString("farm_img"));
                farm.setLatitude(rs.getDouble("latitude"));
                farm.setLongitude(rs.getDouble("longitude"));
                farm.setContractStatus(rs.getString("contract_status"));

                // 카테고리 필드가 있는지 확인
                try {
                    farm.setCategory(rs.getString("category"));
                } catch (SQLException e) {
                    farm.setCategory("일반"); // 기본값 설정
                }

                list.add(farm);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    // 활성 상태 농가만 조회하는 메소드 구현
    @Override
    public List<Farm> selectActiveFarms() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Farm> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            // 프로퍼티 파일의 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.selectActiveFarms"));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Farm farm = new Farm();
                farm.setFarmId(rs.getInt("farm_id"));
                farm.setName(rs.getString("name"));
                farm.setDescription(rs.getString("description"));
                farm.setAddress(rs.getString("address"));
                farm.setFarmImg(rs.getString("farm_img"));
                farm.setLatitude(rs.getDouble("latitude"));
                farm.setLongitude(rs.getDouble("longitude"));
                farm.setContractStatus(rs.getString("contract_status"));

                // 카테고리 필드가 있는지 확인
                try {
                    farm.setCategory(rs.getString("category"));
                } catch (SQLException e) {
                    farm.setCategory("일반"); // 기본값 설정
                }

                list.add(farm);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public int getTotalFarmCount() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            // 프로퍼티 파일의 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getTotalFarmCount"));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public List<Farm> getFarmsByPage(int startIndex, int count) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Farm> farmList = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            // 프로퍼티 파일의 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getFarmsByPage"));
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, count);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Farm farm = new Farm();
                farm.setFarmId(rs.getInt("farm_id"));
                farm.setName(rs.getString("name"));
                farm.setDescription(rs.getString("description"));
                farm.setAddress(rs.getString("address"));
                farm.setFarmImg(rs.getString("farm_img"));
                farm.setLatitude(rs.getDouble("latitude"));
                farm.setLongitude(rs.getDouble("longitude"));
                farm.setContractStatus(rs.getString("contract_status"));

                // 카테고리 필드가 있는지 확인
                try {
                    farm.setCategory(rs.getString("category"));
                } catch (SQLException e) {
                    farm.setCategory("일반"); // 기본값 설정
                }

                farmList.add(farm);
            }
        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return farmList;
    }

    @Override
    public boolean deleteFarm(int farmId) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            // 프로퍼티 파일의 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.deleteFarm"));
            pstmt.setInt(1, farmId);
            result = pstmt.executeUpdate();
        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result > 0;
    }

    @Override
    public Farm selectFarmDetail(int farmId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Farm farm = null;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getDetail"));
            pstmt.setInt(1, farmId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                farm = new Farm();
                farm.setFarmId(rs.getInt("farm_id"));
                farm.setName(rs.getString("name"));
                farm.setDescription(rs.getString("description"));
                farm.setAddress(rs.getString("address"));
                farm.setFarmImg(rs.getString("farm_img"));
                farm.setLatitude(rs.getDouble("latitude"));
                farm.setLongitude(rs.getDouble("longitude"));
                farm.setContractStatus(rs.getString("contract_status"));

                // 카테고리 필드가 있는지 확인
                try {
                    farm.setCategory(rs.getString("category"));
                } catch (SQLException e) {
                    farm.setCategory("일반"); // 기본값 설정
                }
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return farm;
    }

    @Override
    public List<Product> selectFarmProducts(int farmId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getFarmProducts"));
            pstmt.setInt(1, farmId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSubName(rs.getString("sub_name"));
                product.setPrice(rs.getInt("price"));
                product.setDiscountRate(rs.getInt("discount_rate"));
                product.setCategory(rs.getString("category"));
                product.setMainImageName(rs.getString("image_name"));

                products.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return products;
    }

    @Override
    public int insertFarm(Farm farm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = 0;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("farm.insertFarm"), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, farm.getName());
            pstmt.setString(2, farm.getDescription());
            pstmt.setString(3, farm.getAddress());
            pstmt.setString(4, farm.getFarmImg());
            pstmt.setDouble(5, farm.getLatitude());
            pstmt.setDouble(6, farm.getLongitude());
            pstmt.setString(7, farm.getContractStatus());
            pstmt.setString(8, farm.getCategory()); // 카테고리 추가

            int result = pstmt.executeUpdate();

            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return generatedId;
    }

    @Override
    public int updateFarm(Farm farm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("farm.updateFarm"));
            pstmt.setString(1, farm.getName());
            pstmt.setString(2, farm.getDescription());
            pstmt.setString(3, farm.getAddress());
            pstmt.setString(4, farm.getFarmImg());
            pstmt.setDouble(5, farm.getLatitude());
            pstmt.setDouble(6, farm.getLongitude());
            pstmt.setString(7, farm.getContractStatus());
            pstmt.setString(8, farm.getCategory()); // 카테고리 추가
            pstmt.setInt(9, farm.getFarmId());

            result = pstmt.executeUpdate();

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int updateFarmStatus(int farmId, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("farm.updateFarmStatus"));
            pstmt.setString(1, status);
            pstmt.setInt(2, farmId);

            result = pstmt.executeUpdate();

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    // getFarmById 메서드 구현 - 프로퍼티 파일의 쿼리 사용
    @Override
    public Farm getFarmById(int farmId) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Farm farm = null;

        try {
            conn = DbUtil.getConnection();
            // 프로퍼티 파일의 쿼리 사용
            pstmt = conn.prepareStatement(proFile.getProperty("farm.getFarmById"));
            pstmt.setInt(1, farmId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                farm = new Farm();
                farm.setFarmId(rs.getInt("farm_id"));
                farm.setName(rs.getString("name"));
                farm.setDescription(rs.getString("description"));
                farm.setAddress(rs.getString("address"));
                farm.setFarmImg(rs.getString("farm_img"));
                farm.setLatitude(rs.getDouble("latitude"));
                farm.setLongitude(rs.getDouble("longitude"));
                farm.setContractStatus(rs.getString("contract_status"));

                // 카테고리 필드가 있는지 확인
                try {
                    farm.setCategory(rs.getString("category"));
                } catch (SQLException e) {
                    farm.setCategory("일반"); // 기본값 설정
                }
            }
        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return farm;
    }

    @Override
    public boolean reorderFarmIds() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;

        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 카운터 변수 초기화
            pstmt1 = conn.prepareStatement(proFile.getProperty("farm.setVariable"));
            pstmt1.executeUpdate();

            // 2. ID 재정렬
            pstmt2 = conn.prepareStatement(proFile.getProperty("farm.reorderIds"));
            pstmt2.executeUpdate();

            // 3. AUTO_INCREMENT 값 재설정
            pstmt3 = conn.prepareStatement(proFile.getProperty("farm.resetAutoIncrement"));
            pstmt3.executeUpdate();

            conn.commit(); // 트랜잭션 커밋
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 오류 발생 시 롤백
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 자동 커밋 모드 복원
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            DbUtil.dbClose(null, pstmt3);
            DbUtil.dbClose(null, pstmt2);
            DbUtil.dbClose(conn, pstmt1);
        }
    }
}