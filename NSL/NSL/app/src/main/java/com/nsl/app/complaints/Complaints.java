package com.nsl.app.complaints;

/**
 * Created by admin on 12/20/2016.
 */
public class Complaints {

    public int _complaint_id;
    public int _user_id;
    public int _company_id;
    public int _division_id;
    public int _crop_id;
    public int _product_id;
    public String _marketing_lot_number;
    public  String _complaint_type;
    public String _farmer_name;
    public String _contact_no;
    public String _complaint_area_acres;
    public String _soil_type;
    public String _others;
    public String _purchased_quantity;
    public String _complaint_quantity;
    public String _purchase_date;
    public String _bill_number;
    public String _retailer_name;
    public int _distributor;
    public String _mandal;
    public String _village;
    public String _image;
    public String _regulatory_type;
    public String _sampling_date;
    public String _place_sampling;
    public String _sampling_officer_name;
    public String _sampling_officer_contact;
    public String _comments;
    public int _status;
    public String _remarks;
    public String _created_datetime;
    public String _updated_datetime;
    public String _ffmid;

    // Empty constructor
    public Complaints( ){}



    // constructor
    public Complaints(int id,int user_id, int company_id, int division_id, int crop_id, int product_id, String marketing_lot_number, String complaint_type, String farmer_name, String contact_no, String complaint_area_acres, String soil_type,
                      String others, String purchased_quantity, String complaint_quantity, String purchase_date, String bill_number, String retailer_name, int distributor, String mandal, String village, String image,
                      String regulatory_type, String sampling_date, String place_sampling, String sampling_officer_name, String sampling_officer_contact,
                      String comments, int status,String remarks, String created_datetime, String updated_datetime,String ffmid) {
        this._complaint_id = id;
        this._user_id = user_id;
        this._company_id = company_id;
        this._division_id = division_id;
        this._crop_id = crop_id;
        this._product_id = product_id;
        this._marketing_lot_number = marketing_lot_number;
        this._complaint_type = complaint_type;
        this._farmer_name = farmer_name;
        this._contact_no = contact_no;
        this._complaint_area_acres = complaint_area_acres;
        this._soil_type = soil_type;
        this._others = others;
        this._purchased_quantity = purchased_quantity;
        this._complaint_quantity = complaint_quantity;
        this._purchase_date = purchase_date;
        this._bill_number = bill_number;
        this._retailer_name = retailer_name;
        this._distributor = distributor;
        this._mandal = mandal;
        this._village = village;
        this._image = image;
        this._regulatory_type = regulatory_type;
        this._sampling_date = sampling_date;
        this._place_sampling = place_sampling;
        this._sampling_officer_name = sampling_officer_name;
        this._sampling_officer_contact = sampling_officer_contact;
        this._comments = comments;
        this._status = status;
        this._remarks = remarks;
        this._created_datetime = created_datetime;
        this._updated_datetime = updated_datetime;
        this._ffmid = ffmid;
    }

    // constructor
    public Complaints(int user_id, int company_id, int division_id, int crop_id, int product_id, String marketing_lot_number, String complaint_type, String farmer_name, String contact_no, String complaint_area_acres, String soil_type,
                      String others, String purchased_quantity, String complaint_quantity, String purchase_date, String bill_number, String retailer_name, int distributor, String mandal, String village, String image,
                      String regulatory_type, String sampling_date, String place_sampling, String sampling_officer_name, String sampling_officer_contact,
                      String comments, int status,String remarks, String created_datetime, String updated_datetime,String ffmid)
    {
        this._user_id = user_id;
        this._company_id = company_id;
        this._division_id = division_id;
        this._crop_id = crop_id;
        this._product_id = product_id;
        this._marketing_lot_number = marketing_lot_number;

        this._complaint_type = complaint_type;
        this._farmer_name = farmer_name;
        this._contact_no = contact_no;
        this._complaint_area_acres = complaint_area_acres;
        this._soil_type = soil_type;

        this._others = others;
        this._purchased_quantity = purchased_quantity;
        this._complaint_quantity = complaint_quantity;
        this._purchase_date = purchase_date;
        this._bill_number = bill_number;

        this._retailer_name = retailer_name;
        this._distributor = distributor;
        this._mandal = mandal;
        this._village = village;
        this._image = image;

        this._regulatory_type = regulatory_type;
        this._sampling_date = sampling_date;
        this._place_sampling = place_sampling;
        this._sampling_officer_name = sampling_officer_name;
        this._sampling_officer_contact = sampling_officer_contact;

        this._comments = comments;
        this._status = status;
        this._remarks = remarks;
        this._created_datetime = created_datetime;
        this._updated_datetime = updated_datetime;
        this._ffmid = ffmid;
    }

