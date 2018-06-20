package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 6/15/2017.
 */

public class StockMovementRetailerDetails {
    @SerializedName("stock_movement_retailer_id")
    @Expose
    public String stockMovementRetailerId;
    @SerializedName("stock_movement_id")
    @Expose
    public String stockMovementId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("stock_placed")
    @Expose
    public String stockPlaced;
    @SerializedName("current_stock")
    @Expose
    public String currentStock;
    @SerializedName("pog")
    @Expose
    public String pog;
    @SerializedName("placed_date")
    @Expose
    public String placedDate;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("updated_datetime")
    @Expose
    public String updatedDatetime;
    @SerializedName("verified")
    @Expose
    public String verified;
    @SerializedName("verified_by")
    @Expose
    public String verifiedBy;
    @SerializedName("company_id")
    @Expose
    public int companyId=0;
    @SerializedName("division_id")
    @Expose
    public int divisionId=0;
    @SerializedName("customer_id")
    @Expose
    public int customerId=0;
    @SerializedName("ffm_id")
    @Expose
    public int ffmId=0;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
