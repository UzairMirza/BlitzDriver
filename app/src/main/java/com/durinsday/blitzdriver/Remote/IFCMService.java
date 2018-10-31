package com.durinsday.blitzdriver.Remote;

import com.durinsday.blitzdriver.Model.FCMResponse;
import com.durinsday.blitzdriver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA0FI_e6w:APA91bEaau6JA51Cascc_kkb3g-4DtEDVT-vu__Qiix9mzR-tj7qajyMpupc0YdIzWS8McQKtbCJFAzQoOoSQf77LWV1AdHhbNLTyUSaZuC1K_8WYtrmgmX0d0G9CY7JwdVK6MBuxmba"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
