package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class addMemberbelow {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("membername")
    @Expose
    public String membername;
    @SerializedName("relationship")
    @Expose
    public String relationship;
    @SerializedName("birthdate")
    @Expose
    public String birthdate;
    @SerializedName("age_type")
    @Expose
    public String ageType;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("membername", membername).append("relationship", relationship).append("birthdate", birthdate).append("ageType", ageType).toString();
    }
}
