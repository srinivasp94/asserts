package com.nsl.app;

public class Company_division_crops {

	//private variables
	int    _cdc_id;
	String _cdc_company_id;
	String _cdc_division_id;
	String _cdc_crop_id;

	// Empty constructor
	public Company_division_crops(){

	}
	// constructor
	public Company_division_crops(int id, String cdc_company_id, String cdc_division_id, String cdc_crop_id){
		this._cdc_id            = id;
		this._cdc_company_id    = cdc_company_id;
		this._cdc_division_id       = cdc_division_id;
		this._cdc_crop_id   = cdc_crop_id;


	}

	// constructor
	public Company_division_crops(String cdc_company_id, String cdc_division_id, String cdc_crop_id){
		this._cdc_company_id    = cdc_company_id;
		this._cdc_division_id       = cdc_division_id;
		this._cdc_crop_id   = cdc_crop_id;

	}
	// getting ID
	public int getID(){
		return this._cdc_id;
	}
	
	// setting id
	public void setID(int id){
		this._cdc_id = id;
	}

	public String getCdcCompanyId(){
		return this._cdc_company_id;
	}

	// setting name
	public void setCdcCompanyId(String cdc_company_id){
		this._cdc_company_id = cdc_company_id;
	}


	public String getCdcCropId(){
		return this._cdc_crop_id;
	}

	// setting name
	public void setCdcCropId(String cdc_crop_id){
		this._cdc_crop_id = cdc_crop_id;
	}
	public String getCdcDivisionId(){
		return this._cdc_division_id;
	}

	// setting name
	public void setCdcDivisionId(String cdc_division_id){
		this._cdc_division_id = cdc_division_id;
	}


}
