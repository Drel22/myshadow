package com.mas.ethan.mas_myshadow.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements  Parcelable{
    private String user_id;
    private String email;
    private String username;
    private String product_id_1;
    private String product_id_2;
    private String product_id_3;
    private String product_id_4;
    private String product_id_5;
    private String selected_skin;

    public User(String user_id, String email, String username, String product_id_1, String product_id_2, String product_id_3, String product_id_4, String product_id_5, String selected_skin) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.product_id_1 = product_id_1;
        this.product_id_2 = product_id_2;
        this.product_id_3 = product_id_3;
        this.product_id_4 = product_id_4;
        this.product_id_5 = product_id_5;
        this.selected_skin = selected_skin;
    }

    public User() {

    }


    protected User(Parcel in) {
        user_id = in.readString();
        email = in.readString();
        username = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(email);
        dest.writeString(username);
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

    public String getSelected_skin() {
        return selected_skin;
    }

    public void setSelected_skin(String selected_skin) {
        this.selected_skin = selected_skin;
    }
}
