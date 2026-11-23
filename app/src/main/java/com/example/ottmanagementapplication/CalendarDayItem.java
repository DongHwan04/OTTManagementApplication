package com.example.ottmanagementapplication;

import java.util.ArrayList;
import java.util.List;

public class CalendarDayItem {
    private final int year;
    private final int month; // 1 ~ 12
    private final int dayOfMonth;
    private final boolean isCurrentMonth;
    private final int columnIndex; // 0(일요일) ~ 6(토요일)
    private final List<CalendarEvent> events;

    public CalendarDayItem(int year, int month, int dayOfMonth, boolean isCurrentMonth,
                           int columnIndex, List<CalendarEvent> events) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isCurrentMonth = isCurrentMonth;
        this.columnIndex = columnIndex;
        this.events = events != null ? events : new ArrayList<>();
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDayOfMonth() { return dayOfMonth; }
    public boolean isCurrentMonth() { return isCurrentMonth; }
    public int getColumnIndex() { return columnIndex; }
    public List<CalendarEvent> getEvents() { return events; }
}