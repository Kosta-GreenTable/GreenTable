package site.greentable.dto;

import java.util.Date;

public class ProductDetail {
    private int productId;
    private String description;
    private String ingredients;
    private int kcal;
    private int amount;
    private String nutrition;
    private Date createdDate;
    private Date updatedDate;
    
    public ProductDetail() {}
    
    public ProductDetail(int productId, String description, String ingredients, int kcal, 
                         int amount, String nutrition, Date createdDate, Date updatedDate) {
        this.productId = productId;
        this.description = description;
        this.ingredients = ingredients;
        this.kcal = kcal;
        this.amount = amount;
        this.nutrition = nutrition;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    public int getKcal() {
        return kcal;
    }
    
    public void setKcal(int kcal) {
        this.kcal = kcal;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public String getNutrition() {
        return nutrition;
    }
    
    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getUpdatedDate() { 
        return updatedDate;
    }
    
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}