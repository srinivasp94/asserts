package com.example.pegasys.rapmedixuser.activity;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by rk on 06-Jan-18.
 */

public interface ApiInterface {

//    @Multipart
//    @POST("/user/userprofile_update_service")
//    Call<ImageResponse> sendImage(@Part MultipartBody.Part body);

    @Multipart
    @POST
    Call<ResponseBody> uploadFile(@Url String path,
                                  @Part MultipartBody.Part file);
}
