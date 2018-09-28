package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * Created by pegasys on 12/6/2017.
 */

public class Otpresponse {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("membership_type")
    @Expose
    public String membershipType;
    @SerializedName("modified_date")
    @Expose
    public String modifiedDate;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("registration_id")
    @Expose
    public String registrationId;
    @SerializedName("otp_status")
    @Expose
    public String otpStatus;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("membership_id")
    @Expose
    public Object membershipId;
    @SerializedName("adviser_id")
    @Expose
    public String adviserId;
    @SerializedName("member_type")
    @Expose
    public String memberType;
    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;
    @SerializedName("profile_pin")
    @Expose
    public String profilePin;
    @SerializedName("gcm_id")
    @Expose
    public String gcmId;
    @SerializedName("device_id")
    @Expose
    public String deviceId;
    @SerializedName("referral")
    @Expose
    public String referral;
    @SerializedName("referee")
    @Expose
    public String referee;

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("userId", userId).append("name", name).append("mobile", mobile).append("email", email).append("city", city).append("state", state).append("userType", userType).append("membershipType", membershipType).append("modifiedDate", modifiedDate).append("createdDate", createdDate).append("status", status).append("address", address).append("password", password).append("registrationId", registrationId).append("otpStatus", otpStatus).append("profilePic", profilePic).append("membershipId", membershipId).append("adviserId", adviserId).append("memberType", memberType).append("paymentStatus", paymentStatus).append("profilePin", profilePin).append("gcmId", gcmId).append("deviceId", deviceId).append("referral", referral).append("referee", referee).toString();
//    }
}
