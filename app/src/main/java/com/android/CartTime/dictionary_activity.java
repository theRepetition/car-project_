package com.android.CartTime;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class dictionary_activity extends AppCompatActivity {

    private DictionaryDatabaseHelper dbHelper;
    private AutoCompleteTextView wordInputAuto;
    private Button searchButton;
    private TextView definitionOutput;
    private ListView listViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dbHelper = new DictionaryDatabaseHelper(this);

        wordInputAuto = findViewById(R.id.wordInput);
        searchButton = findViewById(R.id.searchButton);
        definitionOutput = findViewById(R.id.definitionOutput);
        listViewResults = findViewById(R.id.listViewResults);
        performSearch();
        // 자동완성 데이터 설정
        ArrayList<String> words = getAllWords();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, words);
        wordInputAuto.setAdapter(adapter);

        wordInputAuto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchButton.performClick(); // 검색 버튼 클릭 효과
                    return true;
                }
                return false;
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWord = (String) parent.getItemAtPosition(position);
                String fullDefinition = getFullDefinition(selectedWord);

                // 검색 및 리스트 뷰 숨기기
                wordInputAuto.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                listViewResults.setVisibility(View.GONE);

                // 전체 정의 표시
                definitionOutput.setText(fullDefinition);
                definitionOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                definitionOutput.setVisibility(View.VISIBLE);
            }
        });
    }
    private String getFullDefinition(String word) {
        String fullDefinition = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + DictionaryDatabaseHelper.COLUMN_DEFINITION + " FROM " + DictionaryDatabaseHelper.TABLE_NAME +
                " WHERE " + DictionaryDatabaseHelper.COLUMN_WORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{word});

        if (cursor != null) {
            int definitionIndex = cursor.getColumnIndex(DictionaryDatabaseHelper.COLUMN_DEFINITION);
            if (cursor.moveToFirst()) {
                fullDefinition = cursor.getString(definitionIndex);
            }
            cursor.close();
        }
        db.close();
        return fullDefinition;
    }

    private void performSearch() {
        String searchText = wordInputAuto.getText().toString();
        ArrayList<String> definitions = getDefinitions(searchText);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(dictionary_activity.this, android.R.layout.simple_list_item_1, definitions);
        listViewResults.setAdapter(arrayAdapter);

        // 검색 결과 화면 설정
        wordInputAuto.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        listViewResults.setVisibility(View.VISIBLE);
        definitionOutput.setVisibility(View.GONE);
    }

    // 뒤로가기 처리
    @Override
    public void onBackPressed() {
        if (definitionOutput.getVisibility() == View.VISIBLE) {
            // 검색 관련 뷰 보이기
            wordInputAuto.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            listViewResults.setVisibility(View.VISIBLE);
            definitionOutput.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
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

    // 단어의 모든 정의를 가져오는 메소드
    private ArrayList<String> getDefinitions(String searchText) {
        ArrayList<String> words = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT DISTINCT " + DictionaryDatabaseHelper.COLUMN_WORD + " FROM " + DictionaryDatabaseHelper.TABLE_NAME +
                " WHERE " + DictionaryDatabaseHelper.COLUMN_WORD + " LIKE ? OR " +
                DictionaryDatabaseHelper.COLUMN_DEFINITION + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + searchText + "%", "%" + searchText + "%"});

        if (cursor != null) {
            int wordIndex = cursor.getColumnIndex(DictionaryDatabaseHelper.COLUMN_WORD);

            while (cursor.moveToNext()) {
                words.add(cursor.getString(wordIndex));
            }
            cursor.close();
        }
        db.close();
        return words;
    }
}
