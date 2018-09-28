package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 1/2/2018.
 */

public class CategoriesList {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("diagnostics_name")
    @Expose
    public String diagnosticsName;
    @SerializedName("diagnostics_image")
    @Expose
    public String diagnosticsImage;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("homevisit_avaliable")
    @Expose
    public String homevisitAvaliable;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("diagnosticsName", diagnosticsName).append("diagnosticsImage", diagnosticsImage).append("description", description).append("homevisitAvaliable", homevisitAvaliable).toString();
    }

}
