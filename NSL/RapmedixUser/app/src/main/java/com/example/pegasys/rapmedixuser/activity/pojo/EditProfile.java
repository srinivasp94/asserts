package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import okhttp3.MultipartBody;

/**
 * Created by rk on 30-Dec-17.
 */

public class EditProfile {

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
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("membership")
    @Expose
    public String membership;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("name", name).append("mobile", mobile).append("email", email).append("profilePic", profilePic).append("membership", membership).append("address", address).append("city", city).append("state", state).toString();
    }
}