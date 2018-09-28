package com.example.pegasys.rapmedixuser.activity.pojo.healthrecords;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 2/7/2018.
 */

public class HealthrecordsService {
    @SerializedName("userdetails")
    @Expose
    public Userdetails userdetails;
    @SerializedName("family_details")
    @Expose
    public List<FamilyDetail> familyDetails = null;
    @SerializedName("belowfamily_details")
    @Expose
    public List<BelowfamilyDetail> belowfamilyDetails = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userdetails", userdetails).append("familyDetails", familyDetails).append("belowfamilyDetails", belowfamilyDetails).toString();
    }
}
