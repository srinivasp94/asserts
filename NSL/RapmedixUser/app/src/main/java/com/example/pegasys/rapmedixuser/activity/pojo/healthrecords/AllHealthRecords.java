package com.example.pegasys.rapmedixuser.activity.pojo.healthrecords;

/**
 * Created by pegasys on 2/7/2018.
 */

public class AllHealthRecords {
    private String user_id;
    private String adviser_id;
    private String name;
    private String mobile;
    private String status;
    private String profile_pic;

    private String email;
    private String user_type;

    private String member_type;
    private String payment_status;


    public AllHealthRecords() {
    }

    public AllHealthRecords(String user_id, String adviser_id, String name, String mobile, String email, String user_type, String status, String member_type, String payment_status, String profile_pic) {
        this.user_id = user_id;
        this.adviser_id = adviser_id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.user_type = user_type;
        this.status = status;
        this.member_type = member_type;
        this.payment_status = payment_status;
        this.profile_pic = profile_pic;
    }

    public String getAdviser_id() {
        return adviser_id;
    }

    public void setAdviser_id(String adviser_id) {
        this.adviser_id = adviser_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
