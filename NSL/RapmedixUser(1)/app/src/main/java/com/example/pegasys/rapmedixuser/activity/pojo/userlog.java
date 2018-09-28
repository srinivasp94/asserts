package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/19/2017.
 */

public class userlog {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("otp")
    @Expose
    public String otp;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("name", name).append("otp", otp).append("mobile", mobile).append("status", status).toString();
    }
}
