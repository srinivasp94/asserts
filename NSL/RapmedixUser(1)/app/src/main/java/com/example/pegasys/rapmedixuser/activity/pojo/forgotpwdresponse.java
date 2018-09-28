package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/15/2017.
 */

public class forgotpwdresponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("message", message).toString();
    }
}
