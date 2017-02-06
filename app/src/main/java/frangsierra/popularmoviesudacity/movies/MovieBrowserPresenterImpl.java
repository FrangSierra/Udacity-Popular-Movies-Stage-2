package frangsierra.popularmoviesudacity.movies;

import android.util.Pair;

import java.util.List;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;
import frangsierra.popularmoviesudacity.core.presentation.BasePresenterImpl;
import frangsierra.popularmoviesudacity.data.MovieSorting;
import frangsierra.popularmoviesudacity.data.model.Movie;
import frangsierra.popularmoviesudacity.data.model.Review;
import frangsierra.popularmoviesudacity.data.model.Video;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter class for {@link MovieBrowserActivity}. It is in charge of communicate the calls from the interactor
 * to the view.
 */
public class MovieBrowserPresenterImpl extends BasePresenterImpl<MovieBrowserView> implements MovieBrowserPresenter {
    private MovieBrowserInteractor interactor;

    @Inject
    public MovieBrowserPresenterImpl(MovieBrowserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void loadMovieData(@MovieSorting.MovieSortingValue String filter, int pages) {
        interactor.retrieveMovies(filter, pages)
                .zipWith(interactor.getSavedMoviesId(), (movies, favoredIds) -> {
                    for (Movie movie : movies) {
                        movie.setFavMovie(favoredIds.contains(movie.getId()));
                    }
                    return movies;
                })
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

    @Override
    public void loadMovieDetails(Movie detailMovie) {
        interactor.retrieveReviewsFromMovie(detailMovie.getId())
                .zipWith(interactor.retrieveVideosFromMovie(detailMovie.getId()),
                        (BiFunction<List<Review>, List<Video>, Pair<List<Video>, List<Review>>>) (reviews, videos) -> new Pair(videos, reviews))
                .subscribe(listListPair -> {
                    getView().startMovieDetailActivity(listListPair.first, listListPair.second, detailMovie);
                });
    }

    @Override
    public void onCreateView() {
        super.onCreateView();
        startListeningFavoredProcessor();
    }

    private void startListeningFavoredProcessor() {
        track(interactor.getFavoredProcessor().subscribe(favMoviePair -> {
            getView().updateMovieAsFavored(favMoviePair.first, favMoviePair.second);
        }));
    }
}

interface MovieBrowserPresenter extends BasePresenter<MovieBrowserView> {

    void loadMovieData(@MovieSorting.MovieSortingValue String filter, int pages);

    void loadMovieDetails(Movie detailMovie);
}
