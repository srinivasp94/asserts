package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/7/2017.
 */

public class OTPpojo {
    @SerializedName("mobile")
    @Expose
    public String mobile;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mobile", mobile).toString();
    }
}
