package frangsierra.popularmoviesudacity.core.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import frangsierra.popularmoviesudacity.core.App;
import frangsierra.popularmoviesudacity.core.AppComponent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base application activity.
 */
public class BaseActivity extends AppCompatActivity {
   private static final String TAG_DIALOG_FRAGMENT = "tagDialogFragment";
   private CompositeDisposable compositeSubscription;

   public BaseActivity() {
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      cancelSubscriptions();
   }

   public AppComponent getApplicationComponent() {
      return getAppApplication().getAppComponent();
   }

   protected App getAppApplication() {
      return (App) getApplication();
   }

   protected void track(Disposable subscription) {
      if (compositeSubscription == null) {
         compositeSubscription = new CompositeDisposable();
      }
      compositeSubscription.add(subscription);
   }

   protected void cancelSubscriptions() {
      if (compositeSubscription != null) {
         compositeSubscription.dispose();
         compositeSubscription = null;
      }
   }

   private Fragment getExistingDialogFragment() {

      return getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_FRAGMENT);
   }
}
