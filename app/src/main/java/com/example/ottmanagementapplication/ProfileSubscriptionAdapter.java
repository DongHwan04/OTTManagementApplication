package com.example.ottmanagementapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileSubscriptionAdapter extends RecyclerView.Adapter<ProfileSubscriptionAdapter.SubscriptionViewHolder> {

    private final List<ProfileSubscriptionItem> items = new ArrayList<>();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
    private int maxPriceKrw = 1;

    public void submitList(List<ProfileSubscriptionItem> newItems) {
        items.clear();
        maxPriceKrw = 1;
        if (newItems != null) {
            items.addAll(newItems);
            for (ProfileSubscriptionItem item : newItems) {
                if (item.getPriceKrw() > maxPriceKrw) {
                    maxPriceKrw = item.getPriceKrw();
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        ProfileSubscriptionItem item = items.get(position);
        Context context = holder.itemView.getContext();

        holder.ivIcon.setImageResource(item.getIconResId());
        holder.tvName.setText(item.getServiceName());
        holder.tvPrice.setText(numberFormat.format(item.getPriceKrw()));

        // 서비스별 고유 그라데이션 ProgressDrawable 적용
        holder.progressBar.setProgressDrawable(
                createGradientProgressDrawable(context, item.getGradientColorResIds())
        );

        int ratioPercent = Math.max(12, Math.round((item.getPriceKrw() * 100f) / maxPriceKrw));
        // ViewHolder 재활용 시 ClipDrawable 레벨이 0으로 남지 않도록 초기화 후 세팅
        holder.progressBar.setProgress(0);
        holder.progressBar.setProgress(ratioPercent);
    }

    private LayerDrawable createGradientProgressDrawable(Context context, @ColorRes int[] colorResIds) {
        float radiusPx = 6f * context.getResources().getDisplayMetrics().density;

        // 1. 회색 배경 트랙
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setColor(Color.parseColor("#E5E8EB"));
        backgroundDrawable.setCornerRadius(radiusPx);

        // 2. colors.xml 리소스 ID를 실제 색상값 배열로 변환
        int[] resolvedColors;
        if (colorResIds == null || colorResIds.length == 0) {
            resolvedColors = new int[]{Color.parseColor("#FF5A5F"), Color.parseColor("#FF8A8E")};
        } else if (colorResIds.length == 1) {
            int singleColor = ContextCompat.getColor(context, colorResIds[0]);
            resolvedColors = new int[]{singleColor, singleColor};
        } else {
            resolvedColors = new int[colorResIds.length];
            for (int i = 0; i < colorResIds.length; i++) {
                resolvedColors[i] = ContextCompat.getColor(context, colorResIds[i]);
            }
        }

        // 3. 좌 -> 우 방향 그라데이션 생성 및 ClipDrawable 래핑
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                resolvedColors
        );
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(radiusPx);

        ClipDrawable progressClip = new ClipDrawable(
                gradientDrawable,
                Gravity.START,
                ClipDrawable.HORIZONTAL
        );

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                backgroundDrawable,
                progressClip
        });
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);

        return layerDrawable;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivIcon;
        final ProgressBar progressBar;
        final TextView tvName;
        final TextView tvPrice;

        SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_profile_sub_icon);
            progressBar = itemView.findViewById(R.id.progress_profile_sub_ratio);
            tvName = itemView.findViewById(R.id.tv_profile_sub_name);
            tvPrice = itemView.findViewById(R.id.tv_profile_sub_price);
        }
    }
}