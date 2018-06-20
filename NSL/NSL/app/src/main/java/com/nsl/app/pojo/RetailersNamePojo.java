package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/15/2017.
 */

public class RetailersNamePojo {
    @SerializedName("retailer_tin_no")
    @Expose
    public String retailerTinNo;
    @SerializedName("retailer_name")
    @Expose
    public String retailerName;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;
    @SerializedName("mobile_no")
    @Expose
    public String mobileNo;
    @SerializedName("phone_no")
    @Expose
    public String phoneNo;
    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("sqlite_id")
    @Expose
    public String sqliteId;
    @SerializedName("StockMovementFirstListPojo")
    @Expose
    public StockMovementFirstListPojo stockMovementFirstListPojo;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
