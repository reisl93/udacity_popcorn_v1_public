package com.example.android.popcorn;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popcorn.data.TMDbMovie;
import com.example.android.popcorn.utils.MovieIntents;
import com.squareup.picasso.Picasso;

import static com.example.android.popcorn.data.DataUrlsHelper.getTMDbImageUri;

public class MovieDetailsActivity extends AppCompatActivity {
    private final static String TAG = MovieDetailsActivity.class.getSimpleName();

    @Nullable
    private TMDbMovie tmDbMovie;
    private ImageView mThumbnail;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mTitle;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mOverview = (TextView) findViewById(R.id.tv_overview);

        Intent calledByIntent = getIntent();
        if (calledByIntent.hasExtra(MovieIntents.VIEW_MOVIE_DETAILS)){
            tmDbMovie = calledByIntent.getParcelableExtra(MovieIntents.VIEW_MOVIE_DETAILS);
        }

        showDetails();
    }

    private void showDetails() {
        if (tmDbMovie != null) {
            mTitle.setText(tmDbMovie.getOriginal_title());
            mRating.setText(String.valueOf(tmDbMovie.getVote_average()));
            mReleaseDate.setText(tmDbMovie.getRelease_date());
            mOverview.setText(tmDbMovie.getOverview());

            Uri imageUri = getTMDbImageUri(tmDbMovie.getPoster_path());
            Log.d(TAG, "loading image " + imageUri);
            Picasso.with(this).load(imageUri).into(mThumbnail);
        }
    }
}
