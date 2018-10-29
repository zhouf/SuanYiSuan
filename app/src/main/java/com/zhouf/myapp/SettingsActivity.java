package com.zhouf.myapp;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public static class SuanPreferenceFragment extends PreferenceFragment {

        public class PrefListener implements Preference.OnPreferenceChangeListener {
            private String format = null;

            public PrefListener(String key) {
                super();
                Preference preference = findPreference(key);
                format = preference.getSummary().toString();

                if (EditTextPreference.class.isInstance(preference)) {
                    // EditText
                    EditTextPreference etp = (EditTextPreference) preference;
                    onPreferenceChange(preference, etp.getText());
                } else if (ListPreference.class.isInstance(preference)) {
                    // List
                    ListPreference lp = (ListPreference) preference;
                    onPreferenceChange(preference, lp.getValue());
                } else {
                    Log.e("SettingActivity", "不支持的Preference类型");
                }
                preference.setOnPreferenceChangeListener(this);
            }

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(format.replace("{v}", newValue==null?"null":newValue.toString()));
                return true;
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            new PrefListener("match_type");
            new PrefListener(getString(R.string.settings_item_dan_key));
            new PrefListener(getString(R.string.settings_item_zhi_key));
            new PrefListener(getString(R.string.settings_item_less16_key));
        }
    }
}
