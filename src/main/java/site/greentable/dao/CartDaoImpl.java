package site.greentable.dao;

import java.awt.color.ProfileDataException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.CartDTO;
import site.greentable.util.DbUtil;

public class CartDaoImpl implements CartDAO {
	private Properties proFile = new Properties();

	public CartDaoImpl() {
		try {
			// properties 파일 로딩
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<CartDTO> selectCartByUserId(int userId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CartDTO> cartList = new ArrayList<>();
		
		String sql = proFile.getProperty("query.selectCart");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				CartDTO cart = new CartDTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
				
				cartList.add(cart);
			}
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return cartList;
	}

	@Override
	public int insertCart(CartDTO cartdto) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("query.insertCart");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cartdto.getQuantity());
			ps.setInt(2, cartdto.getProductId());
			ps.setInt(3, cartdto.getUserId());
			
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int updateQuantity(CartDTO cartdto) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("query.updateCart");
		
		// SQL 확인을 위한 디버깅 코드
	    System.out.println("updateQuantity SQL: " + sql);
	    System.out.println("quantity: " + cartdto.getQuantity() + ", userId: " + cartdto.getUserId() + ", productId: " + cartdto.getProductId());
	    
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cartdto.getQuantity());
			ps.setInt(2, cartdto.getUserId());
			ps.setInt(3, cartdto.getProductId());
			
			result = ps.executeUpdate();
			System.out.println("Update result: " + result);
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int deleteCart(int userId, int productId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("query.deleteCart");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, productId);
			
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

}
