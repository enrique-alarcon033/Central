package com.yonusa.central;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.yonusa.central.loguin.login;

public class Splash extends AppCompatActivity {

    private  static int SPLASH_TIME_OUT = 2500;
ImageView logo,logo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.imageView);

        // Animación: Trasladar Elemento de Derecha a Izquierda
        RotateAnimation an = new RotateAnimation(0,  359, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        an.setDuration(2000);
        logo.startAnimation(an);


        //myRegistrationToken();

        new Handler().postDelayed(new Runnable() {

            @Override

            public void run() {
                Intent homeIntent = new Intent(Splash.this, login.class);
                startActivity(homeIntent);
                finish();
            }

        }, SPLASH_TIME_OUT);

    }
}