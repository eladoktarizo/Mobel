package com.example.eladoktarizo.mobel;

/**
 * Created by Elad Oktarizo on 10/03/2018.
 */

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        if (Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final int welcomeScreenDisplay = 2000; // 3000 = 3 detik
        Thread welcomeThread = new Thread() {

            int wait = 0;

            @Override
            public void run(){
                try {
                    super.run();
                    while (wait < welcomeScreenDisplay){
                        sleep (100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    Intent intent = new Intent(SplashScreen.this, PilihanLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        welcomeThread.start();
    }
}