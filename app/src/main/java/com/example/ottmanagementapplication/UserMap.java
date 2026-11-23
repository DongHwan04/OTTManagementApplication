package com.example.ottmanagementapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMap { // 더미 데이터입니다
    private static Map<Integer, User> users = new HashMap<>(); //고유아이디, 유저 형태로 맵에 저장

    static {
        User user1 = registerUser(1);
        user1.addWatchedMovie(MovieList.getMovieById(1), 5.0f);
        user1.addWatchedMovie(MovieList.getMovieById(2), 4.5f);

        User user2 = registerUser(2);
        user2.addWatchedMovie(MovieList.getMovieById(3), 2.0f);
        user2.addWatchedMovie(MovieList.getMovieById(4), 4.0f);
    }

    //고유 id를 받아 유저를 저장하기
    private static User registerUser(int userId) {
        User user = new User(userId);
        users.put(userId, user);
        return user;
    }

    //Id에 맞춰 유저 객체를 불러오기
    public static User getUserById(int userId) {
        return users.get(userId);
    }

    //전체 유저 목록 불러오기
    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}