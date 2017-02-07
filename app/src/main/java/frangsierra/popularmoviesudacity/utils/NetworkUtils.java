package frangsierra.popularmoviesudacity.utils;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import frangsierra.popularmoviesudacity.BuildConfig;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.MovieSorting.MovieSortingValue;

import static android.content.ContentValues.TAG;

/**
 * Util's class for Network related methods.
 */
public final class NetworkUtils {
   private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
   private static final String API_PARAM_PAGE = "page";
   private static final String API_PARAM_KEY = "api_key";

   /**
    * Builds the URL used to talk to the movie server using a filter and a number of pages.
    *
    * @param filter The filter used for query the movies. It should belong to one of the described in
    *               {@link MovieSorting movie sorting } class
    * @return The URL to use to query the movie server.
    */
   public static URL buildMovieUrl(@MovieSortingValue String filter, int pages) {
      Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
         .appendPath(filter)
         .appendQueryParameter(API_PARAM_PAGE, String.valueOf(pages))
         .appendQueryParameter(API_PARAM_KEY, BuildConfig.MOVIES_API_KEY)
         .build();

      URL url = null;
      try {
         url = new URL(builtUri.toString());
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      Log.v(TAG, "Built URI " + url);
      return url;
   }

   /**
    * Builds the URL used to talk to the movie server using a movie Id and retrieve the associate
    * reviews to the given movie.
    *
    * @param movieId movie id which this reviews belong.
    * @return The URL to use to query the movie server.
    */
   public static URL buildReviewUrl(long movieId) {
      String path = String.format("%s/reviews", movieId);
      Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
         .appendEncodedPath(path)

         .appendQueryParameter(API_PARAM_KEY, BuildConfig.MOVIES_API_KEY)
         .build();

      URL url = null;
      try {
         url = new URL(builtUri.toString());
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      Log.v(TAG, "Built URI " + url);
      return url;
   }

   /**
    * Builds the URL used to talk to the movie server using a movie Id and retrieve the associate
    * reviews to the given movie.
    *
    * @param movieId movie id which this reviews belong.
    * @return The URL to use to query the movie server.
    */
   public static URL buildVideoUrl(long movieId) {
      String path = String.format("%s/videos", movieId);
      Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
         .appendEncodedPath(path)

         .appendQueryParameter(API_PARAM_KEY, BuildConfig.MOVIES_API_KEY)
         .build();

      URL url = null;
      try {
         url = new URL(builtUri.toString());
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      Log.v(TAG, "Built URI " + url);
      return url;
   }

   /**
    * This method returns the entire result from the HTTP response.
    *
    * @param url The URL to fetch the HTTP response from.
    * @return The contents of the HTTP response.
    * @throws IOException Related to network and stream reading
    */
   public static String getResponseFromHttpUrl(URL url) throws IOException {
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
         InputStream in = urlConnection.getInputStream();

         Scanner scanner = new Scanner(in);
         scanner.useDelimiter("\\A");

         boolean hasInput = scanner.hasNext();
         if (hasInput) {
            return scanner.next();
         } else {
            return null;
         }
      } finally {
         urlConnection.disconnect();
      }
   }
}
