package com.example.demo.strategy;

public class SeasonalSaleStrategy implements  DiscountStrategy{
    @Override
    public double calculateDiscount(double price) {
        return price * 0.80;
    }

    @Override
    public String getDiscountName() {
        return "Seasonal Sale (20%)";
    }
}
