/*
 * AndroidMaterialDialog Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.dialog.example;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import de.mrapp.android.dialog.MaterialDialogBuilder;

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
                MaterialDialogBuilder dialogBuilder = createDialogBuilder();
                dialogBuilder.show();
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
                MaterialDialogBuilder dialogBuilder = createDialogBuilder();
                dialogBuilder.setItems(R.array.list_items, createSingleChoiceListener());
                dialogBuilder.show();
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
                MaterialDialogBuilder dialogBuilder = createDialogBuilder();
                dialogBuilder
                        .setSingleChoiceItems(R.array.list_items, 0, createSingleChoiceListener());
                dialogBuilder.show();
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
                MaterialDialogBuilder dialogBuilder = createDialogBuilder();
                dialogBuilder
                        .setMultiChoiceItems(R.array.list_items, new boolean[]{true, false, false},
                                createMultiChoiceListener());
                dialogBuilder.show();
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
                MaterialDialogBuilder dialogBuilder = createDialogBuilder();
                dialogBuilder
                        .setView(View.inflate(getActivity(), R.layout.custom_dialog_content, null));
                dialogBuilder.setCustomTitle(
                        View.inflate(getActivity(), R.layout.custom_dialog_title, null));
                dialogBuilder.show();
                return true;
            }

        };
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
     * Creates and returns a builder, which allows to create dialogs, depending on the app's
     * settings.
     *
     * @return The builder, which has been created, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    @SuppressWarnings("deprecation")
    private MaterialDialogBuilder createDialogBuilder() {
        MaterialDialogBuilder dialogBuilder = new MaterialDialogBuilder(getActivity());

        if (shouldTitleBeShown()) {
            dialogBuilder.setTitle(getDialogTitle());
        }

        if (shouldMessageBeShown()) {
            dialogBuilder.setMessage(getDialogMessage());
        }

        if (shouldIconBeShown()) {
            dialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
        }

        if (shouldNegativeButtonBeShown()) {
            dialogBuilder.setNegativeButton(getNegativeButtonText(), null);
        }

        if (shouldNeutralButtonBeShown()) {
            dialogBuilder.setNeutralButton(getNeutralButtonText(), null);
        }

        if (shouldPositiveButtonBeShown()) {
            dialogBuilder.setPositiveButton(getPositiveButtonText(), null);
        }

        dialogBuilder.stackButtons(shouldStackButtons());

        if (shouldColorsBeInverted()) {
            int invertedPrimaryColor = invertColor(getThemeColor(R.attr.colorPrimary));
            int invertedAccentColor = invertColor(getThemeColor(R.attr.colorAccent));
            int invertedTextColor = invertColor(getThemeColor(android.R.attr.textColorPrimary));
            dialogBuilder.setTitleColor(invertedPrimaryColor);
            dialogBuilder.setMessageColor(invertedTextColor);
            dialogBuilder.setItemColor(invertedTextColor);
            dialogBuilder.setItemControlColor(invertedAccentColor);
            dialogBuilder.setButtonTextColor(invertedAccentColor);
            dialogBuilder.setBackgroundColor(
                    getActivity().getResources().getColor(android.R.color.background_dark));
        }

        return dialogBuilder;
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
     * Returns, whether the colors of all elements of the example dialog should be inverted, or
     * not.
     *
     * @return True, if the colors should be inverted, false otherwise
     */
    private boolean shouldColorsBeInverted() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.invert_colors_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.invert_colors_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns the color of a specific theme attribute.
     *
     * @param resourceId
     *         The resource id of the theme attribute
     * @return The color of the given theme attribute
     */
    private int getThemeColor(final int resourceId) {
        TypedArray typedArray =
                getActivity().getTheme().obtainStyledAttributes(new int[]{resourceId});
        return typedArray.getColor(0, 0);
    }

    /**
     * Inverts a specific color.
     *
     * @param color
     *         The color, which should be inverted
     * @return The inverted color
     */
    private int invertColor(final int color) {
        int alpha = Color.alpha(color);
        int invertedRed = 255 - Color.red(color);
        int invertedGreen = 255 - Color.green(color);
        int invertedBlue = 255 - Color.blue(color);
        return Color.argb(alpha, invertedRed, invertedGreen, invertedBlue);
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initializeShowAlertDialogPreference();
        initializeShowListDialogPreference();
        initializeShowSingleChoiceDialogPreference();
        initializeShowMultiChoiceDialogPreference();
        initializeShowCustomDialogPreference();
    }

}