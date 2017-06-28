package com.example.android.popcorn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.popcorn.activities.details.MovieLoader;
import com.example.android.popcorn.activities.details.ReviewsLoader;
import com.example.android.popcorn.activities.details.TMDbReviewsAdapter;
import com.example.android.popcorn.activities.details.TMDbTrailersAdapter;
import com.example.android.popcorn.activities.details.TrailerClickedListener;
import com.example.android.popcorn.activities.details.TrailersLoader;
import com.example.android.popcorn.data.PopcornContract;
import com.example.android.popcorn.data.json.TMDbMovie;
import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.sync.PopcornSyncInitializer;
import com.example.android.popcorn.utils.MovieIntents;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerClickedListener {
    private final static String TAG = MovieDetailsActivity.class.getSimpleName();

    private int tmDbMovieId;
    private int MOVIE_NOT_EXISTING = -1;

    private TMDbMovie tmDbMovie;
    private MovieLoader mMovieLoader;

    private TMDbTrailersAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;
    private int mTrailerPosition;

    private TMDbReviewsAdapter mReviewsAdapter;
    private RecyclerView mReviewsRecyclerView;
    private int mReviewsPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        final LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerAdapter = new TMDbTrailersAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        final LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewsAdapter = new TMDbReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        Intent calledByIntent = getIntent();
        if (calledByIntent.hasExtra(MovieIntents.VIEW_MOVIE_DETAILS)) {
            tmDbMovieId = calledByIntent.getIntExtra(MovieIntents.VIEW_MOVIE_DETAILS, MOVIE_NOT_EXISTING);
        }

        TrailersLoader trailersLoader = new DetailTrailersLoader(this);
        trailersLoader.setId(tmDbMovieId);
        getSupportLoaderManager().initLoader(TrailersLoader.ID_TRAILER_ID_LOADER, null, trailersLoader);

        ReviewsLoader reviewsLoader = new DetailsReviewsLoader(this);
        reviewsLoader.setId(tmDbMovieId);
        getSupportLoaderManager().initLoader(ReviewsLoader.ID_REVIEW_ID_LOADER, null, reviewsLoader);

        MovieLoader movieLoader = new DetailsMovieLoader(this);
        movieLoader.setId(tmDbMovieId);
        getSupportLoaderManager().initLoader(MovieLoader.ID_MOVIE_LOADER_ID, null, movieLoader);

        PopcornSyncInitializer.initializeReview(this, tmDbMovieId);
        PopcornSyncInitializer.initializeTrailer(this, tmDbMovieId);

        loadMovie();

        hideTrailers();
        hideReviews();
    }

    private void loadMovie() {
    }


    private void hideTrailers() {
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showTrailers() {
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideReviews() {
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showReviews() {
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    public void addMovieToFavorites(View view) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                ContentResolver popcornContentResolver = MovieDetailsActivity.this.getContentResolver();
                ContentValues newFavorite = new ContentValues();
                newFavorite.put(PopcornContract.MoviesEntry.SORTING, TMDbSorting.FAVORITE.toString());
                newFavorite.put(PopcornContract.MoviesEntry._ID, tmDbMovieId);
                newFavorite.put(PopcornContract.MoviesEntry.ORIGINAL_TITLE, tmDbMovieId);

                newFavorite.put(PopcornContract.MoviesEntry._ID, tmDbMovieId);
                popcornContentResolver.insert(PopcornContract.MoviesEntry.CONTENT_URI,
                        newFavorite);
                return null;
            }
        }.execute();
    }

    private final class DetailTrailersLoader extends TrailersLoader {
        DetailTrailersLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                mTrailerAdapter.swapCursor(data);
                if (mTrailerPosition == RecyclerView.NO_POSITION) mTrailerPosition = 0;
                mTrailerRecyclerView.smoothScrollToPosition(mTrailerPosition);
                if (data.getCount() != 0) showTrailers();
            } else {
                Log.w(TAG, "TrailersLoader.onLoadFinished was executed without data!");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTrailerAdapter.swapCursor(null);
        }
    }

    ;

    @Override
    public void onTrailerClicked(final String website, String trailerVideoKey) {
        final Uri uri;
        switch (website) {
            case "YouTube":
                uri = Uri.parse("http://www.youtube.com").buildUpon()
                        .appendPath("watch")
                        .appendQueryParameter("v", trailerVideoKey)
                        .build();
                break;
            default:
                Log.w(TAG, "For trailer clips the website: " + website + " is unknown!");
                uri = Uri.EMPTY;
        }
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (uri != Uri.EMPTY // there is an actual website
                && trailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(trailerIntent);
        }
    }

    private final class DetailsReviewsLoader extends ReviewsLoader {
        DetailsReviewsLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                mReviewsAdapter.swapCursor(data);
                if (mReviewsPosition == RecyclerView.NO_POSITION) mReviewsPosition = 0;
                mReviewsRecyclerView.smoothScrollToPosition(mReviewsPosition);
                if (data.getCount() != 0) showReviews();
            } else {
                Log.w(TAG, "ReviewsLoader.onLoadFinished was executed without data!");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mReviewsAdapter.swapCursor(null);
        }
    }

    private final class DetailsMovieLoader extends MovieLoader {

        DetailsMovieLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<TMDbMovie> loader, TMDbMovie data) {
            if (data != null){
                tmDbMovie = data;
                bindMovieData();
            }
        }

        @Override
        public void onLoaderReset(Loader<TMDbMovie> loader) {
            tmDbMovie = null;
        }
    }

    private void bindMovieData() {

    }
}
