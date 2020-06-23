package com.example.mymovieapp.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovieapp.data.AppDatabase;

public class AddFavouritesViewModelFactory implements ViewModelProvider.Factory {

    private AppDatabase db;
    private int favMovieId;

    public AddFavouritesViewModelFactory(AppDatabase db, int favMovieId){
        this.db = db;
        this.favMovieId = favMovieId;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddFavouriteViewModel(db, favMovieId);
    }
}
