package com.mostafa.firebaseloginwithphonenumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationOtp extends AppCompatActivity {
    private final static String TAG="abc";
    private TextView vCode;
    private Button submit;
    private String phoneNumber;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser !=null){
            startActivity(new Intent(VerificationOtp.this,Dashboard.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);
        _init();
        precessingOpt();
        submit.setOnClickListener(view -> {
            //here need some validation for input code
         String   userPinCode = vCode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, userPinCode);
            signInWithPhoneAuthCredential(credential);

        });
    }




     private  void _init(){
         submit = (Button) findViewById(R.id.btn_otp_confirm);
         vCode = (TextView) findViewById(R.id.txt_otp_id);
         phoneNumber= getIntent().getStringExtra("phone");
         Log.d(TAG,"phoneNumber:  "+phoneNumber);

     }

     private void precessingOpt(){
         PhoneAuthOptions options =
                 PhoneAuthOptions.newBuilder(mAuth)
                         .setPhoneNumber(phoneNumber)       // Phone number to verify
                         .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                         .setActivity(this)                 // Activity (for callback binding)
                         .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                         .build();
         PhoneAuthProvider.verifyPhoneNumber(options);

     }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks   mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);
            String code = credential.getSmsCode();
            if(code != null){
                vCode.setText(code);
                verifyCode(code);
            }

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(VerificationOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(VerificationOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(VerificationOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(VerificationOtp.this,Dashboard.class));
                            Log.d(TAG, "signInWithCredential:success");
                                finish();
                           // FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


}