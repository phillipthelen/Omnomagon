package net.pherth.mensa;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
 
public class MainPreference extends SherlockPreferenceActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.mainpreferences);
                CharSequence entry;
                Preference mensaPref = (Preference) findPreference("mensaPreference");
                mensaPref.setOnPreferenceChangeListener(setListListener());
                entry = ((ListPreference) mensaPref).getEntry();
        		mensaPref.setSummary(entry);
                Preference pricePref = (Preference) findPreference("priceCategory");
                pricePref.setOnPreferenceChangeListener(setListListener());
                entry = ((ListPreference) pricePref).getEntry();
        		pricePref.setSummary(entry);
                
        }
        
        
        private OnPreferenceChangeListener setListListener(){

        	OnPreferenceChangeListener listener = new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(
					Preference arg0, Object arg1) {
						int index = ((ListPreference) arg0).findIndexOfValue(arg1.toString());
						CharSequence summary = ((ListPreference) arg0).getEntries()[index];
                		arg0.setSummary(summary);
                		return true;
					}
        	};
        	return listener;
        }
}
