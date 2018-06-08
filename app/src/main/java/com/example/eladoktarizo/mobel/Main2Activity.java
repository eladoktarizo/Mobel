package com.example.eladoktarizo.mobel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    Button button_logout, menuguru;
    TextView text_idguru, text_username;
    String id_guru, username;
    SharedPreferences sharedpreferences;


    public static final String TAG_ID = "id_guru";
    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

            text_idguru = (TextView) findViewById(R.id.text_idguru);
            text_username = (TextView) findViewById(R.id.text_username);
            button_logout = (Button) findViewById(R.id.button_logout);
            menuguru = (Button) findViewById(R.id.menuguru);

            sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

            id_guru = getIntent().getStringExtra(TAG_ID);
            username = getIntent().getStringExtra(TAG_USERNAME);

            text_idguru.setText("ID_GURU : " + id_guru);
            text_username.setText("USERNAME : " + username);

            menuguru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main2Activity.this, MenuGuru.class);
                    finish();
                    startActivity(intent);
                }
            });

            button_logout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // update login session ke FALSE dan mengosongkan nilai id dan username
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(Login.session_status, false);
                    editor.putString(TAG_ID, null);
                    editor.putString(TAG_USERNAME, null);
                    editor.commit();

                    Intent intent = new Intent(Main2Activity.this, LoginGuru.class);
                    finish();
                    startActivity(intent);
                }
            });

        }

}
