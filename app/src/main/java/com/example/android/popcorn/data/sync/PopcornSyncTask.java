package com.example.android.popcorn.data.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbMovie;
import com.example.android.popcorn.data.json.TMDbMovies;
import com.example.android.popcorn.data.json.TMDbReview;
import com.example.android.popcorn.data.json.TMDbReviews;
import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.json.TMDbTrailer;
import com.example.android.popcorn.data.json.TMDbTrailers;
import com.example.android.popcorn.utils.DataUrlsHelper;
import com.example.android.popcorn.utils.NetworkUtils;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PopcornSyncTask {

    private static final String TAG = PopcornSyncTask.class.getSimpleName();

    synchronized public static void syncMovies(Context context) {

        final Map<TMDbSorting, TMDbMovie[]> tmDbSortingMap = new HashMap<>();

        for (final TMDbSorting sorting : ImmutableList.of(TMDbSorting.POPULAR, TMDbSorting.TOP_RATED)) {
            final URL movieRequestURL = DataUrlsHelper.getTMDbRatingsUrl(sorting);
            if (movieRequestURL != null) {
                try {
                    final String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestURL);

                    try {
                        tmDbSortingMap.put(sorting, new Gson().fromJson(jsonMovieResponse, TMDbMovies.class).getResults());
                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbMovies.class.getSimpleName() + " has changed. " +
                                "Returned JSON: " + jsonMovieResponse);
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ContentValues[] movieValues = PopcornSyncUtils.getMoviesContentValues(tmDbSortingMap);

        if (movieValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();
            //delete all except his favorites
            popcornContentResolver.delete(
                    PopcornContract.MoviesEntry.CONTENT_URI,
                    PopcornContract.MoviesEntry.SORTING + " IN (?, ?)",
                    new String[]{TMDbSorting.TOP_RATED.toString(), TMDbSorting.POPULAR.toString()});
            popcornContentResolver.bulkInsert(PopcornContract.MoviesEntry.CONTENT_URI, movieValues);
        }
    }

    synchronized public static void syncTrailer(Context context, final int id) {

        final URL trailerRequestURL = DataUrlsHelper.getTMDbTrailerUrl(id);
        Map<Integer, TMDbTrailer[]> trailers = new HashMap<>();
        if (trailerRequestURL != null) {
            try {
                final String jsonTrailerResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestURL);

                try {
                    trailers.put(id, new Gson().fromJson(jsonTrailerResponse, TMDbTrailers.class).getTrailers());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbTrailers.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonTrailerResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues[] trailersValues = PopcornSyncUtils.getTrailersContentValues(trailers);

        if (trailersValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();

            popcornContentResolver.delete(PopcornContract.TrailersEntry.CONTENT_URI, PopcornContract.TrailersEntry._ID + "=?", new String[]{String.valueOf(id)});
            popcornContentResolver.bulkInsert(PopcornContract.TrailersEntry.CONTENT_URI, trailersValues);
        }
    }

    synchronized public static void syncReview(Context context, final int id) {

        Map<Integer, TMDbReview[]> reviews = new HashMap<>();
        final URL trailerRequestURL = DataUrlsHelper.getTMDbReviewsUrl(id);
        if (trailerRequestURL != null) {
            try {
                final String jsonReviewResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestURL);

                try {
                    reviews.put(id, new Gson().fromJson(jsonReviewResponse, TMDbReviews.class).getReviews());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbReviews.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonReviewResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues[] reviewValues = PopcornSyncUtils.getReviewsContentValues(reviews);

        if (reviewValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();

            popcornContentResolver.delete(PopcornContract.ReviewsEntry.CONTENT_URI, PopcornContract.ReviewsEntry._ID + "=?", new String[]{String.valueOf(id)});
            popcornContentResolver.bulkInsert(PopcornContract.ReviewsEntry.CONTENT_URI, reviewValues);
        }
    }

}
