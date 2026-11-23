package com.example.ottmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem item, int position);
    }

    private final List<NotificationItem> items = new ArrayList<>();
    private final OnNotificationClickListener listener;

    public NotificationAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<NotificationItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem item = items.get(position);
        holder.tvDate.setText(item.getDateText());
        holder.tvDay.setText(item.getDayOfWeek());
        holder.tvServiceName.setText(item.getServiceName());
        holder.tvMessage.setText(item.getMessage());
        holder.ivServiceIcon.setImageResource(item.getIconResId());

        holder.viewUnreadDot.setVisibility(item.isRead() ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION && listener != null) {
                listener.onNotificationClick(items.get(adapterPos), adapterPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDate;
        final TextView tvDay;
        final View viewUnreadDot;
        final ImageView ivServiceIcon;
        final TextView tvServiceName;
        final TextView tvMessage;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_noti_date);
            tvDay = itemView.findViewById(R.id.tv_noti_day);
            viewUnreadDot = itemView.findViewById(R.id.view_noti_unread_dot);
            ivServiceIcon = itemView.findViewById(R.id.iv_noti_service_icon);
            tvServiceName = itemView.findViewById(R.id.tv_noti_service_name);
            tvMessage = itemView.findViewById(R.id.tv_noti_message);
        }

    }
}