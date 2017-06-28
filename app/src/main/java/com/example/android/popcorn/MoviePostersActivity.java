package com.example.android.popcorn;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popcorn.data.json.TMDbSorting;
import com.example.android.popcorn.data.sync.PopcornSyncInitializer;
import com.example.android.popcorn.activities.posters.MovieClickedListener;
import com.example.android.popcorn.activities.posters.MovieLoader;
import com.example.android.popcorn.activities.posters.TMDbMoviesAdapter;

import static com.example.android.popcorn.utils.MovieIntents.VIEW_MOVIE_DETAILS;

public class MoviePostersActivity extends AppCompatActivity implements MovieClickedListener{
    private static final String TAG = MoviePostersActivity.class.getSimpleName();

    private TMDbMoviesAdapter mTmDbMoviesAdapter;
    private RecyclerView mRecyclerView;
    private TMDbSorting mTmDbSorting = TMDbSorting.POPULAR;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        final GridLayoutManager layoutManager
                = new GridLayoutManager(this,
                getResources().getInteger(R.integer.movies_grid_size),
                LinearLayoutManager.VERTICAL, false);
        mTmDbMoviesAdapter = new TMDbMoviesAdapter(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTmDbMoviesAdapter);

        getSupportLoaderManager().initLoader(MovieLoader.ID_MOVIES_LOADER, null, mMoviesLoader);

        showLoading();

        PopcornSyncInitializer.initialize(this);
    }

    private void showMovies() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
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
            if (mTmDbSorting != TMDbSorting.POPULAR) {
                mMoviesLoader.setSorting(TMDbSorting.POPULAR);
                getSupportLoaderManager().restartLoader(MovieLoader.INDEX_MOVIE_ID, null, mMoviesLoader);
            }
            return true;
        } else if (id == R.id.action_top_rated) {
            if (mTmDbSorting != TMDbSorting.TOP_RATED) {
                mMoviesLoader.setSorting(TMDbSorting.TOP_RATED);
                getSupportLoaderManager().restartLoader(MovieLoader.INDEX_MOVIE_ID, null, mMoviesLoader);
            }
            return true;
        } else if (id == R.id.action_favorite) {
            if (mTmDbSorting != TMDbSorting.FAVORITE) {
                mMoviesLoader.setSorting(TMDbSorting.FAVORITE);
                getSupportLoaderManager().restartLoader(MovieLoader.INDEX_MOVIE_ID, null, mMoviesLoader);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(final int id) {
        Intent intentToStartDetailActivity = new Intent(this, MovieDetailsActivity.class);
        intentToStartDetailActivity.putExtra(VIEW_MOVIE_DETAILS, id);
        startActivity(intentToStartDetailActivity);
    }

    final MovieLoader mMoviesLoader = new MovieLoader(this) {
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                mTmDbMoviesAdapter.swapCursor(data);
                if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
                mRecyclerView.smoothScrollToPosition(mPosition);
                if (data.getCount() != 0) showMovies();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTmDbMoviesAdapter.swapCursor(null);
        }
    };

}
