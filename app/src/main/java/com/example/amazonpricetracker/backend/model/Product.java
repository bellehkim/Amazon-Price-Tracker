package com.example.amazonpricetracker.backend.model;

public class Product {
    public String name;
    public double price;

    public Product(String title, double currentPrice) {
        this.name = title;
        this.price = currentPrice;
    }

    public void printDetails() {
        System.out.println("Title: " + this.name);
    }
}
