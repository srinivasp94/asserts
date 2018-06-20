package com.nsl.app;

public class Employe_visit_management_pojo {

    //private variables
    int    _emp_v_id;
    String _emp_visit_master_id;
    int    _emp_visit_type;
    int    _emp_visit_geo_tracking_id;
    int    _emp_visit_user_id;
    int    _emp_visit_customer_id;
    String    _emp_visit_plantype;
    String    _emp_visit_purposevisit;

    public String get_emp_visit_purposevisit_ids() {
        return _emp_visit_purposevisit_ids;
    }

    public void set_emp_visit_purposevisit_ids(String _emp_visit_purposevisit_ids) {
        this._emp_visit_purposevisit_ids = _emp_visit_purposevisit_ids;
    }

    String    _emp_visit_purposevisit_ids;
    String _emp_visit_plandatetime;
    String _emp_visit_concernpersonname;
    int    _emp_visit_mobile;
    String _emp_visit_village;
    String _emp_visit_locationaddress;
    String _emp_visit_fieldarea;
    String _emp_visit_checkintime;
    String _emp_visit_comments;
    int    _emp_visit_status;
    int    _emp_visit_approval_status;
    String _emp_visit_event_name;
    String _emp_visit_event_end_date;
    String _emp_visit_event_purpose;
    String _emp_visit_event_venue;
    String _emp_visit_event_participants;
    String    _emp_visit_createdby;
    String    _emp_visit_updatedby;
    String _emp_visit_cdatetime;
    String _emp_visit_udatetime;
    int    _emp_visit_ffm_id;


    // constructor
    public Employe_visit_management_pojo(int id, String emp_visit_master_id,
                                         int emp_visit_type,
                                         int emp_visit_geo_tracking_id,
                                         int emp_visit_user_id,
                                         int emp_visit_customer_id,
                                         String emp_visit_plantype,
                                         String emp_visit_purposevisit,
                                         String emp_visit_plandatetime,
                                         String emp_visit_concernpersonname,
                                         int emp_visit_mobile,
                                         String emp_visit_village,
                                         String emp_visit_locationaddress,
                                         String emp_visit_fieldarea,
                                         String emp_visit_checkintime,
                                         String emp_visit_comments,
                                         int emp_visit_status,
                                         int emp_visit_approval_status,
                                         String emp_visit_event_name,
                                         String emp_visit_event_end_date,
                                         String emp_visit_event_purpose,
                                         String emp_visit_event_venue,
                                         String emp_visit_event_participants,
                                         String emp_visit_createdby,
                                         String emp_visit_updatedby,
                                         String emp_visit_cdatetime,
                                         String emp_visit_udatetime,
                                         int emp_visit_ffm_id) {

        this._emp_v_id = id;
        this._emp_visit_master_id = emp_visit_master_id;
        this._emp_visit_type = emp_visit_type;
        this._emp_visit_geo_tracking_id = emp_visit_geo_tracking_id;
        this._emp_visit_user_id = emp_visit_user_id;
        this._emp_visit_customer_id = emp_visit_customer_id;
        this._emp_visit_plantype = emp_visit_plantype;
        this._emp_visit_purposevisit = emp_visit_purposevisit;
        this._emp_visit_plandatetime = emp_visit_plandatetime;
        this._emp_visit_concernpersonname = emp_visit_concernpersonname;
        this._emp_visit_mobile = emp_visit_mobile;
        this._emp_visit_village = emp_visit_village;
        this._emp_visit_locationaddress = emp_visit_locationaddress;
        this._emp_visit_fieldarea = emp_visit_fieldarea;
        this._emp_visit_checkintime = emp_visit_checkintime;
        this._emp_visit_comments = emp_visit_comments;
        this._emp_visit_status = emp_visit_status;
        this._emp_visit_approval_status = emp_visit_approval_status;
        this._emp_visit_event_name = emp_visit_event_name;
        this._emp_visit_event_end_date = emp_visit_event_end_date;
        this._emp_visit_event_purpose = emp_visit_event_purpose;
        this._emp_visit_event_venue = emp_visit_event_venue;
        this._emp_visit_event_participants = emp_visit_event_participants;
        this._emp_visit_createdby = emp_visit_createdby;
        this._emp_visit_updatedby = emp_visit_updatedby;
        this._emp_visit_cdatetime = emp_visit_cdatetime;
        this._emp_visit_udatetime = emp_visit_udatetime;
        this._emp_visit_ffm_id = emp_visit_ffm_id;

    }

