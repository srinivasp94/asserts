package com.example.pegasys.rapmedixuser.activity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by pegasys on 12/13/2017.
 */

public class Doctorworkingdatail implements Parcelable  {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("fee")
    @Expose
    public String fee;
    @SerializedName("geolocation")
    @Expose
    public String geolocation;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("long")
    @Expose
    public String _long;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("hospital_id")
    @Expose
    public String hospitalId;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("hospital_name")
    @Expose
    public String hospitalName;
    @SerializedName("user_type")
    @Expose
    public String userType;

    public Doctorworkingdatail(Parcel in) {
        id = in.readString();
        fee = in.readString();
        geolocation = in.readString();
        lat = in.readString();
        _long = in.readString();
        address = in.readString();
        address1 = in.readString();
        city = in.readString();
        type = in.readString();
        hospitalId = in.readString();
        location = in.readString();
        hospitalName = in.readString();
        userType = in.readString();

    }

    public static final Parcelable.Creator<Doctorworkingdatail> CREATOR = new Parcelable.Creator<Doctorworkingdatail>() {
        @Override
        public Doctorworkingdatail createFromParcel(Parcel in) {
            return new Doctorworkingdatail(in);
        }

        @Override
        public Doctorworkingdatail[] newArray(int size) {
            return new Doctorworkingdatail[size];
        }
    };

    public Doctorworkingdatail(DoctorDescription doctorDescription) {

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("fee", fee).append("geolocation", geolocation).append("lat", lat).append("_long", _long).append("address", address).append("address1", address1).append("city", city).append("type", type).append("hospitalId", hospitalId).append("location", location).append("hospitalName", hospitalName).append("userType", userType).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(geolocation);
        parcel.writeString(lat);
        parcel.writeString(_long);
        parcel.writeString(address);
        parcel.writeString(address1);
        parcel.writeString(city);
        parcel.writeString(type);
        parcel.writeString(hospitalId);
        parcel.writeString(location);
        parcel.writeString(hospitalId);
        parcel.writeString(userType);
    }
}
