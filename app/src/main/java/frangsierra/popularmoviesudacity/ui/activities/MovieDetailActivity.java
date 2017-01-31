package frangsierra.popularmoviesudacity.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MainActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {
   @BindView(R.id.movie_title) TextView mTitleText;
   @BindView(R.id.movie_rating) TextView mRatingText;
   @BindView(R.id.movie_overview) TextView mOverviewText;
   @BindView(R.id.movie_release_date) TextView mReleaseDateText;
   @BindView(R.id.movie_poster) ImageView mMoviePoster;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      ButterKnife.bind(this, this);
      Intent intent = getIntent();
      if (intent != null && intent.hasExtra(MainActivity.MOVIE_EXTRA)) {
         Movie movie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
         if (movie == null) return;
         mTitleText.setText(movie.getTitle());
         final String ratingText = getString(R.string.rating_text) + movie.getRating();
         mRatingText.setText(ratingText);
         mOverviewText.setText(movie.getOverview());
         mReleaseDateText.setText(movie.getRelease_date());

         Picasso.with(this)
            .load(MovieUtils.buildPosterUri(movie.getPoster_path()))
            .into(mMoviePoster);
         setTitle(movie.getTitle());
      }
   }
}
