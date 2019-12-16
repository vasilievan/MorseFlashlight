package com.alekseyvasilev.morseflashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable (){
            @Override
            public void run(){
                Intent toMain = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(toMain);
                SplashScreen.this.finish();
            }
        }, 1500);
    }
}
