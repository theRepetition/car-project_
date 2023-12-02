package com.android.CartTime;

import androidx.appcompat.app.AppCompatActivity;


import android.app.PendingIntent;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    Intent intent; //인텐트 지정
    PendingIntent pendingIntent; //펜딩 인텐트 인텐트하고 같이 알림을 누르면 바로 어플로 이동되게 만들어줌
    @Override
    protected void onResume() { //시작및 복귀시 알림 호출
        super.onResume();
        ScheduleCarPartCheck.scheduleWork(this);
        NotificationHelper.createNotificationChannel(this);//알림 채널
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, dictionary_activity.class);
                startActivity(intent);
            }
        });
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Distance_cal.class);
                startActivity(intent);
            }
        });
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });

    }
}


