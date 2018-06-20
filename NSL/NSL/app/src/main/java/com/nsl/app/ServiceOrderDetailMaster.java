package com.nsl.app;

public class ServiceOrderDetailMaster {

	//private variables
	int    _serviceorderdetail_id;
	String _serviceorderdetail_masterid;
	String _serviceorderdetailorderid_id;
	String _serviceorderdetail_crop_id;
	String _serviceorderdetail_scheme_id;
	String _serviceorderdetail_product_id;
	String _serviceorderdetail_quantity;
	String _serviceorderdetail_order_price;
	String _serviceorderdetail_advance_amount;
	String _serviceorderdetail_status;
	String _serviceorderdetail_cdatetime;
	String _serviceorderdetail_udatetime;
	String  ffmID;
	String  slabId;


	public String getSlabId() {
		return slabId;
	}

	public void setSlabId(String slabId) {
		this.slabId = slabId;
	}




	public String getFfmID() {
		return ffmID;
	}

	public void setFfmID(String ffmID) {
		this.ffmID = ffmID;
	}


	public ServiceOrderDetailMaster(){

	}

	// constructor
	public ServiceOrderDetailMaster(String serviceorderdetail_masterid,
									String serviceorderdetailorderid_id,
									String serviceorderdetail_crop_id,
									String serviceorderdetail_scheme_id,
									String serviceorderdetail_product_id,
									String serviceorderdetail_quantity,
									String serviceorderdetail_order_price,
									String serviceorderdetail_advance_amount,
									String serviceorderdetail_status,
									String serviceorderdetail_cdatetime,
									String serviceorderdetail_udatetime
			, String ffmID, String slabId) {

		this._serviceorderdetail_masterid = serviceorderdetail_masterid;
		this._serviceorderdetailorderid_id = serviceorderdetailorderid_id;
		this._serviceorderdetail_crop_id = serviceorderdetail_crop_id;
		this._serviceorderdetail_scheme_id = serviceorderdetail_scheme_id;
		this._serviceorderdetail_product_id = serviceorderdetail_product_id;
		this._serviceorderdetail_quantity = serviceorderdetail_quantity;
		this._serviceorderdetail_order_price = serviceorderdetail_order_price;
		this._serviceorderdetail_advance_amount = serviceorderdetail_advance_amount;
		this._serviceorderdetail_status = serviceorderdetail_status;
		this._serviceorderdetail_cdatetime = serviceorderdetail_cdatetime;
		this._serviceorderdetail_udatetime = serviceorderdetail_udatetime;
		this.ffmID = ffmID;
		this.slabId = slabId;
	}
	// getting ID
	public int getID(){
		return this._serviceorderdetail_id;
	}
	
	// setting id
	public void setID(int id){
		this._serviceorderdetail_id = id;
	}

	public String getserviceorderdetail_masterid(){
		return this._serviceorderdetail_masterid;
	}


	public void setserviceorderdetail_masterid(String serviceorderdetail_masterid){
		this._serviceorderdetail_masterid = serviceorderdetail_masterid;
	}

	public String getserviceorderdetailorderid_id(){
		return this._serviceorderdetailorderid_id;
	}
	

	public void setserviceorderdetailorderid_id(String serviceorderdetailorderid_id){
		this._serviceorderdetailorderid_id = serviceorderdetailorderid_id;
	}

	public String get_serviceorderdetail_crop_id(){
		return this._serviceorderdetail_crop_id;
	}

	// setting name
	public void set_serviceorderdetail_crop_id(String serviceorderdetail_crop_id){
		this._serviceorderdetail_crop_id = serviceorderdetail_crop_id;
	}

	public String getserviceorderdetail_scheme_id(){
		return this._serviceorderdetail_scheme_id;
	}

	// setting name
	public void setserviceorderdetail_scheme_id(String serviceorderdetail_scheme_id){
		this._serviceorderdetail_scheme_id = serviceorderdetail_scheme_id;
	}

	public String getserviceorderdetail_product_id(){
		return this._serviceorderdetail_product_id;
	}

	// setting name
	public void setserviceorderdetail_product_id(String serviceorderdetail_product_id){
		this._serviceorderdetail_product_id = serviceorderdetail_product_id;
	}
	
	// getting phone number
	public String get_serviceorderdetail_quantity(){

		return this._serviceorderdetail_quantity;
	}
	public void set_serviceorderdetail_quantity(String serviceorderdetail_quantity){

		this._serviceorderdetail_quantity = serviceorderdetail_quantity;
	}


	public String getserviceorderdetail_order_price(){
		return this._serviceorderdetail_order_price;
	}

	// setting phone number
	public void setserviceorderdetail_order_price(String serviceorderdetail_order_price){
		this._serviceorderdetail_order_price = serviceorderdetail_order_price;
	}

	// getting phone number
	public String getserviceorderdetail_advance_amount(){

		return this._serviceorderdetail_advance_amount;
	}

	public void setserviceorderdetail_advance_amount(String serviceorderdetail_advance_amount){

		this._serviceorderdetail_advance_amount = serviceorderdetail_advance_amount;
	}
	public String getserviceorderdetail_status(){

		return this._serviceorderdetail_status;
	}

	public void setserviceorderdetail_status(String serviceorderdetail_status){

		this._serviceorderdetail_status = serviceorderdetail_status;
	}
	public String getserviceorderdetailcdatetime(){

		return this._serviceorderdetail_cdatetime;
	}

	// setting phone number
	public void setserviceorderdetailcdatetime(String serviceorderdetail_cdatetime){

		this._serviceorderdetail_cdatetime = serviceorderdetail_cdatetime;
	}
	public String getserviceorderdetailudatetime(){

		return this._serviceorderdetail_udatetime;
	}

	// setting phone number
	public void setserviceorderdetailudatetime(String serviceorderdetail_udatetime){

		this._serviceorderdetail_udatetime = serviceorderdetail_udatetime;
	}
}
