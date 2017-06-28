package com.example.android.popcorn.activities.posters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popcorn.R;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import static com.example.android.popcorn.utils.DataUrlsHelper.getTMDbImageUri;

public class TMDbMoviesAdapter extends RecyclerView.Adapter<TMDbMoviesAdapter.MoviesViewHolder> {
    private final static String TAG = TMDbMoviesAdapter.class.getSimpleName();

    @Nullable
    private Cursor tmDbMoviesCursor = null;
    private final MovieClickedListener mClickOnListener;

    public TMDbMoviesAdapter(final MovieClickedListener clickHandler) {
        mClickOnListener = clickHandler;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder tmdbAdapterViewHolder, final int position) {
        tmdbAdapterViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == tmDbMoviesCursor) return 0;
        return tmDbMoviesCursor.getCount();
    }

    public void swapCursor(final Cursor tmDbMovies) {
        Log.d(TAG, "Refreshing movies data");
        this.tmDbMoviesCursor = tmDbMovies;
        notifyDataSetChanged();
    }


    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mPoster;
        private final Context mContext;

        MoviesViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        void bind(final int itemIndex) {
            Log.d(TAG, "loading image for itemIndex " + itemIndex);
            final String posterPath;
            if (tmDbMoviesCursor != null && tmDbMoviesCursor.moveToPosition(itemIndex)) {
                posterPath = tmDbMoviesCursor.getString(MovieLoader.INDEX_MOVIE_POSTER_PATH);
                Uri imageUri = getTMDbImageUri(posterPath);
                Log.d(TAG, "loading image " + imageUri);
                Picasso.with(mContext).load(imageUri).into(mPoster);
            }
        }

        @Override
        public void onClick(View v) {
            final int clickedPosition = getAdapterPosition();
            if (tmDbMoviesCursor != null && tmDbMoviesCursor.moveToPosition(clickedPosition)) {
                int movieId = tmDbMoviesCursor.getInt(MovieLoader.INDEX_MOVIE_ID);
                Log.d(TAG, "clicked on movie id: " + movieId);
                mClickOnListener.onMovieClicked(movieId);
            }
        }
    }
}
