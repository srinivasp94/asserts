package com.nsl.app.stockexchange;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by admin on 4/26/2017.
 */
public class PlaceholderFragment extends Fragment {
    private RecyclerView recyclerView;
    private Adapter_cotton_recycle adapterCottonRecycle;
    int dd, mm, yy;
    String date;
    private Context context;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    AlertDialog alert;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    String[] dataset;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_information, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview_cotton);
        final FragmentActivity activity = getActivity();
        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
      /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        adapterCottonRecycle = new Adapter_cotton_recycle(getContext(),new String[]{"srinivas", "rupini", "anil", "puja"});
        adapterCottonRecycle.setOnItemClickListener(new Adapter_cotton_recycle.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    View view1 = inflater.inflate(R.layout.current_stock_popup,null);

                    builder.setView(view1);
                    builder.create();
                    builder.show();

            }
        });

        recyclerView.setAdapter(adapterCottonRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/

        setListOnAdapter(new String[]{"KAREENA-P10G ", "NBH-225-P250G", "P100G-ROHINI"});
        return rootView;
    }

    public void setListOnAdapter(final String[] stringArray) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (stringArray.length != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            adapterCottonRecycle = new Adapter_cotton_recycle(PlaceholderFragment.this.getContext(), stringArray);
            recyclerView.setAdapter(adapterCottonRecycle);
            adapterCottonRecycle.setOnItemClickListener(new Adapter_cotton_recycle.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                 /*   TextView stockPlaced = (TextView) view.findViewById(R.id.TextView_stockplaced);
                    TextView currentStock = (TextView) view.findViewById(R.id.TextView_current_stock);
                    TextView pog = (TextView) view.findViewById(R.id.TextView_pog);

                    TextView nameOfDealer = (TextView) view.findViewById(R.id.tv_dealer_name_cotton);*/

                    switch (view.getId()) {
                        case R.id.TextView_stockplaced:
                            Toast.makeText(getActivity(), "TextView_stockplaced", Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            final View view1 = inflater.inflate(R.layout.stock_placed_popup, null);

                            final TextView txt_date = (TextView) view1.findViewById(R.id.datepicker);
                            EditText qnty_places = (EditText) view1.findViewById(R.id.edittext_quantity_placed);
                            ImageView ok = (ImageView) view1.findViewById(R.id.buttton_stock_placed_ok);
                            Button close = (Button) view1.findViewById(R.id.button_stock_placed_close);

                            final Calendar myCalendar = Calendar.getInstance();
                            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // TODO Auto-generated method stub
                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    String myFormat = "dd/MMM/yyyy"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                                    txt_date.setText(sdf.format(myCalendar.getTime()));
                                }
                            };

                            txt_date.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new DatePickerDialog(getContext(), datePickerListener, myCalendar
                                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                }
                            });

                            String quantityPlaced = qnty_places.getText().toString();
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            });


                            builder.setView(view1);
                            alert = builder.create();
                            builder.show();

                            break;
                        case R.id.TextView_current_stock:
                            Toast.makeText(getActivity(), "textview_current_stock", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder_current_stock = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater1 = LayoutInflater.from(getContext());
                            View view_stock = inflater1.inflate(R.layout.current_stock_popup, null);

                            builder_current_stock.setView(view_stock);
                            builder_current_stock.create();
                            builder_current_stock.show();
                            break;
                        case R.id.TextView_pog:
                            Toast.makeText(getActivity(), "textview_pog", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder_pog = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater2 = LayoutInflater.from(getContext());
                            View view_pog = inflater2.inflate(R.layout.pog_popup, null);

                            builder_pog.setView(view_pog);
                            builder_pog.create();
                            builder_pog.show();
                            break;

                        case R.id.button_r:
                            Toast.makeText(getActivity(), "button retailer", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getActivity(),Stock_for_Retailer.class);
                            startActivity(intent);
                            break;

                    }


                }
            });
            adapterCottonRecycle.notifyDataSetChanged();

        } else if (stringArray == null || stringArray.length == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }
}
