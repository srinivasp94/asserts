package com.nsl.app.retailers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.Constants;
import com.nsl.app.DatabaseHandler;
import com.nsl.app.R;
import com.nsl.app.adapters.RetailerListAdapter;
import com.nsl.app.adapters.StockPlacementPopupListAdapter;
import com.nsl.app.commonutils.Common;
import com.nsl.app.enums.RoleEnum;
import com.nsl.app.multiselectionlist.TeamListViewAdapter;
import com.nsl.app.pojo.MappedRetailerPojo;
import com.nsl.app.pojo.MappedRetailerWithDistributorPojo;
import com.nsl.app.pojo.RetailersNamePojo;
import com.nsl.app.pojo.StockMovementPoJo;
import com.nsl.app.pojo.StockMovementRetailerDetails;
import com.nsl.app.pojo.StockPlacementPopupPojo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.nsl.app.DatabaseHandler.KEY_TABLE_RETAILER_FFMID;
import static com.nsl.app.DatabaseHandler.TABLE_RETAILER;
import static com.nsl.app.DatabaseHandler.TABLE_STOCK_MOVEMENT_RETAILER_DETAILS;


public class AllRetailersActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView empty;
    String jsonData, checkuid;
    private RecyclerView recyclerView;
    // private ItemfavitemAdapter adapter;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    Button refresh;
    DatabaseHandler db;
    SQLiteDatabase sdbw;
    private String company_id;
    private String division_id;
    private String crop_id;
    private String customer_id;
    private String userId;
    private int role;
    private List<RetailersNamePojo> retailersNamePojos;
    private String product_id;
    private String stock_movement_id;
    private List<MappedRetailerPojo> mappedRetailerData;
    private List<MappedRetailerWithDistributorPojo> retailerWithDistributor = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    private JSONArray mainArray;
    private Dialog dialogPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_retailers);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        Log.e("user id ", checkuid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent home = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(home);
                finish();
            }
        });

        db = new DatabaseHandler(AllRetailersActivity.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent newfedback = new Intent(getApplicationContext(), MainRetailerActivity.class);
                newfedback.putExtra("company_id",company_id);
                newfedback.putExtra("division_id",division_id);
                newfedback.putExtra("crop_id",crop_id);
                newfedback.putExtra("customer_id",customer_id);
                startActivity(newfedback);
                finish();*/

                dialogPopup = popup(retailerWithDistributor);
            }
        });

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllRetailersActivity.this, AllRetailersActivity.class);
                startActivity(i);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        empty = (TextView) findViewById(R.id.empty);

        company_id = getIntent().getStringExtra("company_id");
        division_id = getIntent().getStringExtra("division_id");
        crop_id = getIntent().getStringExtra("crop_id");
        customer_id = getIntent().getStringExtra("customer_id");
        product_id = getIntent().getStringExtra("product_id");
        stock_movement_id = getIntent().getStringExtra("stock_movement_id");

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID, null);
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        mappedRetailerData = db.getMappedRetailerWithDistributorAndProduct(stock_movement_id, product_id, crop_id);
        for (int i = 0; i < mappedRetailerData.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            String retailerId = mappedRetailerData.get(i).retailerId;
            stringBuilder.append(retailerId);

        }

        retailerWithDistributor = db.getMappedRetailerWithDistributor(customer_id, stringBuilder.toString());

        Common.Log.i(mappedRetailerData.toString());
        if (mappedRetailerData.size() > 0) {
            setListOnAdapter(mappedRetailerData, recyclerView);
        } else if (retailerWithDistributor.size() > 0) {
            dialogPopup = popup(retailerWithDistributor);
        }


        //Map<String, String> map = new HashMap<String, String>();
        //map.put("name", "distributor_retailer");
