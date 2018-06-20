package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by pegasys on 12/14/2017.
 */

public class checkupResponse {
    @SerializedName("0")
    @Expose
    public List<checkupList> checkupLists = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("img_url")
    @Expose
    public String imgUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("checkupLists", checkupLists).append("status", status).append("imgUrl", imgUrl).toString();
    }

}
