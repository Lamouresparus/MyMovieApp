package com.example.mymovieapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.example.mymovieapp.data.MovieTrailer;
import com.example.mymovieapp.data.MovieTrailerAdapter;
import com.example.mymovieapp.utils.MovieJson;
import com.example.mymovieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";
    private ArrayList<MovieTrailer> movieTrailers;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private RecyclerView movieTrailerRv;
    TextView mMovieTrailerTv;

    String movieId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTrailerRv = findViewById(R.id.rv_movie_trailer);
        ImageView mImageView = findViewById(R.id.movie_image);
        TextView mMovieDescription = findViewById(R.id.movie_description);
        TextView mMovieTitle = findViewById(R.id.movie_title);
        TextView mReleaseDate = findViewById(R.id.movie_release_date);
        TextView mMovieRating = findViewById(R.id.movie_rating);
        mMovieTrailerTv = findViewById(R.id.movie_trailer_tv);

        Intent intent = getIntent();

        Movie movieDetails = intent.getParcelableExtra(MovieAdapter.KEY_MOVIE);

        if (movieDetails != null) {

            Objects.requireNonNull(getSupportActionBar()).setTitle(movieDetails.getmMovieTitle());

            movieId = String.valueOf(movieDetails.getmMovieId());
            mMovieTitle.setText(movieDetails.getmMovieTitle());
            mMovieRating.setText(String.valueOf(movieDetails.getmRating()));
            mMovieDescription.setText(movieDetails.getmMovieDescription());
            mReleaseDate.setText(movieDetails.getmReleaseDate());

            Picasso.get().load(movieDetails.getmImageUrl()).into(mImageView);

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        mMovieTrailerAdapter = new MovieTrailerAdapter(this);

        movieTrailerRv.setLayoutManager(layoutManager);
        movieTrailerRv.hasFixedSize();
        movieTrailerRv.setAdapter(mMovieTrailerAdapter);

        seeMovieTrailer();


    }

    public void seeMovieTrailer() {

        new FetchMovieTrailer().execute(movieId);
    }

    public class FetchMovieTrailer extends AsyncTask<String, Void, ArrayList<MovieTrailer>>{

        @Override
        protected ArrayList<MovieTrailer> doInBackground(String... strings) {

            String param = strings[0];

            URL movieTrailerRequest = NetworkUtils.buildMovieTrailerDetails(param);

                try {
                    String jsonMovieResponse = NetworkUtils.httpUrlConnection(movieTrailerRequest);

                    movieTrailers = MovieJson.getTrailerDetails(jsonMovieResponse);

                    Log.v(TAG, "Trailer Url is "+movieTrailers.get(0).getTrailerStringUrl());

                    return movieTrailers;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
            super.onPostExecute(movieTrailers);
            Log.v(TAG, "Trailer Uri for is "+movieTrailers);
            if (movieTrailers != null) {
                mMovieTrailerAdapter.setMovieTrailerData(movieTrailers);
                }

            else showErrorMessage();
        }

    }

    private void showErrorMessage() {
       mMovieTrailerTv.setText("No Movie Trailers");
    }
}
