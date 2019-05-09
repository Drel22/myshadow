package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 3/31/2019.
 */


public class Skin {

    private String id;
    private String name;
    private String season;
    private String type;
    private String color;


    public Skin(String id, String name, String season, String type, String color) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.type = type;
        this.color = color;
    }

    public Skin() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}