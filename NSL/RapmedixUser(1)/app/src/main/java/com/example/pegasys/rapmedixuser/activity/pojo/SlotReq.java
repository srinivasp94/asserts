package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/14/2017.
 */

public class SlotReq {
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("doctorworkingdetails_id")
    @Expose
    public String doctorworkingdetailsId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("date", date).append("doctorId", doctorId).append("doctorworkingdetailsId", doctorworkingdetailsId).toString();
    }
}
