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

public class TMDbReviewsAdapter extends RecyclerView.Adapter<TMDbReviewsAdapter.ReviewsViewHolder> {
    private final static String TAG = TMDbReviewsAdapter.class.getSimpleName();

    @Nullable
    private Cursor tmDbReviewsCursor = null;

    @Override
    public ReviewsViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        return new ReviewsViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.movie_detail_review_list_item, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(final ReviewsViewHolder tmdbAdapterViewHolder, final int position) {
        tmdbAdapterViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == tmDbReviewsCursor) return 0;
        return tmDbReviewsCursor.getCount();
    }

    public void swapCursor(final Cursor tmDbMovies) {
        Log.d(TAG, "Refreshing reviews data");
        this.tmDbReviewsCursor = tmDbMovies;
        notifyDataSetChanged();
    }


    class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTitle;
        private final TextView mContent;

        ReviewsViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_review_title);
            mContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        public void bind(final int itemIndex) {
            Log.d(TAG, "binding text for itemIndex " + itemIndex);
            if (tmDbReviewsCursor != null && tmDbReviewsCursor.moveToPosition(itemIndex)) {
                mTitle.setText(tmDbReviewsCursor.getString(ReviewsLoader.INDEX_REVIEW_AUTHOR));
                mContent.setText(tmDbReviewsCursor.getString(ReviewsLoader.INDEX_REVIEW_CONTENT));
            }
        }

        @Override
        public void onClick(View v) {
            switch (mContent.getVisibility()){
                case View.GONE:
                    mContent.setVisibility(View.VISIBLE);
                    break;
                case View.VISIBLE:
                    mContent.setVisibility(View.GONE);
                    break;
                case View.INVISIBLE:
                default:
                    Log.w(TAG, "Content is visible with value: " + mContent.getVisibility() + ". This shouldn't happen!");
            }
        }
    }
}
