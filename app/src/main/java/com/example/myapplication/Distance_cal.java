package com.example.myapplication;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.widget.DatePicker;
import java.util.Calendar;

public class Distance_cal extends AppCompatActivity {

    private Button saveButton;
    private DBHelper dbHelper;
    private Spinner partSpinner;
    private Spinner mileageSpinner;
    private ListView carPartsListView;
    private CarPartsAdapter carPartsAdapter;
    private List<DBHelper.RecommendedReplacement> recommendedReplacements;
    // 연, 월, 일 EditText 필드
    private EditText yearEditText, monthEditText, dayEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_cal);

        dbHelper = new DBHelper(this);  // DBHelper 객체 초기화

        // 권장 교체 주기 데이터 가져오기
        recommendedReplacements = dbHelper.getAllRecommendedReplacements();


        // 이미지 뷰와 버튼 변수 선언
        ImageView imageView = findViewById(R.id.imageView);
        Button viewRecommendedButton = findViewById(R.id.viewRecommendedButton);
        Button saveButton = findViewById(R.id.saveButton);
        // 연, 월, 일 EditText 필드
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
        yearEditText = findViewById(R.id.yearEditText);
        monthEditText = findViewById(R.id.monthEditText);
        dayEditText = findViewById(R.id.dayEditText);
        partSpinner = findViewById(R.id.partSpinner);
        mileageSpinner = findViewById(R.id.mileageSpinner);
        saveButton = findViewById(R.id.saveButton);
        carPartsListView = findViewById(R.id.carPartsListView);


        // 부품 목록 설정
        List<String> partList = dbHelper.getAllPartNames();
        ArrayAdapter<String> partAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partList);
        partAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partSpinner.setAdapter(partAdapter);
        if (partAdapter.getCount() > 0) {
            partSpinner.setSelection(0);
        }

        // 주행 거리 목록 설정
        mileageSpinner = findViewById(R.id.mileageSpinner);
        ArrayAdapter<String> mileageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"0", "20", "50", "100", "150", "200", "500"});
        mileageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mileageSpinner.setAdapter(mileageAdapter);
        // 나머지 주행 거리 추가

        // CarPartsAdapter를 ListView에 연결
        carPartsAdapter = new CarPartsAdapter(this, new ArrayList<CarPart>());
        carPartsListView.setAdapter(carPartsAdapter);

        // "저장" 버튼 클릭 이벤트 리스너 설정


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    // 사용자 입력을 정수로 변환
                    int year = Integer.parseInt(yearEditText.getText().toString());
                    int month = Integer.parseInt(monthEditText.getText().toString()) - 1; // Calendar는 월을 0부터 시작
                    int day = Integer.parseInt(dayEditText.getText().toString());

                    // Calendar를 사용하여 날짜 유효성 검사
                    Calendar calendar = Calendar.getInstance();
                    calendar.setLenient(false);
                    calendar.set(year, month, day);
                    calendar.getTime(); // 유효하지 않은 날짜면 여기서 예외가 발생

                    // 데이터베이스에 저장할 날짜 형식으로 변환
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());

                    // 데이터베이스 저장 로직 추가
                    String partName = partSpinner.getSelectedItem().toString();
                    String mileage = mileageSpinner.getSelectedItem().toString();
                    long result = dbHelper.insertOrUpdateCarPart(partName, mileage, formattedDate);

                    if (result != -1) {
                        Toast.makeText(Distance_cal.this, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        // ListView 업데이트
                        carPartsAdapter.clear();
                        carPartsAdapter.addAll(dbHelper.getAllCarParts());
                        carPartsListView.smoothScrollToPosition(carPartsAdapter.getCount() - 1);
                    } else {
                        Toast.makeText(Distance_cal.this, "데이터 저장 실패.", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(Distance_cal.this, "날짜 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(Distance_cal.this, "유효하지 않은 날짜입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private int calculateTotalMileage(String replacementDate, String currentDateString, String partName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        try {
            // 교체 날짜와 현재 날짜를 Date 객체로 파싱
            Date replacementDateObj = dateFormat.parse(replacementDate);
            Date currentDateObj = dateFormat.parse(currentDateString);

            // 현재 날짜와 교체 날짜 간의 일수 차이 계산
            long daysDifference = (currentDateObj.getTime() - replacementDateObj.getTime()) / (24 * 60 * 60 * 1000);

            // 교체 날짜부터 현재까지의 총 주행 거리 계산
            int totalMileage = Integer.parseInt(mileageSpinner.getSelectedItem().toString()) * (int) daysDifference;

            return totalMileage;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1; // 오류 시 -1 반환
    }

    // 다음 교체 예정 주행 거리 계산
    private int calculateNextReplacementDistance(String replacementDate, String partName) {
        // 사용자가 입력한 데이터로 총 주행 거리 계산
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        int totalMileage = calculateTotalMileage(replacementDate, currentDate, partName);

        // 권장 교체 주기 데이터에서 해당 부품의 교체 주기 가져오기
        int recommendedReplacementDistance = getRecommendedReplacementDistance(partName);

        // 다음 교체 예정 주행 거리 계산
        int nextReplacementDistance = recommendedReplacementDistance - totalMileage;

        return nextReplacementDistance;
    }

    // 부품별 권장 교체 주행 거리 가져오기
    private int getRecommendedReplacementDistance(String partName) {
        // 권장 교체 주기 데이터에서 해당 부품의 교체 주기 찾기
        for (DBHelper.RecommendedReplacement recommendedReplacement : recommendedReplacements) {
            if (recommendedReplacement.getPartName().equals(partName)) {
                return recommendedReplacement.getReplacementKilometers();
            }
        }

        return 0; // 해당 부품의 권장 교체 주행 거리가 없으면 0 반환
    }
}
