
package com.nsl.app.advancebooking;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.MainActivity;
import com.nsl.app.R;
import com.nsl.app.Schemes;
import com.nsl.app.adapters.ABSMultiViewTypeAdapter;
import com.nsl.app.adapters.StockPlacementPopupListAdapter;
import com.nsl.app.commonutils.Common;
import com.nsl.app.pojo.GetWayDetailsByID;
import com.nsl.app.pojo.MyWayProduct;
import com.nsl.app.pojo.StockPlacementPopupPojo;
import com.nsl.app.pojo.ViewModalAdvBookingListPojo;
import com.nsl.app.pojo.ViewModalAdvBookingPojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.app.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_SCHEMES_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_CUSTOMER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_DIVISION_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERDATE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERTYPE;
import static com.nsl.app.DatabaseHandler.KEY_TABLE_SERVICEORDER_USER_ID;
import static com.nsl.app.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.app.DatabaseHandler.TABLE_PRODUCTS;
import static com.nsl.app.DatabaseHandler.TABLE_SCHEMES;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.app.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.app.orderindent.FragmentOrderIndent.REQUEST_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewABSDetailsActivity extends AppCompatActivity {


    ProgressDialog progressDialog;

    FloatingActionButton fab;
    String jsonData,userId,customer_id,DivisionId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_amount,tv_token_amount,tv_company_name;
    double sum_osa = 0;
    double osa;
    int j;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    private int requestType;
    SimpleDateFormat dateFormat;
    SimpleDateFormat simpleDateFormat;
    private ArrayList<String>  dateList=new ArrayList<>();
    List<ViewModalAdvBookingPojo> viewModalAdvBookingPojos =new ArrayList<>();
    ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojosFinal =new ArrayList<>();
    ViewModalAdvBookingListPojo viewModalAdvBookingListPojo =new ViewModalAdvBookingListPojo();

    private ViewModalAdvBookingPojo viewModalAdvBookingPojo;

    private ArrayList<MyWayProduct> pProductArrayList;
    private ArrayList<MyWayProduct.SubCategory> pSubItemArrayList;
    private List<String> arrayList;
    private List<GetWayDetailsByID> allFnfListSqlite12=new ArrayList<>();
    private boolean isFirstViewClick;
    private boolean isSecondViewClick;
    private ScrollView mLinearListView2;
    private int sr_no=0;
    private RecyclerView recyclerView;
    private String serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancebooking_customer_details_test);
        db                = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");

        Toolbar toolbar   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(getIntent().getStringExtra("TYPE").equalsIgnoreCase("rm")){
            userId  =  getIntent().getStringExtra("userId");
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent gotohome = new Intent(getApplicationContext(),NewAdvancebokingChooseActivity.class);
               gotohome.putExtra("selection","distributor");
               gotohome.putExtra("customer_id",customer_id);
               startActivity(gotohome);
               finish();
            }
        });


        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        //company_name
        tv_name            = (TextView)findViewById(R.id.tv_name);
        tv_company_name    = (TextView)findViewById(R.id.tv_company_name);
        tv_code            = (TextView)findViewById(R.id.tv_code);
        tv_amount          = (TextView)findViewById(R.id.tv_amount);
        tv_token_amount    = (TextView)findViewById(R.id.tv_token_amount);
        recyclerView    = (RecyclerView)findViewById(R.id.recycler_view);

        customer_id  = getIntent().getStringExtra("customer_id");
        DivisionId  = getIntent().getStringExtra("DivisionId");
        serviceId  = getIntent().getStringExtra("service_id");
        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText("("+getIntent().getStringExtra("customer_code")+")");
        tv_token_amount.setText(""+getIntent().getStringExtra("Token_Amount"));
        tv_company_name.setText(""+getIntent().getStringExtra("company_name"));


        requestType = getIntent().getIntExtra(REQUEST_TYPE,0);

        new Async_getallcustomersoffline().execute();


        if(requestType == 1){
            toolbar.setTitle("Order Indent");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Async_getallcustomersoffline extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ViewABSDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params)
        {
            favouriteItem.clear();
            j=0;

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
            String selectQuery  = " SELECT DISTINCT " + KEY_PRODUCT_BRAND_NAME + ",SD."+ KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + ",C."+ KEY_TABLE_CUSTOMER_MASTER_ID + ",SO." + KEY_TABLE_SERVICEORDER_ORDERDATE+",SD."+ KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY +",SD."+ KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID+",SD."+ KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID+",SD."+ KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID
                    + " FROM " +   TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +
                    " LEFT JOIN "+ TABLE_SERVICEORDERDETAILS + " AS SD ON SD."+ KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_ID  +
                    " LEFT JOIN "+ TABLE_SCHEMES + " AS SCH ON SD."+ KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH."+ KEY_SCHEMES_MASTER_ID +
                    " LEFT JOIN "+ TABLE_PRODUCTS + " AS PRO ON SD."+ KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO."+ KEY_PRODUCT_MASTER_ID +
                    " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id"+
                    "  where SD.order_detail_id is not null AND "+" SO."+ KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId +" AND  C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + customer_id +" AND SO."+KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2"+" AND SO."+KEY_TABLE_SERVICEORDER_DIVISION_ID + "="+DivisionId +" AND SD."+KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = "+serviceId+" group by SD.order_detail_id,DATE(SD.created_datetime)" ;
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            Log.e("selectQuery",selectQuery);
            if (cursor != null && cursor.moveToFirst()) {
                do {


                    if(cursor.getString(2)==null || cursor.getString(2).equalsIgnoreCase("") || cursor.getString(2).equalsIgnoreCase("null")){

                    }
                    else{

                        j= j+1;
                        osa           = Double.parseDouble(cursor.getString(2));
                        Log.e("Values",  cursor.getString(0)+" : "+cursor.getString(1)+" : OSA"+String.valueOf(osa) +" : customer id"+cursor.getString(3));
                        sum_osa       = sum_osa + osa;
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("customer_name",     cursor.getString(0));
                        map.put("customer_code",     cursor.getString(1));
                        map.put("customer_id",       cursor.getString(3));
                        map.put("Division",          "");
                        map.put("ABS",               String.valueOf(osa));
                        map.put("Crop name",         "");
                        map.put("Product Name",      "");
                        map.put("Order price",       "");
                        map.put("Quantity",          "");
                        map.put("OrderDate",         cursor.getString(4));
                        map.put("itemcount",         ""+j);
                        favouriteItem.add(map);


                        String schemeId = cursor.getString(7);
                        String pricePerUnit="0";
                        Log.d("schemeId: ",schemeId);
                        if (schemeId==null || schemeId.equalsIgnoreCase("0") || schemeId.equalsIgnoreCase(" ")){
                             pricePerUnit = db.getPricePerUnitByProductId(cursor.getString(6));
                        }else {
                            List<Schemes> schemesByProducId = db.getSchemesByProducId(cursor.getString(6), cursor.getString(1), cursor.getString(7));
                             pricePerUnit=schemesByProducId.size()>0 ?schemesByProducId.get(0).getScheme_value(): "0" ;
                        }

                        dateList.add(Common.dateformate(cursor.getString(4).split(" ")[0]));
                       // final ArrayList<Schemes>  adapter_schemes = (ArrayList<Schemes>) db.getSchemesByProducId(cursor.getString(6));


                        ViewModalAdvBookingPojo viewModalAdvBookingPojo=new ViewModalAdvBookingPojo();

                        viewModalAdvBookingPojo.customerName=cursor.getString(0);
                        viewModalAdvBookingPojo.customerCode=cursor.getString(1);
                        viewModalAdvBookingPojo.customerId=cursor.getString(3);
                        viewModalAdvBookingPojo.orderDate= Common.dateformate(cursor.getString(4).split(" ")[0]);
                        viewModalAdvBookingPojo.aBS= String.valueOf(osa);
                        viewModalAdvBookingPojo.quantity= cursor.getString(5);
                        viewModalAdvBookingPojo.rate= pricePerUnit;
                        viewModalAdvBookingPojo.slabId= cursor.getString(1).equalsIgnoreCase(null) || cursor.getString(1).equalsIgnoreCase("") || cursor.getString(1).equalsIgnoreCase("null") ?  "--NA--" : cursor.getString(1) ;

                        viewModalAdvBookingPojos.add(viewModalAdvBookingPojo);




                        GetWayDetailsByID getWayDetailsByID=new GetWayDetailsByID();
                        getWayDetailsByID.setCustomer_name(cursor.getString(0));
                        getWayDetailsByID.setCustomer_code(cursor.getString(1));
                        getWayDetailsByID.setCustomer_id(cursor.getString(3));
                        getWayDetailsByID.setOrderdate( Common.dateformate(cursor.getString(4).split(" ")[0]));
                        getWayDetailsByID.setAbs(String.valueOf(osa));
                        getWayDetailsByID.setQuantity(cursor.getString(5));
                        getWayDetailsByID.setRate(db.getPricePerUnitByProductId(cursor.getString(6)));

                        allFnfListSqlite12.add(getWayDetailsByID);
                    }


                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());

            }

            else Log.d("LOG", "returned null!");


            HashSet hs = new HashSet();
            hs.addAll(dateList);
            dateList.clear();
            dateList.addAll(hs);

                for (j=0;j<dateList.size();j++){
                    ViewModalAdvBookingPojo viewModalAdvBookingPojo=new ViewModalAdvBookingPojo();
                    viewModalAdvBookingPojo.orderDate=dateList.get(j);
                    viewModalAdvBookingPojo.type=0;
                    viewModalAdvBookingPojosFinal.add(viewModalAdvBookingPojo);


                    for (int i = 0; i< viewModalAdvBookingPojos.size(); i++){
                        if (dateList.get(j).equalsIgnoreCase(viewModalAdvBookingPojos.get(i).orderDate)){
                            ViewModalAdvBookingPojo viewModalAdvBookingPojo1 = viewModalAdvBookingPojos.get(i);
                            viewModalAdvBookingPojo1.type=1;
                            viewModalAdvBookingPojosFinal.add(viewModalAdvBookingPojo1);
                        }

                }
            }



            Log.d("LOG", "returned null!"+dateList.toString()+"\n"+ viewModalAdvBookingPojos.toString());
            return jsonData;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_amount.setText(""+String.valueOf(sum_osa));
            /*if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        setListOnAdapter(viewModalAdvBookingPojosFinal,recyclerView);
    }
});

        }
    }



    public ABSMultiViewTypeAdapter setListOnAdapter(final ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos, RecyclerView recyclerView) {
       Log.d("viewModal: ",viewModalAdvBookingPojos.toString());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        ABSMultiViewTypeAdapter absMultiViewTypeAdapter = null;

        if (viewModalAdvBookingPojos.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            absMultiViewTypeAdapter = new ABSMultiViewTypeAdapter(viewModalAdvBookingPojos,getApplicationContext());
            recyclerView.setAdapter(absMultiViewTypeAdapter);
            absMultiViewTypeAdapter.setOnItemClickListener(new ABSMultiViewTypeAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {



                }
            });
            absMultiViewTypeAdapter.notifyDataSetChanged();

        } else if (viewModalAdvBookingPojos == null || viewModalAdvBookingPojos.size() == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return absMultiViewTypeAdapter;
    }




}
