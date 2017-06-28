package com.example.android.popcorn.data.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rupert on 20.06.2017.
 */
@SuppressWarnings("unused") // used for GSON
public class TMDbMovies {
    //actually we don't use this count, for simplicity
    @SerializedName("total_results")
    private int totalResults;
    private TMDbMovie[] results;

    public TMDbMovie[] getResults() {
        return results;
    }

    public void setResults(TMDbMovie[] results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    @SerializedName("total_results")
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
