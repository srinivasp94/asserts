package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by admin on 4/18/2017.
 */

public class ViewModalAdvBookingPojo {
    @SerializedName("customer_name")
    @Expose
    public String customerName;
    @SerializedName("customer_code")
    @Expose
    public String customerCode;
    @SerializedName("customer_id")
    @Expose
    public String customerId;
    @SerializedName("OrderDate")
    @Expose
    public String orderDate;
    @SerializedName("ABS")
    @Expose
    public String aBS;
    @SerializedName("rate")
    @Expose
    public String rate;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("slab_id")
    @Expose
    public String slabId;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
