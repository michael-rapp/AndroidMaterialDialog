/*
 * Copyright 2014 - 2018 Michael Rapp
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

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

    /**
     * The activity's fragment.
     */
    private PreferenceFragment fragment;

    /**
     * The activity's floating action button.
     */
    private FloatingActionButton floatingActionButton;

    /**
     * Initializes the floating action button, which allows to show an alert dialog using a circle
     * reveal animation.
     */
    private void initializeFloatingActionButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(createFloatingActionButtonListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to show an alert dialog using a circle reveal
     * animation, when the corresponding floating action button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createFloatingActionButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                fragment.showAlertDialog(v);
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to hide or show the activity's floating action
     * button, when the preferences, which are shown by the activity's fragment, are scrolled.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnScrollListener}
     */
    private OnScrollListener createScrollListener() {
        return new OnScrollListener() {

            /**
             * The index of the first visible item.
             */
            private int firstVisibleItem = 0;

            /**
             * True, if the view is currently scrolling up, false otherwise.
             */
            private boolean scrollingUp;

            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {

            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem,
                                 final int visibleItemCount, final int totalItemCount) {
                if (firstVisibleItem < this.firstVisibleItem && !scrollingUp) {
                    scrollingUp = true;
                    floatingActionButton.show();
                } else if (firstVisibleItem > this.firstVisibleItem && scrollingUp) {
                    floatingActionButton.hide();
                    scrollingUp = false;
                }

                this.firstVisibleItem = firstVisibleItem;
            }

        };
    }

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
            fragment = (PreferenceFragment) Fragment
                    .instantiate(this, PreferenceFragment.class.getName());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment, FRAGMENT_TAG);
            transaction.commit();
        }

        initializeFloatingActionButton();
    }

    @Override
    protected final void onResume() {
        super.onResume();
        fragment = (PreferenceFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setOnScrollListener(createScrollListener());
        }
    }

}