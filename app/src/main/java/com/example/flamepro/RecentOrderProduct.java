package com.example.flamepro;

public class RecentOrderProduct {
    private String shopName;
    private String subStatusTime;
    private Product product;

    public RecentOrderProduct(String shopName, String subStatusTime, Product product) {
        this.shopName = shopName;
        this.subStatusTime = subStatusTime;
        this.product = product;
    }

    public String getShopName() { return shopName; }
    public String getSubStatusTime() { return subStatusTime; }
    public Product getProduct() { return product; }
}
