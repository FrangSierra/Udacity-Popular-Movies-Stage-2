package frangsierra.popularmoviesudacity.movies;

import android.util.Pair;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

/**
 * Interactor class for {@link MovieBrowserActivity}. It is in charge of communicate the repository with
 * the presenter.
 */
public class MovieBrowserInteractorImpl implements MovieBrowserInteractor {
   private PopularMoviesRepository repository;

   @Inject
   public MovieBrowserInteractorImpl(PopularMoviesRepository repository) {
      this.repository = repository;
   }

   @Override public Single<List<Movie>> retrieveMovies(String filter, int page) {
      return repository.retrieveMovies(filter, page);
   }

   @Override public Completable addMovieToFavorites(Movie movie) {
      return null;
   }

   @Override public Single<Set<Long>> getSavedMoviesId() {
      return repository.savedMovieIds();
   }

   @Override public PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor() {
      return repository.getFavoredProcessor();
   }
}

interface MovieBrowserInteractor {

   Single<List<Movie>> retrieveMovies(String filter, int page);

   Completable addMovieToFavorites(Movie movie);

   Single<Set<Long>> getSavedMoviesId();

   PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor();

}
