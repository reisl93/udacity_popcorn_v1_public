package com.example.android.popcorn.movie;

import com.example.android.popcorn.data.json.TMDbMovie;

/**
 * Created by Rupert on 20.06.2017.
 */
public interface MovieClickedListener {
    void onMovieClicked(final int movieId);
}
