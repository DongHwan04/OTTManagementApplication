package com.example.ottmanagementapplication;

import java.util.List;
import java.util.ArrayList;

public class MovieList {

    // 전체 영화 목록을 반환하는 메서드
    public static List<Movie> getMovies() { //더미 데이터입니다
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie(1, "인터스텔라", 1, "SF", "우주 영화", 4, R.drawable.ic_coupangplay));
        movies.add(new Movie(2, "어벤져스", 1, "액션", "아이언맨", 5, R.drawable.ic_coupangplay));
        movies.add(new Movie(3, "컨저링", 2, "공포", "무서움", 3, R.drawable.ic_coupangplay));
        movies.add(new Movie(4, "테넷", 2, "SF", "sf영화", 2, R.drawable.ic_coupangplay));
        movies.add(new Movie(5, "스타워즈", 1, "SF", "제국의 역습", 5, R.drawable.ic_coupangplay));
        movies.add(new Movie(6, "스파이더맨", 1, "액션", "히어로 액션", 4, R.drawable.ic_coupangplay));
        movies.add(new Movie(7, "애나벨", 2, "공포", "악령 인형", 3, R.drawable.ic_coupangplay));
        movies.add(new Movie(8, "매트릭스", 1, "SF", "가상 현실 액션", 5, R.drawable.ic_coupangplay));
        movies.add(new Movie(9, "다크 나이트", 1, "액션", "배트맨", 5, R.drawable.ic_coupangplay));

        return movies;
    }

    public static Movie getMovieById(int contentId) {
        List<Movie> movies = getMovies();
        if (movies == null) {
            return null;
        }
        for (Movie movie : movies) {
            if (movie.getContentId() == contentId) {
                return movie;
            }
        }
        return null;
    }
}