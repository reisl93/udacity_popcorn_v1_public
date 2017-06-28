package com.example.android.popcorn.data.json;

import com.google.gson.annotations.SerializedName;

public class TMDbReview {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    @SerializedName("content")
    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    @SerializedName("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }
}
