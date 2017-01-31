package frangsierra.popularmoviesudacity.data.repository;

import java.util.List;
import java.util.Set;

import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Durdin on 31/01/2017.
 */

public interface PopularMoviesRepository {

   /**
    * {@link Single} used to make a Http call in a background thread and retrieve from our movie API a JSON string
    * object which contains 20 movies per page.
    */
   Single<List<Movie>> retrieveMovies(@MovieSorting.MovieSortingValue String sorting, int page);

   Observable<List<Movie>> savedMovies();

   Observable<Set<Long>> savedMovieIds();

   void saveMovie(Movie movie);

   void deleteMovie(Movie movie);

   Single<List<Video>> videos(long movieId);

   Single<List<Review>> reviews(long movieId);
}
