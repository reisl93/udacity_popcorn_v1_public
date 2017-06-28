package com.example.android.popcorn.activities.details;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popcorn.R;

import javax.annotation.Nullable;

public class TMDbTrailersAdapter extends RecyclerView.Adapter<TMDbTrailersAdapter.TrailersViewHolder> {
    private final static String TAG = TMDbTrailersAdapter.class.getSimpleName();

    @Nullable
    private Cursor tmDbTrailersCursor = null;
    private final TrailerClickedListener mClickOnListener;

    public TMDbTrailersAdapter(final TrailerClickedListener clickHandler) {
        mClickOnListener = clickHandler;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        return new TrailersViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.movie_detail_trailer_list_item, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(final TrailersViewHolder tmdbAdapterViewHolder, final int position) {
        tmdbAdapterViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == tmDbTrailersCursor) return 0;
        return tmDbTrailersCursor.getCount();
    }

    public void swapCursor(final Cursor tmDbMovies) {
        Log.d(TAG, "Refreshing trailers data");
        this.tmDbTrailersCursor = tmDbMovies;
        notifyDataSetChanged();
    }


    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTitle;

        TrailersViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        void bind(final int itemIndex) {
            Log.d(TAG, "binding text for itemIndex " + itemIndex);
            if (tmDbTrailersCursor != null && tmDbTrailersCursor.moveToPosition(itemIndex)) {
                final String videoTitle = tmDbTrailersCursor.getString(TrailersLoader.INDEX_TRAILER_VIDEO_TITLE);
                mTitle.setText(videoTitle);
            }
        }

        @Override
        public void onClick(View v) {
            final int clickedPosition = getAdapterPosition();
            if (tmDbTrailersCursor != null && tmDbTrailersCursor.moveToPosition(clickedPosition)) {
                mClickOnListener.onTrailerClicked(
                        tmDbTrailersCursor.getString(TrailersLoader.INDEX_TRAILER_WEBSITE),
                        tmDbTrailersCursor.getString(TrailersLoader.INDEX_TRAILER_VIDEO_KEY));
            }
        }
    }
}
