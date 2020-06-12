package com.example.mymovieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ImageView mImageView = findViewById(R.id.movie_image);
        TextView mMovieDescription = findViewById(R.id.movie_description);
        TextView mMovieTitle = findViewById(R.id.movie_title);
        TextView mReleaseDate = findViewById(R.id.movie_release_date);
        TextView mMovieRating = findViewById(R.id.movie_rating);

        Intent intent = getIntent();

        Movie movieDetails = (Movie) intent.getSerializableExtra(MovieAdapter.KEY_MOVIE);

        assert movieDetails != null;
        mMovieTitle.setText(movieDetails.getmMovieTitle());
        mMovieRating.setText(String.valueOf(movieDetails.getmRating()));
        mMovieDescription.setText(movieDetails.getmMovieDescription());
        mReleaseDate.setText(movieDetails.getmReleaseDate());

        Picasso.get().load(movieDetails.getmImageUrl()).into(mImageView);


    }
}
