package com.example.pegasys.rapmedixuser.activity.pojo;

/**
 * Created by pegasys on 12/5/2017.
 */

public class User {

    private String name = "";
    private String mobile = "";
    private String email = "";
    private String city = "";
    private String uid = "";
    private String id = "";
    private String otp = "";
    private String status = "";


    public User() {

    }

    public User(String name, String mobile, String email, String city, String age,String status) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.status = status;
    }

    public User(String id, String uid, String mobile, String name) {
        this.uid = uid;
        this.mobile = mobile;
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
