package com.example.android.popcorn.activities.details;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbMovie;

public abstract class MovieLoader implements LoaderManager.LoaderCallbacks<TMDbMovie> {

    public static final int ID_MOVIE_LOADER_ID = 2929292;

    private static final String[] MAIN_MOVIE_PROJECTION = {
            PopcornContract.MoviesEntry.RELEASE_DATE,
            PopcornContract.MoviesEntry.VOTE_AVERAGE,
            PopcornContract.MoviesEntry.TITLE,
            PopcornContract.MoviesEntry.OVERVIEW,
            PopcornContract.MoviesEntry.POSTER_PATH
    };

    static final int INDEX_MOVIE_RELEASE_DATE = 0;
    static final int INDEX_MOVIE_VOTE_AVERAGE = 1;
    static final int INDEX_MOVIE_TITLE = 2;
    static final int INDEX_MOVIE_OVERVIEW = 3;
    static final int INDEX_MOVIE_POSTER_PATH = 4;

    private final Context mContext;

    private int mId;

    public MovieLoader(final Context context){
        this.mContext = context;
    }

    @Override
    public Loader<TMDbMovie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<TMDbMovie>(mContext) {

            TMDbMovie tmDbMovie = null;

            @Override
            protected void onStartLoading() {
                if (tmDbMovie != null) {
                    deliverResult(tmDbMovie);
                } else {
                    forceLoad();
                }
            }

            @Override
            public TMDbMovie loadInBackground() {
                try {
                    final ContentResolver popcornContentResolver = getContext().getContentResolver();
                    final Uri contentUri = PopcornContract.MoviesEntry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(mId)).build();
                    Cursor movie = null;
                    try {
                        movie = popcornContentResolver.query(contentUri, MAIN_MOVIE_PROJECTION, null, null, null);
                        if (movie != null && movie.moveToFirst()) {
                            return new TMDbMovie(
                                    movie.getString(INDEX_MOVIE_TITLE),
                                    mId,
                                    movie.getString(INDEX_MOVIE_POSTER_PATH),
                                    movie.getString(INDEX_MOVIE_OVERVIEW),
                                    movie.getString(INDEX_MOVIE_TITLE),
                                    movie.getDouble(INDEX_MOVIE_VOTE_AVERAGE),
                                    movie.getString(INDEX_MOVIE_RELEASE_DATE)
                                    );
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
            public void deliverResult(TMDbMovie data) {
                tmDbMovie = data;
                super.deliverResult(data);
            }
        };
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
