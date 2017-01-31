package frangsierra.popularmoviesudacity.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.MovieBuilder;

/**
 * POJO class for the movie's object.
 */
public class Movie implements Parcelable {

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
   private final String poster_path;
   private final double vote_average;
   private final long vote_count;
   private final String release_date;
   private final boolean adultsMovie;
   private final String language;
   private final String backdrop;
   private final boolean video;

   public Movie(long id, String title, String originalTitle, String overview, String poster_path,
                double vote_average, long vote_count, String release_date, boolean adultsMovie,
                String language, String backdrop, boolean video) {
      this.id = id;
      this.title = title;
      this.originalTitle = originalTitle;
      this.overview = overview;
      this.poster_path = poster_path;
      this.vote_average = vote_average;
      this.vote_count = vote_count;
      this.release_date = release_date;
      this.adultsMovie = adultsMovie;
      this.language = language;
      this.backdrop = backdrop;
      this.video = video;
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
   public String getRelease_date() {
      return release_date;
   }

   /**
    * Return the current formatted rating of the {@link Movie}.
    */
   public String getRating() {
      return String.format("%s / 10", vote_average);
   }

   /**
    * Return the current poster path of the {@link Movie}.
    */
   public String getPoster_path() {
      return poster_path;
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
         .setPoster_path(jsonObject.getString(POSTER_PATH))
         .setVote_average(jsonObject.getDouble(VOTE_AVERAGE))
         .setVote_count(jsonObject.getLong(VOTE_COUNT))
         .setRelease_date(jsonObject.getString(RELEASE_DATE))
         .setAdultsMovie(jsonObject.getBoolean(ADULTS))
         .setLanguage(jsonObject.getString(LANGUAGE))
         .setBackdrop(jsonObject.getString(BACKDROP))
         .setVideo(jsonObject.getBoolean(VIDEO))
         .createMovie();
   }

   public Movie(Parcel in) {
      this.id = in.readLong();
      this.title = in.readString();
      this.originalTitle = in.readString();
      this.overview = in.readString();
      this.poster_path = in.readString();
      this.vote_average = in.readDouble();
      this.vote_count = in.readLong();
      this.release_date = in.readString();
      this.adultsMovie = in.readByte() != 0;
      this.language = in.readString();
      this.backdrop = in.readString();
      this.video = in.readByte() != 0;
   }

   @Override public int describeContents() {
      return 0;
   }

   @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(id);
      dest.writeString(title);
      dest.writeString(originalTitle);
      dest.writeString(overview);
      dest.writeString(poster_path);
      dest.writeDouble(vote_average);
      dest.writeLong(vote_count);
      dest.writeString(release_date);
      dest.writeByte((byte) (adultsMovie ? 1 : 0));
      dest.writeString(language);
      dest.writeString(backdrop);
      dest.writeByte((byte) (video ? 1 : 0));
   }

   public static final Creator<Movie> CREATOR = new Creator<Movie>() {
      @Override
      public Movie[] newArray(int size) {
         return new Movie[size];
      }

      @Override
      public Movie createFromParcel(Parcel source) {
         return new MovieBuilder().setSource(source).createMovie();
      }
   };
}
