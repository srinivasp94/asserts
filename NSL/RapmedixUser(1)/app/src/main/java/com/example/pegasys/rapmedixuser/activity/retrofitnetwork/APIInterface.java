package com.example.pegasys.rapmedixuser.activity.retrofitnetwork;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by sys on 12/6/2017.
 */

public interface APIInterface {
    @POST
    Call<Object> callPost(@Url String path, @Body Object o);

    @GET
    Call<Object> callGet(@Body Object o);
}
