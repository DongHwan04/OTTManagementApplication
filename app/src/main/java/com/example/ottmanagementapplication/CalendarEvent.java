package com.example.ottmanagementapplication;

import androidx.annotation.ColorInt;

public class CalendarEvent {
    private final String serviceName;
    private final String category; // "영상", "음악", "배달"
    private final int billingDay;
    private final int priceKrw;
    @ColorInt
    private final int dotColor;

    public CalendarEvent(String serviceName, String category, int billingDay,
                         int priceKrw, @ColorInt int dotColor) {
        this.serviceName = serviceName;
        this.category = category;
        this.billingDay = billingDay;
        this.priceKrw = priceKrw;
        this.dotColor = dotColor;
    }

    public String getServiceName() { return serviceName; }
    public String getCategory() { return category; }
    public int getBillingDay() { return billingDay; }
    public int getPriceKrw() { return priceKrw; }
    public int getDotColor() { return dotColor; }
}