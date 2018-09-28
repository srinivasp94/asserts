package com.example.pegasys.rapmedixuser.activity.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.newactivities.DiagnosticsPageSubCatActivity;
import com.example.pegasys.rapmedixuser.activity.pojo.diagnostics.DiagnosticsSubcatList;
import com.example.pegasys.rapmedixuser.activity.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pegasys on 2/15/2018.
 */

public class DiagnosticssubcatAdapter extends RecyclerView.Adapter<DiagnosticssubcatAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DiagnosticsSubcatList> mDiagnosticsSubcatLists;
    private ArrayList<String> subservice_idList = new ArrayList<>();
    private DiagnosticsPageSubCatActivity diagnosticsPageSubCatActivity;
    private Boolean[] multiSelectionStatus;
    private Button btn_proceed;
    private TextView txt_Count, txt_itemCounttoolbar;
    private int status;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFERENCE_NAME = "cartPrefer";

    public DiagnosticssubcatAdapter(Context mContext, int status, ArrayList<DiagnosticsSubcatList> mDiagnosticsSubcatLists, ArrayList<String> subservice_idList, DiagnosticsPageSubCatActivity diagnosticsPageSubCatActivity, Button btn_proceed, TextView txt_Count, TextView txt_Itemcount) {

        this.mContext = mContext;
        this.mDiagnosticsSubcatLists = mDiagnosticsSubcatLists;
        this.subservice_idList = subservice_idList;
        this.diagnosticsPageSubCatActivity = diagnosticsPageSubCatActivity;
        this.btn_proceed = btn_proceed;
        this.status = status;
        multiSelectionStatus = new Boolean[mDiagnosticsSubcatLists.size()];
        this.txt_Count = txt_Count;
        this.txt_itemCounttoolbar = txt_Itemcount;
        Arrays.fill(multiSelectionStatus, Boolean.FALSE);
        sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private OnItemClickListener mListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public ArrayList<DiagnosticsSubcatList> getSelectedList() {

        ArrayList<DiagnosticsSubcatList> dataModels = new ArrayList<>();

        for (int i = 0; i < mDiagnosticsSubcatLists.size(); i++) {
            if (mDiagnosticsSubcatLists.get(i).isClicked) {
                dataModels.add(mDiagnosticsSubcatLists.get(i));
                subservice_idList.add(mDiagnosticsSubcatLists.get(i).id);

            }
        }

        return dataModels;

    }

//------------------------------------------------------------------------------------------

    public ArrayList<DiagnosticsSubcatList> getlistsizeForboolean() {

        ArrayList<DiagnosticsSubcatList> dataModels = new ArrayList<>();

        for (int i = 0; i < mDiagnosticsSubcatLists.size(); i++) {
            if (mDiagnosticsSubcatLists.get(i).isClicked) {
                dataModels.add(mDiagnosticsSubcatLists.get(i));
                subservice_idList.add(mDiagnosticsSubcatLists.get(i).id);


//                if (mDiagnosticsSubcatLists.get(i).isClicked) {

                if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
                    if (!Constants.cartDiagnosticscenters.contains(mDiagnosticsSubcatLists.get(i))) {
                        ArrayList<String> allIDs = new ArrayList<>();

                        for (int j = 0; j < Constants.cartDiagnosticscenters.size(); j++) {
                            allIDs.add(Constants.cartDiagnosticscenters.get(j).id);
                        }
                        // if (!Constants.cartDiagnosticscenters.contains(mDiagnosticsSubcatLists.get(i))) {
                        Log.i("Comstants", Constants.cartDiagnosticscenters + " and " + (mDiagnosticsSubcatLists.get(i)));
                        if (!allIDs.contains(mDiagnosticsSubcatLists.get(i).id)) {
//                                Log.i("itemsPrint", "" + Constants.cartDiagnosticscenters.get(j));
//                                Toast.makeText(activity, "qwertyuiop", Toast.LENGTH_SHORT).show();
                            Constants.cartDiagnosticscenters.add(mDiagnosticsSubcatLists.get(i));
                            break;
                        } else {

//                                break;
                        }

//                            txt_itemCounttoolbar.setText("" + Constants.cartDiagnosticscenters.size());
                        // } else {
                        //      Toast.makeText(activity, "Already Added", Toast.LENGTH_SHORT).show();
                        //  }

//                        }
                    } else {
//                        Constants.cartDiagnosticscenters.remove(mDiagnosticsSubcatLists.get(i));
                    }
                } else {
                    Constants.cartDiagnosticscenters.add(mDiagnosticsSubcatLists.get(i));
//                    txt_itemCounttoolbar.setText("" + Constants.cartDiagnosticscenters.size());
                }
//                }
                /*else
//                    if (mDiagnosticsSubcatLists.get(i).isClicked)
                {
                    if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
                        for (int j = 0; j < Constants.cartDiagnosticscenters.size(); j++) {
                            if (Constants.cartDiagnosticscenters.get(j).equals(mDiagnosticsSubcatLists.get(i))) {
                                Constants.cartDiagnosticscenters.remove(j);
                                txt_itemCounttoolbar.setText("" + Constants.cartDiagnosticscenters.size());
                            }
                        }
                    }

                }*/


            } else {
                if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
                    for (int j = 0; j < Constants.cartDiagnosticscenters.size(); j++) {
                        if (Constants.cartDiagnosticscenters.get(j).id.equals(mDiagnosticsSubcatLists.get(i).id)) {
                            Constants.cartDiagnosticscenters.remove(j);
                            mDiagnosticsSubcatLists.get(i).setClicked(false);
                            txt_itemCounttoolbar.setText("" + Constants.cartDiagnosticscenters.size());
                            notifyDataSetChanged();
                        }
                    }
                }

            }
        }
        Log.i("Total Count",""+Constants.cartDiagnosticscenters.size());
        return dataModels;

    }


    public ArrayList<String> getSelectedListOfIds() {

        ArrayList<String> dataModels = new ArrayList<>();

        for (int i = 0; i < multiSelectionStatus.length; i++) {
            if (multiSelectionStatus[i]) {
                subservice_idList.add(mDiagnosticsSubcatLists.get(i).id);
            }
        }

        return dataModels;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcat_card_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final DiagnosticsSubcatList diagnosticsSubcat = mDiagnosticsSubcatLists.get(position);
        holder.text_diagnosticsname.setText(diagnosticsSubcat.subserviceName);

        if (Constants.cartDiagnosticscenters != null && Constants.cartDiagnosticscenters.size() > 0) {
            for (int i = 0; i < Constants.cartDiagnosticscenters.size(); i++) {
                if (Constants.cartDiagnosticscenters.get(i).id.equals(diagnosticsSubcat.id)) {
                    mDiagnosticsSubcatLists.get(position).setClicked(true);
                    // diagnosticsSubcat.setClicked(true);
                }
            }
        }

       /* if (multiSelectionStatus[position]) {

            holder.btn_AddToCart.setBackgroundColor(Color.RED);
            holder.btn_AddToCart.setTextColor(Color.WHITE);


        } else {
            holder.btn_AddToCart.setBackgroundResource(R.drawable.bgr_slots);
            holder.btn_AddToCart.setTextColor(Color.RED);

        }*/
     /*  if (Constants.cartDiagnosticscenters != null)
       for (int a = 0; a<Constants.cartDiagnosticscenters.size(); a++) {

       }*/


        if (mDiagnosticsSubcatLists.get(position).isClicked) {

            holder.btn_AddToCart.setBackgroundColor(Color.RED);
            holder.btn_AddToCart.setTextColor(Color.WHITE);


        } else if (!mDiagnosticsSubcatLists.get(position).isClicked) {
            holder.btn_AddToCart.setBackgroundResource(R.drawable.bgr_slots);
            holder.btn_AddToCart.setTextColor(Color.RED);

        }
        if (status == 1) {
            holder.btn_AddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // multiSelectionStatus[position] = !multiSelectionStatus[position];
                    if (mDiagnosticsSubcatLists.get(position).isClicked) {
                        mDiagnosticsSubcatLists.get(position).setClicked(false);
                        holder.btn_AddToCart.setBackgroundResource(R.drawable.bgr_slots);
                        holder.btn_AddToCart.setTextColor(Color.RED);
                    } else if (!mDiagnosticsSubcatLists.get(position).isClicked) {
                        mDiagnosticsSubcatLists.get(position).setClicked(true);
                        holder.btn_AddToCart.setBackgroundColor(Color.RED);
                        holder.btn_AddToCart.setTextColor(Color.WHITE);
                    }
                    notifyDataSetChanged();
               /* if (diagnosticsSubcat.isClicked) {

                    holder.btn_AddToCart.setBackgroundColor(Color.RED);
                    holder.btn_AddToCart.setTextColor(Color.WHITE);


                }else if(!diagnosticsSubcat.isClicked){
                    holder.btn_AddToCart.setBackgroundResource(R.drawable.bgr_slots);
                    holder.btn_AddToCart.setTextColor(Color.RED);

                }*/
                    Log.i("count", "" + getSelectedList().size());
                    getlistsizeForboolean();
                    txt_itemCounttoolbar.setText("" + Constants.cartDiagnosticscenters.size());
                    if (subservice_idList != null) {
                        txt_Count.setText(" " + getSelectedList().size() + " Tests Selected");
                    } else {
                        txt_Count.setText(" " + "0" + " Tests Selected");
                    }
                    notifyDataSetChanged();


                /*if (diagnosticsSubcat.getClicked()) {
                    diagnosticsSubcat.setClicked(false);
                    subservice_idList.remove(diagnosticsSubcat.id);
                    if (subservice_idList.size() > 0) {
                        Log.i("subservice_idList", subservice_idList.get(position));
                    }
                    notifyDataSetChanged();

//                    holder.btn_AddToCart.setBackgroundResource(R.drawable.bgr_slots);
//                    holder.btn_AddToCart.setTextColor(Color.RED);


                } else {
                    diagnosticsSubcat.setClicked(true);

                    subservice_idList.add(diagnosticsSubcat.id);
                    Log.i("subservice_idList.size", "" + subservice_idList.size());
//                    Log.i("subservice_idList", subservice_idList.get(position));
                    notifyDataSetChanged();

//                    holder.btn_AddToCart.setBackgroundColor(Color.RED);
//                    holder.btn_AddToCart.setTextColor(Color.WHITE);

                }*/
                }
            });


        } else {
            holder.btn_AddToCart.setVisibility(View.GONE);
        }


        Gson gson = new Gson();
        String lists = gson.toJson(getSelectedList());
        editor.putString("stringObject", lists);
//                editor.putInt("count", adapter.getSelectedList().size());
//                txt_count.setText(""+ adapter.getSelectedList().size());
        editor.commit();
        Log.i("shared", sharedPreferences.toString());

    }


    @Override
    public int getItemCount() {
        return mDiagnosticsSubcatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_diagnosticsname;
        Button btn_AddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            text_diagnosticsname = itemView.findViewById(R.id.tv_docspectitle);
            btn_AddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            btn_AddToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
