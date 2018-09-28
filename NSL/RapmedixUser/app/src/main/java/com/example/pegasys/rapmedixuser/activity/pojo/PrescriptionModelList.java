package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 1/2/2018.
 */

public class PrescriptionModelList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("sender_id")
    @Expose
    public String senderId;
    @SerializedName("recever_id")
    @Expose
    public String receverId;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("doctor_prescription")
    @Expose
    public String doctorPrescription;
    @SerializedName("appintment_id")
    @Expose
    public String appintmentId;
    @SerializedName("uploaded_type")
    @Expose
    public String uploadedType;
    @SerializedName("uploaded_for")
    @Expose
    public String uploadedFor;
    @SerializedName("doctor_name")
    @Expose
    public String doctorName;
    @SerializedName("patient_name")
    @Expose
    public String patientName;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("senderId", senderId).append("receverId", receverId).append("createdDate", createdDate).append("title", title).append("doctorPrescription", doctorPrescription).append("appintmentId", appintmentId).append("uploadedType", uploadedType).append("uploadedFor", uploadedFor).append("doctorName", doctorName).append("patientName", patientName).toString();
    }

}
