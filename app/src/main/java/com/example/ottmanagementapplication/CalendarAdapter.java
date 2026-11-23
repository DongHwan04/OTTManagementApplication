package com.example.ottmanagementapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClick(CalendarDayItem dayItem);
    }

    private final List<CalendarDayItem> dayItems = new ArrayList<>();
    private final OnDayClickListener listener;

    public CalendarAdapter(OnDayClickListener listener) {
        this.listener = listener;
    }

    public void submitDays(List<CalendarDayItem> newDays) {
        dayItems.clear();
        if (newDays != null) {
            dayItems.addAll(newDays);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        CalendarDayItem item = dayItems.get(position);
        holder.tvDayNumber.setText(String.valueOf(item.getDayOfMonth()));

        // 요일별·현재월 여부 날짜 색상 지정 (일요일 빨강, 토요일 파랑, 평일 검정, 이전/다음달 연회색)
        if (!item.isCurrentMonth()) {
            if (item.getColumnIndex() == 0) {
                holder.tvDayNumber.setTextColor(Color.parseColor("#F5B7B1"));
            } else if (item.getColumnIndex() == 6) {
                holder.tvDayNumber.setTextColor(Color.parseColor("#AED6F1"));
            } else {
                holder.tvDayNumber.setTextColor(Color.parseColor("#D1D6DB"));
            }
        } else if (item.getColumnIndex() == 0) {
            holder.tvDayNumber.setTextColor(Color.parseColor("#FF5A5F"));
        } else if (item.getColumnIndex() == 6) {
            holder.tvDayNumber.setTextColor(Color.parseColor("#3182F6"));
        } else {
            holder.tvDayNumber.setTextColor(Color.parseColor("#191F28"));
        }

        // 우측 끝 열(토요일)은 우측 격자선 숨김
        holder.dividerRight.setVisibility(item.getColumnIndex() == 6 ? View.INVISIBLE : View.VISIBLE);

        // 결제 일정 배지 최대 2개 표시 (시안의 '● 넷플...', '● 유튜...', '● 스포...' 형태)
        List<CalendarEvent> events = item.getEvents();
        if (item.isCurrentMonth() && !events.isEmpty()) {
            CalendarEvent first = events.get(0);
            holder.layoutEvent1.setVisibility(View.VISIBLE);
            holder.tvEventName1.setText(first.getServiceName());
            ViewCompat.setBackgroundTintList(holder.viewDot1, ColorStateList.valueOf(first.getDotColor()));
        } else {
            holder.layoutEvent1.setVisibility(View.GONE);
        }

        if (item.isCurrentMonth() && events.size() >= 2) {
            CalendarEvent second = events.get(1);
            holder.layoutEvent2.setVisibility(View.VISIBLE);
            holder.tvEventName2.setText(second.getServiceName());
            ViewCompat.setBackgroundTintList(holder.viewDot2, ColorStateList.valueOf(second.getDotColor()));
        } else {
            holder.layoutEvent2.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION && listener != null) {
                listener.onDayClick(dayItems.get(adapterPos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayItems.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDayNumber;
        final LinearLayout layoutEvent1;
        final View viewDot1;
        final TextView tvEventName1;
        final LinearLayout layoutEvent2;
        final View viewDot2;
        final TextView tvEventName2;
        final View dividerRight;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tv_calendar_day_number);
            layoutEvent1 = itemView.findViewById(R.id.layout_cal_event_1);
            viewDot1 = itemView.findViewById(R.id.view_cal_event_dot_1);
            tvEventName1 = itemView.findViewById(R.id.tv_cal_event_name_1);
            layoutEvent2 = itemView.findViewById(R.id.layout_cal_event_2);
            viewDot2 = itemView.findViewById(R.id.view_cal_event_dot_2);
            tvEventName2 = itemView.findViewById(R.id.tv_cal_event_name_2);
            dividerRight = itemView.findViewById(R.id.divider_cell_right);
        }
    }
}