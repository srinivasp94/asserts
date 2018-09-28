package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/13/2017.
 */

public class DocIdReq {
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("doctorId", doctorId).toString();
    }
}
