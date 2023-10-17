package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class dictionary_activity extends AppCompatActivity {

    private DictionaryDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private EditText wordInput;
    private Button searchButton;
    private TextView definitionOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dbHelper = new DictionaryDatabaseHelper(this);

        wordInput = findViewById(R.id.wordInput);
        searchButton = findViewById(R.id.searchButton);
        definitionOutput = findViewById(R.id.definitionOutput);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = wordInput.getText().toString();
                String definition = getDefinition(word);
                definitionOutput.setText(definition);
            }
        });
    }

    private String getDefinition(String word) {
        String definition = "Not found";
        // 여기에서 읽기 전용 데이터베이스를 엽니다.
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
        db.close(); // 데이터베이스를 닫습니다.
        return definition;
    }
}
