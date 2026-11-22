package com.example.ottmanagementapplication;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

//UserList구현, user를 받았을 때
public class MovieRecommender {
    Map <String, Float> ratings = new HashMap<>(); //유저별로 장르와 총평점
    Map<Movie, Float> watchedMovies;
    List<Movie> movies;
    public MovieRecommender(User user) {
        //this.watchedMovies = UserList.getWatchedMovies(user.getUserId());
        //this.movies = MovieList.getMovies();
        checkPreference();
    }

    public void checkPreference() { //좋아하는 장르 확인 (ratings를 완성하는 과정)
        Map<String, Float> genreScoreSum = new HashMap<>();
        Map<String, Integer> genreCount = new HashMap<>();
        ratings.clear();
        if (watchedMovies == null) {
            return;}
        for (Map.Entry<Movie, Float> watchedMovie : watchedMovies.entrySet()) {
            String genre = watchedMovie.getKey().getGenre();
            float rating = watchedMovie.getValue();
            if (genreScoreSum.containsKey(genre)) { //genreScoreSum에 장르가 있으면
                float currentScore = genreScoreSum.get(genre); //currentScore에 현재 평점 저장
                genreScoreSum.put(genre, currentScore + rating); //나의 평점도 저장해 genreScoreSum에 추가
            }
            else { //genreScoreSum에 장르가 없으면
                genreScoreSum.put(genre, rating); //새로운 장르 총평점에 내 평점 넣기
            }
            if (genreCount.containsKey(genre)) {  //genreCount에 장르가 있으면
                int currentCount = genreCount.get(genre); //currentCount에 현재 장르당 영화수 저장
                genreCount.put(genre, currentCount + 1);
            } else { //genreCount에 장르가 없으면
                genreCount.put(genre, 1);
            }
        }
        for (String genre : genreScoreSum.keySet()) {
            float sum = genreScoreSum.get(genre);
            int count = genreCount.get(genre);
            ratings.put(genre, sum / count);
        }
    }

    public List<String> getTopGenres() { //상위 3개의 선호 장르 추출
        if (watchedMovies == null) {
            return new ArrayList<>();
        }
        return ratings.entrySet().stream()
                .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    public List<Movie> recommend() { //추출한 3개의 장르 바탕으로 영화 추천
        List<Movie> recommendedMovies = new ArrayList<>();
        if (movies == null) {
            return recommendedMovies;
        }
        if (watchedMovies == null || watchedMovies.isEmpty()) {
            int limit = Math.min(5, movies.size());
            for (int i = 0; i < limit; i++) {
                recommendedMovies.add(movies.get(i));
            }
            return recommendedMovies;
        }
        List<String> topGenres = getTopGenres();
        for (Movie movie : movies) {
            if (topGenres.contains(movie.getGenre()) && !watchedMovies.containsKey(movie)) {
                recommendedMovies.add(movie);
                if (recommendedMovies.size() >= 5) { // 5개 제한
                    break;
                    }
            }
        }
        if (recommendedMovies.size() < 5) { //선호 장르만으로 추천 영화가 5개가 안채워졌을 때
            for (Movie movie : movies) {
                if (!watchedMovies.containsKey(movie) && !recommendedMovies.contains(movie)) {
                    recommendedMovies.add(movie);
                    if (recommendedMovies.size() >= 5) {
                        break;
                    }
                }
            }
        }
        return recommendedMovies;
    }
}
