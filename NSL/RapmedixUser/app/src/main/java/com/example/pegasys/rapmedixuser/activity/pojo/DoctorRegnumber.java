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

public class DoctorRegnumber implements Parcelable{
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("registration_number")
    @Expose
    public String registrationNumber;

    public DoctorRegnumber(Parcel in) {
        id = in.readString();
        doctorId = in.readString();
        registrationNumber = in.readString();
    }

    public static final Creator<DoctorRegnumber> CREATOR = new Creator<DoctorRegnumber>() {
        @Override
        public DoctorRegnumber createFromParcel(Parcel in) {
            return new DoctorRegnumber(in);
        }

        @Override
        public DoctorRegnumber[] newArray(int size) {
            return new DoctorRegnumber[size];
        }
    };

    public DoctorRegnumber(DoctorDescription doctorDescription) {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("doctorId", doctorId).append("registrationNumber", registrationNumber).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(doctorId);
        parcel.writeString(registrationNumber);
    }
}
