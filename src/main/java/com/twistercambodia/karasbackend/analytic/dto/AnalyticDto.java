package com.twistercambodia.karasbackend.analytic.dto;

import java.time.Instant;
import java.util.Date;

public class AnalyticDto {
    private String date;
    private int value;

    public AnalyticDto(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
