package com.example.android.popcorn.data.network;

import android.net.Uri;
import android.util.Log;

import com.example.android.popcorn.BuildConfig;
import com.example.android.popcorn.data.json.TMDbSorting;

import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("FieldCanBeLocal")
public class DataUrlsHelper {
    private static final String TAG = DataUrlsHelper.class.getSimpleName();

    private static final String tmdbHost = "http://image.tmdb.org/";
    private static final String tmdbDefaultImagePath = "t/p/";
    private static final String tmdbImageResolution = "w780/";

    private static final String tmdbApiHost = "http://api.themoviedb.org/3";
    private static final String tmdbApiMovie = "movie/";

    private static final String tmdbApiPopular = "popular";
    private static final String tmdbApiTopRated = "top_rated";

    private static final String tmdbApiReviews = "reviews";
    private static final String tmdbApiVideos = "videos";

    private static final String tmdbApiApiKey = "api_key";


    public static Uri getTMDbImageUri(final String imagePath) {
        return Uri.parse(tmdbHost).buildUpon()
                .appendEncodedPath(tmdbDefaultImagePath)
                .appendEncodedPath(tmdbImageResolution)
                .appendEncodedPath(imagePath)
                .build();
    }

    @android.support.annotation.Nullable
    public static URL getTMDbRatingsUrl(final TMDbSorting sorting) {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;

        Uri.Builder uriBuilder = Uri.parse(tmdbApiHost).buildUpon();
        uriBuilder.appendEncodedPath(tmdbApiMovie);
        switch (sorting) {
            case POPULAR:
                uriBuilder.appendEncodedPath(tmdbApiPopular);
                break;
            case TOP_RATED:
                uriBuilder.appendEncodedPath(tmdbApiTopRated);
                break;
            case FAVORITE:
                // ignore
                break;
            default:
                String msg = "Missing branch. Someone appended " + TMDbSorting.class.getSimpleName() + " without editing this switch branch.";
                Log.e(TAG, msg);
                throw new IllegalArgumentException(msg);
        }
        uriBuilder.appendQueryParameter(tmdbApiApiKey, apiKey);

        try {
            return new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong format of TMDb URL.");
            e.printStackTrace();
        }
        return null;
    }

    public static URL getTMDbTrailerUrl(final int id) {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;

        Uri.Builder uriBuilder = Uri.parse(tmdbApiHost).buildUpon();
        uriBuilder.appendEncodedPath(tmdbApiMovie);
        uriBuilder.appendEncodedPath(String.valueOf(id));
        uriBuilder.appendEncodedPath(tmdbApiVideos);
        uriBuilder.appendQueryParameter(tmdbApiApiKey, apiKey);

        try {
            return new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong format of TMDb URL.");
            e.printStackTrace();
        }
        return null;
    }

    public static URL getTMDbReviewsUrl(final int id) {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;

        Uri.Builder uriBuilder = Uri.parse(tmdbApiHost).buildUpon();
        uriBuilder.appendEncodedPath(tmdbApiMovie);
        uriBuilder.appendEncodedPath(String.valueOf(id));
        uriBuilder.appendEncodedPath(tmdbApiReviews);
        uriBuilder.appendQueryParameter(tmdbApiApiKey, apiKey);

        try {
            return new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong format of TMDb URL.");
            e.printStackTrace();
        }
        return null;
    }

}
