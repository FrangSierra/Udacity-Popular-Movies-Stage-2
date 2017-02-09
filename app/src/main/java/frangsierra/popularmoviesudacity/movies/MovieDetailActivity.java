package frangsierra.popularmoviesudacity.movies;


import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.activity.BaseActivity;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;

import static frangsierra.popularmoviesudacity.movies.BrowserActivity.MOVIE_DETAILS_FRAGMENT_TAG;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.MOVIE_EXTRA;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.REVIEW_EXTRA;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.VIDEO_EXTRA;


/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MovieDetailFragment}.
 */
public class MovieDetailActivity extends BaseActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_movie_details);

      Intent intent = getIntent();

      if (intent == null || !intent.hasExtra(MOVIE_EXTRA)) {
         throw new NullPointerException("Movie can't be null");
      }

      if (savedInstanceState == null) {
         Movie movie = intent.getParcelableExtra(MOVIE_EXTRA);
         ArrayList<Video> videos = intent.getParcelableArrayListExtra(VIDEO_EXTRA);
         ArrayList<Review> reviews = intent.getParcelableArrayListExtra(REVIEW_EXTRA);

         MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie, videos, reviews);
         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.movie_detail_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
                 .commit();
      }
   }
}