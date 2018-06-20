package com.nsl.app;

public class Divisions {

	//private variables
	int    _div_id;
	String _div_masterid;
	String _div_name;
	String _div_sap_id;
	String _div_code;
	String _div_status;
	String _div_cdatetime;
	String _div_udatetime;

	// Empty constructor
	public Divisions(){

	}
	// constructor
	public Divisions(int id, String div_master_id, String name, String div_sap_id , String div_code ,String div_status, String div_cdatetime,String div_udatetime){
		this._div_id = id;
		this._div_masterid = div_master_id;
		this._div_name = name;
		this._div_sap_id = div_sap_id;
		this._div_code = div_code;
		this._div_status = div_status;
		this._div_cdatetime = div_cdatetime;
		this._div_udatetime = div_udatetime;
	}

	// constructor
	public Divisions(String div_master_id,String name, String div_sap_id,String div_code,String div_status, String div_cdatetime,String div_udatetime){
		this._div_masterid = div_master_id;
		this._div_name     = name;
		this._div_sap_id   = div_sap_id;
		this._div_code     = div_code;
		this._div_status   = div_status;
		this._div_cdatetime = div_cdatetime;
		this._div_udatetime = div_udatetime;
	}
	// getting ID
	public int getID(){
		return this._div_id;
	}
	
	// setting id
	public void setID(int id){
		this._div_id = id;
	}

	public String getDivMasterId(){
		return this._div_masterid;
	}

	// setting name
	public void setDivMasterID(String div_masterid){
		this._div_masterid = div_masterid;
	}
	// getting name
	public String getDivName(){
		return this._div_name;
	}
	
	// setting name
	public void setDivName(String div_name){
		this._div_name = div_name;
	}

	public String getDivcode(){
		return this._div_code;
	}

	// setting name
	public void setDivcode(String div_code){
		this._div_code = div_code;
	}
	
	// getting phone number
	public String getDivsapid(){

		return this._div_sap_id;
	}
	
	// setting phone number
	public void setDivsapid(String div_sap_id){

		this._div_sap_id = div_sap_id;
	}
	// getting phone number
	public String getDivstatus(){

		return this._div_status;
	}

	// setting phone number
	public void setDivstatus(String div_status){

		this._div_status = div_status;
	}
	public String getDivcdatetime(){

		return this._div_cdatetime;
	}

	// setting phone number
	public void setDivcdatetime(String div_cdatetime){

		this._div_cdatetime = div_cdatetime;
	}
	public String getDivudatetime(){

		return this._div_udatetime;
	}

	// setting phone number
	public void setDivudatetime(String div_udatetime){

		this._div_udatetime = div_udatetime;
	}
}
