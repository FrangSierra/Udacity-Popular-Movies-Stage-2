package frangsierra.popularmoviesudacity.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.DaggerAppComponent;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.MovieSorting.MovieSortingValue;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import frangsierra.popularmoviesudacity.ui.adapter.MovieGridAdapter;
import frangsierra.popularmoviesudacity.ui.listener.EndlessRecyclerScrollListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieAdapterListener, SharedPreferences.OnSharedPreferenceChangeListener {
   private static final int GRID_COLUMNS = 2;
   public static final String MOVIE_EXTRA = "INTENT_MOVIE_DETAIL";
   @Inject PopularMoviesRepository popularMoviesRepository;
   @BindView(R.id.movies_grid_view) RecyclerView mMoviesRecyclerGridView;
   @BindView(R.id.loading_progress_bar) ProgressBar mLoadingProgressBar;
   @BindView(R.id.error_text) TextView mErrorText;
   private MovieGridAdapter mGridAdapter;
   private int mPagesLoaded = 0;
   private boolean mIsLoading = false;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this, this);
      DaggerAppComponent.builder().build().inject(this);
      initializeRecycler();
      loadMovieData();
      setupSharedPreferences();
   }

   /**
    * Initialize all the needed parameters of the {@link RecyclerView}.
    */
   private void initializeRecycler() {
      GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);
      mMoviesRecyclerGridView.setLayoutManager(gridLayoutManager);
      mMoviesRecyclerGridView.addOnScrollListener(new EndlessRecyclerScrollListener(gridLayoutManager) {
         @Override public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            loadMovieData();
         }
      });
      mGridAdapter = new MovieGridAdapter(MainActivity.this, MainActivity.this);
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

   @Override public void onFavClick(int position) {
      //TODO manage favs
      mGridAdapter.getMovieFromPosition(position).getId();
      mGridAdapter.notifyItemChanged(position);
   }

   @Override
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      clearData();
      loadMovieData();
   }


   /**
    * Start the data loading and hide the views when the load is been processed.
    */
   private void loadMovieData() {
      if (mIsLoading) {
         return;
      }

      mLoadingProgressBar.setVisibility(View.VISIBLE);
      mMoviesRecyclerGridView.setVisibility(View.INVISIBLE);

      mIsLoading = true;

      final @MovieSortingValue String filter = PreferenceManager
         .getDefaultSharedPreferences(MainActivity.this)
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER);

      popularMoviesRepository.retrieveMovies(filter, mPagesLoaded + 1)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(movies -> {
            if (movies != null) {
               mGridAdapter.addMovies(movies);
               mPagesLoaded++;
            } else {
               mErrorText.setVisibility(View.VISIBLE);
            }
            stopLoading();
         });
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
   private void stopLoading() {
      if (!mIsLoading) {
         return;
      }

      mIsLoading = false;
      mLoadingProgressBar.setVisibility(View.INVISIBLE);
      mMoviesRecyclerGridView.setVisibility(View.VISIBLE);
      mErrorText.setVisibility(View.INVISIBLE);
   }
}
