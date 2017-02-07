package frangsierra.popularmoviesudacity.movies;

import frangsierra.popularmoviesudacity.core.ui.fragment.DaggerCleanFragment;


public class MovieBrowserFragment extends DaggerCleanFragment<MovieBrowserPresenter, MovieBrowserView, MoviesComponent> {
   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieBrowserModule(new MoviesComponent.MovieBrowserModule())
         .build();
   }

   public static MovieBrowserFragment newInstance() {
      return new MovieBrowserFragment();
   }
}
