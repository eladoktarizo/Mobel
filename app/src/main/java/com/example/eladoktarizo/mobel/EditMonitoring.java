package com.example.eladoktarizo.mobel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;

public class EditMonitoring extends AppCompatActivity {

    String id, judul_laporan, isi_laporan;
    EditText judullaporan, isilaporan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_monitoring);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dataanak);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Monitoring");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent b = getIntent();
        id = b.getStringExtra("id_laporan");
        Log.e("id diterima",id);
        judul_laporan = b.getStringExtra("judul_laporan");
        Log.e("judul masuk", judul_laporan);
        isi_laporan = b.getStringExtra("isi_laporan");
        Log.e("isi masuk", isi_laporan);

        judullaporan = findViewById(R.id.judullaporan);
        isilaporan = findViewById(R.id.isilaporan);
        judullaporan.setText(judul_laporan);
        isilaporan.setText(isi_laporan);
    }



}
