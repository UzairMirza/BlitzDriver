package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavSettingsActivity extends AppCompatActivity {

    Button changePass, changeProf, changeCarType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_settings);

        changePass = findViewById(R.id.changePassword);
        changeProf = findViewById(R.id.changeProfileInfo);
        changeCarType = findViewById(R.id.changeCarType);

        changeProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavSettingsActivity.this,ProfileChangeActivity.class);
                startActivity(intent);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavSettingsActivity.this,PasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        changeCarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavSettingsActivity.this,CarTypeActivity.class);
                startActivity(intent);
            }
        });
    }
}
