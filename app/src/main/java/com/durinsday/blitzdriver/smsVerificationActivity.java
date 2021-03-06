package com.durinsday.blitzdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.durinsday.blitzdriver.Model.User;
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

    private EditText verificaionCode;
    private EditText phoneNumber;
    private Button btnVerify;
    private Button btnVerificationCode;
    private LinearLayout layoutVerification;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private ProgressDialog loadingBar;
    private String phNumber;
    private String intName;
    private String intEmail;
    private String intPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        verificaionCode = findViewById(R.id.verificationCode);
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

                phNumber = phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phNumber) || phNumber.length()<13){
                    Toast.makeText(smsVerificationActivity.this, "Please enter correct Phone Number", Toast.LENGTH_SHORT).show();
                }
                if(phNumber.length()>13){
                    Toast.makeText(smsVerificationActivity.this, "Maximum 10 digits", Toast.LENGTH_SHORT).show();
                }
                else{
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
                        final User user = new User(email, pass, name, phNumber, id);
                        user.setEmail(email);
                        user.setPassword(pass);
                        user.setName(name);
                        user.setPhone(phNumber);
                        users.child(id).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference(Common.user_driver_table)
                                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Common.currentUser = dataSnapshot.getValue(User.class);
                                                        SendUserToNextActivity();
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

    private void SendUserToNextActivity(){
        Intent intent = new Intent(smsVerificationActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}
