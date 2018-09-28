package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/20/2018.
 */

public class Diagnosticscenter {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("email_id")
    @Expose
    public String emailId;
    @SerializedName("contact_number")
    @Expose
    public String contactNumber;
    @SerializedName("contact_person")
    @Expose
    public String contactPerson;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("geolocation")
    @Expose
    public String geolocation;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("landmark")
    @Expose
    public String landmark;
    @SerializedName("emergency_number")
    @Expose
    public String emergencyNumber;
    @SerializedName("helpline_number")
    @Expose
    public String helplineNumber;
    @SerializedName("ambulance_number")
    @Expose
    public String ambulanceNumber;
    @SerializedName("specialisations")
    @Expose
    public String specialisations;
    @SerializedName("service")
    @Expose
    public String service;
    @SerializedName("bed_strenth")
    @Expose
    public String bedStrenth;
    @SerializedName("equipments")
    @Expose
    public String equipments;
    @SerializedName("laser_treatments")
    @Expose
    public String laserTreatments;
    @SerializedName("implantations")
    @Expose
    public String implantations;
    @SerializedName("transplantations")
    @Expose
    public String transplantations;
    @SerializedName("accreditations")
    @Expose
    public String accreditations;
    @SerializedName("awards")
    @Expose
    public String awards;
    @SerializedName("memberships")
    @Expose
    public String memberships;
    @SerializedName("emplancements")
    @Expose
    public String emplancements;
    @SerializedName("criticalcareservices")
    @Expose
    public String criticalcareservices;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("images")
    @Expose
    public String images;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("video_title")
    @Expose
    public String videoTitle;
    @SerializedName("video_url")
    @Expose
    public String videoUrl;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("aboutus")
    @Expose
    public String aboutus;
    @SerializedName("homevisit")
    @Expose
    public String homevisit;
    @SerializedName("homevisit_type")
    @Expose
    public String homevisitType;
    @SerializedName("minumum_order_amount")
    @Expose
    public String minumumOrderAmount;
    @SerializedName("charge_mimumum_order_amount")
    @Expose
    public String chargeMimumumOrderAmount;
    @SerializedName("bprice")
    @Expose
    public String bprice;
    @SerializedName("offprice")
    @Expose
    public String offprice;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("distance")
    @Expose
    public Double distance;
    @SerializedName("subservicecount")
    @Expose
    public String subservicecount;
    @SerializedName("freeuser")
    @Expose
    public String freeuser;
    @SerializedName("premiunmuser")
    @Expose
    public String premiunmuser;
    @SerializedName("diagnostics_id")
    @Expose
    public String diagnosticsId;
    @SerializedName("diagnosticssubservice_id")
    @Expose
    public String diagnosticssubserviceId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("hospitalId", hospitalId).append("emailId", emailId).append("contactNumber", contactNumber).append("contactPerson", contactPerson).append("address", address).append("address1", address1).append("geolocation", geolocation).append("latitude", latitude).append("longitude", longitude).append("state", state).append("city", city).append("pincode", pincode).append("landmark", landmark).append("emergencyNumber", emergencyNumber).append("helplineNumber", helplineNumber).append("ambulanceNumber", ambulanceNumber).append("specialisations", specialisations).append("service", service).append("bedStrenth", bedStrenth).append("equipments", equipments).append("laserTreatments", laserTreatments).append("implantations", implantations).append("transplantations", transplantations).append("accreditations", accreditations).append("awards", awards).append("memberships", memberships).append("emplancements", emplancements).append("criticalcareservices", criticalcareservices).append("profilePic", profilePic).append("images", images).append("password", password).append("status", status).append("videoTitle", videoTitle).append("videoUrl", videoUrl).append("userType", userType).append("aboutus", aboutus).append("homevisit", homevisit).append("homevisitType", homevisitType).append("minumumOrderAmount", minumumOrderAmount).append("chargeMimumumOrderAmount", chargeMimumumOrderAmount).append("bprice", bprice).append("offprice", offprice).append("name", name).append("location", location).append("distance", distance).append("subservicecount", subservicecount).append("freeuser", freeuser).append("premiunmuser", premiunmuser).append("diagnosticsId", diagnosticsId).append("diagnosticssubserviceId", diagnosticssubserviceId).toString();
    }

}
