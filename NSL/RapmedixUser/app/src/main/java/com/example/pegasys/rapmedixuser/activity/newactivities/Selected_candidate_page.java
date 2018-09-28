package com.example.pegasys.rapmedixuser.activity.newactivities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.Home_page;
import com.example.pegasys.rapmedixuser.activity.database.DataBase_Helper;
import com.example.pegasys.rapmedixuser.activity.pojo.CheckoutResponse;
import com.example.pegasys.rapmedixuser.activity.pojo.CnfBookingreq;
import com.example.pegasys.rapmedixuser.activity.pojo.Cnfrespo;
import com.example.pegasys.rapmedixuser.activity.pojo.Familydatum;
import com.example.pegasys.rapmedixuser.activity.pojo.checkoutRequ;
import com.example.pegasys.rapmedixuser.activity.pojo.familydata;
import com.example.pegasys.rapmedixuser.activity.pojo.userIdreq;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitRequester;
import com.example.pegasys.rapmedixuser.activity.retrofitnetwork.RetrofitResponseListener;
import com.example.pegasys.rapmedixuser.activity.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Selected_candidate_page extends AppCompatActivity implements RetrofitResponseListener {

    TextView member_name;
    EditText mobile_number;
    TextView Namee, Desig, Qualification, Experience, price, service_tax, total;
    RadioGroup radioGroup;
    ImageView Imageview, plus_add_member;
    RadioButton fre_con, off_con;
    Button booknow;
    String discountt = "", coupon_type = "";
    DataBase_Helper dataBase_helper;
    SharedPreferences sp, ref_code_sp;
    String mdoctor_id, mHosp_id, mWork_id, mDate, mFee, mtimeslot;
    private Object obj;
    public static final String cost = "price_c";
    private SharedPreferences global;
    private String cate;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedcandidate_page);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        ImageView backButton = toolbar.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        if (intent != null) {

            mdoctor_id = intent.getStringExtra("DocId");
            mWork_id = intent.getStringExtra("WorkingId");
            mHosp_id = intent.getStringExtra("HospitalId");
            mDate = intent.getStringExtra("SelectedDate");
            mFee = intent.getStringExtra("Price");
            mtimeslot = intent.getStringExtra("TimeSlot");
            Log.i("data", mdoctor_id + " workid" + mWork_id + " hospid" + mHosp_id + " date " + mDate + " fee " + mFee + " time" + mtimeslot);
        }
        member_name = findViewById(R.id.member_name);
        mobile_number = findViewById(R.id.mobile_number);
        radioGroup = findViewById(R.id.radioGroup);
        fre_con = findViewById(R.id.fre_con);
        off_con = findViewById(R.id.off_con);
        booknow = findViewById(R.id.booknow);
        Namee = findViewById(R.id.doctor_name);
        Desig = findViewById(R.id.doctor_desig);
        price = findViewById(R.id.price);
        service_tax = findViewById(R.id.service_tax);
        total = findViewById(R.id.total);
        Qualification = findViewById(R.id.doctor_quli);
        Experience = findViewById(R.id.doctor_exp);
        Imageview = findViewById(R.id.doctorImg);
        plus_add_member = findViewById(R.id.plus_add_member);

        Qualification.setText(mDate);
        Experience.setText(mtimeslot);

        dataBase_helper = new DataBase_Helper(this);
        member_name.setText(dataBase_helper.getUserName("1"));
        mobile_number.setText(dataBase_helper.getUserMobileNumber("1"));
        Log.i("mobile", dataBase_helper.getUserMobileNumber("1"));

        sp = getSharedPreferences(cost, MODE_PRIVATE);
        ref_code_sp = getSharedPreferences("category", MODE_PRIVATE);
        cate = ref_code_sp.getString("spec_name", "");
        global = getSharedPreferences("loggers", MODE_PRIVATE);

        String uid = global.getString("UID", null);
