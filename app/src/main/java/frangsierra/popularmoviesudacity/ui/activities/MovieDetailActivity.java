package frangsierra.popularmoviesudacity.ui.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.Movie;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MainActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      Intent intent = getIntent();
      if (intent != null && intent.hasExtra(MainActivity.MOVIE_EXTRA)) {
         Movie movie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
         if (movie == null) return;

         final TextView titleTextView = (TextView) findViewById(R.id.movie_title);
         titleTextView.setText(movie.getTitle());

         final TextView ratingTextView = (TextView) findViewById(R.id.movie_rating);
         final String ratingText = getString(R.string.rating_text) + movie.getRating();
         ratingTextView.setText(ratingText);

         final TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);
         overviewTextView.setText(movie.getOverview());

         final TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
         releaseDateTextView.setText(movie.getRelease_date());

         Uri posterUri = MovieUtils.buildPosterUri(movie.getPoster_path());
         Picasso.with(this)
            .load(posterUri)
            .into((ImageView) findViewById(R.id.movie_poster));

         setTitle(movie.getTitle());
      }
   }
}
