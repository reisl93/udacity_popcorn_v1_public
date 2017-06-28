package com.example.android.popcorn.data.json;

import com.google.gson.annotations.SerializedName;

public class TMDbReviews {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private TMDbReview[] reviews;


    public TMDbReview[] getReviews() {
        return reviews;
    }

    @SerializedName("results")
    public void setReviews(TMDbReview[] reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(int id) {
        this.id = id;
    }
}
