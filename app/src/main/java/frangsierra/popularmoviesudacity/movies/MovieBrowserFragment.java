package frangsierra.popularmoviesudacity.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.fragment.DaggerCleanFragment;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import frangsierra.popularmoviesudacity.ui.adapter.MovieGridAdapter;
import frangsierra.popularmoviesudacity.ui.listener.EndlessRecyclerScrollListener;

import static frangsierra.popularmoviesudacity.data.MovieSorting.SORT_BY_FAVORITE;


public class MovieBrowserFragment extends DaggerCleanFragment<MovieBrowserPresenter, MovieBrowserView, MoviesComponent> implements SharedPreferences.OnSharedPreferenceChangeListener, MovieGridAdapter.MovieAdapterListener, MovieBrowserView {

   public static final String BUNDLE_EXTRA = "BUNDLE_DETAIL";
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
   public MovieBrowserFragment() {
   }

   @Nullable @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.activity_detail, container, false);
   }

   @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      ButterKnife.bind(this, getActivity());
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


   public static MovieBrowserFragment newInstance() {
      return new MovieBrowserFragment();
   }

   /**
    * Initialize all the needed parameters of the {@link RecyclerView}.
    */
   private void initializeRecycler() {
      GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMNS);
      moviesRecyclerGridView.setLayoutManager(gridLayoutManager);
      moviesRecyclerGridView.addOnScrollListener(new EndlessRecyclerScrollListener(gridLayoutManager) {
         @Override public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            //Scroll is not allowed when the filter is sorted by favorite
            if (!PreferenceManager
               .getDefaultSharedPreferences(getActivity())
               .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER).equals(SORT_BY_FAVORITE))
               startMovieLoading();
         }
      });
      gridAdapter = new MovieGridAdapter(MovieBrowserFragment.this, getActivity());
      moviesRecyclerGridView.setAdapter(gridAdapter);
   }

   @Override public void onResume() {
      super.onResume();
      //If a used unselect a movie as favorite, the list should be updated when he come back to the main scree
      if (PreferenceManager
         .getDefaultSharedPreferences(getActivity())
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER).equals(SORT_BY_FAVORITE)) {
         clearData();
         startMovieLoading();
      }
   }

   private void setupSharedPreferences() {
      // Get all of the values from shared preferences to set it up
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
      // Register the listener
      sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
         .getDefaultSharedPreferences(getActivity())
         .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER);

      getPresenter().loadMovieData(filter, pagesLoaded + 1);
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
      PreferenceManager.getDefaultSharedPreferences(getActivity())
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
      ((BrowserActivity) getActivity()).startMovieDetailActivity(videos, reviews, movie);
   }
}
