package com.durinsday.blitzdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.durinsday.blitzdriver.Common.Common;
import com.durinsday.blitzdriver.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private Button signIn;
    private EditText driverEmail2, driverPassword2;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        signIn = findViewById(R.id.btnSignIn2);
        driverEmail2 = findViewById(R.id.driverEmail2);
        driverPassword2 = findViewById(R.id.driverPassword2);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = driverEmail2.getText().toString();
                String pass = driverPassword2.getText().toString();

                DriverSignIn(email, pass);
            }
        });
    }

    private void DriverSignIn(final String email, String pass) {
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
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Common.currentUser = dataSnapshot.getValue(User.class);
                                    startActivity(new Intent(SignInActivity.this,DriverHomeActivity.class));
                                    finish();
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
