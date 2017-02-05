package frangsierra.popularmoviesudacity.utils;

import android.content.ContentValues;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;

import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_BACKDROP_PATH;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_FAVORED;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_ID;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_OVERVIEW;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_POSTER_PATH;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_RELEASE_DATE;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_TITLE;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_AVERAGE;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_COUNT;

/**
 * Util class for methods related with the {@link Movie} object.
 */
public final class MovieUtils {
   private static final String MOVIE_RESULTS = "results";
   private static final String VIDEO_RESULTS = "results";
   private static final String REVIEW_RESULTS = "results";
   private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
   private static final String API_POSTER_DEFAULT_SIZE = "w185";

   /**
    * @param jsonStr Full JSON String of data retrieved from the {@link NetworkUtils#getResponseFromHttpUrl(URL) http call}.
    * @return a list of {@link Movie} objects from the JSON.
    */
   public static List<Movie> fetchMoviesFromJson(String jsonStr) throws JSONException {
      JSONObject json = new JSONObject(jsonStr);
      JSONArray movies = json.getJSONArray(MOVIE_RESULTS);
      ArrayList<Movie> result = new ArrayList<>();

      for (int i = 0; i < movies.length(); i++) {
         result.add(Movie.fromJson(movies.getJSONObject(i)));
      }
      return result;
   }

   /**
    * @param jsonStr Full JSON String of data retrieved from the {@link NetworkUtils#getResponseFromHttpUrl(URL) http call}.
    * @return a list of {@link Video} objects from the JSON.
    */
   public static List<Video> fetchVideosFromMovieJson(String jsonStr) throws JSONException {
      JSONObject json = new JSONObject(jsonStr);
      JSONArray videos = json.getJSONArray(VIDEO_RESULTS);
      ArrayList<Video> result = new ArrayList<>();

      for (int i = 0; i < videos.length(); i++) {
         result.add(Video.fromJson(videos.getJSONObject(i)));
      }
      return result;
   }

   /**
    * @param jsonStr Full JSON String of data retrieved from the {@link NetworkUtils#getResponseFromHttpUrl(URL) http call}.
    * @return a list of {@link Review} objects from the JSON.
    */
   public static List<Review> fetchReviewsFromMovieJson(String jsonStr) throws JSONException {
      JSONObject json = new JSONObject(jsonStr);
      JSONArray reviews = json.getJSONArray(REVIEW_RESULTS);
      ArrayList<Review> result = new ArrayList<>();

      for (int i = 0; i < reviews.length(); i++) {
         result.add(Review.fromJson(reviews.getJSONObject(i)));
      }
      return result;
   }

   /**
    * Build a {@link Uri} from the path retrieved in the JSON data.
    *
    * @param posterPath path from the current poster.
    * @return a formated Uri for this {@link Movie} poster.
    */
   public static Uri buildPosterUri(String posterPath) {

      return Uri.parse(BASE_IMAGE_URL).buildUpon()
         .appendPath(API_POSTER_DEFAULT_SIZE)
         .appendEncodedPath(posterPath)
         .build();
   }

   /**
    * Return a {@link ContentValues} item with the values from a {@link Movie}.
    */
   public static ContentValues getMovieValues(Movie movie) {
      final ContentValues values = new ContentValues();
      values.put(COLUMN_MOVIE_ID, movie.getId());
      values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
      values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
      values.put(COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
      values.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
      values.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
      values.put(COLUMN_MOVIE_FAVORED, movie.isFavMovie());
      values.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
      values.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdrop());
      return values;
   }
}
