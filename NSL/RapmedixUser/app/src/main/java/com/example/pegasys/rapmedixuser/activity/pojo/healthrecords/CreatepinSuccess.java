package com.example.pegasys.rapmedixuser.activity.pojo.healthrecords;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import retrofit2.http.PUT;

/**
 * Created by pegasys on 2/14/2018.
 */

public class CreatepinSuccess {
    @SerializedName("profile_pin")
    @Expose
    public String profile_pin;

    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("profile_pin",profile_pin).append("status",status).toString();
    }
}
