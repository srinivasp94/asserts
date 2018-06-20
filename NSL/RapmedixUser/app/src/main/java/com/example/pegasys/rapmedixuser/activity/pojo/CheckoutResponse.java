package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/18/2017.
 */

public class CheckoutResponse {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("newregistrarion")
    @Expose
    public String newregistrarion;
    @SerializedName("referee")
    @Expose
    public String referee;
    @SerializedName("referral")
    @Expose
    public String referral;
    @SerializedName("addfamilymember")
    @Expose
    public String addfamilymember;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("consultation")
    @Expose
    public String consultation;
    @SerializedName("consultation_service_tax")
    @Expose
    public Double consultationServiceTax;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("hospital_name")
    @Expose
    public String hospitalName;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("geolocation")
    @Expose
    public String geolocation;
    @SerializedName("user_freecoupon")
    @Expose
    public Integer userFreecoupon;
    @SerializedName("res_status")
    @Expose
    public String resStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("newregistrarion", newregistrarion).append("referee", referee).append("referral", referral).append("addfamilymember", addfamilymember).append("status", status).append("consultation", consultation).append("consultationServiceTax", consultationServiceTax).append("name", name).append("profilePic", profilePic).append("hospitalName", hospitalName).append("location", location).append("geolocation", geolocation).append("userFreecoupon", userFreecoupon).append("resStatus", resStatus).toString();
    }
}
