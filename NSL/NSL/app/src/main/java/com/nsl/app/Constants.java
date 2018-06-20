package com.nsl.app;

/**
 * Created by Praveen on 12/5/2016.
 */
public class Constants {
    //base url for retofit
//    public static final String BASE_URL                         = "http://107.180.69.138/nuziveedu/Api";
//    public static final String URL_NSL_MAIN                         = "http://107.180.69.138/nuziveedu/Api/";

//    public static final String BASE_URL                         = "http://107.180.69.138/pegasyssoft/Api";
//    public static final String URL_NSL_MAIN                         = "http://107.180.69.138/pegasyssoft/Api/";



    public static final String BASE_URL                         = "http://promocodesndeals.com/nsl/Authentication/index";
    public static final String URL_NSL_MAIN                         = "http://promocodesndeals.com/nsl/Authentication/index";


//    public static final String BASE_URL = "http://nslbeejtantra.com/Api";
//    public static final String URL_NSL_MAIN = "http://nslbeejtantra.com/Api/";
    public static final String BASE_URL_SHECHES_IMAGE = "http://nslbeejtantra.com/uploads/schemas/";


    public static final String URL_TABLE_UPDATE = URL_NSL_MAIN + "updatetable";
    public static final String URL_LOGIN = URL_NSL_MAIN + "login";
    public static final String URL_CHANGE_PASSWORD = URL_NSL_MAIN + "update_password";
    public static final String URL_CHECKINOUT = "geo_check_inout";
    public static final String URL_ROUTEPATH_UPDATE_INTERVAL = "geo_updatepath";
    public static final String URL_INSERT_ADVANCEBOOKING = "advbooking";
    public static final String URL_MASTER_SERVICE_ORDER = URL_NSL_MAIN + "service_orderadv?";
    public static final String URL_INSERTING_ORDERINDENT = URL_NSL_MAIN + "orderindent";
    public static final String URL_INSERTING_FEEDBACK = URL_NSL_MAIN + "feedback";
    public static final String URL_INSERTING_COMPLAINTS = URL_NSL_MAIN + "complaint";
    public static final String URL_INSERTING_EMP_VISIT_MANAGEMENT = URL_NSL_MAIN + "events";
    public static final String URL_INSERTING_PAYMENT_COLLECTION = URL_NSL_MAIN + "paymentcollection";
    public static final String URL_INSERTING_PUSHTABLE = URL_NSL_MAIN + "tablepush";
    public static final String URL_INSERTING_GEO_PUSH_FULL_PATH = URL_NSL_MAIN + "geopush";
    public static final String URL_INSERTING_DAILYDAIRY = URL_NSL_MAIN + "dailydairy";
    public static final String URL_UPDATING_DAILYDAIRY = URL_NSL_MAIN + "dailydairyedit";
    public static final String URL_UPDATING_PROFILEPIC = URL_NSL_MAIN + "updateprofilepic";
    public static final String URL_ORDER_APPROVAL = URL_NSL_MAIN + "orderapproval";
    public static final String URL_INSERT_STOCKMOVEMENT = URL_NSL_MAIN + "stockmovement";
    public static final String URL_INSERT_STOCKMOVEMENT_RETAILER = URL_NSL_MAIN + "stockmovement_retailer";
    public static final String URL_INSERTING_BANK = URL_NSL_MAIN + "addbankdetails";
    public static final String URL_INSERTING_STOCKRETURNS = URL_NSL_MAIN + "stockreturns";

    public static final String URL_MASTER_DIVISIONS = "details?name=division";
    public static final String URL_MASTER_REGIONS = "details?name=region";
    public static final String URL_MASTER_COMPANIES = "details?name=companies";
    public static final String URL_MASTER_CROPS = "details?name=crops";
    public static final String URL_MASTER_PRODUCTS = "details?name=products";
    public static final String URL_MASTER_PRODUCT_PRICE = "details?name=product_price";
    public static final String URL_MASTER_USERS = "user_details?name=users&user_id=";
    public static final String URL_MASTER_GRADE = "details?name=grade";
    public static final String URL_MASTER_CUSTOMER_BANKDETAILS = "details?name=customer_bankdetails";

    public static final String URL_MASTER_COMPANY_DIVISION_CROP = "details?name=company_division_crop";
    public static final String URL_MASTER_USER_COMPANY_CUSTOMER = "details?name=user_company_customer";
    public static final String URL_MASTER_USER_COMPANY_DIVISION = "user_company_divisions?team=";

    public static final String URL_EMPLOYEE_VISIT_MANAGEMENT = "details?name=employee_visit_management";

    public static final String URL_MASTER_GEO_TRACKING = URL_NSL_MAIN + "details?name=geo_tracking";

