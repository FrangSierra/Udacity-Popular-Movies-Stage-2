package frangsierra.popularmoviesudacity.movies;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;
import frangsierra.popularmoviesudacity.core.presentation.BasePresenterImpl;

/**
 * Created by Durdin on 31/01/2017.
 */

public class MovieDetailPresenterImpl extends BasePresenterImpl<MovieDetailView> implements MovieDetailPresenter {
   @Inject public MovieDetailPresenterImpl() {
   }
}
interface MovieDetailPresenter extends BasePresenter<MovieDetailView>{

}
