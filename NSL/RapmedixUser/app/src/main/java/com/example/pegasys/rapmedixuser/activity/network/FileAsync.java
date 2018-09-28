package com.example.pegasys.rapmedixuser.activity.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class FileAsync extends AsyncTask<String, Integer, String> {

	OnAsyncCompleteRequest caller;
	Context context;
	// String method = "GET";
	String surl="",path="",vid="";
	URL uri;
	String response="";

	ProgressDialog pDialog = null;
	HashMap<String,String> hmap;
	JSONObject obj;
	String param="vendor_id";



	public FileAsync(Context con, String m, String path, String vid, OnAsyncCompleteRequest oa) {
		context = con;
		//	 Log.e("context",context.toString());

		//  caller = (OnAsyncRequestComplete) context;
		caller=oa;

		this.path=path;
		this.vid=vid;
		surl = m;
		
		// obj=ob;
		// hmap=hm;
		// parameters = p;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//super.onPreExecute();
		if (pDialog == null) {
			pDialog = new ProgressDialog(context);
			pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pDialog.setIndeterminate(true);
			//  progressDialog.setContentView(R.layout.progress);
			// pDialog.setMessage("Please Wait...");
			pDialog.show();
			//pDialog.setContentView(R.layout.cload);
			// progressDialog.setProgressStyle(android.)
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
		}  
	}


	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		int serverResponseCode=0;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		//DataInputStream inStream = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;
		BufferedReader br;
		StringBuilder sb;

		//Log.e("sending data in async",vid+"\n "+surl+"\n "+path);

		// String urlString = "http://your_website.com/upload_audio_test/upload_audio.php";
		try
		{
			//------------------ CLIENT REQUEST
			FileInputStream fileInputStream = new FileInputStream(new File(path) );
			// open a URL connection to the Servlet
			URL url = new URL(surl);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			conn.setRequestProperty("profile_pic", path);
			// conn.setRequestProperty("file_type", type);
			conn.setRequestProperty("upid", vid);
			dos = new DataOutputStream( conn.getOutputStream() );
			/****************************************************************/
			/*dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"upid\""
					+ lineEnd);
			dos.writeBytes(lineEnd);

			dos.writeBytes(vid);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);*/
			/****************************************************************/

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\""
					+ path + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0)
			{
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
		//	Log.e("Debug","File is written");

			try {
				serverResponseCode = conn.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//serverResponseMessage = conn.getResponseMessage();
			//------------------ read the SERVER RESPONSE
			if (serverResponseCode ==200) {
				try {
					/*inStream = new DataInputStream ( conn.getInputStream() );
					String str;

					while (( str = inStream.readLine()) != null)
					{
						//Log.e("Debug","Server Response "+str);
						response+=str;
					}
					inStream.close();*/

					br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					sb = new StringBuilder();
					String output=null;
					while ((output = br.readLine()) != null) {
						sb.append(output+ "\n");
					}
					response=sb.toString();
					//Log.e("response",response+"");
				}


				catch (IOException ioex){
				//	Log.e("Debug", "error: " + ioex.getMessage(), ioex);
				}
			}
			else
			{

			}

			fileInputStream.close();
			dos.flush();
			dos.close();
		}
		catch (MalformedURLException ex)
		{
		//	Log.e("Debug", "error: " + ex.getMessage(), ex);
		}
		catch (IOException ioe)
		{
		//	Log.e("Debug", "error: " + ioe.getMessage(), ioe);
		}

		finally{
			try{
				if(pDialog!=null)
				{
					if (pDialog.isShowing()) {
						pDialog.dismiss();
						pDialog = null;
					}
				}
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}

		return response;


		//return null;
	}

	/* // Interface to be implemented by calling activity
	 public interface OnAsyncRequestComplete {
	  public void asyncResponse(String response);
	 }*/


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		try{
			if(pDialog!=null)
			{
				if (pDialog.isShowing()) {
					pDialog.dismiss();
					pDialog = null;
				}
			}
		}catch(Exception exception){
			exception.printStackTrace();
		}
		caller.asyncResponse(result);
	}
}
