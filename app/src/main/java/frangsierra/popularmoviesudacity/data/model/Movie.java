package frangsierra.popularmoviesudacity.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.MovieBuilder;
import frangsierra.popularmoviesudacity.data.provider.MovieDatabaseContract;

/**
 * POJO class for the movie's object.
 */
public class Movie implements Parcelable {

   public static final Creator<Movie> CREATOR = new Creator<Movie>() {
      @Override
      public Movie[] newArray(int size) {
         return new Movie[size];
      }

      @Override
      public Movie createFromParcel(Parcel source) {
         return fromParcel(source);
      }
   };
   private static final String ID = "id";
   private static final String TITLE = "title";
   private static final String ORIGINAL_TITLE = "original_title";
   private static final String OVERVIEW = "overview";
   private static final String POSTER_PATH = "poster_path";
   private static final String VOTE_AVERAGE = "vote_average";
   private static final String VOTE_COUNT = "vote_count";
   private static final String RELEASE_DATE = "release_date";
   private static final String ADULTS = "adult";
   private static final String VIDEO = "video";
   private static final String LANGUAGE = "original_language";
   private static final String BACKDROP = "backdrop_path";
   private final long id;
   private final String title;
   private final String originalTitle;
   private final String overview;
   private final String posterPath;
   private final double voteAverage;
   private final long voteCount;
   private final String releaseDate;
   private final boolean adultsMovie;
   private final String language;
   private final String backdrop;
   private final boolean video;
   private boolean isFavMovie = false;

   @SuppressWarnings("ParameterNumber")
   public Movie(long id, String title, String originalTitle, String overview, String posterPath,
                double voteAverage, long voteCount, String releaseDate, boolean adultsMovie,
                String language, String backdrop, boolean video, boolean isFavMovie) {
      this.id = id;
      this.title = title;
      this.originalTitle = originalTitle;
      this.overview = overview;
      this.posterPath = posterPath;
      this.voteAverage = voteAverage;
      this.voteCount = voteCount;
      this.releaseDate = releaseDate;
      this.adultsMovie = adultsMovie;
      this.language = language;
      this.backdrop = backdrop;
      this.video = video;
      this.isFavMovie = isFavMovie;
   }

   /**
    * Build a {@link Movie} object from a given {@link JSONObject}.
    */
   public static Movie fromJson(JSONObject jsonObject) throws JSONException {
      return new MovieBuilder()
         .setId(jsonObject.getLong(ID))
         .setTitle(jsonObject.getString(TITLE))
         .setOriginalTitle(jsonObject.getString(ORIGINAL_TITLE))
         .setOverview(jsonObject.getString(OVERVIEW))
         .setPosterPath(jsonObject.getString(POSTER_PATH))
         .setVoteAverage(jsonObject.getDouble(VOTE_AVERAGE))
         .setVoteCount(jsonObject.getLong(VOTE_COUNT))
         .setReleaseDate(jsonObject.getString(RELEASE_DATE))
         .setAdultsMovie(jsonObject.getBoolean(ADULTS))
         .setLanguage(jsonObject.getString(LANGUAGE))
         .setBackdrop(jsonObject.getString(BACKDROP))
         .setVideo(jsonObject.getBoolean(VIDEO))
         .createMovie();
   }

   private static Movie fromParcel(Parcel in) {
      return new MovieBuilder()
         .setId(in.readLong())
         .setTitle(in.readString())
         .setOriginalTitle(in.readString())
         .setOverview(in.readString())
         .setPosterPath(in.readString())
         .setVoteAverage(in.readDouble())
         .setVoteCount(in.readLong())
         .setReleaseDate(in.readString())
         .setAdultsMovie(in.readByte() != 0)
         .setLanguage(in.readString())
         .setBackdrop(in.readString())
         .setVideo(in.readByte() != 0)
         .setAsFavorite(in.readByte() != 0)
         .createMovie();
   }

   public long getId() {
      return id;
   }

   /**
    * Return the current title of the {@link Movie}.
    */
   public String getTitle() {
      return title;
   }

   /**
    * Return the current overview of the {@link Movie}.
    */
   public String getOverview() {
      return overview;
   }

   /**
    * Return the current release date of the {@link Movie}.
    */
   public String getReleaseDate() {
      return releaseDate;
   }

   /**
    * Return the current formatted rating of the {@link Movie}.
    */
   public String getRating() {
      return String.format("%s / 10", voteAverage);
   }

   /**
    * Return the current poster path of the {@link Movie}.
    */
   public String getPosterPath() {
      return posterPath;
   }

   public String getOriginalTitle() {
      return originalTitle;
   }

   public double getVoteAverage() {
      return voteAverage;
   }

   public long getVoteCount() {
      return voteCount;
   }

   public boolean isAdultsMovie() {
      return adultsMovie;
   }

   public String getLanguage() {
      return language;
   }

   public String getBackdrop() {
      return backdrop;
   }

   public boolean isVideo() {
      return video;
   }

   public boolean isFavMovie() {
      return isFavMovie;
   }

   public void setFavMovie(boolean favMovie) {
      isFavMovie = favMovie;
   }

   @Override public int describeContents() {
      return 0;
   }

   @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(id);
      dest.writeString(title);
      dest.writeString(originalTitle);
      dest.writeString(overview);
      dest.writeString(posterPath);
      dest.writeDouble(voteAverage);
      dest.writeLong(voteCount);
      dest.writeString(releaseDate);
      dest.writeByte((byte) (adultsMovie ? 1 : 0));
      dest.writeString(language);
      dest.writeString(backdrop);
      dest.writeByte((byte) (video ? 1 : 0));
      dest.writeByte((byte) (isFavMovie ? 1 : 0));
   }

   /**
    * Return a new {@link Movie} object from a {@link Cursor}.
    */
   public static Movie fromCursor(Cursor query) {
      return new MovieBuilder()
      .setId(query.getLong(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_ID)))
         .setTitle(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_TITLE)))
         .setOriginalTitle(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_TITLE)))
         .setOverview(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_OVERVIEW)))
         .setPosterPath(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_POSTER_PATH)))
         .setVoteAverage(query.getDouble(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_AVERAGE)))
         .setVoteCount(query.getLong(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_VOTE_COUNT)))
         .setReleaseDate(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_RELEASE_DATE)))
         .setBackdrop(query.getString(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_BACKDROP_PATH)))
         .setAsFavorite(query.getInt(query.getColumnIndex(MovieDatabaseContract.Movies.COLUMN_MOVIE_FAVORED)) > 0)
         .createMovie();
   }
}
