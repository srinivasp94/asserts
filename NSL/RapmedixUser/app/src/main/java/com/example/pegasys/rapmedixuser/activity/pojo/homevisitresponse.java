package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/18/2017.
 */

public class homevisitresponse {
    @SerializedName("0")
    @Expose
    public List<Visitlist> visitlistList = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("img_url")
    @Expose
    public String imgUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("visitlistList", visitlistList).append("status", status).append("imgUrl", imgUrl).toString();
    }

    public class Visitlist {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("homeservice_name")
        @Expose
        public String homeserviceName;
        @SerializedName("homeservice_image")
        @Expose
        public String homeserviceImage;

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("id", id).append("homeserviceName", homeserviceName).append("homeserviceImage", homeserviceImage).toString();
        }
    }
}
