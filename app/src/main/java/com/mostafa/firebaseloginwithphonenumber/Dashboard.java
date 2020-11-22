package com.mostafa.firebaseloginwithphonenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class Dashboard extends AppCompatActivity {
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        _init();
    }

    private  void _init(){
        logout = (Button) findViewById(R.id.btn_logout_id);
        FirebaseAuth.getInstance().signOut();
        logout.setOnClickListener(view -> {
            startActivity(new Intent(Dashboard.this,MainActivity.class));

        });

    }
}