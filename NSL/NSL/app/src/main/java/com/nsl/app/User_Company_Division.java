package com.nsl.app;

public class User_Company_Division {

	//private variables
	int    _ucd_id;
	String _ucd_user_id;
	String _ucd_company_id;
	String _ucd_division_id;

	// Empty constructor
	public User_Company_Division(){

	}
	// constructor
	public User_Company_Division(int id, String ucd_user_id, String ucd_company_id, String ucd_division_id){
		this._ucd_id            = id;
		this._ucd_user_id       = ucd_user_id;
		this._ucd_company_id   = ucd_company_id;
		this._ucd_division_id       = ucd_division_id;


	}

	// constructor
	public User_Company_Division(String ucd_user_id, String ucd_company_id, String ucd_division_id){
		this._ucd_user_id    = ucd_user_id;
		this._ucd_company_id       = ucd_company_id;
		this._ucd_division_id   = ucd_division_id;

	}
	// getting ID
	public int getID(){
		return this._ucd_id;
	}
	
	// setting id
	public void setID(int id){
		this._ucd_id = id;
	}

	public String getucdUserId(){
		return this._ucd_user_id;
	}

	// setting name
	public void setucdUserId(String ucd_user_id){
		this._ucd_user_id = ucd_user_id;
	}


	public String getucdDivisionId(){
		return this._ucd_division_id;
	}

	// setting name
	public void setucdDivisionId(String ucd_division_id){
		this._ucd_division_id = ucd_division_id;
	}
	public String getucdCompanyId(){
		return this._ucd_company_id;
	}

	// setting name
	public void setucdCompanyId(String ucd_company_id){
		this._ucd_company_id = ucd_company_id;
	}
}
