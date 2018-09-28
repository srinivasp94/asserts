package com.example.pegasys.rapmedixuser.activity.payment;

import org.apache.http.NameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by pegasys on 3/27/2018.
 */

public class ServiceHandler {
    static String response = null;
    public static final int GET = 1;
    public static final int POST = 2;
    HttpURLConnection urlConnection;


    public String makeservieeCall(String url, int method) {
        return this.makeservieeCall(url, method, null);
    }

    public String makeservieeCall(String url, int method,List<NameValuePair> params) {
        StringBuilder builder = new StringBuilder();
        try {

            URL url1 = new URL(url.replace("", "%20"));
            urlConnection = (HttpURLConnection) url1.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();
        }
        return builder.toString();
    }
}