/*
        new RetrofitRequester(new RetrofitResponseListener() {
            @Override
            public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {
                // DistributorsRetailerPojo distributorsRetailerPojo=Common.getSpecificDataObject(object.get(i),DistributorsRetailerPojo.class);
                SQLiteDatabase dbWritable = db.getWritableDatabase();
                for (int i = 0; i < object.size(); i++) {
                    DistributorsRetailerPojo distributorsRetailerPojo = Common.getSpecificDataObject(object.get(i), DistributorsRetailerPojo.class);
                    Common.Log.i("distributorsRetailerPojo" + distributorsRetailerPojo);
                    db.insertDistributorRetailers(dbWritable, distributorsRetailerPojo);


                }
                dbWritable.close();
//                Intent main = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(main);
//                finish();

            }


        }).callGetApi(map);
*/


    }


    private void getofflineRetailers() {
        Log.d("Reading: ", "Reading all retailers..");

        List<Retailer> customers = db.getAllRetailer(checkuid);
        if (customers.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
        for (Retailer cus : customers) {
            String log = "Id: " + cus.get_ret_masterid() + " \n master ID" + cus.get_ret_masterid() + " , \n name: " + cus.get_ret_name() + "\n  code " + cus.get_ret_tin_no() + " \n Address : " + cus.get_ret_address() + " \n phn : " + cus.get_ret_phone() + "\n mobile " + cus.get_ret_mobile() + ":" + cus.get_email() + " \n C dist id" + cus.get_ret_dist_id() + " \n sap" + cus.get_ret_dist_sap_code() + " \n status" + cus.get_ret_status() + " \n cdate" + cus.get_ret_cdatetime() + " \n udate" + cus.get_ret_udatetime() + " \n ffmid" + cus.get_ffmid();
            // Writing Contacts to log
            Log.e("retailer: ", log);

        }

        for (Retailer fb : customers) {

            HashMap<String, String> map = new HashMap<String, String>();
            // map.put("srno", String.valueOf(srno));
            map.put("retailer_id", fb.get_ret_masterid());
            map.put("retailer_name", fb.get_ret_name());
            map.put("retailer_tin_no", fb.get_ret_tin_no());
            map.put("address", fb.get_ret_address());
            map.put("phone", fb.get_ret_phone());
            map.put("mobile", fb.get_ret_mobile());
            map.put("email_id", fb.get_email());
            map.put("ffmid", fb.get_ffmid());

            favouriteItem.add(map);


        }


    }


    public void setListOnAdapter(final List<MappedRetailerPojo> mappedRetailerPojos, final RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        RetailerListAdapter retailerListAdapter = null;

        if (mappedRetailerPojos.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            retailerListAdapter = new RetailerListAdapter(getApplicationContext(), mappedRetailerPojos);
            recyclerView.setAdapter(retailerListAdapter);
            retailerListAdapter.setOnItemClickListener(new RetailerListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, final int position) {

                    TextView stockPlaced = (TextView) view.findViewById(R.id.TextView_stockplaced);
                    TextView currentStock = (TextView) view.findViewById(R.id.TextView_current_stock);
                    TextView pog = (TextView) view.findViewById(R.id.TextView_pog);
                    Button retailer = (Button) view.findViewById(R.id.button_r);
                    //final ArrayList<StockPlacementPopupPojo> stockPlacementPopupPojoArrayList;

                    if (role != Constants.Roles.ROLE_7 && !getIntent().getStringExtra("mo_id").equalsIgnoreCase(userId)) {

                        Toast.makeText(AllRetailersActivity.this, "Sorry you can't place order", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    switch (view.getId()) {

                        case R.id.TextView_stockplaced:


                            ArrayList<StockPlacementPopupPojo> stockPlacementPopupPojoArrayList;


                            final Dialog dialog = new Dialog(AllRetailersActivity.this);
                            dialog.setContentView(R.layout.stock_placed_popup);

                            final TextView txt_date = (TextView) dialog.findViewById(R.id.datepicker);
                            final EditText qnty_places = (EditText) dialog.findViewById(R.id.edittext_quantity_placed);
                            final ImageView ok = (ImageView) dialog.findViewById(R.id.buttton_stock_placed_ok);
                            final Button close = (Button) dialog.findViewById(R.id.button_stock_placed_close);
                            final RecyclerView recyclerViewStockPlacement = (RecyclerView) dialog.findViewById(R.id.recycler_view);
                            stockPlacementPopupPojoArrayList = db.getOfflineSMRDStockPlacementListById(db.getCombinationOf3(Integer.parseInt(userId), Integer.parseInt(company_id), Integer.parseInt(division_id), Integer.parseInt(customer_id)), Integer.parseInt(crop_id), Integer.parseInt(product_id), mappedRetailerPojos.get(position).retailerId);
                            final StockPlacementPopupListAdapter stockPlacementPopupListAdapter = setStockPlacedListOnAdapter(stockPlacementPopupPojoArrayList, recyclerViewStockPlacement);

                            txt_date.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Calendar mcurrentDate = Calendar.getInstance();
                                    final int mYear = mcurrentDate.get(Calendar.YEAR);
                                    final int mMonth = mcurrentDate.get(Calendar.MONTH);
                                    final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                                    DatePickerDialog mDatePicker = new DatePickerDialog(AllRetailersActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                            // TODO Auto-generated method stub
                                            int selmon = selectedmonth + 1;
                                            txt_date.setText(selectedday + "-" + selmon + "-" + selectedyear);
                                        }
                                    }, mYear, mMonth, mDay);
                                    mDatePicker.setTitle("Select date");
                                    mDatePicker.show();

                                }
                            });

                            recyclerView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.dismissDialog(dialog);

                                }
                            });
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Handler handler=Common.disableClickEvent(ok,true);
                                    Common.hideSoftKeyboardFromDialog(dialog, AllRetailersActivity.this);
                                    boolean isInserted = saveStockMovementInDb(txt_date.getText().toString().trim(), qnty_places.getText().toString().trim(), "0", product_id, mappedRetailerPojos.get(position).retailerId);
                                    mappedRetailerData = db.getMappedRetailerWithDistributorAndProduct(stock_movement_id, product_id, crop_id);
                                    if (isInserted) {
                                        txt_date.setText("");
                                        qnty_places.setText("");
                                    }
                                    setListOnAdapter(mappedRetailerData, recyclerView);

                                    setStockPlacedListOnAdapter(db.getOfflineSMRDStockPlacementListById(db.getCombinationOf3(Integer.parseInt(userId), Integer.parseInt(company_id), Integer.parseInt(division_id), Integer.parseInt(customer_id)), Integer.parseInt(crop_id), Integer.parseInt(product_id), mappedRetailerPojos.get(position).retailerId), recyclerViewStockPlacement);

                                    //Common.dismissDialog(dialogPopup);
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Common.dismissDialog(dialog);
                                }
                            });


                            dialog.setCancelable(true);
                            dialog.show();


                            break;
                        case R.id.TextView_current_stock:
                            final Dialog dialog1 = new Dialog(AllRetailersActivity.this);
                            dialog1.setContentView(R.layout.current_stock_popup);
                            final TextView txt_date1 = (TextView) dialog1.findViewById(R.id.datepicker);
                            final TextView currentStock1 = (TextView) dialog1.findViewById(R.id.textview_currentstock_left_value);
                            final EditText qnty_places1 = (EditText) dialog1.findViewById(R.id.edittext_quantity_placed);
                            final ImageView ok1 = (ImageView) dialog1.findViewById(R.id.buttton_stock_placed_ok);
                            Button close1 = (Button) dialog1.findViewById(R.id.button_current_stock_close);

                            currentStock1.setText(currentStock.getText().toString());

                            txt_date1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Calendar mcurrentDate = Calendar.getInstance();
                                    final int mYear = mcurrentDate.get(Calendar.YEAR);
                                    final int mMonth = mcurrentDate.get(Calendar.MONTH);
                                    final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                                    DatePickerDialog mDatePicker = new DatePickerDialog(AllRetailersActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                            // TODO Auto-generated method stub
                                            int selmon = selectedmonth + 1;
                                            txt_date1.setText(selectedday + "-" + selmon + "-" + selectedyear);
                                        }
                                    }, mYear, mMonth, mDay);
                                    mDatePicker.setTitle("Select date");
                                    mDatePicker.show();

                                }
                            });

                            ok1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Handler handler=Common.disableClickEvent(ok1,true);
                                    Common.hideSoftKeyboardFromDialog(dialog1, AllRetailersActivity.this);
                                    boolean isInserted = saveStockMovementInDb(txt_date1.getText().toString().trim(), "0", qnty_places1.getText().toString().trim(), product_id, mappedRetailerPojos.get(position).retailerId);

                                    currentStock1.setText(qnty_places1.getText().toString().trim());
                                    if (isInserted) {
                                        txt_date1.setText("");
                                        qnty_places1.setText("");
                                    }
                                    mappedRetailerData = db.getMappedRetailerWithDistributorAndProduct(stock_movement_id, product_id, crop_id);
                                    setListOnAdapter(mappedRetailerData, recyclerView);

                                }
                            });

                            close1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Common.dismissDialog(dialog1);
                                }
                            });


                            dialog1.setCancelable(false);
                            dialog1.show();
                            break;
                        case R.id.TextView_pog:

                            break;
                        case R.id.button_r:

                            break;


                    }


                }
            });
            retailerListAdapter.notifyDataSetChanged();

        } else if (mappedRetailerPojos == null || mappedRetailerPojos.size() == 0) {

            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }


    public Dialog popup(List<MappedRetailerWithDistributorPojo> retailerWithDistributor) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(true);
        final ListView listView = (ListView) dialog.findViewById(R.id.myListView);
        Button addRetailer = (Button) dialog.findViewById(R.id.buttonStart);
        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newfedback = new Intent(getApplicationContext(), MainRetailerActivity.class);
                newfedback.putExtra("company_id", company_id);
                newfedback.putExtra("division_id", division_id);
                newfedback.putExtra("crop_id", crop_id);
                newfedback.putExtra("customer_id", customer_id);
                startActivity(newfedback);
                finish();


            }
        });


        final TeamListViewAdapter myAdapter = new TeamListViewAdapter(this, R.layout.row_team_layout, retailerWithDistributor);
        listView.setAdapter(myAdapter);
        listView.setItemsCanFocus(false);

        addRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MappedRetailerWithDistributorPojo> selectedTeams = new ArrayList<MappedRetailerWithDistributorPojo>();
                final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int checkedItemsCount = checkedItems.size();
                for (int i = 0; i < checkedItemsCount; ++i) {
                    // Item position in adapter
                    int position = checkedItems.keyAt(i);
                    // Add team if item is checked == TRUE!
                    if (checkedItems.valueAt(i))
                        selectedTeams.add(myAdapter.getItem(position));
                }
                if (selectedTeams.size() < 1)
                    Toast.makeText(getBaseContext(), "Need to select one or more retailers.", Toast.LENGTH_SHORT).show();
                else {
                    // Just logging the output.
                    for (MappedRetailerWithDistributorPojo mappedRetailerWithDistributorPojo : selectedTeams) {
                        Log.d("SELECTED TEAMS: ", mappedRetailerWithDistributorPojo.retailerName);
                        saveStockMovementInDb("2017-06-2017", "0", "0", product_id, mappedRetailerWithDistributorPojo.retailerId);
                    }

                }


            }
        });

        dialog.show();
        return dialog;
    }


    private boolean saveStockMovementInDb(String date, String stockPlacedQuantity, String currentStockQuantity, String productId, String retailerId) {
        if (!date.equalsIgnoreCase("") && (!stockPlacedQuantity.equalsIgnoreCase("") || !currentStockQuantity.equalsIgnoreCase(""))) {

            progressDialog = Common.showProgressDialog(this);


            StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
            stockMovementPoJo.ffmId = 0;
            stockMovementPoJo.movementType = 0;
            stockMovementPoJo.status = 0;
            stockMovementPoJo.divisionId = Integer.parseInt(division_id);
            stockMovementPoJo.companyId = Integer.parseInt(company_id);
            stockMovementPoJo.customerId = Integer.parseInt(customer_id);
            stockMovementPoJo.updatedDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdBy = userId;
            stockMovementPoJo.createdBy = userId;
            stockMovementPoJo.userId = Integer.parseInt(userId);


            StockMovementRetailerDetails stockMovementDetailsPojo = new StockMovementRetailerDetails();
            stockMovementDetailsPojo.stockPlaced = stockPlacedQuantity;
            stockMovementDetailsPojo.currentStock = currentStockQuantity;
            stockMovementDetailsPojo.updatedDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementDetailsPojo.createdDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementDetailsPojo.createdBy = userId;
            stockMovementDetailsPojo.userId = userId;
            stockMovementDetailsPojo.userType = RoleEnum.getRoleTitle(role).getTitle();
            stockMovementDetailsPojo.placedDate = date;
            stockMovementDetailsPojo.productId = productId;
            stockMovementDetailsPojo.retailerId = retailerId;
            stockMovementDetailsPojo.cropId = crop_id;


            db.insertStackMovementForRetailerDetails(stockMovementPoJo, stockMovementDetailsPojo);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Common.haveInternet(getApplicationContext())) {
                prepareStockMovementDataAndPush();
            } else {
                Common.dismissProgressDialog(progressDialog);
                recreate();
            }
            return true;
            // DatabaseUtil.copyDatabaseToExtStg(getContext());
        } else {
            Toast.makeText(this, "Please date and quntity", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public StockPlacementPopupListAdapter setStockPlacedListOnAdapter(final List<StockPlacementPopupPojo> stockPlacementPopupPojos, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        StockPlacementPopupListAdapter stockPlacementPopupListAdapter = null;

        if (stockPlacementPopupPojos.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            stockPlacementPopupListAdapter = new StockPlacementPopupListAdapter(getApplicationContext(), stockPlacementPopupPojos);
            recyclerView.setAdapter(stockPlacementPopupListAdapter);
            stockPlacementPopupListAdapter.setOnItemClickListener(new StockPlacementPopupListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    //   busDetailsFragment();


                }
            });
            stockPlacementPopupListAdapter.notifyDataSetChanged();

        } else if (stockPlacementPopupPojos == null || stockPlacementPopupPojos.size() == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return stockPlacementPopupListAdapter;
    }


    private void prepareStockMovementDataAndPush() {
        mainArray = new JSONArray();

        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;
        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojos = new ArrayList<>();

        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojoList = db.getOfflineStockPlacementSMRDListUnSyncData1();
        for (StockMovementRetailerDetails stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            stockMovementUnSynedPojos = db.getOfflineStockPlacementSMRDListUnSyncData(Integer.parseInt(stockMovementUnSynedPojo.stockMovementId));
            for (StockMovementRetailerDetails stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                    advBookObj.put("user_id", userId);
                    advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                    advBookObj.put("movement_type", "0");
                    advBookObj.put("retailer_id", stockMovementUnSynedPojo1.retailerId);


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                    object_one.put("pog", stockMovementUnSynedPojo1.pog);
                    object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementRetailerId);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockmovement", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        mainArray.put(naimObject);
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0 || stockMovementUnSynedPojos.size() > 0) {
            new callAPIToPushStockmovementData().execute();
        }
    }

    private class callAPIToPushStockmovementData extends AsyncTask<String, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            try {
                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERT_STOCKMOVEMENT_RETAILER)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = null;


                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Common.dismissProgressDialog(progressDialog);
            Common.dismissDialog(dialogPopup);

            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " SET " + db.FFM_ID + " = " + ffmid + " WHERE stock_movement_retailer_id" + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                } finally {
                    recreate();
                }
            }


        }
    }


}
