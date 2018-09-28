package com.example.pegasys.rapmedixuser.activity.retrofitnetwork;

import com.example.pegasys.rapmedixuser.activity.ImageResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by sys on 12/6/2017.
 */

public interface APIInterface {
    @POST
    Call<Object> callPost(@Url String path, @Body Object o);

    @GET
    Call<Object> callGet(@Body Object o);

    @Multipart
    @POST
    Call<ImageResponse> uploadFile(@Url String path,
                                   @Part MultipartBody.Part file);
}
