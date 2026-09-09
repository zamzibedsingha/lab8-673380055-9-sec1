package com.example.demo.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity 
@Table(name = "products")
public class Product {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;
    private String category;
    private String brand;
    private Integer stock;
    private Double price;
    private String discountType;
    
    @Transient
    private Double discountedPrice;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_id", 
                referencedColumnName = "id")
    private ProductDetail detail;

    @OneToMany(mappedBy = "product", 
                cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Product() {
    }

    public ProductDetail getDetail() {
        return detail;
    }

    public void setDetail(ProductDetail detail) {
        this.detail = detail;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

}
