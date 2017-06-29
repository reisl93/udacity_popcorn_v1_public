package com.example.android.popcorn.data.sync;

import android.content.ContentValues;

import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbMovie;
import com.example.android.popcorn.data.json.TMDbReview;
import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.json.TMDbTrailer;

class PopcornSyncUtils {
    static ContentValues[] getMoviesContentValues(TMDbSorting sorting, TMDbMovie[] tmDbMovies) {
        ContentValues[] contentValues = new ContentValues[tmDbMovies.length];
        for (int i = 0; i < tmDbMovies.length; i++) {
            final TMDbMovie tmDbMovie = tmDbMovies[i];
            ContentValues contentValue = new ContentValues();
            contentValue.put(PopcornContract.MoviesEntry._ID, tmDbMovie.getId());
            contentValue.put(PopcornContract.MoviesEntry.SORTING, sorting.toString());
            contentValue.put(PopcornContract.MoviesEntry.TITLE, tmDbMovie.getTitle());
            contentValue.put(PopcornContract.MoviesEntry.POSTER_PATH, tmDbMovie.getPosterPath());
            contentValue.put(PopcornContract.MoviesEntry.OVERVIEW, tmDbMovie.getOverview());
            contentValue.put(PopcornContract.MoviesEntry.ORIGINAL_TITLE, tmDbMovie.getOriginalTitle());
            contentValue.put(PopcornContract.MoviesEntry.VOTE_AVERAGE, tmDbMovie.getVoteAverage());
            contentValue.put(PopcornContract.MoviesEntry.RELEASE_DATE, tmDbMovie.getReleaseDate());
            contentValues[i] = contentValue;
        }

        return contentValues;
    }

    static ContentValues[] getTrailersContentValues(int id, TMDbTrailer[] trailers) {
        ContentValues[] contentValues = new ContentValues[trailers.length];
        for (int i = 0; i < trailers.length; i++) {
            final TMDbTrailer tmDbTrailer = trailers[i];
            ContentValues contentValue = new ContentValues();
            contentValue.put(PopcornContract.TrailersEntry._ID, id);
            contentValue.put(PopcornContract.TrailersEntry.VIDEO_KEY, tmDbTrailer.getVideoKey());
            contentValue.put(PopcornContract.TrailersEntry.WEBSITE, tmDbTrailer.getWebsite());
            contentValue.put(PopcornContract.TrailersEntry.VIDEO_TITLE, tmDbTrailer.getVideoTitle());
            contentValues[i] = contentValue;
        }

        return contentValues;
    }

    static ContentValues[] getReviewsContentValues(int id, TMDbReview[] reviews) {
        ContentValues[] contentValues = new ContentValues[reviews.length];
        for (int i = 0; i < reviews.length; i++) {
            final TMDbReview tmDbReview = reviews[i];
            ContentValues contentValue = new ContentValues();
            contentValue.put(PopcornContract.ReviewsEntry._ID, id);
            contentValue.put(PopcornContract.ReviewsEntry.AUTHOR, tmDbReview.getAuthor());
            contentValue.put(PopcornContract.ReviewsEntry.CONTENT, tmDbReview.getContent());
            contentValue.put(PopcornContract.ReviewsEntry.REVIEW_ID, tmDbReview.getId());
            contentValues[i] = contentValue;
        }

        return contentValues;
    }
}
