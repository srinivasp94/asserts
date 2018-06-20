package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockReturnDetailsPoJo
{
    @SerializedName("stock_return_id")
    @Expose
    public int stockReturnId;

    @SerializedName("user_id")
    @Expose
    public int userId;
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

    @SerializedName("ffm_id")
    @Expose
    public int ffmId=0;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
