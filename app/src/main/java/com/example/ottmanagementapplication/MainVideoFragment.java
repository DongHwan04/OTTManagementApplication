package com.example.ottmanagementapplication;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainVideoFragment extends Fragment {

    public MainVideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. 비디오 총 금액 / 총 효율 바인딩 및 단위 그라데이션 적용
        TextView tvTotalFeeUnit = view.findViewById(R.id.tv_stats_total_video_amount_KRW);
        TextView tvTotalEfficientUnit = view.findViewById(R.id.tv_stats_total_video_efficient_KRW_h);
        TextView tvTotalFeeValue = view.findViewById(R.id.tv_total_video_monthly_amount);
        TextView tvTotalEfficientValue = view.findViewById(R.id.tv_total_video_monthly_efficient);

        tvTotalFeeValue.setText("32,790");
        tvTotalEfficientValue.setText("1,260");

        applyTextGradient(tvTotalFeeUnit, R.color.krw_gradient_start, R.color.krw_gradient_end);
        applyTextGradient(tvTotalEfficientUnit, R.color.eff_gradient_start, R.color.eff_gradient_end);

        // 2. 이용 중인 비디오 서비스 아이콘 나열 (클릭/마스크 기능 없는 순수 표시용)
        LinearLayout llActiveIcons = view.findViewById(R.id.ll_active_video_icons);
        List<Integer> videoServiceIcons = Arrays.asList(
                R.drawable.ic_watcha,
                R.drawable.ic_coupangplay,
                R.drawable.ic_disneyplus,
                R.drawable.ic_laftel
        );

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int iconRes : videoServiceIcons) {
            View iconItem = inflater.inflate(R.layout.layout_video_icons_stats, llActiveIcons, false);
            ImageView ivLogo = iconItem.findViewById(R.id.iv_subscription_logo_video);
            ivLogo.setImageResource(iconRes);
            llActiveIcons.addView(iconItem);
        }

        //유저 아이디 입력
        int userId = 1;

        //1. recent adapter연결
        RecyclerView rvRecentVideos = view.findViewById(R.id.rv_recent_videos);
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        );
        rvRecentVideos.setLayoutManager(recentLayoutManager);
        List<Movie> recentMovies = MovieList.getMovies();
        VideoRecentAdapter videoRecentAdapter = new VideoRecentAdapter(recentMovies);
        rvRecentVideos.setAdapter(videoRecentAdapter);

        //2. recommend adapter연결
        RecyclerView rvRecommendVideos = view.findViewById(R.id.rv_recommended_videos);
        LinearLayoutManager recommendLayoutManager = new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        );
        rvRecommendVideos.setLayoutManager(recommendLayoutManager);
        List<Movie> recommendMovies = new MovieRecommender(userId).recommend();
        VideoRecommendAdapter videoRecommendAdapter = new VideoRecommendAdapter(recommendMovies);
        rvRecommendVideos.setAdapter(videoRecommendAdapter);

        //3. communityTop adapter연결
        RecyclerView rvCommunityTop = view.findViewById(R.id.rv_community_top);
        LinearLayoutManager communityTopLayoutManager = new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        );
        rvCommunityTop.setLayoutManager(communityTopLayoutManager);
        List<Movie> topMovies = MovieList.getMovies();
        VideoCommunityTopAdapter communityTopAdapter = new VideoCommunityTopAdapter(topMovies);
        rvCommunityTop.setAdapter(communityTopAdapter);
    }

    private void applyTextGradient(TextView textView, @ColorRes int startColorRes, @ColorRes int endColorRes) {
        TextPaint paint = textView.getPaint();
        float width = paint.measureText(textView.getText().toString());
        int startColor = ContextCompat.getColor(textView.getContext(), startColorRes);
        int endColor = ContextCompat.getColor(textView.getContext(), endColorRes);

        Shader shader = new LinearGradient(
                0f, 0f, width, 0f,
                new int[]{startColor, endColor},
                null,
                Shader.TileMode.CLAMP
        );
        paint.setShader(shader);
        textView.invalidate();
    }
}