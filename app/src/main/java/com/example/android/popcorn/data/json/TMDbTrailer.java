package com.example.android.popcorn.data.json;

import com.google.gson.annotations.SerializedName;

public class TMDbTrailer {
    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String videoKey;

    @SerializedName("site")
    private String website;

    @SerializedName("name")
    private String videoTitle;

    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getVideoKey() {
        return videoKey;
    }

    @SerializedName("key")
    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getWebsite() {
        return website;
    }

    @SerializedName("site")
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    @SerializedName("name")
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
