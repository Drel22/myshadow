package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 3/22/2019.
 */

public class Swatch {

    private String id;
    private String swatch_name;
    private String img_url;
    private String color;
    private String user_id;


    public Swatch(String id, String swatch_name, String img_url, String color, String user_id) {
        this.id = id;
        this.swatch_name = swatch_name;
        this.img_url = img_url;
        this.color = color;
        this.user_id = user_id;
    }

    public Swatch() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSwatch_name() {
        return swatch_name;
    }

    public void setSwatch_name(String product_name) {
        this.swatch_name = product_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}