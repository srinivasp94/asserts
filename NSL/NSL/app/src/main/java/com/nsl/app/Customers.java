package com.nsl.app;

public class Customers {

	//private variables
	int    _cus_id;
	String _cus_masterid;
	String _cus_name;
	String _cus_code;
	String _cus_region_id;
	String _cus_company_id;
	String _cus_status;
	String _cus_telephone;
	String _cus_address;
	String _cus_street;
	String _cus_city;
	String _cus_country;

	String _cus_cdatetime;
	String _cus_udatetime;

	String _cus_password;
	String _cus_email;
	String _cus_state;
	String _cus_district;
	String _cus_lat_lng;

	// Empty constructor
	public Customers(){

	}
	// constructor
	public Customers(int id,
					 String cus_master_id,
					 String name,
					 String cus_code,
					 String cus_address,
					 String cus_street,
					 String cus_city,
					 String cus_country,
					 String cus_region_id,
					 String cus_telephone,
					 String cus_company_id ,
					 String cus_status,
					 String cus_cdatetime,
					 String cus_udatetime,
					 String cus_password ,
					 String cus_email,
					 String cus_state,
					 String cus_district,
					 String cus_lat_lng){
		this._cus_id            = id;
		this._cus_masterid      = cus_master_id;
		this._cus_name          = name;
		this._cus_code          = cus_code;
		this._cus_address       = cus_address;
		this._cus_street        = cus_street;
		this._cus_city          = cus_city;
		this._cus_country       = cus_country;
		this._cus_region_id     = cus_region_id;
		this._cus_telephone     = cus_telephone;
		this._cus_company_id    = cus_company_id;
		this._cus_status        = cus_status;
		this._cus_cdatetime     = cus_cdatetime;
		this._cus_udatetime     = cus_udatetime;
		this._cus_password      = cus_password;
		this._cus_email         = cus_email;
		this._cus_state         = cus_state;
		this._cus_district      = cus_district;
		this._cus_lat_lng       = cus_lat_lng;
	}

	// constructor
	public Customers(String cus_master_id,
					 String name,
					 String cus_code,
					 String cus_address,
					 String cus_street,
					 String cus_city,
					 String cus_country,
					 String cus_region_id,
					 String cus_telephone,
					 String cus_company_id,
					 String cus_status,
					 String cus_cdatetime,
					 String cus_udatetime,
					 String cus_password ,
					 String cus_email,
					 String cus_state,
					 String cus_district,
					 String cus_lat_lng){

		this._cus_masterid      = cus_master_id;
		this._cus_name          = name;
		this._cus_code          = cus_code;
		this._cus_address       = cus_address;
		this._cus_street        = cus_street;
		this._cus_city          = cus_city;
		this._cus_country       = cus_country;
		this._cus_region_id     = cus_region_id;
		this._cus_telephone     = cus_telephone;
		this._cus_company_id    = cus_company_id;
		this._cus_status        = cus_status;
		this._cus_cdatetime     = cus_cdatetime;
		this._cus_udatetime     = cus_udatetime;
		this._cus_password      = cus_password;
		this._cus_email         = cus_email;
		this._cus_state         = cus_state;
		this._cus_district      = cus_district;
		this._cus_lat_lng       = cus_lat_lng;
	}

	public int getID(){
		return this._cus_id;
	}
	

	public void setID(int id){
		this._cus_id = id;
	}

	public String getCusMasterId(){
		return this._cus_masterid;
	}


	public void setCusMasterID(String cus_masterid){
		this._cus_masterid = cus_masterid;
	}

	public String getCusName(){
		return this._cus_name;
	}
	

	public void setCusName(String cus_name){
		this._cus_name = cus_name;
	}

	public String getCuscode(){
		return this._cus_code;
	}


	public void setCuscode(String cus_code){
		this._cus_code = cus_code;
	}
	

	public String getCuscompany_Id(){

		return this._cus_company_id;
	}
	

	public void setCuscompany_Id(String cus_company_id){

		this._cus_company_id = cus_company_id;
	}


	public String getCusregion_Id(){

		return this._cus_region_id;
	}


	public void setCusregion_Id(String cus_region){

		this._cus_region_id = cus_region;
	}

	public String getCusstreet(){

		return this._cus_street;
	}

	public void setCusstreet(String cus_street){

		this._cus_street = cus_street;
	}
	public void setCus_city(String cus_city){

		this._cus_city = cus_city;
	}
	public String getCus_city(){

		return this._cus_city;
	}

	public String getCustelephone(){

		return this._cus_telephone;
	}


	public void setCustelephone(String cus_telephone){

		this._cus_telephone = cus_telephone;
	}
	public String getCusaddress(){

		return this._cus_address;
	}


	public void setCusaddress(String cus_address){

		this._cus_address = cus_address;
	}
	public String getCuscountry(){

		return this._cus_country;
	}


	public void setCuscountry(String cus_country){

		this._cus_country = cus_country;
	}


	public String getCusstatus(){

		return this._cus_status;
	}


	public void setCusstatus(String cus_status){

		this._cus_status = cus_status;
	}
	public String getCuscdatetime(){

		return this._cus_cdatetime;
	}


	public void setCuscdatetime(String cus_cdatetime){

		this._cus_cdatetime = cus_cdatetime;
	}
	public String getCusudatetime(){

		return this._cus_udatetime;
	}

	public void setCusudatetime(String cus_udatetime){

		this._cus_udatetime = cus_udatetime;
	}

	public String getCuspassword(){

		return this._cus_password;
	}

	public void setCuspassword(String cus_password){

		this._cus_password = cus_password;
	}

	public String getCusEmail(){

		return this._cus_email;
	}

	public void setCusEmail(String cus_email){

		this._cus_email= cus_email;
	}
	public String getCusState(){

		return this._cus_state;
	}
	public void setCusState(String cus_state){

		this._cus_state= cus_state;
	}
	public String getCusDistrict(){

		return this._cus_district;
	}
	public void setCusDistrict(String cus_district){

		this._cus_district= cus_district;
	}
	public String getCusLatlng(){

		return this._cus_lat_lng;
	}
	public void setCusLatlng(String cus_lat_lng){

		this._cus_lat_lng= cus_lat_lng;
	}
}
