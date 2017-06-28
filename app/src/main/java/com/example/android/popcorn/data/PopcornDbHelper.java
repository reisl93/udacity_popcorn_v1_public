package com.example.android.popcorn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.popcorn.data.PopcornContract.*;

public class PopcornDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popcorn.db";
    private static final int DATABASE_VERSION = 5;

    public PopcornDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MoviesEntry.TABLE_NAME + " (" +
                        MoviesEntry._ID + " INTEGER NOT NULL, " +
                        MoviesEntry.SORTING + " TEXT NOT NULL, " +
                        MoviesEntry.TITLE + " TEXT NOT NULL," +
                        MoviesEntry.POSTER_PATH + " TEXT NOT NULL," +
                        MoviesEntry.OVERVIEW + " TEXT NOT NULL," +
                        MoviesEntry.ORIGINAL_TITLE + " TEXT NOT NULL," +
                        MoviesEntry.VOTE_AVERAGE + " REAL NOT NULL," +
                        MoviesEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                        " PRIMARY KEY ( " + MoviesEntry._ID + " , " + MoviesEntry.SORTING + " ) " +
                        ")";
        final String SQL_CREATE_TRAILERS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TrailersEntry.TABLE_NAME + " (" +
                        TrailersEntry._ID + " INTEGER NOT NULL, " +
                        TrailersEntry.VIDEO_KEY + " TEXT NOT NULL, " +
                        TrailersEntry.WEBSITE + " TEXT NOT NULL, " +
                        TrailersEntry.VIDEO_TITLE + " TEXT NOT NULL, " +
                        " PRIMARY KEY ( " + TrailersEntry._ID + " , " + TrailersEntry.VIDEO_KEY + " ) " +
                        ");";
        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ReviewsEntry.TABLE_NAME + " (" +
                        ReviewsEntry._ID + " INTEGER NOT NULL, " +
                        ReviewsEntry.AUTHOR + " TEXT NOT NULL, " +
                        ReviewsEntry.CONTENT + " TEXT NOT NULL, " +
                        ReviewsEntry.REVIEW_ID + " TEXT NOT NULL, " +
                        " PRIMARY KEY ( " + ReviewsEntry._ID + " , " + ReviewsEntry.REVIEW_ID + " ) " +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
