package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockReturnUnSynedPojo {

    @SerializedName("stock_returns_details_id")
    @Expose
    public int stockReturnsDetailsId;
    @SerializedName("crop_id")
    @Expose
    public int cropId;
    @SerializedName("product_id")
    @Expose
    public int productId;
    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("stock_return_id")
    @Expose
    public int stockReturnId;

    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("company_id")
    @Expose
    public int companyId;
    @SerializedName("division_id")
    @Expose
    public int divisionId;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("updated_by")
    @Expose
    public String updatedBy;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("updated_datetime")
    @Expose
    public String updatedDatetime;
    @SerializedName("ffm_id")
    @Expose
    public int ffmId;
    @SerializedName("customer_id")
    @Expose
    public int customerId;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}