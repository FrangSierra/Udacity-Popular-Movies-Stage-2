package frangsierra.popularmoviesudacity.movies;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import frangsierra.popularmoviesudacity.core.ui.fragment.DaggerCleanFragment;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;


public class MovieDetailFragment extends DaggerCleanFragment<MovieDetailPresenter, MovieDetailView, MoviesComponent> {

   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieBrowserModule(new MoviesComponent.MovieBrowserModule())
         .build();
   }

   public static MovieDetailFragment newInstance(Movie movie, List<Video> videos, List<Review> reviews) {
      MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

      Bundle bundle = new Bundle();
      bundle.putParcelable(MovieBrowserActivity.MOVIE_EXTRA, movie);

      if (videos.size() > 0)
         bundle.putParcelableArrayList(MovieBrowserActivity.VIDEO_EXTRA, new ArrayList<>(videos));
      if (reviews.size() > 0)
         bundle.putParcelableArrayList(MovieBrowserActivity.REVIEW_EXTRA, new ArrayList<>(reviews));

      movieDetailFragment.setArguments(bundle);
      return movieDetailFragment;
   }
}
