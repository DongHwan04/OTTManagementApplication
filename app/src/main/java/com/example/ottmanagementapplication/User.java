package com.example.ottmanagementapplication;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int userId;
    private Map<Movie, Float> watchedMovies;

    public User(int userId) {
        this.userId = userId;
        this.watchedMovies = new HashMap<>();
    }

    public int getUserId() {
        return userId;
    }

    public Map<Movie, Float> getWatchedMovies() {
        return watchedMovies;
    }

    public void addWatchedMovie(Movie movie, float rating) {
        if (movie != null) {
            this.watchedMovies.put(movie, rating);
        }
    }
}