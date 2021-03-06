package com.example.mymovieapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie implements Parcelable {
    @PrimaryKey
    private final int mMovieId;
    private  final String mImageUrl;
    private final String mMovieTitle;
    private final String mMovieDescription;
    private final String mReleaseDate;
    private final double mRating;



    public Movie(int mMovieId, String mImageUrl, String movieTitle, String movieDescription, String releaseDate, double rating) {
        this.mImageUrl = mImageUrl;
        this.mMovieTitle = movieTitle;
        this.mMovieDescription = movieDescription;
        this.mReleaseDate = releaseDate;
        this.mRating = rating;
        this.mMovieId = mMovieId;
    }



    public String getMImageUrl() {
        return mImageUrl;
    }

    public String getMMovieTitle() {
        return mMovieTitle;
    }

    public String getMMovieDescription() {
        return mMovieDescription;
    }

    public String getMReleaseDate() {
        return mReleaseDate;
    }

    public double getMRating() {
        return mRating;
    }

    public int getMMovieId() {
        return mMovieId;
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
        dest.writeInt(this.mMovieId);
    }

    private Movie(Parcel in){
        this.mImageUrl = in.readString();
        this.mMovieTitle = in.readString();
        this.mMovieDescription = in.readString();
        this.mReleaseDate = in.readString();
        this.mRating =  in.readDouble();
        this.mMovieId = in.readInt();
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
