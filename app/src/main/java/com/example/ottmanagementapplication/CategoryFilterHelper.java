package com.example.ottmanagementapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryFilterHelper {

    public static final String CATEGORY_VIDEO = "영상";
    public static final String CATEGORY_MUSIC = "음악";

    public interface OnCategoryFilterListener {
        // null이면 전체 보기, "영상" 또는 "음악"이면 해당 카테고리만 필터링
        void onCategoryChanged(@Nullable String selectedCategory);
    }

    private final View layoutVideo;
    private final View layoutMusic;
    private final OnCategoryFilterListener listener;

    @Nullable
    private String selectedCategory = null;

    public CategoryFilterHelper(@NonNull View filterRootView,
                                @NonNull OnCategoryFilterListener listener) {
        this.listener = listener;
        this.layoutVideo = filterRootView.findViewById(R.id.layout_transition_video);
        this.layoutMusic = filterRootView.findViewById(R.id.layout_transition_music);

        View btnVideo = filterRootView.findViewById(R.id.button_transition_video);
        View btnMusic = filterRootView.findViewById(R.id.button_transition_music);

        btnVideo.setOnClickListener(v -> toggleCategory(CATEGORY_VIDEO));
        btnMusic.setOnClickListener(v -> toggleCategory(CATEGORY_MUSIC));

        updateVisualState();
    }

    private void toggleCategory(String targetCategory) {
        if (targetCategory.equals(selectedCategory)) {
            selectedCategory = null; // 이미 선택된 버튼을 다시 누르면 해제(전체 보기)
        } else {
            selectedCategory = targetCategory;
        }
        updateVisualState();
        listener.onCategoryChanged(selectedCategory);
    }

    private void updateVisualState() {
        if (selectedCategory == null) {
            layoutVideo.setAlpha(1.0f);
            layoutMusic.setAlpha(1.0f);
        } else if (CATEGORY_VIDEO.equals(selectedCategory)) {
            layoutVideo.setAlpha(1.0f);
            layoutMusic.setAlpha(0.35f);
        } else {
            layoutVideo.setAlpha(0.35f);
            layoutMusic.setAlpha(1.0f);
        }
    }

    private void setButtonAppearance(View target, float alpha, float scale) {
        target.setAlpha(alpha);
        target.setScaleX(scale);
        target.setScaleY(scale);
    }

    @Nullable
    public String getSelectedCategory() {
        return selectedCategory;
    }
}