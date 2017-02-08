package frangsierra.popularmoviesudacity.data.repository;

import android.content.ContentProvider;
import android.util.Pair;

import java.util.List;
import java.util.Set;

import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

/**
 * Interface used by {@link PopularMoviesRepositoryImpl}.
 */
public interface PopularMoviesRepository {

   /**
    * {@link Single} used to make a Http call in a background thread and retrieve from our movie API a JSON string
    * object which contains 20 movies per page.
    */
   Single<List<Movie>> retrieveMovies(@MovieSorting.MovieSortingValue String sorting, int page);

   /**
    * {@link Single} used to make a Http call in a background thread and retrieve from our movie API a JSON string
    * object which contains 20 movies per page.
    */
   Single<Set<Long>> savedMovieIds();

   /**
    * Completable which contains a call to the app {@link ContentProvider} looking to add a new movie.
    */
   Completable saveMovie(Movie movie);

   /**
    * Completable which contains a call to the app {@link ContentProvider} looking to delete a specific movie.
    */
   Completable deleteMovie(Movie movie);

   /**
    * {@link Single} used to make a Http call in a background thread and retrieve from our movie API with a specific
    * movieId string, a JSON string object which all the {@link Video videos} associated to the given movie.
    */
   Single<List<Video>> videos(long movieId);

   /**
    * {@link Single} used to make a Http call in a background thread and retrieve from our movie API with a specific
    * movieId string, a JSON string object which all the {@link Review reviews} associated to the given movie.
    */
   Single<List<Review>> reviews(long movieId);

   /**
    * Publish processor used to notify every change about movies which have been stored as favored.
    */
   PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor();
}
