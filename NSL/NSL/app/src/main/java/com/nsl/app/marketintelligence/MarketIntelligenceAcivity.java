package com.nsl.app.marketintelligence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.nsl.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketIntelligenceAcivity extends AppCompatActivity {

    private GridView gridView;
    String[] Name={"Commodity Price","Crop Shifts","Pricing Survey","Product Survey"};//
    int[] flags = new int[]{
            R.mipmap.ic_commodityprice,
            R.mipmap.ic_cropshift,
            R.mipmap.ic_pricingsurvey,
            R.mipmap.ic_productsurvey,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_intelligence_acivity);


        Toolbar toolbar   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<4;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt",  Name[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "txt","flag" };

        // Ids of views in listview_layout
        int[] to = { R.id.tv_type,R.id.imageView2};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), aList, R.layout.row_market_intelligence_selection, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                if (i==0){
                    Intent premap = new Intent(getApplicationContext(),CommodityPriceAllActivity.class);
                    startActivity(premap);
                }

                else if (i==1){
                    Intent premap = new Intent(getApplicationContext(),CropShiftAllActivity.class);
                    startActivity(premap);
                }

                else if (i==2){
                    Intent premap = new Intent(getApplicationContext(),PricingSurveyAllActivity.class);
                    startActivity(premap);
                }
                else{

                    Intent postmap = new Intent(getApplicationContext(),ProductSurveyAllActivity.class);
                    startActivity(postmap);
                }
            }
        });
    }
}
