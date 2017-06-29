package com.example.android.popcorn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.popcorn.activities.details.MovieInFavoritesLoader;
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
import com.example.android.popcorn.databinding.ActivityMovieDetailsBinding;
import com.example.android.popcorn.utils.ApiUtils;
import com.example.android.popcorn.utils.MovieIntents;
import com.squareup.picasso.Picasso;

import static com.example.android.popcorn.utils.DataUrlsHelper.getTMDbImageUri;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerClickedListener {
    private final static String TAG = MovieDetailsActivity.class.getSimpleName();

    private int tmDbMovieId;

    private TMDbMovie tmDbMovie;

    private TMDbTrailersAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;
    private int mTrailerPosition;

    private TMDbReviewsAdapter mReviewsAdapter;
    private RecyclerView mReviewsRecyclerView;
    private int mReviewsPosition;

    private ActivityMovieDetailsBinding movieDetailsBinding;

    private MovieInFavoritesLoader mFavoritesLoader;
    private Boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

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
            int MOVIE_NOT_EXISTING = -1;
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

        mFavoritesLoader = new DetailsFavoriteMovieLoader(this);
        mFavoritesLoader.setId(tmDbMovieId);
        getSupportLoaderManager().initLoader(MovieInFavoritesLoader.ID_FAVORITES_LOADER_ID, null, mFavoritesLoader);

        PopcornSyncInitializer.initializeReview(this, tmDbMovieId);
        PopcornSyncInitializer.initializeTrailer(this, tmDbMovieId);

        hideTrailers();
        hideReviews();
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
                if (mIsFavorite) {
                   popcornContentResolver.delete(PopcornContract.MoviesEntry.CONTENT_URI,
                           PopcornContract.MoviesEntry._ID + "=? AND " + PopcornContract.MoviesEntry.SORTING + "=?",
                           new String[]{String.valueOf(tmDbMovieId), TMDbSorting.FAVORITE.toString()});
                } else {
                    final ContentValues newFavorite = new ContentValues();
                    newFavorite.put(PopcornContract.MoviesEntry.SORTING, TMDbSorting.FAVORITE.toString());
                    newFavorite.put(PopcornContract.MoviesEntry._ID, tmDbMovieId);
                    newFavorite.put(PopcornContract.MoviesEntry.ORIGINAL_TITLE, tmDbMovie.getOriginalTitle());
                    newFavorite.put(PopcornContract.MoviesEntry.OVERVIEW, tmDbMovie.getOverview());
                    newFavorite.put(PopcornContract.MoviesEntry.POSTER_PATH, tmDbMovie.getPosterPath());
                    newFavorite.put(PopcornContract.MoviesEntry.RELEASE_DATE, tmDbMovie.getReleaseDate());
                    newFavorite.put(PopcornContract.MoviesEntry.VOTE_AVERAGE, tmDbMovie.getVoteAverage());
                    newFavorite.put(PopcornContract.MoviesEntry.TITLE, tmDbMovie.getTitle());
                    popcornContentResolver.insert(PopcornContract.MoviesEntry.CONTENT_URI, newFavorite);
                }

                getSupportLoaderManager().restartLoader(MovieInFavoritesLoader.ID_FAVORITES_LOADER_ID, null, mFavoritesLoader);
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
        if (tmDbMovie != null) {
            movieDetailsBinding.tvMovieTitle.setText(tmDbMovie.getOriginalTitle());
            movieDetailsBinding.tvOverview.setText(tmDbMovie.getOverview());
            movieDetailsBinding.iDetailHeader.tvRating.setText(getString(R.string.movie_voting_max, String.valueOf(tmDbMovie.getVoteAverage())));
            movieDetailsBinding.iDetailHeader.tvReleaseDate.setText(tmDbMovie.getReleaseDate());
            Uri imageUri = getTMDbImageUri(tmDbMovie.getPosterPath());
            Picasso.with(this).load(imageUri).into(movieDetailsBinding.iDetailHeader.ivThumbnail);
        }
        if (mIsFavorite != null){
            final Drawable drawable;
            if (mIsFavorite){
                drawable = ApiUtils.getDrawableApiSave(this, R.drawable.star_selected);
            } else {
                drawable = ApiUtils.getDrawableApiSave(this, R.drawable.star_unselected);
            }
            movieDetailsBinding.iDetailHeader.bMarkFavorite.setBackground(drawable);
        }
    }

    private final class DetailsFavoriteMovieLoader extends MovieInFavoritesLoader {

        public DetailsFavoriteMovieLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            mIsFavorite = data;
            bindMovieData();
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
            mIsFavorite = null;
        }
    }
}
