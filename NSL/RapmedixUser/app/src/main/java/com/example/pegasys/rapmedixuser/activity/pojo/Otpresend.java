package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pegasys on 12/6/2017.
 */

public class Otpresend {
    @SerializedName("otp")
    @Expose
    public String otp;
    @SerializedName("status")
    @Expose
    public String status;

    /*@Override
    public String toString() {
        return new ToStringBuilder(this).append("otp", otp).append("status", status).toString();
    }*/
}
