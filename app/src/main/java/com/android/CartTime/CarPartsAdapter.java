package com.android.CartTime;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
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

    public CarPartsAdapter(Context context, List<CarPart> carParts, List<DBHelper.RecommendedReplacement> recommendedReplacements) {
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

        CarPart carPart = (CarPart) getItem(position);

        TextView partNameTextView = convertView.findViewById(R.id.partNameTextView);
        TextView replacementDateTextView = convertView.findViewById(R.id.replacementDateTextView);
        TextView mileageTextView = convertView.findViewById(R.id.mileageTextView);
        TextView expectedDateTextView = convertView.findViewById(R.id.expectedDateTextView);

        partNameTextView.setText(carPart.getName());
        mileageTextView.setText("주행 거리: " + carPart.getMileage());

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String expectedReplacementDate = CarPartUtils.calculateExpectedReplacementDate(
                carPart.getReplacementDate(),
                new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()),
                Integer.parseInt(carPart.getMileage()),
                recommendedReplacements,
                carPart.getName());
        try {                               //연월일 형태로 표시 시도
            Date replacementDate = originalFormat.parse(carPart.getReplacementDate());
            String formattedReplacementDate = newFormat.format(replacementDate);
            replacementDateTextView.setText("교체 날짜: " + formattedReplacementDate);
            Date expectedDate = originalFormat.parse(expectedReplacementDate);
            String formattedExpectedDate = newFormat.format(expectedDate);
            expectedDateTextView.setText("교체 예정: " + formattedExpectedDate);
        } catch (ParseException e) {         //변환 실패시 그냥 원래대로라도 표시
            e.printStackTrace();
            replacementDateTextView.setText("교체 날짜: " + carPart.getReplacementDate());
            expectedDateTextView.setText("교체 예정: " + carPart.getReplacementDate());
        }

        Calendar today = Calendar.getInstance();
        Calendar expectedDate = CarPartUtils.convertStringToCalendar(expectedReplacementDate);
        Calendar oneWeekBefore = (Calendar) expectedDate.clone();
        oneWeekBefore.add(Calendar.DAY_OF_YEAR, -7);

        if (today.after(expectedDate)) {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        } else if (today.after(oneWeekBefore) && today.before(expectedDate)) {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        return convertView;
    }

    public void clear() {
        carParts.clear();
        notifyDataSetChanged();
    }

    public void remove(CarPart carPart) {
        carParts.remove(carPart);
        notifyDataSetChanged();
    }

    public void addAll(List<CarPart> carParts) {
        this.carParts.addAll(carParts);
    }
}
