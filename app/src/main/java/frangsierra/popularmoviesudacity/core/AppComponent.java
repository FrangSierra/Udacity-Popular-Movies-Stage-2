package frangsierra.popularmoviesudacity.core;

import dagger.Component;
import frangsierra.popularmoviesudacity.core.scopes.ApplicationScope;
import frangsierra.popularmoviesudacity.data.repository.PopularMoviesRepository;
import frangsierra.popularmoviesudacity.data.repository.RepositoryModule;

/**
 * Created by Durdin on 31/01/2017.
 */
@ApplicationScope
@Component(
   modules = {
      App.AppModule.class,
      RepositoryModule.class,
}
)

public interface AppComponent {
   /**
    * Return application repository.
    */
   PopularMoviesRepository provideRepository();
}
