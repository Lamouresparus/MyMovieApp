package com.example.mymovieapp.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymovieapp.data.AppDatabase;
import com.example.mymovieapp.data.Movie;

public class AddFavouriteViewModel extends ViewModel {
    private LiveData<Movie> favMovie;

    public AddFavouriteViewModel(AppDatabase db, int favMovieId) {

        this.favMovie = db.favouriteDao().loadMovieById(favMovieId);
    }

    public LiveData<Movie> getFavMovie() {
        return favMovie;
    }
}
