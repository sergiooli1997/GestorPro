package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.FCMBody;
import com.escom.gestorpro.models.FCMResponse;
import com.escom.gestorpro.retrofit.IFCMApi;
import com.escom.gestorpro.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
