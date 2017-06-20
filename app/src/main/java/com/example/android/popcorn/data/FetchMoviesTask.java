package com.example.android.popcorn.data;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popcorn.utils.AsyncCallback;
import com.example.android.popcorn.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.URL;


public class FetchMoviesTask extends AsyncTask<TMDbSorting, Void, TMDbMovies> {
    private final static String TAG = FetchMoviesTask.class.getSimpleName();

    private final AsyncCallback<TMDbMovies> uiCallback;

    public FetchMoviesTask(final AsyncCallback<TMDbMovies> uiCallback) {
        this.uiCallback = uiCallback;
    }

    @Override
    @Nullable
    protected TMDbMovies doInBackground(TMDbSorting... params) {
        if (params.length == 0) {
            return null;
        }

        TMDbMovies movies = null;

        final TMDbSorting sorting = params[0];
        final URL movieRequestURL = DataUrlsHelper.getTMDbUrl(sorting);
        if (movieRequestURL != null) {
            try {
                final String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);

                try {
                    movies = new Gson().fromJson(jsonMovieResponse, TMDbMovies.class);
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Either TMDb has changed its API, or the " + TMDbMovies.class.getSimpleName() + " has changed. " +
                            "Returned JSON: " + jsonMovieResponse);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    @Override
    protected void onPostExecute(@Nullable final TMDbMovies movies) {
        uiCallback.asyncCallback(movies);
    }
}
