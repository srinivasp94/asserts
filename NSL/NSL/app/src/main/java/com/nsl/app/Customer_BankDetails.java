package com.nsl.app;

/**
 * Created by admin on 2/14/2017.
 */

public class Customer_BankDetails {

    //private variables
    int    _cust_bank_dtls_id;
    String _cus_bak_dtls_masterid;
    String _customer_id;
    String _account_no;
    String _account_name;
    String _bank_name;
    String _branch_name;
    String _ifsc_code;
    String _status;
    String _created_by;
    String _updated_by;
    String _created_date;
    String _ffmid;


    // Empty constructor
    public Customer_BankDetails(){

    }
    // constructor
    public Customer_BankDetails(int id,
                     String bank_detail_id,
                     String customer_id,
                     String account_no,
                     String account_name,
                     String bank_name,
                     String branch_name,
                     String ifsc_code,
                     String status,
                     String created_by,
                     String updated_by ,
                     String created_date,
                     String ffmid){
        this._cust_bank_dtls_id            = id;
        this._cus_bak_dtls_masterid      = bank_detail_id;
        this._customer_id          = customer_id;
        this._account_no          = account_no;
        this._account_name       = account_name;
        this._bank_name        = bank_name;
        this._branch_name          = branch_name;
        this._ifsc_code       = ifsc_code;
        this._status     = status;
        this._created_by     = created_by;
        this._updated_by    = updated_by;
        this._created_date        = created_date;
        this._ffmid     = ffmid;
    }

    // constructor
    public Customer_BankDetails(String bank_detail_id,
                                String customer_id,
                                String account_no,
                                String account_name,
                                String bank_name,
                                String branch_name,
                                String ifsc_code,
                                String status,
                                String created_by,
                                String updated_by ,
                                String created_date,
                                String ffmid){


        this._cus_bak_dtls_masterid      = bank_detail_id;
        this._customer_id          = customer_id;
        this._account_no          = account_no;
        this._account_name       = account_name;
        this._bank_name        = bank_name;
        this._branch_name          = branch_name;
        this._ifsc_code       = ifsc_code;
        this._status     = status;
        this._created_by     = created_by;
        this._updated_by    = updated_by;
        this._created_date        = created_date;
        this._ffmid     = ffmid;
    }

    // getting ID
    public int getID(){
        return this._cust_bank_dtls_id;
    }
    public void setID(int id){
        this._cust_bank_dtls_id = id;
    }

    public String get_cus_bak_dtls_masterid(){
        return this._cus_bak_dtls_masterid;
    }
    public void set_cus_bak_dtls_masterid(String cus_bak_dtls_masterid){
        this._cus_bak_dtls_masterid = cus_bak_dtls_masterid;
    }
    // getting name
    public String get_customer_id(){
        return this._customer_id;
    }
    public void set_customer_id(String customer_id){
        this._customer_id = customer_id;
    }

    public String get_account_no(){
        return this._account_no;
    }
    public void set_account_no(String account_no){
        this._account_no = account_no;
    }

    // getting phone number
    public String get_account_name(){
        return this._account_name;
    }
    public void set_account_name(String account_name){
        this._account_name = account_name;
    }


    public String get_bank_name(){

        return this._bank_name;
    }

    // setting region_Id
    public void set_bank_name(String bank_name){

        this._bank_name = bank_name;
    }

    public String get_branch_name(){

        return this._branch_name;
    }
    // setting street
    public void set_branch_name(String branch_name){

        this._branch_name = branch_name;
    }

    public String get_ifsc_code(){

        return this._ifsc_code;
    }
    public void set_ifsc_code(String ifsc_code){

        this._ifsc_code = ifsc_code;
    }


    public String get_status(){

        return this._status;
    }

    // setting phone number
    public void set_status(String status){

        this._status = status;
    }
    public String get_created_by(){

        return this._created_by;
    }

    // setting address
    public void set_created_by(String created_by){

        this._created_by = created_by;
    }
    public String get_updated_by(){

        return this._updated_by;
    }

    // setting address
    public void set_updated_by(String updated_by){

        this._updated_by = updated_by;
    }

    // getting status
    public String get_created_date(){

        return this._created_date;
    }

    // setting status
    public void set_created_date(String created_date){

        this._created_date = created_date;
    }
    public String get_ffmid(){

        return this._ffmid;
    }

    // setting phone number
    public void set_ffmid(String ffmid){

        this._ffmid = ffmid;
    }


}
