package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel; //알림 채널
import android.app.NotificationManager; //알림 관리자
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build; // 빌드 불러오기
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    Intent intent; //인텐트 지정
    PendingIntent pendingIntent; //펜딩 인텐트 인텐트하고 같이 알림을 누르면 바로 어플로 이동되게 만들어줌




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MainActivity.class);// 여기 부터
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //여기까지는 버튼 누르면 알림을 통해 어플 들어가기
        //나중에 어플 화면이 아니라 특정 기능으로 바로 이동하게 만들것


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

    }
}


