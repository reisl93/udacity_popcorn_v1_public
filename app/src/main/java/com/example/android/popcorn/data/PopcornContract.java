package com.example.android.popcorn.data;

import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings("WeakerAccess")
public class PopcornContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popcorn";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String SORTING = "sorting";

        public static final String TITLE = "title";
        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
    }

    public static final class TrailersEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS)
                .build();

        public static final String TABLE_NAME = "trailers";

        public static final String VIDEO_KEY = "video_key";
        public static final String WEBSITE = "website";
        public static final String VIDEO_TITLE = "video_title";
    }

    public static final class ReviewsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();

        public static final String TABLE_NAME = "reviews";

        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String REVIEW_ID = "review_id";
    }
}
