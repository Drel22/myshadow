package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 3/31/2019.
 */

public class Bookmark {

    private String id;
    private String product_id;


    public Bookmark(String id, String product_id) {
        this.id = id;
        this.product_id = product_id;
    }

    public Bookmark() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

}
