package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CarTypeActivity extends AppCompatActivity {

    RadioButton uberX, uberBlack;
    Button btnBack, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type);

        uberX = findViewById(R.id.radioGo);
        uberBlack = findViewById(R.id.radioBooking);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarTypeActivity.this, NavSettingsActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> updateInfo = new HashMap<>();
                if (uberX.isChecked()){
                    updateInfo.put("carType", uberX.getText().toString());
                }
                else if (uberBlack.isChecked()){
                    updateInfo.put("carType", uberBlack.getText().toString());
                }
                DatabaseReference driverInformations = FirebaseDatabase.getInstance().getReference(Common.user_driver_table);
                driverInformations.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendToHome();
                                    Toast.makeText(CarTypeActivity.this, "Vehicle Updated!", Toast.LENGTH_SHORT).show();
                                }
                                else {Toast.makeText(CarTypeActivity.this, "Vehicle Update Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }

    private void sendToHome() {
        Intent intent = new Intent(CarTypeActivity.this, DriverHomeActivity.class);
        startActivity(intent);
    }
}
