package frangsierra.popularmoviesudacity.data.model.builders;

import android.os.Parcel;

import frangsierra.popularmoviesudacity.data.model.Video;

public class VideoBuilder {
   private String id;
   private String iso;
   private String key;
   private String name;
   private String site;
   private int size;
   private String type;
   private Parcel in;

   public VideoBuilder setId(String id) {
      this.id = id;
      return this;
   }

   public VideoBuilder setIso(String iso) {
      this.iso = iso;
      return this;
   }

   public VideoBuilder setKey(String key) {
      this.key = key;
      return this;
   }

   public VideoBuilder setName(String name) {
      this.name = name;
      return this;
   }

   public VideoBuilder setSite(String site) {
      this.site = site;
      return this;
   }

   public VideoBuilder setSize(int size) {
      this.size = size;
      return this;
   }

   public VideoBuilder setType(String type) {
      this.type = type;
      return this;
   }

   public VideoBuilder setIn(Parcel in) {
      this.in = in;
      return this;
   }

   public Video createVideo() {
      return new Video(id, iso, key, name, site, size, type);
   }
}