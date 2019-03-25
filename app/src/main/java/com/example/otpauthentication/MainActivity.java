package com.example.otpauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText etPhoneNumber,etOTP;
    Button generateOtp,verifyOtp;
    String phoneNumber,otp,verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        intiateClickListners();
        startFireBaseLogin();
    }

    private void startFireBaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(MainActivity.this, "verification completed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(MainActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(MainActivity.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void intiateClickListners() {
        generateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,MainActivity.this,mCallbacks);
            }
        });
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etOTP.getText().toString();

                PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verificationCode,otp);
                signInWithPhone(credential);
            }
        });
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,SignedIn.class));
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void findViews() {
        etOTP = (EditText)findViewById(R.id.edit_text_otp);
        etPhoneNumber = (EditText)findViewById(R.id.edit_text_phone_number);
        generateOtp = (Button)findViewById(R.id.button_generate_otp);
        verifyOtp = (Button)findViewById(R.id.button_verify_otp);
    }
}
