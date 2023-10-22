package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class dictionary_activity extends AppCompatActivity {

    private DictionaryDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private AutoCompleteTextView wordInputAuto; // 자동완성 뷰
    private Button searchButton;
    private TextView definitionOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dbHelper = new DictionaryDatabaseHelper(this);

        wordInputAuto = findViewById(R.id.wordInput); // 뷰 바인딩
        searchButton = findViewById(R.id.searchButton);
        definitionOutput = findViewById(R.id.definitionOutput);

        // 자동완성 데이터 설정
        ArrayList<String> words = getAllWords();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, words);
        wordInputAuto.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = wordInputAuto.getText().toString(); // 변경된 부분
                String definition = getDefinition(word);
                definitionOutput.setText(definition);
            }
        });
    }

    // 모든 단어를 가져오는 메소드
    private ArrayList<String> getAllWords() {
        ArrayList<String> words = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DictionaryDatabaseHelper.COLUMN_WORD + " FROM " + DictionaryDatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(DictionaryDatabaseHelper.COLUMN_WORD);
            if(columnIndex != -1) {
                while (cursor.moveToNext()) {
                    words.add(cursor.getString(columnIndex));
                }
            } else {
                Log.d("없음", "결과를 찾을수 없음");
            }
            cursor.close();
        }
        db.close();
        return words;
    }

    private String getDefinition(String word) {
        String definition = "Not found";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DictionaryDatabaseHelper.TABLE_NAME + " WHERE " + DictionaryDatabaseHelper.COLUMN_WORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{word});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(DictionaryDatabaseHelper.COLUMN_DEFINITION);
                if (index != -1) {
                    definition = cursor.getString(index);
                } else {
                    Log.d("DB_ERROR", "COLUMN_DEFINITION not found in the result");
                }
            }
            cursor.close();
        }
        db.close();
        return definition;
    }
}
