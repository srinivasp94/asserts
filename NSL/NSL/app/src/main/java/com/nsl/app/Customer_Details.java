package com.nsl.app;


public class Customer_Details {

    //private variables
    int    _customer_details_id;
    String _customer_details_master_id;
    String _customer_details_division_id;
    String _customer_details_credit_limit;
    String _customer_details_inside_bucket;
    String _customer_details_outside_bucket;
    String _customer_details_status;
    String _customer_details_created_datetime;
    String _customer_details_updated_datetime;
    String _customer_details_credit_balance;

    // Empty constructor
    public Customer_Details() {

    }

    // constructor
    public Customer_Details(int id, String master_id, String division_id, String credit_limit, String inside_bucket, String outside_bucket, String status, String created_datetime, String updated_datetime) {
        this._customer_details_id = id;
        this._customer_details_master_id = master_id;
        this._customer_details_division_id = division_id;
        this._customer_details_credit_limit = credit_limit;
        this._customer_details_inside_bucket = inside_bucket;
        this._customer_details_outside_bucket = outside_bucket;
        this._customer_details_status = status;
        this._customer_details_created_datetime = created_datetime;
        this._customer_details_updated_datetime = updated_datetime;
    }

    // constructor
    public Customer_Details(String master_id, String division_id, String credit_limit, String inside_bucket, String outside_bucket, String status, String created_datetime, String updated_datetime, String credit_balance) {
        this._customer_details_master_id = master_id;
        this._customer_details_division_id = division_id;
        this._customer_details_credit_limit = credit_limit;
        this._customer_details_inside_bucket = inside_bucket;
        this._customer_details_outside_bucket = outside_bucket;
        this._customer_details_status = status;
        this._customer_details_created_datetime = created_datetime;
        this._customer_details_updated_datetime = updated_datetime;
        this._customer_details_credit_balance=credit_balance;
    }

    public int getID() {
        return this._customer_details_id;
    }
    public void setID(int id) {
        this._customer_details_id = id;
    }


    public String get_customer_details_master_id() {
        return this._customer_details_master_id;
    }
    public void set_customer_details_master_id(String master_id) {this._customer_details_master_id = master_id; }


    public String get_customer_details_division_id() {
        return this._customer_details_division_id;
    }
    public void set_customer_details_division_id(String division_id) {this._customer_details_division_id = division_id; }

    public String get_customer_details_credit_limit() {return this._customer_details_credit_limit;}
    public void set_customer_details_credit_limit(String credit_limit) {this._customer_details_credit_limit = credit_limit; }


    public String get_customer_details_inside_bucket() { return this._customer_details_inside_bucket;    }
    public void set_customer_details_inside_bucket(String inside_bucket) { this._customer_details_inside_bucket = inside_bucket; }


    public String get_customer_details_outside_bucket() { return this._customer_details_outside_bucket; }
    public void set_customer_details_outside_bucket(String outside_bucket) { this._customer_details_outside_bucket = outside_bucket;  }


    public String get_customer_details_status(){ return this._customer_details_status;  }

    public void set_customer_details_status(String customer_details_status) {this._customer_details_status = customer_details_status; }


    public String get_customer_details_created_datetime() {return this._customer_details_created_datetime;}
    public void set_customer_details_created_datetime(String created_datetime) {this._customer_details_created_datetime = created_datetime;}


    public String get_customer_details_updated_datetime() {return this._customer_details_updated_datetime;}
    public void set_customer_details_updated_datetime(String updated_datetime) {this._customer_details_updated_datetime = updated_datetime;}

    public int get_customer_details_id() {
        return _customer_details_id;
    }

    public void set_customer_details_id(int _customer_details_id) {
        this._customer_details_id = _customer_details_id;
    }

    public String get_customer_details_credit_balance() {
        return _customer_details_credit_balance;
    }

    public void set_customer_details_credit_balance(String _customer_details_credit_balance) {
        this._customer_details_credit_balance = _customer_details_credit_balance;
    }

}
