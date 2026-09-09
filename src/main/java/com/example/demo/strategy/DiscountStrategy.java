package com.example.demo.strategy;

public interface DiscountStrategy {
    double calculateDiscount(double price);
    String getDiscountName();
} 