package com.example.ottmanagementapplication;

//영화 클래스
public class Movie {
    private int contentId;
    private String title;
    private int platformId;
    private String genre;
    private String description;
    private float popularity;
    private int imageResId;


    //생성자
    public Movie(int contentId,
                 String title,
                 int platformId,
                 String genre,
                 String description,
                 float popularity,
                 int imageResId) {
        this.contentId = contentId;
        this.title = title;
        this.platformId = platformId;
        this.genre = genre;
        this.description = description;
        this.popularity = popularity;
        this.imageResId = imageResId;
    }

    //getter & setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getImageResId() {
        return imageResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return contentId == movie.contentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(contentId);
    }

}
