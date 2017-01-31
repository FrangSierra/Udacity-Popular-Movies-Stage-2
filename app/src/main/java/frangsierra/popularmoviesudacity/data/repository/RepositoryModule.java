package frangsierra.popularmoviesudacity.data.repository;

import dagger.Module;
import dagger.Provides;
import frangsierra.popularmoviesudacity.core.scopes.ApplicationScope;


@Module
public class RepositoryModule {

   @ApplicationScope
   @Provides
   public PopularMoviesRepository providesMoviesRepository(PopularMoviesRepositoryImpl impl) {
      return impl;
   }
}
