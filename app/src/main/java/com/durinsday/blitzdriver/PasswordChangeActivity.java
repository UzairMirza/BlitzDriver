package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText oldPass, newPass, newPass2;
    private Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        oldPass = findViewById(R.id.oldPassword);
        newPass = findViewById(R.id.newPassword);
        newPass2 = findViewById(R.id.newPassword2);
        btnChangePass = findViewById(R.id.btnChangePassword);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPass.getText().toString().equals(newPass2.getText().toString())){
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass.getText().toString());
                    FirebaseAuth.getInstance().getCurrentUser()
                            .reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseAuth.getInstance().getCurrentUser()
                                                .updatePassword(newPass2.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Map<String,Object> password = new HashMap<>();
                                                            password.put("password",newPass2.getText().toString());
                                                            DatabaseReference driverInformation = FirebaseDatabase.getInstance().getReference(Common.user_driver_table);
                                                            driverInformation.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .updateChildren(password)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                sendToHome();
                                                                                Toast.makeText(PasswordChangeActivity.this,"Password changed", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            else {Toast.makeText(PasswordChangeActivity.this,"Password was changed but not updated to database", Toast.LENGTH_SHORT).show();}
                                                                        }
                                                                    });

                                                        }
                                                        else {
                                                            Toast.makeText(PasswordChangeActivity.this,"Password can't change", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        Toast.makeText(PasswordChangeActivity.this,"Incorrect Old Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(PasswordChangeActivity.this,"New Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToHome() {
        Intent intent = new Intent(PasswordChangeActivity.this, DriverHomeActivity.class);
        startActivity(intent);
    }
}
