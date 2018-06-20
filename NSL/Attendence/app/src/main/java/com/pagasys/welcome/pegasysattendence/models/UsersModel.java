package com.pagasys.welcome.pegasysattendence.models;

/**
 * Created by pegasys on 10/4/2017.
 */

public class UsersModel {
    //    CharSequence mDate;
    String bioid;
    CharSequence login_time;
    CharSequence logout_time;

    public UsersModel() {
    }

    public UsersModel(String bioid, CharSequence login_time, CharSequence logout_time) {

//        this.mDate = mDate;
        this.bioid = bioid;
        this.login_time = login_time;
        this.logout_time = logout_time;
    }

    public String getBioid() {
        return bioid;
    }

    public void setBioid(String bioid) {
        this.bioid = bioid;
    }

    public CharSequence getLogin_time() {
        return login_time;
    }

    public void setLogin_time(CharSequence login_time) {
        this.login_time = login_time;
    }

    public CharSequence getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(CharSequence logout_time) {
        this.logout_time = logout_time;
    }
    /*  public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }*/


/*
    public CharSequence getmDate() {
        return mDate;
    }

    public void setmDate(CharSequence mDate) {
        this.mDate = mDate;
    }*/
}
