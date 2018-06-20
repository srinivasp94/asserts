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
import android.widget.Toast;

import com.nsl.app.R;

/**
 * Created by admin on 4/26/2017.
 */
public class BajraCottonFragment extends Fragment {
    private RecyclerView recyclerView;
    private Adapter_cotton_recycle adapter_cotton_recycle;
    int dd,mm,yy;
    String date;
    private Context context;

    public BajraCottonFragment(){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bajra_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview__bajra);
        final FragmentActivity activity = getActivity();
       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        Adapter_cotton_recycle adapterCottonRecycle = new Adapter_cotton_recycle(getContext(),new String[]{"KAREENA-P10G ","NBH-225-P250G"});

        recyclerView.setAdapter(adapterCottonRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/
        setListOnAdapter(new String[]{"KAREENA-P10G ","NBH-225-P250G"});
        return rootView;

    }


    public void setListOnAdapter(final String [] stringArray) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (stringArray.length != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            adapter_cotton_recycle = new Adapter_cotton_recycle( BajraCottonFragment.this.getContext(), stringArray);
            recyclerView.setAdapter(adapter_cotton_recycle);
            adapter_cotton_recycle.setOnItemClickListener(new Adapter_cotton_recycle.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
//                    TextView stockPlaced = (TextView) view.findViewById(R.id.TextView_stockplaced);
//                    TextView currentStock = (TextView) view.findViewById(R.id.TextView_current_stock);
//                    TextView pog = (TextView) view.findViewById(R.id.TextView_pog);
//
//                    TextView nameOfDealer = (TextView) view.findViewById(R.id.tv_dealer_name_cotton);

                    switch (view.getId()){
                        case R.id.TextView_stockplaced:
                            Toast.makeText(getActivity(),"TextView_stockplaced",Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            final View view1 = inflater.inflate(R.layout.stock_placed_popup,null);

                            final EditText txt_date=(EditText)view1.findViewById(R.id.datepicker);
                            EditText qnty_places=(EditText)view1.findViewById(R.id.edittext_quantity_placed);
                            ImageView ok=(ImageView)view1.findViewById(R.id.buttton_stock_placed_ok);
                            Button close=(Button)view1.findViewById(R.id.button_stock_placed_close);

                            txt_date.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                            dd=i;
                                            mm=i1 + 1;
                                            yy=i2;
                                            date = "Choosen date is :" + i + "/" + i1 + "/"
                                                    + i2;

                                            txt_date.setText(date);

                                        }
                                    };

                                                                    }
                            });
                            String quantityPlaced=qnty_places.getText().toString();
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    builder.setCancelable(true);
                                }
                            });


                            builder.setView(view1);
                            builder.create();
                            builder.show();
                            break;
                        case R.id.TextView_current_stock:
                            Toast.makeText(getActivity(),"textview_current_stock",Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder_current_stock=new AlertDialog.Builder(getContext());
                            LayoutInflater inflater1 = LayoutInflater.from(getContext());
                            View view_stock = inflater1.inflate(R.layout.current_stock_popup,null);

                            builder_current_stock.setView(view_stock);
                            builder_current_stock.create();
                            builder_current_stock.show();
                            break;
                        case R.id.TextView_pog:
                            Toast.makeText(getActivity(),"textview_pog",Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder_pog=new AlertDialog.Builder(getContext());
                            LayoutInflater inflater2 = LayoutInflater.from(getContext());
                            View view_pog = inflater2.inflate(R.layout.pog_popup,null);

                            builder_pog.setView(view_pog);
                            builder_pog.create();
                            builder_pog.show();
                            break;

                        case R.id.button_r:
                            Toast.makeText(getActivity(),"button retailer",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getActivity(),Stock_for_Retailer.class);
                            startActivity(intent);
                            break;

                    }





                }
            });
            adapter_cotton_recycle.notifyDataSetChanged();

        } else if (stringArray == null || stringArray.length == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }
}
