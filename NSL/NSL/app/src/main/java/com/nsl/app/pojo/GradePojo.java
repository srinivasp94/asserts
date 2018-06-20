package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/15/2017.
 */

public class GradePojo {
    @SerializedName("grade_id")
    @Expose
    public String gradeId;
    @SerializedName("grade_name")
    @Expose
    public String gradeName;
    @SerializedName("price_per_km")
    @Expose
    public String pricePerKm;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
