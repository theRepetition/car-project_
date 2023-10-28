package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;

public class Distance_cal extends AppCompatActivity {
    private EditText replacementDateEditText;
    private Button saveButton;
    private DBHelper dbHelper;
    private Spinner partSpinner;
    private Spinner mileageSpinner;
    private ListView carPartsListView;
    private CarPartsAdapter carPartsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_cal);

        // 이미지 뷰와 버튼 변수 선언
        ImageView imageView = findViewById(R.id.imageView);
        Button viewRecommendedButton = findViewById(R.id.viewRecommendedButton);
        Button saveButton = findViewById(R.id.saveButton);

        viewRecommendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 뷰를 보이도록 설정
                imageView.setImageResource(R.drawable.change);
                imageView.setVisibility(View.VISIBLE);
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 이미지 뷰를 숨깁니다.
                imageView.setVisibility(View.GONE);
                return true; // 터치 이벤트를 소비하여 다른 이벤트가 발생하지 않도록 합니다.
            }
        });

        // UI 요소 초기화
        partSpinner = findViewById(R.id.partSpinner);
        replacementDateEditText = findViewById(R.id.replacementDateEditText);
        mileageSpinner = findViewById(R.id.mileageSpinner);
        saveButton = findViewById(R.id.saveButton);
        carPartsListView = findViewById(R.id.carPartsListView);

        // DBHelper 인스턴스 생성
        dbHelper = new DBHelper(this);


        // 부품 목록 설정
        List<String> partList = new ArrayList<>();
        partList.add("타이어");
        partList.add("엔진오일");
        partList.add("에어필터");
        // 나머지 부품 추가

        ArrayAdapter<String> partAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partList);
        partAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partSpinner.setAdapter(partAdapter);

        // 주행 거리 목록 설정
        List<String> mileageList = new ArrayList<>();
        mileageList.add("10 km");
        mileageList.add("20 km");
        // 나머지 주행 거리 추가

        ArrayAdapter<String> mileageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mileageList);
        mileageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mileageSpinner.setAdapter(mileageAdapter);

        // CarPartsAdapter를 ListView에 연결
        carPartsAdapter = new CarPartsAdapter(this, new ArrayList<CarPart>());
        carPartsListView.setAdapter(carPartsAdapter);

        // "저장" 버튼 클릭 이벤트 리스너 설정
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 사용자가 입력한 데이터 가져오기
                String partName = partSpinner.getSelectedItem().toString();
                String replacementDate = replacementDateEditText.getText().toString();
                String mileage = mileageSpinner.getSelectedItem().toString();

                // 데이터베이스에 데이터 저장 또는 업데이트
                long result = dbHelper.insertOrUpdateCarPart(partName, mileage, replacementDate);

                if (result != -1) {
                    // 데이터가 성공적으로 저장되거나 업데이트됨
                    Toast.makeText(Distance_cal.this, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    // 저장 후, 목록 업데이트
                    carPartsAdapter.clear();
                    carPartsAdapter.addAll(dbHelper.getAllCarParts());
                    // ListView를 업데이트한 후, 스크롤하여 최신 데이터를 볼 수 있도록 이동
                    carPartsListView.smoothScrollToPosition(carPartsAdapter.getCount() - 1);
                } else {
                    // 데이터 저장 중 오류 발생
                    Toast.makeText(Distance_cal.this, "데이터 저장 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}