package com.nsl.app;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Praveen on 1/29/2017.
 */
public class MarkerData {
    LatLng latLng;
    String str_name = "";
    String str_address = "";

    public MarkerData(LatLng latLng,String str_name,String str_address){
        this.latLng = latLng;
        this.str_name = str_name;
        this.str_address = str_address;
    }
}

