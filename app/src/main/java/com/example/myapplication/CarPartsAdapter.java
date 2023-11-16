package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class CarPartsAdapter extends BaseAdapter {
    private Context context;
    private List<CarPart> carParts;

    public CarPartsAdapter(Context context, List<CarPart> carParts) {
        this.context = context;
        this.carParts = carParts;
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

        // TextView 업데이트
        partNameTextView.setText(carPart.getName());
        replacementDateTextView.setText("교체 날짜: " + carPart.getReplacementDate());
        mileageTextView.setText("주행 거리: " + carPart.getMileage());

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