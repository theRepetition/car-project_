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

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    Intent intent; //인텐트 지정
    PendingIntent pendingIntent; //펜딩 인텐트 인텐트하고 같이 알림을 누르면 바로 어플로 이동되게 만들어줌

    private void createNotificationChannel() {   // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            CharSequence name = "이름"; //이름
            String description = "문구"; // 문구
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } //나중에 case-switch나 if문을 통해서 조건에 따라 출력되는 텍스트를 다르게 해줘야됨
    }
    public void showNotification(View view) { // 버튼을 눌러서 알림 테스트 용도
        // 나중에 조건을 통해 알림이 가게끔 수정해야됨
        String channelId = "Notification_ID"; //알림 아이디

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) //이거 지우고
                //.setSmallIcon(R.drawable.ic_notification) // 나중에 R.drawable.ic_notification에 알림 아이콘 경로 추가 할것
                .setContentTitle("제목 - 테스트")
                .setContentText("내용 - 테스트")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)//누르면 전환되게
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this); //권한 추가 향후 mainfest 수정해서 제거
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(123, builder.build()); //123은 알림 id 나중에 필요할때 사용
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MainActivity.class);// 여기 부터
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //여기까지는 버튼 누르면 알림을 통해 어플 들어가기
        //나중에 어플 화면이 아니라 특정 기능으로 바로 이동하게 만들것

        createNotificationChannel(); //함수 호출
    }
}


