package com.nsl.app;

public class Regions {

	//private variables
	int    _region_id;
	String _region_master_id;
	String _region_name;
	String _region_code;
	String _region_status;

	// Empty constructor
	public Regions(){

	}
	// constructor
	public Regions(int id, String region_master_id, String name,  String region_code , String region_status){
		this._region_id     = id;
		this._region_name   = name;
		this._region_master_id   = region_master_id;
		this._region_code   = region_code;
		this._region_status = region_status;

	}

	// constructor
	public Regions(String region_master_id, String name, String region_code, String region_status){
		this._region_master_id = region_master_id;
		this._region_name      = name;
		this._region_code      = region_code;
		this._region_status    = region_status;

	}
	// getting ID
	public int getID(){
		return this._region_id;
	}
	
	// setting id
	public void setID(int id){
		this._region_id = id;
	}

	public String getRegionMasterId(){
		return this._region_master_id;
	}

	// setting name
	public void setRegionMasterID(String region_master_id){
		this._region_master_id = region_master_id;
	}
	// getting name
	public String getRegionName(){
		return this._region_name;
	}
	
	// setting name
	public void setRegionName(String region_name){
		this._region_name = region_name;
	}

	public String getRegioncode(){
		return this._region_code;
	}

	// setting name
	public void setRegioncode(String region_code){
		this._region_code = region_code;
	}
	

	// getting phone number
	public String getRegionstatus(){

		return this._region_status;
	}

	// setting phone number
	public void setRegionstatus(String region_status){

		this._region_status = region_status;
	}


}
