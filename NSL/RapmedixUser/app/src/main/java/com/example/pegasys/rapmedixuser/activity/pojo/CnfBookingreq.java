package com.example.pegasys.rapmedixuser.activity.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/18/2017.
 */

public class CnfBookingreq {
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("appointment_date")
    @Expose
    public String appointmentDate;
    @SerializedName("appointment_time")
    @Expose
    public String appointmentTime;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("fee")
    @Expose
    public String fee;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("doctorworkingdetails_id")
    @Expose
    public String doctorworkingdetailsId;
    @SerializedName("patient_name")
    @Expose
    public String patientName;
    @SerializedName("patient_mobile")
    @Expose
    public String patientMobile;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("sub_total")
    @Expose
    public String subTotal;
    @SerializedName("servicetax")
    @Expose
    public String servicetax;
    @SerializedName("total_payble_amount")
    @Expose
    public String totalPaybleAmount;
    @SerializedName("coupon_type")
    @Expose
    public String couponType;
    @SerializedName("category_name")
    @Expose
    public String categoryName;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("doctorId", doctorId).append("appointmentDate", appointmentDate).append("appointmentTime", appointmentTime).append("userId", userId).append("fee", fee).append("hospitalId", hospitalId).append("doctorworkingdetailsId", doctorworkingdetailsId).append("patientName", patientName).append("patientMobile", patientMobile).append("discount", discount).append("subTotal", subTotal).append("servicetax", servicetax).append("totalPaybleAmount", totalPaybleAmount).append("couponType", couponType).append("categoryName", categoryName).toString();
    }
}
