package com.nsl.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.nsl.app.commonutils.Common;
import com.nsl.app.complaints.Complaints;
import com.nsl.app.dailydairy.DailyDairy;
import com.nsl.app.feedback.Feedback;
import com.nsl.app.marketintelligence.Commodity_Price;
import com.nsl.app.marketintelligence.Crop_Shifts;
import com.nsl.app.marketintelligence.Price_Survey;
import com.nsl.app.marketintelligence.Product_Survey;
import com.nsl.app.pojo.BankDetailsListPojo;
import com.nsl.app.pojo.DistributorsRetailerPojo;
import com.nsl.app.pojo.GradePojo;
import com.nsl.app.pojo.MappedRetailerPojo;
import com.nsl.app.pojo.MappedRetailerWithDistributorPojo;
import com.nsl.app.pojo.RetailersNamePojo;
import com.nsl.app.pojo.StockMovementFirstListPojo;
import com.nsl.app.pojo.StockMovementRetailerDetails;
import com.nsl.app.pojo.StockMovementUnSynedPojo;
import com.nsl.app.pojo.StockPlacementPopupPojo;
import com.nsl.app.pojo.StockMovementDetailsPojo;
import com.nsl.app.pojo.StockMovementPoJo;
import com.nsl.app.pojo.StockReturnDetailsPoJo;
import com.nsl.app.pojo.StockReturnPoJo;
import com.nsl.app.pojo.StockReturnUnSynedPojo;
import com.nsl.app.pojo.VersionControlVo;
import com.nsl.app.retailers.Retailer;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 32;

    // Database Name
    private static final String DATABASE_NAME = "NSL.db";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    // Table Names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_COMPANIES = "companies";
    public static final String TABLE_COMPANY_DIVISION_CROPS = "company_division_crop";
    public static final String TABLE_USER_COMPANY_CUSTOMER = "user_company_customer";
    public static final String TABLE_SCHEME_PRODUCTS = "scheme_products";
    public static final String TABLE_USER_COMPANY_DIVISION = "user_company_division";
    public static final String TABLE_CROPS = "crops";
    public static final String TABLE_SCHEMES = "schemes";
    public static final String TABLE_COMPLAINT = "complaints";
    public static final String TABLE_DAILYDAIRY = "dailydairy";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_CUSTOMER_DETAILS = "customer_details";
    public static final String TABLE_DISTRIBUTOR_RETAILER = "distributor_retailer";
    public static final String TABLE_DIVISION = "division";
    public static final String TABLE_ERRORS = "errors";
    public static final String TABLE_FARMERS = "farmers";
    public static final String TABLE_GEO_TRACKING = "geo_tracking";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_UCDD = "user_company_division_distributor";
    public static final String TABLE_SMD = "stock_movement_detail";
    public static final String TABLE_SM = "stock_movement";
    public static final String TABLE_STOCK_MOVEMENT_RETAILER_DETAILS = "stock_movement_retailer_details";
    public static final String TABLE_EVENT_MANAGMENT = "event_management";
    public static final String TABLE_EMPLOYEE_VISIT_MANAGEMENT = "employee_visit_management";
    public static final String TABLE_PAYMENT_COLLECTION = "payment_collection";
    public static final String TABLE_PLANTS = "plants";
    public static final String TABLE_REGION = "region";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_PRODUCT_PRICE = "product_price";
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String TABLE_SERVICEORDER = "service_order";
    public static final String TABLE_SERVICEORDERDETAILS = "service_order_details";
    public static final String TABLE_CUSTOMER_BANKDETAILS = "customer_bankdetails";
    public static final String TABLE_MI_COMMODITY_PRICE = "mi_commodity_price";
    public static final String TABLE_MI_CROP_SHIFTS = "mi_crop_shifts";
    public static final String TABLE_MI_PRICE_SURVEY = "mi_price_survey";
    public static final String TABLE_MI_PRODUCT_SURVEY = "mi_product_survey";
    public static final String TABLE_RETAILER = "retailers";
    public static final String TABLE_GRADE = "grade";
    public static final String TABLE_VERSION_CONTROL = "table_version_control";
    public static final String TABLE_STOCK_RETURNS = "stock_returns";
    public static final String TABLE_STOCK_RETURNS_DETAILS = "stock_returns_details";

    public static final String VERSION_TABLE_NAME = "table_name";
    public static final String VERSION_CODE = "version_code";
    public static final String UPDATED_DATE = "updated_date";

    // Contacts Table Columns names
    public static final String GRADE_ID = "grade_id";
    public static final String GRADE_NAME = "grade_name";
    public static final String PRICE_PER_KM = "price_per_km";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PH_NO = "phone_number";


    // Contacts Table Columns names
    public static final String KEY_ORDER_ID = "id";
    public static final String KEY_ORDER_NAME = "name";
    public static final String KEY_ORDER_AMT = "phone_number";


    // table companies column names
    public static final String KEY_TABLE_COMPANIES_ID = "id";
    public static final String KEY_TABLE_COMPANIES_MASTER_ID = "company_id";
    public static final String KEY_TABLE_COMPANIES_NAME = "name";
    public static final String KEY_TABLE_COMPANIES_COMPANY_SAP_ID = "company_sap_id";
    public static final String KEY_TABLE_COMPANIES_COMPANY_CODE = "company_code";
    public static final String KEY_TABLE_COMPANIES_COMPANY_STATUS = "status";
    public static final String KEY_TABLE_COMPANIES_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_COMPANIES_UPDATED_DATETIME = "updated_datetime";

    // table company_division_crops column names
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_ID = "id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID = "crop_id";

    // table USER COMPANY CUSTOMER column names
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_ID = "id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID = "user_id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID = "customer_id";

    // table SCHEME PRODUCTS column names
    public static final String KEY_TABLE_SCHEME_PRODUCTS_ID = "id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID = "scheme_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_PRICE = "price_per_packet";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_REGION_ID = "region_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_CROP_ID = "crop_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID = "slab_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE = "booking_incentive";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM = "valid_from";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_VALID_TO = "valid_to";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR = "booking_year";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE = "season_code";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE = "extenstion_date";


    // table USER COMPANY DIVISION column names
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_ID = "id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_USER_ID = "user_id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID = "division_id";

    // table complaints column names
    public static final String KEY_TABLE_COMPLAINTS_ID = "complaint_id";
    public static final String KEY_TABLE_COMPLAINT_USER_ID = "user_id";
    public static final String KEY_TABLE_COMPLAINTS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_COMPLAINTS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_COMPLAINTS_CROP_ID = "crop_id";
    public static final String KEY_TABLE_COMPLAINTS_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO = "marketing_lot_number";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE = "complaint_type";
    public static final String KEY_TABLE_COMPLAINTS_FARMER_NAME = "farmer_name";
    public static final String KEY_TABLE_COMPLAINTS_CONTACT_NO = "contact_no";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES = "complaint_area_acres";
    public static final String KEY_TABLE_COMPLAINTS_SOIL_TYPE = "soil_type";
    public static final String KEY_OTHERS = "others";
    public static final String KEY_TABLE_COMPLAINTS_PURCHASED_QTY = "purchased_quantity";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_QTY = "complain_quantity";
    public static final String KEY_PURCHASE_DATE = "purchase_date";
    public static final String KEY_TABLE_COMPLAINTS_BILL_NUMBER = "bill_number";
    public static final String KEY_TABLE_COMPLAINTS_RETAILER_NAME = "retailer_name";
    public static final String KEY_TABLE_COMPLAINTS_DISTRIBUTOR = "distributor";
    public static final String KEY_TABLE_COMPLAINTS_MANDAL = "mandal";
    public static final String KEY_TABLE_COMPLAINTS_VILAGE = "village";
    public static final String KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD = "image_upload";
    public static final String KEY_REGULATORY_TYPE = "regulatory_type";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_DATE = "sampling_date";
    public static final String KEY_TABLE_COMPLAINTS_PLACE_SAMPLING = "place_sampling";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME = "sampling_officer_name";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT = "sampling_officer_contact";
    public static final String KEY_TABLE_COMPLAINTS_COMMENTS = "comments";
    public static final String KEY_TABLE_COMPLAINTS_STATUS = "status";
    public static final String KEY_TABLE_COMPLAINTS_REMARKS = "remarks";
    public static final String KEY_TABLE_COMPLAINTS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_COMPLAINTS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_COMPLAINTS_FFMID = "ffmid";

    //FEEDBACK TABLE COLUMN NAMES

    public static final String KEY_TABLE_FEEDBACK_FEEDBACK_ID = "feedback_id";
    public static final String KEY_TABLE_FEEDBACK_USER_ID = "user_id";
    public static final String KEY_TABLE_FEEDBACK_FARMER_NAME = "farmer_name";
    public static final String KEY_TABLE_FEEDBACK_PLACE = "place";
    public static final String KEY_TABLE_FEEDBACK_CONTACT_NO = "contact_no";
    public static final String KEY_TABLE_FEEDBACK_CROP = "crop";
    public static final String KEY_TABLE_FEEDBACK_HYBRID = "hybrid";
    public static final String KEY_TABLE_FEEDBACK_SOWING_DATE = "sowing_date";
    public static final String KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE = "feedback_message";
    public static final String KEY_TABLE_FEEDBACK_IMAGE = "image";
    public static final String KEY_TABLE_FEEDBACK_FFMID = "ffmid";

    // table crops column names
    public static final String KEY_TABLE_CROPS_CROP_ID = "ids";
    public static final String KEY_TABLE_CROPS_CROP_MASTER_ID = "crop_id";
    public static final String KEY_TABLE_CROPS_CROP_NAME = "crop_name";
    public static final String KEY_TABLE_CROPS_CROP_SAP_ID = "crop_sap_id";
    public static final String KEY_TABLE_CROPS_CROP_CODE = "crop_code";
    public static final String KEY_TABLE_CROPS_DIVISION_ID = "divsion_id";
    public static final String KEY_TABLE_CROPS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CROPS_UPDATED_DATETIME = "updated_datetime";


    // table customers column names
    public static final String KEY_TABLE_CUSTOMER_ID = "c_id";
    public static final String KEY_TABLE_CUSTOMER_MASTER_ID = "customer_id";
    public static final String KEY_TABLE_CUSTOMER_NAME = "customer_name";
    public static final String KEY_TABLE_CUSTOMER_CODE = "customer_code";
    public static final String KEY_TABLE_CUSTOMER_ADDRESS = "address";
    public static final String KEY_TABLE_CUSTOMER_STREET = "street";
    public static final String KEY_TABLE_CUSTOMER_CITY = "city";
    public static final String KEY_TABLE_CUSTOMER_COUNTRY = "country";
    public static final String KEY_TABLE_CUSTOMER_REGION_ID = "region_id";
    public static final String KEY_TABLE_CUSTOMER_TELEPHONE = "telephone";

    public static final String KEY_TABLE_CUSTOMER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_CUSTOMER_STATUS = "status";
    public static final String KEY_TABLE_CUSTOMER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CUSTOMER_UPDATED_DATETIME = "updated_datetime";

    public static final String KEY_TABLE_CUSTOMER_PASSWORD = "password";
    public static final String KEY_TABLE_CUSTOMER_EMAIL = "email";
    public static final String KEY_TABLE_CUSTOMER_STATE = "state";
    public static final String KEY_TABLE_CUSTOMER_DISTRICT = "district";
    public static final String KEY_TABLE_CUSTOMER_LAT_LNG = "lat_lon";


    // table servce order column names
    public static final String KEY_TABLE_SERVICEORDER_ID = "service_id";
    public static final String KEY_TABLE_SERVICEORDER_MASTER_ID = "order_id";
    public static final String KEY_TABLE_SERVICEORDER_ORDERTYPE = "order_type";
    public static final String KEY_TABLE_SERVICEORDER_ORDERDATE = "order_date";
    public static final String KEY_TABLE_SERVICEORDER_USER_ID = "user_id";
    public static final String KEY_TABLE_SERVICEORDER_CUSTOMER_ID = "customer_id";
    public static final String KEY_TABLE_SERVICEORDER_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_SERVICEORDER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_SERVICEORDER_STATUS = "status";
    public static final String KEY_TABLE_SERVICEORDER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_SERVICEORDER_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_SERVICEORDER_SLAB_ID = "slab_id";
    public static final String KEY_TABLE_SERVICEORDER_FFM_ID = "ffmid";
    public static final String KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT = "advance_amount";
    public static final String KEY_TABLE_SERVICEORDER_CREATED_BY = "createdby";
    public static final String KEY_TABLE_SERVICEORDER_APPROVAL_STATUS = "approval_status";
    public static final String KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS = "approval_comments";
    public static final String KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET = "offline_approval_status";
    public static final String KEY_TABLE_SERVICEORDER_UPDATED_BY = "updatedby";

    // table servce order details column names
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ID = "order_detail_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID = "service_order_detail_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID = "service_relation_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID = "crop_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID = "scheme_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY = "quantity";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE = "order_price";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT = "advance_amount";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_STATUS = "status";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME = "updated_datetime";

    // table customer_details column names
    public static final String KEY_TABLE_CUSTOMER_DETAILS_ID = "customer_details_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID = "customer_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT = "credit_limit";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET = "inside_bucket";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET = "outside_bucket";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_STATUS = "status";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE = "credit_balance";

    // table DISTRIBUTOR_RETAILER column names
    public static final String KEY_TABLE_DISTRIBUTOR_RTAILER_ID = "distributor_retailer_id";
    public static final String KEY_TABLE_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_TABLE_RETAILER_ID = "retailer_id";


    // table division column names
    public static final String KEY_TABLE_DIVISION_ID = "div_id";
    public static final String KEY_TABLE_DIVISION_MASTER_ID = "division_id";
    public static final String KEY_TABLE_DIVISION_NAME = "division_name";
    public static final String KEY_TABLE_DIVISION_SAP_ID = "division_sap_id";
    public static final String KEY_TABLE_DIVISION_CODE = "division_code";
    public static final String KEY_TABLE_DIVISION_STATUS = "status";
    public static final String KEY_TABLE_DIVISION_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_DIVISION_UPDATED_DATETIME = "updated_datetime";


    // table errors column names
    private static final String KEY_TABLE_ERRORS_ID = "id";
    private static final String KEY_TABLE_ERRORS_NAME = "name";
    private static final String KEY_TABLE_ERRORS_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_ERRORS_UPDATED_DATETIME = "updated_datetime";

    public static final String KEY_DD_ID = "id";
    public static final String KEY_DD_MASTER_ID = "dailydairy_id";
    public static final String KEY_DD_TITLE = "title";
    public static final String KEY_DD_USER_ID = "user_id";
    public static final String KEY_DD_COMMENTS = "comments";
    public static final String KEY_DD_TIME_SLOT = "time_slot";
    public static final String KEY_DD_DATE = "dairy_date";
    public static final String KEY_DD_CREATED_DATE = "created_date";
    public static final String KEY_DD_FFMID = "ffmid";
    public static final String KEY_DD_TYPE = "type"; // 1-> Normal DD, 2-> Adhoc Task
    public static final String KEY_DD_TENTATIVE_TIME = "tentative_time";
    public static final String KEY_DD_STATUS = "status";// 1-> Pending DD, 2-> Task Complete

    // table EVENT MANAGEMENT column names
    private static final String KEY_TABLE_EVENT_MANAGEMENT_ID = "event_id";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_TITLE = "title";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_DATE = "planed_event_date";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_TIME = "planed_event_starttime_endtime";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_CONCER_PERSON = "concern_person";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_VILLAGE = "village";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_LOCATION_ADDR = "location_addr";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PARTICIPANTS = "participants";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_STATUS = "status";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_UPDATED_DATETIME = "updated_datetime";

    // table farmers column names

    private static final String KEY_TABLE_FARMER_ID = "farmer_id";
    private static final String KEY_TABLE_FARMER_NAME = "farmer_name";
    private static final String KEY_TABLE_FARMER_PHONE = "phone";
    private static final String KEY_TABLE_FARMER_STATE = "state";
    private static final String KEY_TABLE_FARMER_DISTRICT = "district";
    private static final String KEY_TABLE_FARMER_VILLAGE = "village";
    private static final String KEY_TABLE_FARMER_MANDAL = "mandal";
    private static final String KEY_TABLE_FARMER_TOTAL_LAND_HOLDING = "total_land_holding";
    private static final String KEY_TABLE_FARMER_CROP_ID = "crop_id";
    private static final String KEY_TABLE_FARMER_STATUS = "status";
    private static final String KEY_TABLE_FARMER_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_FARMER_UPDATED_DATETIME = "updated_datetime";


    // table GEO_TRACKING column names
    public static final String KEY_TABLE_GEO_TRACKING_ID = "t_id";
    public static final String KEY_TABLE_GEO_TRACKING_MASTER_ID = "tracking_id";
    public static final String KEY_TABLE_GEO_TRACKING_VISIT_TYPE = "visit_type";
    public static final String KEY_TABLE_GEO_TRACKING_USER_ID = "user_id";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG = "check_in_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG = "check_out_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG = "route_path_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_DISTANCE = "distance";
    public static final String KEY_TABLE_GEO_TRACKING_VISIT_DATE = "visit_date";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME = "check_in_time";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME = "check_out_time";
    public static final String KEY_TABLE_GEO_TRACKING_STATUS = "status";
    public static final String KEY_TABLE_GEO_TRACKING_FFMID = "ffmid";
    public static final String KEY_TABLE_GEO_TRACKING_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_GEO_TRACKING_UPDATED_STATUS = "updated_status"; //1-> Synced, 0-> Need to Sync


    // table companies column names
    public static final String KEY_TABLE_USERS_USER_ID = "id";
    public static final String KEY_TABLE_USERS_MASTER_ID = "user_id";
    public static final String KEY_TABLE_USERS_FIRST_NAME = "first_name";
    public static final String KEY_TABLE_USERS_LAST_NAME = "last_name";
    public static final String KEY_TABLE_USERS_MOBILE_NO = "mobile_no";
    public static final String KEY_TABLE_USERS_EMAIL = "email";
    public static final String KEY_TABLE_USERS_SAP_ID = "sap_id";
    public static final String KEY_TABLE_USERS_PASSWORD = "password";
    public static final String KEY_TABLE_USERS_ROLE_ID = "role_id";
    public static final String KEY_TABLE_USERS_REPORTING_MANAGER_ID = "reporting_manager_id";
    public static final String KEY_TABLE_USERS_STATUS = "status";
    public static final String KEY_TABLE_USERS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_USERS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_USERS_DESIGNATION = "designation";
    public static final String KEY_TABLE_USERS_HEADQUARTER = "headquarter";
    public static final String KEY_TABLE_USERS_LOCATION = "location";
    public static final String KEY_TABLE_USERS_REGION_ID = "region_id";
    public static final String KEY_TABLE_USERS_IMAGE = "image";
    public static final String KEY_TABLE_USERS_GRADE = "grade";
    public static final String KEY_TABLE_USERS_COST_CENTER = "cost_center";


    // table ucdd column names
    private static final String KEY_TABLE_UCDD_JUNCTION_ID = "junction_id";
    private static final String KEY_TABLE_UCDD_USER_ID = "user_id";
    private static final String KEY_TABLE_UCDD_COMPANY_ID = "company_id";
    private static final String KEY_TABLE_UCDD_DIVISION_ID = "division_id";
    private static final String KEY_TABLE_UCDD_CUSTOMER_ID = "customer_id";


    // table SM column names
    private static final String KEY_TABLE_SM_ID = "stock_movement_id";
    private static final String KEY_TABLE_SM_MOVEMENT_TYPE = "movement_type";
    private static final String KEY_TABLE_SM_USER_ID = "user_id";
    private static final String KEY_TABLE_SM_DIVISION = "division_id";
    private static final String KEY_TABLE_SM_COMPANY_ID = "company_id";
    private static final String KEY_TABLE_SM_CREATED_BY = "created_by";
    private static final String KEY_TABLE_SM_UPDATED_BY = "updated_by";
    private static final String KEY_TABLE_SM_STATUS = "status";
    public static final String FFM_ID = "ffm_id";
    private static final String KEY_TABLE_SM_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_SM_UPDATED_DATETIME = "updated_datetime";

    // table SMD column names

    public static final String KEY_TABLE_SMD_DETAIL_ID = "stock_movement_detail_id";
    private static final String KEY_TABLE_SMD_STOCK_MOVEMENT_ID = "stock_movement_id";
    private static final String KEY_TABLE_SMD_USER_TYPE = "user_type";
    private static final String KEY_TABLE_SMD_CUSTOMER_ID = "customer_id";
    private static final String KEY_TABLE_SMD_CROP_ID = "crop_id";
    private static final String KEY_TABLE_SMD_PRODUCT_ID = "product_id";
    private static final String KEY_TABLE_SMD_STOCK_PLACED = "stock_placed";
    private static final String KEY_TABLE_SMD_CURRENT_STOCK = "current_stock";
    private static final String KEY_TABLE_SMD_PLACED_DATE = "placed_date";
    private static final String KEY_TABLE_SMD_POG = "pog";
    private static final String KEY_TABLE_CREATED_BY = "created_by";
    private static final String KEY_TABLE_UPDATED_BY = "updated_by";
    private static final String KEY_TABLE_SMD_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_SMD_UPDATED_DATETIME = "updated_datetime";

    private static final String KEY_TABLE_SMD_RETAILER_ID_PRIMARY_KEY = "stock_movement_retailer_id";
    private static final String KEY_TABLE_SMD_RETAILER_USER_ID = "user_id";
    private static final String KEY_TABLE_SMD_RETAILER_ID = "retailer_id";
    private static final String KEY_TABLE_SMD_RETAILER_VERIFIED = "verified";
    private static final String KEY_TABLE_SMD_RETAILER_VERIFIED_BY = "verified_by";


    //table employee_visit_management column names

    public static final String KEY_EMP_VISIT_ID = "emp_v_id";
    public static final String KEY_EMP_VISIT_MASTER_ID = "emp_visit_id";
    public static final String KEY_EMP_VISIT_USER_ID = "user_id";
    public static final String KEY_EMP_VISIT_CUSTOMER_ID = "customer_id";
    public static final String KEY_EMP_VISIT_PLAN_TYPE = "visit_plan_type";
    public static final String KEY_EMP_PURPOSE_VISIT = "purpose_visit";
    public static final String KEY_EMP_PURPOSE_VISIT_IDS = "purpose_visit_ids";
    public static final String KEY_EMP_PLAN_DATE_TIME = "plan_date_time";
    public static final String KEY_EMP_CONCERN_PERSON_NAME = "concern_person_name";
    public static final String KEY_EMP_MOBILE = "mobile";
    public static final String KEY_EMP_VILLAGE = "village";
    public static final String KEY_EMP_LOCATION_ADDRESS = "location_address";
    public static final String KEY_EMP_FEILD_AREA = "field_area";
    public static final String KEY_EMP_CHECK_IN_TIME = "check_in_time";
    public static final String KEY_EMP_COMMENTS = "comments";
    public static final String KEY_EMP_CREATED_BY = "created_by";
    public static final String KEY_EMP_UPDATED_BY = "updated_by";
    public static final String KEY_EMP_STATUS = "status";
    public static final String KEY_EMP_FFM_ID = "emp_ffm_id";
    public static final String KEY_EMP_TYPE = "type";
    public static final String KEY_EMP_CREATED_DATETIME = "created_datetime";
    public static final String KEY_EMP_UPDATE_DATETIME = "update_datetime";
    public static final String KEY_EMP_GEO_TRACKING_ID = "geo_tracking_id";
    public static final String KEY_EMP_APPROVAL_STATUS = "approval_status";
    public static final String KEY_EMP_EVENT_NAME = "event_name";
    public static final String KEY_EMP_EVENT_END_DATE = "event_end_date";
    public static final String KEY_EMP_EVENT_PURPOSE = "event_purpose";
    public static final String KEY_EMP_EVENT_VENUE = "event_venue";
    public static final String KEY_EMP_EVENT_PARTICIPANTS = "event_participants";
    public static final String KEY_EMP_TASK_COMPLETED_LATLNG = "task_completed_latlng";


// table plants column names

    private static final String KEY_PLANT_ID = "plant_id";
    private static final String KEY_PLANT_NAME = "plant_name";
    private static final String KEY_PLANT_SAP_CODE = "plant_sap_code";
    private static final String KEY_STATE = "state";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PLANTS_STATUS = "status";
    private static final String KEY_PLANTS_CREATED_DATETIME = "created_datetime";
    private static final String KEY_PLANTS_UPDATEd_DATETIME = "updated_datetime";

// Table region column names

    public static final String KEY_SCHEMES_ID = "id";
    public static final String KEY_SCHEMES_MASTER_ID = "scheme_id";
    public static final String KEY_SCHEMES_TITLE = "scheme_title";
    public static final String KEY_SCHEMES_SAP_CODE = "scheme_sap_code";
    public static final String KEY_SCHEMES_FILE_LOCATION = "file_location";
    public static final String KEY_SCHEMES_STATUS = "status";

    // Table schemes column names

    public static final String KEY_REGION_ID = "id";
    public static final String KEY_REGION__MASTER_ID = "region_id";
    public static final String KEY_REGION_CODE = "region_code";
    public static final String KEY_REGION_NAME = "region_name";
    public static final String KEY_REGION_STATUS = "status";

