package com.example.eladoktarizo.mobel;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.eladoktarizo.mobel.adapter.AdapterMonitoring;
import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForMonitoring;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elad Oktarizo on 26/02/2018.
 */

public class Monitoring extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataForMonitoring> itemList = new ArrayList<DataForMonitoring>();
    AdapterMonitoring adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Intent i;

    private static final String TAG = Monitoring.class.getSimpleName();

    private static String url_select 	 = Server.URL + "select_monitoring.php";
//    private static String url_insert 	 = Server.URL + "insert.php";
//    private static String url_edit 	     = Server.URL + "edit.php";
//    private static String url_update 	 = Server.URL + "update.php";
//    private static String url_delete 	 = Server.URL + "delete.php";

    public static final String TAG_ID_LAPORAN       = "id_laporan";
    public static final String TAG_JUDUL_LAPORAN    = "judul_laporan";
    public static final String TAG_ISI_LAPORAN      = "isi_laporan";
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.monitoring);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_monitoring);
    setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle("Monitoring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

        // menghubungkan variablel pada layout dan pada java
        //fab     = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterMonitoring(Monitoring.this, itemList);
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

        // listview ditekan untuk pindah ke activity lain
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getid = ((TextView)view.findViewById(R.id.id_laporan)).getText().toString();
                String getjudullaporan = ((TextView)view.findViewById(R.id.judul_laporan)).getText().toString();
                String getisilaporan = ((TextView)view.findViewById(R.id.isi_laporan)).getText().toString();

                i = new Intent(getApplicationContext(), DetailMonitoring.class);

                i.putExtra("id_laporan",getid);
                Log.e("id_masuk", getid);
                i.putExtra("judul_laporan",getjudullaporan);
                Log.e("judul_masuk", getjudullaporan);
                i.putExtra("isi_laporan",getisilaporan);
                Log.e("isi_masuk", getisilaporan);
                view.getContext().startActivity(i);
            }
        });

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

                        DataForMonitoring item = new DataForMonitoring();

                        item.setId_laporan(obj.getString(TAG_ID_LAPORAN));
                        item.setJudul_laporan(obj.getString(TAG_JUDUL_LAPORAN));
                        item.setIsi_laporan(obj.getString(TAG_ISI_LAPORAN));

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

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
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
}
