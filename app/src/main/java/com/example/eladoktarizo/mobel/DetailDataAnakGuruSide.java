package com.example.eladoktarizo.mobel;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eladoktarizo.mobel.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailDataAnakGuruSide extends AppCompatActivity {

    EditText id_anak_sideguru, namaanak_sideguru, noindukanak_sideguru, tempatlahir_sideguru, tanggallahir_sideguru, namaibu_sideguru;
    CircleImageView iv_profile;
    int success;
    Button btnkembali;

    private static final String TAG = DetailDataAnakGuruSide.class.getSimpleName();

    private static String url_detaildata	    = Server.URL + "detail_dataanak.php";

    public static final String TAG_ID_ANAK          = "id_anak";
    public static final String TAG_NAMALENGKAP      = "nama_lengkap";
    public static final String TAG_NOINDUK          = "noinduk_anak";
    public static final String TAG_TMPLAHIR         = "tempat_lahir";
    public static final String TAG_TGLLAHIR         = "tanggal_lahir";
    public static final String TAG_NAMAIBU          = "namaibu";
    public static final String TAG_IMAGE            = "file_foto";
    private static final String TAG_SUCCESS         = "success";
    private static final String TAG_MESSAGE         = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_anak_guru_side);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_guruside);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Data Anak");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        String id_anak = i.getStringExtra("id_anak");
        Log.e("ID ANAK TERKIRIM",id_anak);

        id_anak_sideguru = findViewById(R.id.id_anak_sideguru);
        namaanak_sideguru = findViewById(R.id.namaanak_sideguru);
        noindukanak_sideguru = findViewById(R.id.noindukanak_sideguru);
        tempatlahir_sideguru = findViewById(R.id.tempatlahir_sideguru);
        tanggallahir_sideguru = findViewById(R.id.tanggallahir_sideguru);
        namaibu_sideguru = findViewById(R.id.namaibu_sideguru);
        iv_profile = findViewById(R.id.iv_profile);
        btnkembali = findViewById(R.id.button_cancel_sideguru);

        btnkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailDataAnakGuruSide.this, DataAnakGuruSide.class));
                finish();
            }
        });

        lihat(id_anak);
    }

    // fungsi untuk get edit data
    private void lihat(final String id_anak) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detaildata, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);


                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get guru data", jObj.toString());
                        String idx_anak = jObj.getString(TAG_ID_ANAK);
                        String namalengkap = jObj.getString(TAG_NAMALENGKAP);
                        String noinduk = jObj.getString(TAG_NOINDUK);
                        String tempatlahir = jObj.getString(TAG_TMPLAHIR);
                        String tanggallahir = jObj.getString(TAG_TGLLAHIR);
                        String namaibu = jObj.getString(TAG_NAMAIBU);

                        id_anak_sideguru.setText(idx_anak);
                        Log.d("id_guru bisa", idx_anak);
                        namaanak_sideguru.setText(namalengkap);
                        Log.d("namalengkap",namalengkap);
                        noindukanak_sideguru.setText(noinduk);
                        tempatlahir_sideguru.setText(tempatlahir);
                        tanggallahir_sideguru.setText(tanggallahir);
                        namaibu_sideguru.setText(namaibu);

                        //adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DetailDataAnakGuruSide.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(DetailDataAnakGuruSide.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak", id_anak);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
