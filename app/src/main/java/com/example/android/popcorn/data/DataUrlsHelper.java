package com.example.android.popcorn.data;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("FieldCanBeLocal")
public class DataUrlsHelper {
    private static final String TAG = DataUrlsHelper.class.getSimpleName();

    private static final String tmdbHost = "http://image.tmdb.org/";
    private static final String tmdbDefaultImagePath = "t/p/";
    private static final String tmdbImageResolution = "w780/";

    private static final String tmdbApiHost = "http://api.themoviedb.org/3";
    private static final String tmdbApiPopular = "movie/popular";
    private static final String tmdbApiTopRated = "movie/top_rated";
    private static final String tmdbApiApiKey = "api_key";


    public static Uri getTMDbImageUri(final String imagePath) {
        return Uri.parse(tmdbHost).buildUpon()
                .appendEncodedPath(tmdbDefaultImagePath)
                .appendEncodedPath(tmdbImageResolution)
                .appendEncodedPath(imagePath)
                .build();
    }

    @android.support.annotation.Nullable
    public static URL getTMDbUrl(final TMDbSorting sorting) {
        Uri.Builder uriBuilder = Uri.parse(tmdbApiHost).buildUpon();
        switch (sorting) {
            case POPULAR:
                uriBuilder.appendEncodedPath(tmdbApiPopular);
                break;
            case TOP_RATED:
                uriBuilder.appendEncodedPath(tmdbApiTopRated);
                break;
            default:
                String msg = "Missing branch. Someone appended " + TMDbSorting.class.getSimpleName() + " without editing this switch branch.";
                Log.e(TAG, msg);
                throw new IllegalArgumentException(msg);
        }
        String apiKey = ApiKey.API_KEY;
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
