package com.example.mymovieapp.data;

public class MovieReview {
    private final String author;
    private final String reviewContent;

    public MovieReview(String author, String reviewContent) {
        this.author = author;
        this.reviewContent = reviewContent;
    }

    String getAuthor() {
        return author;
    }

    String getReviewContent() {
        return reviewContent;
    }
}
