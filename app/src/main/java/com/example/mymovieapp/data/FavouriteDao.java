package com.example.mymovieapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface FavouriteDao {

    /**
     * @return
     */
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMovie(Movie movie);

    @Delete
    void deleteFavourite(Movie movie);

    @Query("SELECT * FROM movie WHERE mMovieId = :movieId")
    LiveData<Movie> loadMovieById (int movieId);

    @Query("DELETE FROM movie WHERE mMovieId = :movieId")
    void removeFromFavourite(int movieId);
}
