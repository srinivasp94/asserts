package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/20/2018.
 */

public class SubservicesName {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("diagnostics_id")
    @Expose
    public String diagnosticsId;
    @SerializedName("subservice_name")
    @Expose
    public String subserviceName;
    @SerializedName("homevisit_avaliable")
    @Expose
    public String homevisitAvaliable;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("diagnosticsId", diagnosticsId).append("subserviceName", subserviceName).append("homevisitAvaliable", homevisitAvaliable).toString();
    }
}
