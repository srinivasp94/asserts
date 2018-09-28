package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/21/2017.
 */

public class Simpleresponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).toString();
    }
}
