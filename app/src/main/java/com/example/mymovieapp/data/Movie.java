package com.example.mymovieapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mMovieTitle);
        dest.writeString(this.mMovieDescription);
        dest.writeString(this.mReleaseDate);
        dest.writeDouble(this.mRating);
    }

    private Movie(Parcel in){
        this.mImageUrl = in.readString();
        this.mMovieTitle = in.readString();
        this.mMovieDescription = in.readString();
        this.mReleaseDate = in.readString();
        this.mRating =  in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
