package com.example.ottmanagementapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private final List<CalendarEvent> allEvents = new ArrayList<>();

    private CalendarAdapter adapter;
    private TextView tvYearMonth;

    private int currentYear = 2026;
    private int currentMonth = 10;

    @Nullable
    private String selectedCategory = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnBack = view.findViewById(R.id.btn_calendar_back);
        FrameLayout btnNotification = view.findViewById(R.id.btn_calendar_notification);
        LinearLayout layoutMonthSelector = view.findViewById(R.id.layout_calendar_month_selector);
        tvYearMonth = view.findViewById(R.id.tv_calendar_year_month);
        RecyclerView rvCalendarGrid = view.findViewById(R.id.rv_calendar_grid);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_calendar_add_subscription);

        btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        btnNotification.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_calendar_to_notification)
        );

        fabAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_calendar_to_subscription_add)
        );

        layoutMonthSelector.setOnClickListener(v -> showMonthPickerDialog());

        rvCalendarGrid.setLayoutManager(new GridLayoutManager(requireContext(), 7));
        adapter = new CalendarAdapter(this::showDayDetailDialog);
        rvCalendarGrid.setAdapter(adapter);

        initDummyBillingEvents();

        View filterBar = view.findViewById(R.id.include_category_filter);
        new CategoryFilterHelper(filterBar, category -> {
            selectedCategory = category;
            refreshCalendar();
        });

        refreshCalendar();
    }

    private void initDummyBillingEvents() {
        allEvents.clear();
        allEvents.add(new CalendarEvent("넷플릭스", "영상", 12, 14900, Color.parseColor("#FF5A5F")));
        allEvents.add(new CalendarEvent("유튜브 프리미엄", "영상", 12, 14900, Color.parseColor("#FF5A5F")));
        allEvents.add(new CalendarEvent("스포티파이", "음악", 25, 10900, Color.parseColor("#34C759")));
        allEvents.add(new CalendarEvent("쿠팡플레이", "영상", 8, 7890, Color.parseColor("#3182F6")));
    }

    private void refreshCalendar() {
        tvYearMonth.setText(String.format(Locale.KOREA, "%d.%02d", currentYear, currentMonth));
        adapter.submitDays(buildMonthCells(currentYear, currentMonth));
    }

    private List<CalendarDayItem> buildMonthCells(int year, int month) {
        List<CalendarDayItem> cells = new ArrayList<>(42);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int leadingDays = firstDayOfWeek - 1;
        int daysInCurrentMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar prevCal = (Calendar) cal.clone();
        prevCal.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < leadingDays; i++) {
            int day = daysInPrevMonth - leadingDays + 1 + i;
            cells.add(new CalendarDayItem(
                    prevCal.get(Calendar.YEAR),
                    prevCal.get(Calendar.MONTH) + 1,
                    day,
                    false,
                    i,
                    null
            ));
        }

        for (int day = 1; day <= daysInCurrentMonth; day++) {
            int colIdx = (leadingDays + day - 1) % 7;
            List<CalendarEvent> dayEvents = new ArrayList<>();
            for (CalendarEvent event : allEvents) {
                boolean matchCategory = (selectedCategory == null)
                        || event.getCategory().equals(selectedCategory);
                if (matchCategory && event.getBillingDay() == day) {
                    dayEvents.add(event);
                }
            }
            cells.add(new CalendarDayItem(year, month, day, true, colIdx, dayEvents));
        }

        Calendar nextCal = (Calendar) cal.clone();
        nextCal.add(Calendar.MONTH, 1);
        int nextDay = 1;
        while (cells.size() < 42) {
            int colIdx = cells.size() % 7;
            cells.add(new CalendarDayItem(
                    nextCal.get(Calendar.YEAR),
                    nextCal.get(Calendar.MONTH) + 1,
                    nextDay++,
                    false,
                    colIdx,
                    null
            ));
        }

        return cells;
    }

    private void showMonthPickerDialog() {
        final String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format(Locale.KOREA, "%d.%02d", currentYear, i + 1);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("조회 월 선택")
                .setItems(months, (dialog, which) -> {
                    currentMonth = which + 1;
                    refreshCalendar();
                })
                .show();
    }

    private void showDayDetailDialog(CalendarDayItem dayItem) {
        if (!dayItem.isCurrentMonth() || dayItem.getEvents().isEmpty()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        StringBuilder sb = new StringBuilder();
        for (CalendarEvent event : dayItem.getEvents()) {
            sb.append("• ")
                    .append(event.getServiceName())
                    .append(" (")
                    .append(event.getCategory())
                    .append(") : ")
                    .append(numberFormat.format(event.getPriceKrw()))
                    .append(" KRW\n");
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(dayItem.getMonth() + "월 " + dayItem.getDayOfMonth() + "일 정기 결제 예정")
                .setMessage(sb.toString().trim())
                .setPositiveButton("확인", null)
                .show();
    }
}