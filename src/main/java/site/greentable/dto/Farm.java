package site.greentable.dto;

public class Farm {
    private int farmId;
    private String name;
    private String description;
    private String address;
    private String farmImg;
    private double latitude;
    private double longitude;
    private String contractStatus;
    // 추가한 필드
    private String category;
    private String detailDescription;
    private String mainProducts;
    
    // Default constructor
    public Farm() {}
    
    // Constructor with fields
    public Farm(int farmId, String name, String description, String address, 
               String farmImg, double latitude, double longitude, String contractStatus) {
        this.farmId = farmId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.farmImg = farmImg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contractStatus = contractStatus;
    }
    
    // Getters and Setters
    public int getFarmId() {
        return farmId;
    }
    
    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getFarmImg() {
        return farmImg;
    }
    
    public void setFarmImg(String farmImg) {
        this.farmImg = farmImg;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getContractStatus() {
        return contractStatus;
    }
    
    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }
    
    // Category 에 대한 getter/setter 추가
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    // 추가한 필드
    public String getDetailDescription() {
        return detailDescription;
    }
    
    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }
    
    public String getMainProducts() {
        return mainProducts;
    }
    
    public void setMainProducts(String mainProducts) {
        this.mainProducts = mainProducts;
    }
}