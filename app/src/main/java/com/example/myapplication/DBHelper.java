package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "car_parts.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String RECOMMENDED_DATABASE_NAME = "recommended_parts.db";
    public void insertRecommendedReplacement(String partName, int years, int kilometers, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("part_name", partName);
        values.put("replacement_years", years);
        values.put("replacement_kilometers", kilometers);

        long newRowId = db.insert("recommended_replacement", null, values);
    }
    public List<String> getAllPartNames() {
        List<String> partNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                "part_name"
        };

        Cursor cursor = db.query(
                "recommended_replacement",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String partName = cursor.getString(cursor.getColumnIndexOrThrow("part_name"));
            partNames.add(partName);
        }

        cursor.close();
        db.close();

        return partNames;
    }


    public class RecommendedReplacement {
        private int id;
        private String partName;
        private int replacementYears;
        private int replacementKilometers;

        public RecommendedReplacement(int id, String partName, int replacementYears, int replacementKilometers) {
            this.id = id;
            this.partName = partName;
            this.replacementYears = replacementYears;
            this.replacementKilometers = replacementKilometers;
        }

        public int getId() {
            return id;
        }

        public String getPartName() {
            return partName;
        }

        public int getReplacementYears() {
            return replacementYears;
        }

        public int getReplacementKilometers() {
            return replacementKilometers;
        }
    }
    private List<RecommendedReplacement> recommendedReplacements = new ArrayList<>();


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CAR_PARTS_TABLE = "CREATE TABLE " +
                CarPartContract.CarPartEntry.TABLE_NAME + " (" +
                CarPartContract.CarPartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CarPartContract.CarPartEntry.COLUMN_PART_NAME + " TEXT NOT NULL, " +
                CarPartContract.CarPartEntry.COLUMN_REPLACEMENT_DATE + " TEXT NOT NULL, " +
                CarPartContract.CarPartEntry.COLUMN_MILEAGE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_CAR_PARTS_TABLE);

        String SQL_CREATE_RECOMMENDED_REPLACEMENT_TABLE = "CREATE TABLE " +
                "recommended_replacement" + " (" +
                "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "part_name" + " TEXT NOT NULL, " +
                "replacement_years" + " INTEGER, " +
                "replacement_kilometers" + " INTEGER);";

        db.execSQL(SQL_CREATE_RECOMMENDED_REPLACEMENT_TABLE);

        // 권장 교체 주기 데이터 추가
        insertRecommendedReplacement("엔진오일", 0, 8500, db);
        insertRecommendedReplacement("에어필터", 0, 17500, db);
        insertRecommendedReplacement("브레이크 패드", 0, 65000, db);
        insertRecommendedReplacement("브레이크 로터와 드럼", 0, 100000, db);
        insertRecommendedReplacement("브레이크 오일", 0, 50000, db);
        insertRecommendedReplacement("냉각수", 0, 85000, db);
        insertRecommendedReplacement("타이어", 5, 50000, db);
        insertRecommendedReplacement("배터리", 3, 0, db);

    }

 // - 같은 부품인데도 불구하고 데이터베이스에 덮어씌워지지 않는 문제
 //- 주행거리가 같을때만 덮어 씌워짐 -> 해결 위해 아래 코드 수정
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 업그레이드 로직 추가
    }

    public long insertOrUpdateCarPart(String partName, String mileage, String replacementDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CarPartContract.CarPartEntry.COLUMN_PART_NAME, partName);
        values.put(CarPartContract.CarPartEntry.COLUMN_REPLACEMENT_DATE, replacementDate);
        values.put(CarPartContract.CarPartEntry.COLUMN_MILEAGE, mileage);

        String whereClause = CarPartContract.CarPartEntry.COLUMN_PART_NAME + " = ?";
        String[] whereArgs = {partName};

        int updatedRows = db.update(
                CarPartContract.CarPartEntry.TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (updatedRows > 0) {
            db.close();
            return updatedRows;
        } else {
            long newRowId = db.insert(CarPartContract.CarPartEntry.TABLE_NAME, null, values);
            db.close();
            return newRowId;
        }
    }

    public List<CarPart> getAllCarParts() {
        List<CarPart> carParts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                CarPartContract.CarPartEntry._ID,
                CarPartContract.CarPartEntry.COLUMN_PART_NAME,
                CarPartContract.CarPartEntry.COLUMN_REPLACEMENT_DATE,
                CarPartContract.CarPartEntry.COLUMN_MILEAGE
        };

        String sortOrder = CarPartContract.CarPartEntry._ID + " DESC";

        Cursor cursor = db.query(
                CarPartContract.CarPartEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CarPartContract.CarPartEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CarPartContract.CarPartEntry.COLUMN_PART_NAME));
            String replacementDate = cursor.getString(cursor.getColumnIndexOrThrow(CarPartContract.CarPartEntry.COLUMN_REPLACEMENT_DATE));
            String mileage = cursor.getString(cursor.getColumnIndexOrThrow(CarPartContract.CarPartEntry.COLUMN_MILEAGE));

            CarPart carPart = new CarPart(id, name, replacementDate, mileage);
            carParts.add(carPart);
        }

        cursor.close();
        db.close();

        return carParts;
    }
    // 데이터 베이스 청소 메소드
    public void clearCarParts() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CarPartContract.CarPartEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteCarPart(String partName, String replacementDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 삭제할 항목을 식별하기 위한 조건을 설정
        String selection = CarPartContract.CarPartEntry.COLUMN_PART_NAME + " = ? AND " + CarPartContract.CarPartEntry.COLUMN_REPLACEMENT_DATE + " = ?";
        String[] selectionArgs = { partName, replacementDate };
        db.delete(CarPartContract.CarPartEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }


    public List<RecommendedReplacement> getAllRecommendedReplacements() {
        List<RecommendedReplacement> replacements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                "_id",
                "part_name",
                "replacement_years",
                "replacement_kilometers"
        };

        Cursor cursor = db.query(
                "recommended_replacement",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String partName = cursor.getString(cursor.getColumnIndexOrThrow("part_name"));
            int replacementYears = cursor.getInt(cursor.getColumnIndexOrThrow("replacement_years"));
            int replacementKilometers = cursor.getInt(cursor.getColumnIndexOrThrow("replacement_kilometers"));

            RecommendedReplacement replacement = new RecommendedReplacement(id, partName, replacementYears, replacementKilometers);
            replacements.add(replacement);
        }

        cursor.close();
        db.close();

        return replacements;
    }





}