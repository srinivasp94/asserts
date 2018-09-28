package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 1/3/2018.
 */

public class Callbackreq {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("service_id")
    @Expose
    public String serviceId;
    @SerializedName("callbackMobile")
    @Expose
    public String callbackMobile;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("serviceId", serviceId).append("callbackMobile", callbackMobile).toString();
    }
}
