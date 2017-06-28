package com.example.android.popcorn.data.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbMovie;
import com.example.android.popcorn.data.json.TMDbReview;
import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.json.TMDbTrailer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PopcornSyncUtils {
    static ContentValues[] getMoviesContentValues(Map<TMDbSorting, TMDbMovie[]> tmDbSortingMap) {
        List<ContentValues> movieValues = new ArrayList<>();
        for (Map.Entry<TMDbSorting, TMDbMovie[]> entry : tmDbSortingMap.entrySet()){
            for (final TMDbMovie tmDbMovie : entry.getValue()){
                ContentValues contentValue = new ContentValues();
                contentValue.put(PopcornContract.MoviesEntry._ID, tmDbMovie.getId());
                contentValue.put(PopcornContract.MoviesEntry.SORTING, entry.getKey().toString());
                contentValue.put(PopcornContract.MoviesEntry.TITLE, tmDbMovie.getTitle());
                contentValue.put(PopcornContract.MoviesEntry.POSTER_PATH, tmDbMovie.getPosterPath());
                contentValue.put(PopcornContract.MoviesEntry.OVERVIEW, tmDbMovie.getOverview());
                contentValue.put(PopcornContract.MoviesEntry.ORIGINAL_TITLE, tmDbMovie.getOriginalTitle());
                contentValue.put(PopcornContract.MoviesEntry.VOTE_AVERAGE, tmDbMovie.getVoteAverage());
                contentValue.put(PopcornContract.MoviesEntry.RELEASE_DATE, tmDbMovie.getReleaseDate());
                movieValues.add(contentValue);
            }
        }
        return movieValues.toArray(new ContentValues[movieValues.size()]);
    }

    public static ContentValues[] getTrailersContentValues(Map<Integer, TMDbTrailer[]> trailers) {
        List<ContentValues> movieValues = new ArrayList<>();
        for (Map.Entry<Integer, TMDbTrailer[]> entry : trailers.entrySet()){
            for (final TMDbTrailer tmDbTrailer : entry.getValue()){
                ContentValues contentValue = new ContentValues();
                contentValue.put(PopcornContract.TrailersEntry._ID, entry.getKey());
                contentValue.put(PopcornContract.TrailersEntry.VIDEO_KEY, tmDbTrailer.getVideoKey());
                contentValue.put(PopcornContract.TrailersEntry.WEBSITE, tmDbTrailer.getWebsite());
                contentValue.put(PopcornContract.TrailersEntry.VIDEO_TITLE, tmDbTrailer.getVideoTitle());
                movieValues.add(contentValue);
            }
        }
        return movieValues.toArray(new ContentValues[movieValues.size()]);
    }

    public static ContentValues[] getReviewsContentValues(Map<Integer, TMDbReview[]> reviews) {
        List<ContentValues> movieValues = new ArrayList<>();
        for (Map.Entry<Integer, TMDbReview[]> entry : reviews.entrySet()){
            for (final TMDbReview tmDbReview : entry.getValue()){
                ContentValues contentValue = new ContentValues();
                contentValue.put(PopcornContract.ReviewsEntry._ID, entry.getKey());
                contentValue.put(PopcornContract.ReviewsEntry.AUTHOR, tmDbReview.getAuthor());
                contentValue.put(PopcornContract.ReviewsEntry.CONTENT, tmDbReview.getContent());
                contentValue.put(PopcornContract.ReviewsEntry.REVIEW_ID, tmDbReview.getId());
                movieValues.add(contentValue);
            }
        }
        return movieValues.toArray(new ContentValues[movieValues.size()]);
    }
}
