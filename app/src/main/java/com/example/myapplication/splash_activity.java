package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;  // 핸들러
import android.content.Intent; // 인텐트

public class splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000); //스플래시 화면이 지속될 시간 지정
    }
}
