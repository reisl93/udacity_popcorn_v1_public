package com.example.android.popcorn.movie;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popcorn.R;
import com.example.android.popcorn.data.TMDbMovie;
import com.example.android.popcorn.data.TMDbMovies;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import static com.example.android.popcorn.data.DataUrlsHelper.getTMDbImageUri;

/**
 * Created by Rupert on 20.06.2017.
 */
public class TMDbAdapter extends RecyclerView.Adapter<TMDbAdapter.MoviesViewHolder> {
    private final static String TAG = TMDbAdapter.class.getSimpleName();

    @Nullable
    private TMDbMovie[] tmDbMovies = null;
    private final MovieClickedListener mClickOnListener;

    public TMDbAdapter(final MovieClickedListener clickHandler) {
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
        if (null == tmDbMovies) return 0;
        return tmDbMovies.length;
    }

    public void setMovies(final TMDbMovie[] tmDbMovies) {
        Log.d(TAG, "Refreshing movies data");
        this.tmDbMovies = tmDbMovies;
        notifyDataSetChanged();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mPoster;
        private final Context mContext;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(final int itemIndex) {
            Log.d(TAG, "loading image for itemIndex " + itemIndex);
            final String posterPath;
            if (tmDbMovies != null) {
                posterPath = tmDbMovies[itemIndex].getPoster_path();
                Uri imageUri = getTMDbImageUri(posterPath);
                Log.d(TAG, "loading image " + imageUri);
                Picasso.with(mContext).load(imageUri).into(mPoster);
            }

        }

        @Override
        public void onClick(View v) {
            final int clickedPosition = getAdapterPosition();
            if (tmDbMovies != null) {
                mClickOnListener.onMovieClicked(tmDbMovies[clickedPosition]);
            }
        }
    }
}
