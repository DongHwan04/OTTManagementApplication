package com.example.ottmanagementapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ottmanagementapplication.databinding.ItemVideoRecommendBinding;
import com.example.ottmanagementapplication.databinding.ItemVideoRecommendBinding;

import java.util.List;

public class VideoRecommendAdapter extends RecyclerView.Adapter<VideoRecommendAdapter.ViewHolder> {
    private List<Movie> movieList;
    public VideoRecommendAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder { //ViewHolder 클래스
        private ItemVideoRecommendBinding recommendBinding;
        public ViewHolder(@NonNull ItemVideoRecommendBinding recommendBinding) {
            super(recommendBinding.getRoot());
            this.recommendBinding = recommendBinding;
        }
        private void bind(Movie movie) {
            recommendBinding.ivRecommendPoster.setImageResource((movie.getImageResId()));
            recommendBinding.tvRecommendTitle.setText(movie.getTitle());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoRecommendBinding recommendBinding = ItemVideoRecommendBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(recommendBinding);
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
