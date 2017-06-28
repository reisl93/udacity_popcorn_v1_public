package com.example.android.popcorn.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


public class PopcornProvider extends ContentProvider {
    private final static String TAG = PopcornProvider.class.getSimpleName();

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;
    public static final int CODE_MOVIES_WITH_SORTING = 102;
    public static final int CODE_TRAILERS = 200;
    public static final int CODE_TRAILERS_WITH_ID = 201;
    public static final int CODE_REVIEWS = 300;
    public static final int CODE_REVIEWS_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopcornDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopcornContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopcornContract.PATH_MOVIES, CODE_MOVIES);
        matcher.addURI(authority, PopcornContract.PATH_MOVIES + "/#", CODE_MOVIES_WITH_ID);
        matcher.addURI(authority, PopcornContract.PATH_MOVIES + "/*", CODE_MOVIES_WITH_SORTING);
        matcher.addURI(authority, PopcornContract.PATH_TRAILERS, CODE_TRAILERS);
        matcher.addURI(authority, PopcornContract.PATH_TRAILERS + "/#", CODE_TRAILERS_WITH_ID);
        matcher.addURI(authority, PopcornContract.PATH_REVIEWS, CODE_REVIEWS);
        matcher.addURI(authority, PopcornContract.PATH_REVIEWS + "/#", CODE_REVIEWS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PopcornDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                return bulkInsertToTable(PopcornContract.MoviesEntry.TABLE_NAME, values, db, uri);
            case CODE_TRAILERS:
                return bulkInsertToTable(PopcornContract.TrailersEntry.TABLE_NAME, values, db, uri);
            case CODE_REVIEWS:
                return bulkInsertToTable(PopcornContract.ReviewsEntry.TABLE_NAME, values, db, uri);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES_WITH_ID: {
                final String id = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{id};
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.MoviesEntry.TABLE_NAME,
                        projection,
                        PopcornContract.MoviesEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_MOVIES_WITH_SORTING: {
                final String sorting = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{sorting};
                Log.d(TAG, "loading movies with sorting " + sorting);
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.MoviesEntry.TABLE_NAME,
                        projection,
                        PopcornContract.MoviesEntry.SORTING + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_MOVIES: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_TRAILERS_WITH_ID: {
                final String id = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{id};
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.TrailersEntry.TABLE_NAME,
                        projection,
                        PopcornContract.TrailersEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_TRAILERS: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_REVIEWS_WITH_ID: {
                final String normalizedUtcDateString = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        PopcornContract.ReviewsEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_REVIEWS: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PopcornContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        PopcornContract.MoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            case CODE_TRAILERS:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        PopcornContract.TrailersEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_REVIEWS:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        PopcornContract.ReviewsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Popcorn.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final long insertedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                insertedRows = db.insert(PopcornContract.MoviesEntry.TABLE_NAME, null, values);
                break;
            case CODE_TRAILERS:
                insertedRows = db.insert(PopcornContract.TrailersEntry.TABLE_NAME, null, values);
                break;
            case CODE_REVIEWS:
                insertedRows = db.insert(PopcornContract.ReviewsEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (insertedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Popcorn");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }

    private int bulkInsertToTable(String table, @NonNull ContentValues[] values, SQLiteDatabase db, Uri notificationUri) {
        int rowsInserted = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(notificationUri, null);
        }

        return rowsInserted;
    }

}
