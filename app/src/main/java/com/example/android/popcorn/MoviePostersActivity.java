package com.example.android.popcorn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.popcorn.data.FetchMoviesTask;
import com.example.android.popcorn.data.TMDbMovie;
import com.example.android.popcorn.data.TMDbMovies;
import com.example.android.popcorn.data.TMDbSorting;
import com.example.android.popcorn.movie.TMDbAdapter;
import com.example.android.popcorn.movie.MovieClickedListener;
import com.example.android.popcorn.utils.AsyncCallback;

import static com.example.android.popcorn.utils.MovieIntents.VIEW_MOVIE_DETAILS;

public class MoviePostersActivity extends AppCompatActivity implements MovieClickedListener, AsyncCallback<TMDbMovies> {
    private static final String TAG = MoviePostersActivity.class.getSimpleName();

    private TMDbAdapter mTmDbAdapter;
    private RecyclerView mRecyclerView;
    private TMDbSorting mTmDbSorting = TMDbSorting.POPULAR;
    private TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);

        final GridLayoutManager layoutManager
                = new GridLayoutManager(this, 3 /* JUST A RANDOM VALUE */, LinearLayoutManager.VERTICAL, false);
        mTmDbAdapter = new TMDbAdapter(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mErrorTextView = (TextView) findViewById(R.id.tv_movie_posters_error);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTmDbAdapter);

        loadMoviesData();
    }

    private void loadMoviesData() {
        showMovies();
        new FetchMoviesTask(this).execute(mTmDbSorting);
    }

    private void showMovies() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void asyncCallback(final TMDbMovies data) {
        Log.d(TAG, "Loaded movies data");
        if (data != null){
            showMovies();
            mTmDbAdapter.setMovies(data.getResults());
        } else {
            showErrorMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movie_poster, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            if (mTmDbSorting != TMDbSorting.POPULAR) {
                mTmDbSorting = TMDbSorting.POPULAR;
                loadMoviesData();
            }
            return true;
        }

        // COMPLETED (2) Launch the map when the map menu item is clicked
        if (id == R.id.action_top_rated) {
            if (mTmDbSorting != TMDbSorting.TOP_RATED) {
                mTmDbSorting = TMDbSorting.TOP_RATED;
                loadMoviesData();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(final TMDbMovie clickedMovie) {
        Context context = this;
        Class destinationClass = MovieDetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(VIEW_MOVIE_DETAILS, clickedMovie);
        startActivity(intentToStartDetailActivity);
    }
}
