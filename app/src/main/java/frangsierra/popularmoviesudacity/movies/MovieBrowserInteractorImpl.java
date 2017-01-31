package frangsierra.popularmoviesudacity.movies;

import java.util.List;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import io.reactivex.Completable;
import io.reactivex.Single;


public class MovieBrowserInteractorImpl implements MovieBrowserInteractor {
   private PopularMoviesRepository repository;

   @Inject
   public MovieBrowserInteractorImpl(PopularMoviesRepository repository) {
      this.repository = repository;
   }

   @Override public Single<List<Movie>> retrieveMovies(String filter, int page) {
      return repository.retrieveMovies(filter, page);
   }

   @Override public Completable addMoviewToFavorites(Movie movie) {
      return null;
   }
}

interface MovieBrowserInteractor {

   Single<List<Movie>> retrieveMovies(String filter, int page);

   Completable addMoviewToFavorites(Movie movie);
}
