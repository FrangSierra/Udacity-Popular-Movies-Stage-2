package frangsierra.popularmoviesudacity.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * POJO class for the movie's object.
 */
public class Movie implements Parcelable {

   private static final String KEY_ID = "id";
   private static final String KEY_TITLE = "title";
   private static final String KEY_ORIGINAL_TITLE = "original_title";
   private static final String KEY_OVERVIEW = "overview";
   private static final String KEY_POSTER_PATH = "poster_path";
   private static final String KEY_VOTE_AVERAGE = "vote_average";
   private static final String KEY_VOTE_COUNT = "vote_count";
   private static final String KEY_RELEASE_DATE = "release_date";
   private static final String KEY_ADULTS = "adult";
   private static final String KEY_VIDEO = "video";
   private static final String KEY_LANGUAGE = "original_language";
   private static final String KEY_BACKDROP = "backdrop_path";
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

   public Movie(Parcel source) {
      this.id = source.readLong();
      this.title = source.readString();
      this.originalTitle = source.readString();
      this.overview = source.readString();
      this.poster_path = source.readString();
      this.vote_average = source.readDouble();
      this.vote_count = source.readLong();
      this.release_date = source.readString();
      this.adultsMovie = source.readByte() != 0;
      this.language = source.readString();
      this.backdrop = source.readString();
      this.video = source.readByte() != 0;
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
      return new Movie(
         jsonObject.getLong(KEY_ID),
         jsonObject.getString(KEY_TITLE),
         jsonObject.getString(KEY_ORIGINAL_TITLE),
         jsonObject.getString(KEY_OVERVIEW),
         jsonObject.getString(KEY_POSTER_PATH),
         jsonObject.getDouble(KEY_VOTE_AVERAGE),
         jsonObject.getLong(KEY_VOTE_COUNT),
         jsonObject.getString(KEY_RELEASE_DATE),
         jsonObject.getBoolean(KEY_ADULTS),
         jsonObject.getString(KEY_LANGUAGE),
         jsonObject.getString(KEY_BACKDROP),
         jsonObject.getBoolean(KEY_VIDEO)
      );
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
         return new Movie(source);
      }
   };
}
