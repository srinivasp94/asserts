package com.nsl.app.marketintelligence;


public class Crop_Shifts {

    //private variables
    String    _crop_shifts_id;
    String _crop_shifts_master_id;
    String _crop_shifts_crop_name;
    String _crop_shifts_variety_type;
    String _crop_shifts_previous_year_area;
    String _crop_shifts_current_year_expected_area;
    String _crop_shifts_percentage_increase_decrease;
    String _crop_shifts_reason_crop_shift;
    String _crop_shifts_created_by;
    String _crop_shifts_created_on;
    String _crop_shifs_crop_in_saved_seed;
    String _crop_shifs_crop_in_previous_year;
    String _crop_shifs_crop_in_current_year;
    String _crop_shifs_crop_in_next_year;
    String _crop_shifts_ffmid;

    // Empty constructor
    public Crop_Shifts(String id, String master_id, String crop_name, String variety_type, String previous_year_area, String current_year_expected_area, String percentage_increase_decrease, String reason_crop_shift, String created_by, String created_on,String saved, String previous,String current, String next, String ffmid) {
        this._crop_shifts_id = id;
        this._crop_shifts_master_id = master_id;
        this._crop_shifts_crop_name = crop_name;
        this._crop_shifts_variety_type = variety_type;
        this._crop_shifts_previous_year_area = previous_year_area;
        this._crop_shifts_current_year_expected_area = current_year_expected_area;
        this._crop_shifts_percentage_increase_decrease= percentage_increase_decrease;
        this._crop_shifts_reason_crop_shift = reason_crop_shift;
        this._crop_shifts_created_by = created_by;
        this._crop_shifts_created_on = created_on;
        this._crop_shifs_crop_in_saved_seed=saved;
        this._crop_shifs_crop_in_previous_year=previous;
        this._crop_shifs_crop_in_current_year=current;
        this._crop_shifs_crop_in_next_year=next;
        this._crop_shifts_ffmid = ffmid;
    }


    // constructor
    public Crop_Shifts( String master_id, String crop_name, String variety_type, String previous_year_area, String current_year_expected_area, String percentage_increase_decrease, String reason_crop_shift, String created_by, String created_on,String saved, String previous,String current, String next, String ffmid) {
        this._crop_shifts_master_id = master_id;
        this._crop_shifts_crop_name = crop_name;
        this._crop_shifts_variety_type = variety_type;
        this._crop_shifts_previous_year_area = previous_year_area;
        this._crop_shifts_current_year_expected_area = current_year_expected_area;
        this._crop_shifts_percentage_increase_decrease= percentage_increase_decrease;
        this._crop_shifts_reason_crop_shift = reason_crop_shift;
        this._crop_shifts_created_by = created_by;
        this._crop_shifts_created_on = created_on;
        this._crop_shifs_crop_in_saved_seed=saved;
        this._crop_shifs_crop_in_previous_year=previous;
        this._crop_shifs_crop_in_current_year=current;
        this._crop_shifs_crop_in_next_year=next;
        this._crop_shifts_ffmid = ffmid;
    }

    public Crop_Shifts() {

    }


    public String getID() {
        return this._crop_shifts_id;
    }
    public void setID(String id) {
        this._crop_shifts_id = id;
    }


    public String get_crop_shifts_master_id() {
        return this._crop_shifts_master_id;
    }
    public void set_crop_shifts_master_id(String master_id) {this._crop_shifts_master_id = master_id; }


    public String get_crop_shifts_crop_name() {
        return this._crop_shifts_crop_name;
    }
    public void set_crop_shifts_crop_name(String crop_name) {this._crop_shifts_crop_name = crop_name; }

    public String get_crop_shifts_variety_type() {return this._crop_shifts_variety_type;}
    public void set_crop_shifts_variety_type(String variety_type) {this._crop_shifts_variety_type = variety_type; }


    public String get_crop_shifts_previous_year_area() { return this._crop_shifts_previous_year_area;    }
    public void set_crop_shifts_previous_year_area(String previous_year_area) { this._crop_shifts_previous_year_area = previous_year_area; }


    public String get_crop_shifts_current_year_expected_area() { return this._crop_shifts_current_year_expected_area; }
    public void set_crop_shifts_current_year_expected_area(String current_year_expected_area) { this._crop_shifts_current_year_expected_area = current_year_expected_area;  }


    public String get_crop_shifts_percentage_increase_decrease(){ return this._crop_shifts_percentage_increase_decrease;  }
    public void set_crop_shifts_percentage_increase_decrease(String percentage_increase_decrease) {this._crop_shifts_percentage_increase_decrease = percentage_increase_decrease; }

    public String get_crop_shifts_reason_crop_shift() {return this._crop_shifts_reason_crop_shift;}
    public void set_crop_shifts_reason_crop_shift(String reason_crop_shift ) {this._crop_shifts_reason_crop_shift = reason_crop_shift ;}

    public String get_crop_shifts_created_by() {return this._crop_shifts_created_by;}
    public void set_crop_shifts_created_by(String created_by) {this._crop_shifts_created_by = created_by;}


    public String get_crop_shifts_created_on() {return this._crop_shifts_created_on;}
    public void set_crop_shifts_created_on(String created_on) {this._crop_shifts_created_on = created_on;}

    public String get_crop_shifs_crop_in_saved_seed() {return _crop_shifs_crop_in_saved_seed;}
    public void set_crop_shifs_crop_in_saved_seed(String crop_shifs_crop_in_saved_seed) {this._crop_shifs_crop_in_saved_seed = crop_shifs_crop_in_saved_seed;}

    public String get_crop_shifs_crop_in_previous_year() {return _crop_shifs_crop_in_previous_year;}
    public void set_crop_shifs_crop_in_previous_year(String _crop_shifs_crop_in_previous_year) {this._crop_shifs_crop_in_previous_year = _crop_shifs_crop_in_previous_year;}

    public String get_crop_shifs_crop_in_current_year() {return _crop_shifs_crop_in_current_year;}
    public void set_crop_shifs_crop_in_current_year(String _crop_shifs_crop_in_current_year) {this._crop_shifs_crop_in_current_year = _crop_shifs_crop_in_current_year;}

    public String get_crop_shifs_crop_in_next_year() {return _crop_shifs_crop_in_next_year;}
    public void set_crop_shifs_crop_in_next_year(String _crop_shifs_crop_in_next_year) {this._crop_shifs_crop_in_next_year = _crop_shifs_crop_in_next_year;}

    public String get_crop_shifts_ffmid() {return this._crop_shifts_ffmid;}
    public void set_crop_shifts_ffmid(String ffmid) {this._crop_shifts_ffmid = ffmid;}


}
