package com.example.eladoktarizo.mobel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class DetailMonitoring extends AppCompatActivity {

    String id, judul_laporan, isi_laporan;
    EditText judullaporan, isilaporan;
    //Button Delete, Edit;
    //Intent b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_monitoring);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_monitoring);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Monitoring");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        id = i.getStringExtra("id_laporan");
        Log.e("id diterima",id);
        judul_laporan = i.getStringExtra("judul_laporan");
        Log.e("judul masuk", judul_laporan);
        isi_laporan = i.getStringExtra("isi_laporan");
        Log.e("isi masuk", isi_laporan);

        judullaporan = findViewById(R.id.judullaporan);
        isilaporan = findViewById(R.id.isilaporan);
        judullaporan.setText(judul_laporan);
        isilaporan.setText(isi_laporan);

//        Delete = findViewById(R.id.btn_delete);
//        Edit = findViewById(R.id.btn_edit);
//
//        Edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String getid = ((TextView)v.findViewById(R.id.id_laporan)).getText().toString();
//                String getjudullaporan = ((TextView)v.findViewById(R.id.judul_laporan)).getText().toString();
//                String getisilaporan = ((TextView)v.findViewById(R.id.isi_laporan)).getText().toString();
//
//                b = new Intent(getApplicationContext(), EditMonitoring.class);
//
//                b.putExtra("id_laporan",getid);
//                Log.e("id_masuk", getid);
//                b.putExtra("judul_laporan",getjudullaporan);
//                Log.e("judul_masuk", getjudullaporan);
//                b.putExtra("isi_laporan",getisilaporan);
//                Log.e("isi_masuk", getisilaporan);
//                v.getContext().startActivity(b);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
