package com.example.mymovieapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
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
import com.example.mymovieapp.view_model.MainViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageTv;
    private ProgressBar mProgressBar;

    /**
     * if sortingCategory 1 : popular and rating
     * else if 0 : loadFavourite
     */
    private int sortingCategory = 1;
    private MainViewModel mainViewModel;
    private boolean networkIsAvailable;
    private InternetBroadcastReceiver mInternetBroadcastReciever;
    private IntentFilter mInternetIntenFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageTv = findViewById(R.id.error_message_tv);
        mRecyclerView = findViewById(R.id.rv_movie_catalog);
        mProgressBar = findViewById(R.id.progress_bar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mInternetIntenFilter = new IntentFilter();
        mInternetBroadcastReciever = new InternetBroadcastReceiver();

        mInternetIntenFilter.addAction(Intent.ACTION_MANAGE_NETWORK_USAGE);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);



        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.hasFixedSize();

        mRecyclerView.setAdapter(mMovieAdapter);

        //default movie catalog will be in sort order of popular
        loadMovieData(NetworkUtils.POPULAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mInternetBroadcastReciever,mInternetIntenFilter);
        if(!networkIsAvailable){
            mErrorMessageTv.setText(R.string.no_internet_connection);
            showErrorMessage();
        }
        else {
            mErrorMessageTv.setText(R.string.error_message);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mInternetBroadcastReciever);
    }

    @SuppressLint("StaticFieldLeak")
    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            String param = strings[0];

            URL movieRequest = NetworkUtils.buildUrl(param);

            try {
                String jsonMovieResponse = NetworkUtils.httpUrlConnection(movieRequest);

                ArrayList<Movie> movies = MovieJson.getMoviesFromJson(jsonMovieResponse);

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
            mProgressBar.setVisibility(View.GONE);
            mErrorMessageTv.setVisibility(View.GONE);
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
        mErrorMessageTv.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);

    }

    private void loadFavourites(){
        showMovieCatalog();
        mainViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(sortingCategory != 0) return;

                ArrayList<Movie> movieArrayList = (ArrayList<Movie>) movies;

                if (movieArrayList != null && movieArrayList.size()!=0) {
                    mMovieAdapter.setMovieData(movieArrayList);
                }
                else showFavouriteErrorMessage();
            }
        });
    }

    private void showFavouriteErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setText(R.string.no_favourite_added);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sort_by_rating) {
            sortingCategory = 1;
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.rating);
            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.RATING);
            return true;
        }
        if (itemId == R.id.sort_by_popular) {
            sortingCategory =1;
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.most_popular);

            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.POPULAR);
            return true;
        }
        if(itemId == R.id.sort_by_favourites){
            sortingCategory = 0;
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.favorites);

            mMovieAdapter.setMovieData(null);
            loadFavourites();
        }
        return super.onOptionsItemSelected(item);

    }

    private class InternetBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {
                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (mobileData != null && wifi!=null) {
                   if ((wifi.isAvailable() || mobileData.isAvailable())){
                       networkIsAvailable=true;
                    }
                }
                else {
                    networkIsAvailable = false;
                }
            }
        }
    }
}
