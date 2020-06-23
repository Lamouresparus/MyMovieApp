package com.example.mymovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.example.mymovieapp.utils.MovieJson;
import com.example.mymovieapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageTv;
    private ProgressBar mProgressBar;
     private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageTv = findViewById(R.id.error_message_tv);
        mRecyclerView = findViewById(R.id.rv_movie_catalog);
        mProgressBar = findViewById(R.id.progress_bar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);



        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.hasFixedSize();

        mRecyclerView.setAdapter(mMovieAdapter);

        //default movie catalog will be in sort order of popular
        loadMovieData(NetworkUtils.POPULAR);




    }

    @SuppressLint("StaticFieldLeak")
    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            Log.v(TAG, "Movie do in back");

            String param = strings[0];

            URL movieRequest = NetworkUtils.buildUrl(param);

            try {
                String jsonMovieResponse = NetworkUtils.httpUrlConnection(movieRequest);

               movies = MovieJson.getMoviesFromJson(jsonMovieResponse);

                assert movies != null;
                Log.v(TAG, movies.get(3).getMMovieDescription());

                return movies;


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mMovieAdapter.setMovieData(movies);
            }
            else showErrorMessage();
        }
    }

    private void loadMovieData(String sort) {

        showMovieCatalog();

        new FetchMoviesTask().execute(sort);
    }

    private void showMovieCatalog() {
        mErrorMessageTv.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sort_by_rating) {

            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.RATING);
            return true;
        }
        if (itemId == R.id.sort_by_popular) {

            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.POPULAR);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
