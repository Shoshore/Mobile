package com.example.project_android;

public class PhotoModel {
    private String title;
    private String author;
    private String location;
    private String date;
    private int imageResId;
    private int likes;
    private boolean likedByUser;

    public PhotoModel(String title, String author, String location, String date, int imageResId, int likes) {
        this.title = title;
        this.author = author;
        this.location = location;
        this.date = date;
        this.imageResId = imageResId;
        this.likes = likes;
        this.likedByUser = false;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public int getImageResId() { return imageResId; }
    public int getLikes() { return likes; }
    public boolean isLikedByUser() { return likedByUser; }

    public void toggleLike() {
        if (likedByUser) { likes--; } else { likes++; }
        likedByUser = !likedByUser;
    }
}