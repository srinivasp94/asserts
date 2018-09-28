package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/20/2018.
 */

public class subCategoriesCentersRequest {
    @SerializedName("user_id")
    @Expose
    public String userid;
    @SerializedName("subservice_id")
    @Expose
    public String subserviceId;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("latitude")
    @Expose
    public double latitude;
    @SerializedName("longitude")
    @Expose
    public double longitude;
    @SerializedName("city")
    @Expose
    public String city;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("subserviceId", subserviceId).append("location", location).append("latitude", latitude).append("longitude", longitude).append("city", city).toString();
    }
}
