package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/19/2017.
 */

public class familydata {
    @SerializedName("familydata")
    @Expose
    public List<Familydatum> familydata = null;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("familydata", familydata).append("username", username).append("status", status).toString();
    }
}
