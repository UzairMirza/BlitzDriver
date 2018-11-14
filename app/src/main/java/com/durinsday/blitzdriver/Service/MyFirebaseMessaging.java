package com.durinsday.blitzdriver.Service;

import android.content.Intent;

import com.durinsday.blitzdriver.Common.Common;
import com.durinsday.blitzdriver.CustomerCall;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {

            Map<String, String> data = new HashMap<>();
            String customer = data.get("customer");
            String lat = data.get("lat");
            String lng = data.get("lng");

            Intent intent = new Intent(getBaseContext(), CustomerCall.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("customer", customer);
            startActivity(intent);
        }
    }
}
