package com.durinsday.blitzdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imgLogo;
    TextView appName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        imgLogo=findViewById(R.id.logo);
        appName=findViewById(R.id.appName);

        Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        imgLogo.startAnimation(myAnimation);
        appName.startAnimation(myAnimation);

        final Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
