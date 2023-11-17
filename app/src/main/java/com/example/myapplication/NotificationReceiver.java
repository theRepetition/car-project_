package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_TITLE = "notification_title";
    public static final String NOTIFICATION_CONTENT = "notification_content";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);

        // 알림을 클릭했을 때 열릴 액티비티 설정
        Intent notificationIntent = new Intent(context, Distance_cal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification_ID")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // 알림 아이콘 설정
                .setContentTitle(title) // 알림 제목
                .setContentText(content) // 알림 내용
                .setContentIntent(pendingIntent) // 알림 클릭 시 인텐트
                .setAutoCancel(true); // 알림 클릭 시 자동으로 삭제

        // 알림 관리자를 통해 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }
}