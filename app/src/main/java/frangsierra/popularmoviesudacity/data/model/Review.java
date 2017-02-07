package frangsierra.popularmoviesudacity.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.ReviewBuilder;

/**
 * POJO object used to work with reviews retrieved from the JSON file.
 */
public class Review implements Parcelable {
   public static final Creator<Review> CREATOR = new Creator<Review>() {
      public Review createFromParcel(Parcel source) {
         return fromParcel(source);
      }

      public Review[] newArray(int size) {
         return new Review[size];
      }
   };
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

   private static Review fromParcel(Parcel in) {
      return new ReviewBuilder()
         .setId(in.readString())
         .setAuthor(in.readString())
         .setContent(in.readString())
         .setUrl(in.readString())
         .createReview();
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

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
      dest.writeString(this.author);
      dest.writeString(this.content);
      dest.writeString(this.url);
   }

}
