package com.example.android.popcorn.data.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.android.popcorn.data.json.TMDbSorting;

public class PopcornSyncIntentService extends IntentService{
    private static final String TAG = PopcornSyncIntentService.class.getSimpleName();

    public PopcornSyncIntentService() {
        super("PopcornSyncIntentService");
    }

    public static final String POPCORN_SYNC_MOVIES = "POPCORN_SYNC_MOVIES";
    public static final String POPCORN_SYNC_TRAILER = "POPCORN_SYNC_TRAILER";
    public static final String POPCORN_SYNC_REVIEW = "POPCORN_SYNC_REVIEW";

    public static final String POPCORN_SYNC_ID_KEY = "POPCORN_SYNC_ID_KEY";
    public static final String POPCORN_SYNC_SORTING_KEY = "POPCORN_SYNC_SORTING_KEY";

    @Override
    protected void onHandleIntent(Intent intent) {
        final int id;
        int invalidId = -1;
        if (intent.hasExtra(POPCORN_SYNC_ID_KEY)){
            id = intent.getIntExtra(POPCORN_SYNC_ID_KEY, invalidId);
        } else {
            id = invalidId;
        }

        TMDbSorting sorting;
        if (intent.hasExtra(POPCORN_SYNC_SORTING_KEY)){
            sorting = TMDbSorting.valueOf(intent.getStringExtra(POPCORN_SYNC_SORTING_KEY));
        } else {
            sorting = TMDbSorting.POPULAR;
        }

        switch (intent.getAction()){

            case POPCORN_SYNC_REVIEW:
                PopcornSyncTask.syncReviews(this, id);
                break;

            case POPCORN_SYNC_TRAILER:
                PopcornSyncTask.syncTrailers(this, id);
                break;

            case POPCORN_SYNC_MOVIES:
                PopcornSyncTask.syncMovies(this, sorting);
                break;
            default:
                Log.w(TAG, "empty sync intent: " + intent.getAction());
        }

    }
}
