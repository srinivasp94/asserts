package com.nsl.app;

import java.util.List;

public class ServiceOrderMaster {

	//private variables
	int    _serviceorder_id;
	String _serviceorder_masterid;
	String _serviceorder_date;
	String _serviceorder_type;
	String _serviceorder_user_id;
	String _serviceorder_customer_id;
	String _serviceorder_division_id;
	String _serviceorder_company_id;
	String _serviceorder_status;
	String _serviceorder_cdatetime;
	String _serviceorder_udatetime;
	String _serviceorder_ffm_id;
	String _token_amount;
	String _created_by;
	String _approval_status;
	String _approval_comments;

	String _updated_by;

	List<ServiceOrderDetailMaster> serviceOrderDetailMasterList;
	// Empty constructor
	public ServiceOrderMaster() {

	}

	// constructor
	public ServiceOrderMaster(int id, String serviceorder_masterid, String serviceorder_type, String serviceorder_date, String serviceorder_user_id, String serviceorder_customer_id, String serviceorder_division_id, String serviceorder_company_id, String serviceorder_status, String serviceorder_cdatetime, String serviceorder_udatetime, String serviceorder_ffm_id,String created_by,String approval_status,String approval_comments) {
		this._serviceorder_id          = id;
		this._serviceorder_masterid    = serviceorder_masterid;
		this._serviceorder_type        = serviceorder_type;
		this._serviceorder_date        = serviceorder_date;
		this._serviceorder_user_id     = serviceorder_user_id;
		this._serviceorder_customer_id = serviceorder_customer_id;
		this._serviceorder_division_id = serviceorder_division_id;
		this._serviceorder_company_id  = serviceorder_company_id;
		this._serviceorder_status      = serviceorder_status;
		this._serviceorder_cdatetime   = serviceorder_cdatetime;
		this._serviceorder_udatetime   = serviceorder_udatetime;
		this._serviceorder_ffm_id      = serviceorder_ffm_id;
		this._created_by               = created_by;
		this._approval_status          = approval_status;
		this._approval_comments        = approval_comments;
	}

	// constructor
	public ServiceOrderMaster(String serviceorder_masterid, String serviceorder_type, String serviceorder_date, String serviceorder_user_id, String serviceorder_customer_id, String serviceorder_division_id, String serviceorder_company_id,
							  String serviceorder_status, String serviceorder_cdatetime, String serviceorder_udatetime, String serviceorder_ffm_id,String _token_amount,String created_by,String approval_status,String approval_comments) {
		this._serviceorder_masterid    = serviceorder_masterid;
		this._serviceorder_type 	   = serviceorder_type;
		this._serviceorder_date 	   = serviceorder_date;
		this._serviceorder_user_id 	   = serviceorder_user_id;
		this._serviceorder_customer_id = serviceorder_customer_id;
		this._serviceorder_division_id = serviceorder_division_id;
		this._serviceorder_company_id  = serviceorder_company_id;
		this._serviceorder_status      = serviceorder_status;
		this._serviceorder_cdatetime   = serviceorder_cdatetime;
		this._serviceorder_udatetime   = serviceorder_udatetime;
		this._serviceorder_ffm_id      = serviceorder_ffm_id;
		this._token_amount             = _token_amount;
		this._created_by               = created_by;
		this._approval_status          = approval_status;
		this._approval_comments        = approval_comments;
	}
	// getting ID
	public int getID(){
		return this._serviceorder_id;
	}
	
	// setting id
	public void setID(int id){
		this._serviceorder_id = id;
	}

	public String getserviceorder_masterid(){
		return this._serviceorder_masterid;
	}

	// setting name
	public void setserviceorder_masterid(String serviceorder_masterid){
		this._serviceorder_masterid = serviceorder_masterid;
	}
	// getting name
	public String getserviceorder_type(){
		return this._serviceorder_type;
	}
	
	// setting name
	public void setserviceorder_type(String serviceorder_type){
		this._serviceorder_type = serviceorder_type;
	}
	public String get_serviceorder_date(){
		return this._serviceorder_date;
	}

	// setting name
	public void set_serviceorder_date(String serviceorder_date){
		this._serviceorder_date = serviceorder_date;
	}

	public String getserviceorder_customer_id(){
		return this._serviceorder_customer_id;
	}

	// setting name
	public void setserviceorder_customer_id(String serviceorder_customer_id){
		this._serviceorder_customer_id = serviceorder_customer_id;
	}

	public String getserviceorder_company_id(){
		return this._serviceorder_company_id;
	}

	// setting name
	public void setserviceorder_company_id(String serviceorder_company_id){
		this._serviceorder_company_id = serviceorder_company_id;
	}
	
	// getting phone number
	public String get_serviceorder_user_id(){

		return this._serviceorder_user_id;
	}
	public void set_serviceorder_user_id(String serviceorder_user_id){

		this._serviceorder_user_id = serviceorder_user_id;
	}
	public String getserviceorder_division_id(){
		return this._serviceorder_division_id;
	}

	// setting phone number
	public void setserviceorder_division_id(String serviceorder_division_id){
		this._serviceorder_division_id = serviceorder_division_id;
	}

	// getting phone number
	public String getserviceorderstatus(){

		return this._serviceorder_status;
	}

	public void setserviceorderstatus(String serviceorder_status){

		this._serviceorder_status = serviceorder_status;
	}
	public String getserviceordercdatetime(){

		return this._serviceorder_cdatetime;
	}

	// setting phone number
	public void setserviceordercdatetime(String serviceorder_cdatetime){

		this._serviceorder_cdatetime = serviceorder_cdatetime;
	}
	public String getserviceorderudatetime(){

		return this._serviceorder_udatetime;
	}

	// setting phone number
	public void setserviceorderudatetime(String serviceorder_udatetime){

		this._serviceorder_udatetime = serviceorder_udatetime;
	}

	public String getserviceorderffm_id(){

		return this._serviceorder_ffm_id;
	}

	// setting phone number
	public void setserviceorderffm_id(String serviceorder_ffm_id){

		this._serviceorder_ffm_id = serviceorder_ffm_id;
	}

	public List<ServiceOrderDetailMaster> getServiceOrderDetailMasterList() {
		return serviceOrderDetailMasterList;
	}

	public void setServiceOrderDetailMasterList(List<ServiceOrderDetailMaster> serviceOrderDetailMasterList) {
		this.serviceOrderDetailMasterList = serviceOrderDetailMasterList;
	}

	public String get_token_amount() {
		return _token_amount;
	}

	public void set_token_amount(String _token_amount) {
		this._token_amount = _token_amount;
	}
	public String get_created_by() {
		return _created_by;
	}

	public void set_created_by(String _created_by) {
		this._created_by = _created_by;
	}
	public String get_approval_status() {
		return _approval_status;
	}

	public void set_approval_status(String _approval_status) {
		this._approval_status = _approval_status;
	}
	public String get_approval_comments() {
		return _approval_comments;
	}

	public void set_approval_comments(String _approval_comments) {
		this._approval_comments = _approval_comments;
	}

	public String get_updated_by() {
		return _updated_by;
	}

	public void set_updated_by(String _updated_by) {
		this._updated_by = _updated_by;
	}
}
