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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.DaggerCleanActivity;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import frangsierra.popularmoviesudacity.settings.SettingActivity;
import frangsierra.popularmoviesudacity.ui.adapter.MovieGridAdapter;
import frangsierra.popularmoviesudacity.ui.listener.EndlessRecyclerScrollListener;

/**
 * Main application activity, it start the movie load when the application is launched.
 */
public class MovieBrowserActivity extends DaggerCleanActivity<MovieBrowserPresenter, MovieBrowserView, MoviesComponent>
   implements MovieBrowserView, MovieGridAdapter.MovieAdapterListener,
   SharedPreferences.OnSharedPreferenceChangeListener {

   public static final String MOVIE_EXTRA = "INTENT_MOVIE_DETAIL";
   private static final int GRID_COLUMNS = 2;

   @Inject PopularMoviesRepository popularMoviesRepository;
   @BindView(R.id.movies_grid_view) RecyclerView mMoviesRecyclerGridView;
   @BindView(R.id.loading_progress_bar) ProgressBar mLoadingProgressBar;
   @BindView(R.id.error_text) TextView mErrorText;

   private MovieGridAdapter mGridAdapter;
   private int mPagesLoaded = 0;
   private boolean mIsLoading = false;

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
      mMoviesRecyclerGridView.setLayoutManager(gridLayoutManager);
      mMoviesRecyclerGridView.addOnScrollListener(new EndlessRecyclerScrollListener(gridLayoutManager) {
         @Override public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            startMovieLoading();
         }
      });
      mGridAdapter = new MovieGridAdapter(MovieBrowserActivity.this, MovieBrowserActivity.this);
      mMoviesRecyclerGridView.setAdapter(mGridAdapter);
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
      Movie detailMovie = mGridAdapter.getMovieFromPosition(position);
      Intent intent = new Intent(this, MovieDetailActivity.class);
      intent.putExtra(MOVIE_EXTRA, detailMovie);
      startActivity(intent);
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
      if (mIsLoading) {
         return;
      }

      mLoadingProgressBar.setVisibility(View.VISIBLE);
      mMoviesRecyclerGridView.setVisibility(View.INVISIBLE);

      mIsLoading = true;

      final @MovieSorting.MovieSortingValue String filter = PreferenceManager
         .getDefaultSharedPreferences(MovieBrowserActivity.this)
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER);

      getPresenter().loadMovieData(filter, mPagesLoaded + 1);
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
      mPagesLoaded = 0;
      mGridAdapter.clear();
   }

   /**
    * Make the needed calls and show the right views when the load as finished.
    */
   public void disableLoadingControls() {
      if (!mIsLoading) {
         return;
      }

      mIsLoading = false;
      mLoadingProgressBar.setVisibility(View.INVISIBLE);
      mMoviesRecyclerGridView.setVisibility(View.VISIBLE);
      mErrorText.setVisibility(View.INVISIBLE);
   }

   @Override public void setMovies(List<Movie> movies) {
      mGridAdapter.addMovies(movies);
      mPagesLoaded++;
   }

   @Override public void showLoadingError() {
      mErrorText.setVisibility(View.VISIBLE);
   }

   @Override public void updateMovieAsFavored(Long movieId, Boolean favored) {
      mGridAdapter.updateMovieAsFavored(movieId, favored);
   }

}


interface MovieBrowserView {

   void disableLoadingControls();

   void setMovies(List<Movie> movies);

   void showLoadingError();

   void updateMovieAsFavored(Long movieId, Boolean favored);
}
