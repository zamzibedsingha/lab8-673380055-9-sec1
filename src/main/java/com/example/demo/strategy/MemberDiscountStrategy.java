package com.example.demo.strategy;

public class MemberDiscountStrategy implements DiscountStrategy {
    
    @Override 
    public   double calculateDiscount(double price){
        return  price * 0.90;
    }
    @Override 
    public String getDiscountName(){
        return  "Member Discount (10%)";
    }
}
