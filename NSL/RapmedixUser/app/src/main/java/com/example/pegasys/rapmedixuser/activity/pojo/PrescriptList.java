package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by BhargavaSunkara on 27-Dec-17.
 */

public class PrescriptList {

    /*@SerializedName("id")
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("docType", docType).append("title", title).append("healthRecords", healthRecords).append("createdDate", createdDate).append("subId", subId).append("uploadedType", uploadedType).toString();
    }*/
    String title;
    String doc_type;
    String createdat;

    public PrescriptList() {
    }

    public PrescriptList(String title, String doc_type, String createdat) {
        this.title = title;
        this.doc_type = doc_type;
        this.createdat = createdat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }
}
