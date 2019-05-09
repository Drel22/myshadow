package com.mas.ethan.mas_myshadow.models;

/**
 * Created by Ethan on 4/2/2019.
 */

public class Recent {

    private String id;
    private String product_id_1;
    private String product_id_2;
    private String product_id_3;
    private String product_id_4;
    private String product_id_5;


    public Recent(String id, String product_id_1, String product_id_2, String product_id_3, String product_id_4, String product_id_5) {
        this.id = id;
        this.product_id_1 = product_id_1;
        this.product_id_2 = product_id_2;
        this.product_id_3 = product_id_3;
        this.product_id_4 = product_id_4;
        this.product_id_5 = product_id_5;
    }

    public Recent() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id_1() {
        return product_id_1;
    }

    public void setProduct_id_1(String product_id_1) {
        this.product_id_1 = product_id_1;
    }

    public String getProduct_id_2() {
        return product_id_2;
    }

    public void setProduct_id_2(String product_id_2) {
        this.product_id_2 = product_id_2;
    }

    public String getProduct_id_3() {
        return product_id_3;
    }

    public void setProduct_id_3(String product_id_3) {
        this.product_id_3 = product_id_3;
    }

    public String getProduct_id_4() {
        return product_id_4;
    }

    public void setProduct_id_4(String product_id_4) {
        this.product_id_4 = product_id_4;
    }

    public String getProduct_id_5() {
        return product_id_5;
    }

    public void setProduct_id_5(String product_id_5) {
        this.product_id_5 = product_id_5;
    }

}