package com.example.ottmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ottmanagementapplication.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class SubscriptionIconAdapter extends RecyclerView.Adapter<SubscriptionIconAdapter.IconViewHolder> {

    public interface OnIconClickListener {
        void onIconClick(int position);
    }

    private final List<Integer> iconResList;
    private final OnIconClickListener listener;
    private int selectedPosition = -1;
    public SubscriptionIconAdapter(List<Integer> iconResList, OnIconClickListener listener) {
        this.iconResList = iconResList;
        this.listener = listener;
    }
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_states_icons_top, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {

        holder.ivLogo.setImageResource(iconResList.get(position));

        boolean showMask = (selectedPosition != -1 && position != selectedPosition);
        holder.viewGrayMask.setVisibility(showMask ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIconClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconResList.size();
    }


    static class IconViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        View viewGrayMask;

        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            // layout_states_icons_top.xml 내부의 ShapeableImageView ID와 일치시켜야 함
            ivLogo = itemView.findViewById(R.id.iv_subscription_logo);
            viewGrayMask = itemView.findViewById(R.id.view_gray_mask);
        }
    }
}
