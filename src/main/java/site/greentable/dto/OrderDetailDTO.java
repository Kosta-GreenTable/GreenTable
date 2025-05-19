package site.greentable.dto;

public class OrderDetailDTO {
	private int orderDetailId;
	private int orderId;
	private int productId;
	private String productName;
	private int quantity;
	private int price;
	private int totalPrice; // price * quantity
	
	
	public OrderDetailDTO() {}


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
