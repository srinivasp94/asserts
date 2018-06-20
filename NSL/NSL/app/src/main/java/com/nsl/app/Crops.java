package com.nsl.app;

public class Crops {

	//private variables
	int    _crop_id;
	String _crop_masterid;
	String _crop_name;
	String _crop_sap_id;
	String _crop_code;
	String _crop_division_id;
	String _crop_cdatetime;
	String _crop_udatetime;

	// Empty constructor
	public Crops(){

	}
	// constructor
	public Crops(int id, String crop_master_id, String name, String crop_sap_id , String crop_code , String crop_division_id, String crop_cdatetime, String crop_udatetime){
		this._crop_id = id;
		this._crop_masterid = crop_master_id;
		this._crop_name = name;
		this._crop_sap_id = crop_sap_id;
		this._crop_code = crop_code;
		this._crop_division_id = crop_division_id;
		this._crop_cdatetime = crop_cdatetime;
		this._crop_udatetime = crop_udatetime;
	}

	// constructor
	public Crops(String crop_master_id, String name, String crop_sap_id, String crop_code, String crop_division_id, String crop_cdatetime, String crop_udatetime){
		this._crop_masterid = crop_master_id;
		this._crop_name = name;
		this._crop_sap_id = crop_sap_id;
		this._crop_code = crop_code;
		this._crop_division_id = crop_division_id;
		this._crop_cdatetime = crop_cdatetime;
		this._crop_udatetime = crop_udatetime;
	}
	// getting ID
	public int getID(){
		return this._crop_id;
	}
	
	// setting id
	public void setID(int id){
		this._crop_id = id;
	}

	public String getCropMasterId(){
		return this._crop_masterid;
	}

	// setting name
	public void setCropMasterID(String crop_masterid){
		this._crop_masterid = crop_masterid;
	}
	// getting name
	public String getCropName(){
		return this._crop_name;
	}
	
	// setting name
	public void setCropName(String div_name){
		this._crop_name = div_name;
	}

	public String getCropcode(){
		return this._crop_code;
	}

	// setting name
	public void setCropcode(String div_code){
		this._crop_code = div_code;
	}
	
	// getting phone number
	public String getCropsapid(){

		return this._crop_sap_id;
	}
	
	// setting phone number
	public void setCropsapid(String div_sap_id){

		this._crop_sap_id = div_sap_id;
	}
	// getting phone number
	public String getCropdivisionId(){

		return this._crop_division_id;
	}

	// setting phone number
	public void setCropdivisionId(String crop_division_id){

		this._crop_division_id = crop_division_id;
	}
	public String getCropcdatetime(){

		return this._crop_cdatetime;
	}

	// setting phone number
	public void setCropcdatetime(String div_cdatetime){

		this._crop_cdatetime = div_cdatetime;
	}
	public String getCropudatetime(){

		return this._crop_udatetime;
	}

	// setting phone number
	public void setCropudatetime(String crop_udatetime){

		this._crop_udatetime = crop_udatetime;
	}
}
