package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/14/2017.
 */

public class SlotResponse {
    /*@SerializedName("schedule")
    @Expose
    public List<String> schedule = null;
    @SerializedName("appointmentsdata")
    @Expose
    public List<Object> appointmentsdata = null;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("schedule", schedule).append("appointmentsdata", appointmentsdata).append("status", status).toString();
    }*/

    @SerializedName("schedule")
    @Expose
    public List<String> schedule = null;
    @SerializedName("appointmentsdata")
    @Expose
    public List<String> appointmentsdata = null;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("schedule", schedule).append("appointmentsdata", appointmentsdata).append("status", status).toString();
    }
}
