package com.example.myapplication;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ScheduleCarPartCheck {
    public static void scheduleWork(Context context) {
        // 1분 간격으로 반복되는 OneTimeWorkRequest 생성
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder(CarPartWorker.class);

        // 필요한 경우 제약조건 설정
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        builder.setConstraints(constraints);

        // 1분 후에 작업 실행(임시 시간)
        OneTimeWorkRequest workRequest = builder
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();

        // WorkManager를 사용하여 작업 예약
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}