package com.mostafa.firebaseloginwithphonenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
private  Button login;
private EditText phone;
private CountryCodePicker countryCodePicker;
FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _init();
    }

    private  void _init(){
        login = (Button) findViewById(R.id.btnLogin);
        phone = (EditText) findViewById(R.id.txtPhone);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.county_code_id);

        login.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,VerificationOtp.class);
            countryCodePicker.registerCarrierNumberEditText(phone);
            countryCodePicker.getFullNumberWithPlus();
          //  countryCodePicker.getFullNumberWithPlus();
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus().replace(" ",""));
             Log.e("abc","value:----"+countryCodePicker.getFullNumberWithPlus());
            // intent.putExtra("phone","+8801821150081");
            startActivity(intent);
        });

    }
}