package com.nsl.app;

public class Scheme_Products {

	//private variables
	int    _sp_id;
	String _sp_scheme_id;
	String _sp_product_id;
	String _sp_price_per_packet;
	String _sp_region_id;
	String _sp_company_id;
	String _sp_crop_id;
	String _sp_slab_id;
	String _sp_booking_incentive;
	String _sp_valid_from;
	String _sp_valid_to;
	String _sp_booking_year;
	String _sp_season_code;
	String _sp_extenstion_date;

	// Empty constructor
	public Scheme_Products(){

	}
	// constructor
	public Scheme_Products(int sp_id,
								String sp_scheme_id,
								String sp_product_id,
								String sp_price_per_packet,
								String sp_region_id,
								String sp_company_id,
								String sp_crop_id,
								String sp_slab_id,
								String sp_booking_incentive,
								String sp_valid_from,
								String sp_valid_to,
								String sp_booking_year,
								String sp_season_code,
								String sp_extenstion_date){
		this._sp_id                   = sp_id;
		this._sp_scheme_id            = sp_scheme_id;
		this._sp_product_id           = sp_product_id;
		this._sp_price_per_packet     = sp_price_per_packet;
		this._sp_region_id            = sp_region_id;
		this._sp_company_id           = sp_company_id;
		this._sp_crop_id              = sp_crop_id;
		this._sp_slab_id              = sp_slab_id;
		this._sp_booking_incentive    = sp_booking_incentive;
		this._sp_valid_from           = sp_valid_from;
		this._sp_valid_to             = sp_valid_to;
		this._sp_booking_year         = sp_booking_year;
		this._sp_season_code          = sp_season_code;
		this._sp_extenstion_date      = sp_extenstion_date;


	}

	// constructor
	public Scheme_Products(String sp_scheme_id,
								String sp_product_id,
								String sp_price_per_packet,
								String sp_region_id,
								String sp_company_id,
								String sp_crop_id,
								String sp_slab_id,
								String sp_booking_incentive,
								String sp_valid_from,
								String sp_valid_to,
								String sp_booking_year,
								String sp_season_code,
								String sp_extenstion_date){
		this._sp_scheme_id            = sp_scheme_id;
		this._sp_product_id           = sp_product_id;
		this._sp_price_per_packet     = sp_price_per_packet;
		this._sp_region_id            = sp_region_id;
		this._sp_company_id           = sp_company_id;
		this._sp_crop_id              = sp_crop_id;
		this._sp_slab_id              = sp_slab_id;
		this._sp_booking_incentive    = sp_booking_incentive;
		this._sp_valid_from           = sp_valid_from;
		this._sp_valid_to             = sp_valid_to;
		this._sp_booking_year         = sp_booking_year;
		this._sp_season_code          = sp_season_code;
		this._sp_extenstion_date      = sp_extenstion_date;
	}
	// getting ID
	public int getID(){
		return this._sp_id;
	}

	// setting id
	public void setID(int id){
		this._sp_id = id;
	}

	public String getspscheme_id(){
		return this._sp_scheme_id;
	}

	// setting name
	public void setspscheme_id(String sp_scheme_id)
	{
		this._sp_scheme_id = sp_scheme_id;
	}

	public String getspproductprice(){
		return this._sp_price_per_packet;
	}

	// setting namek
	public void setspproductprice(String sp_price_per_packet){
		this._sp_price_per_packet = sp_price_per_packet;
	}



	public String getspProductId(){
		return this._sp_product_id;
	}

	// setting name
	public void setspProductId(String sp_product_id){
		this._sp_product_id = sp_product_id;
	}
	public String getspRegionId(){
		return this._sp_region_id;
	}

	// setting name
	public void setspRegionId(String sp_region_id){
		this._sp_region_id = sp_region_id;
	}

	public String get_sp_crop_id() {
		return _sp_crop_id;
	}

	public void set_sp_crop_id(String _sp_crop_id) {
		this._sp_crop_id = _sp_crop_id;
	}

	public String get_sp_slab_id() {
		return _sp_slab_id;
	}

	public void set_sp_slab_id(String _sp_slab_id) {
		this._sp_slab_id = _sp_slab_id;
	}

	public String get_sp_booking_incentive() {
		return _sp_booking_incentive;
	}

	public void set_sp_booking_incentive(String _sp_booking_incentive) {
		this._sp_booking_incentive = _sp_booking_incentive;
	}

	public String get_sp_valid_from() {
		return _sp_valid_from;
	}

	public void set_sp_valid_from(String _sp_valid_from) {
		this._sp_valid_from = _sp_valid_from;
	}

	public String get_sp_valid_to() {
		return _sp_valid_to;
	}

	public void set_sp_valid_to(String _sp_valid_to) {
		this._sp_valid_to = _sp_valid_to;
	}

	public String get_sp_season_code() {
		return _sp_season_code;
	}

	public void set_sp_season_code(String _sp_season_code) {
		this._sp_season_code = _sp_season_code;
	}

	public String get_sp_booking_year() {
		return _sp_booking_year;
	}

	public void set_sp_booking_year(String _sp_booking_year) {
		this._sp_booking_year = _sp_booking_year;
	}

	public String get_sp_extenstion_date() {
		return _sp_extenstion_date;
	}

	public void set_sp_extenstion_date(String sp_extenstion_date) {
		this._sp_extenstion_date = sp_extenstion_date;
	}

	public String getspCompanyId(){

		return this._sp_company_id;
	}

	// setting name
	public void setspCompanyId(String sp_company_id){
		this._sp_company_id = sp_company_id;
	}
}
