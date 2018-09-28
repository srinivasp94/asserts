package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/21/2017.
 */

public class changepwdReq {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("oldpassword")
    @Expose
    public String oldpassword;
    @SerializedName("newpassword")
    @Expose
    public String newpassword;
    @SerializedName("confirmpassword")
    @Expose
    public String confirmpassword;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("oldpassword", oldpassword).append("newpassword", newpassword).append("confirmpassword", confirmpassword).toString();
    }
}
