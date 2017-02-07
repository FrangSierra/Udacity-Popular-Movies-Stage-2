package frangsierra.popularmoviesudacity.data.model.builders;

import frangsierra.popularmoviesudacity.data.model.Video;

/**
 * Factory method for {@link Video} class.
 */
public class VideoBuilder {
   private String id;
   private String iso;
   private String key;
   private String name;
   private String site;
   private int size;
   private String type;

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setId(String id) {
      this.id = id;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setIso(String iso) {
      this.iso = iso;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setKey(String key) {
      this.key = key;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setName(String name) {
      this.name = name;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setSite(String site) {
      this.site = site;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setSize(int size) {
      this.size = size;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public VideoBuilder setType(String type) {
      this.type = type;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public Video createVideo() {
      return new Video(id, iso, key, name, site, size, type);
   }
}