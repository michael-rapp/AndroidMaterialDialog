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

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.dialog.ProgressDialog;
import de.mrapp.android.dialog.WizardDialog;
import de.mrapp.android.dialog.builder.AbstractButtonBarDialogBuilder;
import de.mrapp.android.dialog.builder.AbstractHeaderDialogBuilder;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    /**
     * The toast, which is used to indicate, when a dialog's list item has been selected or
     * unselected.
     */
    private Toast toast;

    /**
     * Initializes the preference, which allows to change the app's theme.
     */
    private void initializeThemePreference() {
        Preference themePreference = findPreference(getString(R.string.theme_preference_key));
        themePreference.setOnPreferenceChangeListener(createThemeChangeListener());
    }

    /**
     * Creates and returns a listener, which allows to adapt the app's theme, when the value of the
     * corresponding preference has been changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * Preference.OnPreferenceChangeListener}
     */
    private Preference.OnPreferenceChangeListener createThemeChangeListener() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                getActivity().recreate();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show an alert dialog.
     */
    private void initializeShowAlertDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_alert_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowAlertDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show an alert dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowAlertDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.show();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a list dialog.
     */
    private void initializeShowListDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_list_dialog_preference_key));
        showDialogPreference.setOnPreferenceClickListener(createShowListDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a list dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowListDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.setItems(R.array.list_items, createSingleChoiceListener());
                builder.show();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a single choice dialog.
     */
    private void initializeShowSingleChoiceDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_single_choice_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowSingleChoiceDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a single choice dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowSingleChoiceDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.setSingleChoiceItems(R.array.list_items, 0, createSingleChoiceListener());
                builder.show();
                return true;
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, which indicates when a single
     * choice list item of a dialog has been selected or unselected.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createSingleChoiceListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int position) {
                String text = getString(R.string.single_choice_listener_text);
                showToast(String.format(text, position));
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a multi choice dialog.
     */
    private void initializeShowMultiChoiceDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_multi_choice_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowMultiChoiceDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a multi choice dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowMultiChoiceDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.setMultiChoiceItems(R.array.list_items, new boolean[]{true, false, false},
                        createMultiChoiceListener());
                builder.show();
                return true;
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, which indicates when a multi
     * choice list item of a dialog has been selected or unselected.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnMultiChoiceClickListener createMultiChoiceListener() {
        return new OnMultiChoiceClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int position,
                                final boolean isChecked) {
                String text = getString(R.string.multi_choice_listener_selected_text);

                if (!isChecked) {
                    text = getString(R.string.multi_choice_listener_unselected_text);
                }

                if (toast != null) {
                    toast.cancel();
                }

                showToast(String.format(text, position));
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a custom dialog.
     */
    private void initializeShowCustomDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_custom_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowCustomDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a custom dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowCustomDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.setView(R.layout.custom_dialog_content);
                builder.setCustomTitle(R.layout.custom_dialog_title);
                builder.show();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a progress dialog.
     */
    private void initializeShowProgressDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_progress_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowProgressDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a progress dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowProgressDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                ProgressDialog.Builder builder =
                        new ProgressDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                configureButtonBarDialogBuilder(builder);
                builder.show();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show a wizard dialog.
     */
    private void initializeShowWizardDialogPreference() {
        Preference showDialogPreference =
                findPreference(getString(R.string.show_wizard_dialog_preference_key));
        showDialogPreference
                .setOnPreferenceClickListener(createShowWizardDialogPreferenceListener());
    }

    /**
     * Creates and returns a listener, which allows to show a wizard dialog.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createShowWizardDialogPreferenceListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                WizardDialog.Builder builder =
                        new WizardDialog.Builder(getActivity(), getThemeId());
                configureHeaderDialogBuilder(builder);
                builder.enableTabLayout(false);

                if (shouldButtonBarDividerBeShown()) {
                    builder.showButtonBar(true);
                }

                addFragment(builder, 1);
                addFragment(builder, 2);
                addFragment(builder, 3);
                builder.show(((AppCompatActivity) getActivity()).getSupportFragmentManager());
                return true;
            }

        };
    }

    /**
     * Adds a new fragment to a builder, which allows to create wizard dialogs.
     *
     * @param builder
     *         The builder, the fragment should be added to, as an instance of the class {@link
     *         WizardDialog.Builder}
     * @param index
     *         The index of the fragment, which should be added
     */
    private void addFragment(@NonNull final WizardDialog.Builder builder, final int index) {
        Bundle arguments = new Bundle();
        arguments.putInt(DialogFragment.INDEX_EXTRA, index);
        builder.addFragment(DialogFragment.class, arguments);
    }

    /**
     * Shows a specific toast and cancels a previous one, if existing.
     *
     * @param text
     *         The text of the toast, which should be shown, as a {@link String}, or null, if no
     *         toast should be shown
     */
    private void showToast(@Nullable final String text) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Configures a builder, which allows to create header dialogs, depending on the app's
     * settings.
     *
     * @param builder
     *         The builder, which should be configured, as an instance of the class {@link
     *         AbstractHeaderDialogBuilder}
     */
    private void configureHeaderDialogBuilder(@NonNull final AbstractHeaderDialogBuilder builder) {
        if (shouldTitleBeShown()) {
            builder.setTitle(getDialogTitle());
        }

        if (shouldMessageBeShown()) {
            builder.setMessage(getDialogMessage());
        }

        if (shouldIconBeShown()) {
            builder.setIcon(
                    ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_alert));
        }

        if (shouldHeaderBeShown()) {
            builder.showHeader(true);
            builder.setHeaderBackground(R.drawable.dialog_header_background);
            builder.setHeaderIcon(R.drawable.dialog_header_icon);
        }
    }

    /**
     * Configures a builder, which allows to create button bar dialogs, depending on the app's
     * settings.
     *
     * @param builder
     *         The builder, which should be configured as an instance of the class {@link
     *         AbstractButtonBarDialogBuilder}
     */
    private void configureButtonBarDialogBuilder(
            @NonNull final AbstractButtonBarDialogBuilder builder) {
        if (shouldNegativeButtonBeShown()) {
            builder.setNegativeButton(getNegativeButtonText(), createNegativeButtonListener());
        }

        if (shouldNeutralButtonBeShown()) {
            builder.setNeutralButton(getNeutralButtonText(), createNeutralButtonListener());
        }

        if (shouldPositiveButtonBeShown()) {
            builder.setPositiveButton(getPositiveButtonText(), createPositiveButtonListener());
        }

        builder.stackButtons(shouldStackButtons());
        builder.showButtonBarDivider(shouldButtonBarDividerBeShown());
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when the negative button of a
     * dialog has been clicked.
     *
     * @return The listener, which has been created, as an instance of the class {@link
     * OnClickListener}
     */
    private OnClickListener createNegativeButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.negative_button_toast, Toast.LENGTH_SHORT)
                        .show();
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when the neutral button of a
     * dialog has been clicked.
     *
     * @return The listener, which has been created, as an instance of the class {@link
     * OnClickListener}
     */
    private OnClickListener createNeutralButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.neutral_button_toast, Toast.LENGTH_SHORT)
                        .show();
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when the positive button of a
     * dialog has been clicked.
     *
     * @return The listener, which has been created, as an instance of the class {@link
     * OnClickListener}
     */
    private OnClickListener createPositiveButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.positive_button_toast, Toast.LENGTH_SHORT)
                        .show();
            }

        };
    }

    /**
     * Returns, whether the title of the example dialog should be shown, or not.
     *
     * @return True, if the title should be shown, false otherwise
     */
    private boolean shouldTitleBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_dialog_title_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_dialog_title_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the title of the example dialog.
     *
     * @return The title of the example dialog
     */
    private String getDialogTitle() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.dialog_title_preference_key);
        String defaultValue = getString(R.string.dialog_title_preference_default_value);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Returns, whether the icon of the example dialog should be shown, or not.
     *
     * @return True, if the icon should be shown, false otherwise
     */
    private boolean shouldIconBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_dialog_icon_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_dialog_icon_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns, whether the message of the example dialog should be shown, or not.
     *
     * @return True, if the message should be shown, false otherwise
     */
    private boolean shouldMessageBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_dialog_message_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_dialog_message_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the message of the example dialog.
     *
     * @return The message of the example dialog
     */
    private String getDialogMessage() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.dialog_message_preference_key);
        String defaultValue = getString(R.string.dialog_message_preference_default_value);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Returns, whether the negative button of the example dialog should be shown, or not.
     *
     * @return True, if the negative button should be shown, false otherwise
     */
    private boolean shouldNegativeButtonBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_negative_button_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_negative_button_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the text of the example dialog's negative button.
     *
     * @return The text of the negative button
     */
    private String getNegativeButtonText() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.negative_button_text_preference_key);
        String defaultValue = getString(R.string.negative_button_text_preference_default_value);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Returns, whether the neutral button of the example dialog should be shown, or not.
     *
     * @return True, if the neutral button should be shown, false otherwise
     */
    private boolean shouldNeutralButtonBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_neutral_button_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_neutral_button_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the text of the example dialog's neutral button.
     *
     * @return The text of the neutral button
     */
    private String getNeutralButtonText() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.neutral_button_text_preference_key);
        String defaultValue = getString(R.string.neutral_button_text_preference_default_value);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Returns, whether the positive button of the example dialog should be shown, or not.
     *
     * @return True, if the positive button should be shown, false otherwise
     */
    private boolean shouldPositiveButtonBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_positive_button_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_positive_button_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the text of the example dialog's positive button.
     *
     * @return The text of the positive button
     */
    private String getPositiveButtonText() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.positive_button_text_preference_key);
        String defaultValue = getString(R.string.positive_button_text_preference_default_value);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Returns, whether the buttons of the example dialog should be stacked, or not.
     *
     * @return True, if the buttons should be stacked, false otherwise
     */
    private boolean shouldStackButtons() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.stack_buttons_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.stack_buttons_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns, whether the divider, which is located above the dialog's buttons, should be shown,
     * or not.
     *
     * @return True, if the divider should be shown, false otherwise
     */
    private boolean shouldButtonBarDividerBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_button_bar_divider_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_button_bar_divider_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns, whether the header of the example dialog should be shown, or not.
     *
     * @return True, if the header should be shown, false otherwise
     */
    private boolean shouldHeaderBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_dialog_header_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_dialog_header_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the resource id of the dialog theme, which should be used.
     *
     * @return The resource id of the dialog theme, which should be used
     */
    private int getThemeId() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.theme_preference_key);
        String defaultValue = getString(R.string.theme_preference_default_value);
        return Integer.valueOf(sharedPreferences.getString(key, defaultValue)) == 0 ?
                R.style.LightDialogTheme : R.style.DarkDialogTheme;
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initializeThemePreference();
        initializeShowAlertDialogPreference();
        initializeShowListDialogPreference();
        initializeShowSingleChoiceDialogPreference();
        initializeShowMultiChoiceDialogPreference();
        initializeShowCustomDialogPreference();
        initializeShowProgressDialogPreference();
        initializeShowWizardDialogPreference();
    }

}