package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/19/2017.
 */

public class userIdreq {
    @SerializedName("user_id")
    @Expose
    public String userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).toString();
    }
}