// Table products column names

    public static final String KEY_PRODUCT_ID = "product_ids";
    public static final String KEY_PRODUCT_MASTER_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_DESCRIPTION = "product_description";
    public static final String KEY_PRODUCT_SAP_CODE = "product_sap_code";
    public static final String KEY_PRODUCT_CROP_ID = "product_crop_id";
    public static final String KEY_PRODUCTS_COMPANY_ID = "company_id";
    public static final String KEY_PRODUCTS_DIVISION_ID = "division_id";
    public static final String KEY_PRODUCTS_REGION = "region";
    public static final String KEY_PRODUCTS_PRICE = "price";
    public static final String KEY_PRODUCTS_PACKETS_COUNT = "no_packets";
    public static final String KEY_PRODUCTS_CATALOG_URL = "catalog_url";
    public static final String KEY_PRODUCT_DISCOUNT = "discount";
    public static final String KEY_PRODUCT_FROM_DATE = "from_date";
    public static final String KEY_PRODUCT_TO_DATE = "to_date";
    public static final String KEY_PRODUCT_STATUS = "status";
    public static final String KEY_PRODUCT_IMAGE = "products_images";
    public static final String KEY_PRODUCT_VIDEO = "product_videos";
    public static final String KEY_PRODUCT_CREATED_DATETIME = "created_datetime";
    public static final String KEY_PRODUCT_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_PRODUCT_BRAND_NAME = "brand_name";

    //table payment_collection column names
    public static final String KEY_PAYMENT_COLLECTION_PAYMENT_ID = "p_id";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID = "payment_id";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_TYPE = "payment_type";
    private static final String KEY_PAYMENT_COLLECTION_USER_ID = "user_id";
    private static final String KEY_PAYMENT_COLLECTION_COMPANY_ID = "company_id";
    private static final String KEY_PAYMENT_COLLECTION_DIVISION_ID = "division_id";
    private static final String KEY_PAYMENT_COLLECTION_CUSTOMER_ID = "customer_id";
    private static final String KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT = "total_amount";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_MODE = "payment_mode";
    private static final String KEY_PAYMENT_COLLECTION_BANK_NAME = "bank_name";
    private static final String KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO = "rtgs_or_neft_no";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME = "payment_datetime";
    private static final String KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER = "date_on_cheque_no";
    private static final String KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO = "cheque_no_dd_no";
    private static final String KEY_PAYMENT_COLLECTION_STATUS = "status";
    private static final String KEY_PAYMENT_COLLECTION_CREATED_DATETIME = "created_datetime";
    private static final String KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME = "updated_datetime";
    public static final String KEY_PAYMENT_COLLECTION_FFMID = "ffmid";

    // Table customer_bankdetails column names

    public static final String KEY_BANKDETAIL_ID = "id";
    public static final String KEY_BANKDETAIL_MASTER_ID = "bank_detail_id";
    public static final String KEY_BANKDETAIL_CUSTOMER_ID = "customer_id";
    public static final String KEY_BANKDETAIL_ACCOUNT_NUMBER = "account_no";
    public static final String KEY_BANKDETAIL_ACCOUNT_NAME = "account_name";
    public static final String KEY_BANKDETAIL_BANK_NAME = "bank_name";
    public static final String KEY_BANKDETAIL_BRANCH_NAME = "branch_name";
    public static final String KEY_BANKDETAIL_IFSC_CODE = "ifsc_code";
    public static final String KEY_BANKDETAIL_STATUS = "status";
    public static final String KEY_BANKDETAIL_CREATED_BY = "created_by";
    public static final String KEY_UPDATED_BY = "updated_by";
    public static final String KEY_CREATED_DATE = "created_date";
    public static final String KEY_BANKDETAIL_FFMID = "ffmid";

// Table mi_commodity_price column names

    public static final String KEY_COMMODITY_PRICE_ID = "id";
    public static final String KEY_COMMODITY_PRICE_MASTER_ID = "commodity_price_id";
    public static final String KEY_COMMODITY_PRICE_CROP_NAME = "crop_name";
    public static final String KEY_COMMODITY_PRICE_VARIETY_TYPE = "variety_type";
    public static final String KEY_COMMODITY_PRICE_APMC_MANDI_PRICE = "apmc_mandi_price";
    public static final String KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE = "commodity_dealer_agent_price";
    public static final String KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY = "purchage_price_by_industry";
    public static final String KEY_COMMODITY_PRICE_CREATED_BY = "created_by";
    public static final String KEY_COMMODITY_PRICE_CREATED_ON = "created_on";
    public static final String KEY_COMMODITY_PRICE_FFMID = "ffmid";

    // Table mi_crop_shifts column names

    public static final String KEY_CROP_SHIFTS_ID = "id";
    public static final String KEY_CROP_SHIFTS_MASTER_ID = "crop_shits_id";
    public static final String KEY_CROP_SHIFTS_CROP_NAME = "crop_name";
    public static final String KEY_CROP_SHIFTS_VARIETY_TYPE = "variety_type";
    public static final String KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA = "previous_year_area";
    public static final String KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA = "current_year_expected_area";
    public static final String KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE = "percentage_increase_decrease";
    public static final String KEY_CROP_SHIFTS_REASON_CROP_SHIFT = "reason_crop_shift";
    public static final String KEY_CROP_SHIFTS_CREATED_BY = "created_by";
    public static final String KEY_CROP_SHIFTS_CREATED_ON = "created_on";
    public static final String KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR = "previous_year_srr";
    public static final String KEY_CROP_SHIFTS_CURRENT_YEAR_SRR = "current_year_srr";
    public static final String KEY_CROP_SHIFTS_NEXT_YEAR_SRR = "next_year_srr";
    public static final String KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED = "crop_in_saved_seed";
    public static final String KEY_CROP_SHIFTS_FFMID = "ffmid";


    // Table mi_price_survey column names

    public static final String KEY_PRICE_SURVEY_ID = "id";
    public static final String KEY_PRICE_SURVEY_MASTER_ID = "price_survey_id";
    public static final String KEY_PRICE_SURVEY_COMPANY_NAME = "company_name";
    public static final String KEY_PRICE_SURVEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRICE_SURVEY_SKU_PACK_SIZE = "sku_pack_size";
    public static final String KEY_PRICE_SURVEY_RETAIL_PRICE = "retail_price";
    public static final String KEY_PRICE_SURVEY_INVOICE_PRICE = "invoice_price";
    public static final String KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE = "net_distributor_landing_price";
    public static final String KEY_PRICE_SURVEY_CREATED_BY = "created_by";
    public static final String KEY_PRICE_SURVEY_CREATED_ON = "created_on";
    public static final String KEY_PRICE_SURVEY_FFMID = "ffmid";


    // Table mi_product_survey column names

    public static final String KEY_PRODUCT_SURVEY_ID = "id";
    public static final String KEY_PRODUCT_SURVEY_MASTER_ID = "product_survey_id";
    public static final String KEY_PRODUCT_SURVEY_COMPANY_NAME = "company_name";
    public static final String KEY_PRODUCT_SURVEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT = "name_of_the_check_segment";
    public static final String KEY_PRODUCT_SURVEY_LAUNCH_YEAR = "launch_year";
    public static final String KEY_PRODUCT_SURVEY_NO_UNITS_SOLD = "no_units_sold";
    public static final String KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT = "area_crop_sown_new_product";
    public static final String KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE = "remarks_unique_feature";
    public static final String KEY_PRODUCT_SURVEY_CREATED_BY = "created_by";
    public static final String KEY_PRODUCT_SURVEY_CREATED_ON = "created_on";
    public static final String KEY_PRODUCT_SURVEY_FFMID = "ffmid";
    private final String userId;


    public static final String KEY_TABLE_RETAILER_PRIMARY_ID = "id";
    public static final String KEY_TABLE_RETAILER_MASTER_ID = "retailer_id";
    public static final String KEY_TABLE_RETAILER_NAME = "retailer_name";
    public static final String KEY_TABLE_RETAILER_TIN_NO = "retailer_tin_no";
    public static final String KEY_TABLE_RETAILER_ADDRESS = "address";
    public static final String KEY_TABLE_RETAILER_PHONE = "phone";
    public static final String KEY_TABLE_RETAILER_MOBILE = "mobile";
    public static final String KEY_TABLE_RETAILER_EMAIL_ID = "email_id";
    public static final String KEY_TABLE_RETAILER_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_TABLE_RETAILER_SAP_CODE = "distributor_sap_code";
    public static final String KEY_TABLE_RETAILER_STATUS = "status";
    public static final String KEY_TABLE_RETAILER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_RETAILER_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_RETAILER_FFMID = "ffmid";

    public static final String KEY_STOCK_RETURNS_ID = "stock_returns_id";
    public static final String KEY_COMPANY_ID = "company_id";
    public static final String KEY_DIVISION_ID = "division_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CREATED_BY = "created_by";
    public static final String KEY_CREATED_DATETIME = "created_datetime";
    public static final String KEY_UPDATED_DATETIME = "updated_datetime";


    public static final String KEY_STOCK_RETURNS_DETAILS_ID = "stock_returns_details_id";
    public static final String KEY_CROP_ID = "crop_id";
    public static final String KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID = "product_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_FFMID = "ffmid";


    private static final String CREATE_TABLE_STOCK_RETURNS = "CREATE TABLE "
            + TABLE_STOCK_RETURNS + "(" + KEY_STOCK_RETURNS_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMPANY_ID + " TEXT,"
            + KEY_DIVISION_ID + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_CREATED_DATETIME + " DATETIME,"
            + KEY_UPDATED_DATETIME + " DATETIME,"
            + KEY_FFMID + " TEXT"
            + ")";

    private static final String CREATE_TABLE_STOCK_RETURNS_DETAILS = "CREATE TABLE "
            + TABLE_STOCK_RETURNS_DETAILS + "(" + KEY_STOCK_RETURNS_DETAILS_ID + " INTEGER PRIMARY KEY,"
            + KEY_STOCK_RETURNS_ID + " TEXT,"
            + KEY_CROP_ID + " TEXT,"
            + KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID + " TEXT,"
            + KEY_QUANTITY + " TEXT,"
            + KEY_FFMID + " TEXT"
            + ")";


    // Table Create Statements
    // ROUTE ATH table create statement
    String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";

    // Table Create Statements
    // ROUTE ATH table create statement
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";
    // COMPANIES table create statement
    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE "
            + TABLE_COMPANIES + "(" + KEY_TABLE_COMPANIES_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPANIES_NAME + " TEXT,"
            + KEY_TABLE_COMPANIES_MASTER_ID + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_CODE + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_STATUS + " TEXT,"
            + KEY_TABLE_COMPANIES_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPANIES_UPDATED_DATETIME + " DATETIME" + ")";

    // COMPANY_CROP table create statement
    private static final String CREATE_TABLE_USER_COMPANY_CUSTOMER = "CREATE TABLE "
            + TABLE_USER_COMPANY_CUSTOMER + "("
            + KEY_TABLE_USER_COMPANY_CUSTOMER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " TEXT" + ")";


    // SCHEME_PRODUCTS table create statement
    private static final String CREATE_TABLE_SCHEME_PRODUCTS = "CREATE TABLE "
            + TABLE_SCHEME_PRODUCTS + "("
            + KEY_TABLE_SCHEME_PRODUCTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_CROP_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_VALID_TO + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_PRICE + " TEXT" + ")";

    // UCD table create statement
    private static final String CREATE_TABLE_USER_COMPANY_DIVISION = "CREATE TABLE "
            + TABLE_USER_COMPANY_DIVISION + "("
            + KEY_TABLE_USER_COMPANY_DIVISION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " TEXT" + ")";

    // USER COMPANY CUSTOMER table create statement
    private static final String CREATE_TABLE_COMPANY_DIVISION_CROPS = "CREATE TABLE "
            + TABLE_COMPANY_DIVISION_CROPS + "("
            + KEY_TABLE_COMPANY_DIVISION_CROPS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " TEXT,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " TEXT" + ")";

    // COMPLAINTS table create statement
    private static final String CREATE_TABLE_COMPLAINTS = "CREATE TABLE "
            + TABLE_COMPLAINT + "(" + KEY_TABLE_COMPLAINTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPLAINT_USER_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_COMPANY_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_DIVISION_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_CROP_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_FARMER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_CONTACT_NO + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES + " REAL,"
            + KEY_TABLE_COMPLAINTS_SOIL_TYPE + " TEXT,"
            + KEY_OTHERS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_PURCHASED_QTY + " REAL,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_QTY + " REAL,"
            + KEY_PURCHASE_DATE + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_BILL_NUMBER + " TEXT,"
            + KEY_TABLE_COMPLAINTS_RETAILER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DISTRIBUTOR + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_MANDAL + " TEXT,"
            + KEY_TABLE_COMPLAINTS_VILAGE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " TEXT,"
            + KEY_REGULATORY_TYPE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_DATE + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_PLACE_SAMPLING + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMMENTS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_STATUS + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_REMARKS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_FFMID + " INTEGER" + ")";

    // COMPLAINTS1 table create statement


    // CROPS table create statement
    private static final String CREATE_TABLE_CROPS = "CREATE TABLE "
            + TABLE_CROPS + "(" + KEY_TABLE_CROPS_CROP_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CROPS_CROP_MASTER_ID + " TEXT,"
            + KEY_TABLE_CROPS_CROP_NAME + " TEXT,"
            + KEY_TABLE_CROPS_CROP_CODE + " TEXT,"
            + KEY_TABLE_CROPS_CROP_SAP_ID + " TEXT,"
            + KEY_TABLE_CROPS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_CROPS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CROPS_UPDATED_DATETIME + " DATETIME" + ")";
    // CUSTOMER table create statement
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE "
            + TABLE_CUSTOMERS + "(" + KEY_TABLE_CUSTOMER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CUSTOMER_MASTER_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_NAME + " TEXT,"
            + KEY_TABLE_CUSTOMER_CODE + " TEXT,"
            + KEY_TABLE_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_TABLE_CUSTOMER_STREET + " TEXT,"
            + KEY_TABLE_CUSTOMER_CITY + " TEXT,"
            + KEY_TABLE_CUSTOMER_COUNTRY + " TEXT,"
            + KEY_TABLE_CUSTOMER_REGION_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_TELEPHONE + " TEXT,"
            + KEY_TABLE_CUSTOMER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_STATUS + " TEXT,"
            + KEY_TABLE_CUSTOMER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CUSTOMER_UPDATED_DATETIME + " DATETIME,"


            + KEY_TABLE_CUSTOMER_PASSWORD + " TEXT,"
            + KEY_TABLE_CUSTOMER_EMAIL + " TEXT,"
            + KEY_TABLE_CUSTOMER_STATE + " TEXT,"
            + KEY_TABLE_CUSTOMER_DISTRICT + " TEXT,"
            + KEY_TABLE_CUSTOMER_LAT_LNG + " TEXT" + ")";


    // serviceorder table create statement
    private static final String CREATE_TABLE_SERVICEORDER = "CREATE TABLE "
            + TABLE_SERVICEORDER + "(" + KEY_TABLE_SERVICEORDER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SERVICEORDER_MASTER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_ORDERTYPE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_ORDERDATE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_USER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DIVISION_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT + " TEXT,"
            + KEY_TABLE_SERVICEORDER_FFM_ID + " TEXT ,"
            + KEY_TABLE_SERVICEORDER_CREATED_BY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET + " INTEGER DEFAULT 0,"
            + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS + " TEXT" + ")";

    // serviceorderDETAILS table create statement
    private static final String CREATE_TABLE_SERVICEORDERDETAILS = "CREATE TABLE "
            + TABLE_SERVICEORDERDETAILS + "(" + KEY_TABLE_SERVICEORDER_DETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_FFM_ID + " INTEGER DEFAULT 0,"
            + KEY_TABLE_SERVICEORDER_SLAB_ID + " TEXT "
            + ")";

    // CUSTOMER_DETAIL table create statement
    // CUSTOMER_DETAIL table create statement
    private static final String CREATE_TABLE_CUSTOMER_DETAILS = "CREATE TABLE "
            + TABLE_CUSTOMER_DETAILS + "(" + KEY_TABLE_CUSTOMER_DETAILS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_STATUS + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME + " DATETIME, "
            + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE + " TEXT"
            + ")";

    // DISTRIBUTOR_RETAILER table create statement
    private static final String CREATE_TABLE_DISTRIBUTOR_RETAILER = "CREATE TABLE "
            + TABLE_DISTRIBUTOR_RETAILER + "("
            + KEY_TABLE_DISTRIBUTOR_RTAILER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_DISTRIBUTOR_ID + " TEXT,"
            + KEY_TABLE_RETAILER_ID + " TEXT" + ")";


    // DIVISION table create statement
    private static final String CREATE_TABLE_DIVISION = "CREATE TABLE "
            + TABLE_DIVISION + "("
            + KEY_TABLE_DIVISION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_DIVISION_MASTER_ID + " TEXT,"
            + KEY_TABLE_DIVISION_NAME + " TEXT,"
            + KEY_TABLE_DIVISION_SAP_ID + " TEXT,"
            + KEY_TABLE_DIVISION_CODE + " TEXT,"
            + KEY_TABLE_DIVISION_STATUS + " TEXT,"
            + KEY_TABLE_DIVISION_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_DIVISION_UPDATED_DATETIME + " DATETIME" + ")";

    // COMPANIES table create statement
    private static final String CREATE_TABLE_ERRORS = "CREATE TABLE "
            + TABLE_ERRORS + "(" + KEY_TABLE_ERRORS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_ERRORS_NAME + " TEXT,"
            + KEY_TABLE_ERRORS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_ERRORS_UPDATED_DATETIME + " DATETIME" + ")";

    // COMPLAINTS table create statement
    private static final String CREATE_TABLE_EVENT_MANAGMENT = "CREATE TABLE "
            + TABLE_EVENT_MANAGMENT + "(" + KEY_TABLE_EVENT_MANAGEMENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_EVENT_MANAGEMENT_TITLE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_DATE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_TIME + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_CONCER_PERSON + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_VILLAGE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_LOCATION_ADDR + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PARTICIPANTS + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_STATUS + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_EVENT_MANAGEMENT_UPDATED_DATETIME + " DATETIME" + ")";

    // FARMERS table create statement
    private static final String CREATE_TABLE_FARMERS = "CREATE TABLE "
            + TABLE_FARMERS + "(" + KEY_TABLE_FARMER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_FARMER_NAME + " TEXT,"
            + KEY_TABLE_FARMER_PHONE + " TEXT,"
            + KEY_TABLE_FARMER_STATE + " TEXT,"
            + KEY_TABLE_FARMER_DISTRICT + " TEXT,"
            + KEY_TABLE_FARMER_MANDAL + " TEXT,"
            + KEY_TABLE_FARMER_VILLAGE + " TEXT,"
            + KEY_TABLE_FARMER_TOTAL_LAND_HOLDING + " TEXT,"
            + KEY_TABLE_FARMER_CROP_ID + " TEXT,"
            + KEY_TABLE_FARMER_STATUS + " TEXT,"
            + KEY_TABLE_FARMER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_FARMER_UPDATED_DATETIME + " DATETIME" + ")";


    // CUSTOMER table create statement
    private static final String CREATE_TABLE_GEO_TRACKING = "CREATE TABLE "
            + TABLE_GEO_TRACKING + "(" + KEY_TABLE_GEO_TRACKING_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_GEO_TRACKING_MASTER_ID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_VISIT_TYPE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_USER_ID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + "  TEXT,"
            + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_DISTANCE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_STATUS + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_FFMID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME + " DATETIME, "
            + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " TEXT"
            + ")";


    // users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_TABLE_USERS_USER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USERS_MASTER_ID + " TEXT,"
            + KEY_TABLE_USERS_FIRST_NAME + " TEXT,"
            + KEY_TABLE_USERS_LAST_NAME + " TEXT,"
            + KEY_TABLE_USERS_MOBILE_NO + " TEXT,"
            + KEY_TABLE_USERS_EMAIL + " TEXT,"
            + KEY_TABLE_USERS_SAP_ID + " TEXT,"
            + KEY_TABLE_USERS_PASSWORD + " TEXT,"
            + KEY_TABLE_USERS_ROLE_ID + " TEXT,"
            + KEY_TABLE_USERS_REPORTING_MANAGER_ID + " TEXT,"
            + KEY_TABLE_USERS_STATUS + " TEXT,"
            + KEY_TABLE_USERS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_USERS_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_USERS_DESIGNATION + " TEXT,"
            + KEY_TABLE_USERS_HEADQUARTER + " TEXT,"
            + KEY_TABLE_USERS_LOCATION + " TEXT,"
            + KEY_TABLE_USERS_IMAGE + " TEXT,"
            + KEY_TABLE_USERS_REGION_ID + " TEXT, "
            + KEY_TABLE_USERS_GRADE + " TEXT, "
            + KEY_TABLE_USERS_COST_CENTER + " TEXT "

            + ")";

    // COMPANY_CROP table create statement
    private static final String CREATE_TABLE_UCDD = "CREATE TABLE "
            + TABLE_UCDD + "("
            + KEY_TABLE_UCDD_JUNCTION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_UCDD_USER_ID + " TEXT,"
            + KEY_TABLE_UCDD_COMPANY_ID + " TEXT,"
            + KEY_TABLE_UCDD_DIVISION_ID + " TEXT,"
            + KEY_TABLE_UCDD_CUSTOMER_ID + " TEXT" + ")";


    // SM table create statement
    private static final String CREATE_TABLE_SM = "CREATE TABLE "
            + TABLE_SM + "(" + KEY_TABLE_SM_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SM_MOVEMENT_TYPE + " INTEGER,"
            + KEY_TABLE_SM_USER_ID + " INTEGER,"
            + KEY_TABLE_SM_COMPANY_ID + " INTEGER,"
            + KEY_TABLE_SM_DIVISION + " INTEGER,"
            + KEY_TABLE_SM_STATUS + " INTEGER,"
            + KEY_TABLE_SM_CREATED_BY + " TEXT,"
            + KEY_TABLE_SM_UPDATED_BY + " TEXT,"
            + FFM_ID + " INTEGER,"
            + KEY_TABLE_SM_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SM_UPDATED_DATETIME + " DATETIME,"
            + KEY_CUSTOMER_ID + " TEXT"
            + ")";


    // SMD table create statement
    private static final String CREATE_TABLE_SMD = "CREATE TABLE "
            + TABLE_SMD + "(" + KEY_TABLE_SMD_DETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " INTEGER,"
            + KEY_TABLE_SMD_USER_TYPE + " INTEGER,"
            + KEY_TABLE_SMD_CUSTOMER_ID + " INTEGER,"
            + KEY_TABLE_SMD_CROP_ID + " INTEGER,"
            + KEY_TABLE_SMD_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_SMD_STOCK_PLACED + " TEXT,"
            + KEY_TABLE_SMD_CURRENT_STOCK + " TEXT,"
            + KEY_TABLE_SMD_PLACED_DATE + " TEXT,"
            + KEY_TABLE_SMD_POG + " TEXT,"
            + KEY_TABLE_CREATED_BY + " TEXT,"
            + KEY_TABLE_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SMD_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_UPDATED_DATETIME + " DATETIME,"
            + FFM_ID + " INTEGER"
            + ")";


    private static final String sqltable_stock_movement_retailer_details = "CREATE TABLE "
            + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + "(" + KEY_TABLE_SMD_RETAILER_ID_PRIMARY_KEY + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " INTEGER,"
            + KEY_TABLE_SMD_USER_TYPE + " INTEGER,"
            + KEY_TABLE_SMD_CROP_ID + " INTEGER,"
            + KEY_TABLE_SMD_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_SMD_STOCK_PLACED + " TEXT,"
            + KEY_TABLE_SMD_CURRENT_STOCK + " TEXT,"
            + KEY_TABLE_SMD_PLACED_DATE + " TEXT,"
            + KEY_TABLE_SMD_POG + " TEXT,"
            + KEY_TABLE_CREATED_BY + " TEXT,"
            + KEY_TABLE_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SMD_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_RETAILER_USER_ID + " INTEGER,"
            + KEY_TABLE_SMD_RETAILER_ID + " TEXT,"
            + KEY_TABLE_SMD_RETAILER_VERIFIED + " TEXT,"
            + KEY_TABLE_SMD_RETAILER_VERIFIED_BY + " TEXT,"
            + FFM_ID + " INTEGER"
            + ")";


    ////////////////////////////////////////////////////////////////////////////////////////
    //employ visit management create statement


    private static final String CREATE_TABLE_EMPLOYEE_VISIT_MANAGEMENT = "CREATE TABLE "
            + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "(" + KEY_EMP_VISIT_ID + " INTEGER PRIMARY KEY,"
            + KEY_EMP_VISIT_MASTER_ID + " TEXT,"
            + KEY_EMP_TYPE + " INTEGER,"
            + KEY_EMP_GEO_TRACKING_ID + " INTEGER,"
            + KEY_EMP_VISIT_USER_ID + " INTEGER,"
            + KEY_EMP_VISIT_CUSTOMER_ID + " INTEGER,"
            + KEY_EMP_VISIT_PLAN_TYPE + " INTEGER,"
            + KEY_EMP_PURPOSE_VISIT + " TEXT,"
            + KEY_EMP_PLAN_DATE_TIME + " DATETIME,"
            + KEY_EMP_CONCERN_PERSON_NAME + " TEXT,"
            + KEY_EMP_MOBILE + " INTEGER,"
            + KEY_EMP_VILLAGE + " TEXT,"
            + KEY_EMP_LOCATION_ADDRESS + " TEXT,"
            + KEY_EMP_FEILD_AREA + " DATETIME,"
            + KEY_EMP_CHECK_IN_TIME + " DATETIME,"
            + KEY_EMP_COMMENTS + " TEXT,"
            + KEY_EMP_STATUS + " INTEGER,"
            + KEY_EMP_APPROVAL_STATUS + " INTEGER,"
            + KEY_EMP_EVENT_NAME + " TEXT,"
            + KEY_EMP_EVENT_END_DATE + " TEXT,"
            + KEY_EMP_EVENT_PURPOSE + " TEXT,"
            + KEY_EMP_EVENT_VENUE + " TEXT,"
            + KEY_EMP_EVENT_PARTICIPANTS + " TEXT,"
            + KEY_EMP_CREATED_BY + " INTEGER,"
            + KEY_EMP_UPDATED_BY + " INTEGER,"
            + KEY_EMP_CREATED_DATETIME + " DATETIME,"
            + KEY_EMP_UPDATE_DATETIME + " DATETIME,"
            + KEY_EMP_FFM_ID + " INTEGER ,"
            + KEY_EMP_PURPOSE_VISIT_IDS + " TEXT ,"
            + KEY_EMP_TASK_COMPLETED_LATLNG + " TEXT "

            + ")";

    //payment_collection create statement

    private static final String CREATE_TABLE_PAYMENT_COLLECTION = "CREATE TABLE "
            + TABLE_PAYMENT_COLLECTION + "(" + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_TYPE + " TEXT,"
            + KEY_PAYMENT_COLLECTION_USER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_COMPANY_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_DIVISION_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT + " TEXT,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_MODE + " TEXT,"
            + KEY_PAYMENT_COLLECTION_BANK_NAME + " TEXT,"
            + KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO + " TEXT,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO + " TEXT,"
            + KEY_PAYMENT_COLLECTION_STATUS + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_CREATED_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_FFMID + " INTEGER" + ")";


    // plants create statement

    private static final String CREATE_TABLE_PLANTS = "CREATE TABLE "
            + TABLE_PLANTS + "(" + KEY_PLANT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PLANT_NAME + " TEXT,"
            + KEY_PLANT_SAP_CODE + " TEXT,"
            + KEY_STATE + " INTEGER,"
            + KEY_DISTRICT + " INTEGER,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_PLANTS_STATUS + " INTEGER,"
            + KEY_PLANTS_CREATED_DATETIME + " DATETIME,"
            + KEY_PLANTS_UPDATEd_DATETIME + " DATETIME," + ")";


    // region table create statement
    private static final String CREATE_TABLE_REGION = "CREATE TABLE "
            + TABLE_REGION + "(" + KEY_REGION_ID + " INTEGER PRIMARY KEY,"
            + KEY_REGION_NAME + " TEXT,"
            + KEY_REGION__MASTER_ID + " TEXT,"
            + KEY_REGION_CODE + " TEXT,"
            + KEY_REGION_STATUS + " TEXT" + ")";

    // SCHEMES table create statement

    private static final String CREATE_TABLE_SCHEMES = "CREATE TABLE "
            + TABLE_SCHEMES + "(" + KEY_SCHEMES_ID + " INTEGER PRIMARY KEY,"
            + KEY_SCHEMES_MASTER_ID + " TEXT,"
            + KEY_SCHEMES_TITLE + " TEXT,"
            + KEY_SCHEMES_SAP_CODE + " TEXT,"
            + KEY_SCHEMES_FILE_LOCATION + " TEXT,"
            + KEY_SCHEMES_STATUS + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_MASTER_ID + " TEXT,"
            + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_DESCRIPTION + " TEXT,"
            + KEY_PRODUCT_SAP_CODE + " TEXT,"
            + KEY_PRODUCT_CROP_ID + " TEXT,"
            + KEY_PRODUCTS_COMPANY_ID + " TEXT,"
            + KEY_PRODUCTS_DIVISION_ID + " TEXT,"
            + KEY_PRODUCTS_REGION + " TEXT,"
            + KEY_PRODUCTS_PACKETS_COUNT + " TEXT,"
            + KEY_PRODUCTS_PRICE + " TEXT,"
            + KEY_PRODUCT_DISCOUNT + " TEXT,"
            + KEY_PRODUCT_FROM_DATE + " DATETIME,"
            + KEY_PRODUCT_TO_DATE + " DATETIME,"
            + KEY_PRODUCTS_CATALOG_URL + " TEXT,"
            + KEY_PRODUCT_CREATED_DATETIME + " DATETIME,"
            + KEY_PRODUCT_UPDATED_DATETIME + " DATETIME,"
            + KEY_PRODUCT_STATUS + " TEXT,"
            + KEY_PRODUCT_IMAGE + " TEXT,"
            + KEY_PRODUCT_VIDEO + " TEXT,"
            + KEY_PRODUCT_BRAND_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCT_PRICE = "CREATE TABLE "
            + TABLE_PRODUCT_PRICE + "(" + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_MASTER_ID + " TEXT,"
            + KEY_PRODUCTS_PRICE + " TEXT,"
            + KEY_PRODUCT_DISCOUNT + " TEXT,"
            + KEY_PRODUCT_FROM_DATE + " DATETIME,"
            + KEY_PRODUCT_TO_DATE + " DATETIME,"
            + KEY_PRODUCT_STATUS + " TEXT ,"
            + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + " TEXT" + ")";

