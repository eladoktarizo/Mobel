package com.example.eladoktarizo.mobel;

/**
 * Created by Elad Oktarizo on 26/02/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eladoktarizo.mobel.R;

public class MenuOrtu extends AppCompatActivity {
    Button dataanakortu, monitoring, profilguru, tentang, logout;
    Intent i;
    String id_ortu;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    final String TAG = this.getClass().getName();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuortu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menuortu);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Orangtua");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        dataanakortu = (Button) findViewById(R.id.dataanakortu);
        monitoring = (Button) findViewById(R.id.monitoring);
        profilguru = (Button) findViewById(R.id.profilguru);
        tentang = (Button) findViewById(R.id.tentang);
            //logout = (Button) findViewById(R.id.btn_logout);



        id_ortu = getIntent().getStringExtra("id_ortu");

            //logout.setOnClickListener((View.OnClickListener) this) ;

            dataanakortu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(getApplicationContext(), DataAnak.class);
                    i.putExtra("id_ortu", id_ortu);
                    startActivity(i);
                }
            });
            monitoring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(getApplicationContext(), Monitoring.class);
                    startActivity(i);
                }
            });
            profilguru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = new Intent(getApplicationContext(), ProfilGuru.class);
                    startActivity(i);
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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
        Toast.makeText(MenuOrtu.this, "Tekan tombol sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d(TAG, "twice :" + twice);
            }
        }, 3000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO: method untuk item menu toolbar compose, lalu ganti nilai return -> true
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(MenuOrtu.this, Login.class);
                startActivity(intent);
                finish();
                return false;
            default:
                return false;
        }


    }

}
