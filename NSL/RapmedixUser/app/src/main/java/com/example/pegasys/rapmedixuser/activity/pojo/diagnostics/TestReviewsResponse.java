package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/20/2018.
 */

public class TestReviewsResponse {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("diagnostics_id")
    @Expose
    public String diagnosticsId;
    @SerializedName("diagnosticssubservice_id")
    @Expose
    public String diagnosticssubserviceId;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("basicprice")
    @Expose
    public String basicprice;
    @SerializedName("offerprice")
    @Expose
    public String offerprice;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("subservice_name")
    @Expose
    public String subserviceName;
    @SerializedName("homevisit_avaliable")
    @Expose
    public String homevisitAvaliable;
    @SerializedName("freeuser")
    @Expose
    public String freeuser;
    @SerializedName("premiunmuser")
    @Expose
    public String premiunmuser;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("diagnosticsId", diagnosticsId).append("diagnosticssubserviceId", diagnosticssubserviceId).append("hospitalId", hospitalId).append("basicprice", basicprice).append("offerprice", offerprice).append("status", status).append("subserviceName", subserviceName).append("homevisitAvaliable", homevisitAvaliable).append("freeuser", freeuser).append("premiunmuser", premiunmuser).toString();
    }

}
