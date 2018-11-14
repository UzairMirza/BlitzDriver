package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ProfileChangeActivity extends AppCompatActivity {

    EditText newFirstName, newLastName, newPhoneNumber;
    TextView newPhoneCode;
    Button editInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);

        newFirstName = findViewById(R.id.newFirstName);
        newLastName = findViewById(R.id.newLastName);
        newPhoneCode = findViewById(R.id.newPhoneCode);
        newPhoneNumber = findViewById(R.id.newPhoneNumber);
        editInfo = findViewById(R.id.editInfo);

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newFirstName.getText().toString() + " " + newLastName.getText().toString();
                String phNumber = newPhoneCode.getText().toString() + newPhoneNumber.getText().toString();

                Map<String, Object> updateInfo = new HashMap<>();
                if(!TextUtils.isEmpty(name)){
                    updateInfo.put("name", name);
                }
                if (TextUtils.isEmpty(newFirstName.getText().toString()) && TextUtils.isEmpty(newLastName.getText().toString())){
                    String tname = Common.currentBlitzDriver.getName();
                    updateInfo.put("name", tname);
                }
                if (!TextUtils.isEmpty(phNumber)){
                    updateInfo.put("phone", phNumber);
                }
                if (TextUtils.isEmpty(newPhoneNumber.getText().toString())){
                    String tphNumber = Common.currentBlitzDriver.getPhone();
                    updateInfo.put("phone", tphNumber);
                }
                DatabaseReference driverInformations = FirebaseDatabase.getInstance().getReference(Common.user_driver_table);
                driverInformations.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendToHome();
                                    Toast.makeText(ProfileChangeActivity.this, "Information Updated!", Toast.LENGTH_SHORT).show();
                                }
                                else {Toast.makeText(ProfileChangeActivity.this, "Information Update Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void sendToHome() {
        Intent intent = new Intent(ProfileChangeActivity.this, DriverHomeActivity.class);
        startActivity(intent);
    }
}