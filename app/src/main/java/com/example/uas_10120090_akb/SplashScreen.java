package com.example.uas_10120090_akb;
//NIM   : 10102090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 3000L); //3000 L = 3 detik
    }
}