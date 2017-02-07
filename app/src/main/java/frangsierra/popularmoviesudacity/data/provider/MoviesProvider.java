package frangsierra.popularmoviesudacity.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies;

import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.BASE_CONTENT_URI;
import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.CONTENT_AUTHORITY;

/**
 * Custom {@link ContentProvider} used to work together with the {@link MovieDatabaseContract} and
 * {@link MovieDbHelper} classes. Storing and working with the values related to the {@link Movie movies}.
 */
public final class MoviesProvider extends ContentProvider {

   private static final int MOVIES = 100;
   private static final int MOVIES_ID = 101;
   private static final String MOVIES_PATH = "movies";
   private static final String MOVIES_ID_PATH = "movies/*";
   private static final UriMatcher URI_MATCHER = buildUriMatcher();
   private MovieDbHelper movieHelper;

   @Inject public MoviesProvider() {
   }

   private static UriMatcher buildUriMatcher() {
      final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
      final String authority = CONTENT_AUTHORITY;

      matcher.addURI(authority, MOVIES_PATH, MOVIES);
      matcher.addURI(authority, MOVIES_ID_PATH, MOVIES_ID);

      return matcher;
   }

   @Override public boolean onCreate() {
      movieHelper = new MovieDbHelper(getContext());
      return true;
   }

   @Nullable @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
      final SQLiteDatabase db = movieHelper.getWritableDatabase();
      final int match = URI_MATCHER.match(uri);

      Cursor query;
      switch (match) {
         case MOVIES: {
            query = db.query(Movies.TABLE_NAME,
               projection,
               selection,
               selectionArgs,
               null,
               null,
               sortOrder
            );
            break;
         }
         default: {
            throw new UnsupportedOperationException("Unknown insert uri: " + uri);
         }
      }
      query.setNotificationUri(getContext().getContentResolver(), uri);
      return query;
   }

   @Nullable @Override public String getType(Uri uri) {
      return null;
   }

   @Nullable @Override public Uri insert(Uri uri, ContentValues values) {
      final SQLiteDatabase db = movieHelper.getWritableDatabase();
      final int match = URI_MATCHER.match(uri);
      Uri insertUri;
      switch (match) {
         case MOVIES: {
            long id = db.insertOrThrow(Movies.TABLE_NAME, null, values);
            insertUri = Movies.buildMovieUri(String.valueOf(id));
            break;
         }
         default: {
            throw new UnsupportedOperationException("Unknown insert uri: " + uri);
         }
      }
      notifyResolver(uri);
      return insertUri;
   }

   private void notifyResolver(Uri uri) {
      getContext().getContentResolver().notifyChange(uri, null);
   }

   @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
      if (uri.equals(BASE_CONTENT_URI)) {
         movieHelper.close();
         movieHelper.deleteDatabase();
         movieHelper = new MovieDbHelper(getContext());
         return 1;
      }
      final SQLiteDatabase db = movieHelper.getWritableDatabase();
      final int match = URI_MATCHER.match(uri);
      int retrieveValue;
      switch (match) {
         case MOVIES: {
            retrieveValue = db.delete(Movies.TABLE_NAME,
               selection,
               selectionArgs);
            break;
         }
         default: {
            throw new UnsupportedOperationException("Unknown insert uri: " + uri);
         }
      }
      notifyResolver(uri);
      return retrieveValue;
   }

   @Override
   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      final SQLiteDatabase db = movieHelper.getWritableDatabase();
      final int match = URI_MATCHER.match(uri);
      int retrieveValue;
      switch (match) {
         case MOVIES: {
            retrieveValue = db.update(Movies.TABLE_NAME,
               values,
               selection,
               selectionArgs);
            break;
         }
         default: {
            throw new UnsupportedOperationException("Unknown insert uri: " + uri);
         }
      }
      notifyResolver(uri);
      return retrieveValue;
   }
}
