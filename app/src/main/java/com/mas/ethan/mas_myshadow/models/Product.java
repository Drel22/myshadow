package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 3/1/2019.
 */

public class Product {

    private String id;
    private String product_name;
    private String brand;
    private String img_url;
    private String price;
    private int rating;


    public Product(String id, String product_name, String brand, String img_url, String price, int rating) {
        this.id = id;
        this.product_name = product_name;
        this.brand = brand;
        this.img_url = img_url;
        this.price = price;
        this.rating = rating;
    }

    public Product() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
