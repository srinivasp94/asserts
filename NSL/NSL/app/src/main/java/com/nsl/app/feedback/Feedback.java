package com.nsl.app.feedback;

/**
 * Created by admin on 12/19/2016.
 */
public class Feedback {


   //private variables
   public int    _feedback_id;
    public int    _user_id;
    public String _farmer_name;
    public String _place;
    public String _contact_no;
    public String _crop;
    public String _hybrid;
    public String _sowing_date;
    public  String _feedback_message;
    public String _image;
    public String _ffmid;
    public String image_64;

    public String getImage_64() {
        return image_64;
    }

    public void setImage_64(String image_64) {
        this.image_64 = image_64;
    }



    // Empty constructor
    public Feedback(){

    }
    // constructor
    public Feedback(int id,int user_id, String farmer_name,String place, String contact_no , String crop , String hybrid, String sowing_date, String feedback_message,  String image, String ffmid){
        this._feedback_id          = id;
        this._user_id             = user_id;
        this._farmer_name   = farmer_name;
        this._place        = place;
        this._contact_no      = contact_no;
        this._crop        = crop;
        this._hybrid      = hybrid;
        this._sowing_date   = sowing_date;
        this._feedback_message   = feedback_message;
        this._image = image;
        this._ffmid = ffmid;
    }

    // constructor
    public Feedback(int user_id,String farmer_name,String place, String contact_no, String crop, String hybrid, String sowing_date, String feedback_message, String image,String ffmid){
        this._user_id = user_id;
        this._farmer_name   = farmer_name;
        this._place        = place;
        this._contact_no      = contact_no;
        this._crop        = crop;
        this._hybrid      = hybrid;
        this._sowing_date   = sowing_date;
        this._feedback_message   = feedback_message;
        this._image = image;
        this._ffmid = ffmid;
    }

    public int getID(){
        return this._feedback_id;
    }
    public void setID(int id){
        this._feedback_id = id;
    }


    public int get_user_id(){
        return this._user_id;
    }
    public void set_user_id(int user_id){
        this._user_id = user_id;
    }



    public String getFarmerName(){
        return this._farmer_name;
    }


    public void setFarmerName(String farmer_name){
        this._farmer_name = farmer_name;
    }

    public String getplace(){
        return this._place;
    }


    public void setPlace(String place){
        this._place = place;
    }

    public String getContactNo(){
        return this._contact_no;
    }


    public void setContactNo(String contact_no){
        this._contact_no = contact_no;
    }


    public String getCrop(){

        return this._crop;
    }


    public void setCrop(String crop){

        this._crop = crop;
    }

    public String getHybrid(){

        return this._hybrid;
    }

    public void setHybrid(String hybrid){

        this._hybrid = hybrid;



    }
    public String getSowingDate(){

        return this._sowing_date;
    }

     public void setSowingDate(String sowingDate){

        this._sowing_date = sowingDate;
    }


    public String getfeedbackmessage(){

        return this._feedback_message;
    }

    public void setFeedbackMessage(String feedbackMessage){

        this._feedback_message = feedbackMessage;
    }

    public String getImage(){

        return this._image;
    }

    public void setImage(String image){

        this._image = image;
    }

    public String get_ffmid(){

        return this._ffmid;
    }

    public void set_ffmid(String ffmid){

        this._ffmid = ffmid;
    }
}
