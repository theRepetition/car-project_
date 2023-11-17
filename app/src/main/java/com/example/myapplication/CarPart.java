package com.example.myapplication;

import java.util.List;

public class CarPart {
    private int id;
    private String name;
    private String replacementDate;
    private String mileage;
    private String note;  // 'note' 필드 추가

    public CarPart(int id, String name, String replacementDate, String mileage) {
        this.id = id;
        this.name = name;
        this.replacementDate = replacementDate;
        this.mileage = mileage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReplacementDate() {
        return replacementDate;
    }

    public String getMileage() {
        return mileage;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReplacementDate(String replacementDate) {
        this.replacementDate = replacementDate;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String calculateExpectedReplacementDate(List<DBHelper.RecommendedReplacement> recommendedReplacements) {
        for (DBHelper.RecommendedReplacement replacement : recommendedReplacements) {
            if (this.name.equals(replacement.getPartName())) {
                // 교체 예정 날짜 계산 로직을 추가
                // 현재 날짜에서 권장 교체 주기를 기반으로 교체 예정 날짜를 계산
            }
        }
        return "계산된 날짜"; // 교체 예정 날짜를 문자열로 반환
    }
}
