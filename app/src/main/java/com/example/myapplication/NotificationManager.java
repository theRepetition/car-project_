package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationManager {
    private Context context;
    private DBHelper dbHelper;
    private List<DBHelper.RecommendedReplacement> recommendedReplacements;

    private static final String NOTIFICATION_PREFS = "NOTIFICATION_SETT";
    public NotificationManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);


    }


    public void scheduleNotification(Calendar scheduleTime, String title, String content) {
        // 로그 추가
        Log.d("NotificationManager", "스케줄: " + title);
        // 현재 시간을 가져옴
        Calendar currentTime = Calendar.getInstance();
        //5초 뒤의 시간을 계산하여 scheduleTime에 설정
        scheduleTime = (Calendar) currentTime.clone();
        scheduleTime = (Calendar) currentTime.clone();
        scheduleTime.add(Calendar.SECOND, 5);
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, title);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_CONTENT, content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // AlarmManager를 사용한 알림 예약구현
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    scheduleTime.getTimeInMillis(),
                    pendingIntent

            );
        }
    }

    public void setupNotifications() {
        // 로그 추가

        SharedPreferences prefs = context.getSharedPreferences(NOTIFICATION_PREFS, Context.MODE_PRIVATE);
        List<CarPart> carParts = dbHelper.getAllCarParts();
        String currentDateString = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        recommendedReplacements = dbHelper.getAllRecommendedReplacements();

        for (CarPart carPart : carParts) {
            String notificationKey = carPart.getName() + "_notification_sent";
            boolean notificationSent = prefs.getBoolean(notificationKey, false);

            // 데이터베이스에서 저장된 교체 날짜 가져오기
            String actualReplacementDate = carPart.getReplacementDate();

            Calendar today = Calendar.getInstance();
            Log.d("actualReplacementDate", actualReplacementDate);
            Log.d("currentDateString", currentDateString);
            Log.d("parseInt(carPart.getMileage()", carPart.getMileage());

            // 예상 교체 날짜 계산
            String expectedReplacementDate = CarPartUtils.calculateExpectedReplacementDate(
                    actualReplacementDate, // 실제 교체 날짜를 사용
                    currentDateString,
                    Integer.parseInt(carPart.getMileage()),
                    recommendedReplacements,
                    carPart.getName()
            );

            // 나머지 코드는 그대로 유지

            Calendar expectedDate = CarPartUtils.convertStringToCalendar(expectedReplacementDate);
            Calendar oneWeekBefore = (Calendar) expectedDate.clone();
            oneWeekBefore.add(Calendar.DAY_OF_YEAR, -7);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = dateFormat.format(expectedDate.getTime());

            if (today.before(oneWeekBefore) && today.before(expectedDate)) {
                // 교체 예정 날짜가 일주일 남았을 경우
                scheduleNotification(oneWeekBefore, carPart.getName() + " 교체 예정:"+dateString, "교체 예정일이 일주일 남았습니다.");

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(notificationKey, true);
                editor.apply();
            } else if (isSameDay(today, expectedDate) || (today.after(expectedDate))) {
                // 교체 예정 날짜가 이미 지났을 경우
                scheduleNotification(expectedDate, carPart.getName() + " 교체 예정:"+dateString, "교체 예정일이 지났습니다.");

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(notificationKey, true);
                editor.apply();
            } else {

                return;
            }
        }
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

}

