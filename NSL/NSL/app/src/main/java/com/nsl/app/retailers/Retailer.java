package com.nsl.app.retailers;

/**
 * Created by admin on 2/21/2017.
 */
public class Retailer {
    int    _ret_id;
    String _ret_masterid;
    String _ret_name;
    String _ret_tin_no;
    String _ret_address;
    String _ret_phone;
    String _ret_mobile;
    String _ret_email_id;
    String _ret_dist_id;
    String _ret_dist_sap_code;
    String _ret_status;
    String _ret_cdatetime;
    String _ret_udatetime;
    String _ffmid;

    public Retailer(){

    }

    public Retailer(int id,
                    String ret_master_id,
                    String name,
                    String tin,
                    String ret_address,
                    String ret_phone,
                    String ret_mobile,
                    String ret_email,
                    String ret_dist_id,
                    String ret_dist_sap_code,
                    String ret_status,
                    String ret_cdatetime,
                    String ret_udatetime ,
                    String ffmid){

        this._ret_id           = id;
        this._ret_masterid      = ret_master_id;
        this._ret_name          = name;
        this._ret_tin_no          = tin;
        this._ret_address          = ret_address;
        this._ret_phone             = ret_phone;
        this._ret_mobile       = ret_mobile;
        this._ret_email_id         = ret_email;
        this._ret_dist_id      = ret_dist_id;
        this._ret_dist_sap_code        = ret_dist_sap_code;
        this._ret_status          = ret_status;
        this._ret_cdatetime       = ret_cdatetime;
        this._ret_udatetime     = ret_udatetime;
        this._ffmid           = ffmid;

    }

    // constructor
    public Retailer(String ret_master_id,
                    String name,
                    String tin,
                    String ret_address,
                    String ret_phone,
                    String ret_mobile,
                    String ret_email,
                    String ret_dist_id,
                    String ret_dist_sap_code,
                    String ret_status,
                    String ret_cdatetime,
                    String ret_udatetime ,
                    String ffmid){

        this._ret_masterid      = ret_master_id;
        this._ret_name          = name;
        this._ret_tin_no        = tin;
        this._ret_address       = ret_address;
        this._ret_phone         = ret_phone;
        this._ret_mobile        = ret_mobile;
        this._ret_email_id      = ret_email;
        this._ret_dist_id       = ret_dist_id;
        this._ret_dist_sap_code = ret_dist_sap_code;
        this._ret_status        = ret_status;
        this._ret_cdatetime     = ret_cdatetime;
        this._ret_udatetime     = ret_udatetime;
        this._ffmid             = ffmid;
    }
    // getting ID
    public int getID(){
        return this._ret_id;
    }

    // setting id
    public void setID(int id){
        this._ret_id = id;
    }

    public String get_ret_masterid(){
        return this._ret_masterid;
    }

    // setting name
    public void set_ret_masterid(String ret_masterid){
        this._ret_masterid = ret_masterid;
    }
    // getting name
    public String get_ret_name(){
        return this._ret_name;
    }

    // setting name
    public void set_ret_name(String ret_name){
        this._ret_name = ret_name;
    }

    public String get_ret_tin_no(){
        return this._ret_tin_no;
    }

    // setting name
    public void set_ret_tin_no(String ret_tin_no){
        this._ret_tin_no = ret_tin_no;
    }


    public String get_ret_address(){
        return this._ret_address;
    }

    // setting name
    public void set_ret_address(String address){
        this._ret_address = address;
    }


    public String get_email(){
        return this._ret_email_id;
    }

    // setting name
    public void set_email(String email){
        this._ret_email_id = email;
    }

    public String get_ret_status(){
        return this._ret_status;
    }

    // setting name
    public void set_ret_status(String status){
        this._ret_status = status;
    }

    // getting phone number
    public String get_ret_phone(){

        return this._ret_phone;
    }

    // setting phone number
    public void set_ret_phone(String ret_phone){

        this._ret_phone = ret_phone;
    }
    public String get_ret_mobile(){
        return this._ret_mobile;
    }

    // setting name
    public void set_ret_mobile(String ret_mobile){
        this._ret_mobile = ret_mobile;
    }

    public String get_ret_dist_id(){
        return this._ret_dist_id;
    }

    // setting name
    public void set_ret_dist_id(String dist_id){
        this._ret_dist_id = dist_id;
    }


    public String get_ret_dist_sap_code(){

        return this._ret_dist_sap_code;
    }

    // setting region_Id
    public void set_ret_dist_sap_code(String sap_code){

        this._ret_dist_sap_code = sap_code;
    }

    public String get_ret_cdatetime(){

        return this._ret_cdatetime;
    }
    // setting street
    public void set_ret_cdatetime(String cdatetime){

        this._ret_cdatetime = cdatetime;
    }
    public void set_ret_udatetime(String udatetime){

        this._ret_udatetime = udatetime;
    }
    public String get_ret_udatetime(){

        return this._ret_udatetime;
    }

    public String get_ffmid(){

        return this._ffmid;
    }

    // setting phone number
    public void set_ffmid(String ffmid){

        this._ffmid = ffmid;
    }

}
