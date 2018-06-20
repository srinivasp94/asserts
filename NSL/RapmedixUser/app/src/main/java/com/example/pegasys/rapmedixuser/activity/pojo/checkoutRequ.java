package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/18/2017.
 */

public class checkoutRequ {
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("doctorworkingdetails_id")
    @Expose
    public String doctorworkingdetailsId;
    @SerializedName("user_id")
    @Expose
    public String userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("doctorId", doctorId).append("hospitalId", hospitalId).append("doctorworkingdetailsId", doctorworkingdetailsId).append("userId", userId).toString();
    }
}
