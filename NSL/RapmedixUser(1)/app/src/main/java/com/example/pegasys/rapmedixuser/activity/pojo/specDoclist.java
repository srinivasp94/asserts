package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/11/2017.
 */

public class specDoclist {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("specialisation_name")
    @Expose
    public String specialisationName;
    @SerializedName("experience")
    @Expose
    public String experience;
    @SerializedName("degree_name")
    @Expose
    public String degreeName;
    @SerializedName("hospital_name")
    @Expose
    public String hospitalName;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("id", id).append("specialisationName", specialisationName).append("experience", experience).append("degreeName", degreeName).append("hospitalName", hospitalName).append("distance", distance).append("profilePic", profilePic).toString();
    }
}
