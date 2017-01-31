package frangsierra.popularmoviesudacity.core;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import frangsierra.popularmoviesudacity.core.scopes.ApplicationScope;

/**
 * Application instance responsible for initialice the application and basic Dagger components.
 */
public class App extends Application {
   private AppComponent appComponent;

   @Override public void onCreate() {
      super.onCreate();
      appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
   }

   public AppComponent getAppComponent() {
      return appComponent;
   }

   @Module
   static class AppModule {
      private final App app;

      AppModule(App app) {
         this.app = app;
      }

      @ApplicationScope
      @Provides
      App provideApp() {
         return app;
      }
   }

}
