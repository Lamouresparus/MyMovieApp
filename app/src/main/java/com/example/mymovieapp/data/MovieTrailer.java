package com.example.mymovieapp.data;

public class MovieTrailer {

    private final String trailerName;
    private final String trailerStringUrl;

    public MovieTrailer(String trailerName, String trailerStringUrl) {
        this.trailerName = trailerName;
        this.trailerStringUrl = trailerStringUrl;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerStringUrl() {
        return trailerStringUrl;
    }

}
