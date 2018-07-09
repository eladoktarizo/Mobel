package com.example.eladoktarizo.mobel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Elad Oktarizo on 27/03/2018.
 */

public class MenuGuru extends AppCompatActivity {

    Button dataanak, laporanharian, monitoring, profilguru, tentang, logout;

    Intent i;

    SharedPreferences sharedpreferences;


    public static final String TAG_ID = "id_guru";
    public static final String TAG_USERNAME = "username";
    final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuguru);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menuguru);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Guru");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);


        dataanak = (Button) findViewById(R.id.dataanakguru);
        laporanharian = (Button) findViewById(R.id.laporanharianguru);
        tentang = (Button) findViewById(R.id.tentangguruside);
        logout = (Button) findViewById(R.id.logout);

        dataanak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), DataAnakGuruSide.class);
                startActivity(i);
            }
        });

        laporanharian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), LaporanHarian.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(MenuGuru.this, LoginGuru.class);
                finish();
                startActivity(intent);
            }
        });
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), TentangAplikasi.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
    boolean twice;
    @Override
    public void onBackPressed() {
        Log.d(TAG, "klik");
        if (twice == true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice = true;
        Log.d(TAG, "twice :" + twice);
        //super.onBackPressed();
        Toast.makeText(MenuGuru.this, "Tekan tombol sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d(TAG, "twice :" + twice);
            }
        }, 3000);
    }
}
