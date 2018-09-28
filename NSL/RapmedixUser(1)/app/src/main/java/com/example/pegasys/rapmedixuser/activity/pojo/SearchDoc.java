package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/11/2017.
 */

public class SearchDoc {
    @SerializedName("0")
    @Expose
    public List<specDoclist> specialDoclist= null;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("specialDoclist", specialDoclist).append("status", status).toString();
    }
}
