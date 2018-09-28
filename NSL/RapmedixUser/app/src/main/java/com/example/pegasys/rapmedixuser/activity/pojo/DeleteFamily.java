package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by rk on 30-Dec-17.
 */

public class DeleteFamily {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).toString();
    }
}
