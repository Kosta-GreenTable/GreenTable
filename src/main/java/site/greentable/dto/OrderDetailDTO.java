package site.greentable.dto;

public class OrderDetailDTO {
	private int orderDetailId;
	private int orderId;
	private int productId;
	private int quantity;
	private int price;
	
	private String productName;
	private int totalPrice; // price * quantity
	
	
	public OrderDetailDTO() {}
	
	// 마이페이지 주문 조회용
	public OrderDetailDTO(int orderDetailId, int productId, String productName, int quantity, int price) {
		super();
		this.orderDetailId = orderDetailId;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}
	
	//상세 주문 조회용
	public OrderDetailDTO(int quantity, int price, String productName) {
		this.quantity = quantity;
		this.price = price;
		this.productName = productName;
	}



	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public int getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
