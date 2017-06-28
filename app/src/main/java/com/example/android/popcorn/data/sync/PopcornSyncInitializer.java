package com.example.android.popcorn.data.sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PopcornSyncInitializer {
    private static boolean sInitialized;
    private static Map<Integer, Boolean> sInitializedTrailers = new HashMap<>();
    private static Map<Integer, Boolean> sInitializedReviews = new HashMap<>();

    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;
        sInitialized = true;

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                startImmediateSync(context, PopcornSyncIntentService.POPCORN_SYNC_MOVIES);
                return null;
            }
        }.execute();
    }

    synchronized public static void initializeTrailer(@NonNull final Context context, final int id) {

        if (sInitializedTrailers.containsKey(id) && sInitializedTrailers.get(id)) return;
        sInitializedTrailers.put(id, true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                startImmediateSyncWithId(context, PopcornSyncIntentService.POPCORN_SYNC_TRAILER, id);
                return null;
            }
        }.execute();
    }

    synchronized public static void initializeReview(@NonNull final Context context, final int id) {

        if (sInitializedReviews.containsKey(id) && sInitializedReviews.get(id)) return;
        sInitializedReviews.put(id, true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                startImmediateSyncWithId(context, PopcornSyncIntentService.POPCORN_SYNC_REVIEW, id);
                return null;
            }
        }.execute();
    }


    public static void startImmediateSync(@NonNull final Context context, final String sync_action) {
        Intent intentToSyncImmediately = new Intent(context, PopcornSyncIntentService.class);
        intentToSyncImmediately.setAction(sync_action);
        context.startService(intentToSyncImmediately);
    }

    public static void startImmediateSyncWithId(@NonNull final Context context, final String sync_action, final int id) {
        Intent intentToSyncImmediately = new Intent(context, PopcornSyncIntentService.class);
        intentToSyncImmediately.setAction(sync_action);
        intentToSyncImmediately.putExtra(PopcornSyncIntentService.POPCORN_SYNC_ID_KEY, id);
        context.startService(intentToSyncImmediately);
    }
}
