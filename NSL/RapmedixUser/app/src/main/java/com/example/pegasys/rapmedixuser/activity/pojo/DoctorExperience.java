package com.example.pegasys.rapmedixuser.activity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/28/2017.
 */

public class DoctorExperience implements Parcelable{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("start_year")
    @Expose
    public String startYear;
    @SerializedName("end_year")
    @Expose
    public String endYear;
    @SerializedName("position")
    @Expose
    public String position;

    public DoctorExperience(Parcel in) {
        id = in.readString();
        doctorId = in.readString();
        startYear = in.readString();
        endYear = in.readString();
        position = in.readString();
    }

    public static final Parcelable.Creator<DoctorExperience> CREATOR = new Parcelable.Creator<DoctorExperience>() {
        @Override
        public DoctorExperience createFromParcel(Parcel in) {
            return new DoctorExperience(in);
        }

        @Override
        public DoctorExperience[] newArray(int size) {
            return new DoctorExperience[size];
        }
    };

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("doctorId", doctorId).append("startYear", startYear).append("endYear", endYear).append("position", position).toString();
    }

    public DoctorExperience(DoctorDescription doctorDescription) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(doctorId);
        parcel.writeString(startYear);
        parcel.writeString(endYear);
        parcel.writeString(position);
    }
}
