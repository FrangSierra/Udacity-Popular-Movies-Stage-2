package frangsierra.popularmoviesudacity.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.activity.BaseActivity;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.settings.SettingActivity;


public class BrowserActivity extends BaseActivity implements BrowserView {

   private static final String MOVIES_FRAGMENT_TAG = "fragment_movies";
   private static final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_movie_details";
   private boolean tabletMode;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_browser);

      tabletMode = findViewById(R.id.movie_details_container) != null;

     setBrowserFragment(MovieBrowserFragment.newInstance());
   }

   @Override public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
   }

   @Override public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.settings:
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void startMovieDetailActivity(List<Video> videos, List<Review> reviews, Movie movie) {

      if (tabletMode) {
         setMovieDetailsFragment(MovieDetailFragment.newInstance(movie, videos, reviews));
      } else {
         Intent intent = new Intent(this, MovieDetailActivity.class);
         Bundle bundle = new Bundle();
         bundle.putParcelable(MovieBrowserActivity.MOVIE_EXTRA, movie);
         if (videos.size() > 0)
            bundle.putParcelableArrayList(MovieBrowserActivity.VIDEO_EXTRA, new ArrayList<>(videos));
         if (reviews.size() > 0)
            bundle.putParcelableArrayList(MovieBrowserActivity.REVIEW_EXTRA, new ArrayList<>(reviews));
         intent.putExtra(MovieBrowserActivity.BUNDLE_EXTRA, bundle);
         startActivity(intent);
      }
   }

   private void setMovieDetailsFragment(MovieDetailFragment fragment) {
      getSupportFragmentManager().beginTransaction()
         .replace(R.id.movie_details_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
         .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
         .commit();
   }

   private void setBrowserFragment(MovieBrowserFragment fragment) {
      getSupportFragmentManager().beginTransaction()
         .replace(R.id.movies_container, fragment, MOVIES_FRAGMENT_TAG)
         .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
         .commit();
   }
}

interface BrowserView {

   void startMovieDetailActivity(List<Video> videos, List<Review> reviews, Movie movie);
}