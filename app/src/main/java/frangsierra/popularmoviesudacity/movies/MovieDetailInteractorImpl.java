package frangsierra.popularmoviesudacity.movies;

import android.util.Pair;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import io.reactivex.Completable;
import io.reactivex.processors.PublishProcessor;

interface MovieDetailInteractor {

   PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor();

   Completable setMovieFavored(Movie movie, boolean favored);
}

/**
 * Interactor class for {@link MovieDetailActivity}. It is in charge of communicate the repository with
 * the presenter.
 */
public class MovieDetailInteractorImpl implements MovieDetailInteractor {
   private PopularMoviesRepository repository;

   @Inject public MovieDetailInteractorImpl(PopularMoviesRepository repository) {
      this.repository = repository;
   }

   @Override public PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor() {
      return repository.getFavoredProcessor();
   }

   @Override public Completable setMovieFavored(Movie movie, boolean favored) {
      movie.setFavMovie(favored);
      return favored ? repository.saveMovie(movie) : repository.deleteMovie(movie);
   }
}
