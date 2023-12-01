package com.example.myapplication;

import android.os.Bundle;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // 24시간 형식으로 설정


    }
}