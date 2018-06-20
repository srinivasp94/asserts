package com.nsl.app;

public class Users {


    //private variables
    int _user_id;
    String _user_masterid;
    String _user_first_name;
    String _user_last_name;
    String _user_mobile_no;
    String _user_email;
    String _user_sap_id;
    String _user_password;
    String _user_role_id;
    String _user_reporting_manager_id;
    String _user_status;
    String _user_cdatetime;
    String _user_udatetime;
    String _user_designation;
    String _user_headquarter;
    String _user_location;
    String _user_region_id;
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCost_center() {
        return cost_center;
    }

    public void setCost_center(String cost_center) {
        this.cost_center = cost_center;
    }

    String grade;
    String cost_center;
    // Empty constructor
    public Users() {

    }




    // constructor
    public Users(String user_master_id, String user_first_name, String user_last_name, String user_mobile_no, String user_email, String user_sap_id, String user_password, String user_role_id,
                 String user_reporting_manager_id, String user_status, String user_cdatetime, String user_udatetime, String user_designation,
                 String user_headquarter, String user_location, String user_region_id,String grade,String cost_center,String image) {
        this._user_masterid = user_master_id;
        this._user_first_name = user_first_name;
        this._user_last_name = user_last_name;
        this._user_mobile_no = user_mobile_no;
        this._user_email = user_email;
        this._user_sap_id = user_sap_id;
        this._user_password = user_password;
        this._user_role_id = user_role_id;
        this._user_reporting_manager_id = user_reporting_manager_id;
        this._user_status = user_status;
        this._user_cdatetime = user_cdatetime;
        this._user_udatetime = user_udatetime;
        this._user_designation = user_designation;
        this._user_headquarter = user_headquarter;
        this._user_location = user_location;
        this._user_region_id = user_region_id;
        this.grade = grade;
        this.cost_center = cost_center;
        this.image = image;
    }

    // getting ID
    public int getID() {
        return this._user_id;
    }

    // setting id
    public void setID(int id) {
        this._user_id = id;
    }

    public String getUserMasterId() {
        return this._user_masterid;
    }

    // setting name
    public void setUserMasterID(String user_masterid) {
        this._user_masterid = user_masterid;
    }

    // getting name
    public String getUser_first_name() {
        return this._user_first_name;
    }

    // setting name
    public void setUser_first_name(String user_first_name) {
        this._user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return this._user_last_name;
    }

    // setting name
    public void setUser_last_name(String user_last_name) {
        this._user_last_name = user_last_name;
    }

    public String getUser_email() {
        return this._user_email;
    }

    // setting name
    public void setUser_email(String user_email) {
        this._user_email = user_email;
    }

    public String getUser_password() {
        return this._user_password;
    }

    // setting name
    public void setUser_password(String user_password) {
        this._user_password = user_password;
    }

    // getting phone number
    public String getUser_mobile_no() {

        return this._user_mobile_no;
    }

    // setting phone number
    public void setUser_mobile_no(String user_mobile_no) {

        this._user_mobile_no = user_mobile_no;
    }

    public String getUser_sap_id() {
        return this._user_sap_id;
    }

    // setting phone number
    public void setUser_sap_id(String user_sap_id) {
        this._user_sap_id = user_sap_id;
    }

    public String getUser_role_id() {
        return this._user_role_id;
    }

    // setting phone number
    public void setUser_role_id(String user_role_id) {
        this._user_role_id = user_role_id;
    }

    public String getUser_reporting_manager_id() {
        return this._user_reporting_manager_id;
    }

    // setting phone number
    public void setUser_reporting_manager_id(String user_reporting_manager_id) {
        this._user_reporting_manager_id = user_reporting_manager_id;
    }

    // getting phone number
    public String getUserstatus() {

        return this._user_status;
    }

    // setting phone number
    public void setUserstatus(String user_status) {

        this._user_status = user_status;
    }

    public String getUsercdatetime() {

        return this._user_cdatetime;
    }

    // setting phone number
    public void setUsercdatetime(String user_cdatetime) {

        this._user_cdatetime = user_cdatetime;
    }

    public String getUserudatetime() {

        return this._user_udatetime;
    }

    // setting phone number
    public void setUserudatetime(String user_udatetime) {

        this._user_udatetime = user_udatetime;
    }

    public String getUserdesignation() {

        return this._user_designation;
    }

    // setting phone number
    public void setUserdesignation(String user_designation) {

        this._user_designation = user_designation;
    }

    public String getUserheadquarter() {

        return this._user_headquarter;
    }

    // setting phone number
    public void setUserheadquarter(String user_headquarter) {

        this._user_headquarter = user_headquarter;
    }

    public String getUserlocation() {

        return this._user_headquarter;
    }

    // setting phone number
    public void setUserlocation(String user_headquarter) {

        this._user_headquarter = user_headquarter;
    }
    public String getUserregionId() {

        return this._user_region_id;
    }

    // setting phone number
    public void setUserregionId(String user_region_id) {

        this._user_region_id = user_region_id;
    }


}
