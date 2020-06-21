package com.example.mymovieapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.example.mymovieapp.data.MovieReview;
import com.example.mymovieapp.data.MovieReviewAdapter;
import com.example.mymovieapp.data.MovieTrailer;
import com.example.mymovieapp.data.MovieTrailerAdapter;
import com.example.mymovieapp.data.MovieTrailerAndReviews;
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
    private ArrayList<MovieReview> movieReviews;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;
    private RecyclerView movieTrailerRv;
    private RecyclerView movieReviewRv;
    private TextView mMovieTrailerTv;
    private TextView mMovieReviewTv;

    String movieId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTrailerRv = findViewById(R.id.rv_movie_trailer);
        movieReviewRv = findViewById(R.id.rv_movie_reviews);
        ImageView mImageView = findViewById(R.id.movie_image);
        TextView mMovieDescription = findViewById(R.id.movie_description);
        TextView mMovieTitle = findViewById(R.id.movie_title);
        TextView mReleaseDate = findViewById(R.id.movie_release_date);
        TextView mMovieRating = findViewById(R.id.movie_rating);
        mMovieTrailerTv = findViewById(R.id.movie_trailer_tv);
        mMovieReviewTv = findViewById(R.id.movie_reviews_tv);

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

        LinearLayoutManager movieTrailerLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        LinearLayoutManager movieReviewLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        mMovieTrailerAdapter = new MovieTrailerAdapter(this);
        movieReviewAdapter = new MovieReviewAdapter();


        movieTrailerRv.setLayoutManager(movieTrailerLayoutManager);
        movieReviewRv.setLayoutManager(movieReviewLayoutManager);

        movieTrailerRv.hasFixedSize();
        movieReviewRv.hasFixedSize();

        movieTrailerRv.setAdapter(mMovieTrailerAdapter);
        movieReviewRv.setAdapter(movieReviewAdapter);


        seeMovieTrailerAndReview();


    }

    public void seeMovieTrailerAndReview() {

        new FetchMovieTrailer().execute(movieId);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchMovieTrailer extends AsyncTask<String, Void, MovieTrailerAndReviews>{

        @Override
        protected MovieTrailerAndReviews doInBackground(String... strings) {

            String param = strings[0];

            URL movieTrailerRequest = NetworkUtils.buildMovieTrailerDetails(param);
            URL movieReviewRequest = NetworkUtils.buildMovieReview(param);

                try {
                    String jsonMovieTrailerResponse = NetworkUtils.httpUrlConnection(movieTrailerRequest);
                    String jsonMovieReviewResponse = NetworkUtils.httpUrlConnection(movieReviewRequest);

                    movieTrailers = MovieJson.getTrailerDetails(jsonMovieTrailerResponse);
                    movieReviews = MovieJson.getReviews(jsonMovieReviewResponse);

                    //Log.v(TAG, "Trailer Url is "+movieTrailers.get(0).getTrailerStringUrl());
                  //  Log.v(TAG, "Review is "+movieReviews.get(0).getReviewContent());


                    return new MovieTrailerAndReviews(movieTrailers,movieReviews);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
        }

        @Override
        protected void onPostExecute(MovieTrailerAndReviews movieTrailerAndReviews) {
            super.onPostExecute(movieTrailerAndReviews);
            if (movieTrailerAndReviews != null) {
                Log.v(TAG, "Trailer Uri for is "+movieTrailerAndReviews.getmMovieTrailers().get(0).getTrailerStringUrl());

                movieTrailers = movieTrailerAndReviews.getmMovieTrailers();
                movieReviews = movieTrailerAndReviews.getmMovieReviews();
                if(movieTrailers != null){
                    mMovieTrailerAdapter.setMovieTrailerData(movieTrailers);
                }
                else showMovieTrailerErrorMessage();

                if(movieReviews != null){
                    movieReviewAdapter.setMovieTrailerData(movieReviews);
                }
                else showMovieReviewErrorMessage();

                }

            else {
                showMovieTrailerErrorMessage();
                showMovieReviewErrorMessage();
            }
        }

    }

    private void showMovieTrailerErrorMessage() {
       mMovieTrailerTv.setText(R.string.no_movie_trailers);
    }

    private void showMovieReviewErrorMessage() {
        mMovieReviewTv.setText(R.string.no_movie_review);
    }
}
