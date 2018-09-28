package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class HealthRecordsRespo {
    @SerializedName("0")
    @Expose
    public Typos typos;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Typos", typos).append("status", status).toString();
    }

}
