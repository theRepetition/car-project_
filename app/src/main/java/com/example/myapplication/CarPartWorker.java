package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CarPartWorker extends Worker {

    public CarPartWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // NotificationManager 인스턴스 생성
        NotificationManager notificationManager = new NotificationManager(getApplicationContext());
        Log.d("NotificationWorker", "doWork() 메서드 실행");
        // NotificationManager의 알림 체크 및 발송 메소드 호출
        notificationManager.setupNotifications();

        return Result.success();
    }
}

