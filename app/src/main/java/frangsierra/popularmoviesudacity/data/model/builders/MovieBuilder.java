package frangsierra.popularmoviesudacity.data.model.builders;

import android.os.Parcel;

import frangsierra.popularmoviesudacity.data.model.Movie;

public class MovieBuilder {
   private long id;
   private String title;
   private String originalTitle;
   private String overview;
   private String poster_path;
   private double vote_average;
   private long vote_count;
   private String release_date;
   private boolean adultsMovie;
   private String language;
   private String backdrop;
   private boolean video;
   private Parcel source;

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

   public MovieBuilder setPoster_path(String poster_path) {
      this.poster_path = poster_path;
      return this;
   }

   public MovieBuilder setVote_average(double vote_average) {
      this.vote_average = vote_average;
      return this;
   }

   public MovieBuilder setVote_count(long vote_count) {
      this.vote_count = vote_count;
      return this;
   }

   public MovieBuilder setRelease_date(String release_date) {
      this.release_date = release_date;
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

   public MovieBuilder setSource(Parcel source) {
      this.source = source;
      return this;
   }

   public Movie createMovie() {
      return new Movie(id, title, originalTitle, overview, poster_path, vote_average, vote_count, release_date, adultsMovie, language, backdrop, video);
   }
}