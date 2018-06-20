package com.pagasys.welcome.pegasysattendence.models;

/**
 * Created by pegasys on 10/4/2017.
 */

public class LogoutModel {
    //    CharSequence mDate;
    String bioid;
    CharSequence logout_time;

    public LogoutModel() {
    }

    public LogoutModel(String bioid, CharSequence logout_time) {

//        this.mDate = mDate;
        this.bioid = bioid;
        this.logout_time = logout_time;
    }




/*
    public CharSequence getmDate() {
        return mDate;
    }

    public void setmDate(CharSequence mDate) {
        this.mDate = mDate;
    }*/
}
