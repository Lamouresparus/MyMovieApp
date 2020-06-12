package com.example.mymovieapp.data;

import java.io.Serializable;

public class Movie implements Serializable {
    private  final String mImageUrl;
    private final String mMovieTitle;
    private final String mMovieDescription;
    private final String mReleaseDate;
    private final double mRating;

    public Movie(String mImageUrl, String movieTitle, String movieDescription, String releaseDate, double rating) {
        this.mImageUrl = mImageUrl;
        this.mMovieTitle = movieTitle;
        this.mMovieDescription = movieDescription;
        this.mReleaseDate = releaseDate;
        this.mRating = rating;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieDescription() {
        return mMovieDescription;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public double getmRating() {
        return mRating;
    }

}
