package com.example.ottmanagementapplication;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class ProfileSubscriptionItem {
    private final String serviceName;
    private final String category; // "영상", "음악"
    @DrawableRes
    private final int iconResId;
    private final int priceKrw;
    private final int efficiencyKrwPerHour;
    @ColorInt
    private final int[] gradientColorResIds;

    public ProfileSubscriptionItem(String serviceName, String category,
                                   @DrawableRes int iconResId, int priceKrw,
                                   int efficiencyKrwPerHour,
                                   @ColorRes int... gradientColorResIds) {
        this.serviceName = serviceName;
        this.category = category;
        this.iconResId = iconResId;
        this.priceKrw = priceKrw;
        this.efficiencyKrwPerHour = efficiencyKrwPerHour;
        this.gradientColorResIds = gradientColorResIds;
    }

    public String getServiceName() { return serviceName; }
    public String getCategory() { return category; }
    public int getIconResId() { return iconResId; }
    public int getPriceKrw() { return priceKrw; }
    public int getEfficiencyKrwPerHour() { return efficiencyKrwPerHour; }
    public int[] getGradientColorResIds() { return gradientColorResIds; }
}