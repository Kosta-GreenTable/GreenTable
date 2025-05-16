package site.greentable.dto;

public class ProductImage {
    private int productImageId;
    private String imageName;
    private boolean isMain;
    private int productId;
    
    public ProductImage() {}
    
    public ProductImage(int productImageId, String imageName, boolean isMain, int productId) {
        this.productImageId = productImageId;
        this.imageName = imageName;
        this.isMain = isMain;
        this.productId = productId;
    }
    
    // Getters and Setters
    public int getProductImageId() {
        return productImageId;
    }
    
    public void setProductImageId(int productImageId) {
        this.productImageId = productImageId;
    }
    
    public String getImageName() {
        return imageName;
    }
    
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    public boolean isMain() {
        return isMain;
    }
    
    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
}