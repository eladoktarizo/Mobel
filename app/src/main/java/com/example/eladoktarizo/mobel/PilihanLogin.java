package com.example.eladoktarizo.mobel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PilihanLogin extends AppCompatActivity {

    Button btnloginortu, btnloginguru;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilihan_login);

        btnloginguru = findViewById(R.id.buttonlogin1);
        btnloginortu = findViewById(R.id.buttonlogin2);

        btnloginguru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PilihanLogin.this, LoginGuru.class);
                startActivity(intent);
            }
        });

        btnloginortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PilihanLogin.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
