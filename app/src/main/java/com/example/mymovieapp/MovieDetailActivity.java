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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovieapp.data.AppDatabase;
import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.example.mymovieapp.data.MovieReview;
import com.example.mymovieapp.data.MovieReviewAdapter;
import com.example.mymovieapp.data.MovieTrailer;
import com.example.mymovieapp.data.MovieTrailerAdapter;
import com.example.mymovieapp.data.MovieTrailerAndReviews;
import com.example.mymovieapp.utils.AppExecutors;
import com.example.mymovieapp.utils.MovieJson;
import com.example.mymovieapp.utils.NetworkUtils;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
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
    private MaterialFavoriteButton favoriteButton;
    private AppDatabase mDb;

    Movie mMovieDetails;

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
        favoriteButton = findViewById(R.id.favourites_button);
        mDb = AppDatabase.getInstance(this);

        Intent intent = getIntent();

        mMovieDetails = intent.getParcelableExtra(MovieAdapter.KEY_MOVIE);

        if (mMovieDetails != null) {

            Objects.requireNonNull(getSupportActionBar()).setTitle(mMovieDetails.getmMovieTitle());

            movieId = String.valueOf(mMovieDetails.getmMovieId());
            mMovieTitle.setText(mMovieDetails.getmMovieTitle());
            mMovieRating.setText(String.valueOf(mMovieDetails.getmRating()));
            mMovieDescription.setText(mMovieDetails.getmMovieDescription());
            mReleaseDate.setText(mMovieDetails.getmReleaseDate());

            Picasso.get().load(mMovieDetails.getmImageUrl()).into(mImageView);

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

        checkIfFavourite();


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

                movieTrailers = movieTrailerAndReviews.getmMovieTrailers();
                movieReviews = movieTrailerAndReviews.getmMovieReviews();
                if(movieTrailers != null && movieTrailers.size() != 0){
                    mMovieTrailerAdapter.setMovieTrailerData(movieTrailers);
                }
                else showMovieTrailerErrorMessage();

                if(movieReviews != null && movieReviews.size() != 0){
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

    private void checkIfFavourite(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mDb.favouriteDao().loadMovieById(mMovieDetails.getmMovieId()) !=null){
                    favoriteButton.setFavorite(true);
                };

            }
        });

        favouriteButtonOnChange();
    }

    private void favouriteButtonOnChange(){
        favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if(favorite){
                    saveFavourites(mMovieDetails);
                    Toast toast =Toast.makeText(getApplicationContext(),"added to favourites",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    deleteFavourite(mMovieDetails);
                    Toast toast =Toast.makeText(getApplicationContext(),"removed from favourites",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void deleteFavourite(final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favouriteDao().deleteFavourite(movie);
            }
        });
    }

    private void saveFavourites(final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favouriteDao().insertFavouriteMovie(movie);
            }
        });
    }

}
