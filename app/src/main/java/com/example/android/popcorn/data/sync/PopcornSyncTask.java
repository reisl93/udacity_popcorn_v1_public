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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.URL;

class PopcornSyncTask {

    private static final String TAG = PopcornSyncTask.class.getSimpleName();

    synchronized static void syncMovies(Context context, TMDbSorting sorting) {

        TMDbMovie[] tmDbMovies = null;

        final URL movieRequestURL = DataUrlsHelper.getTMDbRatingsUrl(sorting);
        if (movieRequestURL != null) {
            try {
                final String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);
                try {
                    tmDbMovies =  new Gson().fromJson(jsonMovieResponse, TMDbMovies.class).getResults();
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbMovies.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonMovieResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues[] movieValues = PopcornSyncUtils.getMoviesContentValues(sorting, tmDbMovies);

        if (movieValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();
            //delete all except his favorites
            popcornContentResolver.delete(
                    PopcornContract.MoviesEntry.CONTENT_URI,
                    PopcornContract.MoviesEntry.SORTING + " = ?",
                    new String[]{sorting.toString()});
            popcornContentResolver.bulkInsert(PopcornContract.MoviesEntry.CONTENT_URI, movieValues);
        }
    }

    synchronized public static void syncTrailers(Context context, final int id) {

        final URL trailerRequestURL = DataUrlsHelper.getTMDbTrailerUrl(id);
        TMDbTrailer[] trailers = null;
        if (trailerRequestURL != null) {
            try {
                final String jsonTrailerResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestURL);

                try {
                    trailers = new Gson().fromJson(jsonTrailerResponse, TMDbTrailers.class).getTrailers();
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbTrailers.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonTrailerResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues[] trailersValues = PopcornSyncUtils.getTrailersContentValues(id, trailers);

        if (trailersValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();

            popcornContentResolver.delete(PopcornContract.TrailersEntry.CONTENT_URI, PopcornContract.TrailersEntry._ID + "=?", new String[]{String.valueOf(id)});
            popcornContentResolver.bulkInsert(PopcornContract.TrailersEntry.CONTENT_URI, trailersValues);
        }
    }

    synchronized public static void syncReviews(Context context, final int id) {

       TMDbReview[] reviews = null;
        final URL trailerRequestURL = DataUrlsHelper.getTMDbReviewsUrl(id);
        if (trailerRequestURL != null) {
            try {
                final String jsonReviewResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestURL);

                try {
                    reviews= new Gson().fromJson(jsonReviewResponse, TMDbReviews.class).getReviews();
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbReviews.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonReviewResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues[] reviewValues = PopcornSyncUtils.getReviewsContentValues(id, reviews);

        if (reviewValues.length > 0) {
            ContentResolver popcornContentResolver = context.getContentResolver();

            popcornContentResolver.delete(PopcornContract.ReviewsEntry.CONTENT_URI, PopcornContract.ReviewsEntry._ID + "=?", new String[]{String.valueOf(id)});
            popcornContentResolver.bulkInsert(PopcornContract.ReviewsEntry.CONTENT_URI, reviewValues);
        }
    }

}