//        member_name.setText(user);
        member_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIdreq idreq = new userIdreq();
                idreq.userId = new DataBase_Helper(Selected_candidate_page.this).getUserId("1");
                try {
                    obj = Class.forName(userIdreq.class.getName()).cast(idreq);
                    Log.d("obj", obj.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                new RetrofitRequester(Selected_candidate_page.this).callPostServices(obj, 3, "/user/get_family_members", true);
            }
        });
        plus_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_plus = new Intent(Selected_candidate_page.this, FamilymemberActivity.class);
                intent_plus.putExtra("Add_Family", "1");
                startActivity(intent_plus);
                finish();
            }
        });

        checkoutRequ requ = new checkoutRequ();
        requ.doctorId = mdoctor_id;
        requ.hospitalId = mHosp_id;
        requ.doctorworkingdetailsId = mWork_id;
//        requ.userId = "1";
        requ.userId = new DataBase_Helper(Selected_candidate_page.this).getUserId("1");
        try {
            obj = Class.forName(checkoutRequ.class.getName()).cast(requ);
            Log.d("obj", obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new RetrofitRequester(this).callPostServices(obj, 1, "/webservices/checkout_service", true);

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (member_name.getText().toString().trim().equals("") || mobile_number.getText().toString().trim().equals("")) {

                    Snackbar snackBar = Snackbar.make(view, "Fields Should not be Empty!", Snackbar.LENGTH_SHORT)
                            .setAction("", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackBar.setActionTextColor(Color.RED);
                    View sbView = snackBar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackBar.show();
                } else {
                    CnfBookingreq req = new CnfBookingreq();
                    req.doctorId = mdoctor_id;
                    req.appointmentDate = Qualification.getText().toString().trim();
                    req.appointmentTime = Experience.getText().toString().trim();
                    req.fee = mFee;
                    req.hospitalId = mHosp_id;
                    req.doctorworkingdetailsId = mWork_id;
                    req.patientName = member_name.getText().toString().trim();
                    req.patientMobile = mobile_number.getText().toString().trim();
                    req.discount = discountt;
                    req.subTotal = price.getText().toString().trim();
                    req.servicetax = service_tax.getText().toString().trim();
                    req.totalPaybleAmount = total.getText().toString().trim();
                    req.couponType = coupon_type;
                    req.userId = dataBase_helper.getUserId("1");
                    req.categoryName = cate;
//                    req.userId = dataBase_helper.getUserId("1");
//                            req. = ref_code_sp.getString("spec_name", "em");

                    try {
                        obj = Class.forName(CnfBookingreq.class.getName()).cast(req);
                        Log.d("objreq!!!", obj.toString());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    new RetrofitRequester(Selected_candidate_page.this).callPostServices(obj, 2, "/webservices/bookanappointment", true);
                }

            }
        });


    }

    @Override
    public void onResponseSuccess(Object objectResponse, Object objectRequest, int requestId) {
        if (objectResponse == null || objectResponse.equals("")) {
            Toast.makeText(Selected_candidate_page.this, "Please Retry", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestId) {
                case 1:
                    final CheckoutResponse Respo = Common.getSpecificDataObject(objectResponse, CheckoutResponse.class);
                    Gson gson = new Gson();
                    if (Respo.resStatus.equals("success")) {
                        Namee.setText(Respo.name);
                        Desig.setText(Respo.hospitalName);
                        if (!Respo.profilePic.equals("")) {
                            Picasso.with(this).load(Respo.profilePic).into(Imageview);
                        } else {
                            Imageview.setImageResource(R.drawable.rap_banner);
                        }
                        String mConsultation = Respo.consultation;
                        if (mConsultation.equals("0")) {
                            off_con.setVisibility(View.GONE);
                        } else {
                            off_con.setText(mConsultation + "% OFF");
                        }
                        price.setText(mFee);
                        Double d = Respo.consultationServiceTax;
                        service_tax.setText("" + Math.round(Integer.parseInt(mFee) * (d / 100)));
                        Log.e("zzzaaa", "Aa " + Respo.userFreecoupon);

                        total.setText("" + (Integer.parseInt(mFee) + Double.parseDouble(service_tax.getText().toString())));
                        int value = Respo.userFreecoupon;
                        if (!Respo.userFreecoupon.equals(1)) {
                            fre_con.setVisibility(View.GONE);
                        }

                        fre_con.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (buttonView.isChecked()) {
                                    coupon_type = "1";

                                    price.setText("0");
                                    service_tax.setText("0");
                                    total.setText("0");
                                }
                            }
                        });

                        off_con.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (buttonView.isChecked()) {
                                    try {
                                        coupon_type = "2";
                                        Log.e("aaaaaaaa", "price" + mFee);

                                        if (sp.getString("Fee", "null").equals("null")) {
                                            //  price.setText("" + Math.round((Double.parseDouble(price.getText().toString()) - (Integer.parseInt(price.getText().toString()) * (jo.getDouble("consultation") / 100)))));
                                            Log.e("ffeeeeee", "Aa" + mFee);
                                            double pprice = ((Integer.parseInt(mFee)) - (Math.round(Integer.parseInt(mFee) * (Double.parseDouble(Respo.consultation) / 100))));
                                            price.setText("" + pprice);
                                            Log.e("original", "" + pprice);
                                            service_tax.setText("" + Math.round(Double.parseDouble(price.getText().toString()) * (Respo.consultationServiceTax / 100)));

                                            total.setText("" + Math.round(Double.parseDouble(price.getText().toString()) + (Double.parseDouble(price.getText().toString()) * (Respo.consultationServiceTax / 100))));

                                            discountt = "" + (Double.parseDouble(mFee) * (Double.parseDouble(Respo.consultation) / 100));


                                            Log.e("disc", "" + discountt.toString().trim());
                                        } else {
                                            price.setText(sp.getString("price", "0"));
                                            service_tax.setText(sp.getString("serice_tax", "0"));
                                            total.setText(sp.getString("total", "0"));
                                            discountt = price.getText().toString();
                                        }

                                        SharedPreferences.Editor editor = sp.edit();

                                        editor.putString("price", price.getText().toString().trim());
                                        editor.putString("serice_tax", service_tax.getText().toString().trim().substring(0, 2));
                                        editor.putString("total", total.getText().toString().trim());
                                        editor.commit();
                                        Log.e("aaaaaaaa", "price" + sp.getString("price", "0") + "   " + sp.getString("serice_tax", "0"));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("aa", "Aa" + e.toString());

                                    }
                                }


                            }
                        });


                    } else {
                        Toast.makeText(Selected_candidate_page.this, " " + Respo.resStatus, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Cnfrespo mcnfrespo = Common.getSpecificDataObject(objectResponse, Cnfrespo.class);
                    gson = new Gson();
                    if (mcnfrespo.resStatus.equals("success")) {
                        Toast.makeText(Selected_candidate_page.this, mcnfrespo.resStatus, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Selected_candidate_page.this, Home_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {
                        Toast.makeText(Selected_candidate_page.this, mcnfrespo.resStatus, Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 3:
                    familydata mFamilydata = Common.getSpecificDataObject(objectResponse, familydata.class);
                    gson = new Gson();
                    if (mFamilydata.status.equals("success")) {
                        user = mFamilydata.username;
                        ArrayList<Familydatum> list = new ArrayList<>();
                        final ArrayList<String> nameslist = new ArrayList<>();
                        final ArrayList<String> mobilelist = new ArrayList<>();
                        list = (ArrayList<Familydatum>) mFamilydata.familydata;
                        nameslist.add(mFamilydata.username);
                        mobilelist.add(mobile_number.getText().toString());
                        for (int i = 0; i < list.size(); i++) {
                            String mem_name = list.get(i).name;
                            String mem_mobile = list.get(i).mobile;
                            nameslist.add(mem_name);
                            mobilelist.add(list.get(i).mobile);
                        }

                        final Dialog dialog = new Dialog(Selected_candidate_page.this);
                        dialog.setContentView(R.layout.person_details);
                        ListView listView = dialog.findViewById(R.id.person_details_list);
                        ArrayAdapter dataAdapter_age = new ArrayAdapter(Selected_candidate_page.this, R.layout.spinner_item, nameslist);
                        listView.setAdapter(dataAdapter_age);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                member_name.setText(nameslist.get(position).toString());
                                mobile_number.setText(mobilelist.get(position).toString());
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        Toast.makeText(Selected_candidate_page.this, " " + mFamilydata.status, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(Selected_candidate_page.this, " " + "Respo.resStatus", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
