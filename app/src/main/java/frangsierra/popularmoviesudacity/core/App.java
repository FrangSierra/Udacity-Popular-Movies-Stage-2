package frangsierra.popularmoviesudacity.core;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import frangsierra.popularmoviesudacity.core.scopes.ApplicationScope;


public class App extends Application {
   private AppComponent appComponent;

   @Override public void onCreate() {
      super.onCreate();
      appComponent = DaggerAppComponent.builder()
         .appModule(new AppModule(this)).build();
      appComponent.inject(this);
   }

   @Module
   public class AppModule {
      private final App app;

      public AppModule(App app) {
         this.app = app;
      }

      @ApplicationScope
      @Provides
      App provideApp() {
         return app;
      }
   }

}
