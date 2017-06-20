package com.example.android.popcorn.movie;

import com.example.android.popcorn.data.TMDbMovie;

/**
 * Created by Rupert on 20.06.2017.
 */
public interface MovieClickedListener {
    void onMovieClicked(final TMDbMovie clickedPosition);
}
