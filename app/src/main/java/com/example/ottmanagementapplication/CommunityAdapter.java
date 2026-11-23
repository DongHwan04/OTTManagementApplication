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

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.PostViewHolder> {

    public interface OnPostClickListener {
        void onPostClick(CommunityPost post, int position);
    }

    private final List<CommunityPost> posts = new ArrayList<>();
    private final OnPostClickListener listener;

    public CommunityAdapter(OnPostClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CommunityPost> newPosts) {
        posts.clear();
        if (newPosts != null) {
            posts.addAll(newPosts);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        CommunityPost post = posts.get(position);
        holder.tvServiceName.setText(post.getServiceName());
        holder.tvContent.setText(post.getContent());
        holder.tvTypeBadge.setText(post.getPostType());
        holder.tvUserTag.setText(post.getUserTag());
        holder.ivServiceIcon.setImageResource(post.getServiceIconResId());

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION && listener != null) {
                listener.onPostClick(posts.get(adapterPos), adapterPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivServiceIcon;
        final TextView tvServiceName;
        final TextView tvContent;
        final TextView tvTypeBadge;
        final TextView tvUserTag;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivServiceIcon = itemView.findViewById(R.id.iv_post_service_icon);
            tvServiceName = itemView.findViewById(R.id.tv_post_service_name);
            tvContent = itemView.findViewById(R.id.tv_post_content);
            tvTypeBadge = itemView.findViewById(R.id.tv_post_type_badge);
            tvUserTag = itemView.findViewById(R.id.tv_post_user_tag);
        }
    }
}