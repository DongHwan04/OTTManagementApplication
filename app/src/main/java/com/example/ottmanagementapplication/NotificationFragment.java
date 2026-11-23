package com.example.ottmanagementapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private final List<NotificationItem> allNotifications = new ArrayList<>();

    private NotificationAdapter adapter;
    private TextView tvCountSummary;
    private TextView tvEmpty;
    private View viewToolbarBellDot;

    @Nullable
    private String currentCategory = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnBack = view.findViewById(R.id.btn_notification_back);
        tvCountSummary = view.findViewById(R.id.tv_notification_count_summary);
        tvEmpty = view.findViewById(R.id.tv_notification_empty);
        viewToolbarBellDot = view.findViewById(R.id.view_toolbar_bell_dot);
        RecyclerView rvNotifications = view.findViewById(R.id.rv_notifications);

        btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        adapter = new NotificationAdapter((item, position) -> {
            if (!item.isRead()) {
                item.setRead(true);
                adapter.notifyItemChanged(position);
                updateSummaryHeader();
            }
        });
        rvNotifications.setAdapter(adapter);

        initDummyNotifications();

        View filterBar = view.findViewById(R.id.include_category_filter);
        new CategoryFilterHelper(filterBar, this::applyFilter);
        applyFilter(null);
    }

    private void initDummyNotifications() {
        allNotifications.clear();
        allNotifications.add(new NotificationItem(
                "1", "영상", "10.12", "MON", "유튜브 프리미엄",
                R.drawable.ic_youtube, "3일 후, 결제일입니다.", false
        ));
        allNotifications.add(new NotificationItem(
                "2", "영상", "10.12", "MON", "디즈니 플러스",
                R.drawable.ic_disneyplus, "소비 효율이 저번 달 대비 30% 감소했습니다.", false
        ));
        allNotifications.add(new NotificationItem(
                "3", "영상", "10.10", "SAT", "왓챠",
                R.drawable.ic_watcha, "이번 달 목표 효율(2,500 KRW / h)을 달성했습니다.", false
        ));
        allNotifications.add(new NotificationItem(
                "4", "영상", "10.08", "THU", "쿠팡플레이",
                R.drawable.ic_coupangplay, "와우 멤버십 정기 결제가 완료되었습니다.", true
        ));
        allNotifications.add(new NotificationItem(
                "5", "음악", "10.05", "MON", "유튜브 뮤직",
                R.drawable.ic_youtube, "지난주 음악 감상 시간이 4시간 증가했습니다.", true
        ));
        allNotifications.add(new NotificationItem(
                "6", "영상", "10.03", "SAT", "라프텔",
                R.drawable.ic_laftel, "신작 애니메이션 업데이트 알림이 도착했습니다.", true
        ));
        allNotifications.add(new NotificationItem(
                "7", "영상", "10.01", "THU", "디즈니 플러스",
                R.drawable.ic_disneyplus, "10월 구독 리포트가 생성되었습니다.", true
        ));
    }

    private void applyFilter(@Nullable String category) {
        currentCategory = category;

        List<NotificationItem> filtered = new ArrayList<>();
        for (NotificationItem item : allNotifications) {
            if (category == null || item.getCategory().equals(category)) {
                filtered.add(item);
            }
        }

        adapter.submitList(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        updateSummaryHeader();
    }

    private void updateSummaryHeader() {
        int totalCount = 0;
        int unreadCount = 0;

        for (NotificationItem item : allNotifications) {
            if (currentCategory == null || item.getCategory().equals(currentCategory)) {
                totalCount++;
                if (!item.isRead()) {
                    unreadCount++;
                }
            }
        }

        String prefix = "총 " + totalCount + "건 \\ ";
        String unreadText = unreadCount + "건";
        SpannableString spannable = new SpannableString(prefix + unreadText);
        spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FF5A5F")),
                prefix.length(),
                spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvCountSummary.setText(spannable);

        boolean hasAnyUnread = false;
        for (NotificationItem item : allNotifications) {
            if (!item.isRead()) {
                hasAnyUnread = true;
                break;
            }
        }
        viewToolbarBellDot.setVisibility(hasAnyUnread ? View.VISIBLE : View.GONE);
    }
}