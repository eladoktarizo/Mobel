package com.example.eladoktarizo.mobel;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
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

public class DetailProfilGuru extends AppCompatActivity {

    EditText id_guru_dg, namaguru, pendidikanguru, tempatlahirguru, tanggallahirguru, alamatguru;
    CircleImageView iv_profile;
    int success;
    Button btnbatal;


    private static final String TAG = DetailProfilGuru.class.getSimpleName();

    private static String url_lihatdataguru	    = Server.URL + "lihat_dataguru.php";

    public static final String TAG_ID_GURU          = "id_guru";
    public static final String TAG_NAMALENGKAP      = "namalengkap";
    public static final String TAG_ALAMAT           = "alamat";
    public static final String TAG_PENDIDIKAN       = "pendidikan";
    public static final String TAG_TMPLAHIR_GURU    = "tmplahir_guru";
    public static final String TAG_TGLLAHIR_GURU    = "tgllahir_guru";
    public static final String TAG_IMAGE            = "file_foto_gr";
    private static final String TAG_SUCCESS         = "success";
    private static final String TAG_MESSAGE         = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profil_guru);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_profileguru);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profil Guru");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        String id_guru = i.getStringExtra("id_guru");
        Log.e("id diterima",id_guru);

        id_guru_dg = findViewById(R.id.id_guru_dg);
        namaguru = findViewById(R.id.namaguru);
        pendidikanguru = findViewById(R.id.pendidikanguru);
        tempatlahirguru = findViewById(R.id.tempatlahirguru);
        tanggallahirguru = findViewById(R.id.tanggallahirguru);
        alamatguru = findViewById(R.id.alamatguru);
        iv_profile = findViewById(R.id.iv_profile);
        btnbatal = findViewById(R.id.button_cancel);

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailProfilGuru.this, ProfilGuru.class));
                finish();
            }
        });

        lihat(id_guru);
    }
    // fungsi untuk get edit data
    private void lihat(final String id_guru) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_lihatdataguru, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);


                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get guru data", jObj.toString());
                        String idx_guru = jObj.getString(TAG_ID_GURU);
                        String namalengkap = jObj.getString(TAG_NAMALENGKAP);
                        String alamat = jObj.getString(TAG_ALAMAT);
                        String pendidikan = jObj.getString(TAG_PENDIDIKAN);
                        String tmplahir_guru = jObj.getString(TAG_TMPLAHIR_GURU);
                        String tgllahir_guru = jObj.getString(TAG_TGLLAHIR_GURU);

                        id_guru_dg.setText(idx_guru);
                        Log.d("id_guru bisa", idx_guru);
                        namaguru.setText(namalengkap);
                        Log.d("namalengkap",namalengkap);
                        pendidikanguru.setText(pendidikan);
                        tempatlahirguru.setText(tmplahir_guru);
                        tanggallahirguru.setText(tgllahir_guru);
                        alamatguru.setText(alamat);

                        //adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DetailProfilGuru.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DetailProfilGuru.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru", id_guru);

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
