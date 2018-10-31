package com.durinsday.blitzdriver;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.durinsday.blitzdriver.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class DriverTripDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    TextView textDate;
    TextView textFee;
    TextView textBaseFare;
    TextView textTime;
    TextView textDistance;
    TextView textEstimatedPayout;
    TextView textFrom;
    TextView textTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textDate = findViewById(R.id.textDate);
        textFee = findViewById(R.id.textFee);
        textBaseFare = findViewById(R.id.textBaseFare);
        textTime = findViewById(R.id.textTime);
        textDistance = findViewById(R.id.textDistance);
        textEstimatedPayout = findViewById(R.id.textEstimatedPayout);
        textFrom = findViewById(R.id.textFrom);
        textTo = findViewById(R.id.textTo);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        settingInformation();
    }

    private void settingInformation() {
        if (getIntent() != null){
            Calendar calendar = Calendar.getInstance();
            String date = String.format("%s, %d/%d", convertToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH));
            textDate.setText(date);
            textFee.setText(String.format("Rs %.2f", getIntent().getDoubleExtra("total",0.0)));
            textEstimatedPayout.setText(String.format("Rs %.2f", getIntent().getDoubleExtra("total",0.0)));
            textBaseFare.setText(String.format("Rs %.2f", Common.base_fare));
            textTime.setText(String.format("%s min", getIntent().getStringExtra("time")));
            textDistance.setText(String.format("%s km", getIntent().getStringExtra("distance")));
            textFrom.setText(getIntent().getStringExtra("start_address"));
            textTo.setText(getIntent().getStringExtra("end_address"));

            String[] location_end = getIntent().getStringExtra("location_end").split(",");
            LatLng dropOff = new LatLng(Double.parseDouble(location_end[0]),Double.parseDouble(location_end[1]));

            mMap.addMarker(new MarkerOptions().position(dropOff)
            .title("END TRIP")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropOff, 12.0f));

        }
    }

    private String convertToDayOfWeek(int day) {
        switch (day)
        {
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";

                default:
                    return "UNK";


        }
    }
}
