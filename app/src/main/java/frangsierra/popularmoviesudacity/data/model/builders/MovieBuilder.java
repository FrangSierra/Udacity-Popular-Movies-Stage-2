package frangsierra.popularmoviesudacity.data.model.builders;

import frangsierra.popularmoviesudacity.data.model.Movie;

/**
 * Factory builder class for {@link Movie}
 */
public class MovieBuilder {
   private long id;
   private String title;
   private String originalTitle;
   private String overview;
   private String posterPath;
   private double voteAverage;
   private long voteCount;
   private String releaseDate;
   private boolean adultsMovie;
   private String language;
   private String backdrop;
   private boolean video;
   private boolean isFav = false;

   public MovieBuilder setId(long id) {
      this.id = id;
      return this;
   }

   public MovieBuilder setTitle(String title) {
      this.title = title;
      return this;
   }

   public MovieBuilder setOriginalTitle(String originalTitle) {
      this.originalTitle = originalTitle;
      return this;
   }

   public MovieBuilder setOverview(String overview) {
      this.overview = overview;
      return this;
   }

   public MovieBuilder setPosterPath(String posterPath) {
      this.posterPath = posterPath;
      return this;
   }

   public MovieBuilder setVoteAverage(double voteAverage) {
      this.voteAverage = voteAverage;
      return this;
   }

   public MovieBuilder setVoteCount(long voteCount) {
      this.voteCount = voteCount;
      return this;
   }

   public MovieBuilder setReleaseDate(String releaseDate) {
      this.releaseDate = releaseDate;
      return this;
   }

   public MovieBuilder setAdultsMovie(boolean adultsMovie) {
      this.adultsMovie = adultsMovie;
      return this;
   }

   public MovieBuilder setLanguage(String language) {
      this.language = language;
      return this;
   }

   public MovieBuilder setBackdrop(String backdrop) {
      this.backdrop = backdrop;
      return this;
   }

   public MovieBuilder setVideo(boolean video) {
      this.video = video;
      return this;
   }

   public MovieBuilder setAsFavorite(boolean isFav) {
      this.isFav = isFav;
      return this;
   }

   public Movie createMovie() {
      return new Movie(id, title, originalTitle, overview, posterPath, voteAverage, voteCount, releaseDate, adultsMovie, language, backdrop, video, isFav);
   }
}