/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.dialog.example;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * The main activity of the example app.
 *
 * @author Michael Rapp
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The tag, which is used to show the activity's fragment.
     */
    private static final String FRAGMENT_TAG = MainActivity.class.getSimpleName() + "::fragmentTag";

    @Override
    public final void setTheme(final int resid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeKey = getString(R.string.theme_preference_key);
        String themeDefaultValue = getString(R.string.theme_preference_default_value);
        int theme = Integer.valueOf(sharedPreferences.getString(themeKey, themeDefaultValue));
        String fullscreenKey = getString(R.string.fullscreen_preference_key);
        boolean fullscreenDefaultValue =
                getResources().getBoolean(R.bool.fullscreen_preference_default_value);
        boolean fullscreen = sharedPreferences.getBoolean(fullscreenKey, fullscreenDefaultValue);

        if (theme != 0) {
            super.setTheme(fullscreen ? R.style.DarkFullscreenTheme : R.style.DarkTheme);
        } else {
            super.setTheme(fullscreen ? R.style.LightFullscreenTheme : R.style.LightTheme);
        }
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            Fragment fragment = Fragment.instantiate(this, PreferenceFragment.class.getName());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

}