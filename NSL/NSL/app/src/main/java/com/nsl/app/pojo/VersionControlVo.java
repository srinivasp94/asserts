package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by admin on 4/18/2017.
 */

public class VersionControlVo {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("table_name")
    @Expose
    public String tableName;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("updated_date")
    @Expose
    public String updatedDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
