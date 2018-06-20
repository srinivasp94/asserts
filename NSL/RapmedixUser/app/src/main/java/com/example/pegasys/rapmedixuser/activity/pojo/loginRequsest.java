package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/8/2017.
 */

public class loginRequsest {

    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("device_token")
    @Expose
    public String deviceToken;
    @SerializedName("device_type")
    @Expose
    public String deviceType;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mobile", mobile).append("password", password).append("deviceToken", deviceToken).append("deviceType", deviceType).toString();
    }
}
