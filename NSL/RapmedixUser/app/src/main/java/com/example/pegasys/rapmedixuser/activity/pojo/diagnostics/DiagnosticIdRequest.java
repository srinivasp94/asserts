package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 2/15/2018.
 */

public class DiagnosticIdRequest {
    @SerializedName("diagnostic_id")
    @Expose
    public String diagnostic_id;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("diagnostic_id", diagnostic_id).toString();
    }
}
