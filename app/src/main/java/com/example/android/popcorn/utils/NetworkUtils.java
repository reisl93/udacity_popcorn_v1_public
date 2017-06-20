package com.example.android.popcorn.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.annotation.Nullable;

/**
 * Created by Rupert on 20.06.2017.
 */
public class NetworkUtils {

    private final static String TAG = NetworkUtils.class.getSimpleName();

    public static @Nullable String getResponseFromHttpUrl(final URL url) throws IOException {
        Log.d(TAG, "Loading url " + url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
