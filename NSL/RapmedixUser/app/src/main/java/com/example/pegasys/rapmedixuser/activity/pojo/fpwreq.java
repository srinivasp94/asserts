package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/15/2017.
 */

public class fpwreq {
    @SerializedName("usermobile")
    @Expose
    public String usermobile;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("usermobile", usermobile).toString();
    }
}
