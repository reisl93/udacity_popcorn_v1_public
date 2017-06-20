package com.example.android.popcorn.data;

/**
 * Created by Rupert on 20.06.2017.
 */

@SuppressWarnings("unused") // used for GSON
public class TMDbMovies {
    //actually we don't use this count, for simplicity
    private int total_results;
    private TMDbMovie[] results;

    public TMDbMovie[] getResults() {
        return results;
    }

    public void setResults(TMDbMovie[] results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