    ///* constructor
    public Employe_visit_management_pojo(
            String emp_visit_master_id,
            int emp_visit_type,
            int emp_visit_geo_tracking_id,
            int emp_visit_user_id,
            int emp_visit_customer_id,
            String emp_visit_plantype,
            String emp_visit_purposevisit,
            String _emp_visit_purposevisit_ids,
            String emp_visit_plandatetime,
            String emp_visit_concernpersonname,
            int    emp_visit_mobile,
            String emp_visit_village,
            String emp_visit_locationaddress,
            String emp_visit_fieldarea,
            String emp_visit_checkintime,
            String emp_visit_comments,
            int emp_visit_status,
            int emp_visit_approval_status,
            String emp_visit_event_name,
            String emp_visit_event_end_date,
            String emp_visit_event_purpose,
            String emp_visit_event_venue,
            String emp_visit_event_participants,
            String emp_visit_createdby,
            String emp_visit_updatedby,
            String emp_visit_cdatetime,
            String emp_visit_udatetime,
            int emp_visit_ffm_id) {

        this._emp_visit_master_id = emp_visit_master_id;
        this._emp_visit_type = emp_visit_type;
        this._emp_visit_geo_tracking_id = emp_visit_geo_tracking_id;
        this._emp_visit_user_id = emp_visit_user_id;
        this._emp_visit_customer_id = emp_visit_customer_id;
        this._emp_visit_plantype = emp_visit_plantype;
        this._emp_visit_purposevisit = emp_visit_purposevisit;
        this._emp_visit_purposevisit_ids = _emp_visit_purposevisit_ids;
        this._emp_visit_plandatetime = emp_visit_plandatetime;
        this._emp_visit_concernpersonname = emp_visit_concernpersonname;
        this._emp_visit_mobile = emp_visit_mobile;
        this._emp_visit_village = emp_visit_village;
        this._emp_visit_locationaddress = emp_visit_locationaddress;
        this._emp_visit_fieldarea          = emp_visit_fieldarea;
        this._emp_visit_checkintime        = emp_visit_checkintime;
        this._emp_visit_comments           = emp_visit_comments;
        this._emp_visit_status             = emp_visit_status;
        this._emp_visit_approval_status    = emp_visit_approval_status;
        this._emp_visit_event_name         = emp_visit_event_name;
        this._emp_visit_event_end_date     = emp_visit_event_end_date;
        this._emp_visit_event_purpose      = emp_visit_event_purpose;
        this._emp_visit_event_venue        = emp_visit_event_venue;
        this._emp_visit_event_participants = emp_visit_event_participants;
        this._emp_visit_createdby          = emp_visit_createdby;
        this._emp_visit_updatedby          = emp_visit_updatedby;
        this._emp_visit_cdatetime          = emp_visit_cdatetime;
        this._emp_visit_udatetime          = emp_visit_udatetime;
        this._emp_visit_ffm_id             = emp_visit_ffm_id;
    }

