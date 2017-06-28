package com.example.android.popcorn.data.sync;

import android.app.IntentService;
import android.content.Intent;

public class PopcornSyncIntentService extends IntentService{
    public PopcornSyncIntentService() {
        super("PopcornSyncIntentService");
    }

    public static final String POPCORN_SYNC_ALL = "POPCORN_SYNC_ALL";
    public static final String POPCORN_SYNC_MOVIES = "POPCORN_SYNC_MOVIES";

    @Override
    protected void onHandleIntent(Intent intent) {
        intent.getAction();
        switch (intent.getAction()){

            case POPCORN_SYNC_MOVIES:
                PopcornSyncTask.syncMovies(this);
                break;

            case POPCORN_SYNC_ALL: /* redundant action key, but here for overall code readability */
            default:
                PopcornSyncTask.syncMovies(this);
                PopcornSyncTask.syncTrailers(this);
                PopcornSyncTask.syncReviews(this);
        }

    }
}
