package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/20/2018.
 */

public class testreviewsRequest {
    @SerializedName("user_id")
    @Expose
    public String userid;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("subservice_id")
    @Expose
    public String subserviceId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userid",userid).append("hospitalId", hospitalId).append("subserviceId", subserviceId).toString();
    }
}
