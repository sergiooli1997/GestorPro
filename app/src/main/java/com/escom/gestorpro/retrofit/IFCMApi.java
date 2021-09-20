package com.escom.gestorpro.retrofit;

import com.escom.gestorpro.models.FCMBody;
import com.escom.gestorpro.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA-oSYVY4:APA91bHXe7x-ICI32aWQmTqOCcv14MoM_sr0CfMr9uQx1C39r9Haj5RJfzuNS3n9GidW3hXWX74dlXhT7CBhkAS4YNFpZv5ZGmCfPyPXaIheYXuMaiHU67hmoHPa-G1F2ZgIle9ms2Ya"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
