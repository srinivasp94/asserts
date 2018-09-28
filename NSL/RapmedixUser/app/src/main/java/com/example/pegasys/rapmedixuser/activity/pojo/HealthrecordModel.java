package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by rk on 10-Jan-18.
 */

public class HealthrecordModel {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("doc_type")
    @Expose
    public String docType;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("document")
    @Expose
    public String document;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("docType", docType).append("title", title).append("document", document).toString();
    }
}
