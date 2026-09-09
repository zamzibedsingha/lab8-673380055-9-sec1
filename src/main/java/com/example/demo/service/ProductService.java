package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;
import com.example.demo.strategy.MemberDiscountStrategy;
import com.example.demo.strategy.NoDiscountStrategy;
import com.example.demo.strategy.SeasonalSaleStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getAllProducts() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            calculateDiscountedPrice(product);
        }
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public Product saveProduct(Product product) {

    if (product.getDetail() != null) {
        product.getDetail().setProduct(product);
    }

    if (product.getReviews() != null) {
        for (Review review : product.getReviews()) {
            review.setProduct(product);

            if (review.getReviewDate() == null) {
                review.setReviewDate(LocalDate.now());
            }
        }
    }

    return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void calculateDiscountedPrice(Product product) {

        DiscountContext context;

        if ("MEMBER".equalsIgnoreCase(product.getDiscountType())) {

            context = new DiscountContext(
                    new MemberDiscountStrategy()
            );

        } else if ("SEASONAL".equalsIgnoreCase(product.getDiscountType())) {

            context = new DiscountContext(
                    new SeasonalSaleStrategy()
            );

        } else {

            context = new DiscountContext(
                    new NoDiscountStrategy()
            );
        }

        double discountedPrice =
                context.calculateDiscount(product.getPrice());

        product.setDiscountedPrice(discountedPrice);
    }
}