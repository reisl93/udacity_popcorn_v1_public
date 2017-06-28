package com.example.android.popcorn.data.json;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class TMDbTrailers {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    @Nullable
    private TMDbTrailer[] trailers;


    @Nullable
    public TMDbTrailer[] getTrailers() {
        return trailers;
    }

    @SerializedName("results")
    public void setTrailers(@Nullable TMDbTrailer[] trailers) {
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
