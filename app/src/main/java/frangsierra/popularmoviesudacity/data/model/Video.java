package frangsierra.popularmoviesudacity.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.VideoBuilder;

/**
 * Created by Durdin on 31/01/2017.
 */

public class Video implements Parcelable {
   private static final String ID = "id";
   private static final String ISO = "iso_639_1";
   private static final String KEY = "original_title";
   private static final String NAME = "overview";
   private static final String SITE = "poster_path";
   private static final String SIZE = "vote_average";
   private static final String TYPE = "vote_count";
   private String id;
   private String iso;
   private String key;
   private String name;
   private String site;
   private int size;
   private String type;

   public Video(String id, String iso, String key, String name, String site, int size, String type) {
      this.id = id;
      this.iso = iso;
      this.key = key;
      this.name = name;
      this.site = site;
      this.size = size;
      this.type = type;
   }

   public String getId() {
      return id;
   }

   public String getIso() {
      return iso;
   }

   public String getKey() {
      return key;
   }

   public String getName() {
      return name;
   }

   public String getSite() {
      return site;
   }

   public int getSize() {
      return size;
   }

   public String getType() {
      return type;
   }

   /**
    * Build a {@link Video} object from a given {@link JSONObject}.
    */
   public static Video fromJson(JSONObject jsonObject) throws JSONException {
      return new VideoBuilder().setId(jsonObject.getString(ID))
         .setIso(jsonObject.getString(ISO))
         .setKey(jsonObject.getString(KEY))
         .setName(jsonObject.getString(NAME))
         .setSite(jsonObject.getString(SITE))
         .setSize(jsonObject.getInt(SIZE))
         .setType(jsonObject.getString(TYPE))
         .createVideo();
   }

   @Override public int describeContents() {
      return 0;
   }

   @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.id);
      dest.writeString(this.iso);
      dest.writeString(this.key);
      dest.writeString(this.name);
      dest.writeString(this.site);
      dest.writeInt(this.size);
      dest.writeString(this.type);
   }

   protected Video(Parcel in) {
      this.id = in.readString();
      this.iso = in.readString();
      this.key = in.readString();
      this.name = in.readString();
      this.site = in.readString();
      this.size = in.readInt();
      this.type = in.readString();
   }

   public static final Creator<Video> CREATOR = new Creator<Video>() {
      public Video createFromParcel(Parcel source) {
         return new VideoBuilder().setIn(source).createVideo();
      }

      public Video[] newArray(int size) {
         return new Video[size];
      }
   };

}
