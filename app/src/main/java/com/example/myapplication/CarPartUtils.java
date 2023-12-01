package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CarPartUtils {

//교체 예정 날짜 계산 클래스
    public static String calculateExpectedReplacementDate(String replacementDate, String currentDateString, int dailyMileage, List<DBHelper.RecommendedReplacement> recommendedReplacements, String partName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date replacementDateObj = dateFormat.parse(replacementDate);
            Date currentDateObj = dateFormat.parse(currentDateString);
            if (replacementDateObj != null && currentDateObj != null) {
                //날짜 계산 성공한 경우만
                long daysPassed = (currentDateObj.getTime() - replacementDateObj.getTime()) / (24 * 60 * 60 * 1000); //교체 이후 날짜 계산
                long totalMileageSinceReplacement = daysPassed * dailyMileage; // 주행거리 계산

                int recommendedKilometers = getRecommendedReplacementKilometers(recommendedReplacements, partName);// 권장 교체주기(주행거리) 가져오기
                String a= String.valueOf(recommendedKilometers);
                Log.d("recommendedKilometers", a);
                long daysUntilNextReplacement = (recommendedKilometers - totalMileageSinceReplacement) / dailyMileage;//다음교체까지 남은 일수 계산
                String b= String.valueOf(daysUntilNextReplacement);
                Log.d("daysUntilNextReplacement", b);
                long expectedTime = currentDateObj.getTime() + (daysUntilNextReplacement * 24 * 60 * 60 * 1000L); //예상되는 교체 날짜 계산
                String c= String.valueOf(expectedTime);
                Log.d("expectedTime", c);
                return dateFormat.format(new Date(expectedTime));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null; //오류시 널값
    }

//주행거리 계산
    private static int getRecommendedReplacementKilometers(List<DBHelper.RecommendedReplacement> recommendedReplacements, String partName) {
        for (DBHelper.RecommendedReplacement replacement : recommendedReplacements) {
            //Log.d("getRecommendedReplacementKilometers",partName);
            if (replacement.getPartName().equals(partName)) {

                return replacement.getReplacementKilometers();
            }
        }
        return 0;
    }
    //캘린더 객체로 변환
    public static Calendar convertStringToCalendar(String dateString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        if (dateString == null || dateString.equals("00000000")) {

            return calendar; // 기본값설정

        }
        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    private static void scheduleNotification(Context context, String notificationTitle, String notificationContent, Calendar calendar) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, notificationTitle);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_CONTENT, notificationContent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}
