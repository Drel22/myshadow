package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 4/2/2019.
 */

public class Like {

    private String id;
    private String swatch_id;


    public Like(String id, String swatch_id) {
        this.id = id;
        this.swatch_id = swatch_id;
    }

    public Like() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSwatch_id() {
        return swatch_id;
    }

    public void setSwatch_id(String swatch_id) {
        this.swatch_id = swatch_id;
    }

}
