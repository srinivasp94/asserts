package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/11/2017.
 */

public class DocspecList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("specialisation_name")
    @Expose
    public String specialisationName;
    @SerializedName("specialisation_image")
    @Expose
    public String specialisationImage;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("specialisationName", specialisationName).append("specialisationImage", specialisationImage).toString();
    }

}