//feedback table create statement

    private static final String CREATE_DAILYDAIRY_TABLE = "CREATE TABLE " + TABLE_DAILYDAIRY + "("
            + KEY_DD_ID + " INTEGER PRIMARY KEY,"
            + KEY_DD_MASTER_ID + " TEXT,"
            + KEY_DD_TITLE + " TEXT,"
            + KEY_DD_USER_ID + " INTEGER,"
            + KEY_DD_COMMENTS + " TEXT,"
            + KEY_DD_TIME_SLOT + " TEXT,"
            + KEY_DD_DATE + " TEXT,"
            + KEY_DD_CREATED_DATE + " TEXT,"
            + KEY_DD_FFMID + " INTEGER,"
            + KEY_DD_TENTATIVE_TIME + " TEXT,"
            + KEY_DD_TYPE + " INTEGER,"
            + KEY_DD_STATUS + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_FEEDBACK = " CREATE TABLE "
            + TABLE_FEEDBACK + "(" + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_FEEDBACK_USER_ID + " INTEGER,"
            + KEY_TABLE_FEEDBACK_FARMER_NAME + " TEXT,"
            + KEY_TABLE_FEEDBACK_PLACE + " TEXT,"
            + KEY_TABLE_FEEDBACK_CONTACT_NO + " TEXT,"
            + KEY_TABLE_FEEDBACK_CROP + " TEXT,"
            + KEY_TABLE_FEEDBACK_HYBRID + " TEXT,"
            + KEY_TABLE_FEEDBACK_SOWING_DATE + " DATETIME,"
            + KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE + " TEXT,"
            + KEY_TABLE_FEEDBACK_IMAGE + " TEXT,"
            + KEY_TABLE_FEEDBACK_FFMID + " INTEGER" + ")";

    private static final String CREATE_TABLE_CUSTOMER_BANKDETAILS = "CREATE TABLE "
            + TABLE_CUSTOMER_BANKDETAILS + "(" + KEY_BANKDETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_BANKDETAIL_MASTER_ID + " TEXT,"
            + KEY_BANKDETAIL_CUSTOMER_ID + " TEXT,"
            + KEY_BANKDETAIL_ACCOUNT_NUMBER + " TEXT,"
            + KEY_BANKDETAIL_ACCOUNT_NAME + " TEXT,"
            + KEY_BANKDETAIL_BANK_NAME + " TEXT,"
            + KEY_BANKDETAIL_BRANCH_NAME + " TEXT,"
            + KEY_BANKDETAIL_IFSC_CODE + " TEXT,"
            + KEY_BANKDETAIL_STATUS + " TEXT,"
            + KEY_BANKDETAIL_CREATED_BY + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " DATETIME,"
            + KEY_BANKDETAIL_FFMID + " TEXT" + ")";

    private static final String CREATE_TABLE_MI_COMMODITY_PRICE = "CREATE TABLE "
            + TABLE_MI_COMMODITY_PRICE + "(" + KEY_COMMODITY_PRICE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMMODITY_PRICE_MASTER_ID + " TEXT,"
            + KEY_COMMODITY_PRICE_CROP_NAME + " TEXT,"
            + KEY_COMMODITY_PRICE_VARIETY_TYPE + " TEXT,"
            + KEY_COMMODITY_PRICE_APMC_MANDI_PRICE + " TEXT,"
            + KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE + " TEXT,"
            + KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY + " TEXT,"
            + KEY_COMMODITY_PRICE_CREATED_BY + " TEXT,"
            + KEY_COMMODITY_PRICE_CREATED_ON + " TEXT,"
            + KEY_COMMODITY_PRICE_FFMID + " TEXT" + ")";

    private static final String CREATE_TABLE_MI_CROP_SHIFTS = "CREATE TABLE "
            + TABLE_MI_CROP_SHIFTS + "(" + KEY_CROP_SHIFTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_CROP_SHIFTS_MASTER_ID + " TEXT,"
            + KEY_CROP_SHIFTS_CROP_NAME + " TEXT,"
            + KEY_CROP_SHIFTS_VARIETY_TYPE + " TEXT,"
            + KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA + " TEXT,"
            + KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA + " TEXT,"
            + KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE + " TEXT,"
            + KEY_CROP_SHIFTS_REASON_CROP_SHIFT + " TEXT,"
            + KEY_CROP_SHIFTS_CREATED_BY + " TEXT,"
            + KEY_CROP_SHIFTS_CREATED_ON + " TEXT,"
            + KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED + " TEXT,"
            + KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_CURRENT_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_NEXT_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_FFMID + " TEXT" + ")";


    private static final String CREATE_TABLE_MI_PRICE_SURVEY = "CREATE TABLE "
            + TABLE_MI_PRICE_SURVEY + "(" + KEY_PRICE_SURVEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRICE_SURVEY_MASTER_ID + " TEXT,"
            + KEY_PRICE_SURVEY_COMPANY_NAME + " TEXT,"
            + KEY_PRICE_SURVEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE_SURVEY_SKU_PACK_SIZE + " TEXT,"
            + KEY_PRICE_SURVEY_RETAIL_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_INVOICE_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_CREATED_BY + " TEXT,"
            + KEY_PRICE_SURVEY_CREATED_ON + " TEXT,"
            + KEY_PRICE_SURVEY_FFMID + " TEXT" + ")";

    private static final String CREATE_TABLE_MI_PRODUCT_SURVEY = "CREATE TABLE "
            + TABLE_MI_PRODUCT_SURVEY + "(" + KEY_PRODUCT_SURVEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_SURVEY_MASTER_ID + " TEXT,"
            + KEY_PRODUCT_SURVEY_COMPANY_NAME + " TEXT,"
            + KEY_PRODUCT_SURVEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT + " TEXT,"
            + KEY_PRODUCT_SURVEY_LAUNCH_YEAR + " TEXT,"
            + KEY_PRODUCT_SURVEY_NO_UNITS_SOLD + " TEXT,"
            + KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT + " TEXT,"
            + KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE + " TEXT,"
            + KEY_PRODUCT_SURVEY_CREATED_BY + " TEXT,"
            + KEY_PRODUCT_SURVEY_CREATED_ON + " TEXT,"
            + KEY_PRODUCT_SURVEY_FFMID + " TEXT" + ")";


    private static final String CREATE_TABLE_RETAILER = "CREATE TABLE "
            + TABLE_RETAILER + "(" + KEY_TABLE_RETAILER_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_RETAILER_MASTER_ID + " TEXT,"
            + KEY_TABLE_RETAILER_NAME + " TEXT,"
            + KEY_TABLE_RETAILER_TIN_NO + " TEXT,"
            + KEY_TABLE_RETAILER_ADDRESS + " TEXT,"
            + KEY_TABLE_RETAILER_PHONE + " TEXT,"
            + KEY_TABLE_RETAILER_MOBILE + " TEXT,"
            + KEY_TABLE_RETAILER_EMAIL_ID + " TEXT,"
            + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " TEXT,"
            + KEY_TABLE_RETAILER_SAP_CODE + " TEXT,"
            + KEY_TABLE_RETAILER_STATUS + " TEXT,"
            + KEY_TABLE_RETAILER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_RETAILER_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_RETAILER_FFMID + " TEXT" + ")";

    private static final String CREATE_TABLE_GRADE = "CREATE TABLE "
            + TABLE_GRADE + "(" + GRADE_ID + " INTEGER ,"
            + GRADE_NAME + " TEXT,"
            + PRICE_PER_KM + " TEXT" + ")";

    private static final String sql_version_control = "CREATE TABLE "
            + TABLE_VERSION_CONTROL + "(" + KEY_ID + " INTEGER ,"
            + VERSION_TABLE_NAME + " TEXT,"
            + UPDATED_DATE + " TEXT,"
            + VERSION_CODE + " TEXT" + ")";


    private SQLiteDatabase dbsql;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

     /*   super(context, Environment.getExternalStorageDirectory()
                + File.separator + "NSL.db"
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);

    */
        this.context = context;

        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID, null);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        Common.Log.i("onUpgrade..");
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_TABLE_COMPANIES);
        db.execSQL(CREATE_TABLE_COMPANY_DIVISION_CROPS);
        db.execSQL(CREATE_TABLE_USER_COMPANY_CUSTOMER);
        db.execSQL(CREATE_TABLE_USER_COMPANY_DIVISION);
        db.execSQL(CREATE_TABLE_COMPLAINTS);
        db.execSQL(CREATE_TABLE_SCHEME_PRODUCTS);

        db.execSQL(CREATE_TABLE_CROPS);
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(CREATE_TABLE_CUSTOMER_DETAILS);
        db.execSQL(CREATE_TABLE_DISTRIBUTOR_RETAILER);
        db.execSQL(CREATE_TABLE_DIVISION);
        db.execSQL(CREATE_TABLE_ERRORS);
        db.execSQL(CREATE_TABLE_EVENT_MANAGMENT);
        db.execSQL(CREATE_TABLE_FARMERS);
        db.execSQL(CREATE_TABLE_GEO_TRACKING);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_UCDD);
        db.execSQL(CREATE_TABLE_SM);
        db.execSQL(CREATE_TABLE_SMD);
        db.execSQL(sqltable_stock_movement_retailer_details);
        db.execSQL(CREATE_TABLE_SERVICEORDER);
        db.execSQL(CREATE_TABLE_SERVICEORDERDETAILS);
        db.execSQL(CREATE_TABLE_PAYMENT_COLLECTION);
        db.execSQL(CREATE_TABLE_EMPLOYEE_VISIT_MANAGEMENT);
        db.execSQL(CREATE_TABLE_REGION);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRODUCT_PRICE);
        db.execSQL(CREATE_TABLE_SCHEMES);
        db.execSQL(CREATE_TABLE_FEEDBACK);
        db.execSQL(CREATE_DAILYDAIRY_TABLE);
        db.execSQL(CREATE_TABLE_CUSTOMER_BANKDETAILS);
        db.execSQL(CREATE_TABLE_MI_COMMODITY_PRICE);
        db.execSQL(CREATE_TABLE_MI_CROP_SHIFTS);
        db.execSQL(CREATE_TABLE_MI_PRICE_SURVEY);
        db.execSQL(CREATE_TABLE_MI_PRODUCT_SURVEY);
        db.execSQL(CREATE_TABLE_RETAILER);
        db.execSQL(CREATE_TABLE_GRADE);
        db.execSQL(sql_version_control);
        db.execSQL(CREATE_TABLE_STOCK_RETURNS);
        db.execSQL(CREATE_TABLE_STOCK_RETURNS_DETAILS);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Common.Log.i("onUpgrade" + "oldVersion :" + oldVersion + "\n newVersion: " + newVersion);
