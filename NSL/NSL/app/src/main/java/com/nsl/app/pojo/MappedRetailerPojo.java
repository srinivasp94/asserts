package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/15/2017.
 */

public class MappedRetailerPojo {
    @SerializedName("retailer_tin_no")
    @Expose
    public String retailerTinNo;
    @SerializedName("retailer_name")
    @Expose
    public String retailerName;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;
    @SerializedName("stock_movement_retailer_id")
    @Expose
    public String stockMovementRetailerId;
    @SerializedName("current_stock")
    @Expose
    public String currentStock;
    @SerializedName("stock_placed")
    @Expose
    public String stockPlaced;

 @SerializedName("pog")
    @Expose
    public String pog="0";



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
