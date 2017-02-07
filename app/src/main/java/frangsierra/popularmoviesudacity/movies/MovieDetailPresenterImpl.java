package frangsierra.popularmoviesudacity.movies;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;
import frangsierra.popularmoviesudacity.core.presentation.BasePresenterImpl;
import frangsierra.popularmoviesudacity.data.model.Movie;

interface MovieDetailPresenter extends BasePresenter<MovieDetailView> {

   void setFavoredProcessor();

   void markMovieAsFavorite(Movie movie, Boolean favored);
}

/**
 * Presenter class for {@link MovieDetailActivity}. It is in charge of communicate the calls from the interactor
 * to the view.
 */
public class MovieDetailPresenterImpl extends BasePresenterImpl<MovieDetailView> implements MovieDetailPresenter {
   private MovieDetailInteractor interactor;

   @Inject public MovieDetailPresenterImpl(MovieDetailInteractor interactor) {
      this.interactor = interactor;
   }

   @Override public void setFavoredProcessor() {
      track(interactor.getFavoredProcessor()
         .subscribe(favMoviePair -> {
            getView().setFavoredMovie(favMoviePair.first, favMoviePair.second);
         }));
   }

   @Override public void markMovieAsFavorite(Movie movie, Boolean favored) {
      interactor.setMovieFavored(movie, favored).subscribe();
   }
}