if (Common.haveInternet(context)){
    new Async_Logout().execute();
}
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_DIVISION_CROPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_COMPANY_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_COMPANY_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTOR_RETAILER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ERRORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_MANAGMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEO_TRACKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UCDD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_VISIT_MANAGEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICEORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICEORDERDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILYDAIRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_BANKDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_COMMODITY_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_CROP_SHIFTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_PRICE_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_PRODUCT_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION_CONTROL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_RETURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_RETURNS_DETAILS);

        // Create tables again
        onCreate(db);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Divisions", "false");
        editor.putString("userId", "");
        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
        editor.putString("fcm_id", "");
        editor.commit();

        // context.deleteDatabase(DATABASE_NAME);

        Intent login = new Intent(context, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(login);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new order
    void addOrders(Orders orders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_NAME, orders.getName()); // Contact Name
        values.put(KEY_ORDER_AMT, orders.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_ORDERS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new division
    public void addDivisions(Divisions divisions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_division = new ContentValues();
        values_division.put(KEY_TABLE_DIVISION_MASTER_ID, divisions.getDivMasterId());
        values_division.put(KEY_TABLE_DIVISION_NAME, divisions.getDivName());
        values_division.put(KEY_TABLE_DIVISION_CODE, divisions.getDivcode());
        values_division.put(KEY_TABLE_DIVISION_SAP_ID, divisions.getDivsapid());
        values_division.put(KEY_TABLE_DIVISION_STATUS, divisions.getDivstatus());
        values_division.put(KEY_TABLE_DIVISION_CREATED_DATETIME, divisions.getDivcdatetime());
        values_division.put(KEY_TABLE_DIVISION_UPDATED_DATETIME, divisions.getDivudatetime());
        // Inserting Row
        db.insert(TABLE_DIVISION, null, values_division);
        db.close(); // Closing database connection
    }


    // Adding new company
    public void addCompanies(Companies companies) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_companies = new ContentValues();
        values_companies.put(KEY_TABLE_COMPANIES_MASTER_ID, companies.getCompanyMasterId());
        values_companies.put(KEY_TABLE_COMPANIES_NAME, companies.getCompanyName());
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_CODE, companies.getCompanycode()); // Contact Div_code
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_SAP_ID, companies.getCompanysapid()); // Contact DivName
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_STATUS, companies.getCompanystatus()); // Contact Div_code
        values_companies.put(KEY_TABLE_COMPANIES_CREATED_DATETIME, companies.getCompanycdatetime()); // Contact DivName
        values_companies.put(KEY_TABLE_COMPANIES_UPDATED_DATETIME, companies.getCompanyudatetime()); // Contact Div_code

        // Inserting Row
        db.insert(TABLE_COMPANIES, null, values_companies);
        db.close(); // Closing database connection
    }


    // Adding new company
    void addEVM(Employe_visit_management_pojo employe_visit_management_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_evm = new ContentValues();
        values_evm.put(KEY_EMP_VISIT_MASTER_ID, employe_visit_management_pojo.getEmp_visitMasterId());
        values_evm.put(KEY_EMP_TYPE, employe_visit_management_pojo.getEmpvisitstype());
        values_evm.put(KEY_EMP_GEO_TRACKING_ID, employe_visit_management_pojo.getEmpvisitgeotrackingid());
        values_evm.put(KEY_EMP_VISIT_USER_ID, employe_visit_management_pojo.getEmp_visit_userid());
        values_evm.put(KEY_EMP_VISIT_CUSTOMER_ID, employe_visit_management_pojo.getEmp_visit_customerid()); // Contact Div_code
        values_evm.put(KEY_EMP_VISIT_PLAN_TYPE, employe_visit_management_pojo.getEmp_visit_plantype()); // Contact DivName
        values_evm.put(KEY_EMP_PURPOSE_VISIT, employe_visit_management_pojo.get_emp_visit_purposevisit()); // Contact Div_code
        values_evm.put(KEY_EMP_PURPOSE_VISIT_IDS, employe_visit_management_pojo.get_emp_visit_purposevisit_ids()); // Contact Div_code
        values_evm.put(KEY_EMP_PLAN_DATE_TIME, employe_visit_management_pojo.getEmp_visit_plandatetime()); // Contact DivName
        values_evm.put(KEY_EMP_CONCERN_PERSON_NAME, employe_visit_management_pojo.getEmp_visit_concernpersonname()); // Contact Div_code
        values_evm.put(KEY_EMP_MOBILE, employe_visit_management_pojo.getEmp_visit_mobile()); // Contact Div_code
        values_evm.put(KEY_EMP_VILLAGE, employe_visit_management_pojo.getEmp_visit_village());
        values_evm.put(KEY_EMP_LOCATION_ADDRESS, employe_visit_management_pojo.getEmp_visit_locationaddress());
        values_evm.put(KEY_EMP_FEILD_AREA, employe_visit_management_pojo.getEmp_visit_fieldarea());
        values_evm.put(KEY_EMP_CHECK_IN_TIME, employe_visit_management_pojo.getEmp_visit_checkintime());
        values_evm.put(KEY_EMP_COMMENTS, employe_visit_management_pojo.getEmp_visit_comments());
        values_evm.put(KEY_EMP_STATUS, employe_visit_management_pojo.getEmpvisitstatus());
        values_evm.put(KEY_EMP_APPROVAL_STATUS, employe_visit_management_pojo.getEmpvisitapproval_status());
        values_evm.put(KEY_EMP_EVENT_NAME, employe_visit_management_pojo.getEmpvisitevent_name());
        values_evm.put(KEY_EMP_EVENT_END_DATE, employe_visit_management_pojo.getEmpvisitend_date());
        values_evm.put(KEY_EMP_EVENT_PURPOSE, employe_visit_management_pojo.getEmpvisitevent_purpose());
        values_evm.put(KEY_EMP_EVENT_VENUE, employe_visit_management_pojo.getEmpvisitevent_venue());
        values_evm.put(KEY_EMP_EVENT_PARTICIPANTS, employe_visit_management_pojo.getEmpvisitevent_participants());
        values_evm.put(KEY_EMP_CREATED_BY, employe_visit_management_pojo.getEmp_visit_createdby());
        values_evm.put(KEY_EMP_UPDATED_BY, employe_visit_management_pojo.getEmp_visit_updatedby());
        values_evm.put(KEY_EMP_CREATED_DATETIME, employe_visit_management_pojo.getEmpvisitcdatetime());
        values_evm.put(KEY_EMP_UPDATE_DATETIME, employe_visit_management_pojo.getEmpvisitudatetime());
        values_evm.put(KEY_EMP_FFM_ID, employe_visit_management_pojo.getEmpvisitsffmid());

        // Inserting Row
        db.insert(TABLE_EMPLOYEE_VISIT_MANAGEMENT, null, values_evm);
        db.close(); // Closing database connection
    }


    //public void updateFFMId


    // Adding new REGION
    public void addRegions(Regions regions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_regions = new ContentValues();
        values_regions.put(KEY_REGION_NAME, regions.getRegionName());
        values_regions.put(KEY_REGION__MASTER_ID, regions.getRegionMasterId());
        values_regions.put(KEY_REGION_CODE, regions.getRegioncode());
        values_regions.put(KEY_REGION_STATUS, regions.getRegionstatus());


        // Inserting Row
        db.insert(TABLE_REGION, null, values_regions);
        db.close(); // Closing database connection
    }


    // Adding new crop
    public void addCrops(Crops crops) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_crops = new ContentValues();
        values_crops.put(KEY_TABLE_CROPS_CROP_MASTER_ID, crops.getCropMasterId());
        values_crops.put(KEY_TABLE_CROPS_CROP_NAME, crops.getCropName());
        values_crops.put(KEY_TABLE_CROPS_CROP_CODE, crops.getCropcode());
        values_crops.put(KEY_TABLE_CROPS_CROP_SAP_ID, crops.getCropsapid());
        values_crops.put(KEY_TABLE_CROPS_DIVISION_ID, crops.getCropdivisionId());
        values_crops.put(KEY_TABLE_CROPS_CREATED_DATETIME, crops.getCropcdatetime());
        values_crops.put(KEY_TABLE_CROPS_UPDATED_DATETIME, crops.getCropudatetime());

        // Inserting Row
        db.insert(TABLE_CROPS, null, values_crops);
        db.close(); // Closing database connection
    }

    // Adding new user
    public void addusers(Users users) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_users = new ContentValues();
        values_users.put(KEY_TABLE_USERS_MASTER_ID, users.getUserMasterId());
        values_users.put(KEY_TABLE_USERS_FIRST_NAME, users.getUser_first_name());
        values_users.put(KEY_TABLE_USERS_LAST_NAME, users.getUser_last_name());
        values_users.put(KEY_TABLE_USERS_MOBILE_NO, users.getUser_mobile_no());
        values_users.put(KEY_TABLE_USERS_EMAIL, users.getUser_email());
        values_users.put(KEY_TABLE_USERS_SAP_ID, users.getUser_sap_id());
        values_users.put(KEY_TABLE_USERS_PASSWORD, users.getUser_password());
        values_users.put(KEY_TABLE_USERS_ROLE_ID, users.getUser_role_id());
        values_users.put(KEY_TABLE_USERS_REPORTING_MANAGER_ID, users.getUser_reporting_manager_id());
        values_users.put(KEY_TABLE_USERS_STATUS, users.getUserstatus());
        values_users.put(KEY_TABLE_USERS_CREATED_DATETIME, users.getUsercdatetime());
        values_users.put(KEY_TABLE_USERS_UPDATED_DATETIME, users.getUserudatetime());
        values_users.put(KEY_TABLE_USERS_DESIGNATION, users.getUserdesignation());
        values_users.put(KEY_TABLE_USERS_HEADQUARTER, users.getUserheadquarter());
        values_users.put(KEY_TABLE_USERS_LOCATION, users.getUserlocation());
        values_users.put(KEY_TABLE_USERS_REGION_ID, users.getUserregionId());
        values_users.put(KEY_TABLE_USERS_GRADE, users.getGrade());
        values_users.put(KEY_TABLE_USERS_COST_CENTER, users.getCost_center());
        values_users.put(KEY_TABLE_USERS_IMAGE, users.getImage());

        // Inserting Row
        db.insert(TABLE_USERS, null, values_users);
        // db.close(); // Closing database connection
    }

    // Updating new user
    void updateusers(String userId, String imageData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_users = new ContentValues();
        values_users.put(KEY_TABLE_USERS_IMAGE, imageData);

        // Inserting Row
        db.update(TABLE_USERS, values_users, KEY_TABLE_USERS_MASTER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close(); // Closing database connection
    }

    // Adding new COMPANY DIVISION crop
    void addCompany_division_crops(Company_division_crops company_division_crops) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_company_division_crops = new ContentValues();

        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID, company_division_crops.getCdcCompanyId());
        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID, company_division_crops.getCdcDivisionId());
        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID, company_division_crops.getCdcCropId());

        // Inserting Row
        db.insert(TABLE_COMPANY_DIVISION_CROPS, null, values_company_division_crops);
        db.close(); // Closing database connection
    }

    // Adding new USER COMPANY CUSTOMER
    void addUser_Company_Customer(User_Company_Customer user_company_customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_customer = new ContentValues();

        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID, user_company_customer.getuccUserId());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID, user_company_customer.getuccCompanyId());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID, user_company_customer.getuccCustomerId());

        // Inserting Row
        db.insert(TABLE_USER_COMPANY_CUSTOMER, null, values_user_company_customer);
        db.close(); // Closing database connection
    }

    // Adding new Scheme Products
    public void addScheme_Products(Scheme_Products scheme_products) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_customer = new ContentValues();
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID, scheme_products.getspscheme_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID, scheme_products.getspProductId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_PRICE, scheme_products.getspproductprice());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_REGION_ID, scheme_products.getspRegionId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID, scheme_products.getspCompanyId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_CROP_ID, scheme_products.get_sp_crop_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID, scheme_products.get_sp_slab_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE, scheme_products.get_sp_booking_incentive());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM, scheme_products.get_sp_valid_from());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_VALID_TO, scheme_products.get_sp_valid_to());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR, scheme_products.get_sp_booking_year());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE, scheme_products.get_sp_season_code());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE, scheme_products.get_sp_extenstion_date());
        // Inserting Row
        db.insert(TABLE_SCHEME_PRODUCTS, null, values_user_company_customer);
        db.close(); // Closing database connection
    }

    // Adding new USER COMPANY Division
    void addUser_Company_Division(User_Company_Division user_company_division) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_division = new ContentValues();

        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_USER_ID, user_company_division.getucdUserId());
        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID, user_company_division.getucdCompanyId());
        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID, user_company_division.getucdDivisionId());

        // Inserting Row
        db.insert(TABLE_USER_COMPANY_DIVISION, null, values_user_company_division);
        db.close(); // Closing database connection
    }

    // Adding new product
    public void addProducts(Products_Pojo products_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_products = new ContentValues();
        values_products.put(KEY_PRODUCT_MASTER_ID, products_pojo.getProductMasterId());
        values_products.put(KEY_PRODUCT_NAME, products_pojo.getProductName());
        values_products.put(KEY_PRODUCT_DESCRIPTION, products_pojo.getProductDescription());
        values_products.put(KEY_PRODUCT_SAP_CODE, products_pojo.getProductSapCode());
        values_products.put(KEY_PRODUCT_CROP_ID, products_pojo.getProductcropid());
        values_products.put(KEY_PRODUCTS_COMPANY_ID, products_pojo.getProductcompanyid());
        values_products.put(KEY_PRODUCTS_DIVISION_ID, products_pojo.getProductdivisionid());
        values_products.put(KEY_PRODUCTS_REGION, products_pojo.getProductregeion());
        values_products.put(KEY_PRODUCTS_PRICE, products_pojo.getProductprice());
        values_products.put(KEY_PRODUCTS_PACKETS_COUNT, products_pojo.get_product_no_packets());
        values_products.put(KEY_PRODUCT_DISCOUNT, products_pojo.getProductdiscount());
        values_products.put(KEY_PRODUCT_FROM_DATE, products_pojo.getProductfromdate());
        values_products.put(KEY_PRODUCT_TO_DATE, products_pojo.getProducttodate());
        values_products.put(KEY_PRODUCT_STATUS, products_pojo.getProductstatus());
        values_products.put(KEY_PRODUCT_CREATED_DATETIME, products_pojo.getProductcdatetime());
        values_products.put(KEY_PRODUCT_UPDATED_DATETIME, products_pojo.getProductudatetime());
        values_products.put(KEY_PRODUCT_IMAGE, products_pojo.getProductImages());
        values_products.put(KEY_PRODUCT_VIDEO, products_pojo.getProductVideos());
        values_products.put(KEY_PRODUCTS_CATALOG_URL, products_pojo.get_product_catalog_url());
        values_products.put(KEY_PRODUCT_BRAND_NAME, products_pojo.getProduct_brand_name());

        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values_products);
        db.close(); // Closing database connection
    }


    // Adding new product
    public void addProductPrice(Products_Pojo products_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_products = new ContentValues();
        values_products.put(KEY_PRODUCT_MASTER_ID, products_pojo.getProductMasterId());
        values_products.put(KEY_PRODUCTS_PRICE, products_pojo.getProductprice());
        values_products.put(KEY_PRODUCT_DISCOUNT, products_pojo.getProductdiscount());
        values_products.put(KEY_PRODUCT_FROM_DATE, products_pojo.getProductfromdate());
        values_products.put(KEY_PRODUCT_TO_DATE, products_pojo.getProducttodate());
        values_products.put(KEY_PRODUCT_STATUS, products_pojo.getProductstatus());
        values_products.put(KEY_TABLE_SCHEME_PRODUCTS_REGION_ID, products_pojo.getProductregeion());

        // Inserting Row
        db.insert(TABLE_PRODUCT_PRICE, null, values_products);
        db.close(); // Closing database connection
    }

    // Adding new customers
    public void addCustomers(Customers customers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_TABLE_CUSTOMER_MASTER_ID, customers.getCusMasterId());
        values_customers.put(KEY_TABLE_CUSTOMER_NAME, customers.getCusName());
        values_customers.put(KEY_TABLE_CUSTOMER_CODE, customers.getCuscode());
        values_customers.put(KEY_TABLE_CUSTOMER_ADDRESS, customers.getCusaddress());
        values_customers.put(KEY_TABLE_CUSTOMER_STREET, customers.getCusstreet());
        values_customers.put(KEY_TABLE_CUSTOMER_CITY, customers.getCus_city());
        values_customers.put(KEY_TABLE_CUSTOMER_COMPANY_ID, customers.getCuscompany_Id());
        values_customers.put(KEY_TABLE_CUSTOMER_REGION_ID, customers.getCusregion_Id());
        values_customers.put(KEY_TABLE_CUSTOMER_TELEPHONE, customers.getCustelephone());
        values_customers.put(KEY_TABLE_CUSTOMER_COUNTRY, customers.getCuscountry());
        values_customers.put(KEY_TABLE_CUSTOMER_STATUS, customers.getCusstatus());
        values_customers.put(KEY_TABLE_CUSTOMER_CREATED_DATETIME, customers.getCuscdatetime());
        values_customers.put(KEY_TABLE_CUSTOMER_UPDATED_DATETIME, customers.getCusudatetime());
        values_customers.put(KEY_TABLE_CUSTOMER_PASSWORD, customers.getCuspassword());
        values_customers.put(KEY_TABLE_CUSTOMER_EMAIL, customers.getCusEmail());
        values_customers.put(KEY_TABLE_CUSTOMER_STATE, customers.getCusState());
        values_customers.put(KEY_TABLE_CUSTOMER_DISTRICT, customers.getCusDistrict());
        values_customers.put(KEY_TABLE_CUSTOMER_LAT_LNG, customers.getCusLatlng());
        // Inserting Row
        db.insert(TABLE_CUSTOMERS, null, values_customers);
        db.close(); // Closing database connection
    }

    public void addCustomer_details(Customer_Details customer_details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues customerdetails = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID, customer_details.get_customer_details_master_id());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID, customer_details.get_customer_details_division_id());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT, customer_details.get_customer_details_credit_limit());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET, customer_details.get_customer_details_inside_bucket());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET, customer_details.get_customer_details_outside_bucket());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_STATUS, customer_details.get_customer_details_status());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME, customer_details.get_customer_details_created_datetime());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME, customer_details.get_customer_details_updated_datetime());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE, customer_details.get_customer_details_credit_balance());
        // Inserting Row
        db.insert(TABLE_CUSTOMER_DETAILS, null, customerdetails);
