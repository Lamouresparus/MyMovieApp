package com.example.mymovieapp.utils;

import android.util.Log;

import com.example.mymovieapp.data.Movie;
import com.example.mymovieapp.data.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieJson {

    private static final String TAG = "MovieJson";

    private static final String RESULT = "results";
    private static final String TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String IMAGE_URL = "poster_path";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String KEY = "key";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";


    public static ArrayList<Movie> getMoviesFromJson(String jsonString) {



        try{
            JSONObject object = new JSONObject(jsonString);
            JSONArray results = object.getJSONArray(RESULT);

            ArrayList<Movie> movies = new ArrayList<>();






            for (int i=0; i<results.length(); i++){
                JSONObject movieObject = results.getJSONObject(i);
                String title = movieObject.getString(TITLE);
                int id = movieObject.getInt(ID);
                String overview = movieObject.getString(OVERVIEW);
                Object releaseDate = movieObject.get(RELEASE_DATE);
                String releaseDateString = releaseDate.toString();
                String imageUrl = movieObject.getString(IMAGE_URL);
                imageUrl = BASE_IMAGE_URL+imageUrl;
                double rating = movieObject.getDouble(RATING);

                Movie movie = new Movie(id,imageUrl,title,overview,releaseDateString,rating);

                movies.add(i, movie);
            }

            Log.v(TAG, "Movies ds" +movies.size());


            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static ArrayList<MovieTrailer> getTrailerDetails(String jsonMovieResponse) {

        try {
            JSONObject object = new JSONObject(jsonMovieResponse);

        JSONArray results = object.getJSONArray(RESULT);
        ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();


        for (int i=0; i<results.length(); i++) {
            JSONObject trailerObject = results.getJSONObject(i);
            String trailerKey = trailerObject.getString(KEY);
            String trailerName = trailerObject.getString(NAME);

            String trailerUrlString = YOUTUBE_BASE_URL + trailerKey;

            MovieTrailer movieTrailer = new MovieTrailer(trailerName,trailerUrlString);

            movieTrailers.add(i, movieTrailer);
        }

            return movieTrailers;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
