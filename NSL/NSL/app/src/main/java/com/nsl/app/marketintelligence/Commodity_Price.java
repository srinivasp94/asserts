package com.nsl.app.marketintelligence;


public class Commodity_Price {

    //private variables
    int    _commodity_price_id;
    String _commodity_price_master_id;
    String _commodity_price_crop_name;
    String _commodity_price_variety_type;
    String _commodity_price_apmc_mandi_price;
    String _commodity_price_commodity_dealer_agent_price;
    String _commodity_price_purchage_price_by_industry;
    String _commodity_price_created_by;
    String _commodity_price_created_on;
    String _commodity_price_ffmid;

    // Empty constructor
    public Commodity_Price() {

    }

    // constructor
    public Commodity_Price(int id, String master_id, String crop_name, String variety_type, String apmc_mandi_price, String commodity_dealer_agent_price, String purchage_price_by_industry, String created_by, String created_on, String ffmid) {
        this._commodity_price_id = id;
        this._commodity_price_master_id = master_id;
        this._commodity_price_crop_name = crop_name;
        this._commodity_price_variety_type = variety_type;
        this._commodity_price_apmc_mandi_price = apmc_mandi_price;
        this._commodity_price_commodity_dealer_agent_price = commodity_dealer_agent_price;
        this._commodity_price_purchage_price_by_industry = purchage_price_by_industry;
        this._commodity_price_created_by = created_by;
        this._commodity_price_created_on = created_on;
        this._commodity_price_ffmid = ffmid;
    }

    // constructor
    public Commodity_Price(String master_id, String crop_name, String variety_type, String apmc_mandi_price, String commodity_dealer_agent_price, String purchage_price_by_industry, String created_by, String created_on,String ffmid ) {
        this._commodity_price_master_id = master_id;
        this._commodity_price_crop_name = crop_name;
        this._commodity_price_variety_type = variety_type;
        this._commodity_price_apmc_mandi_price = apmc_mandi_price;
        this._commodity_price_commodity_dealer_agent_price = commodity_dealer_agent_price;
        this._commodity_price_purchage_price_by_industry = purchage_price_by_industry;
        this._commodity_price_created_by = created_by;
        this._commodity_price_created_on = created_on;
        this._commodity_price_ffmid = ffmid;
    }

    public int getID() {
        return this._commodity_price_id;
    }
    public void setID(int id) {
        this._commodity_price_id = id;
    }


    public String get_commodity_price_master_id() {
        return this._commodity_price_master_id;
    }
    public void set_commodity_price_master_id(String master_id) {this._commodity_price_master_id = master_id; }


    public String get_commodity_price_crop_name() {
        return this._commodity_price_crop_name;
    }
    public void set_commodity_price_crop_name(String crop_name) {this._commodity_price_crop_name = crop_name; }

    public String get_commodity_price_variety_type() {return this._commodity_price_variety_type;}
    public void set_commodity_price_variety_type(String variety_type) {this._commodity_price_variety_type = variety_type; }


    public String get_commodity_price_apmc_mandi_price() { return this._commodity_price_apmc_mandi_price;    }
    public void set_commodity_price_apmc_mandi_price(String apmc_mandi_price) { this._commodity_price_apmc_mandi_price = apmc_mandi_price; }


    public String get_commodity_price_commodity_dealer_agent_price() { return this._commodity_price_commodity_dealer_agent_price; }
    public void set_commodity_price_commodity_dealer_agent_price(String commodity_dealer_agent_price) { this._commodity_price_commodity_dealer_agent_price = commodity_dealer_agent_price;  }


    public String get_commodity_price_purchage_price_by_industry(){ return this._commodity_price_purchage_price_by_industry;  }
    public void set_commodity_price_purchage_price_by_industry(String purchage_price_by_industry) {this._commodity_price_purchage_price_by_industry = purchage_price_by_industry; }


    public String get_commodity_price_created_by() {return this._commodity_price_created_by;}
    public void set_commodity_price_created_by(String created_by) {this._commodity_price_created_by = created_by;}


    public String get_commodity_price_created_on() {return this._commodity_price_created_on;}
    public void set_commodity_price_created_on(String created_on) {this._commodity_price_created_on = created_on;}

    public String get_commodity_price_ffmid() {return this._commodity_price_ffmid;}
    public void set_commodity_price_ffmid(String ffmid) {this._commodity_price_ffmid = ffmid;}


}
