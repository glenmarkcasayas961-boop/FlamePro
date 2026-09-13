package com.example.flamepro;

import java.util.List;

public class Product {
    private String name;
    private String price;
    private String originalPrice;
    private String discount;
    private float rating;
    private int reviews;
    private int imageResource;
    private List<Integer> carouselImages;
    private String weight;
    private String type;
    private String coverage;
    private List<String> keyFeatures;
    private String categoryTag;
    private boolean inStock;

    public Product(String name, String price, String originalPrice, String discount, 
                   float rating, int reviews, int imageResource, List<Integer> carouselImages,
                   String weight, String type, String coverage, List<String> keyFeatures,
                   String categoryTag, boolean inStock) {
        this.name = name;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.rating = rating;
        this.reviews = reviews;
        this.imageResource = imageResource;
        this.carouselImages = carouselImages;
        this.weight = weight;
        this.type = type;
        this.coverage = coverage;
        this.keyFeatures = keyFeatures;
        this.categoryTag = categoryTag;
        this.inStock = inStock;
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getOriginalPrice() { return originalPrice; }
    public String getDiscount() { return discount; }
    public float getRating() { return rating; }
    public int getReviews() { return reviews; }
    public int getImageResource() { return imageResource; }
    public List<Integer> getCarouselImages() { return carouselImages; }
    public String getWeight() { return weight; }
    public String getType() { return type; }
    public String getCoverage() { return coverage; }
    public List<String> getKeyFeatures() { return keyFeatures; }
    public String getCategoryTag() { return categoryTag; }
    public boolean isInStock() { return inStock; }
}
