package com.nsl.app;

public class Products_Pojo {

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
    String _product_no_packets;
    String _product_catalog_url;
    String _product_brand_name;

    // Empty constructor
    public Products_Pojo() {

    }


    // constructor
    public Products_Pojo(String product_masterid,
                         String product_name,
                         String product_description,
                         String product_sap_code,
                         String product_crop_id,
                         String product_company_id,
                         String product_division_id,
                         String product_region,
                         String product_price,
                         String product_discount,
                         String product_from_date,
                         String product_to_date,
                         String product_status,
                         String product_cdatetime,
                         String product_udatetime,
                         String products_images,
                         String product_videos,
                         String product_no_of_packets,
                         String product_catalog_url,
                         String product_brand_name) {
        this._product_masterid = product_masterid;
        this._product_name = product_name;
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
        this._product_status = product_status;
        this._product_cdatetime = product_cdatetime;
        this._product_udatetime = product_udatetime;
        this._products_images = products_images;
        this._product_videos = product_videos;
        this._product_no_packets = product_no_of_packets;
        this._product_catalog_url = product_catalog_url;
        this._product_brand_name = product_brand_name;
    }

    // constructor
    public Products_Pojo(String product_id,
                         String product_price,
                         String product_discount,
                         String product_from_date,
                         String product_to_date,
                         String product_status,
                         String reason_id
                         ) {
        this._product_masterid = product_id;
        this._product_price = product_price;
        this._product_discount = product_discount;
        this._product_from_date = product_from_date;
        this._product_to_date = product_to_date;
        this._product_status = product_status;
        this._product_region=reason_id;

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
    public void setProductMasterId(String product_masterid) {
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

    public String getProductDescription() {
        return this._product_description;
    }

    // setting name
    public void setProductDescription(String product_description) {
        this._product_description = product_description;
    }

    // getting phone number
    public String getProductSapCode() {

        return this._product_sap_code;
    }

    // setting phone number
    public void setProductSapCode(String product_sap_code) {

        this._product_sap_code = product_sap_code;
    }

    // getting phone number
    public String getProductcropid() {

        return this._product_crop_id;
    }

    // setting phone number
    public void setProductcropid(String product_crop_id) {

        this._product_crop_id = product_crop_id;
    }

    public String getProductcompanyid() {

        return this._product_company_id;
    }


    public void setProductcompanyid(String product_company_id) {

        this._product_company_id = product_company_id;
    }

    public String getProductdivisionid() {

        return this._product_division_id;
    }


    public void setProductdivisionid(String product_division_id) {

        this._product_division_id = product_division_id;
    }

    public String getProductregeion() {

        return this._product_region;
    }


    public void setProductregeion(String product_region) {

        this._product_region = product_region;
    }

    public String getProductprice() {

        return this._product_price;
    }


    public void setProductprice(String product_price) {

        this._product_price = product_price;
    }

    public String getProductdiscount() {

        return this._product_discount;
    }


    public void setProductdiscount(String product_discount) {

        this._product_discount = product_discount;
    }

    public String getProductfromdate() {

        return this._product_from_date;
    }


    public void setProductfromdate(String product_from_date) {

        this._product_from_date = product_from_date;
    }

    public String getProducttodate() {

        return this._product_to_date;
    }


    public void setProducttodate(String product_to_date) {

        this._product_to_date = product_to_date;
    }

    public String getProductstatus() {

        return this._product_status;
    }


    public void setProductstatus(String product_status) {

        this._product_status = product_status;
    }

    public String getProductcdatetime() {

        return this._product_cdatetime;
    }


    public void setProductcdatetime(String product_cdatetime) {

        this._product_cdatetime = product_cdatetime;
    }

    public String getProductudatetime() {

        return this._product_udatetime;
    }


    public void setProductudatetime(String product_udatetime) {

        this._product_udatetime = product_udatetime;
    }

    public String getProductImages() {

        return this._products_images;
    }


    public void setProductImages(String products_images) {

        this._products_images = products_images;
    }

    public String getProductVideos() {

        return this._product_videos;
    }


    public void setProductVideos(String product_videos) {

        this._product_videos = product_videos;
    }

    public String get_product_no_packets() {
        return _product_no_packets;
    }

    public void set_product_no_packets(String _product_no_packets) {
        this._product_no_packets = _product_no_packets;
    }

    public String get_product_catalog_url() {
        return _product_catalog_url;
    }

    public void set_product_catalog_url(String _product_catalog_url) {
        this._product_catalog_url = _product_catalog_url;
    }
    public String getProduct_brand_name() {

        return this._product_brand_name;
    }

    // setting phone number
    public void setProduct_brand_name(String product_brand_name) {

        this._product_brand_name = product_brand_name;
    }
}
