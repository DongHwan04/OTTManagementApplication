package com.example.ottmanagementapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottmanagementapplication.databinding.ItemVideoRecentBinding;

import java.util.List;

public class VideoRecentAdapter extends RecyclerView.Adapter<VideoRecentAdapter.ViewHolder> {
    private List<Movie> movieList;
    public VideoRecentAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder { //ViewHolder 클래스
        private ItemVideoRecentBinding recentBinding;
        public ViewHolder(@NonNull ItemVideoRecentBinding recentBinding) {
            super(recentBinding.getRoot());
            this.recentBinding = recentBinding;
        }
        private void bind(Movie movie) {
            recentBinding.ivRecentThumbnail.setImageResource((movie.getImageResId()));
            recentBinding.tvRecentTitle.setText(movie.getTitle());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoRecentBinding recentBinding = ItemVideoRecentBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(recentBinding);
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
