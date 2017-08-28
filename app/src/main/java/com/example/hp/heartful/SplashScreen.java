package com.example.hp.heartful;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class SplashScreen extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}