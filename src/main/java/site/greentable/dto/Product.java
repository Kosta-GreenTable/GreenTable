package site.greentable.dto;

import java.util.List;

public class Product {
    private int productId;
    private String name;
    private String subName;
    private int price;
    private int stock;
    private String category;
    private int discountRate;
    private ProductDetail productDetail;
    private List<ProductImage> productImages;
    private String mainImageName;
    // 추가 필드
    private double rating;
    private int reviewCount;
    private int popularity;
    private String createDate;
    private String description;
    private String thumbnailUrl;

    public Product() {
    }

    public Product(int productId, String name, String subName, int price, int stock, String category,
            int discountRate) {
        this.productId = productId;
        this.name = name;
        this.subName = subName;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.discountRate = discountRate;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getMainImageName() {
        return mainImageName;
    }

    public void setMainImageName(String mainImageName) {
        this.mainImageName = mainImageName;
    }

    // 호환성을 위한 mainImage getter 추가 - JSP에서 product.mainImage를 사용하는 경우
    public String getMainImage() {
        return mainImageName; // mainImageName을 반환하여 기존 코드와 호환
    }

    // Calculate discounted price
    public int getDiscountedPrice() {
        return price - (price * discountRate / 100);
    }

    // New getters and setters for additional fields
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    // Utility methods
    public String getFormattedPrice() {
        return String.format("%,d", getDiscountedPrice());
    }

    // For JSP compatibility - return ID from either method name
    public int getId() {
        return productId;
    }
}