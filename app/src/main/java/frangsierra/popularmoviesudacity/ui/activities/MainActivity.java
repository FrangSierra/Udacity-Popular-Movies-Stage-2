package frangsierra.popularmoviesudacity.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.data.Movie;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.MovieSorting.MovieSortingValue;
import frangsierra.popularmoviesudacity.ui.adapter.MovieGridAdapter;
import frangsierra.popularmoviesudacity.ui.listener.EndlessRecyclerScrollListener;
import frangsierra.popularmoviesudacity.utils.NetworkUtils;

import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchMoviesFromJson;
import static frangsierra.popularmoviesudacity.utils.NetworkUtils.getResponseFromHttpUrl;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieAdapterListener {
   private final String TAG = this.getClass().getSimpleName();
   private static final int GRID_COLUMNS = 2;
   public static final String MOVIE_EXTRA = "INTENT_MOVIE_DETAIL";

   private RecyclerView mMoviesRecyclerGridView;
   private ProgressBar mLoadingProgressBar;
   private MovieGridAdapter mGridAdapter;
   private int mPagesLoaded = 0;
   private boolean mIsLoading = false;
   private TextView mErrorText;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      initializeRecycler();
      mLoadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
      mErrorText = (TextView) findViewById(R.id.error_text);
      initializeRecycler();
      loadMovieData();
   }

   /**
    * Initialize all the needed parameters of the {@link RecyclerView}.
    */
   private void initializeRecycler() {
      mMoviesRecyclerGridView = (RecyclerView) findViewById(R.id.movies_grid_view);
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

   @Override public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
   }

   @Override public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.filter_by_popular:
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.pref_sorting_key), MovieSorting.SORT_BY_POPULAR).apply();
            clearData();
            loadMovieData();
            break;
         case R.id.filter_by_rating:
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.pref_sorting_key), MovieSorting.SORT_BY_TOP_RATED).apply();
            clearData();
            loadMovieData();
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

   /**
    * {@link AsyncTask} used to make a Http call in a background thread and retrieve from our movie API a JSON string
    * object which contains 20 movies per page.
    */
   private class MovieDataRetrieveTask extends AsyncTask<Integer, Void, List<Movie>> {

      @Override protected List<Movie> doInBackground(Integer... params) {
         if (params.length == 0) {
            return null;
         }

         int page = params[0];
         final @MovieSortingValue String filter = PreferenceManager
            .getDefaultSharedPreferences(MainActivity.this)
            .getString(getString(R.string.pref_sorting_key), MovieSorting.DEFAULT_FILTER);
         final URL url = NetworkUtils.buildUrl(filter, page);
         try {
            return fetchMoviesFromJson(getResponseFromHttpUrl(url));
         } catch (IOException | JSONException e) {
            Log.d(TAG, "Can't parse JSON: " + e);
         }
         return null;
      }

      @Override protected void onPostExecute(List<Movie> movies) {
         if (movies != null) {
            mGridAdapter.addMovies(movies);
            mPagesLoaded++;
         } else {
            mErrorText.setVisibility(View.VISIBLE);
         }
         stopLoading();
      }
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
      new MovieDataRetrieveTask().execute(mPagesLoaded + 1);
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
