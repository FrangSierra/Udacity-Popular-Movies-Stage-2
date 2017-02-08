package frangsierra.popularmoviesudacity.movies;


import dagger.Component;
import dagger.Module;
import dagger.Provides;
import frangsierra.popularmoviesudacity.core.ActivityModule;
import frangsierra.popularmoviesudacity.core.AppComponent;
import frangsierra.popularmoviesudacity.core.scopes.ViewScope;

@ViewScope
@Component(
   modules = {
      MoviesComponent.MovieBrowserModule.class,
      MoviesComponent.MovieDetailModule.class,
      ActivityModule.class,
   },
   dependencies = AppComponent.class
)
interface MoviesComponent {

   void inject(MovieBrowserFragment movieBrowserFragment);

   void inject(MovieDetailFragment movieDetailFragment);

   @Module
   class MovieBrowserModule {
      @ViewScope
      @Provides
      MovieBrowserPresenter providePresenter(MovieBrowserPresenterImpl impl) {
         return impl;
      }

      @ViewScope
      @Provides
      MovieBrowserInteractor provideInteractor(MovieBrowserInteractorImpl impl) {
         return impl;
      }

   }

   @Module
   class MovieDetailModule {
      @ViewScope
      @Provides
      MovieDetailPresenter providePresenter(MovieDetailPresenterImpl impl) {
         return impl;
      }

      @ViewScope
      @Provides
      MovieDetailInteractor provideInteractor(MovieDetailInteractorImpl impl) {
         return impl;
      }

   }
}
