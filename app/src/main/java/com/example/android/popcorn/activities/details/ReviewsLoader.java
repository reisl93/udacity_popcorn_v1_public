package com.example.android.popcorn.activities.details;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popcorn.data.PopcornContract;

public abstract class ReviewsLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ID_REVIEW_ID_LOADER = 2343231;
    private final Context mContext;


    private static final String[] MAIN_REVIEWS_PROJECTION = {
            PopcornContract.ReviewsEntry.AUTHOR,
            PopcornContract.ReviewsEntry.CONTENT};

    static final int INDEX_REVIEW_AUTHOR = 0;
    static final int INDEX_REVIEW_CONTENT = 1;

    private int mId = -1;

    public ReviewsLoader(Context context) {
        this.mContext = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_REVIEW_ID_LOADER:
                Uri reviewQueryUri = PopcornContract.ReviewsEntry.CONTENT_URI.buildUpon()
                        .appendEncodedPath(String.valueOf(mId))
                        .build();
                return new CursorLoader(mContext, reviewQueryUri, MAIN_REVIEWS_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    public void setId(final int id) {
        mId = id;
    }

}
