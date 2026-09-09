package com.example.demo.strategy;

public class DiscountContext {
    public  DiscountStrategy strategy;
    public DiscountContext(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateDiscount(double price) {
        return strategy.calculateDiscount(price);
    }

    public String getDiscountName() {
        return strategy.getDiscountName();
    }
}
