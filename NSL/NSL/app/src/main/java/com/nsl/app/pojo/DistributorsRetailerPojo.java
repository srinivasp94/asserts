package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/15/2017.
 */

public class DistributorsRetailerPojo {
    @SerializedName("distributor_retailer_id")
    @Expose
    public String distributorRetailerId;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
