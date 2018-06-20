package com.nsl.app;

public class Companies {

	//private variables
	int    _company_id;
	String _company_master_id;
	String _company_name;
	String _company_sap_id;
	String _company_code;
	String _company_status;
	String _company_cdatetime;
	String _company_udatetime;

	// Empty constructor
	public Companies(){

	}
	// constructor
	public Companies(int id, String company_master_id,String name, String company_sap_id , String company_code , String company_status, String company_cdatetime, String company_udatetime){
		this._company_id          = id;
		this._company_master_id   = company_master_id;
		this._company_name        = name;
		this._company_sap_id      = company_sap_id;
		this._company_code        = company_code;
		this._company_status      = company_status;
		this._company_cdatetime   = company_cdatetime;
		this._company_udatetime   = company_udatetime;
	}

	// constructor
	public Companies(String company_master_id,String name, String company_sap_id, String company_code, String company_status, String company_cdatetime, String company_udatetime){
		this._company_master_id   = company_master_id;
		this._company_name        = name;
		this._company_sap_id      = company_sap_id;
		this._company_code        = company_code;
		this._company_status      = company_status;
		this._company_cdatetime   = company_cdatetime;
		this._company_udatetime   = company_udatetime;
	}
	// getting ID
	public int getID(){
		return this._company_id;
	}
	
	// setting id
	public void setID(int id){
		this._company_id = id;
	}


	public String getCompanyMasterId(){
		return this._company_master_id;
	}

	// setting name
	public void setCompanyMasterId(String company_master_id){
		this._company_master_id = company_master_id;
	}
	// getting name
	public String getCompanyName(){
		return this._company_name;
	}
	
	// setting name
	public void setCompanyName(String company_name){
		this._company_name = company_name;
	}

	public String getCompanycode(){
		return this._company_code;
	}

	// setting name
	public void setCompanycode(String company_code){
		this._company_code = company_code;
	}
	
	// getting phone number
	public String getCompanysapid(){

		return this._company_sap_id;
	}
	
	// setting phone number
	public void setCompanysapid(String company_sap_id){

		this._company_sap_id = company_sap_id;
	}
	// getting phone number
	public String getCompanystatus(){

		return this._company_status;
	}

	// setting phone number
	public void setCompanystatus(String company_status){

		this._company_status = company_status;
	}
	public String getCompanycdatetime(){

		return this._company_cdatetime;
	}

	// setting phone number
	public void setCompanycdatetime(String company_cdatetime){

		this._company_cdatetime = company_cdatetime;
	}
	public String getCompanyudatetime(){

		return this._company_udatetime;
	}

	// setting phone number
	public void setCompanyudatetime(String company_udatetime){

		this._company_udatetime = company_udatetime;
	}
}
