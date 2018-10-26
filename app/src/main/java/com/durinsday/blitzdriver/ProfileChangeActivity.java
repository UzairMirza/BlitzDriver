package com.durinsday.blitzdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileChangeActivity extends AppCompatActivity {

    private EditText newFirstName, newLastName, newPhoneNumber;
    private Button editInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);

        newFirstName = findViewById(R.id.newFirstName);
        newLastName = findViewById(R.id.newLastName);
        newPhoneNumber = findViewById(R.id.newPhoneNumber);
        editInfo = findViewById(R.id.editInfo);

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newFirstName.getText().toString() + " " + newLastName.getText().toString();
                String phNumber = newPhoneNumber.getText().toString();

                Map<String, Object> updateInfo = new HashMap<>();
                if(!TextUtils.isEmpty(name)){
                    updateInfo.put("name", name);
                }
                else if (!TextUtils.isEmpty(phNumber)){
                    updateInfo.put("phone", phNumber);
                }
                DatabaseReference driverInformations = FirebaseDatabase.getInstance().getReference(Common.user_driver_table);
                driverInformations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sendToHome();
                                if (task.isSuccessful()){Toast.makeText(ProfileChangeActivity.this, "Information Updated!", Toast.LENGTH_SHORT).show();
                                }
                                else {if (task.isSuccessful()){Toast.makeText(ProfileChangeActivity.this, "Information Update Failed!", Toast.LENGTH_SHORT).show();}
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