package com.nsl.app;

/**
 * Created by admin on 12/27/2016.
 */

public class Payment_collection {
    //private variables
    String    _payment_id;
    String _payment_type;
    String    _user_id;
    String    _company_id;
    String    _division_id;
    String    _customer_id;
    String _total_amount;
    String _payment_mode;
    String _bank_name;
    String _rtgs_or_neft_no;
    String _payment_datetime;
    String _date_on_cheque_no;
    String _cheque_no_dd_no;
    int    _status;
    String _created_datetime;
    String _updated_datetime;
    String _ffmid;

    // Empty constructor
    public Payment_collection(){

    }
    // constructor
    public Payment_collection(String id,  String payment_type,String user_id, String company_id , String division_id ,
                              String customer_id, String total_amount,String payment_mode,String bank_name,
                              String rtgs_or_neft_no,String payment_datetime,String date_on_cheque_no,
                              String cheque_no_dd_no,int status,String created_datetime, String updated_datetime, String ffmid){

        this._payment_id          = id;
        this._payment_type   = payment_type;
        this._user_id        = user_id;
        this._company_id      = company_id;
        this._division_id        = division_id;
        this._customer_id      = customer_id;
        this._total_amount   = total_amount;
        this._payment_mode   = payment_mode;
        this._bank_name = bank_name;
        this._rtgs_or_neft_no=rtgs_or_neft_no;
        this._payment_datetime      = payment_datetime;
        this._date_on_cheque_no        = date_on_cheque_no;
        this._cheque_no_dd_no      = cheque_no_dd_no;
        this._status   = status;
        this._created_datetime = created_datetime;
        this._updated_datetime=updated_datetime;
        this._ffmid=ffmid;
    }

    // constructor
    public Payment_collection( String payment_type,String user_id, String company_id , String division_id ,
                               String customer_id, String total_amount,String payment_mode,String bank_name,
                               String rtgs_or_neft_no,String payment_datetime,String date_on_cheque_no,
                               String cheque_no_dd_no,int status,String created_datetime, String updated_datetime){
        this._payment_type   = payment_type;
        this._user_id        = user_id;
        this._company_id      = company_id;
        this._division_id        = division_id;
        this._customer_id      = customer_id;
        this._total_amount   = total_amount;
        this._payment_mode   = payment_mode;
        this._bank_name = bank_name;
        this._rtgs_or_neft_no=rtgs_or_neft_no;
        this._payment_datetime      = payment_datetime;
        this._date_on_cheque_no        = date_on_cheque_no;
        this._cheque_no_dd_no      = cheque_no_dd_no;
        this._status   = status;
        this._created_datetime = created_datetime;
        this._updated_datetime=updated_datetime;
    }

    public String getID(){
        return this._payment_id;
    }


    public void setID(String id){
        this._payment_id = id;
    }


    public String get_payment_type(){
        return this._payment_type;
    }


    public void set_payment_type(String payment_type){
        this._payment_type = payment_type;
    }

    public String get_user_id(){
        return this._user_id;
    }


    public void set_user_id(String user_id){
        this._user_id = user_id;
    }

    public String get_company_id(){
        return this._company_id;
    }


    public void set_company_id(String company_id){
        this._company_id = company_id;
    }


    public String get_division_id(){

        return this._division_id;
    }


    public void set_division_id(String division_id){

        this._division_id = division_id;
    }

    public String get_customer_id(){

        return this._customer_id;
    }

    public void set_customer_id(String customer_id){

        this._customer_id = customer_id;



    }
    public String get_total_amount(){

        return this._total_amount;
    }

    public void set_total_amount(String total_amount){

        this._total_amount = total_amount;
    }


    public String get_payment_mode(){

        return this._payment_mode;
    }

    public void set_payment_mode(String payment_mode){

        this._payment_mode = payment_mode;
    }

    public String get_bank_name(){

        return this._bank_name;
    }

    public void set_bank_name(String bank_name){

        this._bank_name = bank_name;
    }
    public String get_rtgs_or_neft_no(){

        return this._rtgs_or_neft_no;
    }

    public void set_rtgs_or_neft_no(String rtgs_or_neft_no){

        this._rtgs_or_neft_no = rtgs_or_neft_no;
    }
    public String get_payment_datetime(){

        return this._payment_datetime;
    }

    public void set_payment_datetime(String payment_datetime){

        this._payment_datetime = payment_datetime;
    }
    public String get_date_on_cheque_no(){

        return this._date_on_cheque_no;
    }

    public void set_date_on_cheque_no(String date_on_cheque_no){

        this._date_on_cheque_no = date_on_cheque_no;
    }
    public String get_cheque_no_dd_no(){

        return this._cheque_no_dd_no;
    }

    public void set_cheque_no_dd_no(String cheque_no_dd_no){

        this._cheque_no_dd_no = cheque_no_dd_no;
    }
    public int get_status(){

        return this._status;
    }

    public void set_status(int status){

        this._status = status;
    }

    public String get_created_datetime(){

        return this._created_datetime;
    }

    public void set_created_datetime(String created_datetime){

        this._created_datetime = created_datetime;
    }
    public String get_updated_datetime(){

        return this._updated_datetime;
    }

    public void set_updated_datetime(String updated_datetime){

        this._updated_datetime = updated_datetime;
    }

    public String get_ffmid(){
        return this._ffmid;
    }

    public void set_ffmid(String ffmid){
        this._ffmid=ffmid;
    }
}
