package com.example.pegasys.rapmedixuser.activity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by pegasys on 12/28/2017.
 */

public class DoctorAword implements Parcelable{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("doctor_membership_awords_details")
    @Expose
    public String doctorMembershipAwordsDetails;

    protected DoctorAword(Parcel in) {
        id = in.readString();
        doctorId = in.readString();
        doctorMembershipAwordsDetails = in.readString();
    }

    public static final Creator<DoctorAword> CREATOR = new Creator<DoctorAword>() {
        @Override
        public DoctorAword createFromParcel(Parcel in) {
            return new DoctorAword(in);
        }

        @Override
        public DoctorAword[] newArray(int size) {
            return new DoctorAword[size];
        }
    };

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("doctorId", doctorId).append("doctorMembershipAwordsDetails", doctorMembershipAwordsDetails).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(doctorId);
        parcel.writeString(doctorMembershipAwordsDetails);
    }
}
