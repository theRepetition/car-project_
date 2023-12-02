package com.android.CartTime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ScheduleCarPartCheck {
    public static void scheduleWork(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int defaultHour = 12; // 기본 시간 설정
        int defaultMinute = 0; // 기본 분 설정
        int hour = sharedPreferences.getInt("hour", defaultHour);
        int minute = sharedPreferences.getInt("minute", defaultMinute); //preferences로 설정한 시간 불러오기
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND, 10);

        // 다음 날로 넘어갔으면 시간을 하루 더해주기
        if (calendar.getTimeInMillis() < currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        long delay = calendar.getTimeInMillis() - currentTime;

        // OneTimeWorkRequest 생성 및 초기 지연 시간 설정
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CarPartWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();

        // WorkManager를 사용하여 작업 예약
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
