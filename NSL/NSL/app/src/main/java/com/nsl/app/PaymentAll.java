package com.nsl.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PaymentAll extends AppCompatActivity {
    TextView tv_pay_type,tv_name,tv_amt,tv_mode,tv_amount ,tv_type,tv_total_amount,tv_Payment_mode,title_RTGS,colonfeedback,textViewrtgs,tv_pay_date;
   TextView title_dateoncheque,colondateoncheque,tv_dateoncheque,title_chequeno,colonchequeno,tv_chequeno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdetails);
        tv_name = (TextView)findViewById(R.id.tv_name) ;
        tv_amount = (TextView)findViewById(R.id.tv_amount) ;
        tv_mode = (TextView)findViewById(R.id.tv_mode) ;
        tv_type = (TextView)findViewById(R.id.tv_type) ;
        tv_total_amount = (TextView)findViewById(R.id.tv_total_amount) ;
        tv_Payment_mode = (TextView)findViewById(R.id.tv_Payment_mode) ;
        textViewrtgs = (TextView)findViewById(R.id.textViewrtgs) ;
        title_RTGS = (TextView)findViewById(R.id.title_RTGS) ;
        colonfeedback = (TextView)findViewById(R.id.colonfeedback) ;
        tv_pay_date = (TextView)findViewById(R.id.tv_pay_date) ;
        tv_dateoncheque = (TextView)findViewById(R.id.tv_dateoncheque) ;
        tv_chequeno = (TextView)findViewById(R.id.tv_chequeno) ;
        colonchequeno = (TextView)findViewById(R.id.colonchequeno) ;
        title_chequeno = (TextView)findViewById(R.id.title_chequeno) ;
        title_dateoncheque = (TextView)findViewById(R.id.title_dateoncheque) ;
        colondateoncheque = (TextView)findViewById(R.id.colondateoncheque) ;
        tv_dateoncheque = (TextView)findViewById(R.id.tv_dateoncheque) ;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(newfedback);

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(),Paymentcollection_All_Activity.class);
                startActivity(home);
                finish();
            }
        });

        tv_name.setText(getIntent().getStringExtra("name"));
        tv_amount.setText(getIntent().getStringExtra("amount"));
        tv_mode.setText(getIntent().getStringExtra("mode"));
        tv_type.setText(getIntent().getStringExtra("type"));
        tv_total_amount.setText(getIntent().getStringExtra("total_amount"));
        tv_Payment_mode.setText(getIntent().getStringExtra("payment_mode"));
        String date = getIntent().getStringExtra("pay_date");
        if(date.contains("00:00:00")){
            date = date.substring(0, date.length() - 8);
            tv_pay_date.setText(date);
        }
       else{
            tv_pay_date.setText(getIntent().getStringExtra("pay_date"));
        }

        String pay_mode = tv_Payment_mode.getText().toString();
        if(pay_mode.equalsIgnoreCase("Online")){
            title_chequeno.setVisibility(View.GONE);
            colonchequeno.setVisibility(View.GONE);
            tv_chequeno.setVisibility(View.GONE);

            title_RTGS.setVisibility(View.VISIBLE);
            colonfeedback.setVisibility(View.VISIBLE);
            textViewrtgs.setVisibility(View.VISIBLE);
            textViewrtgs.setText(getIntent().getStringExtra("rtgs"));
        }
        else if(pay_mode.equalsIgnoreCase("Cheque/DD")){
            title_RTGS.setVisibility(View.GONE);
            colonfeedback.setVisibility(View.GONE);
            textViewrtgs.setVisibility(View.GONE);

            title_dateoncheque.setVisibility(View.VISIBLE);
            colondateoncheque.setVisibility(View.VISIBLE);
            tv_dateoncheque.setVisibility(View.VISIBLE);
            String datec = getIntent().getStringExtra("dateoncheque");
            datec = datec.substring(0, datec.length() - 8);
            tv_dateoncheque.setText(datec);

            title_chequeno.setVisibility(View.VISIBLE);
            colonchequeno.setVisibility(View.VISIBLE);
            tv_chequeno.setVisibility(View.VISIBLE);
            tv_chequeno.setText(getIntent().getStringExtra("chequeno"));
        }


    }

}
