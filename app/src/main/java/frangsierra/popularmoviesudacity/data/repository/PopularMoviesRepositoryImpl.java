package frangsierra.popularmoviesudacity.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract;
import frangsierra.popularmoviesudacity.utils.NetworkUtils;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.processors.PublishProcessor;

import static frangsierra.popularmoviesudacity.data.MovieSorting.SORT_BY_FAVORITE;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_ID;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.CONTENT_URI;
import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchMoviesFromJson;
import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchReviewsFromMovieJson;
import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchVideosFromMovieJson;
import static frangsierra.popularmoviesudacity.utils.MovieUtils.getMovieValues;
import static frangsierra.popularmoviesudacity.utils.NetworkUtils.getResponseFromHttpUrl;

/**
 * Implementation for the {@link PopularMoviesRepository} interface. It contains all the methods
 * associated to work together with application data.
 */
public class PopularMoviesRepositoryImpl implements PopularMoviesRepository {
   private final String[] movieIdsProjection = new String[]{
      MovieDatabaseContract.Movies.COLUMN_MOVIE_ID
   };

   private final String[] movieProjection = new String[]{
      MovieDatabaseContract.Movies.COLUMN_MOVIE_ID,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_TITLE,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_OVERVIEW,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_COUNT,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_AVERAGE,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_RELEASE_DATE,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_FAVORED,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_POSTER_PATH,
      MovieDatabaseContract.Movies.COLUMN_MOVIE_BACKDROP_PATH,
   };

   private final PublishProcessor<Pair<Long, Boolean>> favoredProcessor = PublishProcessor.create();
   private ContentResolver contentResolver;

   @Inject public PopularMoviesRepositoryImpl(ContentResolver contentResolver) {
      this.contentResolver = contentResolver;
   }

   @Override
   public Single<List<Movie>> retrieveMovies(@MovieSorting.MovieSortingValue String sorting, int page) {
      if (sorting.equals(SORT_BY_FAVORITE)) {
         return Single.create((SingleEmitter<List<Movie>> e) -> {
            List<Movie> movies = new ArrayList<Movie>();
            final Cursor query = contentResolver.query(CONTENT_URI, movieProjection, null, null, null);
            if (query.moveToFirst()) {
               do {
                  movies.add(Movie.fromCursor(query));
               } while (query.moveToNext());
            }
            e.onSuccess(movies);
         });
      } else {
         return Single.create(e -> {
            final URL url = NetworkUtils.buildMovieUrl(sorting, page);
            try {
               final List<Movie> movies = fetchMoviesFromJson(getResponseFromHttpUrl(url));
               e.onSuccess(movies);
            } catch (IOException | JSONException ex) {
               e.onError(ex);
            }
         });
      }
   }

   @Override public Single<Set<Long>> savedMovieIds() {
      return Single.create(e -> {
         Set<Long> idSet = new HashSet<>();
         final Cursor query = contentResolver.query(CONTENT_URI, movieIdsProjection, null, null, null);
         if (query.moveToFirst()) {
            do {
               idSet.add(query.getLong(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_ID)));
            } while (query.moveToNext());
         }
         e.onSuccess(idSet);
      });
   }

   @Override public Completable saveMovie(Movie movie) {
      return Completable.create(e -> {
         final ContentValues movieValues = getMovieValues(movie);
         contentResolver.insert(CONTENT_URI, movieValues);
         e.onComplete();
      }).doOnComplete(() -> favoredProcessor.onNext(new Pair<>(movie.getId(), true)));
   }

   @Override public Completable deleteMovie(Movie movie) {
      return Completable.create(e -> {
         final String where = String.format("%s=?", COLUMN_MOVIE_ID);
         final String[] args = new String[]{String.valueOf(movie.getId())};
         contentResolver.delete(CONTENT_URI, where, args);
         e.onComplete();
      }).doOnComplete(() -> favoredProcessor.onNext(new Pair<>(movie.getId(), false)));
   }

   @Override public Single<List<Video>> videos(long movieId) {
      return Single.create(new SingleOnSubscribe<List<Video>>() {
         @Override public void subscribe(SingleEmitter<List<Video>> e) throws Exception {
            final URL url = NetworkUtils.buildVideoUrl(movieId);
            try {
               final List<Video> videos = fetchVideosFromMovieJson(getResponseFromHttpUrl(url));
               e.onSuccess(videos);
            } catch (IOException | JSONException ex) {
               e.onError(ex);
            }
         }
      });
   }

   @Override public Single<List<Review>> reviews(long movieId) {
      return Single.create(new SingleOnSubscribe<List<Review>>() {
         @Override public void subscribe(SingleEmitter<List<Review>> e) throws Exception {
            final URL url = NetworkUtils.buildReviewUrl(movieId);
            try {
               final List<Review> reviews = fetchReviewsFromMovieJson(getResponseFromHttpUrl(url));
               e.onSuccess(reviews);
            } catch (IOException | JSONException ex) {
               e.onError(ex);
            }
         }
      });
   }

   @Override public PublishProcessor<Pair<Long, Boolean>> getFavoredProcessor() {
      return favoredProcessor;
   }
}
