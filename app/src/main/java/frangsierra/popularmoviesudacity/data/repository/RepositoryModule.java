package frangsierra.popularmoviesudacity.data.repository;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import frangsierra.popularmoviesudacity.core.App;
import frangsierra.popularmoviesudacity.core.scopes.ApplicationScope;

/**
 * DaggerModule with the provides methods for the repository classes.
 */
@Module
public class RepositoryModule {

   @ApplicationScope
   @Provides PopularMoviesRepository providesMoviesRepository(PopularMoviesRepositoryImpl impl) {
      return impl;
   }

   @ApplicationScope
   @Provides
   ContentResolver provideContentResolver(App application) {
      return application.getContentResolver();
   }
}
