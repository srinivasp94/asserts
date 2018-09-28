package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by rk on 03-Jan-18.
 */

public class LabModel {
    @SerializedName("0")
    @Expose
    public List<LabList> mLabs= null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("img_url")
    @Expose
    public String imgUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mLabs", mLabs).append("status", status).append("imgUrl", imgUrl).toString();
    }
}
