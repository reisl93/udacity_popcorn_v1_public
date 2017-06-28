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
import android.util.Log;
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
    private RecyclerView mMoviesRecyclerView;
    private TMDbSorting mMoviesSorting = TMDbSorting.POPULAR;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    private MovieLoader mMoviesLoader;

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

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mTmDbMoviesAdapter);

        mMoviesLoader = new PostersMovieLoader(this);
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
        mMoviesSorting = sorting;
        mMoviesLoader.setSorting(mMoviesSorting);
        getSupportLoaderManager().restartLoader(MovieLoader.ID_MOVIES_LOADER, null, mMoviesLoader);
    }

    @Override
    public void onMovieClicked(final int id) {
        Intent intentToStartDetailActivity = new Intent(this, MovieDetailsActivity.class);
        intentToStartDetailActivity.putExtra(VIEW_MOVIE_DETAILS, id);
        startActivity(intentToStartDetailActivity);
    }

    final class PostersMovieLoader extends MovieLoader {
        public PostersMovieLoader(Context context) {
            super(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                mTmDbMoviesAdapter.swapCursor(data);
                if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
                mMoviesRecyclerView.smoothScrollToPosition(mPosition);
                if (data.getCount() != 0) showMovies();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTmDbMoviesAdapter.swapCursor(null);
        }
    };

}
