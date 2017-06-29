package com.example.android.popcorn.activities.posters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbSorting;

public abstract class MovieLoader implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int ID_MOVIES_LOADER = 8769874;
    private final Context mContext;


    private static final String[] MAIN_MOVIES_PROJECTION = {
            PopcornContract.MoviesEntry._ID,
            PopcornContract.MoviesEntry.POSTER_PATH};

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    static final int INDEX_MOVIE_ID = 0;
    static final int INDEX_MOVIE_POSTER_PATH = 1;

    private TMDbSorting mSorting = TMDbSorting.POPULAR;

    public MovieLoader(Context context){
        this.mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MOVIES_LOADER:
                Uri moviesQueryUri = PopcornContract.MoviesEntry.CONTENT_URI.buildUpon()
                        .appendEncodedPath(mSorting.toString())
                        .build();
                return new CursorLoader(mContext, moviesQueryUri, MAIN_MOVIES_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    public void setSorting(TMDbSorting mSorting) {
        this.mSorting = mSorting;
    }
}
