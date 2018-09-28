package com.example.pegasys.rapmedixuser.activity.pojo.healthrecords;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/7/2018.
 */

public class Userdetails {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("profile_pin")
    @Expose
    public String profilePin;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("userId", userId).append("profilePin", profilePin).toString();
    }
}
