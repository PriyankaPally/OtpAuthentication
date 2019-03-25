package com.example.otpauthentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedIn extends AppCompatActivity {

    TextView phoneText;
    Button btn_signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        init();
        setPhoneNumber();
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SignedIn.this,MainActivity.class));
                finish();
            }
        });
    }

    private void setPhoneNumber() {

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        try {
            phoneText.setText(user.getPhoneNumber());

        }catch (Exception e){
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show();

        }
    }

    private void init() {
        phoneText = (TextView)findViewById(R.id.tv_phone_number);
        btn_signOut = (Button)findViewById(R.id.btn_sign_out);
    }
}
