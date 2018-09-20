package com.mobitechs.vbags;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mobitechs.vbags.sessionManager.SessionManager;

public class Splash_Screen extends AppCompatActivity implements View.OnClickListener {

    SessionManager sessionManager;
    private static int SPLASH_TIME_OUT = 4000;
    private ImageView mImage;
    private AnimationDrawable mAnimationDrawable;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mImage = (ImageView) findViewById(R.id.spin);
        mImage.setBackground(ContextCompat.getDrawable(this, R.drawable.drawable_animator_list));
        mAnimationDrawable = (AnimationDrawable) mImage.getBackground();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            mAnimationDrawable.start();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
//                SessionManager sessionManager = new SessionManager(Splash_Screen.this);
//                sessionManager.checkLogin();
//                Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                    SessionManager sessionManager = new SessionManager(Splash_Screen.this);
                    sessionManager.checkLogin();

                }
            }, SPLASH_TIME_OUT);
        }
        else{
            mAnimationDrawable.stop();

        }



    }

    @Override
    public void onClick(View v) {

    }
}