package frangsierra.popularmoviesudacity.movies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.DaggerCleanActivity;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.ui.adapter.ReviewAdapter;
import frangsierra.popularmoviesudacity.ui.adapter.VideoAdapter;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

interface MovieDetailView {

   void setFavoredMovie(Long movieId, Boolean favored);
}

/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MovieBrowserActivity}.
 */
public class MovieDetailActivity extends DaggerCleanActivity<MovieDetailPresenter, MovieDetailView, MoviesComponent>
   implements MovieDetailView, VideoAdapter.VideoAdapterListener {

   @BindView(R.id.movie_title) TextView titleText;
   @BindView(R.id.movie_rating) TextView ratingText;
   @BindView(R.id.movie_overview) TextView overviewText;
   @BindView(R.id.movie_release_date) TextView releaseDateText;
   @BindView(R.id.movie_poster) ImageView moviePoster;
   @BindView(R.id.movie_favorite_button) ImageView favButton;
   @BindView(R.id.videos_list) RecyclerView videosRecycler;
   @BindView(R.id.reviews_list) RecyclerView reviewsRecycler;
   @BindView(R.id.video_container) ViewGroup videoContainer;
   @BindView(R.id.review_container) ViewGroup reviewContainer;
   private Movie currentMovie;
   private ArrayList<Video> movieVideos;
   private ArrayList<Review> movieReviews;
   private VideoAdapter videoAdapter;
   private ReviewAdapter reviewAdapter;

   @Inject
   public MovieDetailActivity() {
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      ButterKnife.bind(this, this);
      Intent intent = getIntent();
      if (intent == null || !intent.hasExtra(MovieBrowserActivity.MOVIE_EXTRA)) {
         throw new NullPointerException("Movie can't be null");
      }
      currentMovie = intent.getParcelableExtra(MovieBrowserActivity.MOVIE_EXTRA);
      movieVideos = intent.getParcelableArrayListExtra(MovieBrowserActivity.VIDEO_EXTRA);
      movieReviews = intent.getParcelableArrayListExtra(MovieBrowserActivity.REVIEW_EXTRA);
      if (currentMovie == null) return;
      titleText.setText(currentMovie.getTitle());
      final String ratingText = getString(R.string.rating_text) + currentMovie.getRating();
      this.ratingText.setText(ratingText);
      overviewText.setText(currentMovie.getOverview());
      releaseDateText.setText(currentMovie.getReleaseDate());
      favButton.setSelected(currentMovie.isFavMovie());
      Picasso.with(this)
         .load(MovieUtils.buildPosterUri(currentMovie.getPosterPath()))
         .into(moviePoster);
      setTitle(currentMovie.getTitle());


      if (movieVideos != null)
         inflateVideos();
      if (movieReviews != null)
         inflateReviews();
   }

   private void inflateReviews() {
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
      reviewsRecycler.setLayoutManager(linearLayoutManager);
      reviewAdapter = new ReviewAdapter(movieReviews);
      reviewsRecycler.setAdapter(reviewAdapter);
      reviewContainer.setVisibility(View.VISIBLE);
   }

   private void inflateVideos() {
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
      videosRecycler.setLayoutManager(linearLayoutManager);
      videoAdapter = new VideoAdapter(movieVideos, this);
      videosRecycler.setAdapter(videoAdapter);
      videoContainer.setVisibility(View.VISIBLE);
   }

   @Override protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      getPresenter().setFavoredProcessor();
   }

   /**
    * Check the actual favorite value from the movie and call the presenter to mark it as favorite.
    */
   @OnClick(R.id.movie_favorite_button)
   public void onFavoredMovie() {
      if (currentMovie == null) return;

      boolean favored = !currentMovie.isFavMovie();
      getPresenter().markMovieAsFavorite(currentMovie, favored);
   }


   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieDetailModule(new MoviesComponent.MovieDetailModule())
         .build();
   }

   @Override public void setFavoredMovie(Long movieId, Boolean favored) {
      if (currentMovie.getId() != movieId) return;
      currentMovie.setFavMovie(favored);
      favButton.setSelected(favored);
   }

   @Override public void onVideoClick(int position) {
      Video video = videoAdapter.getVideoFromPosition(position);
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
   }
}