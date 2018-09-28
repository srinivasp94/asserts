package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 1/8/2018.
 */

public class CancelAppointmentmodel {
    @SerializedName("appointment_id")
    @Expose
    public String appointmentId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("appointmentId", appointmentId).toString();

    }
}
