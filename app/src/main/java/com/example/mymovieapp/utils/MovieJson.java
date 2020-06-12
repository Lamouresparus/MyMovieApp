package com.example.mymovieapp.utils;

import android.util.Log;

import com.example.mymovieapp.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJson {

    private static final String TAG = "MovieJson";

    private static final String RESULT = "results";
    private static final String TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String IMAGE_URL = "poster_path";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";


    public static Movie[] getMoviesFromJson(String jsonString) {



        try{
            JSONObject object = new JSONObject(jsonString);
            JSONArray results = object.getJSONArray(RESULT);

            Movie[] movies = new Movie[results.length()];

            for (int i=0; i<results.length(); i++){
                JSONObject movieObject = results.getJSONObject(i);
                String title = movieObject.getString(TITLE);
                String overview = movieObject.getString(OVERVIEW);
                String releaseDate = movieObject.getString(RELEASE_DATE);
                String imageUrl = movieObject.getString(IMAGE_URL);
                imageUrl = BASE_IMAGE_URL+imageUrl;
                double rating = movieObject.getDouble(RATING);

                Movie movie = new Movie(imageUrl,title,overview,releaseDate,rating);

                movies[i] = movie;
            }
            Log.v(TAG, movies[3].getmMovieDescription());


            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
