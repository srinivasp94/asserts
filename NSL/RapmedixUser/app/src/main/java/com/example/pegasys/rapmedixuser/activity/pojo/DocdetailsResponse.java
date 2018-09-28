package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/13/2017.
 */

public class DocdetailsResponse {
    @SerializedName("doctordatails")
    @Expose
    public List<Doctordatail> doctordatails = null;
    @SerializedName("doctorworkingdatails")
    @Expose
    public List<Doctorworkingdatail> doctorworkingdatails = null;
    @SerializedName("doctor_experience")
    @Expose
    public List<DoctorExperience> doctorExperience = null;
    @SerializedName("doctor_qualification")
    @Expose
    public List<Object> doctorQualification = null;
    @SerializedName("doctor_awords")
    @Expose
    public List<DoctorAword> doctorAwords = null;
    @SerializedName("doctor_regnumbers")
    @Expose
    public List<DoctorRegnumber> doctorRegnumbers = null;
    @SerializedName("doctor_presentations")
    @Expose
    public List<DoctorPresentation> doctorPresentations = null;
    @SerializedName("doctor_selected_service")
    @Expose
    public List<DoctorSelectedService> doctorSelectedService = null;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("doctordatails", doctordatails).append("doctorworkingdatails", doctorworkingdatails).append("doctorExperience", doctorExperience).append("doctorQualification", doctorQualification).append("doctorAwords", doctorAwords).append("doctorRegnumbers", doctorRegnumbers).append("doctorPresentations", doctorPresentations).append("doctorSelectedService", doctorSelectedService).append("status", status).toString();
    }
}
