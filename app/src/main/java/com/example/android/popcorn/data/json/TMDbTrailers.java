package com.example.android.popcorn.data.json;

import com.google.gson.annotations.SerializedName;

public class TMDbTrailers {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private TMDbTrailer[] trailers;


    public TMDbTrailer[] getTrailers() {
        return trailers;
    }

    @SerializedName("results")
    public void setTrailers(TMDbTrailer[] trailers) {
        this.trailers = trailers;
    }

    public int getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(int id) {
        this.id = id;
    }
}
