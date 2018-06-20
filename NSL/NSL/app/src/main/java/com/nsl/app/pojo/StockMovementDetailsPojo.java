package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockMovementDetailsPojo {

    @SerializedName("stock_movement_detail_id")
    @Expose
    public int stockMovementDetailId=0;
    @SerializedName("stock_movement_id")
    @Expose
    public int stockMovementId=0;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("customer_id")
    @Expose
    public int customerId=0;
    @SerializedName("crop_id")
    @Expose
    public int cropId=0;
    @SerializedName("product_id")
    @Expose
    public int productId=0;
    @SerializedName("stock_placed")
    @Expose
    public String stockPlaced;
    @SerializedName("current_stock")
    @Expose
    public String currentStock;
    @SerializedName("placed_date")
    @Expose
    public String placedDate;
    @SerializedName("pog")
    @Expose
    public String pog;
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
    public int ffmId=0;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
