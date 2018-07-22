package com.example.eladoktarizo.mobel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.eladoktarizo.mobel.adapter.AdapterGuru;
import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForGuru;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilGuru extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    //FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataForGuru> itemList = new ArrayList<DataForGuru>();
    AdapterGuru adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    Intent i;
    EditText txt_id_guru, txt_namalengkap, txt_alamat, txt_pendidikan, txt_tmplahir_guru, txt_tgllahir_guru;
    String id_guru, namalengkap, alamat, pendidikan, tmplahir_guru, tgllahir_guru;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_selectguru 	    = Server.URL + "select_dataguru.php";
    private static String url_edit 	            = Server.URL + "lihat_dataguru.php";

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
        setContentView(R.layout.profil_guru);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profilguru);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profil Guru");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // menghubungkan variablel pada layout dan pada java
        //fab     = (FloatingActionButton) findViewById(R.id.fab_profilguru);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listguru);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterGuru(ProfilGuru.this, itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getidguru = ((TextView)view.findViewById(R.id.id_guru)).getText().toString();

                i = new Intent(getApplicationContext(), DetailProfilGuru.class);

                //id_guru, namalengkap, alamat, pendidikan, tmplahir_guru, tgllahir_guru
                i.putExtra("id_guru",getidguru);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();

    }

    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_id_guru.setText(null);
        txt_namalengkap.setText(null);
        txt_alamat.setText(null);
        txt_pendidikan.setText(null);
        txt_tmplahir_guru.setText(null);
        txt_tgllahir_guru.setText(null);
    }

    // untuk menampilkan dialog form biodata
    private void DialogForm(String idx_guru, String namalengkapx, String alamatx, String pendidikanx, String tmplahirx_guru, String tgllahirx_guru, String button) {
        dialog = new AlertDialog.Builder(ProfilGuru.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_bioguru, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Biodata Guru");

        txt_id_guru      = (EditText) dialogView.findViewById(R.id.txt_idguru);
        txt_namalengkap    = (EditText) dialogView.findViewById(R.id.txt_nama_lengkap);
        txt_alamat  = (EditText) dialogView.findViewById(R.id.txt_alamat);
        txt_pendidikan    = (EditText) dialogView.findViewById(R.id.txt_pendidikan);
        txt_tmplahir_guru    = (EditText) dialogView.findViewById(R.id.txt_tmplahir_guru);
        txt_tgllahir_guru    = (EditText) dialogView.findViewById(R.id.txt_tgllahir_guru);

        if (!idx_guru.isEmpty()){
            txt_id_guru.setText(idx_guru);
            txt_namalengkap.setText(namalengkapx);
            txt_alamat.setText(alamatx);
            txt_pendidikan.setText(pendidikanx);
            txt_tmplahir_guru.setText(tmplahirx_guru);
            txt_tgllahir_guru.setText(tgllahirx_guru);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_guru      = txt_id_guru.getText().toString();
                namalengkap    = txt_namalengkap.getText().toString();
                alamat      = txt_alamat.getText().toString();
                pendidikan    = txt_pendidikan.getText().toString();
                tmplahir_guru    = txt_tmplahir_guru.getText().toString();
                tgllahir_guru    = txt_tgllahir_guru.getText().toString();

                //simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_selectguru, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataForGuru item = new DataForGuru();

                        item.setId_guru(obj.getString(TAG_ID_GURU));
                        item.setNamalengkap(obj.getString(TAG_NAMALENGKAP));
                        item.setAlamat(obj.getString(TAG_ALAMAT));
                        item.setIv_guru(obj.getString(TAG_IMAGE));
                        //item.setNohp(obj.getString(TAG_NOHP));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk get edit data
    private void ubah(final String idx_guru){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx_guru             = jObj.getString(TAG_ID_GURU);
                        String namalengkapx         = jObj.getString(TAG_NAMALENGKAP);
                        String alamatx              = jObj.getString(TAG_ALAMAT);
                        String pendidikanx          = jObj.getString(TAG_PENDIDIKAN);
                        String tmplahirx_guru            = jObj.getString(TAG_TMPLAHIR_GURU);
                        String tgllahirx_guru            = jObj.getString(TAG_TGLLAHIR_GURU);

                        DialogForm(idx_guru, namalengkapx, alamatx, pendidikanx, tmplahirx_guru, tgllahirx_guru, "");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(ProfilGuru.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(ProfilGuru.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru", idx_guru);

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
