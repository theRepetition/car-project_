package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CarPartsAdapter extends BaseAdapter {
    private Context context;
    private List<CarPart> carParts;
    private List<DBHelper.RecommendedReplacement> recommendedReplacements;
    public CarPartsAdapter(Context context, List<CarPart> carParts,List<DBHelper.RecommendedReplacement> recommendedReplacements) {
        this.context = context;
        this.carParts = carParts;
        this.recommendedReplacements = recommendedReplacements != null ? recommendedReplacements : new ArrayList<>();
    }

    @Override
    public int getCount() {
        return carParts.size();
    }

    @Override
    public Object getItem(int position) {
        return carParts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_car_part, parent, false);
        }

        // 현재 위치의 CarPart 객체 가져오기
        CarPart carPart = (CarPart) getItem(position);

        // TextView 엘리먼트 찾기
        TextView partNameTextView = convertView.findViewById(R.id.partNameTextView);
        TextView replacementDateTextView = convertView.findViewById(R.id.replacementDateTextView);
        TextView mileageTextView = convertView.findViewById(R.id.mileageTextView);
        TextView expectedDateTextView = convertView.findViewById(R.id.expectedDateTextView); //교체 예정 날짜 추가

        // TextView 업데이트
        partNameTextView.setText(carPart.getName());
        replacementDateTextView.setText("교체 날짜: " + carPart.getReplacementDate());
        mileageTextView.setText("주행 거리: " + carPart.getMileage());

        // 교체 예정 날짜 계산 및 표시





        // 남은 날짜에 따라 색깔 표시
        // 교체 예정 날짜 계산
        String currentDateString = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String expectedReplacementDate = CarPartUtils.calculateExpectedReplacementDate(
                carPart.getReplacementDate(),
                currentDateString,
                Integer.parseInt(carPart.getMileage()),
                recommendedReplacements,
                carPart.getName()
        );
        expectedDateTextView.setText("교체 예정: " + expectedReplacementDate);
        // 현재 날짜와 교체 예정 날짜 비교
        Calendar today = Calendar.getInstance();
        Calendar expectedDate = CarPartUtils.convertStringToCalendar(expectedReplacementDate);
        Calendar oneWeekBefore = (Calendar) expectedDate.clone();
        oneWeekBefore.add(Calendar.DAY_OF_YEAR, -7);

        // 남은 기간에 따라 색상 변경
        if (today.after(expectedDate)) {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light)); // 초록색
        } else if (today.after(oneWeekBefore) && today.before(expectedDate)) {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light)); // 하늘색
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white)); // 기본 색상
        }

        return convertView;





    }

    // clear 메소드 정의
    public void clear() {
        carParts.clear();
        notifyDataSetChanged();
    }
    //삭제
    public void remove(CarPart carPart) {
        carParts.remove(carPart);
        notifyDataSetChanged();
    }

    // addAll 메소드 정의
    public void addAll(List<CarPart> carParts) {
        this.carParts.addAll(carParts);
    }
}