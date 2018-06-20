package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/13/2017.
 */

public class Doctordatail {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("experience")
    @Expose
    public String experience;
    @SerializedName("aboutus")
    @Expose
    public String aboutus;
    @SerializedName("technicsprocedures")
    @Expose
    public String technicsprocedures;
    @SerializedName("educationdetails")
    @Expose
    public String educationdetails;
    @SerializedName("awards")
    @Expose
    public String awards;
    @SerializedName("presentations")
    @Expose
    public String presentations;
    @SerializedName("equipment")
    @Expose
    public String equipment;
    @SerializedName("working_hospitals")
    @Expose
    public String workingHospitals;
    @SerializedName("clinik_hospital")
    @Expose
    public String clinikHospital;
    @SerializedName("guest_visit")
    @Expose
    public String guestVisit;
    @SerializedName("home_visit")
    @Expose
    public String homeVisit;
    @SerializedName("specialisation_name")
    @Expose
    public String specialisationName;
    @SerializedName("degree_name")
    @Expose
    public String degreeName;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("profilePic", profilePic).append("experience", experience).append("aboutus", aboutus).append("technicsprocedures", technicsprocedures).append("educationdetails", educationdetails).append("awards", awards).append("presentations", presentations).append("equipment", equipment).append("workingHospitals", workingHospitals).append("clinikHospital", clinikHospital).append("guestVisit", guestVisit).append("homeVisit", homeVisit).append("specialisationName", specialisationName).append("degreeName", degreeName).toString();
    }
}
