package com.nsl.app;

public class Products {

    //private variables
    int    _product_id;
    String _product_masterid;
    String _product_name;
    String _product_description;
    String _product_sap_code;
    String _product_crop_id;
    String _product_company_id;
    String _product_division_id;
    String _product_region;
    String _product_price;
    String _product_discount;
    String _product_from_date;
    String _product_to_date;
    String _product_status;
    String _product_cdatetime;
    String _product_udatetime;
    String _products_images;
    String _product_videos;
    String _product_brand_name;

    // Empty constructor
    public Products() {

    }

    // constructor
    public Products(int id, String product_masterid, String name, String product_description, String product_sap_code, String product_crop_id, String product_company_id, String product_division_id, String product_region, String product_price, String product_discount, String product_from_date, String product_to_date, String product_status, String products_images, String product_videos, String product_cdatetime, String product_udatetime, String product_brand_name) {
        this._product_id = id;
        this._product_masterid = product_masterid;
        this._product_name = name;
        this._product_description = product_description;
        this._product_sap_code = product_sap_code;
        this._product_crop_id = product_crop_id;
        this._product_company_id = product_company_id;
        this._product_division_id = product_division_id;
        this._product_region = product_region;
        this._product_price = product_price;
        this._product_discount = product_discount;
        this._product_from_date = product_from_date;
        this._product_to_date = product_to_date;
        this._products_images = products_images;
        this._product_videos = product_videos;


        this._product_status     = product_status;
        this._product_cdatetime  = product_cdatetime;
        this._product_udatetime  = product_udatetime;
        this._product_brand_name = product_brand_name;
    }

    // constructor
    public Products(String product_masterid, String name, String product_description, String product_sap_code, String product_crop_id, String product_company_id, String product_division_id, String product_region, String product_price, String product_discount, String product_from_date, String product_to_date, String product_status, String products_images, String product_videos, String product_cdatetime, String product_udatetime, String product_brand_name) {
        this._product_masterid = product_masterid;
        this._product_name = name;
        this._product_description = product_description;
        this._product_sap_code = product_sap_code;
        this._product_crop_id = product_crop_id;
        this._product_company_id = product_company_id;
        this._product_division_id = product_division_id;
        this._product_region = product_region;
        this._product_price = product_price;
        this._product_discount = product_discount;
        this._product_from_date = product_from_date;
        this._product_to_date = product_to_date;
        this._products_images = products_images;
        this._product_videos = product_videos;
        this._product_status = product_status;
        this._product_cdatetime = product_cdatetime;
        this._product_udatetime = product_udatetime;
        this._product_brand_name = product_brand_name;
    }

    // getting ID
    public int getID() {
        return this._product_id;
    }

    // setting id
    public void setID(int id) {
        this._product_id = id;
    }

    public String getProductMasterId() {
        return this._product_masterid;
    }

    // setting name
    public void setProductMasterID(String product_masterid) {
        this._product_masterid = product_masterid;
    }

    // getting name
    public String getProductName() {
        return this._product_name;
    }

    // setting name
    public void setProductName(String product_name) {
        this._product_name = product_name;
    }

    public String getProduct_description() {
        return this._product_description;
    }

    // setting name
    public void setProduct_description(String product_description) {
        this._product_description = product_description;
    }

    public String getProduct_crop_id() {
        return this._product_crop_id;
    }

    // setting name
    public void setProduct_crop_id(String product_crop_id) {
        this._product_crop_id = product_crop_id;
    }

    public String getProduct_company_id() {
        return this._product_company_id;
    }

    // setting name
    public void setProduct_company_id(String product_company_id) {
        this._product_company_id = product_company_id;
    }

    public String getProduct_division_id() {
        return this._product_division_id;
    }

    // setting name
    public void setProduct_division_id(String product_region) {
        this._product_region = product_region;
    }

    public String getProduct_region() {
        return this._product_division_id;
    }

    // setting name
    public void setProduct_region(String product_region) {
        this._product_region = product_region;
    }

    public String getProduct_price() {
        return this._product_price;
    }

    // setting name
    public void setProduct_price(String product_price) {
        this._product_price = product_price;
    }

    public String getProduct_discount() {
        return this._product_discount;
    }

    // setting name
    public void setProduct_discount(String product_discount) {
        this._product_discount = product_discount;
    }


    // getting phone number
    public String getProduct_sap_code() {

        return this._product_sap_code;
    }

    // setting phone number
    public void setProduct_sap_code(String product_sap_code) {

        this._product_sap_code = product_sap_code;
    }

    public String getProduct_from_date() {

        return this._product_from_date;
    }

    // setting phone number
    public void setProduct_from_date(String product_from_date) {

        this._product_from_date = product_from_date;
    }

    public String getProduct_to_date() {

        return this._product_to_date;
    }

    // setting phone number
    public void setProduct_to_date(String product_to_date) {

        this._product_to_date = product_to_date;
    }

    // setting phone number
    public void setProduct_status(String product_status) {

        this._product_status = product_status;
    }

    public String getProduct_status() {

        return this._product_status;
    }

    public void setProduct_images(String products_images) {

        this._products_images = products_images;
    }

    public String getProducts_images() {

        return this._products_images;
    }

    public void setProduct_videos(String product_videos) {

        this._product_videos = product_videos;
    }

    public String getProduct_videos() {

        return this._product_videos;
    }

    // setting phone number
    public String getProductcdatetime() {

        return this._product_cdatetime;
    }

    public void setProductcdatetime(String product_cdatetime) {

        this._product_cdatetime = product_cdatetime;
    }

    public String getProductudatetime() {

        return this._product_udatetime;
    }

    // setting phone number
    public void setProductudatetime(String product_udatetime) {

        this._product_udatetime = product_udatetime;
    }
    public String getProduct_brand_name() {

        return this._product_brand_name;
    }

    // setting phone number
    public void setProduct_brand_name(String product_brand_name) {

        this._product_brand_name = product_brand_name;
    }
}
