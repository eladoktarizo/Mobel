package com.example.eladoktarizo.mobel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

public class DetailDataAnak extends AppCompatActivity {

    String id_anak, namalengkap, ttlahir, umur, namaibu;
    EditText et_id_anak, et_nama_lengkap,et_ttlahir, et_umur, et_namaibu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_data_anak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dataanak);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Data Anak");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        Intent i = getIntent();
//        id_anak = i.getStringExtra("id_anak");
//        Log.e("id diterima",id_anak);
//        namalengkap = i.getStringExtra("namalengkap");
//        Log.e("nama masuk", namalengkap);
//        ttlahir = i.getStringExtra("ttlahir");
//        Log.e("ttlahir masuk", ttlahir);
//        umur = i.getStringExtra("umur");
//        Log.e("umur masuk", umur);
//        namaibu = i.getStringExtra("namaibu");
//        Log.e("namaibu masuk", namaibu);
//
//        et_id_anak = findViewById(R.id.judullaporan);
//        et_nama_lengkap = findViewById(R.id.isilaporan);
//        et_ttlahir = findViewById(R.id.judullaporan);
//        et_umur = findViewById(R.id.isilaporan);
//        et_namaibu = findViewById(R.id.isilaporan);
//
//        et_id_anak.setText(id_anak);
//        et_nama_lengkap.setText(namalengkap);
//        et_ttlahir.setText(ttlahir);
//        et_umur.setText(umur);
//        et_namaibu.setText(namaibu);
    }
}
