package frangsierra.popularmoviesudacity.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import frangsierra.popularmoviesudacity.R;

/**
 * Fragment in charge of store and update the application Settings.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

   @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings_preferences);
      final SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

      PreferenceCategory category = (PreferenceCategory) findPreference(getString(R.string.category_general_settings_key));
      for (int i = 0; i < category.getPreferenceCount(); i++) {
         Preference p = category.getPreference(i);
         // You don't need to set up preference summaries for checkbox preferences because
         // they are already set up in xml using summaryOff and summary On
         if (!(p instanceof CheckBoxPreference)) {
            String value = sharedPreferences.getString(p.getKey(), "");
            p.setSummary(value);
         }
      }
      Preference preference = findPreference(getString(R.string.pref_sorting_key));
      preference.setOnPreferenceChangeListener(this);
   }

   @Override public void onDestroy() {
      super.onDestroy();
   }


   @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
      // Figure out which preference was changed
      if (null != preference) {
         // Updates the summary for the preference
         preference.setSummary(newValue.toString());
         return true;
      }
      return false;
   }
}