package com.example.pegasys.rapmedixuser.activity.pojo.healthrecords;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/9/2018.
 */

public class ProfilepinCheck {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("adviser_id")
    @Expose
    public String adviserId;
    @SerializedName("profile_pin")
    @Expose
    public String profilePin;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("adviserId", adviserId).append("profilePin", profilePin).toString();
    }
}
