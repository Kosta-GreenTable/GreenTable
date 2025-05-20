package site.greentable.dto;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
	private int orderId;
	private int userId;
	private String merchantUid;
	private String customerName;
	private String customerPhone;
	private String customerEmail;
	private String recipient;
	private String recipientPhone;
	private String zipCode;
	private String address;
	private String addressDetail;
	private String guestPassword;
	private int totalAmount;
	private int usedPoint;
	private String orderStatus;
	private Date orderAt;
	
	private String mainImageName;
	
	private List<OrderDetailDTO> orderDetails;
	
	public OrderDTO() {}
	
	// 주문 목록 조회용 생성자
    public OrderDTO(int orderId, int userId, String merchantUid, int totalAmount, String orderStatus,
                    Date orderAt, String mainImageName, List<OrderDetailDTO> orderDetails) {
        this.orderId = orderId;
        this.userId = userId;
        this.merchantUid = merchantUid;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderAt = orderAt;
        this.mainImageName = mainImageName;
        this.orderDetails = orderDetails;
    }

	
	public String getMainImageName() {
		return mainImageName;
	}

	public void setMainImageName(String mainImageName) {
		this.mainImageName = mainImageName;
	}


	public int getOrderId() {
		return orderId;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMerchantUid() {
		return merchantUid;
	}

	public void setMerchantUid(String merchantUid) {
		this.merchantUid = merchantUid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getGuestPassword() {
		return guestPassword;
	}

	public void setGuestPassword(String guestPassword) {
		this.guestPassword = guestPassword;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getUsedPoint() {
		return usedPoint;
	}

	public void setUsedPoint(int usedPoint) {
		this.usedPoint = usedPoint;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}



	
}
