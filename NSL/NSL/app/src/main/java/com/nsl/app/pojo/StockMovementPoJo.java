package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/12/2017.
 */

public class StockMovementPoJo
{
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
    @SerializedName("customer_id")
    @Expose
    public int customerId;
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
