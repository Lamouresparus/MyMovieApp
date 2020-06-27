package com.example.mymovieapp;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieAdapter;
import com.example.mymovieapp.utils.MovieJson;
import com.example.mymovieapp.utils.NetworkUtils;
import com.example.mymovieapp.view_model.MainViewModel;

import org.jetbrains.annotations.NotNull;

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
    private String sort;
    private static final String SORT_KEY = "sort key";
    private ArrayList<Movie> fav;
    private ConnectivityManager.NetworkCallback networkStatusCallback;
    private ConnectivityManager connectivityManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageTv = findViewById(R.id.error_message_tv);
        mRecyclerView = findViewById(R.id.rv_movie_catalog);
        mProgressBar = findViewById(R.id.progress_bar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeFavourite();


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);


        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.hasFixedSize();

        mRecyclerView.setAdapter(mMovieAdapter);

        Log.v(TAG, "savedInstance is " + savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getString(SORT_KEY) != null) {
            sort = savedInstanceState.getString(SORT_KEY);

            Log.v(TAG, "sort is " + sort);
            if (!sort.equals(getResources().getString(R.string.favorites))) {
                loadMovieData(sort);
            } else {
                Log.v(TAG, "SORT IS " + sort);
                sortingCategory = 0;
                loadFavourites();
            }

        } else {

            //default movie catalog will be in sort order of popular
            loadMovieData(NetworkUtils.POPULAR);
        }
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkStatusCallback = new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    if(savedInstanceState == null){
                        Toast.makeText(MainActivity.this,"Connected to the Internet", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            };
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putString(SORT_KEY, sort);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkStatusCallback);
        }
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
            } else showErrorMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadMovieData(String sort) {
        if (sort.equals(NetworkUtils.POPULAR)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.most_popular);
        } else Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.rating);
        showMovieCatalog();
        new FetchMoviesTask().execute(sort);
    }


    private void showMovieCatalog() {
        mErrorMessageTv.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setText(getResources().getText(R.string.error_message));
        mErrorMessageTv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }


    private void observeFavourite() {

        mainViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(List<Movie> movies) {
                fav = (ArrayList) movies;
                if (sortingCategory == 0) {
                    loadFavourites();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    private void loadFavourites() {

        showMovieCatalog();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.favorites);

        if (fav != null && fav.size() != 0) {
            mMovieAdapter.setMovieData(fav);
        } else showFavouriteErrorMessage();
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
            sort = NetworkUtils.RATING;
            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.RATING);
            return true;
        }
        if (itemId == R.id.sort_by_popular) {
            sortingCategory = 1;
            sort = NetworkUtils.POPULAR;

            mMovieAdapter.setMovieData(null);

            loadMovieData(NetworkUtils.POPULAR);
            return true;
        }
        if (itemId == R.id.sort_by_favourites) {
            sortingCategory = 0;
            sort = getResources().getString(R.string.favorites);

            mMovieAdapter.setMovieData(null);
            loadFavourites();
        }
        return super.onOptionsItemSelected(item);

    }
}

