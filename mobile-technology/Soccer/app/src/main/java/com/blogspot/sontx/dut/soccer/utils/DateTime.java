package com.blogspot.sontx.dut.soccer.utils;

import java.util.Calendar;
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
        Calendar calendar = Calendar.getInstance();
        dt.setMinute(calendar.get(Calendar.MINUTE));
        dt.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        dt.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dt.setMonth(calendar.get(Calendar.MONTH) + 1);
        dt.setYear(calendar.get(Calendar.YEAR));
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
