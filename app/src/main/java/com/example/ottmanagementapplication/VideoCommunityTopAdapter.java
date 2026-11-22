package com.example.ottmanagementapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottmanagementapplication.databinding.ItemVideoCommunityTopBinding;

import java.util.List;

public class VideoCommunityTopAdapter extends RecyclerView.Adapter<VideoCommunityTopAdapter.ViewHolder> {
    private List<Movie> movieList;
    public VideoCommunityTopAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder { //ViewHolder 클래스
        private ItemVideoCommunityTopBinding communityTopBinding;
        public ViewHolder(@NonNull ItemVideoCommunityTopBinding communityTopBinding) {
            super(communityTopBinding.getRoot());
            this.communityTopBinding = communityTopBinding;
        }
        private void bind(Movie movie) {
            communityTopBinding.ivRecommendPoster.setImageResource((movie.getImageResId()));
            communityTopBinding.tvRecommendTitle.setText(movie.getTitle());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoCommunityTopBinding communityTopBinding = ItemVideoCommunityTopBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(communityTopBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }
}
