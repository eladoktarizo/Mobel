package com.example.eladoktarizo.mobel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.eladoktarizo.mobel.adapter.AdapterAnak;
import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForAnak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnakGuruSide extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataForAnak> itemList = new ArrayList<DataForAnak>();
    AdapterAnak adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_idanak_gs, txt_noindukanak_gs, txt_namaanak_gs, txt_ttlanak_gs, txt_umuranak_gs, txt_namaibuanak_gs;
    String id_anak_gs, noinduk_anak_gs, nama_lengkap_gs,  ttlahir_gs, umur_gs, namaibu_gs;

    private static final String TAG = DataAnakGuruSide.class.getSimpleName();

    private static String url_select 	 = Server.URL + "selectdataanak.php";
    private static String url_edit 	     = Server.URL + "edit_dataanak.php";

    public static final String TAG_ID_ANAK                 = "id_anak";
    public static final String TAG_NOINDUK_ANAK_GS         = "noinduk_anak";
    public static final String TAG_NAMA_LENGKAP_GS         = "nama_lengkap";
    public static final String TAG_TTLAHIR_GS              = "ttlahir";
    public static final String TAG_UMUR_GS                 = "umur";
    public static final String TAG_NAMAIBU_GS              = "namaibu";
    public static final String TAG_IMAGE                   = "file_foto";
    private static final String TAG_SUCCESS                = "success";
    private static final String TAG_MESSAGE                = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_anak_guru_side);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dataanak);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Data Anak");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list_dataanak_guruside);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterAnak(DataAnakGuruSide.this, itemList);
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

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx_anakgs = itemList.get(position).getId_anak();

                final CharSequence[] dialogitem = {"Lihat Detail"};
                dialog = new AlertDialog.Builder(DataAnakGuruSide.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                lihat(idx_anakgs);
                                break;
                        }
                    }
                }).show();
                return false;
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
        txt_idanak_gs.setText(null);
        txt_noindukanak_gs.setText(null);
        txt_namaanak_gs.setText(null);
        txt_ttlanak_gs.setText(null);
        txt_umuranak_gs.setText(null);
        txt_namaibuanak_gs.setText(null);
    }

    // untuk menampilkan dialog form biodata
    private void DialogForm(String idx_anak_gs, String noindukx_anak_gs, String namax_lengkap_gs, String ttlahirx_gs, String umurx_gs, String namaibux_gs, String button) {
        dialog = new AlertDialog.Builder(DataAnakGuruSide.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_dataanak, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        //dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Data Anak");

        txt_idanak_gs         = (EditText) dialogView.findViewById(R.id.txt_id_anak);
        txt_noindukanak_gs    = (EditText) dialogView.findViewById(R.id.txt_noindukanak);
        txt_namaanak_gs       = (EditText) dialogView.findViewById(R.id.txt_namaanak);
        txt_ttlanak_gs        = (EditText) dialogView.findViewById(R.id.txt_ttlanak);
        txt_umuranak_gs       = (EditText) dialogView.findViewById(R.id.txt_umuranak);
        txt_namaibuanak_gs    = (EditText) dialogView.findViewById(R.id.txt_namaibuanak);

        if (!idx_anak_gs.isEmpty()){
            txt_idanak_gs.setText(idx_anak_gs);
            txt_noindukanak_gs.setText(noindukx_anak_gs);
            txt_namaanak_gs.setText(namax_lengkap_gs);
            txt_ttlanak_gs.setText(ttlahirx_gs);
            txt_umuranak_gs.setText(umurx_gs);
            txt_namaibuanak_gs.setText(namaibux_gs);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_anak_gs      = txt_idanak_gs.getText().toString();
                noinduk_anak_gs = txt_noindukanak_gs.getText().toString();
                nama_lengkap_gs = txt_namaanak_gs.getText().toString();
                ttlahir_gs      = txt_ttlanak_gs.getText().toString();
                umur_gs         = txt_umuranak_gs.getText().toString();
                namaibu_gs      = txt_namaibuanak_gs.getText().toString();

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
                        item.setNoinduk_anak(obj.getString(TAG_NOINDUK_ANAK_GS));
                        item.setNama_lengkap(obj.getString(TAG_NAMA_LENGKAP_GS));
                        item.setUmur(obj.getString(TAG_UMUR_GS));
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

    // fungsi untuk get edit data
    private void lihat(final String idx_anak_gs){
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
                        String idx_anak_gs         = jObj.getString(TAG_ID_ANAK);
                        String noindukx_anak_gs    = jObj.getString(TAG_NOINDUK_ANAK_GS);
                        String namax_lengkap_gs    = jObj.getString(TAG_NAMA_LENGKAP_GS);
                        String ttlahirx_gs         = jObj.getString(TAG_TTLAHIR_GS);
                        String umurx_gs            = jObj.getString(TAG_UMUR_GS);
                        String namaibux_gs         = jObj.getString(TAG_NAMAIBU_GS);

                        DialogForm(idx_anak_gs, noindukx_anak_gs, namax_lengkap_gs, ttlahirx_gs, umurx_gs, namaibux_gs, "");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(DataAnakGuruSide.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DataAnakGuruSide.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak", idx_anak_gs);

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
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
