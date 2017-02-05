package frangsierra.popularmoviesudacity.movies;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.DaggerCleanActivity;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MovieBrowserActivity}.
 */
public class MovieDetailActivity extends DaggerCleanActivity<MovieDetailPresenter, MovieDetailView, MoviesComponent>
   implements MovieDetailView {

   @BindView(R.id.movie_title) TextView mTitleText;
   @BindView(R.id.movie_rating) TextView mRatingText;
   @BindView(R.id.movie_overview) TextView mOverviewText;
   @BindView(R.id.movie_release_date) TextView mReleaseDateText;
   @BindView(R.id.movie_poster) ImageView mMoviePoster;
   @BindView(R.id.movie_favorite_button) ImageView mFavButton;
   private Movie mMovie;

   @Inject
   public MovieDetailActivity() {
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      ButterKnife.bind(this, this);
      Intent intent = getIntent();
      if (intent != null && intent.hasExtra(MovieBrowserActivity.MOVIE_EXTRA)) {
         mMovie = intent.getParcelableExtra(MovieBrowserActivity.MOVIE_EXTRA);
         if (mMovie == null) return;
         mTitleText.setText(mMovie.getTitle());
         final String ratingText = getString(R.string.rating_text) + mMovie.getRating();
         mRatingText.setText(ratingText);
         mOverviewText.setText(mMovie.getOverview());
         mReleaseDateText.setText(mMovie.getReleaseDate());
         mFavButton.setSelected(mMovie.isFavMovie());
         Picasso.with(this)
            .load(MovieUtils.buildPosterUri(mMovie.getPosterPath()))
            .into(mMoviePoster);
         setTitle(mMovie.getTitle());
      }
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
      if (mMovie == null) return;

      boolean favored = !mMovie.isFavMovie();
      getPresenter().markMovieAsFavorite(mMovie, favored);
   }


   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieDetailModule(new MoviesComponent.MovieDetailModule())
         .build();
   }

   @Override public void setFavoredMovie(Long movieId, Boolean favored) {
      if (mMovie.getId() != movieId) return;
      mMovie.setFavMovie(favored);
      mFavButton.setSelected(favored);
   }
}

interface MovieDetailView {

   void setFavoredMovie(Long movieId, Boolean favored);
}