package com.example.android.popcorn.data.sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.popcorn.data.json.TMDbSorting;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PopcornSyncInitializer {
    private static Map<TMDbSorting, Boolean> sInitializedMovies = new EnumMap<TMDbSorting, Boolean>(TMDbSorting.class);
    private static Map<Integer, Boolean> sInitializedTrailers = new HashMap<>();
    private static Map<Integer, Boolean> sInitializedReviews = new HashMap<>();

    synchronized public static void initializeMovies(@NonNull final Context context, final TMDbSorting sorting) {

        if (sInitializedMovies.containsKey(sorting) && sInitializedMovies.get(sorting)) return;
        sInitializedMovies.put(sorting, true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                startImmediateSync(context, PopcornSyncIntentService.POPCORN_SYNC_MOVIES, sorting);
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


    public static void startImmediateSync(@NonNull final Context context, final String sync_action, TMDbSorting sorting) {
        Intent intentToSyncImmediately = new Intent(context, PopcornSyncIntentService.class);
        intentToSyncImmediately.setAction(sync_action);
        intentToSyncImmediately.putExtra(PopcornSyncIntentService.POPCORN_SYNC_SORTING_KEY, sorting.toString());
        context.startService(intentToSyncImmediately);
    }

    public static void startImmediateSyncWithId(@NonNull final Context context, final String sync_action, final int id) {
        Intent intentToSyncImmediately = new Intent(context, PopcornSyncIntentService.class);
        intentToSyncImmediately.setAction(sync_action);
        intentToSyncImmediately.putExtra(PopcornSyncIntentService.POPCORN_SYNC_ID_KEY, id);
        context.startService(intentToSyncImmediately);
    }
}
