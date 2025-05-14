package site.greentable.dto;

public class UserDTO {
	private int userId;
	private String email;
	private String password;
	private String status;
	private String userType;
	private String provider;
	private String oauthId;
	private String createdAt;
	private String lastLogin;
	
	private UserInfoDTO userInfoDto;
	

	public UserDTO() {

	}

	public UserDTO(int userId, String email, String password, String status, String userType, String provider,
			String oauthId, String createdAt, String lastLogin) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.status = status;
		this.userType = userType;
		this.provider = provider;
		this.oauthId = oauthId;
		this.createdAt = createdAt;
		this.lastLogin = lastLogin;
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOauthId() {
		return oauthId;
	}

	public void setOauthId(String oauthId) {
		this.oauthId = oauthId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public UserInfoDTO getUserInfoDto() {
		return userInfoDto;
	}

	public void setUserInfoDto(UserInfoDTO userInfoDto) {
		this.userInfoDto = userInfoDto;
	}
	
	
	

}