//    Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }

    // Adding new Scheme
    public void addSchemes(Schemes schemes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_schemes = new ContentValues();
        values_schemes.put(KEY_SCHEMES_MASTER_ID, schemes.getschemeMasterId());
        values_schemes.put(KEY_SCHEMES_TITLE, schemes.getschemeName());
        values_schemes.put(KEY_SCHEMES_SAP_CODE, schemes.getscheme_sap_code());
        values_schemes.put(KEY_SCHEMES_FILE_LOCATION, schemes.getscheme_file_location());
        values_schemes.put(KEY_SCHEMES_STATUS, schemes.getscheme_status());
        // Inserting Row
        db.insert(TABLE_SCHEMES, null, values_schemes);
        db.close(); // Closing database connection
    }

    // Adding new geo_tracking
    void addGeotracking(Geo_Tracking_POJO geo_tracking_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_geotracking = new ContentValues();
        //values_geotracking.put(KEY_TABLE_GEO_TRACKING_ID, geo_tracking_pojo.getID());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_MASTER_ID, geo_tracking_pojo.getGeoMasterId());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_VISIT_TYPE, geo_tracking_pojo.getGeoVisitType());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_USER_ID, geo_tracking_pojo.get_Geo_user_id());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG, geo_tracking_pojo.get_Geo_check_in_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG, geo_tracking_pojo.getGeo_check_out_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG, geo_tracking_pojo.getGeo_route_path_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_DISTANCE, geo_tracking_pojo.getGeo_distance());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_VISIT_DATE, geo_tracking_pojo.getGeo_visit_date());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME, geo_tracking_pojo.getGeo_check_in_time());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME, geo_tracking_pojo.getGeo_check_out_time());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_STATUS, geo_tracking_pojo.getGeostatus());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_FFMID, geo_tracking_pojo.getGeoffmid());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CREATED_DATETIME, geo_tracking_pojo.getGeocdatetime());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME, geo_tracking_pojo.getGeoudatetime());

        // Inserting Row
        db.insert(TABLE_GEO_TRACKING, null, values_geotracking);
//		Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }

    // Adding Serviceorder
    public void addServiceorder(ServiceOrderMaster serviceOrderMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_serviceorder = new ContentValues();
        // values_serviceorder.put(KEY_TABLE_SERVICEORDER_MASTER_ID,         serviceOrderMaster.getserviceorder_masterid());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_ORDERTYPE, serviceOrderMaster.getserviceorder_type());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_ORDERDATE, serviceOrderMaster.get_serviceorder_date()); // Contact Div_code
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_USER_ID, serviceOrderMaster.get_serviceorder_user_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CUSTOMER_ID, serviceOrderMaster.getserviceorder_customer_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_DIVISION_ID, serviceOrderMaster.getserviceorder_division_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_COMPANY_ID, serviceOrderMaster.getserviceorder_company_id()); // Contact DivName
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_STATUS, serviceOrderMaster.getserviceorderstatus()); // Contact Div_code
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CREATED_DATETIME, serviceOrderMaster.getserviceordercdatetime()); // Contact DivName
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_UPDATED_DATETIME, serviceOrderMaster.getserviceorderudatetime()); // Contact Div_code
        if (serviceOrderMaster.getserviceorderffm_id() != "" || serviceOrderMaster.getserviceorderffm_id() != null) {
            values_serviceorder.put(KEY_TABLE_SERVICEORDER_FFM_ID, serviceOrderMaster.getserviceorderffm_id());
        }
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT, serviceOrderMaster.get_token_amount());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CREATED_BY, serviceOrderMaster.get_created_by());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_APPROVAL_STATUS, serviceOrderMaster.get_approval_status());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS, serviceOrderMaster.get_approval_comments());

        // Inserting Row
        db.insert(TABLE_SERVICEORDER, null, values_serviceorder);
        db.close(); // Closing database connection
    }


    // Adding new customers
    void addCustomersBankDetails(Customer_Bank_Details customerBankDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_BANKDETAIL_MASTER_ID, customerBankDetails.get_cus_bak_dtls_masterid());
        values_customers.put(KEY_BANKDETAIL_CUSTOMER_ID, customerBankDetails.get_customer_id());
        values_customers.put(KEY_BANKDETAIL_ACCOUNT_NUMBER, customerBankDetails.get_account_no());
        values_customers.put(KEY_BANKDETAIL_ACCOUNT_NAME, customerBankDetails.get_account_name());
        values_customers.put(KEY_BANKDETAIL_BANK_NAME, customerBankDetails.get_bank_name());
        values_customers.put(KEY_BANKDETAIL_BRANCH_NAME, customerBankDetails.get_branch_name());
        values_customers.put(KEY_BANKDETAIL_IFSC_CODE, customerBankDetails.get_ifsc_code());
        values_customers.put(KEY_BANKDETAIL_STATUS, customerBankDetails.get_status());
        values_customers.put(KEY_BANKDETAIL_CREATED_BY, customerBankDetails.get_created_by());
        values_customers.put(KEY_UPDATED_BY, customerBankDetails.get_updated_by());
        values_customers.put(KEY_CREATED_DATE, customerBankDetails.get_created_date());
        values_customers.put(KEY_BANKDETAIL_FFMID, customerBankDetails.get_ffmid());

        // Inserting Row
        db.insert(TABLE_CUSTOMER_BANKDETAILS, null, values_customers);
        db.close(); // Closing database connection
    }

    // Adding new customers
    public void addCommodityPrice(Commodity_Price commodity_price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();

        values_customers.put(KEY_COMMODITY_PRICE_MASTER_ID, commodity_price.get_commodity_price_master_id());
        values_customers.put(KEY_COMMODITY_PRICE_CROP_NAME, commodity_price.get_commodity_price_crop_name());
        values_customers.put(KEY_COMMODITY_PRICE_VARIETY_TYPE, commodity_price.get_commodity_price_variety_type());
        values_customers.put(KEY_COMMODITY_PRICE_APMC_MANDI_PRICE, commodity_price.get_commodity_price_apmc_mandi_price());
        values_customers.put(KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE, commodity_price.get_commodity_price_commodity_dealer_agent_price());
        values_customers.put(KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY, commodity_price.get_commodity_price_purchage_price_by_industry());
        values_customers.put(KEY_COMMODITY_PRICE_CREATED_BY, commodity_price.get_commodity_price_created_by());
        values_customers.put(KEY_COMMODITY_PRICE_CREATED_ON, commodity_price.get_commodity_price_created_on());
        values_customers.put(KEY_COMMODITY_PRICE_FFMID, commodity_price.get_commodity_price_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_COMMODITY_PRICE, null, values_customers);
        db.close(); // Closing database connection
    }

    // Adding new crop shifta
    public void addCropShifts(Crop_Shifts crop_shifts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_CROP_SHIFTS_MASTER_ID, crop_shifts.get_crop_shifts_master_id());
        values_customers.put(KEY_CROP_SHIFTS_CROP_NAME, crop_shifts.get_crop_shifts_crop_name());
        values_customers.put(KEY_CROP_SHIFTS_VARIETY_TYPE, crop_shifts.get_crop_shifts_variety_type());
        values_customers.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA, crop_shifts.get_crop_shifts_previous_year_area());
        values_customers.put(KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA, crop_shifts.get_crop_shifts_current_year_expected_area());
        values_customers.put(KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE, crop_shifts.get_crop_shifts_percentage_increase_decrease());
        values_customers.put(KEY_CROP_SHIFTS_REASON_CROP_SHIFT, crop_shifts.get_crop_shifts_reason_crop_shift());
        values_customers.put(KEY_CROP_SHIFTS_CREATED_BY, crop_shifts.get_crop_shifts_created_by());
        values_customers.put(KEY_CROP_SHIFTS_CREATED_ON, crop_shifts.get_crop_shifts_created_on());
        values_customers.put(KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED, crop_shifts.get_crop_shifs_crop_in_saved_seed());
        values_customers.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_previous_year());
        values_customers.put(KEY_CROP_SHIFTS_CURRENT_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_current_year());
        values_customers.put(KEY_CROP_SHIFTS_NEXT_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_next_year());
        values_customers.put(KEY_CROP_SHIFTS_FFMID, crop_shifts.get_crop_shifts_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_CROP_SHIFTS, null, values_customers);
        db.close(); // Closing database connection
    }

    public void addPricingServey(Price_Survey price_survey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues pricing_servey = new ContentValues();

        pricing_servey.put(KEY_PRICE_SURVEY_MASTER_ID, price_survey.get_price_survey_master_id());
        pricing_servey.put(KEY_PRICE_SURVEY_COMPANY_NAME, price_survey.get_price_survey_company_name());
        pricing_servey.put(KEY_PRICE_SURVEY_PRODUCT_NAME, price_survey.get_price_survey_product_name());
        pricing_servey.put(KEY_PRICE_SURVEY_SKU_PACK_SIZE, price_survey.get_price_survey_sku_pack_size());
        pricing_servey.put(KEY_PRICE_SURVEY_RETAIL_PRICE, price_survey.get_price_survey_retail_price());
        pricing_servey.put(KEY_PRICE_SURVEY_INVOICE_PRICE, price_survey.get_price_survey_invoice_price());
        pricing_servey.put(KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE, price_survey.get_price_survey_net_distributor_landing_price());
        pricing_servey.put(KEY_PRICE_SURVEY_CREATED_BY, price_survey.get_price_survey_created_by());
        pricing_servey.put(KEY_PRICE_SURVEY_CREATED_ON, price_survey.get_price_survey_created_on());
        pricing_servey.put(KEY_PRICE_SURVEY_FFMID, price_survey.get_price_survey_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_PRICE_SURVEY, null, pricing_servey);
        db.close(); // Closing database connection
    }

    public void addProductServey(Product_Survey product_survey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues product_servey = new ContentValues();

        product_servey.put(KEY_PRODUCT_SURVEY_MASTER_ID, product_survey.get_product_survey_master_id());
        product_servey.put(KEY_PRODUCT_SURVEY_COMPANY_NAME, product_survey.get_product_survey_company_name());
        product_servey.put(KEY_PRODUCT_SURVEY_PRODUCT_NAME, product_survey.get_product_survey_product_name());
        product_servey.put(KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT, product_survey.get_product_survey_name_of_the_check_segment());
        product_servey.put(KEY_PRODUCT_SURVEY_LAUNCH_YEAR, product_survey.get_product_survey_launch_year());
        product_servey.put(KEY_PRODUCT_SURVEY_NO_UNITS_SOLD, product_survey.get_product_survey_no_units_sold());
        product_servey.put(KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT, product_survey.get_product_survey_area_crop_sown_new_product());
        product_servey.put(KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE, product_survey.get_product_survey_remarks_unique_feature());
        product_servey.put(KEY_PRODUCT_SURVEY_CREATED_BY, product_survey.get_product_survey_created_by());
        product_servey.put(KEY_PRODUCT_SURVEY_CREATED_ON, product_survey.get_product_survey_created_on());
        product_servey.put(KEY_PRODUCT_SURVEY_FFMID, product_survey.get_product_survey_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_PRODUCT_SURVEY, null, product_servey);
        db.close(); // Closing database connection
    }

    // get ServiceorderDetails advance booking
    public List<ServiceOrderMaster> getOfflineAdvanceBookingData() {

        List<ServiceOrderMaster> serviceOrderMasterList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + ","
                + KEY_TABLE_SERVICEORDER_ORDERDATE + ","
                + KEY_TABLE_SERVICEORDER_USER_ID + ","
                + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + ","
                + KEY_TABLE_SERVICEORDER_DIVISION_ID + ","
                + KEY_TABLE_SERVICEORDER_COMPANY_ID + ","
                + KEY_TABLE_SERVICEORDER_STATUS + ","
                + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_FFM_ID + ","
                + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT


                + " FROM " + TABLE_SERVICEORDER + " WHERE (" + KEY_TABLE_SERVICEORDER_FFM_ID + " = 0 OR " + KEY_TABLE_SERVICEORDER_FFM_ID + " IS NULL) AND " + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2";
        Log.d("selectQuery1", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                serviceOrderMasterData.setserviceorder_masterid(cursor.getString(0));
                serviceOrderMasterData.setserviceorder_type(cursor.getString(1));
                serviceOrderMasterData.set_serviceorder_date(cursor.getString(2));
                serviceOrderMasterData.set_serviceorder_user_id(cursor.getString(3));
                serviceOrderMasterData.setserviceorder_customer_id(cursor.getString(4));
                serviceOrderMasterData.setserviceorder_division_id(cursor.getString(5));
                serviceOrderMasterData.setserviceorder_company_id(cursor.getString(6));
                serviceOrderMasterData.setserviceorderstatus(cursor.getString(7));
                serviceOrderMasterData.setserviceordercdatetime(cursor.getString(8));
                serviceOrderMasterData.setserviceorderffm_id(cursor.getString(10));
                serviceOrderMasterData.set_token_amount(cursor.getString(11));

                serviceOrderMasterData.setServiceOrderDetailMasterList(getServiceorderDetails(serviceOrderMasterData));


                // Adding contact to list
                serviceOrderMasterList.add(serviceOrderMasterData);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // get ServiceorderDetails
    public List<ServiceOrderMaster> getOfflineServiceorders() {

        List<ServiceOrderMaster> serviceOrderMasterList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + ","
                + KEY_TABLE_SERVICEORDER_ORDERDATE + ","
                + KEY_TABLE_SERVICEORDER_USER_ID + ","
                + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + ","
                + KEY_TABLE_SERVICEORDER_DIVISION_ID + ","
                + KEY_TABLE_SERVICEORDER_COMPANY_ID + ","
                + KEY_TABLE_SERVICEORDER_STATUS + ","
                + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_FFM_ID + ","
                + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT

                + " FROM " + TABLE_SERVICEORDER + " WHERE (" + KEY_TABLE_SERVICEORDER_FFM_ID + " = 0 OR " + KEY_TABLE_SERVICEORDER_FFM_ID + " IS NULL) AND " + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1";
        Log.d("selectQuery1 OrderIn", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                serviceOrderMasterData.setserviceorder_masterid(String.valueOf(cursor.getInt(0)));
                serviceOrderMasterData.setserviceorder_type(cursor.getString(1));
                serviceOrderMasterData.set_serviceorder_date(cursor.getString(2));
                serviceOrderMasterData.set_serviceorder_user_id(cursor.getString(3));
                serviceOrderMasterData.setserviceorder_customer_id(cursor.getString(4));
                serviceOrderMasterData.setserviceorder_division_id(cursor.getString(5));
                serviceOrderMasterData.setserviceorder_company_id(cursor.getString(6));
                serviceOrderMasterData.setserviceorderstatus(cursor.getString(7));
                serviceOrderMasterData.setserviceordercdatetime(cursor.getString(8));
                serviceOrderMasterData.setserviceorderffm_id(cursor.getString(10));
                serviceOrderMasterData.set_token_amount(cursor.getString(11));

                serviceOrderMasterData.setServiceOrderDetailMasterList(getServiceorderDetails(serviceOrderMasterData));


                // Adding contact to list
                serviceOrderMasterList.add(serviceOrderMasterData);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // get Serviceordersetails
    public List<ServiceOrderDetailMaster> getServiceorderDetails(ServiceOrderMaster serviceOrderMaster) {

        List<ServiceOrderDetailMaster> serviceOrderMasterList = new ArrayList<ServiceOrderDetailMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_DETAIL_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME+","
                + KEY_TABLE_SERVICEORDER_SLAB_ID
                + " FROM " + TABLE_SERVICEORDERDETAILS + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceOrderMaster.getserviceorder_masterid();
        Log.d("selectQuery2 ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderDetailMaster serviceOrderDetailMaster = new ServiceOrderDetailMaster();
                serviceOrderDetailMaster.setserviceorderdetail_masterid(cursor.getString(0));
                serviceOrderDetailMaster.setserviceorderdetailorderid_id(cursor.getString(1));
                serviceOrderDetailMaster.set_serviceorderdetail_crop_id(cursor.getString(2));
                serviceOrderDetailMaster.setserviceorderdetail_scheme_id(cursor.getString(3));
                serviceOrderDetailMaster.setserviceorderdetail_product_id(cursor.getString(4));
                serviceOrderDetailMaster.set_serviceorderdetail_quantity(cursor.getString(5));
                serviceOrderDetailMaster.setserviceorderdetail_order_price(cursor.getString(6));
                serviceOrderDetailMaster.setserviceorderdetail_advance_amount(cursor.getString(7));
                serviceOrderDetailMaster.setserviceorderdetail_status(cursor.getString(8));
                serviceOrderDetailMaster.setserviceorderdetailcdatetime(cursor.getString(9));
                serviceOrderDetailMaster.setSlabId(cursor.getString(11));

                // Adding contact to list
                serviceOrderMasterList.add(serviceOrderDetailMaster);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // Adding Serviceorderdetails
    public void addServiceorderdetails(ServiceOrderDetailMaster serviceOrderDetailMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_serviceorderdetails = new ContentValues();
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID, serviceOrderDetailMaster.getserviceorderdetail_masterid());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID, serviceOrderDetailMaster.getserviceorderdetailorderid_id());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID, serviceOrderDetailMaster.get_serviceorderdetail_crop_id());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID, serviceOrderDetailMaster.getserviceorderdetail_scheme_id()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID, serviceOrderDetailMaster.getserviceorderdetail_product_id());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY, serviceOrderDetailMaster.get_serviceorderdetail_quantity());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE, serviceOrderDetailMaster.getserviceorderdetail_order_price());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT, serviceOrderDetailMaster.getserviceorderdetail_advance_amount()); // Contact DivName
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_STATUS, serviceOrderDetailMaster.getserviceorderdetail_status()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME, serviceOrderDetailMaster.getserviceorderdetailcdatetime()); // Contact DivName
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME, serviceOrderDetailMaster.getserviceorderdetailudatetime()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_FFM_ID, serviceOrderDetailMaster.getFfmID()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_SLAB_ID, serviceOrderDetailMaster.getSlabId()); // Contact Div_code

        // Inserting Row
        db.insert(TABLE_SERVICEORDERDETAILS, null, values_serviceorderdetails);
        db.close(); // Closing database connection
    }

    void addPaymentCollection(Payment_collection payment_collection) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_payment_collection = new ContentValues();
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID, payment_collection.getID());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_TYPE, payment_collection.get_payment_type());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_USER_ID, payment_collection.get_user_id()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_COMPANY_ID, payment_collection.get_company_id()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_DIVISION_ID, payment_collection.get_division_id()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CUSTOMER_ID, payment_collection.get_customer_id()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT, payment_collection.get_total_amount()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_MODE, payment_collection.get_payment_mode());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_BANK_NAME, payment_collection.get_bank_name());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO, payment_collection.get_rtgs_or_neft_no()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME, payment_collection.get_payment_datetime()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER, payment_collection.get_date_on_cheque_no()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO, payment_collection.get_cheque_no_dd_no()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_STATUS, payment_collection.get_status()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CREATED_DATETIME, payment_collection.get_created_datetime()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME, payment_collection.get_updated_datetime()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_FFMID, payment_collection.get_ffmid());
        // Inserting Row
        db.insert(TABLE_PAYMENT_COLLECTION, null, values_payment_collection);
        db.close(); // Closing database connection
    }


    public void addDailyDairy(DailyDairy dailydairy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_dd = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        values_dd.put(KEY_DD_MASTER_ID, dailydairy.getID());
        values_dd.put(KEY_DD_TITLE, dailydairy.get_title());
        values_dd.put(KEY_DD_USER_ID, dailydairy.get_userid());
        values_dd.put(KEY_DD_COMMENTS, dailydairy.get_comments());
        values_dd.put(KEY_DD_TIME_SLOT, dailydairy.get_time());
        values_dd.put(KEY_DD_DATE, dailydairy.get_date());
        values_dd.put(KEY_DD_CREATED_DATE, dailydairy.get_createddate());
        values_dd.put(KEY_DD_FFMID, dailydairy.get_ffmid());
        values_dd.put(KEY_DD_TYPE, dailydairy.get_type());
        values_dd.put(KEY_DD_TENTATIVE_TIME, dailydairy.get_tentative_time());
        values_dd.put(KEY_DD_STATUS, dailydairy.get_status());


        // Inserting Row
        db.insert(TABLE_DAILYDAIRY, null, values_dd);
//    Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    public List<DailyDairy> getAlldateDailyDairy(String userID, String item) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_DATE + " = '" + item + "'" + " AND " + KEY_DD_FFMID + " IS NOT NULL AND "+KEY_DD_TYPE +"= 1";
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " = 1" + " AND " + KEY_DD_DATE + " = '" + item + "'";

        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));


                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAlldateDairyAdhoc(String userID, String item) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_DATE + " = '" + item + "'" + " AND " + KEY_DD_FFMID + " IS NOT NULL AND "+KEY_DD_TYPE +"= 1";
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " IN(1,2) AND " + KEY_DD_DATE + " = '" + item + "'" + " ORDER BY " + KEY_DD_TYPE;
        ;

        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));


                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }


    public int getAdhocCompletedPendingCount(String userID, String date, int status) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " IN(" + userID + ") AND " + KEY_DD_DATE + " = '" + date + "'" + " AND " + KEY_DD_STATUS + "= " + status + " AND " + KEY_DD_TYPE + "=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.getCount();
        Log.e("DD query", selectQuery + "\n count:" + cursor.getCount());

        // return contact list
        return cursor.getCount();
    }

    public int getRetailersCount() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.getCount();
        Log.e("DD query", selectQuery + "\n count:" + cursor.getCount());

        // return contact list
        return cursor.getCount();
    }

    public List<DailyDairy> getAllDailyDairy(String userID) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_FFMID + " IS NOT NULL";
        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));


                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAdhocDailyDairy(String userID, String date) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " = 2" + " AND " + KEY_DD_DATE + " = '" + date + "'";
        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAllnullDailyDairy(String userId) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userId + " AND " + KEY_DD_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(9));
                dailyDairy.set_tentative_time(cursor.getString(10));
                dailyDairy.set_status(cursor.getInt(11));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAllnullAdhocDailyDairy(String userId) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userId + " AND " + KEY_DD_FFMID + " IS NULL AND " + KEY_DD_TYPE + "=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }


    public List<Payment_collection> getAllPaymentCollectionhistory(String userId, String cust_id) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " PC LEFT JOIN " + TABLE_CUSTOMERS + " C ON PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " WHERE PC." + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = " + cust_id + " ORDER BY " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("payment select ", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Log.e("length", String.valueOf(paymentCollectionListList.size()));
        if (cursor.moveToFirst()) {
            do {
                Payment_collection pc = new Payment_collection();
                pc.setID(cursor.getString(0));
                pc.set_payment_type(cursor.getString(2));
                pc.set_user_id(cursor.getString(3));
                pc.set_company_id(cursor.getString(4));
                pc.set_division_id(cursor.getString(5));
                pc.set_customer_id(cursor.getString(6));
                pc.set_total_amount(cursor.getString(7));
                pc.set_payment_mode(cursor.getString(8));
                pc.set_bank_name(cursor.getString(9));
                pc.set_rtgs_or_neft_no(cursor.getString(10));
                pc.set_payment_datetime(cursor.getString(11));
                pc.set_date_on_cheque_no(cursor.getString(12));
                pc.set_cheque_no_dd_no(cursor.getString(13));
                pc.set_status(Integer.parseInt(cursor.getString(14)));
                pc.set_created_datetime(cursor.getString(15));
                pc.set_updated_datetime(cursor.getString(16));
                pc.set_ffmid(cursor.getString(17));
                // Adding contact to list
                paymentCollectionListList.add(pc);
            } while (cursor.moveToNext());
        }

        // return contact list
        return paymentCollectionListList;
    }

    public List<Payment_collection> getAllPaymentCollectionhistoryForABS(String userId, String cust_id, String division_id) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " PC LEFT JOIN " + TABLE_CUSTOMERS + " C ON PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " WHERE PC." + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = " + cust_id + " AND PC." + KEY_PAYMENT_COLLECTION_DIVISION_ID + " = " + division_id + " AND PC." + KEY_PAYMENT_COLLECTION_PAYMENT_TYPE + " = 1" + " ORDER BY " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("payment select ", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Log.e("length", String.valueOf(paymentCollectionListList.size()));
        if (cursor.moveToFirst()) {
            do {
                Payment_collection pc = new Payment_collection();
                pc.setID(cursor.getString(0));
                pc.set_payment_type(cursor.getString(2));
                pc.set_user_id(cursor.getString(3));
                pc.set_company_id(cursor.getString(4));
                pc.set_division_id(cursor.getString(5));
                pc.set_customer_id(cursor.getString(6));
                pc.set_total_amount(cursor.getString(7));
                pc.set_payment_mode(cursor.getString(8));
                pc.set_bank_name(cursor.getString(9));
                pc.set_rtgs_or_neft_no(cursor.getString(10));
                pc.set_payment_datetime(cursor.getString(11));
                pc.set_date_on_cheque_no(cursor.getString(12));
                pc.set_cheque_no_dd_no(cursor.getString(13));
                pc.set_status(Integer.parseInt(cursor.getString(14)));
                pc.set_created_datetime(cursor.getString(15));
                pc.set_updated_datetime(cursor.getString(16));
                pc.set_ffmid(cursor.getString(17));
                // Adding contact to list
                paymentCollectionListList.add(pc);
            } while (cursor.moveToNext());
        }

        // return contact list
        return paymentCollectionListList;
    }


    public List<Payment_collection> getAllPaymentCollection(String userId) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " WHERE " + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND " + KEY_PAYMENT_COLLECTION_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_collection pc = new Payment_collection();
                pc.setID(cursor.getString(0));
                pc.set_payment_type(cursor.getString(2));
                pc.set_user_id(cursor.getString(3));
                pc.set_company_id(cursor.getString(4));
                pc.set_division_id(cursor.getString(5));
                pc.set_customer_id(cursor.getString(6));
                pc.set_total_amount(cursor.getString(7));
                pc.set_payment_mode(cursor.getString(8));
                pc.set_bank_name(cursor.getString(9));
                pc.set_rtgs_or_neft_no(cursor.getString(10));
                pc.set_payment_datetime(cursor.getString(11));
                pc.set_date_on_cheque_no(cursor.getString(12));
                pc.set_cheque_no_dd_no(cursor.getString(13));
                pc.set_status(Integer.parseInt(cursor.getString(14)));
                pc.set_created_datetime(cursor.getString(15));
                pc.set_updated_datetime(cursor.getString(16));
                pc.set_ffmid(cursor.getString(17));
                // Adding contact to list
                paymentCollectionListList.add(pc);
            } while (cursor.moveToNext());
        }

        // return contact list
        return paymentCollectionListList;
    }


    public List<Customer_BankDetails> getAllNullCustomerbankdetails(String cust_id) {
        List<Customer_BankDetails> dairy = new ArrayList<Customer_BankDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_BANKDETAILS + " WHERE " + KEY_BANKDETAIL_CUSTOMER_ID + " = '" + cust_id + "' AND " + KEY_BANKDETAIL_FFMID + " IS NULL";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_BankDetails dailyDairy = new Customer_BankDetails();
                dailyDairy.set_cus_bak_dtls_masterid(cursor.getString(1));
                dailyDairy.set_customer_id(cursor.getString(2));
                dailyDairy.set_account_no(cursor.getString(3));
                dailyDairy.set_account_name(cursor.getString(4));
                dailyDairy.set_bank_name(cursor.getString(5));
                dailyDairy.set_branch_name(cursor.getString(6));
                dailyDairy.set_ifsc_code(cursor.getString(7));
                dailyDairy.set_status(cursor.getString(8));
                dailyDairy.set_created_by(cursor.getString(9));
                dailyDairy.set_updated_by(cursor.getString(10));
                dailyDairy.set_created_date(cursor.getString(11));
                dailyDairy.set_ffmid(cursor.getString(12));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<Customer_Bank_Details> getAllCustomerbankdetails(String cust_id) {
        List<Customer_Bank_Details> dairy = new ArrayList<Customer_Bank_Details>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_BANKDETAILS + " WHERE " + KEY_BANKDETAIL_CUSTOMER_ID + " = '" + cust_id + "'";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_Bank_Details dailyDairy = new Customer_Bank_Details();
                dailyDairy.set_cus_bak_dtls_masterid(cursor.getString(1));
                dailyDairy.set_customer_id(cursor.getString(2));
                dailyDairy.set_account_no(cursor.getString(3));
                dailyDairy.set_account_name(cursor.getString(4));
                dailyDairy.set_bank_name(cursor.getString(5));
                dailyDairy.set_branch_name(cursor.getString(6));
                dailyDairy.set_ifsc_code(cursor.getString(7));
                dailyDairy.set_status(cursor.getString(8));
                dailyDairy.set_created_by(cursor.getString(9));
                dailyDairy.set_updated_by(cursor.getString(10));
                dailyDairy.set_created_date(cursor.getString(11));
                dailyDairy.set_ffmid(cursor.getString(12));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<Commodity_Price> getAllnullCommodity_price(String userId) {
        List<Commodity_Price> commodity_prices = new ArrayList<Commodity_Price>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE + " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId + " AND " + KEY_COMMODITY_PRICE_FFMID + " IS NULL";
        // String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE +  " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId +" AND "+KEY_COMMODITY_PRICE_FFMID +".compareTo('0') > 0";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Commodity_Price commodity_price = new Commodity_Price();
                commodity_price.setID(Integer.parseInt(cursor.getString(0)));
                commodity_price.set_commodity_price_master_id(cursor.getString(1));
                commodity_price.set_commodity_price_crop_name(cursor.getString(2));
                commodity_price.set_commodity_price_variety_type(cursor.getString(3));
                commodity_price.set_commodity_price_apmc_mandi_price(cursor.getString(4));
                commodity_price.set_commodity_price_commodity_dealer_agent_price(cursor.getString(5));
                commodity_price.set_commodity_price_purchage_price_by_industry(cursor.getString(6));
                commodity_price.set_commodity_price_created_by(cursor.getString(7));
                commodity_price.set_commodity_price_created_on(cursor.getString(8));
                commodity_price.set_commodity_price_ffmid(cursor.getString(9));

                // Adding contact to list
                commodity_prices.add(commodity_price);
            } while (cursor.moveToNext());
        }

        // return contact list
        return commodity_prices;
    }

    public List<Commodity_Price> getAllCommodity_price(String userId) {
        List<Commodity_Price> commodity_prices = new ArrayList<Commodity_Price>();
        // Select All Query
        //  String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE +  " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId +" AND "+KEY_COMMODITY_PRICE_FFMID +".compareTo(0) > 0";

        String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE + " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId + " AND " + KEY_COMMODITY_PRICE_FFMID + " IS NOT NULL ORDER BY " + KEY_COMMODITY_PRICE_ID + " DESC";
        //String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE ;
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Commodity_Price commodity_price = new Commodity_Price();
                commodity_price.setID(Integer.parseInt(cursor.getString(0)));
                commodity_price.set_commodity_price_master_id(cursor.getString(1));
                commodity_price.set_commodity_price_crop_name(cursor.getString(2));
                commodity_price.set_commodity_price_variety_type(cursor.getString(3));
                commodity_price.set_commodity_price_apmc_mandi_price(cursor.getString(4));
                commodity_price.set_commodity_price_commodity_dealer_agent_price(cursor.getString(5));
                commodity_price.set_commodity_price_purchage_price_by_industry(cursor.getString(6));
                commodity_price.set_commodity_price_created_by(cursor.getString(7));
                commodity_price.set_commodity_price_created_on(cursor.getString(8));
                commodity_price.set_commodity_price_ffmid(cursor.getString(9));

                // Adding contact to list
                commodity_prices.add(commodity_price);
            } while (cursor.moveToNext());
        }

        // return contact list
        return commodity_prices;
    }

    public List<Crop_Shifts> getAllnullCrop_Shifts(String userId) {
        List<Crop_Shifts> crop_shifts = new ArrayList<Crop_Shifts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_CREATED_BY + " = " + userId + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NULL";
        Log.e("cs query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crop_Shifts crop_shifts1 = new Crop_Shifts();
                crop_shifts1.setID(cursor.getString(0));
                crop_shifts1.set_crop_shifts_master_id(cursor.getString(1));
                crop_shifts1.set_crop_shifts_crop_name(cursor.getString(2));
                crop_shifts1.set_crop_shifts_variety_type(cursor.getString(3));
                crop_shifts1.set_crop_shifts_previous_year_area(cursor.getString(4));
                crop_shifts1.set_crop_shifts_current_year_expected_area(cursor.getString(5));
                crop_shifts1.set_crop_shifts_percentage_increase_decrease(cursor.getString(6));
                crop_shifts1.set_crop_shifts_reason_crop_shift(cursor.getString(7));
                crop_shifts1.set_crop_shifts_created_by(cursor.getString(8));
                crop_shifts1.set_crop_shifts_created_on(cursor.getString(9));
                crop_shifts1.set_crop_shifs_crop_in_saved_seed(cursor.getString(10));
                crop_shifts1.set_crop_shifs_crop_in_previous_year(cursor.getString(11));
                crop_shifts1.set_crop_shifs_crop_in_current_year(cursor.getString(12));
                crop_shifts1.set_crop_shifs_crop_in_next_year(cursor.getString(13));
                crop_shifts1.set_crop_shifts_ffmid(cursor.getString(14));

                // Adding contact to list
                crop_shifts.add(crop_shifts1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return crop_shifts;
    }

    public List<Crop_Shifts> getAllCrop_Shifts(String userId) {
        List<Crop_Shifts> crop_shifts = new ArrayList<Crop_Shifts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_CREATED_BY + " = " + userId + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NOT NULL ORDER BY " + KEY_CROP_SHIFTS_ID + " DESC";
        Log.e("cs query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crop_Shifts crop_shifts1 = new Crop_Shifts();
                crop_shifts1.setID(cursor.getString(0));
                crop_shifts1.set_crop_shifts_master_id(cursor.getString(1));
                crop_shifts1.set_crop_shifts_crop_name(cursor.getString(2));
                crop_shifts1.set_crop_shifts_variety_type(cursor.getString(3));
                crop_shifts1.set_crop_shifts_previous_year_area(cursor.getString(4));
                crop_shifts1.set_crop_shifts_current_year_expected_area(cursor.getString(5));
                crop_shifts1.set_crop_shifts_percentage_increase_decrease(cursor.getString(6));
                crop_shifts1.set_crop_shifts_reason_crop_shift(cursor.getString(7));
                crop_shifts1.set_crop_shifts_created_by(cursor.getString(8));
                crop_shifts1.set_crop_shifts_created_on(cursor.getString(9));
                crop_shifts1.set_crop_shifs_crop_in_saved_seed(cursor.getString(10));
                crop_shifts1.set_crop_shifs_crop_in_previous_year(cursor.getString(11));
                crop_shifts1.set_crop_shifs_crop_in_current_year(cursor.getString(12));
                crop_shifts1.set_crop_shifs_crop_in_next_year(cursor.getString(13));
                crop_shifts1.set_crop_shifts_ffmid(cursor.getString(14));

                // Adding contact to list
                crop_shifts.add(crop_shifts1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return crop_shifts;
    }


    public List<Price_Survey> getAllnullPrice_Survey(String checkuid) {
        List<Price_Survey> price_surveys = new ArrayList<Price_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRICE_SURVEY + " WHERE " + KEY_PRICE_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NULL";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price_Survey price_survey1 = new Price_Survey();
                price_survey1.setID(cursor.getInt(0));
                price_survey1.set_price_survey_master_id(cursor.getString(1));
                price_survey1.set_price_survey_company_name(cursor.getString(2));
                price_survey1.set_price_survey_product_name(cursor.getString(3));
                price_survey1.set_price_survey_sku_pack_size(cursor.getString(4));
                price_survey1.set_price_survey_retail_price(cursor.getString(5));
                price_survey1.set_price_survey_invoice_price(cursor.getString(6));
                price_survey1.set_price_survey_net_distributor_landing_price(cursor.getString(7));
                price_survey1.set_price_survey_created_by(cursor.getString(8));
                price_survey1.set_price_survey_created_on(cursor.getString(9));
                price_survey1.set_price_survey_ffmid(cursor.getString(10));

                // Adding contact to list
                price_surveys.add(price_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return price_surveys;
    }


    public List<Price_Survey> getAllPrice_Survey(String userId) {
        List<Price_Survey> price_surveys = new ArrayList<Price_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRICE_SURVEY + " WHERE " + KEY_PRICE_SURVEY_CREATED_BY + " = " + userId + " AND " + KEY_PRICE_SURVEY_FFMID + " IS NOT NULL ORDER BY " + KEY_PRICE_SURVEY_ID + " DESC";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price_Survey price_survey1 = new Price_Survey();
                price_survey1.setID(cursor.getInt(0));
                price_survey1.set_price_survey_master_id(cursor.getString(1));
                price_survey1.set_price_survey_company_name(cursor.getString(2));
                price_survey1.set_price_survey_product_name(cursor.getString(3));
                price_survey1.set_price_survey_sku_pack_size(cursor.getString(4));
                price_survey1.set_price_survey_retail_price(cursor.getString(5));
                price_survey1.set_price_survey_invoice_price(cursor.getString(6));
                price_survey1.set_price_survey_net_distributor_landing_price(cursor.getString(7));
                price_survey1.set_price_survey_created_by(cursor.getString(8));
                price_survey1.set_price_survey_created_on(cursor.getString(9));
                price_survey1.set_price_survey_ffmid(cursor.getString(10));

                // Adding contact to list
                price_surveys.add(price_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return price_surveys;
    }


    public List<Product_Survey> getAllnullProduct_Survey(String checkuid) {
        List<Product_Survey> product_surveys = new ArrayList<Product_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRODUCT_SURVEY + " WHERE " + KEY_PRODUCT_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_PRODUCT_SURVEY_FFMID + " IS NULL";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product_Survey product_survey1 = new Product_Survey();
                product_survey1.setID(cursor.getInt(0));
                product_survey1.set_product_survey_master_id(cursor.getString(1));
                product_survey1.set_product_survey_company_name(cursor.getString(2));
                product_survey1.set_product_survey_product_name(cursor.getString(3));
                product_survey1.set_product_survey_name_of_the_check_segment(cursor.getString(4));
                product_survey1.set_product_survey_launch_year(cursor.getString(5));
                product_survey1.set_product_survey_no_units_sold(cursor.getString(6));
                product_survey1.set_product_survey_area_crop_sown_new_product(cursor.getString(7));
                product_survey1.set_product_survey_remarks_unique_feature(cursor.getString(8));
                product_survey1.set_product_survey_created_by(cursor.getString(9));
                product_survey1.set_product_survey_created_by(cursor.getString(10));
                product_survey1.set_product_survey_ffmid(cursor.getString(11));

                // Adding contact to list
                product_surveys.add(product_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return product_surveys;
    }


    public List<Product_Survey> getAllProduct_Survey(String checkuid) {
        List<Product_Survey> product_surveys = new ArrayList<Product_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRODUCT_SURVEY + " WHERE " + KEY_PRODUCT_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_PRODUCT_SURVEY_FFMID + " IS NOT NULL ORDER BY " + KEY_PRODUCT_SURVEY_ID + " DESC";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product_Survey product_survey1 = new Product_Survey();
                product_survey1.setID(cursor.getInt(0));
                product_survey1.set_product_survey_master_id(cursor.getString(1));
                product_survey1.set_product_survey_company_name(cursor.getString(2));
                product_survey1.set_product_survey_product_name(cursor.getString(3));
                product_survey1.set_product_survey_name_of_the_check_segment(cursor.getString(4));
                product_survey1.set_product_survey_launch_year(cursor.getString(5));
                product_survey1.set_product_survey_no_units_sold(cursor.getString(6));
                product_survey1.set_product_survey_area_crop_sown_new_product(cursor.getString(7));
                product_survey1.set_product_survey_remarks_unique_feature(cursor.getString(8));
                product_survey1.set_product_survey_created_by(cursor.getString(9));
                product_survey1.set_product_survey_created_on(cursor.getString(10));
                product_survey1.set_product_survey_ffmid(cursor.getString(11));

                // Adding contact to list
                product_surveys.add(product_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return product_surveys;
    }


    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting single division
    Divisions getDivision(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIVISION, new String[]{KEY_TABLE_DIVISION_ID,
                        KEY_TABLE_DIVISION_MASTER_ID, KEY_TABLE_DIVISION_NAME, KEY_TABLE_DIVISION_CODE, KEY_TABLE_DIVISION_SAP_ID, KEY_TABLE_DIVISION_STATUS, KEY_TABLE_DIVISION_CREATED_DATETIME, KEY_TABLE_DIVISION_UPDATED_DATETIME}, KEY_TABLE_DIVISION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Divisions divisions = new Divisions(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        return divisions;
    }


    // Getting single COMPANY
    Companies getCompany(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMPANIES, new String[]{KEY_TABLE_COMPANIES_ID,
                        KEY_TABLE_COMPANIES_MASTER_ID, KEY_TABLE_COMPANIES_NAME, KEY_TABLE_COMPANIES_COMPANY_CODE, KEY_TABLE_COMPANIES_COMPANY_SAP_ID, KEY_TABLE_COMPANIES_COMPANY_STATUS, KEY_TABLE_COMPANIES_CREATED_DATETIME, KEY_TABLE_COMPANIES_UPDATED_DATETIME}, KEY_TABLE_COMPANIES_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Companies companies = new Companies(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        return companies;
    }


    // Getting single Region
    Regions getRegion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REGION, new String[]{KEY_REGION_ID,
                        KEY_REGION__MASTER_ID, KEY_REGION_NAME, KEY_REGION_CODE, KEY_REGION_STATUS}, KEY_REGION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Regions regions = new Regions(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        return regions;
    }

    // Getting single crop
    Crops getCrop(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CROPS, new String[]{KEY_TABLE_CROPS_CROP_ID,
                        KEY_TABLE_CROPS_CROP_MASTER_ID, KEY_TABLE_CROPS_CROP_NAME}, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Crops crops = new Crops(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        Log.v("Cropidis xyz", cursor.getString(1) + cursor.getString(2));
        return crops;
    }

    // Getting All Orders
    public List<Orders> getAllOrders() {
        List<Orders> ordersList = new ArrayList<Orders>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Orders orders = new Orders();
                orders.setID(Integer.parseInt(cursor.getString(0)));
                orders.setName(cursor.getString(1));
                orders.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                ordersList.add(orders);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ordersList;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All Divisions
    public List<Divisions> getAllDivisions() {
        List<Divisions>
                divisionList = new ArrayList<Divisions>();
        try {
            //String selectQuery = "SELECT  * FROM " + TABLE_DIVISION;
            String selectQuery = "SELECT  * FROM " + TABLE_DIVISION + " where " + KEY_TABLE_DIVISION_MASTER_ID + " = 2  " + " or " + KEY_TABLE_DIVISION_MASTER_ID + " = 3  ";
            //	String selectQuery = "SELECT  * FROM " + TABLE_DIVISION + " where " + KEY_TABLE_DIVISION_MASTER_ID + " = " + example + "";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {

                    Divisions divisions = new Divisions();
                    divisions.setID(Integer.parseInt(cursor.getString(0)));
                    divisions.setDivMasterID(cursor.getString(1));
                    divisions.setDivName(cursor.getString(2));
                    divisions.setDivcode(cursor.getString(3));
                    divisions.setDivsapid(cursor.getString(4));
                    divisions.setDivstatus(cursor.getString(5));
                    divisions.setDivcdatetime(cursor.getString(6));
                    divisions.setDivudatetime(cursor.getString(7));
                    // Adding contact to list
                    divisionList.add(divisions);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }

        return divisionList;
    }

    // Getting All Companies
    public List<Companies> getAllCompanies() {
        List<Companies> companiesList = new ArrayList<Companies>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Companies companies = new Companies();
                companies.setID(Integer.parseInt(cursor.getString(0)));
                companies.setCompanyMasterId(cursor.getString(2));
                companies.setCompanyName(cursor.getString(1));
                companies.setCompanycode(cursor.getString(3));
                companies.setCompanysapid(cursor.getString(4));
                companies.setCompanystatus(cursor.getString(5));
                companies.setCompanycdatetime(cursor.getString(6));
                companies.setCompanyudatetime(cursor.getString(7));
                // Adding contact to list
                companiesList.add(companies);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }


    // Getting All EVM
    /*public List<Employe_visit_management_pojo> getAllEVM() {
        List<Employe_visit_management_pojo> evmList = new ArrayList<Employe_visit_management_pojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Employe_visit_management_pojo employe_visit_management_pojo =  new Employe_visit_management_pojo();
				employe_visit_management_pojo.setEmpvisitffmid(Integer.parseInt(cursor.getString(27)));
				employe_visit_management_pojo.setEmp_visitMasterId(cursor.getString(1));
				employe_visit_management_pojo.setEmp_visit_userid(cursor.getString(2));
				employe_visit_management_pojo.setEmp_visit_customerid(cursor.getString(3));
				employe_visit_management_pojo.setEmp_visit_plantype(cursor.getString(4));
				employe_visit_management_pojo.setEmp_visit_purposevisit(cursor.getString(5));
				employe_visit_management_pojo.setEmp_visit_plandatetime(cursor.getString(6));
				employe_visit_management_pojo.setEmp_visit_concernpersonname(cursor.getString(7));
				employe_visit_management_pojo.setEmp_visit_mobile(cursor.getString(8));
				employe_visit_management_pojo.setEmp_visit_village(cursor.getString(9));
				employe_visit_management_pojo.setEmp_visit_locationaddress(cursor.getString(10));
				employe_visit_management_pojo.setEmp_visit_fieldarea(cursor.getString(11));
				employe_visit_management_pojo.setEmp_visit_checkintime(cursor.getString(12));
				employe_visit_management_pojo.setEmp_visit_comments(cursor.getString(13));
				employe_visit_management_pojo.setEmpvisitstatus(cursor.getString(14));
				employe_visit_management_pojo.setEmp_visit_createdby(cursor.getString(15));
				employe_visit_management_pojo.setEmp_visit_updatedby(cursor.getString(16));
				// Adding contact to list
				evmList.add(employe_visit_management_pojo);
			} while (cursor.moveToNext());
		}

		// return contact list
		return evmList;
	}*/
    // Getting All Geo Tracking
    public List<Geo_Tracking_POJO> getAllGeotracking() {
        List<Geo_Tracking_POJO> geotrackingList = new ArrayList<Geo_Tracking_POJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " =0 OR " + KEY_TABLE_GEO_TRACKING_FFMID + " IS NULL OR " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " =0 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Geo_Tracking_POJO geo_tracking_pojo = new Geo_Tracking_POJO();
                geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(0)));
                geo_tracking_pojo.setGeoMasterID(cursor.getString(2));
                geo_tracking_pojo.setGeoVisitType(cursor.getString(1));
                geo_tracking_pojo.set_Geo_user_id(cursor.getString(3));
                geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(4));
                geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(5));
                geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(6));
                geo_tracking_pojo.setGeo_distance(cursor.getString(7));
                geo_tracking_pojo.setGeo_visit_date(cursor.getString(8));
                geo_tracking_pojo.setGeo_check_in_time(cursor.getString(9));
                geo_tracking_pojo.setGeo_check_out_time(cursor.getString(10));
                geo_tracking_pojo.setGeostatus(cursor.getString(11));
                geo_tracking_pojo.setGeoffmid(cursor.getString(12));
                geo_tracking_pojo.setGeocdatetime(cursor.getString(13));
                geo_tracking_pojo.setGeoudatetime(cursor.getString(14));
                // Adding contact to list
                geotrackingList.add(geo_tracking_pojo);
            } while (cursor.moveToNext());
        }

        // return contact list
        return geotrackingList;
    }


    public boolean getAllGeotracking(String traking_id) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " IS NULL ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {

        }

        // return contact list
        return false;
    }


    // Getting Schemes by product Id
    public List<Schemes> getSchemesByProducId(String productId) {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String CURDATE = df.format(c.getTime());
        String selectQuery = "SELECT  " + KEY_SCHEMES_TITLE + "," + KEY_TABLE_SCHEME_PRODUCTS_PRICE + ",s." + KEY_SCHEMES_MASTER_ID + ",slab_id" + " FROM "
                + TABLE_SCHEME_PRODUCTS + " sp JOIN " + TABLE_SCHEMES + " s ON s." + KEY_SCHEMES_MASTER_ID + " = sp." + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID
                + " WHERE sp." + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + "=" + productId
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + "<= '" + CURDATE + "' AND " + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " >= '" + CURDATE
                + "' AND sp." + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + "=" + sharedpreferences.getString("region_id","0")
                + " GROUP BY s." + KEY_SCHEMES_MASTER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setschemeName(cursor.getString(3));
                schemes.setScheme_value(cursor.getString(1));
                schemes.setschemeMasterID(cursor.getString(2));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }


    public List<Schemes> getSchemesByProducId(String productId,String slabId,String schemeID) {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String CURDATE = df.format(c.getTime());
        String selectQuery = "SELECT  id," +KEY_TABLE_SCHEME_PRODUCTS_PRICE + "," + KEY_SCHEMES_MASTER_ID + ",slab_id" + " FROM "
                + TABLE_SCHEME_PRODUCTS
                + " WHERE " + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + "=" + productId
               // + " AND " + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + "<= '" + CURDATE + "' AND " + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " >= '" + CURDATE
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + "=" + sharedpreferences.getString("region_id","0")
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + "='" + slabId+"'"
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID + "='" + schemeID+"'";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
Log.d("selectQuery",selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setschemeName(cursor.getString(3));
                schemes.setScheme_value(cursor.getString(1));
                schemes.setschemeMasterID(cursor.getString(2));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }

    // Getting amount by product Id
    public Products_Pojo getAmountByProducId(String productId) {
        Products_Pojo products = null;
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID
        String selectQuery = "SELECT  pp." + KEY_PRODUCTS_PRICE + "," + KEY_PRODUCTS_PACKETS_COUNT + "," + KEY_PRODUCTS_CATALOG_URL + " FROM " + TABLE_PRODUCTS + " p JOIN " + TABLE_PRODUCT_PRICE + " pp ON p." + KEY_PRODUCT_MASTER_ID + "=pp." + KEY_PRODUCT_MASTER_ID + " WHERE p." + KEY_PRODUCT_MASTER_ID + "=" + productId + " AND DATE('now') > DATE(pp." + KEY_PRODUCT_FROM_DATE + ") AND date('now') < DATE(pp." + KEY_PRODUCT_TO_DATE + ")";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                products = new Products_Pojo();
                products.setProductprice(cursor.getString(0));
                products.set_product_no_packets(cursor.getString(1));
                products.set_product_catalog_url(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return products
        return products;
    }

    // Getting All Geo Tracking with checkin
    public List<Geo_Tracking_POJO> getAllGeotrackingwithcheckin(String user_id) {
        List<Geo_Tracking_POJO> geotrackingList = new ArrayList<Geo_Tracking_POJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + " IS NOT NULL" + " AND " + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " IS NOT NULL" + " AND " + KEY_TABLE_GEO_TRACKING_FFMID + " IS  NULL" + " AND " + KEY_TABLE_GEO_TRACKING_USER_ID + "=" + user_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Geo_Tracking_POJO geo_tracking_pojo = new Geo_Tracking_POJO();
                geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(0)));
                geo_tracking_pojo.setGeoMasterID(cursor.getString(2));
                geo_tracking_pojo.setGeoVisitType(cursor.getString(1));
                geo_tracking_pojo.set_Geo_user_id(cursor.getString(3));
                geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(4));
                geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(5));
                geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(6));
                geo_tracking_pojo.setGeo_distance(cursor.getString(7));
                geo_tracking_pojo.setGeo_visit_date(cursor.getString(8));
                geo_tracking_pojo.setGeo_check_in_time(cursor.getString(9));
                geo_tracking_pojo.setGeo_check_out_time(cursor.getString(10));
                geo_tracking_pojo.setGeostatus(cursor.getString(11));
                geo_tracking_pojo.setGeoffmid(cursor.getString(12));
                geo_tracking_pojo.setGeocdatetime(cursor.getString(13));
                geo_tracking_pojo.setGeoudatetime(cursor.getString(14));
                // Adding contact to list
                geotrackingList.add(geo_tracking_pojo);
            } while (cursor.moveToNext());
        }

        // return contact list
        return geotrackingList;
    }

    // Adding new feedback
    public void addFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_feedback = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        values_feedback.put(KEY_TABLE_FEEDBACK_USER_ID, feedback.get_user_id());
        values_feedback.put(KEY_TABLE_FEEDBACK_FARMER_NAME, feedback.getFarmerName());
        values_feedback.put(KEY_TABLE_FEEDBACK_PLACE, feedback.getplace());
        values_feedback.put(KEY_TABLE_FEEDBACK_CONTACT_NO, feedback.getContactNo());
        values_feedback.put(KEY_TABLE_FEEDBACK_CROP, feedback.getCrop());
        values_feedback.put(KEY_TABLE_FEEDBACK_HYBRID, feedback.getHybrid());
        values_feedback.put(KEY_TABLE_FEEDBACK_SOWING_DATE, feedback.getSowingDate());
        values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE, feedback.getfeedbackmessage());
        values_feedback.put(KEY_TABLE_FEEDBACK_IMAGE, feedback.getImage());
        values_feedback.put(KEY_TABLE_FEEDBACK_FFMID, feedback.get_ffmid());
        // Inserting Row
        db.insert(TABLE_FEEDBACK, null, values_feedback);
