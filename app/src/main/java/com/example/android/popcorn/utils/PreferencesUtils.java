package com.example.android.popcorn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.popcorn.data.json.TMDbSorting;

public class PreferencesUtils {

    private static final String PREF_LAST_USED_SORTING = "LAST_USED_SORTING";
    private static final String TAG = PreferencesUtils.class.getSimpleName();

    /**
     * we use the shared preferences to keep the last used sorting over app kills alive
     */
    public static void setLastUsedSorting(Context context, TMDbSorting newSorting) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Log.d(TAG, "preferences saved to new movies sorting: " + newSorting);
        editor.putString(PREF_LAST_USED_SORTING, newSorting.toString());
        editor.apply();
    }

    public static TMDbSorting getLastUsedSorting(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return TMDbSorting.valueOf(
                sp.getString(PREF_LAST_USED_SORTING, TMDbSorting.POPULAR.toString()));
    }

}
