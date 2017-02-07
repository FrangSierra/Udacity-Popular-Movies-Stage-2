package frangsierra.popularmoviesudacity.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.activity.DaggerCleanActivity;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import frangsierra.popularmoviesudacity.settings.SettingActivity;
import frangsierra.popularmoviesudacity.ui.adapter.MovieGridAdapter;
import frangsierra.popularmoviesudacity.ui.listener.EndlessRecyclerScrollListener;

import static frangsierra.popularmoviesudacity.data.MovieSorting.SORT_BY_FAVORITE;

interface MovieBrowserView {

   void disableLoadingControls();

   void setMovies(List<Movie> movies);

   void showLoadingError(int messageId);

   void updateMovieAsFavored(Long movieId, Boolean favored);

   void startMovieDetailActivity(List<Video> videos, List<Review> reviews, Movie movie);
}

/**
 * Main application activity, it start the movie load when the application is launched.
 */
public class MovieBrowserActivity extends DaggerCleanActivity<MovieBrowserPresenter, MovieBrowserView, MoviesComponent>
   implements MovieBrowserView, MovieGridAdapter.MovieAdapterListener,
   SharedPreferences.OnSharedPreferenceChangeListener {

   public static final String MOVIE_EXTRA = "INTENT_MOVIE_DETAIL";
   public static final String VIDEO_EXTRA = "INTENT_VIDEO_DETAIL";
   public static final String REVIEW_EXTRA = "INTENT_REVIEW_DETAIL";

   private static final int GRID_COLUMNS = 2;

   @Inject PopularMoviesRepository popularMoviesRepository;
   @BindView(R.id.movies_grid_view) RecyclerView moviesRecyclerGridView;
   @BindView(R.id.loading_progress_bar) ProgressBar loadingProgressBar;
   @BindView(R.id.error_text) TextView errorText;

   private MovieGridAdapter gridAdapter;
   private int pagesLoaded = 0;
   private boolean isLoading = false;

   @Inject
   public MovieBrowserActivity() {
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this, this);
      initializeRecycler();
      startMovieLoading();
      setupSharedPreferences();
   }

   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieBrowserModule(new MoviesComponent.MovieBrowserModule())
         .build();
   }

   /**
    * Initialize all the needed parameters of the {@link RecyclerView}.
    */
   private void initializeRecycler() {
      GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);
      moviesRecyclerGridView.setLayoutManager(gridLayoutManager);
      moviesRecyclerGridView.addOnScrollListener(new EndlessRecyclerScrollListener(gridLayoutManager) {
         @Override public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            //Scroll is not allowed when the filter is sorted by favorite
           if (!PreferenceManager
               .getDefaultSharedPreferences(MovieBrowserActivity.this)
               .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER).equals(SORT_BY_FAVORITE))
            startMovieLoading();
         }
      });
      gridAdapter = new MovieGridAdapter(MovieBrowserActivity.this, MovieBrowserActivity.this);
      moviesRecyclerGridView.setAdapter(gridAdapter);
   }

   @Override protected void onResume() {
      super.onResume();
      //If a used unselect a movie as favorite, the list should be updated when he come back to the main scree
      if (PreferenceManager
         .getDefaultSharedPreferences(MovieBrowserActivity.this)
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER).equals(SORT_BY_FAVORITE)) {
         clearData();
         startMovieLoading();
      }
   }

   private void setupSharedPreferences() {
      // Get all of the values from shared preferences to set it up
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      // Register the listener
      sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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

   @Override public void onMovieClick(int position) {
      Movie detailMovie = gridAdapter.getMovieFromPosition(position);
      getPresenter().loadMovieDetails(detailMovie);
   }

   @Override
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      clearData();
      startMovieLoading();
   }


   /**
    * Start the data loading and hide the views when the load is been processed.
    */
   public void startMovieLoading() {
      if (isLoading) {
         return;
      }

      loadingProgressBar.setVisibility(View.VISIBLE);
      moviesRecyclerGridView.setVisibility(View.INVISIBLE);

      isLoading = true;

      final @MovieSorting.MovieSortingValue String filter = PreferenceManager
         .getDefaultSharedPreferences(MovieBrowserActivity.this)
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER);

      getPresenter().loadMovieData(filter, pagesLoaded + 1);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
      PreferenceManager.getDefaultSharedPreferences(this)
         .unregisterOnSharedPreferenceChangeListener(this);
   }

   /**
    * Clear the adapter and reset the page index to 0.
    */
   private void clearData() {
      pagesLoaded = 0;
      gridAdapter.clear();
   }

   /**
    * Make the needed calls and show the right views when the load as finished.
    */
   public void disableLoadingControls() {
      if (!isLoading) {
         return;
      }

      isLoading = false;
      loadingProgressBar.setVisibility(View.INVISIBLE);
      moviesRecyclerGridView.setVisibility(View.VISIBLE);
      errorText.setVisibility(View.INVISIBLE);
   }

   @Override public void setMovies(List<Movie> movies) {
      gridAdapter.addMovies(movies);
      pagesLoaded++;
   }

   @Override public void showLoadingError(int messageId) {
      if (!isLoading) {
         return;
      }
      isLoading = false;

      errorText.setVisibility(View.VISIBLE);
      loadingProgressBar.setVisibility(View.INVISIBLE);
      errorText.setText(getString(messageId));
   }

   @Override public void updateMovieAsFavored(Long movieId, Boolean favored) {
      gridAdapter.updateMovieAsFavored(movieId, favored);
   }

   @Override
   public void startMovieDetailActivity(List<Video> videos, List<Review> reviews, Movie movie) {
      Intent intent = new Intent(this, MovieDetailActivity.class);
      intent.putExtra(MOVIE_EXTRA, movie);
      if (videos.size() > 0)
         intent.putParcelableArrayListExtra(VIDEO_EXTRA, new ArrayList<>(videos));
      if (reviews.size() > 0)
         intent.putParcelableArrayListExtra(REVIEW_EXTRA, new ArrayList<>(reviews));
      startActivity(intent);
   }

}
