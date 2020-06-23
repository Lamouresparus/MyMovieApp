package com.example.mymovieapp.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovieapp.data.AppDatabase;
import com.example.mymovieapp.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        favouriteMovies = appDatabase.favouriteDao().loadAllFavourites();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
