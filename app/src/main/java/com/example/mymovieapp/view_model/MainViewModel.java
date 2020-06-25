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
    private LiveData<List<Movie>> favouriteMovies;
    private final AppDatabase appDatabase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        fetchFavouritesFromDb();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    private void fetchFavouritesFromDb(){
        favouriteMovies = appDatabase.favouriteDao().loadAllFavourites();
    }
}
