package frangsierra.popularmoviesudacity.movies;

import android.util.Pair;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

interface MovieBrowserInteractor {

   Single<List<Movie>> retrieveMovies(String filter, int page);

   Single<List<Video>> retrieveVideosFromMovie(Long movieId);

   Single<List<Review>> retrieveReviewsFromMovie(Long movieId);

   Single<Set<Long>> getSavedMoviesId();

   PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor();

}

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

   @Override
   public Single<List<Video>> retrieveVideosFromMovie(Long movieId) {
      return repository.videos(movieId);
   }

   @Override
   public Single<List<Review>> retrieveReviewsFromMovie(Long movieId) {
      return repository.reviews(movieId);
   }

   @Override public Single<Set<Long>> getSavedMoviesId() {
      return repository.savedMovieIds();
   }

   @Override public PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor() {
      return repository.getFavoredProcessor();
   }
}
