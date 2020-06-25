package com.example.mymovieapp.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovieapp.data.AppDatabase;
import com.example.mymovieapp.data.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private final LiveData<List<Movie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favouriteMovies = appDatabase.favouriteDao().loadAllFavourites();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