    public static final String URL_MASTER_COMPLAINTS = "details?name=complaints";
    public static final String URL_MASTER_FEEDBACK = "details?name=feedback";
    public static final String URL_MASTER_DAILY_DAIRY = "details?name=dailydairy";
    public static final String URL_MASTER_COMMODITY_PRICE = "details?name=mi_commodity_price";
    public static final String URL_MASTER_CROP_SHIFTS = "details?name=mi_crop_shifts";
    public static final String URL_MASTER_PRICE_SURVEY = "details?name=mi_price_survey";
    public static final String URL_MASTER_PRODUCT_SURVEY = "details?name=mi_product_survey";

    public static final String URL_MASTER_RETAILERS = "details?name=retailers";
    public static final String URL_INSERTING_RETAILER = URL_NSL_MAIN + "addretailer";

    public static final String NEW_OR_UPDATED_RECORDS_EMPLOYEE_VISIT_MANAGEMENT = Constants.URL_NSL_MAIN + "neworupdated?table=employee_visit_management";
    public static final String NEW_OR_UPDATED_RECORDS_SERVICE_ORDER = Constants.URL_NSL_MAIN + "neworupdated_sales?table=service_order";
    public static final String ACKNOWLEDGE_TO_SERVER = BASE_URL + "/mo_rmsync";
    public static final String GET_CUSTOMERS_AND_CUSTOMER_DETAILS = URL_NSL_MAIN + "customer_details?";
    public static final String GET_PAYMENT_COLLECTION_DETAILS = URL_NSL_MAIN + "payment_collection?team=";
    public static final String ALL_IN_ONE_MASTERS = BASE_URL + "/masterdata";
    public static final String GET_DISTRIBUTOR_RETAILERS = "/distributor_retailers?team=";
    public static final String GET_STOCKMOVEMENT_LIST = "/stockmovement_list?team=";
    public static final String GET_STOCKRETURNS_LIST = "/stockreturns_list?team=";
    public static final String URL_MASTER_SCHEMES = "scheme_regions?user_id=";
    public static final String CHANGE_PASSWORD = URL_NSL_MAIN + "change_password";


    public static final boolean isShowNetworkToast = true;
    public static final String MYPREFERENCE = "mypref";
    public static final String USER_ID = "userId";
    public static final String CUSTOM_FONT_PATH_NORMAL = "fonts/SEGOEWP.TTF";
    public static final String CUSTOM_FONT_PATH_BOLD = "fonts/SEGOEWP-SEMIBOLD.TTF";
    public static final String CUSTOM_FONT_PATH_LIGHT = "fonts/SEGOEWP-LIGHT.TTF";
    public static final String CUSTOMER_ID_GEO = "customer_id_geo";
    public static final String URL_LOGOUT = URL_NSL_MAIN + "logout";
    public static final String URL_APP_LOGGED_IN = URL_NSL_MAIN + "app_logged_in";
    public static final String URL_UPDATE_LAST_LOGIN = URL_NSL_MAIN + "update_last_login";


    public interface Roles {
        int ROLE_5 = 5;   // RM
        int ROLE_6 = 6;   // RM
        int ROLE_7 = 7;   // MO

        int ROLE_12 = 12; //
    }

    public interface PaymentTypes {
        int PAYMENT_TYPES_ABS = 1;
        int PAYMENT_TYPES_NORMAL = 2;

    }

    public interface SharedPrefrancesKey {
        String ROLE = "role";
        String USER_ID = "userId";
        String NETWORK_CHANGED_LAST_TIME = "network_changed_last_time";
        String CURRENT_DATE = "current_date";
        String IS_DEFAULT_PASSWORD = "is_default_password";
    }

    public interface DateFormat {
        String COMMON_DATE_FORMAT = "yyyy-MM-dd";
        String COMMON_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public interface MasterTableNames {
        String TABLE_DIVISION = "division";
        String TABLE_REGION = "region";
        String TABLE_COMPANIES = "companies";
        String TABLE_CROPS = "crops";
        String TABLE_PRODUCTS = "products";
        String TABLE_PRODUCT_PRICE = "product_price";
        String TABLE_SCHEMES = "schemes";
        String TABLE_SCHEME_PRODUCTS = "scheme_products";
        String TABLE_USERS = "users";
        String TABLE_GRADE = "grade";
    }

    public static final String ANDROID = "Android";
    public static final int SUCCESS = 200;
    public static final String KEY_1 = "key1";
    public static final long CONNECTION_TIME_OUT = 60;
    public static final long READ_TIME_OUT = 60;
    public static final long WRITE_TIME_OUT = 60;
    public static final int ZOOM_LEVEL = 20;
}
