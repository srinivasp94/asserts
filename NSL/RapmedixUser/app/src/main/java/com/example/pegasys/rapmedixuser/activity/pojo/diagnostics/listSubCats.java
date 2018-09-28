package com.example.pegasys.rapmedixuser.activity.pojo.diagnostics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

/**
 * Created by pegasys on 2/15/2018.
 */

public class listSubCats {
    @SerializedName("")
    @Expose
    public ArrayList<DiagnosticsSubcatList> diagnosticsSubcatListArrayList;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("",diagnosticsSubcatListArrayList).toString();
    }
}
