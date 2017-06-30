package com.example.android.popcorn;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popcorn.activities.posters.MovieClickedListener;
import com.example.android.popcorn.activities.posters.MovieLoader;
import com.example.android.popcorn.activities.posters.TMDbMoviesAdapter;
import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.sync.PopcornSyncInitializer;
import com.example.android.popcorn.utils.PreferencesUtils;

import static com.example.android.popcorn.utils.MovieIntents.VIEW_MOVIE_DETAILS;

public class MoviePostersActivity extends AppCompatActivity implements MovieClickedListener {
    private static final String TAG = MoviePostersActivity.class.getSimpleName();
    private static final String POSTER_POSITION_KEY = "POSTER_POSITION_KEY";

    private TMDbMoviesAdapter mTmDbMoviesAdapter;
    private RecyclerView mMoviesRecyclerView;
    private TMDbSorting mMoviesSorting = TMDbSorting.POPULAR;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    private MovieLoader mMoviesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);

        /*if (savedInstanceState != null && savedInstanceState.containsKey(POSTER_POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(POSTER_POSITION_KEY);
            Log.d(TAG, "onCreate: setting position to " + mPosition);
        }*/

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        final GridLayoutManager layoutManager
                = new GridLayoutManager(this,
                getResources().getInteger(R.integer.movies_grid_size),
                LinearLayoutManager.VERTICAL, false);
        mTmDbMoviesAdapter = new TMDbMoviesAdapter(this);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mTmDbMoviesAdapter);

        mMoviesSorting = PreferencesUtils.getLastUsedSorting(this);

        mMoviesLoader = new PostersMovieLoader(this);
        mMoviesLoader.setSorting(mMoviesSorting);
        getSupportLoaderManager().initLoader(MovieLoader.ID_MOVIES_LOADER, null, mMoviesLoader);

        showLoading();

        PopcornSyncInitializer.initializeMovies(this, mMoviesSorting);
    }

    private void showMovies() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            if (mMoviesSorting != TMDbSorting.POPULAR) {
                reloadMoviesToSorting(TMDbSorting.POPULAR);
                PopcornSyncInitializer.initializeMovies(this, mMoviesSorting);
            }
            return true;
        } else if (id == R.id.action_top_rated) {
            if (mMoviesSorting != TMDbSorting.TOP_RATED) {
                reloadMoviesToSorting(TMDbSorting.TOP_RATED);
                PopcornSyncInitializer.initializeMovies(this, mMoviesSorting);
            }
            return true;
        } else if (id == R.id.action_favorite) {
            if (mMoviesSorting != TMDbSorting.FAVORITE) {
                reloadMoviesToSorting(TMDbSorting.FAVORITE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadMoviesToSorting(TMDbSorting sorting) {
        Log.d(TAG, "movies sorted to new sorting: " + sorting);
        mPosition = 0;
        mMoviesSorting = sorting;
        PreferencesUtils.setLastUsedSorting(this, sorting);
        mMoviesLoader.setSorting(mMoviesSorting);
        getSupportLoaderManager().restartLoader(MovieLoader.ID_MOVIES_LOADER, null, mMoviesLoader);
    }

    @Override
    public void onMovieClicked(final int id) {
        Intent intentToStartDetailActivity = new Intent(this, MovieDetailsActivity.class);
        intentToStartDetailActivity.putExtra(VIEW_MOVIE_DETAILS, id);
        startActivity(intentToStartDetailActivity);
    }

    private final class PostersMovieLoader extends MovieLoader {
        PostersMovieLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                mTmDbMoviesAdapter.swapCursor(data);
                /*if (mPosition == RecyclerView.NO_POSITION) {
                    Log.d(TAG, "mPosition not set, setting to 0");
                    mPosition = 0;
                }
                showMovies();
                Log.d(TAG, "onLoadFinished: scrolling to position " + mPosition);
                mMoviesRecyclerView.smoothScrollToPosition(mPosition);*/
            }
            showMovies();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTmDbMoviesAdapter.swapCursor(null);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(POSTER_POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(POSTER_POSITION_KEY);
            // because the images take time to load, we need to scroll delayed
            Log.d(TAG, "onRestoreInstanceState: scrolling to position " + mPosition);
            mMoviesRecyclerView.smoothScrollToPosition(mPosition);
            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 3000);*/
            // ((GridLayoutManager) mMoviesRecyclerView.getLayoutManager()).scrollToPosition(mPosition);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        mPosition = ((GridLayoutManager) mMoviesRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "onSaveInstanceState: saving mPosition to " + mPosition);
        outState.putInt(POSTER_POSITION_KEY, mPosition);

        super.onSaveInstanceState(outState);
    }
}
