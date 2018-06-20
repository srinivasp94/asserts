package com.nsl.app.network;


import java.util.ArrayList;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.QueryMap;


public interface RetrofitAPI
{
    /*@POST("/requestprocessor/requestProcessing")
    void callApiService(@Body CommonRequestKey login, Callback<Object> response);

    @Multipart
    @POST("/api?api_token=tatx123")
    void upload(@Part("image") TypedFile file,
                @Part("multipart") String multipart,
                @Part("requestparameters") CommonRequestKey requestparameters,
                Callback<RestResponse> response);


    @Multipart
    @POST("/api?api_token=tatx123")
    void uploadImages(
            @Part("multipart") String multipart,
            @Part("requestparameters") CommonRequestKey requestparameters,
            @PartMap Map<String, TypedFile> files,
            Callback<ApiResponseVo> response);
*/

    /*@GET("/needtosync")
    void callGetAPIS(@FieldMap Map<String, String> params,Callback<Object> response);
*/
    @Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==","Cache-Control: no-cache"})
    @GET("/details")
    void callGetAPIS(@QueryMap Map<String,String>  params, Callback<ArrayList<Object>> response);

    @Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==","Cache-Control: no-cache"})
    @POST("/details")
    void acknowledgeToServerAPIS(@QueryMap Map<String,String>  params, Callback<ArrayList<Object>> response);

    @Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==","Cache-Control: no-cache"})
    @POST("/login")
    @FormUrlEncoded
    void login(@Field("username") String username,@Field("password") String password,@Field("fcm_id") String fcm_id, Callback<Object> response);


}
