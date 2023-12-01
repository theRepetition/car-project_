package com.example.myapplication;

import android.provider.BaseColumns;

public final class CarPartContract {
    private CarPartContract() {}

    public static class CarPartEntry implements BaseColumns {
        public static final String TABLE_NAME = "car_parts";
        public static final String COLUMN_PART_NAME = "part_name";
        public static final String COLUMN_REPLACEMENT_DATE = "replacement_date";
        public static final String COLUMN_MILEAGE = "mileage";
        public static final String COLUMN_NOTE = "note"; // 이 부분을 추가
    }
}