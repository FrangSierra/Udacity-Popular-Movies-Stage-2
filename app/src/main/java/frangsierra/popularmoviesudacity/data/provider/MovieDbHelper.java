package frangsierra.popularmoviesudacity.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract.Movies.TABLE_NAME;


/**
 * Created by Durdin on 05/02/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
   private static final String DB_NAME = "movies.db";
   private static final int DB_VERSION = 1;
   private final Context mContext;

   public MovieDbHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      mContext = context;
   }

   @Override public void onCreate(SQLiteDatabase db) {

      db.execSQL("CREATE TABLE " + TABLE_NAME + "("
         + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_ID + " TEXT NOT NULL,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_OVERVIEW + " TEXT,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_AVERAGE + " REAL,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_COUNT + " INTEGER,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_BACKDROP_PATH + " TEXT,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_POSTER_PATH + " TEXT,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_RELEASE_DATE + " TEXT,"
         + MovieDatabaseContract.Movies.COLUMN_MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
         + "UNIQUE (" + MovieDatabaseContract.Movies.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)");

   }

   @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }

   public void deleteDatabase() {
      mContext.deleteDatabase(DB_NAME);
   }
}
