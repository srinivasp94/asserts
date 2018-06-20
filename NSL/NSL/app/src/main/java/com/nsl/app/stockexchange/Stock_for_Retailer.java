package com.nsl.app.stockexchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nsl.app.R;

public class Stock_for_Retailer extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_for__retailer);
        listView=(ListView)findViewById(R.id.list_stock_for_retailer);

    }
}
