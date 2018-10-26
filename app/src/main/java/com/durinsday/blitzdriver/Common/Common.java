package com.durinsday.blitzdriver.Common;

import android.location.Location;

import com.durinsday.blitzdriver.Model.User;
import com.durinsday.blitzdriver.Remote.FCMClient;
import com.durinsday.blitzdriver.Remote.IFCMService;
import com.durinsday.blitzdriver.Remote.IGoogleAPI;
import com.durinsday.blitzdriver.Remote.RetrofitClient;

public class Common {



    public static final String driver_table = "Drivers";
    public static final String user_driver_table = "DriversInformation";
    public static final String user_rider_table = "RidersInformation";
    public static final String pickup_request_table = "PickupRequest";
    public static final String token_table = "Tokens";


    public static User currentUser;

    public static Location mLastLocation = null;


    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static IFCMService getFCMService(){
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

}
