package com.Kalaathon.imagetexttranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentuser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentuser != null) {
                    Intent i = new Intent(MainActivity.this, interactive.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity.this,
                            Login.class);
                    startActivity(i);
                }
                finish();
            }
        }, 2000);
    }
}
