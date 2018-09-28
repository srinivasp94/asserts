package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 2/20/2018.
 */

public class CentersResponse {
    @SerializedName("diagnosticscenters")
    @Expose
    public List<Diagnosticscenter> diagnosticscenters = null;
    @SerializedName("subservices_names")
    @Expose
    public List<SubservicesName> subservicesNames = null;
    @SerializedName("subservice_id")
    @Expose
    public List<String> subserviceId = null;
    @SerializedName("city")
    @Expose
    public String city;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("diagnosticscenters", diagnosticscenters).append("subservicesNames", subservicesNames).append("subserviceId", subserviceId).append("city", city).toString();
    }

}
