package com.example.pegasys.rapmedixuser.activity.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.pegasys.rapmedixuser.activity.network.OnAsyncCompleteRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class CustomAsync extends AsyncTask<String, String, String> {
	ProgressDialog pd=null;
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		
		caller.asyncResponse(result);
//		try{
//			if(pd!=null)
//			{
//				pd.dismiss();
//				pd=null;
//			}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//super.onPreExecute();
//		if(pd==null){
//			pd=new ProgressDialog(con);
//			pd.setMessage("Loading");
//			pd.show();
//			pd.setCancelable(false);
//			pd.setCanceledOnTouchOutside(false);
//
//			}
	}

	Context con;
	JSONObject job;
	String url="";
	OnAsyncCompleteRequest caller;
	String otp = "";
	String mobile = "";
	
	public CustomAsync(Context con, JSONObject jo, String url, OnAsyncCompleteRequest oas) {
		// TODO Auto-generated constructor stub
		this.con=con;
		job=jo;
		this.url=url;
		caller=oas;
		//this.otp = otp;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
			String text="";
			BufferedReader reader = null;
			
			try {
				//URL uri = new URL ("http://combauserapp.nedsupport.in/mobile.php");
				URL uri = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setReadTimeout(50000);
				conn.setConnectTimeout(50000);
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(job.toString());
				wr.flush();
				
				if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
				{
				
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					
					sb.append(line);
					
				}
				text = sb.toString();
				Log.e("text",text);
				}
				
			}
			catch (Exception ex){
				Log.e("Excep",ex.toString());
			}
			finally {
				try {
					reader.close();
//					if(pd!=null)
//					{
//						pd.dismiss();
//						pd=null;
//					}
				}
				catch (Exception ex){
					Log.e("Excep1",ex.toString());
				}
				
				
			}
			
			return text;
		
		
	}
	
	

}
