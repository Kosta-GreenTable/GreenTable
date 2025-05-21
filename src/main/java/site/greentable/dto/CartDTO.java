package site.greentable.dto;

import java.sql.Date;

public class CartDTO {
	private int cartId;
	private int quantity;
	private Date createAt;
	private int productId;
	private int userId;
	
	private String productName;
	private int price;
	private int discountRate;
	private String imageName;
	
	public CartDTO() {}
	
	//장바구니 조회
	public CartDTO(int productId, int quantity, String productName, int price, int discountRate, String imageName) {
		this.productId = productId;
		this.quantity = quantity;
		this.productName = productName;
		this.price = price;
		this.discountRate = discountRate;
		this.imageName = imageName;
	}
	
	//장바구니 등록 (나중에 상품상세랑 연결시 discountRate도 추가하기)
	public CartDTO(int quantity, int productId, int userId) {
		this.quantity = quantity;
		this.productId = productId;
		this.userId = userId;
		//this.discountRate = discountRate;
	}
	
	//주문 정보 저장용
	public CartDTO(String productName, int productId, int price, int discountRate, String imageName) {
		this.productName = productName;
		this.productId = productId;
		this.price = price;
		this.discountRate = discountRate;
		this.imageName = imageName;
	}
	
	
	public CartDTO(int cartId, int quantity, int productId, int userId, String productName, int price,
			int discountRate, String imageName) {
		this.cartId = cartId;
		this.quantity = quantity;
		this.productId = productId;
		this.userId = userId;
		this.productName = productName;
		this.price = price;
		this.discountRate = discountRate;
		this.imageName = imageName;
	}

	public int getCartId() {
		return cartId;
	}
	
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(int discountRate) {
		this.discountRate = discountRate;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	
}
