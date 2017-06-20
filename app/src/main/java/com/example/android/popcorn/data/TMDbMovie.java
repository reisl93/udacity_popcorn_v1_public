package com.example.android.popcorn.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

@SuppressWarnings("unused") //allowed since GSON uses it
public class TMDbMovie implements Parcelable {

    @Nullable
    private String title;
    private int id;
    @Nullable
    private String poster_path;
    @Nullable
    private String overview;
    @Nullable
    private String original_title;
    private double vote_average;
    @Nullable
    private String release_date;

    protected TMDbMovie(Parcel in) {
        title = in.readString();
        id = in.readInt();
        poster_path = in.readString();
        overview = in.readString();
        original_title = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
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
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(original_title);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(@Nullable final String poster_path) {
        this.poster_path = poster_path;
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
    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(@Nullable final String original_title) {
        this.original_title = original_title;
    }
    @Nullable
    public String getOverview() {
        return overview;
    }

    public void setOverview(@Nullable final String overview) {
        this.overview = overview;
    }

    @Nullable
    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(@Nullable String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
}
