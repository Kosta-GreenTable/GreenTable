package site.greentable.dto;

public class UserInfoDTO {
	private int userId;
	private String userName;
	private String phone;
	private int zipCode;
	private String address;
	private String detailAddress;
	private int orderCount;
	private int totalAmount;
	private String userGrade;
	private int point;
	
	public UserInfoDTO() {
		// TODO Auto-generated constructor stub
	}

	public UserInfoDTO(int userId, String userName, String phone, int zipCode, String address, String detailAddress,
			int orderCount, int totalAmount, String userGrade, int point) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.phone = phone;
		this.zipCode = zipCode;
		this.address = address;
		this.detailAddress = detailAddress;
		this.orderCount = orderCount;
		this.totalAmount = totalAmount;
		this.userGrade = userGrade;
		this.point = point;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	
	
}
