package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/19/2017.
 */

public class Familydatum {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("relation_ship")
    @Expose
    public String relationShip;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("age")
    @Expose
    public String age;
    @SerializedName("dateofbirth")
    @Expose
    public String dateofbirth;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("name", name).append("mobile", mobile).append("email", email).append("relationShip", relationShip).append("status", status).append("age", age).append("dateofbirth", dateofbirth).toString();
    }

}
