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

public class DoctorSelectedService implements Parcelable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("doctor_id")
    @Expose
    public String doctorId;
    @SerializedName("hospital_services_id")
    @Expose
    public String hospitalServicesId;
    @SerializedName("service_name")
    @Expose
    public String serviceName;
    @SerializedName("service_image")
    @Expose
    public String serviceImage;

    public DoctorSelectedService(Parcel in) {
        id = in.readString();
        doctorId = in.readString();
        hospitalServicesId = in.readString();
        serviceName = in.readString();
        serviceImage = in.readString();
    }

    public DoctorSelectedService(DoctorDescription doctorDescription) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(doctorId);
        dest.writeString(hospitalServicesId);
        dest.writeString(serviceName);
        dest.writeString(serviceImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorSelectedService> CREATOR = new Creator<DoctorSelectedService>() {
        @Override
        public DoctorSelectedService createFromParcel(Parcel in) {
            return new DoctorSelectedService(in);
        }

        @Override
        public DoctorSelectedService[] newArray(int size) {
            return new DoctorSelectedService[size];
        }
    };

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("doctorId", doctorId).append("hospitalServicesId", hospitalServicesId).append("serviceName", serviceName).append("serviceImage", serviceImage).toString();
    }
}