   /* public Employe_visit_management_pojo(
            String emp_visit_master_id,
            int emp_visit_type,
            int emp_visit_geo_tracking_id,
            int emp_visit_user_id,
            int emp_visit_customer_id,
            int emp_visit_plantype,
            int emp_visit_purposevisit,
            String emp_visit_plandatetime,
            String emp_visit_concernpersonname,
            int    emp_visit_mobile,
            String emp_visit_village,
            String emp_visit_locationaddress,
            String emp_visit_fieldarea,
            String emp_visit_checkintime,
            String emp_visit_comments,
            int emp_visit_status,
            int emp_visit_approval_status,
            String emp_visit_event_name,
            String emp_visit_event_end_date,
            String emp_visit_event_purpose,
            String emp_visit_event_venue,
            String emp_visit_event_participants,
            int emp_visit_createdby,
            int emp_visit_updatedby,
            String emp_visit_cdatetime,
            String emp_visit_udatetime,
            int emp_visit_ffm_id) {
        this._emp_visit_type = emp_visit_type;
        this._emp_visit_geo_tracking_id = emp_visit_geo_tracking_id;
        this._emp_visit_user_id = emp_visit_user_id;
        this._emp_visit_customer_id = emp_visit_customer_id;
        this._emp_visit_plantype = emp_visit_plantype;
        this._emp_visit_purposevisit = emp_visit_purposevisit;
        this._emp_visit_plandatetime = emp_visit_plandatetime;
        this._emp_visit_concernpersonname = emp_visit_concernpersonname;
        this._emp_visit_mobile = emp_visit_mobile;
        this._emp_visit_village = emp_visit_village;
        this._emp_visit_locationaddress = emp_visit_locationaddress;
        this._emp_visit_fieldarea          = emp_visit_fieldarea;
        this._emp_visit_checkintime        = emp_visit_checkintime;
        this._emp_visit_comments           = emp_visit_comments;
        this._emp_visit_status             = emp_visit_status;
        this._emp_visit_approval_status    = emp_visit_approval_status;
        this._emp_visit_event_name         = emp_visit_event_name;
        this._emp_visit_event_end_date     = emp_visit_event_end_date;
        this._emp_visit_event_purpose      = emp_visit_event_purpose;
        this._emp_visit_event_venue        = emp_visit_event_venue;
        this._emp_visit_event_participants = emp_visit_event_participants;
        this._emp_visit_createdby          = emp_visit_createdby;
        this._emp_visit_updatedby          = emp_visit_updatedby;
        this._emp_visit_cdatetime          = emp_visit_cdatetime;
        this._emp_visit_udatetime          = emp_visit_udatetime;
        this._emp_visit_ffm_id             = emp_visit_ffm_id;
    }*/


    // getting ID
    public int getID() {
        return this._emp_v_id;
    }

    // setting id
    public void setID(int id) {
        this._emp_v_id = id;
    }


    public String getEmp_visitMasterId() {
        return this._emp_visit_master_id;
    }

    // setting name
    public void setEmp_visitMasterId(String emp_visit_master_id) {
        this._emp_visit_master_id = emp_visit_master_id;
    }

    // getting name
    public int getEmp_visit_userid() {
        return this._emp_visit_user_id;
    }

    // setting name
    public void setEmp_visit_userid(int emp_visit_user_id) {
        this._emp_visit_user_id = emp_visit_user_id;
    }

    // getting phone number
    public int getEmp_visit_customerid() {

        return this._emp_visit_customer_id;
    }

    // setting phone number
    public void setEmp_visit_customerid(int emp_visit_customer_id) {

        this._emp_visit_customer_id = emp_visit_customer_id;
    }

    public String getEmp_visit_plantype() {
        return this._emp_visit_plantype;
    }

    // setting name
    public void setEmp_visit_plantype(String emp_visit_plantype) {
        this._emp_visit_plantype = emp_visit_plantype;
    }

    public String get_emp_visit_purposevisit() {
        return this._emp_visit_purposevisit;
    }

    // setting name
    public void setEmp_visit_purposevisit(String emp_visit_purposevisit) {
        this._emp_visit_purposevisit = emp_visit_purposevisit;
    }


    public String getEmp_visit_plandatetime() {
        return this._emp_visit_plandatetime;
    }

    // setting name
    public void setEmp_visit_plandatetime(String emp_visit_plandatetime) {
        this._emp_visit_plandatetime = emp_visit_plandatetime;
    }

    public String getEmp_visit_concernpersonname() {
        return this._emp_visit_concernpersonname;
    }

    // setting name
    public void setEmp_visit_concernpersonname(String emp_visit_concernpersonname) {
        this._emp_visit_concernpersonname = emp_visit_concernpersonname;
    }

    public int getEmp_visit_mobile() {
        return this._emp_visit_mobile;
    }

    // setting name
    public void setEmp_visit_mobile(int emp_visit_mobile) {
        this._emp_visit_mobile = emp_visit_mobile;
    }

    public String getEmp_visit_village() {
        return this._emp_visit_village;
    }

    // setting name
    public void setEmp_visit_village(String emp_visit_village) {
        this._emp_visit_village = emp_visit_village;
    }

    public String getEmp_visit_locationaddress() {
        return this._emp_visit_locationaddress;
    }

    // setting name
    public void setEmp_visit_locationaddress(String emp_visit_locationaddress) {
        this._emp_visit_locationaddress = emp_visit_locationaddress;
    }

    public String getEmp_visit_fieldarea() {
        return this._emp_visit_fieldarea;
    }

