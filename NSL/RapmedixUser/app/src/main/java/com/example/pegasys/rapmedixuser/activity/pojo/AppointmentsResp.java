package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pegasys on 12/26/2017.
 */

public class AppointmentsResp {
    @SerializedName("0")
    @Expose
    public ArrayList<AppointmentsList> appointmentsLists = null;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("", appointmentsLists).append("status", status).toString();
    }
}
