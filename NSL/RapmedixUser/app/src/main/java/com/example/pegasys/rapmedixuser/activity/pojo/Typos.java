package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class Typos {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("doc_type")
    @Expose
    public String docType;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("health_records")
    @Expose
    public String healthRecords;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("sub_id")
    @Expose
    public String subId;
    @SerializedName("uploaded_type")
    @Expose
    public String uploadedType;

    public String docName;
    public String time;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("docType", docType).append("title", title).append("healthRecords", healthRecords).append("createdDate", createdDate).append("subId", subId).append("uploadedType", uploadedType).toString();
    }

}
