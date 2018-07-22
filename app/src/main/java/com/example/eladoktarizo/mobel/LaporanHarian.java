package com.example.eladoktarizo.mobel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.eladoktarizo.mobel.adapter.AdapterAnak;
import com.example.eladoktarizo.mobel.adapter.AdapterLaporanHarian;
import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForAnak;
import com.example.eladoktarizo.mobel.data.DataForLaporanHarian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elad Oktarizo on 10/03/2018.
 */

public class LaporanHarian extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataForAnak> itemList = new ArrayList<DataForAnak>();
    AdapterAnak adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Intent i;

    EditText txt_idanak_lh, txt_idlaporan, txt_judullaporan, txt_isilaporan;// txt_noindukanak_lh, txt_namaanak_lh, txt_tmplahir_lh, txt_tgllahir_lh, txt_namaibuanak_lh;
    String id_laporan, id_anak_lh, judul_laporan, isi_laporan;


    private static final String TAG = LaporanHarian.class.getSimpleName();

    private static String url_select 	 = Server.URL + "selectdataanak.php";
    private static String url_insert     = Server.URL + "insert_broadcast.php";
    private static String url_fcm 	     = Server.URL + "fcm_insert.php";

    public static final String TAG_ID_ANAK                 = "id_anak";
    public static final String TAG_NOINDUK_ANAK_LH         = "noinduk_anak";
    public static final String TAG_NAMA_LENGKAP_LH         = "nama_lengkap";
    public static final String TAG_TMPLAHIR_LH             = "tempat_lahir";
    public static final String TAG_TGLLAHIR_LH             = "tanggal_lahir";
    public static final String TAG_NAMAIBU_LH              = "namaibu";
    public static final String TAG_IMAGE                   = "file_foto";
    private static final String TAG_SUCCESS                = "success";
    private static final String TAG_MESSAGE                = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporanharian);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_laporanharian);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Laporan Harian");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list_dataanak_lh);
        fab     = (FloatingActionButton) findViewById(R.id.fab_laporanharian);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterAnak(LaporanHarian.this, itemList);
        list.setAdapter(adapter);

        // menampilkan widget refresh
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "","", "SIMPAN");

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_fcm, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("fcm_token", token);

                        return params;
                    }
                };
                MySingleton.getmInstance(LaporanHarian.this).addToRequestque(stringRequest);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getid = ((TextView)view.findViewById(R.id.id_anak)).getText().toString();
                String getnama_lengkap = ((TextView)view.findViewById(R.id.nama_lengkap)).getText().toString();

                i = new Intent(getApplicationContext(), TulisLaporan.class);

                i.putExtra("id_anak",getid);
                Log.e("id_masuk", getid);
                i.putExtra("nama_lengkap",getnama_lengkap);
                Log.e("nama_masuk", getnama_lengkap);
                view.getContext().startActivity(i);
            }
        });
    }

    // untuk menampilkan dialog form biodata
    private void DialogForm(String idx_anak, String idx_laporan, String judulx_laporan, String isix_laporan, String button) {
        dialog = new AlertDialog.Builder(LaporanHarian.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_laporanharian, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Laporan Harian");

        //txt_idanak_lh, txt_noindukanak_lh, txt_namaanak_lh, txt_tmplahir_lh, txt_tgllahir_lh, txt_namaibuanak_lh
        txt_idanak_lh         = (EditText) dialogView.findViewById(R.id.tl_id_anak);
        txt_idlaporan      = (EditText) dialogView.findViewById(R.id.tl_id_laporan);
        txt_judullaporan    = (EditText) dialogView.findViewById(R.id.tl_judullaporan);
        txt_isilaporan  = (EditText) dialogView.findViewById(R.id.tl_isilaporan);

        if (!idx_laporan.isEmpty()){
            txt_idanak_lh.setText(idx_anak);
            txt_idlaporan.setText(idx_laporan);
            txt_judullaporan.setText(judulx_laporan);
            txt_isilaporan.setText(isix_laporan);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_laporan          = txt_idlaporan.getText().toString();
                judul_laporan       = txt_judullaporan.getText().toString();
                isi_laporan         = txt_isilaporan.getText().toString();
                id_anak_lh             = txt_idanak_lh.getText().toString();

                simpan_update();
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

    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_idlaporan.setText(null);
        txt_judullaporan.setText(null);
        txt_isilaporan.setText(null);
        txt_idanak_lh.setText(null);
    }

    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_laporan.isEmpty()){
            url = url_insert;
        } else {
            url = url_insert;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        callVolley();
                        kosong();

                        Toast.makeText(LaporanHarian.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(LaporanHarian.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(LaporanHarian.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_laporan.isEmpty()){
                    params.put("judul_laporan", judul_laporan);
                    params.put("isi_laporan", isi_laporan);
                    params.put("id_anak", id_anak_lh);
                } else {
                    params.put("id_laporan", id_laporan);
                    params.put("id_anak", id_anak_lh);
                    params.put("judul_laporan", judul_laporan);
                    params.put("isi_laporan", isi_laporan);
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataForAnak item = new DataForAnak();

                        item.setId_anak(obj.getString(TAG_ID_ANAK));
                        item.setNoinduk_anak(obj.getString(TAG_NOINDUK_ANAK_LH));
                        item.setNama_lengkap(obj.getString(TAG_NAMA_LENGKAP_LH));
                        item.setIv_anak(obj.getString(TAG_IMAGE));

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

//    Toolbar toolbar;
//    FloatingActionButton fab;
//    ListView list;
//    SwipeRefreshLayout swipe;
//    List<DataForLaporanHarian> itemList = new ArrayList<DataForLaporanHarian>();
//    AdapterLaporanHarian adapter;
//    int success;
//    AlertDialog.Builder dialog;
//    LayoutInflater inflater;
//    View dialogView;
//    Intent i;
//    EditText txt_idanak, txt_idlaporan, txt_judullaporan, txt_isilaporan;
//    String id_anak, id_laporan, judul_laporan, isi_laporan;
//
//    private static final String TAG = Monitoring.class.getSimpleName();
//
//    private static String url_select 	 = Server.URL + "select_monitoring.php";
//    private static String url_insert 	 = Server.URL + "insert_laporanharian.php";
//    private static String url_fcm 	     = Server.URL + "fcm_insert.php";
//    private static String url_update 	 = Server.URL + "insert_laporanharian.php";
////    private static String url_delete 	 = Server.URL + "delete.php";
//
//    public static final String TAG_ID_LAPORAN       = "id_laporan";
//    public static final String TAG_ID_ANAK         = "id_anak";
//    public static final String TAG_JUDUL_LAPORAN    = "judul_laporan";
//    public static final String TAG_ISI_LAPORAN      = "isi_laporan";
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_MESSAGE = "message";
//
//    String tag_json_obj = "json_obj_req";
//
//    @Override
//    public void onCreate (Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.laporanharian);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_laporanharian);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("Laporan Harian");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//    // menghubungkan variablel pada layout dan pada java
//    swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//    list    = (ListView) findViewById(R.id.list_dataanak_guruside);
//    fab     = (FloatingActionButton) findViewById(R.id.fab_laporanharian);
//
//    // untuk mengisi data dari JSON ke dalam adapter
//    adapter = new AdapterLaporanHarian(LaporanHarian.this, itemList);
//    list.setAdapter(adapter);
//
//    // menampilkan widget refresh
//        swipe.setOnRefreshListener(this);
//
//        swipe.post(new Runnable() {
//        @Override
//        public void run() {
//            swipe.setRefreshing(true);
//            itemList.clear();
//            adapter.notifyDataSetChanged();
//            callVolley();
//        }
//    }
//        );
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogForm("", "", "","", "SIMPAN");
//
////                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
////                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
////                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_fcm, new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////
////                    }
////                }, new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////
////                    }
////                })
////                {
////                    @Override
////                    protected Map<String, String> getParams() throws AuthFailureError {
////                        Map<String, String> params = new HashMap<String, String>();
////                        params.put("fcm_token", token);
////
////                        return params;
////                    }
////                };
////                MySingleton.getmInstance(LaporanHarian.this).addToRequestque(stringRequest);
//            }
//        });
//
//        // listview ditekan untuk pindah ke activity lain
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String getid = ((TextView)view.findViewById(R.id.id_laporan)).getText().toString();
//                String getjudullaporan = ((TextView)view.findViewById(R.id.judul_laporan)).getText().toString();
//                String getisilaporan = ((TextView)view.findViewById(R.id.isi_laporan)).getText().toString();
//
//                i = new Intent(getApplicationContext(), DetailMonitoring.class);
//
//                i.putExtra("id_laporan",getid);
//                Log.e("id_masuk", getid);
//                i.putExtra("judul_laporan",getjudullaporan);
//                Log.e("judul_masuk", getjudullaporan);
//                i.putExtra("isi_laporan",getisilaporan);
//                Log.e("isi_masuk", getisilaporan);
//                view.getContext().startActivity(i);
//            }
//        });
//    }
//
//    // untuk menampilkan dialog form biodata
//    private void DialogForm(String idx_anak, String idx_laporan, String judulx_laporan, String isix_laporan, String button) {
//        dialog = new AlertDialog.Builder(LaporanHarian.this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.form_laporanharian, null);
//        dialog.setView(dialogView);
//        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
//        dialog.setTitle("Laporan Harian");
//
//        txt_idanak         = (EditText) dialogView.findViewById(R.id.tl_id_anak);
//        txt_idlaporan      = (EditText) dialogView.findViewById(R.id.tl_id_laporan);
//        txt_judullaporan    = (EditText) dialogView.findViewById(R.id.tl_judullaporan);
//        txt_isilaporan  = (EditText) dialogView.findViewById(R.id.tl_isilaporan);
//
//        if (!idx_laporan.isEmpty()){
//            txt_idanak.setText(idx_anak);
//            txt_idlaporan.setText(idx_laporan);
//            txt_judullaporan.setText(judulx_laporan);
//            txt_isilaporan.setText(isix_laporan);
//        } else {
//            kosong();
//        }
//
//        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                id_laporan      = txt_idlaporan.getText().toString();
//                judul_laporan    = txt_judullaporan.getText().toString();
//                isi_laporan  = txt_isilaporan.getText().toString();
//                id_anak = txt_idanak.getText().toString();
//
//                simpan_update();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                kosong();
//            }
//        });
//
//        dialog.show();
//    }
//
//    // untuk mengosongi edittext pada form
//    private void kosong(){
//        txt_idlaporan.setText(null);
//        txt_judullaporan.setText(null);
//        txt_isilaporan.setText(null);
//        txt_idanak.setText(null);
//    }
//
//    // untuk menampilkan semua data pada listview
//    private void callVolley(){
//        itemList.clear();
//        adapter.notifyDataSetChanged();
//        swipe.setRefreshing(true);
//
//        // membuat request JSON
//        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d(TAG, response.toString());
//
//                // Parsing json
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject obj = response.getJSONObject(i);
//
//                        DataForLaporanHarian item = new DataForLaporanHarian();
//
//                        item.setId_laporan(obj.getString(TAG_ID_LAPORAN));
//                        item.setJudul_laporan(obj.getString(TAG_JUDUL_LAPORAN));
//                        item.setIsi_laporan(obj.getString(TAG_ISI_LAPORAN));
//
//                        // menambah item ke array
//                        itemList.add(item);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // notifikasi adanya perubahan data pada adapter
//                adapter.notifyDataSetChanged();
//
//                swipe.setRefreshing(false);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                swipe.setRefreshing(false);
//            }
//        });
//
//        // menambah request ke request queue
//        AppController.getInstance().addToRequestQueue(jArr);
//    }
//
//    @Override
//    public void onRefresh() {
//        itemList.clear();
//        adapter.notifyDataSetChanged();
//        callVolley();
//    }
//
//    private void simpan_update() {
//        String url;
//        // jika id kosong maka simpan, jika id ada nilainya maka update
//        if (id_laporan.isEmpty()){
//            url = url_insert;
//        } else {
//            url = url_update;
//        }
//
//        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Response: " + response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    success = jObj.getInt(TAG_SUCCESS);
//
//                    // Cek error node pada json
//                    if (success == 1) {
//                        Log.d("Add/update", jObj.toString());
//
//                        callVolley();
//                        kosong();
//
//                        Toast.makeText(LaporanHarian.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                        adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(LaporanHarian.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(LaporanHarian.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters ke post url
//                Map<String, String> params = new HashMap<String, String>();
//                // jika id kosong maka simpan, jika id ada nilainya maka update
//                if (id_laporan.isEmpty()){
//                    params.put("judul_laporan", judul_laporan);
//                    params.put("isi_laporan", isi_laporan);
//                    params.put("id_anak", id_anak);
//                } else {
//                    params.put("id_laporan", id_laporan);
//                    params.put("id_anak", id_anak);
//                    params.put("judul_laporan", judul_laporan);
//                    params.put("isi_laporan", isi_laporan);
//                }
//
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return false;
//        }
    }
