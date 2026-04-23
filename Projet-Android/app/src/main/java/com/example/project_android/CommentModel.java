package com.example.project_android;

public class CommentModel {
    private String author;
    private String text;
    private String date;

    public CommentModel(String author, String text, String date) {
        this.author = author;
        this.text   = text;
        this.date   = date;
    }

    public String getAuthor() { return author; }
    public String getText()   { return text; }
    public String getDate()   { return date; }
}