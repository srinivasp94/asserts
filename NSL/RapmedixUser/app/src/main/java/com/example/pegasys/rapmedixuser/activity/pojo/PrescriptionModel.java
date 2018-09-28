package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 1/2/2018.
 */

public class PrescriptionModel {
    @SerializedName("0")
    @Expose
    public List<PrescriptionModelList> mPrescriptionModelList = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("img_url")
    @Expose
    public String imgUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("PrescriptionModelList ", mPrescriptionModelList ).append("status", status).append("imgUrl", imgUrl).toString();
    }
}
