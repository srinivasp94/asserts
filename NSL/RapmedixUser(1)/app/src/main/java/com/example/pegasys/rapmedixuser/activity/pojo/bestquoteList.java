package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class bestquoteList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("bestquote_name")
    @Expose
    public String bestquoteName;
    @SerializedName("bestquote_image")
    @Expose
    public String bestquoteImage;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("bestquoteName", bestquoteName).append("bestquoteImage", bestquoteImage).toString();
    }


}
