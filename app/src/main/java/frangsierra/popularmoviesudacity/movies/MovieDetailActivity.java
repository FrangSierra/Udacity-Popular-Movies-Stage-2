package frangsierra.popularmoviesudacity.movies;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frangsierra.popularmoviesudacity.R;
import frangsierra.popularmoviesudacity.core.ui.DaggerCleanActivity;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.utils.MovieUtils;

/**
 * Activity used for show the details of a {@link Movie} when the user click's in one of them in {@link MovieBrowserActivity}.
 */
public class MovieDetailActivity extends DaggerCleanActivity<MovieDetailPresenter, MovieDetailView, MoviesComponent>
implements MovieDetailView{
   @BindView(R.id.movie_title) TextView mTitleText;
   @BindView(R.id.movie_rating) TextView mRatingText;
   @BindView(R.id.movie_overview) TextView mOverviewText;
   @BindView(R.id.movie_release_date) TextView mReleaseDateText;
   @BindView(R.id.movie_poster) ImageView mMoviePoster;

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
         Movie movie = intent.getParcelableExtra(MovieBrowserActivity.MOVIE_EXTRA);
         if (movie == null) return;
         mTitleText.setText(movie.getTitle());
         final String ratingText = getString(R.string.rating_text) + movie.getRating();
         mRatingText.setText(ratingText);
         mOverviewText.setText(movie.getOverview());
         mReleaseDateText.setText(movie.getReleaseDate());

         Picasso.with(this)
            .load(MovieUtils.buildPosterUri(movie.getPosterPath()))
            .into(mMoviePoster);
         setTitle(movie.getTitle());
      }
   }

   @Override protected MoviesComponent buildComponent() {
      return DaggerMoviesComponent.builder()
         .appComponent(getApplicationComponent())
         .movieDetailModule(new MoviesComponent.MovieDetailModule())
         .build();
   }
}

interface MovieDetailView{

}