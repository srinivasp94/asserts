package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/20/2017.
 */

public class BankDetailsListPojo {
    @SerializedName("bank_name")
    @Expose
    public String bankName;
    @SerializedName("account_no")
    @Expose
    public String accountNo;
    @SerializedName("ifsc_code")
    @Expose
    public String ifscCode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
