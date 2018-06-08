package com.example.eladoktarizo.mobel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

public class DataAnak extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataForAnak> itemList = new ArrayList<>();
    AdapterAnak adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    Intent intent;
    View dialogView;
    EditText txt_id_anak, txt_noindukanak, txt_namaanak, txt_ttlanak, txt_umuranak, txt_namaibuanak, txt_id_ortu;
    String id_anak, noinduk_anak, nama_lengkap, ttlahir, umur, namaibu, id_ortu;

    private static final String TAG = DataAnak.class.getSimpleName();

    private static String url_select = Server.URL + "select_dataanak.php";
    private static String url_insert = Server.URL + "insert_dataanak.php";
    private static String url_edit = Server.URL + "edit_dataanak.php";
    private static String url_update = Server.URL + "update_dataanak.php";
    private static String url_delete = Server.URL + "delete_dataanak.php";

    public static final String TAG_ID_ORTU = "id_ortu";
    public static final String TAG_ID_ANAK = "id_anak";
    public static final String TAG_NOINDUK_ANAK = "noinduk_anak";
    public static final String TAG_NAMA_LENGKAP = "nama_lengkap";
    public static final String TAG_TTLAHIR = "ttlahir";
    public static final String TAG_UMUR = "umur";
    public static final String TAG_IMAGE = "file_foto";
    public static final String TAG_NAMAIBU = "namaibu";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataanak);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dataanak);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Data Anak");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // menghubungkan variablel pada layout dan pada java
        fab = (FloatingActionButton) findViewById(R.id.fab_add_dataanak);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_dataanak);

        id_ortu = getIntent().getStringExtra("id_ortu");

        adapter = new AdapterAnak(DataAnak.this, itemList);
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

        // fungsi floating action button memanggil form biodata
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataAnak.this, TambahDataAnak.class);
                intent.putExtra("id_ortu", id_ortu);
                startActivity(intent);
            }
//                DialogForm("", "", "", "", "", "", "SIMPAN");
//            }
        });

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final String idx_anak = itemList.get(position).getId_anak();
//
//                Intent intent = new Intent(this, ActivityBaru.class);
//                intent.putExtra("id_anak", idx_anak);
//                startActivity(intent);
//
//            }
//        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx_anak = itemList.get(position).getId_anak();

                final CharSequence[] dialogitem = {"Ubah Data", "Hapus"};
                dialog = new AlertDialog.Builder(DataAnak.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                ubah(idx_anak);
                                break;
                            case 1:
                                hapus(idx_anak);
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
    private void kosong() {
        txt_id_anak.setText(null);
        txt_noindukanak.setText(null);
        txt_namaanak.setText(null);
        txt_ttlanak.setText(null);
        txt_umuranak.setText(null);
        txt_namaibuanak.setText(null);
        txt_id_ortu.setText(null);
    }

    // untuk menampilkan dialog form dataanak
    private void DialogForm(String idx_ortu, String idx_anak, String noindukx_anak, String namax_lengkap, String ttlahirx, String umurx, String namaibux, String button) {
        dialog = new AlertDialog.Builder(DataAnak.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_dataanak, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        //dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Data Anak");

        txt_id_ortu = (EditText) dialogView.findViewById(R.id.txt_id_ortu);
        txt_id_anak = (EditText) dialogView.findViewById(R.id.txt_id_anak);
        txt_noindukanak = (EditText) dialogView.findViewById(R.id.txt_noindukanak);
        txt_namaanak = (EditText) dialogView.findViewById(R.id.txt_namaanak);
        txt_ttlanak = (EditText) dialogView.findViewById(R.id.txt_ttlanak);
        txt_umuranak = (EditText) dialogView.findViewById(R.id.txt_umuranak);
        txt_namaibuanak = (EditText) dialogView.findViewById(R.id.txt_namaibuanak);

        if (!idx_anak.isEmpty()) {
            txt_noindukanak.setText(noindukx_anak);
            txt_namaanak.setText(namax_lengkap);
            txt_ttlanak.setText(ttlahirx);
            txt_umuranak.setText(umurx);
            txt_namaibuanak.setText(namaibux);
            txt_id_ortu.setText(idx_ortu);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_anak = txt_id_anak.getText().toString();
                noinduk_anak = txt_noindukanak.getText().toString();
                nama_lengkap = txt_namaanak.getText().toString();
                ttlahir = txt_ttlanak.getText().toString();
                umur = txt_umuranak.getText().toString();
                namaibu = txt_namaibuanak.getText().toString();
                id_ortu = txt_id_ortu.getText().toString();

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

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        StringRequest jArr = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    // Parsing json
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        DataForAnak item = new DataForAnak();

                        item.setId_anak(obj.getString(TAG_ID_ANAK));
                        item.setNoinduk_anak(obj.getString(TAG_NOINDUK_ANAK));
                        item.setNama_lengkap(obj.getString(TAG_NAMA_LENGKAP));
                        item.setUmur(obj.getString(TAG_UMUR));
                        item.setIv_anak(obj.getString(TAG_IMAGE));

                        // menambah item ke array
                        itemList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_ortu", id_ortu);
                return params;
            }
        };

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_anak.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
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

                        Toast.makeText(DataAnak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(DataAnak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DataAnak.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_anak.isEmpty()) {
                    params.put("id_ortu", id_ortu);
                    //params.put("id_anak", id_anak);
                    params.put("noinduk_anak", noinduk_anak);
                    params.put("nama_lengkap", nama_lengkap);
                    params.put("ttlahir", ttlahir);
                    params.put("umur", umur);
                    params.put("namaibu", namaibu);
                } else {
                    params.put("id_ortu", id_ortu);
                    params.put("id_anak", id_anak);
                    params.put("noinduk_anak", noinduk_anak);
                    params.put("nama_lengkap", nama_lengkap);
                    params.put("ttlahir", ttlahir);
                    params.put("umur", umur);
                    params.put("namaibu", namaibu);
                }
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get edit data
    private void ubah(final String idx_anak) {
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
                        String idx_ortu = jObj.getString(TAG_ID_ANAK);
                        String idx_anak = jObj.getString(TAG_ID_ANAK);
                        String noindukx_anak = jObj.getString(TAG_NOINDUK_ANAK);
                        String namax_lengkap = jObj.getString(TAG_NAMA_LENGKAP);
                        String ttlahirx = jObj.getString(TAG_TTLAHIR);
                        String umurx = jObj.getString(TAG_UMUR);
                        String namaibux = jObj.getString(TAG_NAMAIBU);

                        DialogForm(idx_ortu, idx_anak, noindukx_anak, namax_lengkap, ttlahirx, umurx, namaibux, "UPDATE");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(DataAnak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DataAnak.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak", idx_anak);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void hapus(final String idx_anak) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(DataAnak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(DataAnak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DataAnak.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak", idx_anak);

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
