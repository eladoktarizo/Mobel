package com.example.eladoktarizo.mobel;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eladoktarizo.mobel.app.AppController;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elad Oktarizo on 18/03/2018.
 */

public class TambahDataAnak extends AppCompatActivity {

    EditText td_id_anak, td_namaanak, td_noindukanak, td_tempatlahir, td_tanggallahir, td_namaibu, td_id_ortu;
    //String id_ortu;
    Button btnsimpan;
    ImageView ivCamera, ivGallery, ivUpload, ivImage;
    ProgressDialog pd;
    Intent i;
    String id_ortu, stringImage64;
    private int mYear, mMonth, mDay;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;

    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;

    String selectedPhoto;

    private static final String TAG = TambahDataAnak.class.getSimpleName();

    private static String url_insert 	    = Server.URL + "insert_dataanak.php";
    private static String url_update 	    = Server.URL + "update_dataanak.php";
    private static String url_edit          = Server.URL + "edit_dataanak.php";
    private static String url_upload        = Server.URL + "upload.php";

//    public static final String TAG_ID_ANAK      = "id_anak";
//    public static final String TAG_NOINDUK_ANAK = "noinduk_anak";
//    public static final String TAG_NAMA_LENGKAP = "nama_lengkap";
//    public static final String TAG_TTLAHIR      = "ttlahir";
//    public static final String TAG_UMUR         = "umur";
//    public static final String TAG_NAMAIBU      = "namaibu";
//    private static final String TAG_SUCCESS     = "success";
//    private static final String TAG_MESSAGE     = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data_anak);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tambahdataanak);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Data Anak");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        Intent data = getIntent();
        final int update            = data.getIntExtra("update", 0);
        String intent_id_anak       =  data.getStringExtra("id_anak");
        String intent_id_ortu       =  data.getStringExtra("id_ortu");
        String intent_noinduk_anak  =  data.getStringExtra("noinduk_anak");
        String intent_namaanak      =  data.getStringExtra("nama_lengkap");
        String intent_tempat_lahir   =  data.getStringExtra("tempat_lahir");
        String intent_tanggal_lahir  =  data.getStringExtra("tanggal_lahir");
        String intent_namaibu       =  data.getStringExtra("namaibu");

        id_ortu         = getIntent().getStringExtra("id_ortu");
        Log.e("ID ORTU", id_ortu);
        td_id_anak      = (EditText) findViewById(R.id.td_id_anak);
        td_noindukanak  = (EditText) findViewById(R.id.td_noindukanak);
        td_namaanak     = (EditText) findViewById(R.id.td_namaanak);
        td_tempatlahir  = (EditText) findViewById(R.id.td_tempatlahir);
        td_tanggallahir = (EditText) findViewById(R.id.td_tanggallahir);
        td_namaibu      = (EditText) findViewById(R.id.td_namaibu);

        //id_ortu = getIntent().getStringExtra("id_ortu");


        btnsimpan = (Button) findViewById(R.id.btnsimpan);

        ivCamera    = (ImageView) findViewById(R.id.ivCamera);
        ivGallery   = (ImageView) findViewById(R.id.ivGallery);
        ivUpload    = (ImageView) findViewById(R.id.ivUpload);
        ivImage     = (ImageView) findViewById(R.id.ivImage);

        pd          = new ProgressDialog(TambahDataAnak.this);

        td_tanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(TambahDataAnak.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                td_tanggallahir.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        if (update == 1)
        {
            btnsimpan.setText("Update Data");
            td_id_anak.setText(intent_id_anak);
            td_id_ortu.setText(intent_id_ortu);
            td_namaanak.setText(intent_namaanak);
            td_tempatlahir.setText(intent_tempat_lahir);
            td_tanggallahir.setText(intent_tanggal_lahir);
            td_namaibu.setText(intent_namaibu);

        }

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }

        });

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                if (update == 1)
                {
                    Update_data();
                    url = url_update;
                } else {
                    simpanData();
                    url = url_insert;

                    if(selectedPhoto == null || selectedPhoto.equals("")){
                        Toast.makeText(getApplicationContext(),"No Image Selected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
                        String encodedImage = ImageBase64.encode(bitmap);
                        Log.d(TAG, encodedImage);

                        HashMap<String, String > postData = new HashMap<String, String>();
                        postData.put("image", encodedImage );

                        PostResponseAsyncTask task = new PostResponseAsyncTask(TambahDataAnak.this, postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                Log.d(TAG, s);
                                if(s.contains("upload success")){
                                    Toast.makeText(getApplicationContext(),"Error while upload", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Image Uploaded Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        task.execute("http://10.0.2.2/mobel/upload.php");
                        task.execute(url_upload);
                        task.setEachExceptionsHandler(new EachExceptionsHandler() {
                            @Override
                            public void handleIOException(IOException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Cannot Connect to Server", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleMalformedURLException(MalformedURLException e) {
                                Toast.makeText(getApplicationContext(),
                                        "URL error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleProtocolException(ProtocolException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Protocol error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                Toast.makeText(getApplicationContext(),
                                        "encoding error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Something Wrong while encoding photos", Toast.LENGTH_SHORT).show();
                    }
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(getRotateBitmap(bitmap, 90));
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
                getStringImage(selectedPhoto);
            }

            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
            getStringImage(selectedPhoto);
        }
    }

    private Bitmap getRotateBitmap (Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap1 = Bitmap.createBitmap(source,0,0, source.getWidth(), source.getHeight(),matrix, true);
        return bitmap1;
    }

    private void getStringImage (String selectedPhoto){
        try{
            Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(512, 512).getBitmap();
            stringImage64 = ImageBase64.encode(bitmap);
            Log.e("SUKSES ENCODES", stringImage64);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void simpanData()
    {
        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, url_insert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(TambahDataAnak.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity( new Intent(TambahDataAnak.this,DataAnak.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(TambahDataAnak.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                //map.put("id_ortu", id_ortu);

                map.put("id_anak",td_id_anak.getText().toString());
                map.put("noinduk_anak",td_noindukanak.getText().toString());
                map.put("nama_lengkap",td_namaanak.getText().toString());
                map.put("tempat_lahir",td_tempatlahir.getText().toString());
                map.put("tanggal_lahir",td_tanggallahir.getText().toString());
                map.put("namaibu",td_namaibu.getText().toString());
                map.put("id_ortu",id_ortu);
                map.put("image", stringImage64);
                Log.e("IMAGE", stringImage64);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }

    private void Update_data()
    {
        pd.setMessage("Update Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, url_edit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(TambahDataAnak.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(TambahDataAnak.this,MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(TambahDataAnak.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id_anak",td_id_anak.getText().toString());
                map.put("noinduk_anak",td_noindukanak.getText().toString());
                map.put("nama_lenglap",td_namaanak.getText().toString());
                map.put("tempat_lahir",td_tempatlahir.getText().toString());
                map.put("tanggal_lahir",td_tanggallahir.getText().toString());
                map.put("namaibu",td_namaibu.getText().toString());
                //map.put("id_ortu",td_id_ortu.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(updateReq);
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
