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

public class DoctorPresentation implements Parcelable{
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("seminors_details")
    @Expose
    public String seminorsDetails;

    public DoctorPresentation(Parcel in) {
        id = in.readString();
        doctorId = in.readString();
        seminorsDetails = in.readString();
    }

    public DoctorPresentation(DoctorDescription doctorDescription) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(doctorId);
        dest.writeString(seminorsDetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorPresentation> CREATOR = new Creator<DoctorPresentation>() {
        @Override
        public DoctorPresentation createFromParcel(Parcel in) {
            return new DoctorPresentation(in);
        }

        @Override
        public DoctorPresentation[] newArray(int size) {
            return new DoctorPresentation[size];
        }
    };

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("doctorId", doctorId).append("seminorsDetails", seminorsDetails).toString();
    }
}
