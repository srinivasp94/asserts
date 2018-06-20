package com.nsl.app.marketintelligence;


public class Price_Survey {

    //private variables
    int    _price_survey_id;
    String _price_survey_master_id;
    String _price_survey_company_name;
    String _price_survey_product_name;
    String _price_survey_sku_pack_size;
    String _price_survey_retail_price;
    String _price_survey_invoice_price;
    String _price_survey_net_distributor_landing_price;
    String _price_survey_created_by;
    String _price_survey_created_on;
    String _price_survey_ffmid;

    // Empty constructor
    public Price_Survey() {

    }

    // constructor
    public Price_Survey(int id, String master_id, String company_name, String product_name, String sku_pack_size, String retail_price, String invoice_price, String net_distributor_landing_price, String created_by, String created_on, String ffmid) {
        this._price_survey_id = id;
        this._price_survey_master_id = master_id;
        this._price_survey_company_name = company_name;
        this._price_survey_product_name = product_name;
        this._price_survey_sku_pack_size = sku_pack_size ;
        this._price_survey_retail_price = retail_price;
        this._price_survey_invoice_price= invoice_price;
        this._price_survey_net_distributor_landing_price = net_distributor_landing_price;
        this._price_survey_created_by = created_by;
        this._price_survey_created_on = created_on;
        this._price_survey_ffmid = ffmid;
    }

    // constructor
    public Price_Survey(String master_id, String company_name, String product_name, String sku_pack_size, String retail_price, String invoice_price, String net_distributor_landing_price, String created_by, String created_on, String ffmid) {
        this._price_survey_master_id = master_id;
        this._price_survey_company_name = company_name;
        this._price_survey_product_name = product_name;
        this._price_survey_sku_pack_size = sku_pack_size;
        this._price_survey_retail_price = retail_price;
        this._price_survey_invoice_price = invoice_price;
        this._price_survey_net_distributor_landing_price = net_distributor_landing_price;
        this._price_survey_created_by = created_by;
        this._price_survey_created_on = created_on;
        this._price_survey_ffmid = ffmid;
    }

    public int getID() {
        return this._price_survey_id;
    }
    public void setID(int id) {
        this._price_survey_id = id;
    }


    public String get_price_survey_master_id() {
        return this._price_survey_master_id;
    }
    public void set_price_survey_master_id(String master_id) {this._price_survey_master_id = master_id; }


    public String get_price_survey_company_name() {
        return this._price_survey_company_name;
    }
    public void set_price_survey_company_name(String company_name) {this._price_survey_company_name = company_name; }

    public String get_price_survey_product_name() {return this._price_survey_product_name;}
    public void set_price_survey_product_name(String product_name) {this._price_survey_product_name = product_name; }


    public String get_price_survey_sku_pack_size() { return this._price_survey_sku_pack_size;    }
    public void set_price_survey_sku_pack_size(String sku_pack_size) { this._price_survey_sku_pack_size = sku_pack_size; }


    public String get_price_survey_retail_price() { return this._price_survey_retail_price; }
    public void set_price_survey_retail_price(String retail_price) { this._price_survey_retail_price = retail_price;  }


    public String get_price_survey_invoice_price(){ return this._price_survey_invoice_price;  }
    public void set_price_survey_invoice_price(String invoice_price) {this._price_survey_invoice_price = invoice_price; }

    public String get_price_survey_net_distributor_landing_price() {return this._price_survey_net_distributor_landing_price;}
    public void set_price_survey_net_distributor_landing_price(String net_distributor_landing_price ) {this._price_survey_net_distributor_landing_price = net_distributor_landing_price ;}

    public String get_price_survey_created_by() {return this._price_survey_created_by;}
    public void set_price_survey_created_by(String created_by) {this._price_survey_created_by = created_by;}


    public String get_price_survey_created_on() {return this._price_survey_created_on;}
    public void set_price_survey_created_on(String created_on) {this._price_survey_created_on = created_on;}

    public String get_price_survey_ffmid() {return this._price_survey_ffmid;}
    public void set_price_survey_ffmid(String ffmid) {this._price_survey_ffmid = ffmid;}


}
