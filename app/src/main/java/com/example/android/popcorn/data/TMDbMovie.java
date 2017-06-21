package com.example.android.popcorn.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused") //allowed since GSON uses it
public class TMDbMovie implements Parcelable {

    @Nullable
    @SerializedName("title")
    private String title;
    private int id;
    @Nullable
    @SerializedName("poster_path")
    private String posterPath;
    @Nullable
    private String overview;
    @Nullable
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("vote_average")
    private double voteAverage;
    @Nullable
    @SerializedName("release_date")
    private String releaseDate;

    protected TMDbMovie(Parcel in) {
        title = in.readString();
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        originalTitle = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Creator<TMDbMovie> CREATOR = new Creator<TMDbMovie>() {
        @Override
        public TMDbMovie createFromParcel(Parcel in) {
            return new TMDbMovie(in);
        }

        @Override
        public TMDbMovie[] newArray(int size) {
            return new TMDbMovie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(originalTitle);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    @SerializedName("poster_path")
    public void setPosterPath(@Nullable final String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable final String title) {
        this.title = title;
    }

    @Nullable
    public String getOriginalTitle() {
        return originalTitle;
    }

    @SerializedName("original_title")
    public void setOriginalTitle(@Nullable final String originalTitle) {
        this.originalTitle = originalTitle;
    }
    @Nullable
    public String getOverview() {
        return overview;
    }

    public void setOverview(@Nullable final String overview) {
        this.overview = overview;
    }

    @Nullable
    public String getReleaseDate() {
        return releaseDate;
    }

    @SerializedName("release_date")
    public void setReleaseDate(@Nullable String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @SerializedName("vote_average")
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
