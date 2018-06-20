package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 6/15/2017.
 */

public class MappedRetailerWithDistributorPojo {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("retailer_name")
    @Expose
    public String retailerName;




    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
