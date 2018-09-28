package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/14/2017.
 */

public class checkupList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("plan_name")
    @Expose
    public String planName;
    @SerializedName("free_user_discount")
    @Expose
    public String freeUserDiscount;
    @SerializedName("premium_user_discount")
    @Expose
    public String premiumUserDiscount;
    @SerializedName("plan_image")
    @Expose
    public String planImage;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("description")
    @Expose
    public String description;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("planName", planName).append("freeUserDiscount", freeUserDiscount).append("premiumUserDiscount", premiumUserDiscount).append("planImage", planImage).append("status", status).append("description", description).toString();
    }
}