    public int getID() {
        return this._complaint_id;
    }
    public void setID(int id) {
        this._complaint_id = id;
    }

    public int get_user_id() {
        return this._user_id;
    }
    public void set_user_id(int user_id) {
        this._user_id = user_id;
    }

    public int getCompanyId() { return this._company_id; }
    public void setCompanyId(int companyId) {
        this._company_id = companyId;
    }

    public int get_division_id() { return this._division_id; }
    public void set_division_id(int division_id) {
        this._division_id = division_id;
    }

    public int getCropid() {
        return this._crop_id;
    }
    public void setCropid(int cropid) {
        this._crop_id = cropid;
    }

    public int getProductid() {
        return this._product_id;
    }
    public void setProductid(int productid) {
        this._product_id = productid;
    }

    public String get_marketing_lot_number() { return this._marketing_lot_number; }
    public void set_marketing_lot_number(String marketing_lot_number){this._marketing_lot_number = marketing_lot_number;}

    public String get_complaint_type() { return this._complaint_type;}
    public void set_complaint_type(String complaint_type) {this._complaint_type = complaint_type;}

    public String get_farmer_name() { return this._farmer_name; }
    public void set_farmer_name(String farmer_name) {
        this._farmer_name = farmer_name;
    }

    public String get_contact_no() { return this._contact_no;}
    public void set_contact_no(String contact_no) {
        this._contact_no = contact_no;
    }

    public String get_complaint_area_acres() {
        return this._complaint_area_acres;
    }
    public void set_complaint_area_acres(String complaint_area_acres) {this._complaint_area_acres = complaint_area_acres;}

    public String get_soil_type() { return this._soil_type; }
    public void set_soil_type(String soil_type){this._soil_type = soil_type;}

    public String get_others() { return this._others;}
    public void set_others(String others) {this._others = others;}

    public String get_purchased_quantity() { return this._purchased_quantity;}
    public void set_purchased_quantity(String purchased_quantity){this._purchased_quantity=purchased_quantity;}

    public String get_complaint_quantity() { return this._complaint_quantity;}
    public void set_complaint_quantity(String complaint_quantity){this._complaint_quantity=complaint_quantity;}

    public String get_purchase_date() { return this._purchase_date;}
    public void set_purchase_date(String purchase_date){this._purchase_date=purchase_date;}

    public String get_bill_number() {
        return this._bill_number;
    }
    public void set_bill_number(String bill_number){
        this._bill_number = bill_number;
    }

    public String get_retailer_name() { return this._retailer_name;}
    public void set_retailer_name(String retailer_name) {this._retailer_name = retailer_name;}

    public int get_distributor() { return this._distributor;}
    public void set_distributor(int distributor){this._distributor=distributor;}

    public String get_mandal() { return this._mandal;}
    public void set_mandal(String mandal){this._mandal= mandal;}

    public String get_village() { return this._village;}
    public void set_village(String village){this._village = village;}

    public String get_image() { return this._image;}
    public void set_image(String image){this._image=image;}

    public String get_regulatory_type() { return this._regulatory_type;}
    public void set_regulatory_type(String regulatory_type) {this._regulatory_type = regulatory_type;}

    public String get_sampling_date() { return this._sampling_date;}
    public void set_sampling_date(String sampling_date){this._sampling_date = sampling_date;}

    public String get_place_sampling() { return this._place_sampling;}
    public void set_place_sampling(String place_sampling){this._place_sampling=place_sampling;}

    public String get_sampling_officer_name() { return this._sampling_officer_name;}
    public void set_sampling_officer_name(String sampling_officer_name){this._sampling_officer_name=sampling_officer_name;}

    public String get_sampling_officer_contact() { return this._sampling_officer_contact;}
    public void set_sampling_officer_contact(String sampling_officer_contact){this._sampling_officer_contact=sampling_officer_contact;}

    public String get_comments() { return this._comments;}
    public void set_comments(String comments){this._comments=comments;}

    public int get_status() { return this._status;}
    public void set_status(int status){this._status=status;}

    public String get_remarks() { return this._remarks;}
    public void set_remarks(String remarks){this._remarks=remarks;}

    public String get_created_datetime() { return this._created_datetime;}
    public void set_created_datetime(String created_datetime){this._created_datetime=created_datetime;}

    public String get_updated_datetime() { return this._updated_datetime;}
    public void set_updated_datetime(String updated_datetime){this._updated_datetime=updated_datetime;}


    public String get_ffmid(){
        return this._ffmid;
    }

    public void set_ffmid(String ffmid){
        this._ffmid=ffmid;
    }

}
