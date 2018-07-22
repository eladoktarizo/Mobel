package com.example.eladoktarizo.mobel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eladoktarizo.mobel.adapter.AdapterAnak;
import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForAnak;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TulisLaporan extends AppCompatActivity {

    Toolbar toolbar;
    Intent i;
    List<DataForAnak> itemList = new ArrayList<DataForAnak>();
    AdapterAnak adapter;
    ProgressDialog pd;

    String id_anak, nama_lengkap, judul_laporan, isi_laporan;
    EditText id_anak_laporan, nama_anak_laporan, id_laporan_tl, judul_laporan_tl, isi_laporan_tl;
    Button btn_simpan, btn_batal;


    private static final String TAG = Monitoring.class.getSimpleName();

    private static String url_insert 	 = Server.URL + "insert_laporanharian.php";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tulis_laporan);

        Toolbar toolbar = findViewById(R.id.toolbar_tulis_laporan);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tulis Laporan Harian");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        id_anak = i.getStringExtra("id_anak");
        Log.e("id diterima",id_anak);
        nama_lengkap = i.getStringExtra("nama_lengkap");
        Log.e("nama masuk", nama_lengkap);

        id_anak_laporan = findViewById(R.id.txt_idanak_laporan);
        nama_anak_laporan = findViewById(R.id.nama_anak_laporan);
        id_anak_laporan.setText(id_anak);
        nama_anak_laporan.setText(nama_lengkap);

        id_laporan_tl       = (EditText) findViewById(R.id.txt_id_laporan);
        judul_laporan_tl    = (EditText) findViewById(R.id.judullaporan);
        isi_laporan_tl      = (EditText) findViewById(R.id.isilaporan);

        btn_simpan          = (Button) findViewById(R.id.btn_simpan_tl);
        btn_batal           = (Button) findViewById(R.id.btn_batal_tl);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                simpan();
                url = url_insert;
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TulisLaporan.this, LaporanHarian.class));
                finish();
            }
        });
    }

    private void simpan()
    {
//        pd.setMessage("Menyimpan Data");
//        pd.setCancelable(false);
//        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, url_insert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(TulisLaporan.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity( new Intent(TulisLaporan.this,DataAnak.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pd.cancel();
                        Toast.makeText(TulisLaporan.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                //id_anak_laporan, nama_anak_laporan, id_laporan_tl, judul_laporan_tl, isi_laporan_tl
                map.put("id_anak",id_anak_laporan.getText().toString());
                map.put("id_laporan",id_anak_laporan.getText().toString());
                map.put("judul_laporan",judul_laporan_tl.getText().toString());
                map.put("isi_laporan",isi_laporan_tl.getText().toString());
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
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
