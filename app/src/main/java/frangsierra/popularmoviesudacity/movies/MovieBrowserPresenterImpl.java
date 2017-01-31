package frangsierra.popularmoviesudacity.movies;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;
import frangsierra.popularmoviesudacity.core.presentation.BasePresenterImpl;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MovieBrowserPresenterImpl extends BasePresenterImpl<MovieBrowserView> implements MovieBrowserPresenter {
   private MovieBrowserInteractor interactor;

   @Inject
   public MovieBrowserPresenterImpl(MovieBrowserInteractor interactor) {
      this.interactor = interactor;
   }

   @Override
   public void loadMovieData(@MovieSorting.MovieSortingValue String filter, int pages) {
      interactor.retrieveMovies(filter, pages)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(movies -> {
            if (movies != null) {
               getView().setMovies(movies);
            } else {
               getView().showLoadingError();
            }
            getView().disableLoadingControls();
         });
   }

   @Override public void addMovieToFavorites(Movie movie, int position) {
      interactor.addMoviewToFavorites(movie)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new CompletableObserver() {
            @Override public void onSubscribe(Disposable d) {

            }

            @Override public void onComplete() {
               getView().updateFavoriteMovie(position);
            }

            @Override public void onError(Throwable e) {

            }
         });
   }
}

interface MovieBrowserPresenter extends BasePresenter<MovieBrowserView> {

   void loadMovieData(@MovieSorting.MovieSortingValue String filter, int pages);

   void addMovieToFavorites(Movie movieFromPosition, int position);
}
