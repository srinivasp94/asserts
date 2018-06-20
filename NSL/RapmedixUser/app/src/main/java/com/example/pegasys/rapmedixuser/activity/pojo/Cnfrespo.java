package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/19/2017.
 */

public class Cnfrespo {
    @SerializedName("res_status")
    @Expose
    public String resStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("resStatus", resStatus).toString();
    }
}