//    Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    // Getting All Feedback
    public List<Feedback> getAllFeedback(String userId) {
        List<Feedback> feedbackList = new ArrayList<Feedback>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK + " WHERE " + KEY_TABLE_FEEDBACK_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_FEEDBACK_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = new Feedback();
                feedback.setID(Integer.parseInt(cursor.getString(0)));
                feedback.set_user_id(Integer.parseInt(cursor.getString(1)));
                feedback.setFarmerName(cursor.getString(2));
                feedback.setPlace(cursor.getString(3));
                feedback.setContactNo(cursor.getString(4));
                feedback.setCrop(cursor.getString(5));
                feedback.setHybrid(cursor.getString(6));
                feedback.setSowingDate(cursor.getString(7));
                feedback.setFeedbackMessage(cursor.getString(8));
                feedback.setImage(cursor.getString(9));
                feedback.set_ffmid(cursor.getString(10));
                // Adding contact to list
                feedbackList.add(feedback);
            } while (cursor.moveToNext());
        }

        // return contact list
        return feedbackList;
    }

    public List<Feedback> getAllFeedbacks(String userId) {
        List<Feedback> feedbacksList = new ArrayList<Feedback>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK + " WHERE " + KEY_TABLE_FEEDBACK_USER_ID + " = '" + userId + "'  ORDER BY " + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = new Feedback();
                feedback.setID(Integer.parseInt(cursor.getString(0)));
                feedback.set_user_id(cursor.getInt(1));
                feedback.setFarmerName(cursor.getString(2));
                feedback.setPlace(cursor.getString(3));
                feedback.setContactNo(cursor.getString(4));
                feedback.setCrop(cursor.getString(5));
                feedback.setHybrid(cursor.getString(6));
                feedback.setSowingDate(cursor.getString(7));
                feedback.setFeedbackMessage(cursor.getString(8));
                feedback.setImage(cursor.getString(9));
                feedback.set_ffmid(cursor.getString(10));
                // Adding contact to list
                feedbacksList.add(feedback);
            } while (cursor.moveToNext());
        }

        // return contact list
        return feedbacksList;
    }

    // Getting All service orders
    public List<ServiceOrderMaster> getAllServiceorders() {
        List<ServiceOrderMaster> companiesList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICEORDER;
        //String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID  +" FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMaster = new ServiceOrderMaster();
                serviceOrderMaster.setID(Integer.parseInt(cursor.getString(0)));
                serviceOrderMaster.setserviceorder_masterid(cursor.getString(2));
                serviceOrderMaster.setserviceorder_type(cursor.getString(1));
                serviceOrderMaster.set_serviceorder_date(cursor.getString(3));
                serviceOrderMaster.set_serviceorder_user_id(cursor.getString(4));
                serviceOrderMaster.setserviceorder_customer_id(cursor.getString(5));
                serviceOrderMaster.setserviceorder_division_id(cursor.getString(6));
                serviceOrderMaster.setserviceorder_company_id(cursor.getString(7));
                serviceOrderMaster.setserviceorderstatus(cursor.getString(8));
                serviceOrderMaster.setserviceordercdatetime(cursor.getString(9));
                serviceOrderMaster.setserviceorderudatetime(cursor.getString(10));
                serviceOrderMaster.setserviceorderffm_id(cursor.getString(11));
                // Adding contact to list
                companiesList.add(serviceOrderMaster);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }

    // Getting All service orders
    public List<ServiceOrderDetailMaster> getAllServiceordersdetails() {
        List<ServiceOrderDetailMaster> companiesList = new ArrayList<ServiceOrderDetailMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICEORDERDETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderDetailMaster serviceOrderDetailMaster = new ServiceOrderDetailMaster();
                serviceOrderDetailMaster.setID(Integer.parseInt(cursor.getString(0)));
                serviceOrderDetailMaster.setserviceorderdetail_masterid(cursor.getString(1));
                serviceOrderDetailMaster.setserviceorderdetailorderid_id(cursor.getString(2));
                serviceOrderDetailMaster.set_serviceorderdetail_crop_id(cursor.getString(3));
                serviceOrderDetailMaster.setserviceorderdetail_scheme_id(cursor.getString(4));
                serviceOrderDetailMaster.setserviceorderdetail_product_id(cursor.getString(5));
                serviceOrderDetailMaster.set_serviceorderdetail_quantity(cursor.getString(6));
                serviceOrderDetailMaster.setserviceorderdetail_order_price(cursor.getString(7));
                serviceOrderDetailMaster.setserviceorderdetail_advance_amount(cursor.getString(8));
                serviceOrderDetailMaster.setserviceorderdetail_status(cursor.getString(9));
                serviceOrderDetailMaster.setserviceorderdetailcdatetime(cursor.getString(10));
                serviceOrderDetailMaster.setserviceorderdetailudatetime(cursor.getString(11));
                // Adding contact to list
                companiesList.add(serviceOrderDetailMaster);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }


    // Getting All Schemes
    public List<Schemes> getAllSchemes() {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setID(Integer.parseInt(cursor.getString(0)));
                schemes.setschemeMasterID(cursor.getString(1));
                schemes.setschemeName(cursor.getString(2));
                //  schemes.setscheme_company_id(cursor.getString(3));
                ////   schemes.setscheme_division_id(cursor.getString(4));
                //   schemes.setscheme_crop_id(cursor.getString(5));
                schemes.setscheme_sap_code(cursor.getString(6));
                //   schemes.setscheme_value(cursor.getString(7));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }

    // Getting All Users
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<Users>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();
                users.setUserMasterID(cursor.getString(1));
                users.setUser_first_name(cursor.getString(2));
                users.setUser_last_name(cursor.getString(3));
                users.setUser_mobile_no(cursor.getString(4));
                users.setUser_email(cursor.getString(5));
                users.setUser_sap_id(cursor.getString(6));
                users.setUser_password(cursor.getString(7));
                users.setUser_role_id(cursor.getString(8));
                users.setUser_reporting_manager_id(cursor.getString(9));
                users.setUserstatus(cursor.getString(10));
                users.setUsercdatetime(cursor.getString(11));
                users.setUserudatetime(cursor.getString(12));
                // Adding contact to list
                usersList.add(users);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }

    // Getting All REGIONS
    public List<Regions> getAllRegions() {
        List<Regions> regionsList = new ArrayList<Regions>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Regions regions = new Regions();
                regions.setID(Integer.parseInt(cursor.getString(0)));
                regions.setRegionMasterID(cursor.getString(2));
                regions.setRegionName(cursor.getString(1));
                regions.setRegioncode(cursor.getString(3));
                regions.setRegionstatus(cursor.getString(4));

                // Adding contact to list
                regionsList.add(regions);
            } while (cursor.moveToNext());
        }

        // return contact list
        return regionsList;
    }


    // Getting All Company Division Crop
    public List<Company_division_crops> getAllCompany_division_crops() {
        List<Company_division_crops> cdcList = new ArrayList<Company_division_crops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = 1  " + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3  ";
        // String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = 1" +  " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3 " ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Company_division_crops company_division_crops = new Company_division_crops();

                company_division_crops.setCdcCropId(cursor.getString(3));
                Log.d("Selected crop id is", cursor.getString(3));

				/*String selectQuerys = "SELECT  KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME FROM " + TABLE_CROPS +" where " + cursor.getString(3)+ " = 1  " + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3  " ;*/
                // Adding contact to list
                // getCrop(cursor.getString(3));
                cdcList.add(company_division_crops);


            } while (cursor.moveToNext());
        }

        // return contact list
        return cdcList;
    }

    // Getting All User_Company_Customer
    public List<User_Company_Customer> getAllUser_Company_Customer() {
        List<User_Company_Customer> uccList = new ArrayList<User_Company_Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_COMPANY_CUSTOMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Company_Customer user_company_customer = new User_Company_Customer();
                user_company_customer.setID(Integer.parseInt(cursor.getString(0)));
                user_company_customer.setuccUserId(cursor.getString(1));
                user_company_customer.setuccCompanyId(cursor.getString(2));
                user_company_customer.setuccCustomerId(cursor.getString(3));


                // Adding contact to list
                uccList.add(user_company_customer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return uccList;

    }

    // Getting All Scheme Products
    public List<Scheme_Products> getAllSchemeProducts() {
        List<Scheme_Products> spList = new ArrayList<Scheme_Products>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scheme_Products scheme_products = new Scheme_Products();
                scheme_products.setspscheme_id(cursor.getString(1));
                scheme_products.setspProductId(cursor.getString(2));
                // Adding contact to list
                spList.add(scheme_products);
            } while (cursor.moveToNext());
        }

        // return contact list
        return spList;

    }

    // Getting All User_Company_Division
    public List<User_Company_Division> getAllUser_Company_Division() {
        List<User_Company_Division> ucdList = new ArrayList<User_Company_Division>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_COMPANY_DIVISION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Company_Division user_company_division = new User_Company_Division();
                user_company_division.setID(Integer.parseInt(cursor.getString(0)));
                user_company_division.setucdUserId(cursor.getString(1));
                user_company_division.setucdCompanyId(cursor.getString(2));
                user_company_division.setucdDivisionId(cursor.getString(3));


                // Adding contact to list
                ucdList.add(user_company_division);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ucdList;
    }

    // Getting All Crops
    public List<Crops> getAllCrops() {
        List<Crops> cropsList = new ArrayList<Crops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CROPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crops crops = new Crops();
                crops.setCropMasterID(cursor.getString(1));
                crops.setCropName(cursor.getString(2));
                crops.setCropcode(cursor.getString(3));
                crops.setCropsapid(cursor.getString(4));
                crops.setCropdivisionId(cursor.getString(5));
                crops.setCropcdatetime(cursor.getString(6));
                crops.setCropudatetime(cursor.getString(7));
                // Adding contact to list
                cropsList.add(crops);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return cropsList;
    }

    // Getting AllProducts
    public List<Products_Pojo> getAllProducts() {
        List<Products_Pojo> productsList = new ArrayList<Products_Pojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products_Pojo products_pojo = new Products_Pojo();
                products_pojo.setProductMasterId(cursor.getString(1));
                products_pojo.setProductName(cursor.getString(2));
                products_pojo.setProductDescription(cursor.getString(3));
                products_pojo.setProductSapCode(cursor.getString(4));
                products_pojo.setProductcropid(cursor.getString(5));
                products_pojo.setProductcompanyid(cursor.getString(6));
                products_pojo.setProductdivisionid(cursor.getString(7));
                products_pojo.setProductregeion(cursor.getString(8));
                products_pojo.setProductprice(cursor.getString(9));
                products_pojo.setProductdiscount(cursor.getString(10));
                products_pojo.setProductfromdate(cursor.getString(11));
                products_pojo.setProducttodate(cursor.getString(12));
                products_pojo.setProductstatus(cursor.getString(13));
                products_pojo.setProductcdatetime(cursor.getString(14));
                products_pojo.setProductudatetime(cursor.getString(15));
                products_pojo.setProductImages(cursor.getString(16));
                products_pojo.setProductVideos(cursor.getString(17));
                // Adding contact to list
                productsList.add(products_pojo);
            } while (cursor.moveToNext());
        }

        // return Products list
        return productsList;
    }


    // Getting All Customers
    public List<Customers> getAllCustomers() {
        List<Customers> customerList = new ArrayList<Customers>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers customers = new Customers();
                customers.setCusMasterID(cursor.getString(1));
                customers.setCusName(cursor.getString(2));
                customers.setCuscode(cursor.getString(3));
                customers.setCusaddress(cursor.getString(4));
                customers.setCusstreet(cursor.getString(5));
                customers.setCus_city(cursor.getString(6));
                customers.setCuscountry(cursor.getString(7));
                customers.setCusregion_Id(cursor.getString(8));
                customers.setCustelephone(cursor.getString(9));
                customers.setCuscompany_Id(cursor.getString(10));
                customers.setCusstatus(cursor.getString(11));
                customers.setCuscdatetime(cursor.getString(12));
                customers.setCusudatetime(cursor.getString(13));
                // Adding contact to list
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }

    // Getting All Customer_details
    public List<Customer_Details> getAllCustomer_details() {
        List<Customer_Details> customerdetailsListList = new ArrayList<Customer_Details>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_Details customer_details = new Customer_Details();
                customer_details.setID(Integer.parseInt(cursor.getString(0)));
                customer_details.set_customer_details_master_id(cursor.getString(1));
                customer_details.set_customer_details_division_id(cursor.getString(2));
                customer_details.set_customer_details_credit_limit(cursor.getString(3));
                customer_details.set_customer_details_inside_bucket(cursor.getString(4));
                customer_details.set_customer_details_outside_bucket(cursor.getString(5));
                customer_details.set_customer_details_status(cursor.getString(6));
                customer_details.set_customer_details_created_datetime(cursor.getString(7));
                customer_details.set_customer_details_updated_datetime(cursor.getString(8));
                // Adding contact to list
                customerdetailsListList.add(customer_details);
            } while (cursor.moveToNext());
        }

        // return contact list
        return customerdetailsListList;
    }

    public List<Complaints> getAllComplaintreg(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";
        Log.e("select reg", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public void addComplaint(Complaints complaints) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_complaint = new ContentValues();
        //values_complaint.put(KEY_TABLE_COMPLAINTS_ID, complaints.getID());
        values_complaint.put(KEY_TABLE_COMPLAINT_USER_ID, complaints.get_user_id());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPANY_ID, complaints.getCompanyId());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DIVISION_ID, complaints.get_division_id());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CROP_ID, complaints.getCropid());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PRODUCT_ID, complaints.getProductid());
        values_complaint.put(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO, complaints.get_marketing_lot_number());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE, complaints.get_complaint_type());
        values_complaint.put(KEY_TABLE_COMPLAINTS_FARMER_NAME, complaints.get_farmer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CONTACT_NO, complaints.get_contact_no());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES, complaints.get_complaint_area_acres());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SOIL_TYPE, complaints.get_soil_type());
        values_complaint.put(KEY_OTHERS, complaints.get_others());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PURCHASED_QTY, complaints.get_purchased_quantity());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY, complaints.get_complaint_quantity());
        values_complaint.put(KEY_PURCHASE_DATE, complaints.get_purchase_date());
        values_complaint.put(KEY_TABLE_COMPLAINTS_BILL_NUMBER, complaints.get_bill_number());
        values_complaint.put(KEY_TABLE_COMPLAINTS_RETAILER_NAME, complaints.get_retailer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DISTRIBUTOR, complaints.get_distributor());
        values_complaint.put(KEY_TABLE_COMPLAINTS_MANDAL, complaints.get_mandal());
        values_complaint.put(KEY_TABLE_COMPLAINTS_VILAGE, complaints.get_village());
        values_complaint.put(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD, complaints.get_image());
        values_complaint.put(KEY_REGULATORY_TYPE, complaints.get_regulatory_type());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_DATE, complaints.get_sampling_date());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING, complaints.get_place_sampling());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME, complaints.get_sampling_officer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT, complaints.get_sampling_officer_contact());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMMENTS, complaints.get_comments());
        values_complaint.put(KEY_TABLE_COMPLAINTS_STATUS, complaints.get_status());
        values_complaint.put(KEY_TABLE_COMPLAINTS_REMARKS, complaints.get_remarks());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CREATED_DATETIME, complaints.get_created_datetime());
        values_complaint.put(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME, complaints.get_updated_datetime());
        values_complaint.put(KEY_TABLE_COMPLAINTS_FFMID, complaints.get_ffmid());
        // Inserting Row
        db.insert(TABLE_COMPLAINT, null, values_complaint);
