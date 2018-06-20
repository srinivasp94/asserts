package com.nsl.app.dailydairy;

/**
 * Created by admin on 1/31/2017.
 */

public class DailyDairy {

    int _dailydairyid=0;
    String _title="";
    int _userid=0;
    String _comments="";
    String _time="";
    String _date="";
    String _createddate="";
    String _ffmid="";
    String _tentative_time="";
    int _type=0;
    int _status=1;

    // Empty constructor
    public DailyDairy(){

    }
    // constructor
    public DailyDairy(int id, String title, int userid,  String comments, String time, String date,String createdate,String ffmid){
        this._dailydairyid = id;
        this._title = title;
        this._userid=userid;
        this._comments = comments;
        this._time = time;
        this._date = date;
        this._createddate = createdate;
        this._ffmid =ffmid;
    }

    // constructor
    public DailyDairy(String title, int userid, String comments, String time, String date, String createdate, String ffmid, int type, String tentative_time, int status){
        this._title = title;
        this._userid=userid;
        this._comments = comments;
        this._time = time;
        this._date = date;
        this._createddate = createdate;
        this._ffmid = ffmid;
        this._tentative_time = tentative_time;
        this._type = type;
        this._status=status;
    }
    // getting ID
    public int getID(){
        return this._dailydairyid;
    }

    // setting id
    public void setID(int id){
        this._dailydairyid = id;
    }

    // getting name
    public String get_title(){
        return this._title;
    }

    // setting name
    public void set_title(String title){
        this._title = title;
    }

    public int get_userid(){
        return this._userid;
    }

    // setting name
    public void set_userid(int userid){
        this._userid = userid;
    }

    // getting phone number
    public String get_comments(){
        return this._comments;
    }

    // setting phone number
    public void set_comments(String comments){
        this._comments = comments;
    }

    // getting phone number
    public String get_time(){
        return this._time;
    }
    // setting phone number
    public void set_time(String time){
        this._time = time;
    }

    // getting phone number
    public String get_date(){
        return this._date;
    }

    // setting phone number
    public void set_date(String date){
        this._date = date;
    }


    public String get_createddate(){
        return this._createddate;
    }

    // setting phone number
    public void set_createddate(String createddate){
        this._createddate = createddate;
    }


    public String get_ffmid() {
        return _ffmid;
    }

    public void set_ffmid(String _ffmid) {
        this._ffmid = _ffmid;
    }


    public String get_tentative_time() {
        return _tentative_time;
    }

    public void set_tentative_time(String _tentative_time) {
        this._tentative_time = _tentative_time;
    }

    public int get_type() {
        return _type;
    }

    public void set_type(int _type) {
        this._type = _type;
    }
    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }

}
