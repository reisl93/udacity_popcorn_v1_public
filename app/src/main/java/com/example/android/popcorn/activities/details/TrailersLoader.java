package com.example.android.popcorn.activities.details;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.popcorn.data.PopcornContract;

public abstract class TrailersLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ID_TRAILER_ID_LOADER = 9283;
    private static final String TAG = TrailersLoader.class.getSimpleName();
    private final Context mContext;


    private static final String[] MAIN_TRAILERS_PROJECTION = {
            PopcornContract.TrailersEntry.VIDEO_KEY,
            PopcornContract.TrailersEntry.VIDEO_TITLE,
            PopcornContract.TrailersEntry.WEBSITE};

    static final int INDEX_TRAILER_VIDEO_KEY = 0;
    static final int INDEX_TRAILER_VIDEO_TITLE = 1;
    static final int INDEX_TRAILER_WEBSITE = 2;

    private int mId = -1;

    public TrailersLoader(Context context) {
        this.mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_TRAILER_ID_LOADER:
                Log.d(TAG, "Creating Trailer Loader");
                Uri trailerQueryUri = PopcornContract.TrailersEntry.CONTENT_URI.buildUpon()
                        .appendEncodedPath(String.valueOf(mId))
                        .build();
                return new CursorLoader(mContext, trailerQueryUri, MAIN_TRAILERS_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    public void setId(final int id) {
        mId = id;
    }

}
