package com.example.ottmanagementapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {

    private final List<CommunityPost> allPosts = new ArrayList<>();
    private final Map<String, TextView> typeChipMap = new LinkedHashMap<>();

    private CommunityAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    private String selectedServiceCategory = null;
    private String selectedPostType = "전체";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnCalendar = view.findViewById(R.id.btn_community_calendar);
        FrameLayout btnNotification = view.findViewById(R.id.btn_community_notification);
        tvEmpty = view.findViewById(R.id.tv_community_empty);
        RecyclerView rvPosts = view.findViewById(R.id.rv_community_posts);

        btnCalendar.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_to_calendar)
        );
        btnNotification.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_community_to_notification)
        );

        adapter = new CommunityAdapter((post, position) ->
                Toast.makeText(requireContext(),
                        "[" + post.getPostType() + "] " + post.getServiceName() + " 게시글 선택",
                        Toast.LENGTH_SHORT).show()
        );
        rvPosts.setAdapter(adapter);

        initDummyPosts();

        View filterBar = view.findViewById(R.id.include_category_filter);
        new CategoryFilterHelper(filterBar, category -> {
            selectedServiceCategory = category;
            applyCombinedFilter();
        });

        setupPostTypeChips(view);
        applyCombinedFilter();
    }

    private void initDummyPosts() {
        allPosts.clear();
        allPosts.add(new CommunityPost("1", "영상", "추천", "유튜브 프리미엄", R.drawable.ic_youtube,
                "백그라운드 재생이랑 뮤직 묶어서 쓰면 체감 효율 최고입니다.", "#1024"));
        allPosts.add(new CommunityPost("2", "영상", "후기", "디즈니 플러스", R.drawable.ic_disneyplus,
                "이번 달 오리지널 시리즈 정주행 완료! 화질 만족스럽네요.", "#2048"));
        allPosts.add(new CommunityPost("3", "영상", "팁", "왓챠", R.drawable.ic_watcha,
                "월간 이용 시간 15시간 넘기면 시간당 500원대로 떨어집니다.", "#3091"));
        allPosts.add(new CommunityPost("4", "영상", "추천", "쿠팡플레이", R.drawable.ic_coupangplay,
                "와우 멤버십으로 스포츠 중계까지 같이 보니까 가성비 좋아요.", "#4412"));
        allPosts.add(new CommunityPost("5", "영상", "후기", "라프텔", R.drawable.ic_laftel,
                "신작 동시방영 속도 빨라서 이번 분기도 연장했습니다.", "#5820"));
        allPosts.add(new CommunityPost("6", "음악", "팁", "유튜브 뮤직", R.drawable.ic_youtube,
                "출퇴근길 오프라인 저장 기능 켜두면 데이터 절약됩니다.", "#7731"));
    }

    private void setupPostTypeChips(View root) {
        typeChipMap.clear();
        typeChipMap.put("전체", root.findViewById(R.id.tv_type_all));
        typeChipMap.put("추천", root.findViewById(R.id.tv_type_recommend));
        typeChipMap.put("후기", root.findViewById(R.id.tv_type_review));
        typeChipMap.put("팁", root.findViewById(R.id.tv_type_tip));

        bindTypeChip(root.findViewById(R.id.chip_type_all), "전체");
        bindTypeChip(root.findViewById(R.id.chip_type_recommend), "추천");
        bindTypeChip(root.findViewById(R.id.chip_type_review), "후기");
        bindTypeChip(root.findViewById(R.id.chip_type_tip), "팁");
    }

    private void bindTypeChip(View container, String postType) {
        container.setOnClickListener(v -> {
            selectedPostType = postType;
            applyCombinedFilter();
        });
    }

    private void applyCombinedFilter() {
        for (Map.Entry<String, TextView> entry : typeChipMap.entrySet()) {
            boolean isSelected = entry.getKey().equals(selectedPostType);
            TextView tv = entry.getValue();
            tv.setSelected(isSelected);
            tv.setTextColor(isSelected ? Color.WHITE : Color.parseColor("#191F28"));
            tv.setTypeface(null, isSelected ? Typeface.BOLD : Typeface.NORMAL);
        }

        List<CommunityPost> filtered = new ArrayList<>();
        for (CommunityPost post : allPosts) {
            boolean matchCategory = (selectedServiceCategory == null)
                    || post.getServiceCategory().equals(selectedServiceCategory);
            boolean matchType = "전체".equals(selectedPostType)
                    || post.getPostType().equals(selectedPostType);

            if (matchCategory && matchType) {
                filtered.add(post);
            }
        }

        adapter.submitList(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}