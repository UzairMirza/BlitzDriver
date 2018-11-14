package com.durinsday.blitzdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.durinsday.blitzdriver.Model.BlitzDriver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    Button signIn, btnForgotPassword;
    EditText driverEmail2, driverPassword2;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        signIn = findViewById(R.id.btnSignIn2);
        driverEmail2 = findViewById(R.id.driverEmail2);
        driverPassword2 = findViewById(R.id.driverPassword2);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = driverEmail2.getText().toString();
                String pass = driverPassword2.getText().toString();

                DriverSignIn(email, pass);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }



    private void DriverSignIn(final String email, final String pass) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(SignInActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(SignInActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Driver SignIn");
            loadingBar.setMessage("Authentication in progress");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    loadingBar.dismiss();
                    FirebaseDatabase.getInstance().getReference(Common.user_driver_table)
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Paper.book().write(Common.user_field,email);
                                    Paper.book().write(Common.pwd_field,pass);

                                    Common.currentBlitzDriver = dataSnapshot.getValue(BlitzDriver.class);
                                    startActivity(new Intent(SignInActivity.this,DriverHomeActivity.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });
        }
    }
}
