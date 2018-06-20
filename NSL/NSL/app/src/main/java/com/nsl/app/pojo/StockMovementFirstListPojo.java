package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockMovementFirstListPojo {

    @SerializedName("brand_name")
    @Expose
    public int brandName;
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
    public String stockMovementId;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
