package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 3/22/2018.
 */

public class pharmacyModel {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("patient_name")
    @Expose
    public String patientName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("document")
    @Expose
    public String document;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("patientName", patientName).append("description", description).append("document", document).toString();
    }
}
