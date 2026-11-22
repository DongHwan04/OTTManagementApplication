package com.example.ottmanagementapplication;

import androidx.annotation.DrawableRes;

public class NotificationItem {
    private final String id;
    private final String category; // "영상", "음악", "배달" 등
    private final String dateText; // "10.12"
    private final String dayOfWeek; // "MON"
    private final String serviceName;
    @DrawableRes
    private final int iconResId;
    private final String message;
    private boolean isRead;

    public NotificationItem(String id, String category, String dateText, String dayOfWeek,
                            String serviceName, @DrawableRes int iconResId,
                            String message, boolean isRead) {
        this.id = id;
        this.category = category;
        this.dateText = dateText;
        this.dayOfWeek = dayOfWeek;
        this.serviceName = serviceName;
        this.iconResId = iconResId;
        this.message = message;
        this.isRead = isRead;
    }

    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getDateText() { return dateText; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getServiceName() { return serviceName; }
    public int getIconResId() { return iconResId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}