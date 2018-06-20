package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/18/2017.
 */

public class ViewModalAdvBookingListPojo {

    @SerializedName("OrderDate")
    @Expose
    public String orderDate;
    @SerializedName("ViewModalAdvBookingPojo")
    @Expose
    public List<ViewModalAdvBookingPojo> uiewModalAdvBookingPojo=new ArrayList<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
