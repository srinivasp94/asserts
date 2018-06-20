package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/20/2017.
 */

public class SchemeDetailsListPojo {
    @SerializedName("product_name")
    @Expose
    public String productName;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("region")
    @Expose
    public String region;

    @SerializedName("validfrom")
    @Expose
    public String validfrom;

    @SerializedName("validto")
    @Expose
    public String validto;

    @SerializedName("slabid")
    @Expose
    public String slabid;
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
