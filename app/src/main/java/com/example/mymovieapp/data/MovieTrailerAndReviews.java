package com.example.mymovieapp.data;

import java.util.ArrayList;

public class MovieTrailerAndReviews {
    private final ArrayList<MovieTrailer> mMovieTrailers;
    private final ArrayList<MovieReview> mMovieReviews;

    public MovieTrailerAndReviews(ArrayList<MovieTrailer> mMovieTrailers, ArrayList<MovieReview> mMovieReviews) {
        this.mMovieTrailers = mMovieTrailers;
        this.mMovieReviews = mMovieReviews;
    }

    public ArrayList<MovieTrailer> getmMovieTrailers() {
        return mMovieTrailers;
    }


    public ArrayList<MovieReview> getmMovieReviews() {
        return mMovieReviews;
    }

}
