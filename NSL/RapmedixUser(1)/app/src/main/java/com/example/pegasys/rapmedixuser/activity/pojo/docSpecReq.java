package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/12/2017.
 */

public class docSpecReq {
    @SerializedName("specialisation_id")
    @Expose
    public String specialisationId;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("specialisationId", specialisationId).append("latitude", latitude).append("longitude", longitude).toString();
    }
}
