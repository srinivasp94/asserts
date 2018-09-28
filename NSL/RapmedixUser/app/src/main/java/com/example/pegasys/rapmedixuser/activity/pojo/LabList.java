package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by rk on 03-Jan-18.
 */

public class LabList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("diagnostics_id")
    @Expose
    public String diagnosticsId;
    @SerializedName("samplecollection")
    @Expose
    public String samplecollection;
    @SerializedName("eqipment")
    @Expose
    public String eqipment;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("category_names")
    @Expose
    public String categoryNames;
    @SerializedName("hospital_name")
    @Expose
    public String hospitalName;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("geolocation")
    @Expose
    public String geolocation;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("aboutus")
    @Expose
    public String aboutus;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("distance")
    @Expose
    public String distance;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("diagnosticsId", diagnosticsId).append("samplecollection", samplecollection).append("eqipment", eqipment).append("hospitalId", hospitalId).append("categoryNames", categoryNames).append("hospitalName", hospitalName).append("location", location).append("geolocation", geolocation).append("latitude", latitude).append("longitude", longitude).append("profilePic", profilePic).append("aboutus", aboutus).append("userType", userType).append("distance", distance).toString();
    }

}
