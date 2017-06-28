package com.example.android.popcorn.data.sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class PopcornSyncInitializer {
    private static boolean sInitialized;
    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;
        sInitialized = true;

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                startImmediateSync(context);
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, PopcornSyncIntentService.class);
        intentToSyncImmediately.setAction(PopcornSyncIntentService.POPCORN_SYNC_ALL);
        context.startService(intentToSyncImmediately);
    }
}
