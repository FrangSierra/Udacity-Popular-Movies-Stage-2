package frangsierra.popularmoviesudacity.data.model.builders;

import frangsierra.popularmoviesudacity.data.model.Review;

/**
 * Builder class for the {@link Review class}.
 */
public class ReviewBuilder {
   private String id;
   private String author;
   private String content;
   private String url;

   @SuppressWarnings("javadocmethod")
   public ReviewBuilder setId(String id) {
      this.id = id;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public ReviewBuilder setAuthor(String author) {
      this.author = author;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public ReviewBuilder setContent(String content) {
      this.content = content;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public ReviewBuilder setUrl(String url) {
      this.url = url;
      return this;
   }

   @SuppressWarnings("javadocmethod")
   public Review createReview() {
      return new Review(id, author, content, url);
   }
}