//		Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    public List<Complaints> getAllComplaints(String userId) {
        List<Complaints> complaintsList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintsList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintsList;
    }

    public List<Complaints> getAllComplaintregnew(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " = 'Regulatory'" + " ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";
        Log.e("reg query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintprodnew(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " = 'product'" + " ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }


    public List<Complaints> getAllComplaint(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintsregulatory(String userID) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        //String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE "+KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " = regulatory ORDER BY "+ KEY_TABLE_COMPLAINTS_ID + " DESC" ;
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userID + "' AND LOWER(" + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + ") = 'regulatory' AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintsproducts(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL AND LOWER(" + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + ") = 'product'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                Constants.DateFormat.COMMON_DATE_TIME_FORMAT, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void deleteComplaints() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_COMPLAINT);
    }


    public void deleteFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FEEDBACK);
    }

    public void deletePayment() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PAYMENT_COLLECTION);
    }

    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACTS);
    }

    public void deleteGeo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_GEO_TRACKING);
    }

    public void deleteseviceorders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SERVICEORDER);
    }

    public void deleteserviceorderdetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SERVICEORDERDETAILS);
    }


    public void deleteDailyDiary() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DAILYDAIRY);
    }

    public void deleteCustomerbankdetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CUSTOMER_BANKDETAILS);
    }


    public void deleteCommodityPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MI_COMMODITY_PRICE);
    }

    public void deleteCropShifts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MI_CROP_SHIFTS);
    }


    // Updating approval/reject status
    public void updateApprovalOrRejectStatus(String userId, String approval_status, String approval_comments, String orderId, boolean offlineStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TABLE_SERVICEORDER_APPROVAL_STATUS, approval_status);
        contentValues.put(KEY_TABLE_SERVICEORDER_UPDATED_BY, userId);
        if (approval_comments != null && approval_comments.length() > 0)
            contentValues.put(KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS, approval_comments);

        contentValues.put(KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET, offlineStatus ? 1 : 0);

        // Inserting Row
        db.update(TABLE_SERVICEORDER, contentValues, KEY_TABLE_SERVICEORDER_FFM_ID + "=?", new String[]{String.valueOf(orderId)});
        db.close(); // Closing database connection
    }


    public ServiceOrderMaster getApproveRejectCommentData(String orderId) {


        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_BY + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS
                + " FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " =" + orderId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
            serviceOrderMasterData.setID(cursor.getInt(0));
            serviceOrderMasterData.set_approval_status(cursor.getString(1));
            serviceOrderMasterData.set_updated_by(cursor.getString(2));
            serviceOrderMasterData.set_approval_comments(cursor.getString(3));

            return serviceOrderMasterData;


        }

        // return serviceOrder Master list
        return null;

    }


    // get Approve Reject Data offline
    public List<ServiceOrderMaster> getOfflineApproveRejectData() {

        List<ServiceOrderMaster> approvalRejectOrderList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_BY + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS
                + " FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET + " =1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                serviceOrderMasterData.setID(cursor.getInt(0));
                serviceOrderMasterData.set_approval_status(cursor.getString(1));
                serviceOrderMasterData.set_updated_by(cursor.getString(2));
                serviceOrderMasterData.set_approval_comments(cursor.getString(3));

                approvalRejectOrderList.add(serviceOrderMasterData);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return approvalRejectOrderList;

    }


    public String getPlannerCompleteStatus(String date) {
        String selectQuerys = "SELECT  * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " where " + KEY_EMP_PLAN_DATE_TIME + " like '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cc = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cc != null && cc.moveToFirst()) {

            return String.valueOf(cc.getInt(16)); //The 0 is the column index, we only have 1 column, so the index is 0

        }


        return "0";
    }


    public String[] getCheckinStatus(String date) {
        String[] aa = new String[5];
        String selectQuerys = "SELECT  * FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " like '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cc = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cc != null && cc.moveToFirst()) {
            aa[0] = cc.getString(9);
            aa[1] = cc.getString(10);


            return aa; //The 0 is the column index, we only have 1 column, so the index is 0

        }


        return null;
    }

    public void updateAprrovalStatus(String sqlitId, String ffmId, String aprrovalStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EMP_APPROVAL_STATUS, aprrovalStatus);
        contentValues.put(KEY_EMP_FFM_ID, ffmId);
        db.update(TABLE_EMPLOYEE_VISIT_MANAGEMENT, contentValues, KEY_EMP_VISIT_ID + " = " + sqlitId, null);
        db.close();
    }

    // insert version control data
    public void insertVersionControlData(List<VersionControlVo> versionControlVos) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VersionControlVo versionControlVo : versionControlVos) {
            ContentValues values_regions = new ContentValues();
            values_regions.put(KEY_ID, versionControlVo.id);
            values_regions.put(VERSION_TABLE_NAME, versionControlVo.tableName);
            values_regions.put(VERSION_CODE, versionControlVo.status);
            values_regions.put(UPDATED_DATE, versionControlVo.updatedDate);

            db.insert(TABLE_VERSION_CONTROL, null, values_regions);
        }
        db.close(); // Closing database connection
    }

    public void updateVersionControlData(VersionControlVo versionControlVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_regions = new ContentValues();
        values_regions.put(VERSION_CODE, versionControlVo.status);
        values_regions.put(UPDATED_DATE, versionControlVo.updatedDate);

        db.update(TABLE_VERSION_CONTROL, values_regions, VERSION_TABLE_NAME + " = '" + versionControlVo.tableName + "'", null);

        db.close(); // Closing database connection
    }


    public List<VersionControlVo> getVersionControlList() {
        List<VersionControlVo> versionControlVoList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_VERSION_CONTROL + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            VersionControlVo versionControlVo = new VersionControlVo();
            versionControlVo.id = res.getString(0);
            versionControlVo.tableName = res.getString(1);
            versionControlVo.status = res.getString(3);

            versionControlVoList.add(versionControlVo);
            res.moveToNext();
        }

        res.close();


        return versionControlVoList;
    }


    public int deleteDataByTableName(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("deleted table: ", tableName);
        return db.delete(tableName, null, null);


    }

    public int[] deleteDataByTableName(String[] tableName) {
        int[] ids = new int[tableName.length];

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < tableName.length; i++) {
            int id = db.delete(tableName[i], null, null);
            ids[i] = id;
            Log.d("deleted table: ", tableName[i]);
        }

        return ids;
    }


    // table SMD column names


    public long insertStackMovement(StockMovementPoJo stockMovementPoJo, StockMovementDetailsPojo stockMovementDetailsPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3(stockMovementPoJo.userId, stockMovementPoJo.companyId, stockMovementPoJo.divisionId, stockMovementPoJo.customerId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_TABLE_SM_MOVEMENT_TYPE, stockMovementPoJo.movementType);
            contentValues.put(KEY_TABLE_SM_USER_ID, stockMovementPoJo.userId);
            contentValues.put(KEY_TABLE_SM_DIVISION, stockMovementPoJo.divisionId);
            contentValues.put(KEY_TABLE_SM_COMPANY_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_CUSTOMER_ID, stockMovementPoJo.customerId);
            contentValues.put(KEY_TABLE_SM_CREATED_BY, stockMovementPoJo.createdBy);
            contentValues.put(KEY_TABLE_SM_UPDATED_BY, stockMovementPoJo.updatedBy);
            contentValues.put(KEY_TABLE_SM_STATUS, stockMovementPoJo.status);
            contentValues.put(FFM_ID, stockMovementPoJo.ffmId);
            contentValues.put(KEY_TABLE_SM_CREATED_DATETIME, stockMovementPoJo.createdDatetime);
            contentValues.put(KEY_TABLE_SM_UPDATED_DATETIME, stockMovementPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_SM, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackMovementDetails(stockMovementDetailsPojo, relationId);

        return rowid;
    }

    public void insertStackMovementDetails(StockMovementDetailsPojo stockMovementDetailsPojo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TABLE_SMD_STOCK_MOVEMENT_ID, relationId);
        contentValues.put(KEY_TABLE_SMD_USER_TYPE, stockMovementDetailsPojo.userType);
        contentValues.put(KEY_TABLE_SMD_CUSTOMER_ID, stockMovementDetailsPojo.customerId);
        contentValues.put(KEY_TABLE_SMD_CROP_ID, stockMovementDetailsPojo.cropId);
        contentValues.put(KEY_TABLE_SMD_PRODUCT_ID, stockMovementDetailsPojo.productId);
        contentValues.put(KEY_TABLE_SMD_STOCK_PLACED, stockMovementDetailsPojo.stockPlaced);
        if (!stockMovementDetailsPojo.currentStock.equalsIgnoreCase("")) {
            contentValues.put(KEY_TABLE_SMD_CURRENT_STOCK, stockMovementDetailsPojo.currentStock);
        }

        contentValues.put(KEY_TABLE_SMD_PLACED_DATE, stockMovementDetailsPojo.placedDate);
        contentValues.put(KEY_TABLE_SMD_POG, stockMovementDetailsPojo.pog);
        contentValues.put(KEY_TABLE_CREATED_BY, stockMovementDetailsPojo.createdBy);
        contentValues.put(KEY_TABLE_UPDATED_BY, stockMovementDetailsPojo.updatedBy);
        contentValues.put(KEY_TABLE_SMD_CREATED_DATETIME, stockMovementDetailsPojo.createdDatetime);
        contentValues.put(KEY_TABLE_SMD_UPDATED_DATETIME, stockMovementDetailsPojo.updatedDatetime);
        contentValues.put(FFM_ID, stockMovementDetailsPojo.ffmId);


        // Inserting Row
        db.insert(TABLE_SMD, null, contentValues);
        db.close(); // Closing database connection
    }

    public long insertStackMovementForRetailerDetails(StockMovementPoJo stockMovementPoJo, StockMovementRetailerDetails stockMovementDetailsPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3(stockMovementPoJo.userId, stockMovementPoJo.companyId, stockMovementPoJo.divisionId, stockMovementPoJo.customerId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_TABLE_SM_MOVEMENT_TYPE, stockMovementPoJo.movementType);
            contentValues.put(KEY_TABLE_SM_USER_ID, stockMovementPoJo.userId);
            contentValues.put(KEY_TABLE_SM_DIVISION, stockMovementPoJo.divisionId);
            contentValues.put(KEY_TABLE_SM_COMPANY_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_CUSTOMER_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_TABLE_SM_CREATED_BY, stockMovementPoJo.createdBy);
            contentValues.put(KEY_TABLE_SM_UPDATED_BY, stockMovementPoJo.updatedBy);
            contentValues.put(KEY_TABLE_SM_STATUS, stockMovementPoJo.status);
            contentValues.put(FFM_ID, stockMovementPoJo.ffmId);
            contentValues.put(KEY_TABLE_SM_CREATED_DATETIME, stockMovementPoJo.createdDatetime);
            contentValues.put(KEY_TABLE_SM_UPDATED_DATETIME, stockMovementPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_SM, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackMovementRetailerDetails(stockMovementDetailsPojo, relationId);

        return rowid;
    }

    public void insertStackMovementRetailerDetails(StockMovementRetailerDetails stockMovementDetailsPojo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TABLE_SMD_STOCK_MOVEMENT_ID, relationId);
        contentValues.put(KEY_TABLE_SMD_USER_TYPE, stockMovementDetailsPojo.userType);
        contentValues.put(KEY_TABLE_SMD_CROP_ID, stockMovementDetailsPojo.cropId);
        contentValues.put(KEY_TABLE_SMD_PRODUCT_ID, stockMovementDetailsPojo.productId);
        contentValues.put(KEY_TABLE_SMD_STOCK_PLACED, stockMovementDetailsPojo.stockPlaced);
        if (!stockMovementDetailsPojo.currentStock.equalsIgnoreCase("")) {
            contentValues.put(KEY_TABLE_SMD_CURRENT_STOCK, stockMovementDetailsPojo.currentStock);
        }

        contentValues.put(KEY_TABLE_SMD_PLACED_DATE, stockMovementDetailsPojo.placedDate);
        contentValues.put(KEY_TABLE_SMD_POG, stockMovementDetailsPojo.pog);
        contentValues.put(KEY_TABLE_CREATED_BY, stockMovementDetailsPojo.createdBy);
        contentValues.put(KEY_TABLE_SMD_CREATED_DATETIME, stockMovementDetailsPojo.createdDatetime);
        contentValues.put(KEY_TABLE_SMD_UPDATED_DATETIME, stockMovementDetailsPojo.updatedDatetime);
        contentValues.put(KEY_TABLE_SMD_RETAILER_USER_ID, stockMovementDetailsPojo.userId);
        contentValues.put(KEY_TABLE_SMD_RETAILER_ID, stockMovementDetailsPojo.retailerId);
        contentValues.put(KEY_TABLE_SMD_RETAILER_VERIFIED, stockMovementDetailsPojo.verified);
        contentValues.put(KEY_TABLE_SMD_RETAILER_VERIFIED_BY, stockMovementDetailsPojo.verifiedBy);
        contentValues.put(FFM_ID, stockMovementDetailsPojo.ffmId);


        // Inserting Row
        db.insert(TABLE_STOCK_MOVEMENT_RETAILER_DETAILS, null, contentValues);
        db.close(); // Closing database connection
    }


    public long insertStackReturn(StockReturnPoJo stockReturnPoJo, StockReturnDetailsPoJo stockReturnDetailsPoJo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3FromStockReturn(stockReturnPoJo.userId, stockReturnPoJo.companyId, stockReturnPoJo.divisionId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();


            contentValues.put(KEY_CUSTOMER_ID, stockReturnPoJo.customerId);
            contentValues.put(KEY_USER_ID, stockReturnPoJo.userId);
            contentValues.put(KEY_DIVISION_ID, stockReturnPoJo.divisionId);
            contentValues.put(KEY_COMPANY_ID, stockReturnPoJo.companyId);
            contentValues.put(KEY_CREATED_BY, stockReturnPoJo.createdBy);
            contentValues.put(KEY_UPDATED_BY, stockReturnPoJo.updatedBy);
            contentValues.put(KEY_FFMID, stockReturnPoJo.ffmId);
            contentValues.put(KEY_CREATED_DATETIME, stockReturnPoJo.createdDatetime);
            contentValues.put(KEY_UPDATED_DATETIME, stockReturnPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_STOCK_RETURNS, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackReturnDetails(stockReturnDetailsPoJo, relationId);

        return rowid;
    }

    public void insertStackReturnDetails(StockReturnDetailsPoJo stockReturnDetailsPoJo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_STOCK_RETURNS_ID, relationId);
        contentValues.put(KEY_CROP_ID, stockReturnDetailsPoJo.cropId);
        contentValues.put(KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID, stockReturnDetailsPoJo.productId);
        contentValues.put(KEY_QUANTITY, stockReturnDetailsPoJo.quantity);
        contentValues.put(KEY_FFMID, stockReturnDetailsPoJo.ffmId);


        // Inserting Row
        db.insert(TABLE_STOCK_RETURNS_DETAILS, null, contentValues);
        db.close(); // Closing database connection
    }

    public ArrayList<StockMovementPoJo> getOfflineStockPlacementUnSynced(int status) {
        ArrayList<StockMovementPoJo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_SM + " where " + KEY_TABLE_SM_STATUS + " = " + status;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
                stockMovementPoJo.stockMovementId = cursor.getInt(0);
                stockMovementPoJo.movementType = cursor.getInt(1);
                stockMovementPoJo.userId = cursor.getInt(2);
                stockMovementPoJo.companyId = cursor.getInt(3);
                stockMovementPoJo.divisionId = cursor.getInt(4);
                stockMovementPoJo.status = cursor.getInt(5);
                stockMovementPoJo.createdBy = cursor.getString(6);
                stockMovementPoJo.updatedBy = cursor.getString(7);
                stockMovementPoJo.ffmId = cursor.getInt(8);
                stockMovementPoJo.createdDatetime = cursor.getString(9);
                stockMovementPoJo.updatedDatetime = cursor.getString(10);


                list.add(stockMovementPoJo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockPlacementPopupPojo> getOfflineStockPlacementListById(int relationId, int cropId, int productId, int customerId) {
        ArrayList<StockPlacementPopupPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_SMD + " where " + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " = " + relationId + " and " + KEY_TABLE_SMD_STOCK_PLACED + " IS NOT 0 and " + KEY_TABLE_SMD_CROP_ID + " = " + cropId + " and " + KEY_TABLE_SMD_PRODUCT_ID + " = " + productId + " and " + KEY_TABLE_SMD_CUSTOMER_ID + " = " + customerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockPlacementPopupPojo stockPlacementPopupPojo = new StockPlacementPopupPojo();
                stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(0);
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(1);
                stockPlacementPopupPojo.userType = cursor.getString(2);
                stockPlacementPopupPojo.customerId = cursor.getInt(3);
                stockPlacementPopupPojo.cropId = cursor.getInt(4);
                stockPlacementPopupPojo.productId = cursor.getInt(5);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(6);
                stockPlacementPopupPojo.currentStock = cursor.getString(7);
                stockPlacementPopupPojo.placedDate = cursor.getString(8);
                stockPlacementPopupPojo.pog = cursor.getString(9);
                stockPlacementPopupPojo.createdBy = cursor.getString(10);
                stockPlacementPopupPojo.updatedBy = cursor.getString(11);
                stockPlacementPopupPojo.createdDatetime = cursor.getString(12);
                stockPlacementPopupPojo.updatedDatetime = cursor.getString(13);
                stockPlacementPopupPojo.ffmId = cursor.getInt(14);

                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockPlacementPopupPojo> getOfflineSMRDStockPlacementListById(int relationId, int cropId, int productId, String retailerId) {
        ArrayList<StockPlacementPopupPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " where " + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " = " + relationId + " and " + KEY_TABLE_SMD_STOCK_PLACED + " IS NOT 0 and " + KEY_TABLE_SMD_CROP_ID + " = " + cropId + " and " + KEY_TABLE_SMD_PRODUCT_ID + " = " + productId + " and " + KEY_TABLE_RETAILER_ID + " = " + retailerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockPlacementPopupPojo stockPlacementPopupPojo = new StockPlacementPopupPojo();
                stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(0);
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(1);
                stockPlacementPopupPojo.userType = cursor.getString(2);
                stockPlacementPopupPojo.cropId = cursor.getInt(3);
                stockPlacementPopupPojo.productId = cursor.getInt(4);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(5);
                stockPlacementPopupPojo.currentStock = cursor.getString(6);
                stockPlacementPopupPojo.placedDate = cursor.getString(7);
                stockPlacementPopupPojo.pog = cursor.getString(8);
                stockPlacementPopupPojo.createdBy = cursor.getString(9);
                stockPlacementPopupPojo.updatedBy = cursor.getString(10);
                stockPlacementPopupPojo.createdDatetime = cursor.getString(11);
                stockPlacementPopupPojo.updatedDatetime = cursor.getString(12);
                stockPlacementPopupPojo.ffmId = cursor.getInt(17);

                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementUnSynedPojo> getOfflineStockPlacementListUnSyncData1() {
        ArrayList<StockMovementUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,stock_movement.customer_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_detail_id from stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id  where stock_movement_detail.ffm_id=0 group by (stock_movement_detail.stock_movement_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementUnSynedPojo stockPlacementPopupPojo = new StockMovementUnSynedPojo();
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(4);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<StockReturnUnSynedPojo> getOfflineStockReturnListUnSyncData1() {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.ffmid=0 group by (stock_returns_details.stock_returns_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public ArrayList<StockReturnUnSynedPojo> getOfflineStockReturnsListUnSyncData(int stockReturnId) {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id,quantity from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.ffmid=0 and stock_returns_details.stock_returns_id=" + stockReturnId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.customerId = cursor.getInt(2);
                stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);
                stockPlacementPopupPojo.cropId = cursor.getInt(4);
                stockPlacementPopupPojo.productId = cursor.getInt(5);
                stockPlacementPopupPojo.stockReturnsDetailsId = cursor.getInt(6);
                stockPlacementPopupPojo.quantity = cursor.getString(7);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockReturnUnSynedPojo> getStockReturnsData(int stockReturnId, String product_id) {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id,quantity from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.stock_returns_id=" + stockReturnId + " and product_id=" + product_id + " order by stock_returns_details_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.customerId = cursor.getInt(2);
                stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);
                stockPlacementPopupPojo.cropId = cursor.getInt(4);
                stockPlacementPopupPojo.productId = cursor.getInt(5);
                stockPlacementPopupPojo.stockReturnsDetailsId = cursor.getInt(6);
                stockPlacementPopupPojo.quantity = cursor.getString(7);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementRetailerDetails> getOfflineStockPlacementSMRDListUnSyncData1() {
        ArrayList<StockMovementRetailerDetails> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_retailer_id from stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id  where stock_movement_retailer_details.ffm_id=0 group by (stock_movement_retailer_details.stock_movement_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementRetailerDetails stockPlacementPopupPojo = new StockMovementRetailerDetails();
                stockPlacementPopupPojo.stockMovementId = cursor.getString(3);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementRetailerDetails> getOfflineStockPlacementSMRDListUnSyncData(int stockMovementId) {
        ArrayList<StockMovementRetailerDetails> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_retailer_id,user_type,retailer_id,customer_id from stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id  where stock_movement_retailer_details.ffm_id=0 and stock_movement_retailer_details.stock_movement_id=" + stockMovementId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementRetailerDetails stockPlacementPopupPojo = new StockMovementRetailerDetails();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.placedDate = cursor.getString(2);
                stockPlacementPopupPojo.stockMovementId = cursor.getString(3);
                stockPlacementPopupPojo.cropId = cursor.getString(4);
                stockPlacementPopupPojo.productId = cursor.getString(5);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(6);
                stockPlacementPopupPojo.currentStock = cursor.getString(7);
                stockPlacementPopupPojo.stockMovementRetailerId = cursor.getString(8);
                stockPlacementPopupPojo.userType = cursor.getString(9);
                stockPlacementPopupPojo.retailerId = cursor.getString(10);
                stockPlacementPopupPojo.customerId = cursor.getInt(11);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockMovementUnSynedPojo> getOfflineStockPlacementListUnSyncData(int stockMovementId) {
        ArrayList<StockMovementUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,stock_movement.customer_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_detail_id,user_type from stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id  where stock_movement_detail.ffm_id=0 and stock_movement_detail.stock_movement_id=" + stockMovementId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementUnSynedPojo stockPlacementPopupPojo = new StockMovementUnSynedPojo();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.customerId = cursor.getInt(2);
                stockPlacementPopupPojo.placedDate = cursor.getString(3);
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(4);
                stockPlacementPopupPojo.cropId = cursor.getInt(5);
                stockPlacementPopupPojo.productId = cursor.getInt(6);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(7);
                stockPlacementPopupPojo.currentStock = cursor.getString(8);
                stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(9);
                stockPlacementPopupPojo.userType = cursor.getString(10);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementFirstListPojo> getOfflineStockPlacementList(String user_id, String company_id, String division_id, String customer_id) {
        ArrayList<StockMovementFirstListPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced,stock_movement_detail.product_id,brand_name,crop_id,stock_movement_detail.stock_movement_id,stock_movement_detail.customer_id FROM stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id left join products on products.product_id=stock_movement_detail.product_id where user_id=" + user_id + " and stock_movement.company_id=" + company_id + " and stock_movement.customer_id =" + customer_id + " and stock_movement.division_id=" + division_id + " group by (stock_movement_detail.product_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementFirstListPojo stockMovementFirstListPojo = new StockMovementFirstListPojo();
                /*String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id)-
                        (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)+
                                getstockMovementDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5))));
*/
                int pog = 0;
               /* if (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)==0){
                    pog="0";
                }else{
                    pog=String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id) -
                            (getstockMovementRetailerDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                                    getstockMovementDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));
                }*/

                List<MappedRetailerPojo> mappedRetailerData = getMappedRetailerWithDistributorAndProduct(cursor.getString(4), String.valueOf(cursor.getInt(1)), String.valueOf(cursor.getInt(3)));
                for (MappedRetailerPojo mappedRetailerPojo : mappedRetailerData) {
                    pog = pog + Integer.parseInt(mappedRetailerPojo.pog);
                }
                stockMovementFirstListPojo.stockPlaced = cursor.getString(0);
                stockMovementFirstListPojo.currentStock = getCurrentStock(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4));
                stockMovementFirstListPojo.productId = cursor.getInt(1);
                stockMovementFirstListPojo.brandName = cursor.getInt(2);
                stockMovementFirstListPojo.cropId = cursor.getInt(3);
                stockMovementFirstListPojo.pog = String.valueOf(pog);
                stockMovementFirstListPojo.stockMovementId = cursor.getString(4);


                list.add(stockMovementFirstListPojo);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }


    public String getCurrentStock(int product_id, int crop_id, int stock_movement_id) {
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }

    public String getCurrentStockSMRD(int product_id, int crop_id, int stock_movement_id, String retailer_id) {
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and retailer_id =" + retailer_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }


    public int getCombinationOf3(int user_id, int company_id, int division_id, int customerId) {
        String selectQuerys = "SELECT " + KEY_TABLE_SM_ID + " FROM " + TABLE_SM + " where " + KEY_TABLE_SM_USER_ID + " = " + user_id + " AND " + KEY_TABLE_SM_COMPANY_ID + " = " + company_id + " AND " + KEY_TABLE_SM_DIVISION + " = " + division_id + " AND " + KEY_CUSTOMER_ID + " = " + customerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getInt(0);

        }


        return 0;
    }

    public int getCombinationOf3FromStockReturn(int user_id, int company_id, int division_id) {
        String selectQuerys = "SELECT " + KEY_STOCK_RETURNS_ID + " FROM " + TABLE_STOCK_RETURNS + " where " + KEY_USER_ID + " = " + user_id + " AND " + KEY_COMPANY_ID + " = " + company_id + " AND " + KEY_DIVISION_ID + " = " + division_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            int stockRetunId = cursor.getInt(0);
            cursor.close();
            return stockRetunId;

        }

        cursor.close();
        return 0;
    }

    public int getCombinationOf3SMRD(int user_id, int company_id, int division_id) {
        String selectQuerys = "SELECT " + KEY_TABLE_SM_ID + " FROM " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " where " + KEY_TABLE_SM_USER_ID + " = " + user_id + " AND " + KEY_TABLE_SM_COMPANY_ID + " = " + company_id + " AND " + KEY_TABLE_SM_DIVISION + " = " + division_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getInt(0);

        }


        return 0;
    }


    public long getSumOfStockPlacement(int product_id, int crop_id, int stock_movement_id, String customer_id) {
        long sum = 0;
        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced FROM stock_movement_detail  where product_id=" + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getString(0) == null) {
                sum = 0;
            } else {
                sum = Long.parseLong(cursor.getString(0));
            }
            return sum;
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementRetailerDetails(int product_id, int crop_id, int stock_movement_id, String user_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + user_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementDetails(int product_id, int crop_id, int stock_movement_id, int customer_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }


    public void addRetailers(Retailer retailer) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!isTINnoExist(retailer.get_ret_tin_no())) {
            ContentValues values_retailers = new ContentValues();
            values_retailers.put(KEY_TABLE_RETAILER_MASTER_ID, retailer.get_ret_masterid());
            values_retailers.put(KEY_TABLE_RETAILER_NAME, retailer.get_ret_name());
            values_retailers.put(KEY_TABLE_RETAILER_TIN_NO, retailer.get_ret_tin_no());
            values_retailers.put(KEY_TABLE_RETAILER_ADDRESS, retailer.get_ret_address());
            values_retailers.put(KEY_TABLE_RETAILER_PHONE, retailer.get_ret_phone());
            values_retailers.put(KEY_TABLE_RETAILER_MOBILE, retailer.get_ret_mobile());
            values_retailers.put(KEY_TABLE_RETAILER_EMAIL_ID, retailer.get_email());
            values_retailers.put(KEY_TABLE_RETAILER_DISTRIBUTOR_ID, retailer.get_ret_dist_id());
            values_retailers.put(KEY_TABLE_RETAILER_SAP_CODE, retailer.get_ret_dist_sap_code());
            values_retailers.put(KEY_TABLE_RETAILER_STATUS, retailer.get_ret_status());
            values_retailers.put(KEY_TABLE_RETAILER_CREATED_DATETIME, retailer.get_ret_cdatetime());
            values_retailers.put(KEY_TABLE_RETAILER_UPDATED_DATETIME, retailer.get_ret_udatetime());
            values_retailers.put(KEY_TABLE_RETAILER_FFMID, retailer.get_ffmid());

            // Inserting Row
            db.insert(TABLE_RETAILER, null, values_retailers);

        }else {

        }
        db.close(); // Closing database connection

    }

    public List<Retailer> getAllnullRetailer(String custid) {
        List<Retailer> customerList = new ArrayList<Retailer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '" + custid + "' AND " + KEY_TABLE_RETAILER_FFMID + " IS NULL";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer customers = new Retailer();
                customers.setID(Integer.parseInt(cursor.getString(0)));
                customers.set_ret_masterid(cursor.getString(1));
                customers.set_ret_name(cursor.getString(2));
                customers.set_ret_tin_no(cursor.getString(3));
                customers.set_ret_address(cursor.getString(4));
                customers.set_ret_phone(cursor.getString(5));
                customers.set_ret_mobile(cursor.getString(6));
                customers.set_email(cursor.getString(7));
                customers.set_ret_dist_id(cursor.getString(8));
                customers.set_ret_dist_sap_code(cursor.getString(9));
                customers.set_ret_status(cursor.getString(10));
                customers.set_ret_cdatetime(cursor.getString(11));
                customers.set_ret_udatetime(cursor.getString(12));
                customers.set_ffmid(cursor.getString(13));
                // Adding contact to list
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }


    public List<Retailer> getAllRetailer(String id) {
        List<Retailer> customerList = new ArrayList<Retailer>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        String selectQuery = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM " + TABLE_RETAILER + " left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id=" + id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer customers = new Retailer();
                // customers.setID(Integer.parseInt(cursor.getString(0)));
                //   customers.set_ret_masterid(cursor.getString(1));
                customers.set_ret_name(cursor.getString(0));
                customers.set_ret_address(cursor.getString(2));
                customers.set_ret_tin_no(cursor.getString(1));
                customers.set_ret_phone(cursor.getString(3));
                customers.set_ret_mobile(cursor.getString(4));
                customers.set_email(cursor.getString(5));
                //  customers.set_ret_dist_id(cursor.getString(8));
                //  customers.set_ret_dist_sap_code(cursor.getString(9));
                //  customers.set_ret_status(cursor.getString(10));
                //  customers.set_ret_cdatetime(cursor.getString(11));
                //  customers.set_ret_udatetime(cursor.getString(12));
                customers.set_ffmid(cursor.getString(6));
                // Adding contact to list
               /* Log.e("Result","ID"+cursor.getString(0)+"ret_master_id"+cursor.getString(1)+"retname"+cursor.getString(2)
                        +"rettinno"+cursor.getString(3)+"retaddress"+cursor.getString(4)


                        +"retphone"+cursor.getString(5)
                        +"retmobile"+cursor.getString(6)
                        +"retemail"+cursor.getString(7)
                        +"retdistid"+cursor.getString(8)
                        +"retsapcode"+cursor.getString(9)
                        +"retstatus"+cursor.getString(10)
                        +"retcdate"+cursor.getString(11)
                        +"retudate"+cursor.getString(12));*/
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }

    public boolean isTINnoExist(String tin_no) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_TIN_NO + " = '" + tin_no + "'";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }

        // return DIVISIONS list
        return false;
    }

    public int getSqlPrimaryKeyByFFMID(String ffm_id) {

        // Select All Query
        String selectQuery = "SELECT  id FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_FFMID + " = '" + ffm_id + "'";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        // return DIVISIONS list
        return 0;
    }


    public List<RetailersNamePojo> getAllRetailerByDistributorId(String customer_id, String userId, String company_id, String distributorId) {
        List<RetailersNamePojo> customerList = new ArrayList<RetailersNamePojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT retailers.retailer_id,retailer_name,retailer_tin_no FROM distributor_retailer left  join retailers on retailers.retailer_id=distributor_retailer.retailer_id  where  distributor_retailer.distributor_id=" + distributorId;


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RetailersNamePojo retailersNamePojo = new RetailersNamePojo();

                retailersNamePojo.retailerId = cursor.getString(0);
                retailersNamePojo.retailerName = cursor.getString(1);
                retailersNamePojo.retailerTinNo = cursor.getString(2);
                retailersNamePojo.stockMovementFirstListPojo = getOfflineStockPlacementRetailerList(userId, company_id, customer_id);


                customerList.add(retailersNamePojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return customerList;
    }

    public List<MappedRetailerPojo> getMappedRetailerWithDistributorAndProduct(String stock_movement_id, String product_id, String crop_id) {
        List<MappedRetailerPojo> mappedRetailerPojoList = new ArrayList<MappedRetailerPojo>();
        // Select All Query
        String selectQuery = "SELECT retailer_name, retailer_tin_no, sum(stock_placed)stock_placed, sum(current_stock)current_stock, stock_movement_retailer_id,stock_movement_retailer_details.retailer_id FROM stock_movement_retailer_details LEFT JOIN retailers ON retailers.retailer_id=stock_movement_retailer_details.retailer_id WHERE stock_movement_id = " + stock_movement_id + " and product_id = " + product_id + " GROUP BY stock_movement_retailer_details.retailer_id";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MappedRetailerPojo mappedRetailerPojo = new MappedRetailerPojo();

                String currentStock = getCurrentStockSMRD(Integer.valueOf(product_id), Integer.parseInt(crop_id), Integer.valueOf(stock_movement_id), cursor.getString(5));
                mappedRetailerPojo.retailerName = cursor.getString(0);
                mappedRetailerPojo.retailerTinNo = cursor.getString(1);
                mappedRetailerPojo.stockPlaced = cursor.getString(2);
                mappedRetailerPojo.currentStock = currentStock;
                mappedRetailerPojo.stockMovementRetailerId = cursor.getString(4);
                mappedRetailerPojo.retailerId = cursor.getString(5);
                mappedRetailerPojo.pog = String.valueOf(Integer.parseInt(cursor.getString(2)) - Integer.parseInt(currentStock));


                mappedRetailerPojoList.add(mappedRetailerPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return mappedRetailerPojoList;
    }


    public String getPOGMappedRetailerWithDistributorAndProduct(String stock_movement_id, String product_id) {
        // List<MappedRetailerPojo> mappedRetailerPojoList = new ArrayList<MappedRetailerPojo>();
        // Select All Query
        String selectQuery = "SELECT (sum(stock_placed)- sum(current_stock)) as pog, sum(stock_placed)stock_placed, sum(current_stock)current_stock, stock_movement_retailer_id,stock_movement_retailer_details.retailer_id FROM stock_movement_retailer_details LEFT JOIN retailers ON retailers.retailer_id=stock_movement_retailer_details.retailer_id WHERE stock_movement_id = " + stock_movement_id + " and product_id = " + product_id;


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            return cursor.getString(0);


        }

        // return DIVISIONS list
        cursor.close();
        return "0";
    }

    public List<MappedRetailerWithDistributorPojo> getMappedRetailerWithDistributor(String customer_id, String retailerIds) {
        List<MappedRetailerWithDistributorPojo> mappedRetailerWithDistributorPojo = new ArrayList<MappedRetailerWithDistributorPojo>();
        // Select All Query
        String selectQuery = "select retailers.retailer_id,retailers.retailer_name,distributor_retailer.distributor_id from distributor_retailer left join  retailers on retailers.id=distributor_retailer.retailer_id where distributor_retailer.distributor_id=" + customer_id + " and retailers.retailer_id not in (" + retailerIds + ")";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MappedRetailerWithDistributorPojo mappedRetailerPojo = new MappedRetailerWithDistributorPojo();

                mappedRetailerPojo.retailerId = cursor.getString(0);
                mappedRetailerPojo.retailerName = cursor.getString(1);
                mappedRetailerPojo.distributorId = cursor.getString(2);
               System.out.println("ret id"+cursor.getString(0)+"ret name"+cursor.getString(1)+"dist id"+cursor.getString(2));

                mappedRetailerWithDistributorPojo.add(mappedRetailerPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return mappedRetailerWithDistributorPojo;
    }


    public List<RetailersNamePojo> searchTinNoIsAvailable(String tin_no) {
        List<RetailersNamePojo> customerList = new ArrayList<RetailersNamePojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT * FROM retailers where retailer_tin_no = '" + tin_no + "'";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RetailersNamePojo retailersNamePojo = new RetailersNamePojo();

                retailersNamePojo.sqliteId = cursor.getString(0);
                retailersNamePojo.retailerId = cursor.getString(1);
                retailersNamePojo.retailerName = cursor.getString(2);
                retailersNamePojo.retailerTinNo = cursor.getString(3);
                retailersNamePojo.address = cursor.getString(4);
                retailersNamePojo.phoneNo = cursor.getString(5);
                retailersNamePojo.mobileNo = cursor.getString(6);
                retailersNamePojo.email = cursor.getString(7);
                retailersNamePojo.distributorId = cursor.getString(8);


                customerList.add(retailersNamePojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return customerList;
    }

    public boolean isDistributorRetailerMapped(String retailer_id,String distributor_id) {

        String selectQuery = "SELECT * FROM distributor_retailer where retailer_id = '" + retailer_id + "' and distributor_id='" + distributor_id + "'";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            cursor.close();
            return true;
        }

        // return DIVISIONS list
        cursor.close();
        return false;
    }


    public void insertDistributorRetailers(SQLiteDatabase db, DistributorsRetailerPojo distributorsRetailerPojo) {
        //SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(KEY_TABLE_DISTRIBUTOR_ID, distributorsRetailerPojo.distributorId);
        values_retailers.put(KEY_TABLE_RETAILER_ID, distributorsRetailerPojo.retailerId);

        // Inserting Row
        db.insert(TABLE_DISTRIBUTOR_RETAILER, null, values_retailers);

        //  db.close(); // Closing database connection
    }

    public void insertDistributorRetailers(DistributorsRetailerPojo distributorsRetailerPojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(KEY_TABLE_DISTRIBUTOR_ID, distributorsRetailerPojo.distributorId);
        values_retailers.put(KEY_TABLE_RETAILER_ID, distributorsRetailerPojo.retailerId);

        // Inserting Row
        db.insert(TABLE_DISTRIBUTOR_RETAILER, null, values_retailers);

        db.close(); // Closing database connection
    }


    public StockMovementFirstListPojo getOfflineStockPlacementRetailerList(String user_id, String company_id, String division_id) {
        StockMovementFirstListPojo stockMovementFirstListPojo = null;
        // String selectQuerys = "SELECT sum(stock_placed)sumstockpalced,stock_movement_detail.product_id,brand_name,crop_id,stock_movement_detail.stock_movement_id,stock_movement_detail.customer_id FROM stock_movement_detail_retailer left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id left join products on products.product_id=stock_movement_detail.product_id where user_id="+ user_id+" and stock_movement.company_id="+company_id+" and stock_movement.division_id="+division_id+" group by (stock_movement_detail.product_id)";
        String selectQuerys = "SELECT stock_movement_retailer_details.retailer_id, retailer_tin_no,sum(stock_placed)sumstockpalced,stock_movement_retailer_details.product_id,brand_name,products.product_crop_id,stock_movement_retailer_details.stock_movement_id FROM stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id left join products on products.product_id=stock_movement_retailer_details.product_id left join retailers on retailers.retailer_id=stock_movement_retailer_details.retailer_id where stock_movement_retailer_details.user_id=" + user_id + " and stock_movement.company_id=" + company_id + " and stock_movement.division_id= " + division_id + " group by (stock_movement_retailer_details.product_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            stockMovementFirstListPojo = new StockMovementFirstListPojo();

            String.valueOf(getSumOfStockPlacementRetailer(cursor.getInt(1)) -
                    (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                            getstockMovementDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));

            String pog;
            if (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) == 0) {
                pog = "0";
            } else {
                pog = String.valueOf(getSumOfStockPlacementRetailer(cursor.getInt(1)) -
                        (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                                getstockMovementDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));
            }

            stockMovementFirstListPojo.stockPlaced = cursor.getString(0);
            stockMovementFirstListPojo.currentStock = getCurrentStockRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4));
            stockMovementFirstListPojo.productId = cursor.getInt(1);
            stockMovementFirstListPojo.brandName = cursor.getInt(2);
            stockMovementFirstListPojo.cropId = cursor.getInt(3);
            stockMovementFirstListPojo.pog = pog;


        }

        cursor.close();
        return stockMovementFirstListPojo;
    }


    public long getSumOfStockPlacementRetailer(int product_id) {
        long sum = 0;
        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced FROM stock_movement_detail  where product_id=" + product_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementRetailerDetailsRetailer(int product_id, int crop_id, int stock_movement_id, String user_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + user_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementDetailsRetailer(int product_id, int crop_id, int stock_movement_id, int customer_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public String getCurrentStockRetailer(int product_id, int crop_id, int stock_movement_id) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + userId + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }


    public String getTokenAmount(String customer_id, String division_id, int year, String payment_type) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "select sum(total_amount)total_amount from payment_collection  where  customer_id= " + customer_id + " and division_id=" + division_id + " and payment_datetime like '" + year + "%' and payment_type like '" + payment_type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getString(0);
        }
        return "0";
    }

    public String getSumOfOrderAmount(String customer_id, String division_id) {
        String selectQuerys = "SELECT sum(SD.order_price) as sum_order FROM service_order AS SO LEFT JOIN service_order_details AS SD ON SD.service_relation_id = SO.service_id where SO.user_id = " + userId + " and division_id=" + division_id + " AND SO.customer_id = " + customer_id + " AND SO.order_type=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return String.valueOf(cursor.getDouble(0));
        }
        return "0";
    }

    public String getSumOfOrderInAmount(String userId, String customer_id, String division_id) {
        double sum = 0;
        String selectQuery = " SELECT "
                + "SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE
                + " FROM " + TABLE_SERVICEORDER
                + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                + " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD."
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID
                + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                + " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID
                + " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID
                + " LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                + " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id" +
                "  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId
                + " AND  SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " = " + customer_id + " AND SO."
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1 AND SO.division_id=" + division_id + " group by " + KEY_TABLE_SERVICEORDER_ORDERDATE + "," + KEY_PRODUCT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            do {
                sum = sum + cursor.getDouble(0);


            } while (cursor.moveToNext());


            return String.valueOf(sum);
        }
        return "0";
    }

    public void insertGrade(SQLiteDatabase db, GradePojo gradePojo) {
        //SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(GRADE_ID, gradePojo.gradeId);
        values_retailers.put(GRADE_NAME, gradePojo.gradeName);
        values_retailers.put(PRICE_PER_KM, gradePojo.pricePerKm);

        // Inserting Row
        db.insert(TABLE_GRADE, null, values_retailers);

        //  db.close(); // Closing database connection
    }


    public String getTravelAllowance(String user_id) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "SELECT grade.grade_id,grade.price_per_km from users join grade on grade.grade_id = users.grade where users.user_id= " + user_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getString(1);
        }
        return "0";
    }

    public List<BankDetailsListPojo> getBankDetails(String customer_id) {
        List<BankDetailsListPojo> bankDetailsListPojos = new ArrayList<BankDetailsListPojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER_BANKDETAILS + " where customer_id = '" + customer_id + "'";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BankDetailsListPojo bankDetailsListPojo = new BankDetailsListPojo();

                bankDetailsListPojo.bankName = cursor.getString(5);
                bankDetailsListPojo.accountNo = cursor.getString(3);
                bankDetailsListPojo.ifscCode = cursor.getString(7);


                bankDetailsListPojos.add(bankDetailsListPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return bankDetailsListPojos;
    }


    public String getPricePerUnitByProductId(String productId) {
        String price = "0";
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "'";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            float value = (Float.parseFloat(cursor.getString(2)) * Float.parseFloat(cursor.getString(3))) / 100;
            float value1 = Float.parseFloat(cursor.getString(2)) - value;
            price = String.valueOf(value1);
        }

        // return DIVISIONS list
        cursor.close();

        return price;
    }

    public String getPricePerUnitByProductId(String productId,String regionId) {
        String price = "0";
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "' and region_id = '"+regionId+ "' order by product_ids desc limit 1";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            float value1 = Float.parseFloat(cursor.getString(2)) ;
            price = String.valueOf(value1);
        }else {
            String selectQuery5 = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "'";
            Cursor cursor5 = db.rawQuery(selectQuery5, null);
            if (cursor5.moveToFirst()) {
                float productPrice = Float.parseFloat(cursor5.getString(2));
                price = String.valueOf(productPrice);
            }
        }

        cursor.close();

        return price;
    }



    private class Async_Logout extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + Common.getUserIdFromSP(context) );
                Request request = new Request.Builder()
                        .url(Constants.URL_LOGOUT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                     Log.e("Logout","Logged out success..");
                    } else {
                        Log.e("Logout","Logged out error..");
                      //  Toast.makeText(getApplicationContext(), jsonobject.getString("msg"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }
}
