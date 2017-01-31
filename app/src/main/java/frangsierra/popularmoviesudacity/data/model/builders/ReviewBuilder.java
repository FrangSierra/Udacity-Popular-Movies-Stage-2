package frangsierra.popularmoviesudacity.data.model.builders;

import android.os.Parcel;

import frangsierra.popularmoviesudacity.data.model.Review;

public class ReviewBuilder {
   private String id;
   private String author;
   private String content;
   private String url;
   private Parcel in;

   public ReviewBuilder setId(String id) {
      this.id = id;
      return this;
   }

   public ReviewBuilder setAuthor(String author) {
      this.author = author;
      return this;
   }

   public ReviewBuilder setContent(String content) {
      this.content = content;
      return this;
   }

   public ReviewBuilder setUrl(String url) {
      this.url = url;
      return this;
   }

   public ReviewBuilder setIn(Parcel in) {
      this.in = in;
      return this;
   }

   public Review createReview() {
      return new Review(id, author, content, url);
   }
}