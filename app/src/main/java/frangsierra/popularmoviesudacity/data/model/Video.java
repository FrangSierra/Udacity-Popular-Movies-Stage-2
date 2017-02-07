package frangsierra.popularmoviesudacity.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import frangsierra.popularmoviesudacity.data.model.builders.VideoBuilder;


/**
 * POJO object used to work with videos retrieved from the JSON file.
 */
public class Video implements Parcelable {

   public static final String YOUTUBE = "YouTube";
   public static final String TRAILER = "Trailer";
   public static final Creator<Video> CREATOR = new Creator<Video>() {
      public Video createFromParcel(Parcel source) {
         return fromParcel(source);
      }

      public Video[] newArray(int size) {
         return new Video[size];
      }
   };
   private static final String ID = "id";
   private static final String ISO = "iso_639_1";
   private static final String KEY = "key";
   private static final String NAME = "name";
   private static final String SITE = "site";
   private static final String SIZE = "size";
   private static final String TYPE = "type";
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

   private static Video fromParcel(Parcel in) {
      return new VideoBuilder()
         .setId(in.readString())
         .setIso(in.readString())
         .setKey(in.readString())
         .setName(in.readString())
         .setSite(in.readString())
         .setSize(in.readInt())
         .setType(in.readString())
         .createVideo();
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

}
