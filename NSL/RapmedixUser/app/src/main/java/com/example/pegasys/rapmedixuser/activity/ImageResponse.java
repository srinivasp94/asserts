package com.example.pegasys.rapmedixuser.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by rk on 06-Jan-18.
 */

public class ImageResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("profilePic", profilePic).toString();
    }
}
