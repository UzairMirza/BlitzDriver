package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.durinsday.blitzdriver.Common.Common;

public class NavProfileActivity extends AppCompatActivity {

    TextView profileName, profilePhone, textClick, profileVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_profile);

        profileName = findViewById(R.id.profileName);
        profilePhone = findViewById(R.id.profileNumber);
        textClick = findViewById(R.id.textClick);
        profileVehicle = findViewById(R.id.profileVehicleType);

        profileName.setText(Common.currentBlitzDriver.getName());
        profilePhone.setText(Common.currentBlitzDriver.getPhone());
        profileVehicle.setText(Common.currentBlitzDriver.getCarType());

        textClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavProfileActivity.this, NavSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
