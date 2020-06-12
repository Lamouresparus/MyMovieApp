package com.example.mymovieapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=";
    //You would have to input your API key. You can get this from https://developers.themoviedb.org/3
    private static final String API_KEY = "";
    private static final String LANGUAGE_PARAM = "language";
    private static final String language = "en-US";
    private static final String SORT_BY_PARAM = "sort_by";
    public static final String POPULAR = "popularity.desc";
    public static final String RATING = "vote_average.desc";
    private static final String TAG = "NetworkUtils";

    public static URL buildUrl(String sortParameter){
        Uri builtUri = Uri.parse(BASE_URL+API_KEY)
                .buildUpon()
                .appendQueryParameter(LANGUAGE_PARAM,language)
                .appendQueryParameter(SORT_BY_PARAM, sortParameter)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG,"Url formed is" + url);

        return url;
    }



    public static  String httpUrlConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
