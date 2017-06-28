package com.example.android.popcorn.activities.details;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbSorting;

public abstract class MovieInFavoritesLoader implements LoaderManager.LoaderCallbacks<Boolean>{

    public static final int ID_FAVORITES_LOADER_ID = 12123532;

    private static final String[] MAIN_MOVIE_PROJECTION = {
            PopcornContract.MoviesEntry._ID
    };

    private final Context mContext;

    private int mId;

    public MovieInFavoritesLoader(final Context context){
        this.mContext = context;
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Boolean>(mContext) {

            Boolean isFavorite = null;

            @Override
            protected void onStartLoading() {
                if (isFavorite != null) {
                    deliverResult(isFavorite);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Boolean loadInBackground() {
                try {
                    final ContentResolver popcornContentResolver = getContext().getContentResolver();
                    Cursor movie = null;
                    try {
                        movie = popcornContentResolver.query(PopcornContract.MoviesEntry.CONTENT_URI,
                                MAIN_MOVIE_PROJECTION,
                                PopcornContract.MoviesEntry._ID + "=? AND " + PopcornContract.MoviesEntry.SORTING + "=?",
                                new String[]{String.valueOf(mId), TMDbSorting.FAVORITE.toString()}, null);
                        if (movie != null) {
                            return movie.moveToFirst();
                        } else {
                            return null;
                        }
                    } finally {
                        if (movie != null) movie.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(Boolean data) {
                isFavorite = data;
                super.deliverResult(data);
            }
        };
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
