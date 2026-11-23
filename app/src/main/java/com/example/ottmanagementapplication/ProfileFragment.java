package com.example.ottmanagementapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private final List<ProfileSubscriptionItem> allSubscriptions = new ArrayList<>();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

    private ProfileSubscriptionAdapter adapter;
    private LinearLayout llOverlappingIcons;
    private TextView tvUserName;
    private TextView tvTotalAmountValue;
    private TextView tvTotalEfficiencyValue;
    private TextView tvTargetValue;
    private TextView tvBarCurrentEfficiency;
    private TextView tvBarTargetEfficiency;
    private ProgressBar progressCurrentEfficiency;
    private ProgressBar progressTargetEfficiency;

    @Nullable
    private String selectedCategory = null;
    private int targetEfficiencyKrw = 2500;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnCalendar = view.findViewById(R.id.btn_profile_calendar);
        FrameLayout btnNotification = view.findViewById(R.id.btn_profile_notification);
        ImageButton btnEditProfile = view.findViewById(R.id.btn_profile_edit);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_profile_add_subscription);

        llOverlappingIcons = view.findViewById(R.id.ll_profile_overlapping_icons);
        tvUserName = view.findViewById(R.id.tv_profile_user_name);
        tvTotalAmountValue = view.findViewById(R.id.tv_profile_total_amount_value);
        tvTotalEfficiencyValue = view.findViewById(R.id.tv_profile_total_efficiency_value);
        tvTargetValue = view.findViewById(R.id.tv_profile_target_value);
        tvBarCurrentEfficiency = view.findViewById(R.id.tv_bar_current_efficiency);
        tvBarTargetEfficiency = view.findViewById(R.id.tv_bar_target_efficiency);
        progressCurrentEfficiency = view.findViewById(R.id.progress_current_efficiency);
        progressTargetEfficiency = view.findViewById(R.id.progress_target_efficiency);

        // KRW / KRW / h 단위 텍스트 그라데이션 적용
        applyTextGradient(view.findViewById(R.id.tv_profile_unit_krw), "#D9779B", "#8E7CC3");
        applyTextGradient(view.findViewById(R.id.tv_profile_unit_krw_per_h), "#48B84E", "#4DD0B1");
        applyTextGradient(view.findViewById(R.id.tv_profile_target_unit), "#48B84E", "#4DD0B1");

        btnCalendar.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_to_calendar)
        );
        btnNotification.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_notification)
        );
        fabAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_subscription_add)
        );

        btnEditProfile.setOnClickListener(v -> showEditUserNameDialog());
        view.findViewById(R.id.layout_profile_target_efficiency)
                .setOnClickListener(v -> showEditTargetEfficiencyDialog());

        RecyclerView rvSubscriptions = view.findViewById(R.id.rv_profile_subscriptions);
        adapter = new ProfileSubscriptionAdapter();
        rvSubscriptions.setAdapter(adapter);

        initDummySubscriptions();

        View filterBar = view.findViewById(R.id.include_category_filter);
        new CategoryFilterHelper(filterBar, category -> {
            selectedCategory = category;
            refreshProfileStats();
        });

        refreshProfileStats();
    }

    private void initDummySubscriptions() {
        allSubscriptions.clear();
        // 아래 R.color.* 이름은 colors.xml에 정의해 두신 리소스 ID로 교체하세요.
        allSubscriptions.add(new ProfileSubscriptionItem(
                "유튜브 프리미엄", "영상", R.drawable.ic_youtube, 14900, 4200,
                R.color.youtube_gradient_start, R.color.youtube_gradient_end
        ));
        allSubscriptions.add(new ProfileSubscriptionItem(
                "디즈니 플러스", "영상", R.drawable.ic_disneyplus, 7900, 3800,
                R.color.disney_gradient_start, R.color.disney_gradient_end
        ));
        allSubscriptions.add(new ProfileSubscriptionItem(
                "유튜브 뮤직", "음악", R.drawable.ic_youtube, 5900, 3200,
                R.color.youtube_music_gradient_start, R.color.youtube_music_gradient_end
        ));
        allSubscriptions.add(new ProfileSubscriptionItem(
                "왓챠", "영상", R.drawable.ic_watcha, 7900, 4800,
                R.color.watcha_gradient_start, R.color.watcha_gradient_end
        ));
    }

    private void refreshProfileStats() {
        List<ProfileSubscriptionItem> filtered = new ArrayList<>();
        int totalAmount = 0;
        int efficiencySum = 0;

        for (ProfileSubscriptionItem item : allSubscriptions) {
            if (selectedCategory == null || item.getCategory().equals(selectedCategory)) {
                filtered.add(item);
                totalAmount += item.getPriceKrw();
                efficiencySum += item.getEfficiencyKrwPerHour();
            }
        }

        int avgEfficiency = filtered.isEmpty() ? 0 : (efficiencySum / filtered.size());

        tvTotalAmountValue.setText(numberFormat.format(totalAmount));
        tvTotalEfficiencyValue.setText(numberFormat.format(avgEfficiency));
        tvTargetValue.setText(numberFormat.format(targetEfficiencyKrw));

        tvBarCurrentEfficiency.setText(numberFormat.format(avgEfficiency));
        tvBarTargetEfficiency.setText(numberFormat.format(targetEfficiencyKrw));

        int maxVal = Math.max(Math.max(avgEfficiency, targetEfficiencyKrw), 1);
        progressCurrentEfficiency.setProgress(Math.round((avgEfficiency * 100f) / maxVal));
        progressTargetEfficiency.setProgress(Math.round((targetEfficiencyKrw * 100f) / maxVal));

        renderOverlappingIcons(filtered);
        adapter.submitList(filtered);
    }

    private void renderOverlappingIcons(List<ProfileSubscriptionItem> items) {
        llOverlappingIcons.removeAllViews();
        int sizePx = dpToPx(24);
        int overlapMarginPx = dpToPx(-8);
        int strokePx = dpToPx(1);
        int radiusPx = dpToPx(12);

        for (int i = 0; i < items.size(); i++) {
            ProfileSubscriptionItem item = items.get(i);
            MaterialCardView card = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizePx, sizePx);
            if (i < items.size() - 1) {
                params.setMarginEnd(overlapMarginPx);
            }
            card.setLayoutParams(params);
            card.setRadius(radiusPx);
            card.setCardElevation(0f);
            card.setStrokeWidth(strokePx);
            card.setStrokeColor(Color.parseColor("#E5E8EB"));

            ImageView iv = new ImageView(requireContext());
            iv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(item.getIconResId());

            card.addView(iv);
            llOverlappingIcons.addView(card);
        }
    }

    private void showEditUserNameDialog() {
        EditText input = new EditText(requireContext());
        input.setText(tvUserName.getText());
        input.setSelection(input.getText().length());

        new AlertDialog.Builder(requireContext())
                .setTitle("닉네임 변경")
                .setView(input)
                .setPositiveButton("저장", (dialog, which) -> {
                    String updated = input.getText().toString().trim();
                    if (!updated.isEmpty()) {
                        tvUserName.setText(updated);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void showEditTargetEfficiencyDialog() {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(targetEfficiencyKrw));
        input.setSelection(input.getText().length());

        new AlertDialog.Builder(requireContext())
                .setTitle("목표 효율 설정 (KRW / h)")
                .setView(input)
                .setPositiveButton("적용", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        try {
                            targetEfficiencyKrw = Integer.parseInt(text);
                            refreshProfileStats();
                        } catch (NumberFormatException ignored) {
                        }
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void applyTextGradient(TextView textView, String startColorHex, String endColorHex) {
        textView.post(() -> {
            float width = textView.getPaint().measureText(textView.getText().toString());
            if (width <= 0) width = 100f;
            Shader shader = new LinearGradient(
                    0f, 0f, width, 0f,
                    new int[]{Color.parseColor(startColorHex), Color.parseColor(endColorHex)},
                    null,
                    Shader.TileMode.CLAMP
            );
            textView.getPaint().setShader(shader);
            textView.invalidate();
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}