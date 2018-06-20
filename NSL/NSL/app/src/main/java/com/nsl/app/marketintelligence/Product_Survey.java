package com.nsl.app.marketintelligence;


public class Product_Survey {

    //private variables
    int    _product_survey_id;
    String _product_survey_master_id;
    String _product_survey_company_name;
    String _product_survey_product_name;
    String _product_survey_name_of_the_check_segment;
    String _product_survey_launch_year;
    String _product_survey_no_units_sold;
    String _product_survey_area_crop_sown_new_product;
    String _product_survey_remarks_unique_feature;
    String _product_survey_created_by;
    String _product_survey_created_on;
    String _product_survey_ffmid;

    // Empty constructor
    public Product_Survey() {

    }

    // constructor
    public Product_Survey(int id, String master_id, String company_name, String product_name, String name_of_the_check_segment , String launch_year, String no_units_sold, String crop_sown_new_product, String unique_feature , String created_by , String created_on, String ffmid) {
        this._product_survey_id = id;
        this._product_survey_master_id = master_id;
        this._product_survey_company_name = company_name;
        this._product_survey_product_name = product_name;
        this._product_survey_name_of_the_check_segment = name_of_the_check_segment ;
        this._product_survey_launch_year = launch_year;
        this._product_survey_no_units_sold= no_units_sold;
        this._product_survey_area_crop_sown_new_product = crop_sown_new_product;
        this._product_survey_remarks_unique_feature = unique_feature;
        this._product_survey_created_by = created_by;
        this._product_survey_created_on = created_on;
        this._product_survey_ffmid = ffmid;
    }

    // constructor
    public Product_Survey(String master_id, String company_name, String product_name, String name_of_the_check_segment , String launch_year, String no_units_sold, String crop_sown_new_product, String unique_feature, String created_by, String created_on, String ffmid) {
        this._product_survey_master_id = master_id;
        this._product_survey_company_name = company_name;
        this._product_survey_product_name = product_name;
        this._product_survey_name_of_the_check_segment = name_of_the_check_segment;
        this._product_survey_launch_year = launch_year;
        this. _product_survey_no_units_sold= no_units_sold;
        this._product_survey_area_crop_sown_new_product = crop_sown_new_product;
        this._product_survey_remarks_unique_feature = unique_feature;
        this._product_survey_created_by = created_by;
        this._product_survey_created_on = created_on;
        this._product_survey_ffmid = ffmid;
    }

    public int getID() {
        return this._product_survey_id;
    }
    public void setID(int id) {
        this._product_survey_id = id;
    }


    public String get_product_survey_master_id() {
        return this._product_survey_master_id;
    }
    public void set_product_survey_master_id(String master_id) {this._product_survey_master_id = master_id; }


    public String get_product_survey_company_name() {
        return this._product_survey_company_name;
    }
    public void set_product_survey_company_name(String company_name) {this._product_survey_company_name = company_name; }

    public String get_product_survey_product_name() {return this._product_survey_product_name;}
    public void set_product_survey_product_name(String product_name) {this._product_survey_product_name = product_name; }


    public String get_product_survey_name_of_the_check_segment() { return this._product_survey_name_of_the_check_segment;    }
    public void set_product_survey_name_of_the_check_segment(String name_of_the_check_segment) { this._product_survey_name_of_the_check_segment = name_of_the_check_segment; }


    public String get_product_survey_launch_year() { return this._product_survey_launch_year; }
    public void set_product_survey_launch_year(String launch_year) { this._product_survey_launch_year = launch_year;  }


    public String get_product_survey_no_units_sold(){ return this._product_survey_no_units_sold;  }
    public void set_product_survey_no_units_sold(String no_units_sold) {this._product_survey_no_units_sold = no_units_sold; }

    public String get_product_survey_area_crop_sown_new_product() {return this._product_survey_area_crop_sown_new_product;}
    public void set_product_survey_area_crop_sown_new_product(String area_crop_sown_new_product ) {this._product_survey_area_crop_sown_new_product = area_crop_sown_new_product ;}


    public String get_product_survey_remarks_unique_feature() {return this._product_survey_remarks_unique_feature;}
    public void set_product_survey_remarks_unique_feature(String remarks_unique_feature) {this._product_survey_remarks_unique_feature = remarks_unique_feature;}

    public String get_product_survey_created_by() {return this._product_survey_created_by;}
    public void set_product_survey_created_by(String created_by) {this._product_survey_created_by = created_by;}


    public String get_product_survey_created_on() {return this._product_survey_created_on;}
    public void set_product_survey_created_on(String created_on) {this._product_survey_created_on = created_on;}

    public String get_product_survey_ffmid() {return this._product_survey_ffmid;}
    public void set_product_survey_ffmid(String ffmid) {this._product_survey_ffmid = ffmid;}



}
