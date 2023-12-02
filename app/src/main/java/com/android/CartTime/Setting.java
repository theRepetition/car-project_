package com.android.CartTime;

import android.os.Build;
import android.os.Bundle;

import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting extends AppCompatActivity {

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int defaultHour = 12; // 기본 시간 설정, 예: 오전 12시
        int defaultMinute = 0; // 기본 분 설정, 예: 0분

        int hour = sharedPreferences.getInt("hour", defaultHour);
        int minute = sharedPreferences.getInt("minute", defaultMinute);

        // TimePicker에 저장된 시간 설정
        if (Build.VERSION.SDK_INT >= 23 ) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

        Button saveButton = findViewById(R.id.Savebutton); // 저장 버튼
        saveButton.setOnClickListener(v -> saveTime());
    }

    public void saveTime() {
        int hour, minute;
        if (Build.VERSION.SDK_INT >= 23 ) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.apply();

        ScheduleCarPartCheck.scheduleWork(this);

    }
}