package frangsierra.popularmoviesudacity.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import frangsierra.popularmoviesudacity.R;

/**
 * Activity in charge of inflate the {@link SettingFragment} class which store and update the application Settings.
 */
public class SettingActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_settings);
      ActionBar actionBar = this.getSupportActionBar();

      // Set the action bar back button to look like an up button
      if (actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      // When the home button is pressed, take the user back to the VisualizerActivity
      if (id == android.R.id.home) {
         onBackPressed();
      }
      return super.onOptionsItemSelected(item);
   }
}