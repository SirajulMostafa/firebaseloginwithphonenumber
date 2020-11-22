package com.mostafa.firebaseloginwithphonenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
private  Button login;
private EditText phone;
private CountryCodePicker countryCodePicker;

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