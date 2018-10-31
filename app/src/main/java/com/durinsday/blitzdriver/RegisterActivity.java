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
import android.widget.TextView;
import android.widget.Toast;

import com.durinsday.blitzdriver.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister2;
    TextView toLogin, toTerms;
    EditText driverEmail, driverPassword;
    EditText firstName;
    EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister2 = findViewById(R.id.btnRegister2);
        toLogin = findViewById(R.id.toLogin);
        driverEmail = findViewById(R.id.driverEmail);
        driverPassword = findViewById(R.id.driverPassword);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        toTerms = findViewById(R.id.toTerms);


        toTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });

        btnRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = driverEmail.getText().toString();
                String pass = driverPassword.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String name = fName + " " + lName;

                RegisterDriver(name, email, pass);
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


    }

    private void RegisterDriver(final String name, final String email, final String pass) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        if (pass.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Minimum 8 characters are required", Toast.LENGTH_SHORT).show();
        } else {
            Intent nextIntent = new Intent(getApplicationContext(), smsVerificationActivity.class);
            nextIntent.putExtra("intName", name);
            nextIntent.putExtra("intEmail", email);
            nextIntent.putExtra("intPass", pass);
            startActivity(nextIntent);
        }
    }
}