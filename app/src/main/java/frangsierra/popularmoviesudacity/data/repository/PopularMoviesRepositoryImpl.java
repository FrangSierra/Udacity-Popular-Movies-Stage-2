package frangsierra.popularmoviesudacity.data.repository;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.utils.NetworkUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchMoviesFromJson;
import static frangsierra.popularmoviesudacity.utils.MovieUtils.fetchVideosFromMovieJson;
import static frangsierra.popularmoviesudacity.utils.NetworkUtils.getResponseFromHttpUrl;

public class PopularMoviesRepositoryImpl implements PopularMoviesRepository{

   @Inject public PopularMoviesRepositoryImpl() {
   }

   @Override
   public Single<List<Movie>> retrieveMovies(@MovieSorting.MovieSortingValue String sorting, int page) {
      return Single.create(new SingleOnSubscribe<List<Movie>>() {
         @Override public void subscribe(SingleEmitter<List<Movie>> e) throws Exception {
            final URL url = NetworkUtils.buildMovieUrl(sorting, page);
            try {
               final List<Movie> movies = fetchMoviesFromJson(getResponseFromHttpUrl(url));
               e.onSuccess(movies);
            } catch (IOException | JSONException ex) {
               e.onError(ex);
            }
         }
      });
   }

   @Override public Observable<List<Movie>> savedMovies() {
      return null;
   }

   @Override public Observable<Set<Long>> savedMovieIds() {
      return null;
   }

   @Override public void saveMovie(Movie movie) {

   }

   @Override public void deleteMovie(Movie movie) {

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
}
