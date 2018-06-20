package com.nsl.app.network;




import java.util.ArrayList;
import java.util.Map;

public interface RetrofitResponseListener
{


    void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId);



}
