package com.blogspot.sontx.dut.soccer.utils;

import java.util.Date;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/04/2016.
 */
public final class DateTime {
    private int hour;
    private int minute;
    private int day;
    private int month;
    private int year;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d - %02d/%02d/%d", hour, minute, day, month, year);
    }

    public static DateTime now() {
        DateTime dt = new DateTime();
        Date date = new Date();
        dt.setMinute(date.getMinutes());
        dt.setHour(date.getHours());
        dt.setDay(date.getDay());
        dt.setMonth(date.getMonth());
        dt.setYear(date.getYear());
        return dt;
    }

    public static Date parse(String st) {
        String[] parts = st.split("/");
        Date date = new Date(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        return date;
    }
    public static String getFriendlyString(Date date) {
        return String.format("%02d:%02d - %02d/%02d/%d",
                date.getHours(), date.getMinutes(), date.getDay(), date.getMonth(), date.getYear());
    }
    private DateTime() {}
}