    // setting name
    public void setEmp_visit_fieldarea(String emp_visit_fieldarea) {
        this._emp_visit_fieldarea = emp_visit_fieldarea;
    }

    public String getEmp_visit_checkintime() {
        return this._emp_visit_checkintime;
    }

    // setting name
    public void setEmp_visit_checkintime(String emp_visit_checkintime) {
        this._emp_visit_checkintime = emp_visit_checkintime;
    }

    public String getEmp_visit_comments() {
        return this._emp_visit_comments;
    }

    // setting name
    public void setEmp_visit_comments(String emp_visit_comments) {
        this._emp_visit_comments = emp_visit_comments;
    }

    public String getEmp_visit_createdby() {
        return this._emp_visit_createdby;
    }

    // setting name
    public void setEmp_visit_createdby(String emp_visit_createdby) {
        this._emp_visit_createdby = emp_visit_createdby;
    }

    public String getEmp_visit_updatedby() {
        return this._emp_visit_updatedby;
    }

    // setting name
    public void setEmp_visit_updatedby(String emp_visit_updatedby) {
        this._emp_visit_updatedby = emp_visit_updatedby;
    }

    public int getEmpvisitstatus() {

        return this._emp_visit_status;
    }


    public void setEmpvisitstatus(int emp_visit_status) {

        this._emp_visit_status = emp_visit_status;
    }

    public int getEmpvisitapproval_status() {

        return this._emp_visit_approval_status;
    }

    public void setEmpvisitapproval_status(int emp_visit_approval_status) {

        this._emp_visit_approval_status = emp_visit_approval_status;
    }

    public void setEmpvisitevent_name(String emp_visit_event_name) {

        this._emp_visit_event_name = emp_visit_event_name;
    }

    public String getEmpvisitevent_name() {

        return this._emp_visit_event_name;
    }

    public void setEmpvisitevent_purpose(String emp_visit_event_purpose) {

        this._emp_visit_event_purpose = emp_visit_event_purpose;
    }

    public String getEmpvisitevent_purpose() {

        return this._emp_visit_event_purpose;
    }

    public void setEmpvisitevent_venue(String emp_visit_event_venue) {

        this._emp_visit_event_venue = emp_visit_event_venue;
    }

    public String getEmpvisitevent_venue() {

        return this._emp_visit_event_venue;
    }

    public void setEmpvisitevent_participants(String emp_visit_event_participants) {

        this._emp_visit_event_participants = emp_visit_event_participants;
    }

    public String getEmpvisitevent_participants() {

        return this._emp_visit_event_participants;
    }

    public void setEmpvisitend_date(String emp_visit_event_end_date) {

        this._emp_visit_event_end_date = emp_visit_event_end_date;
    }

    public String getEmpvisitend_date() {

        return this._emp_visit_event_end_date;
    }

    // setting phone number
    public void setEmpvisitsemp_visit_approval_statuss(int emp_visit_approval_status) {

        this._emp_visit_approval_status = emp_visit_approval_status;
    }

    public int getEmpvisitsffmid() {

        return this._emp_visit_ffm_id;
    }

    // setting phone number
    public void setEmpvisitffmid(int emp_visit_ffm_id) {

        this._emp_visit_ffm_id = emp_visit_ffm_id;
    }

    public int getEmpvisitstype() {

        return this._emp_visit_type;
    }

    // setting phone number
    public void setEmpvisittype(int emp_visit_type) {

        this._emp_visit_type = emp_visit_type;
    }

    public String getEmpvisitcdatetime() {

        return this._emp_visit_cdatetime;
    }

    // setting phone number
    public void setEmpvisitcdatetime(String emp_visit_cdatetime) {

        this._emp_visit_cdatetime = emp_visit_cdatetime;
    }

    public String getEmpvisitudatetime() {

        return this._emp_visit_udatetime;
    }

    // setting phone number
    public void setEmpvisitudatetime(String emp_visit_udatetime) {

        this._emp_visit_udatetime = emp_visit_udatetime;
    }

    public int getEmpvisitgeotrackingid() {

        return this._emp_visit_geo_tracking_id;
    }

    // setting phone number
    public void setEmpvisitgeotrackingid(int emp_visit_geo_tracking_id) {

        this._emp_visit_geo_tracking_id = emp_visit_geo_tracking_id;
    }
}
