package frangsierra.popularmoviesudacity.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.ReviewBuilder;

/**
 * Created by Durdin on 31/01/2017.
 */

public class Review implements Parcelable {
   private static final String ID = "id";
   private static final String AUTHOR = "author";
   private static final String CONTENT = "content";
   private static final String URL = "url";
   private String id;
   private String author;
   private String content;
   private String url;

   public Review(String id, String author, String content, String url) {
      this.id = id;
      this.author = author;
      this.content = content;
      this.url = url;
   }

   public String getId() {
      return id;
   }

   public String getAuthor() {
      return author;
   }

   public String getContent() {
      return content;
   }

   public String getUrl() {
      return url;
   }

   /**
    * Build a {@link Video} object from a given {@link JSONObject}.
    */
   public static Review fromJson(JSONObject jsonObject) throws JSONException {
      return new ReviewBuilder()
         .setId(jsonObject.getString(ID))
         .setAuthor(jsonObject.getString(AUTHOR))
         .setContent(jsonObject.getString(CONTENT))
         .setUrl(jsonObject.getString(URL))
         .createReview();
   }

   @Override public int describeContents() {
      return 0;
   }

   @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
      dest.writeString(this.author);
      dest.writeString(this.content);
      dest.writeString(this.url);
   }

   protected Review(Parcel in) {
      this.id = in.readString();
      this.author = in.readString();
      this.content = in.readString();
      this.url = in.readString();
   }

   public static final Creator<Review> CREATOR = new Creator<Review>() {
      public Review createFromParcel(Parcel source) {
         return new ReviewBuilder().setIn(source).createReview();
      }

      public Review[] newArray(int size) {
         return new Review[size];
      }
   };

}
