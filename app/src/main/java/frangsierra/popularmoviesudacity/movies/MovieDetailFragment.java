package frangsierra.popularmoviesudacity.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.fragment.DaggerCleanFragment;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import frangsierra.popularmoviesudacity.ui.adapter.ReviewAdapter;
import frangsierra.popularmoviesudacity.ui.adapter.VideoAdapter;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.MOVIE_EXTRA;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.REVIEW_EXTRA;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.SCROLL_DETAIL_POSITION;
import static frangsierra.popularmoviesudacity.movies.MovieBrowserFragment.VIDEO_EXTRA;

interface MovieDetailView {

   void setFavoredMovie(Long movieId, Boolean favored);
}

/**
 * {@link android.support.v4.app.Fragment} called by {@link MovieDetailActivity} to show
 * the details of a specific movie.
 */
public class MovieDetailFragment extends DaggerCleanFragment<MovieDetailPresenter, MovieDetailView, MoviesComponent>
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
   @BindView(R.id.scrollview) ScrollView scrollContainer;
   private Movie currentMovie;
   private ArrayList<Video> movieVideos;
   private ArrayList<Review> movieReviews;
   private VideoAdapter videoAdapter;
   private ReviewAdapter reviewAdapter;
   private int scrollXPosition;
   private int scrollYPosition;

   @Inject
   public MovieDetailFragment() {
   }

   /**
    * Return a new instance for{@link MovieDetailFragment}.
    */
   public static MovieDetailFragment newInstance(Movie movie, List<Video> videos, List<Review> reviews) {
      MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

      Bundle bundle = new Bundle();
      bundle.putParcelable(MOVIE_EXTRA, movie);

      if (videos != null && videos.size() > 0)
         bundle.putParcelableArrayList(VIDEO_EXTRA, new ArrayList<>(videos));
      if (reviews != null && reviews.size() > 0)
         bundle.putParcelableArrayList(REVIEW_EXTRA, new ArrayList<>(reviews));

      movieDetailFragment.setArguments(bundle);
      return movieDetailFragment;
   }

   @Nullable @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.activity_detail, container, false);
   }

   @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      ButterKnife.bind(this, getActivity());
      final Bundle bundle = getArguments();
      if (bundle == null || bundle.getParcelable(MOVIE_EXTRA) == null) {
         throw new NullPointerException("Movie can't be null");
      }
      currentMovie = bundle.getParcelable(MOVIE_EXTRA);
      movieVideos = bundle.getParcelableArrayList(VIDEO_EXTRA);
      movieReviews = bundle.getParcelableArrayList(REVIEW_EXTRA);
      if (currentMovie == null) return;
      titleText.setText(currentMovie.getTitle());
      final String ratingText = getString(R.string.rating_text) + currentMovie.getRating();
      this.ratingText.setText(ratingText);
      overviewText.setText(currentMovie.getOverview());
      releaseDateText.setText(currentMovie.getReleaseDate());
      favButton.setSelected(currentMovie.isFavMovie());

      Picasso.with(getActivity())
         .load(MovieUtils.buildPosterUri(currentMovie.getPosterPath()))
         .into(moviePoster);
      getActivity().setTitle(currentMovie.getTitle());


      if (movieVideos != null)
         inflateVideos();
      if (movieReviews != null)
         inflateReviews();

      scrollContainer.post(() -> scrollContainer.scrollTo(scrollXPosition, scrollYPosition));
   }

   @Override public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      if (scrollContainer != null) {
         outState.putIntArray(SCROLL_DETAIL_POSITION, new int[]{scrollContainer.getScrollX(), scrollContainer.getScrollY()});
      }
      if (movieVideos != null) {
         outState.putParcelableArrayList(VIDEO_EXTRA, new ArrayList<>(movieVideos));
      }
      if (movieReviews != null) {
         outState.putParcelableArrayList(REVIEW_EXTRA, new ArrayList<>(movieReviews));
      }
   }

   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieDetailModule(new MoviesComponent.MovieDetailModule())
         .build();
   }

   private void inflateReviews() {
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
      reviewsRecycler.setLayoutManager(linearLayoutManager);
      reviewAdapter = new ReviewAdapter(movieReviews);
      reviewsRecycler.setAdapter(reviewAdapter);
      reviewContainer.setVisibility(View.VISIBLE);
   }

   private void inflateVideos() {
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
      videosRecycler.setLayoutManager(linearLayoutManager);
      videoAdapter = new VideoAdapter(movieVideos, this);
      videosRecycler.setAdapter(videoAdapter);
      videoContainer.setVisibility(View.VISIBLE);
   }

   @Override public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      getPresenter().setFavoredProcessor();

      if (savedInstanceState != null) {
         movieVideos = savedInstanceState.getParcelableArrayList(VIDEO_EXTRA);
         movieReviews = savedInstanceState.getParcelableArrayList(REVIEW_EXTRA);
         int[] scrollPositions = savedInstanceState.getIntArray(SCROLL_DETAIL_POSITION);
         scrollXPosition = scrollPositions[0];
         scrollYPosition = scrollPositions[1];
      }
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
