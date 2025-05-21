package site.greentable.dao;

import site.greentable.dto.QnaDTO;
import site.greentable.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;

public class QnaDAOImpl implements QnaDAO {
    private Properties proFile;

    public QnaDAOImpl() {
        try {
            proFile = new Properties();

            // 다양한 방법으로 properties 파일 로드 시도
            boolean loaded = false;

            // 1. 클래스로더를 사용하여 파일 로드 시도
            ClassLoader classLoader = getClass().getClassLoader();
            if (classLoader != null) {
                try (java.io.InputStream is = classLoader.getResourceAsStream("dbQuery.properties")) {
                    if (is != null) {
                        proFile.load(is);
                        loaded = true;
                        System.out.println("클래스로더로 dbQuery.properties 로드 성공");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 2. 첫 번째 방법이 실패했을 경우 상대 경로에서 로드 시도
            if (!loaded) {
                try {
                    proFile.load(new FileInputStream("resources/dbQuery.properties"));
                    loaded = true;
                    System.out.println("상대 경로로 dbQuery.properties 로드 성공");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 3. 두 번째 방법도 실패했을 경우 build 폴더에서 로드 시도
            if (!loaded) {
                try {
                    proFile.load(new FileInputStream("build/classes/dbQuery.properties"));
                    loaded = true;
                    System.out.println("build 경로에서 dbQuery.properties 로드 성공");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 로드 상태 출력
            System.out.println("properties 파일 로드 " + (loaded ? "성공" : "실패"));
            System.out.println("properties 항목 수: " + proFile.size());

            // 직접 필요한 SQL 쿼리들 설정
            if (!proFile.containsKey("qna.insert")) {
                proFile.setProperty("qna.insert",
                        "INSERT INTO product_qna (title, content, user_id, product_id) VALUES (?, ?, ?, ?)");
                System.out.println("qna.insert 속성 직접 설정");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertQna(QnaDTO qna) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            // 디버그 출력
            System.out.println("qna.insert 속성값: "
                    + (proFile.getProperty("qna.insert") != null ? proFile.getProperty("qna.insert") : "NULL"));
            System.out.println("proFile 로드됨?: " + (proFile != null ? "예" : "아니요"));
            System.out.println("proFile 크기: " + proFile.size());

            // SQL 쿼리 가져오기
            String sql = proFile.getProperty("qna.insert");

            // SQL이 null인 경우 직접 정의
            if (sql == null) {
                sql = "INSERT INTO product_qna (title, content, user_id, product_id) VALUES (?, ?, ?, ?)";
                System.out.println("기본 SQL 쿼리 사용: " + sql);
            }

            ps = con.prepareStatement(sql);

            ps.setString(1, qna.getTitle());
            ps.setString(2, qna.getContent());
            ps.setInt(3, qna.getUserId());
            ps.setInt(4, qna.getProductId());

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public List<QnaDTO> getProductQnas(int productId, int offset, int limit) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<QnaDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.getByProduct"));

            ps.setInt(1, productId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            rs = ps.executeQuery();

            while (rs.next()) {
                QnaDTO qna = new QnaDTO();
                qna.setQnaId(rs.getInt("qna_id"));
                qna.setProductId(rs.getInt("product_id"));
                qna.setUserId(rs.getInt("user_id"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setAnswer(rs.getString("answer"));
                qna.setIsAnswered(rs.getString("is_answered"));
                qna.setCreatedAt(rs.getTimestamp("created_at"));
                qna.setAnsweredAt(rs.getTimestamp("answered_at"));
                qna.setUserName(rs.getString("user_name"));

                list.add(qna);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<QnaDTO> getUserQnas(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<QnaDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.getByUser"));

            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                QnaDTO qna = new QnaDTO();
                qna.setQnaId(rs.getInt("qna_id"));
                qna.setProductId(rs.getInt("product_id"));
                qna.setUserId(rs.getInt("user_id"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setAnswer(rs.getString("answer"));
                qna.setIsAnswered(rs.getString("is_answered"));
                qna.setCreatedAt(rs.getTimestamp("created_at"));
                qna.setAnsweredAt(rs.getTimestamp("answered_at"));
                qna.setProductName(rs.getString("product_name"));
                qna.setProductImage(rs.getString("product_image"));

                list.add(qna);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<QnaDTO> getUserQnasWithFilter(int userId, int period, String status) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<QnaDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.getByUserWithFilter"));

            ps.setInt(1, userId);
            ps.setInt(2, period);
            ps.setString(3, status);

            if (!"all".equals(status)) {
                ps.setString(4, status);
            } else {
                ps.setString(4, "");
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                QnaDTO qna = new QnaDTO();
                qna.setQnaId(rs.getInt("qna_id"));
                qna.setProductId(rs.getInt("product_id"));
                qna.setUserId(rs.getInt("user_id"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setAnswer(rs.getString("answer"));
                qna.setIsAnswered(rs.getString("is_answered"));
                qna.setCreatedAt(rs.getTimestamp("created_at"));
                qna.setAnsweredAt(rs.getTimestamp("answered_at"));
                qna.setProductName(rs.getString("product_name"));
                qna.setProductImage(rs.getString("product_image"));

                list.add(qna);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public QnaDTO getQna(int qnaId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        QnaDTO qna = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.getById"));

            ps.setInt(1, qnaId);

            rs = ps.executeQuery();

            if (rs.next()) {
                qna = new QnaDTO();
                qna.setQnaId(rs.getInt("qna_id"));
                qna.setProductId(rs.getInt("product_id"));
                qna.setUserId(rs.getInt("user_id"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setAnswer(rs.getString("answer"));
                qna.setIsAnswered(rs.getString("is_answered"));
                qna.setCreatedAt(rs.getTimestamp("created_at"));
                qna.setAnsweredAt(rs.getTimestamp("answered_at"));
            }

            return qna;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public void updateQna(QnaDTO qna) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.update"));

            ps.setString(1, qna.getTitle());
            ps.setString(2, qna.getContent());
            ps.setInt(3, qna.getQnaId());
            ps.setInt(4, qna.getUserId());

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public void deleteQna(int qnaId, int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.delete"));

            ps.setInt(1, qnaId);
            ps.setInt(2, userId); // 두 번째 파라미터 설정

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public int getQnaCount(int productId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.count"));

            ps.setInt(1, productId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<QnaDTO> getProductList(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<QnaDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("qna.getProductList"));

            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                QnaDTO qna = new QnaDTO();
                qna.setProductId(rs.getInt("product_id"));
                qna.setProductName(rs.getString("name"));

                list.add(qna);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }
}