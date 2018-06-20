package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockMovementUnSynedPojo {

    @SerializedName("stock_movement_detail_id")
    @Expose
    public int stockMovementDetailId;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("customer_id")
    @Expose
    public int customerId;
    @SerializedName("crop_id")
    @Expose
    public int cropId;
    @SerializedName("product_id")
    @Expose
    public int productId;
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

    @SerializedName("stock_movement_id")
    @Expose
    public int stockMovementId;
    @SerializedName("movement_type")
    @Expose
    public int movementType;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
