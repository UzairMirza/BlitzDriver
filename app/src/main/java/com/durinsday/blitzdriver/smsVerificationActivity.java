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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.durinsday.blitzdriver.Model.BlitzDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class smsVerificationActivity extends AppCompatActivity {

    EditText verificaionCode;
    EditText phoneNumber;
    TextView phoneCode;
    Button btnVerify;
    Button btnVerificationCode;
    LinearLayout layoutVerification;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;
    ProgressDialog loadingBar;
    String phNumber;
    String intName;
    String intEmail;
    String intPass;

    String carType = "uberX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        verificaionCode = findViewById(R.id.verificationCode);
        phoneCode = findViewById(R.id.phoneCode);
        phoneNumber = findViewById(R.id.phoneNumber);
        btnVerify = findViewById(R.id.btnVerify);
        btnVerificationCode = findViewById(R.id.btnVerificationCode);
        layoutVerification = findViewById(R.id.layoutVerification);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.user_driver_table);
        loadingBar = new ProgressDialog(this);

        intName = getIntent().getStringExtra("intName");
        intEmail = getIntent().getStringExtra("intEmail");
        intPass = getIntent().getStringExtra("intPass");



        btnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phNumber = phoneCode.getText().toString() + phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phNumber) || phNumber.length()<13){
                    Toast.makeText(smsVerificationActivity.this, "Please enter correct Phone Number", Toast.LENGTH_SHORT).show();
                }
                if(phNumber.length()>13){
                    Toast.makeText(smsVerificationActivity.this, "Maximum 10 digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    phoneCode.setVisibility(View.INVISIBLE);
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("Verification in progress");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            smsVerificationActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVerificationCode.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);

                String vCode = verificaionCode.getText().toString();
                if (TextUtils.isEmpty(vCode)){
                    Toast.makeText(smsVerificationActivity.this, "Enter the Code", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setTitle("Code Verification");
                    loadingBar.setMessage("Verification in progress");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vCode);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(smsVerificationActivity.this, "Invalid. Please enter correct number", Toast.LENGTH_SHORT).show();

                btnVerificationCode.setVisibility(View.VISIBLE);
                phoneNumber.setVisibility(View.VISIBLE);

                layoutVerification.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();
                Toast.makeText(smsVerificationActivity.this, "Verification code has been sent", Toast.LENGTH_SHORT).show();

                btnVerificationCode.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);

                layoutVerification.setVisibility(View.VISIBLE);


            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            RegisterDriver(intEmail, intPass, intName, phNumber);
                            loadingBar.dismiss();
                            Toast.makeText(smsVerificationActivity.this, "Congratulations. You are now verified.", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().toString();
                            Toast.makeText(smsVerificationActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void RegisterDriver(final String email, final String pass, final String name, final String phNumber) {
        loadingBar.setTitle("Driver Registration");
        loadingBar.setMessage("Please wait while registration is in progress");
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String id = users.push().getKey();
                        final BlitzDriver blitzDriver = new BlitzDriver(email, pass, name, phNumber, id, carType);
                        blitzDriver.setEmail(email);
                        blitzDriver.setPassword(pass);
                        blitzDriver.setName(name);
                        blitzDriver.setPhone(phNumber);
                        blitzDriver.setCarType(carType);
                        users.child(id).setValue(blitzDriver)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference(Common.user_driver_table)
                                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Common.currentBlitzDriver = dataSnapshot.getValue(BlitzDriver.class);
                                                        startActivity(new Intent(smsVerificationActivity.this,MainActivity.class));
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                        Toast.makeText(smsVerificationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(smsVerificationActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(smsVerificationActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                });

    }
}
