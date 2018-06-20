package com.nsl.app;

public class Schemes {

    //private variables
    public int _scheme_id;
    public String _scheme_masterid;
    public String _scheme_title;
    public String _scheme_sap_code;
    public String _scheme_file_location;
    public String _scheme_status;
    public String _slab_id;

    public String scheme_value;


    public String get_slab_id() {
        return _slab_id;
    }

    public void set_slab_id(String _slab_id) {
        this._slab_id = _slab_id;
    }



    // Empty constructor
    public Schemes() {

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Schemes) {
            Schemes sc = (Schemes) o;
            if (this._scheme_title.equals(sc._scheme_title)) {
                return true;
            }
        }
        return false;
    }



    // constructor
    public Schemes(String scheme_masterid, String name, String scheme_sap_code, String scheme_file_location, String scheme_status) {
        this._scheme_masterid = scheme_masterid;
        this._scheme_title = name;
        this._scheme_sap_code = scheme_sap_code;
        this._scheme_file_location = scheme_file_location;
        this._scheme_status = scheme_status;
        this._slab_id = _slab_id;

    }

    // getting ID
    public int getID() {
        return this._scheme_id;
    }

    // setting id
    public void setID(int id) {
        this._scheme_id = id;
    }

    public String getschemeMasterId() {
        return this._scheme_masterid;
    }

    // setting name
    public void setschemeMasterID(String scheme_masterid) {
        this._scheme_masterid = scheme_masterid;
    }

    // getting name
    public String getschemeName() {
        return this._scheme_title;
    }

    // setting name
    public void setschemeName(String scheme_title) {
        this._scheme_title = scheme_title;
    }


    public String getscheme_file_location() {
        return this._scheme_file_location;
    }

    // setting name
    public void setscheme_file_location(String scheme_file_location) {
        this._scheme_file_location = scheme_file_location;
    }


    // getting phone number
    public String getscheme_sap_code() {

        return this._scheme_sap_code;
    }

    // setting phone number
    public void setscheme_sap_code(String scheme_sap_code) {

        this._scheme_sap_code = scheme_sap_code;
    }


    // setting phone number
    public void setscheme_status(String scheme_status) {

        this._scheme_status = scheme_status;
    }

    public String getscheme_status() {

        return this._scheme_status;
    }

    public String getScheme_value() {
        return scheme_value;
    }

    public void setScheme_value(String scheme_value) {
        this.scheme_value = scheme_value;
    }

    @Override
    public String toString() {
        return _scheme_title;
    }
}
