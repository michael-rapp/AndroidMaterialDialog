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
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.dialog.ProgressDialog;
import de.mrapp.android.dialog.WizardDialog;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.model.HeaderDialog;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    /**
     * The tag, which is used to show the wizard dialog.
     */
    private static final String WIZARD_DIALOG_TAG =
            PreferenceFragment.class.getSimpleName() + "::wizardDialogTag";

    /**
     * The name of the extra, which is used to store the state of the alert dialog within a bundle.
     */
    private static final String ALERT_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::alertDialogState";

    /**
     * The name of the extra, which is used to store the state of the list dialog within a bundle.
     */
    private static final String LIST_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::listDialogState";

    /**
     * The name of the extra, which is used to store the state of the single choice list dialog
     * within a bundle.
     */
    private static final String SINGLE_CHOICE_LIST_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::singleChoiceListDialogState";

    /**
     * The name of the extra, which is used to store the state of the multiple choice list dialog
     * within a bundle.
     */
    private static final String MULTIPLE_CHOICE_LIST_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::multipleChoiceListDialogState";

    /**
     * The name of the extra, which is used to store the state of the custom dialog within a
     * bundle.
     */
    private static final String CUSTOM_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::customDialogState";

    /**
     * The name of the extra, which is used to store the state of the progress dialog within a
     * bundle.
     */
    private static final String PROGRESS_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::progressDialogState";

    /**
     * The alert dialog.
     */
    private MaterialDialog alertDialog;

    /**
     * The list dialog.
     */
    private MaterialDialog listDialog;

    /**
     * The single choice list dialog.
     */
    private MaterialDialog singleChoiceListDialog;

    /**
     * The multiple choice list dialog.
     */
    private MaterialDialog multipleChoiceListDialog;

    /**
     * The custom dialog.
     */
    private MaterialDialog customDialog;

    /**
     * The progress dialog.
     */
    private ProgressDialog progressDialog;

    /**
     * The wizard dialog.
     */
    private WizardDialog wizardDialog;

    /**
     * The toast, which is used to indicate, when a dialog's list item has been selected or
     * unselected.
     */
    private Toast toast;

    /**
     * Initializes the dialogs.
     */
    private void initializeDialogs() {
        initializeAlertDialog();
        initializeListDialog();
        initializeSingleChoiceListDialog();
        initializeMultipleChoiceListDialog();
        initializeCustomDialog();
        initializeProgressDialog();
        initializeWizardDialog();
    }

    /**
     * Initializes the alert dialog.
     */
    private void initializeAlertDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        alertDialog = builder.create();
        configureHeaderDialog(alertDialog);
        configureButtonBarDialog(alertDialog);
    }

    /**
     * Initializes the list dialog.
     */
    private void initializeListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.setItems(R.array.list_items, createSingleChoiceListener());
        listDialog = builder.create();
        configureHeaderDialog(listDialog);
        configureButtonBarDialog(listDialog);
    }

    /**
     * Initializes the single choice list dialog.
     */
    private void initializeSingleChoiceListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.setSingleChoiceItems(R.array.list_items, 0, createSingleChoiceListener());
        singleChoiceListDialog = builder.create();
        configureHeaderDialog(singleChoiceListDialog);
        configureButtonBarDialog(singleChoiceListDialog);
    }

    /**
     * Initializes the multiple choice list dialog.
     */
    private void initializeMultipleChoiceListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.setMultiChoiceItems(R.array.list_items, new boolean[]{true, false, false},
                createMultiChoiceListener());
        multipleChoiceListDialog = builder.create();
        configureHeaderDialog(multipleChoiceListDialog);
        configureButtonBarDialog(multipleChoiceListDialog);
    }

    /**
     * Initializes the custom dialog.
     */
    private void initializeCustomDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.setView(R.layout.custom_dialog_content);
        builder.setCustomTitle(R.layout.custom_dialog_title);
        customDialog = builder.create();
        configureHeaderDialog(customDialog);
        configureButtonBarDialog(customDialog);
    }

    /**
     * Initializes the progress dialog.
     */
    private void initializeProgressDialog() {
        ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
        progressDialog = builder.create();
        configureHeaderDialog(progressDialog);
        configureButtonBarDialog(progressDialog);
    }

    /**
     * Initializes the wizard dialog.
     */
    private void initializeWizardDialog() {
        FragmentManager fragmentManager =
                ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        wizardDialog = (WizardDialog) fragmentManager.findFragmentByTag(WIZARD_DIALOG_TAG);

        if (wizardDialog == null) {
            WizardDialog.Builder builder = new WizardDialog.Builder(getActivity());
            wizardDialog = builder.create();
        }

        configureHeaderDialog(wizardDialog);
        wizardDialog.enableTabLayout(false);

        if (shouldButtonBarDividerBeShown()) {
            wizardDialog.showButtonBarDivider(true);
        }

        addFragment(1);
        addFragment(2);
        addFragment(3);
    }

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
     * OnPreferenceChangeListener}
     */
    private OnPreferenceChangeListener createThemeChangeListener() {
        return new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                getActivity().recreate();
                return true;
            }

        };
    }

    /**
     * Initializes the preference, which allows to show the alert dialog.
     */
    private void initializeShowAlertDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_alert_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeAlertDialog();
                alertDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the list dialog.
     */
    private void initializeShowListDialogPreference() {
        Preference preference = findPreference(getString(R.string.show_list_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeListDialog();
                listDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the single choice list dialog.
     */
    private void initializeShowSingleChoiceListDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_single_choice_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeSingleChoiceListDialog();
                singleChoiceListDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the multiple choice list dialog.
     */
    private void initializeShowMultipleChoiceListDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_multi_choice_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeMultipleChoiceListDialog();
                multipleChoiceListDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the custom dialog.
     */
    private void initializeShowCustomDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_custom_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeCustomDialog();
                customDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the progress dialog.
     */
    private void initializeShowProgressDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_progress_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeProgressDialog();
                progressDialog.show();
                return true;
            }

        });
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
     * Initializes the preference, which allows to show a wizard dialog.
     */
    private void initializeShowWizardDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_wizard_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeWizardDialog();
                wizardDialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(),
                        WIZARD_DIALOG_TAG);
                return true;
            }

        });
    }

    /**
     * Adds a new fragment to the wizard dialog.
     *
     * @param index
     *         The index of the fragment, which should be added
     */
    private void addFragment(final int index) {
        Bundle arguments = new Bundle();
        arguments.putInt(DialogFragment.INDEX_EXTRA, index);
        wizardDialog.addFragment(DialogFragment.class, arguments);
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
     * Configures a header dialog, depending on the app's settings.
     *
     * @param dialog
     *         The dialog, which should be configured, as an instance of the type {@link
     *         HeaderDialog}
     */
    private void configureHeaderDialog(@NonNull final HeaderDialog dialog) {
        if (shouldTitleBeShown()) {
            dialog.setTitle(getDialogTitle());
        }

        if (shouldMessageBeShown()) {
            dialog.setMessage(getDialogMessage());
        }

        if (shouldIconBeShown()) {
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
        }

        if (shouldHeaderBeShown()) {
            dialog.showHeader(true);
            dialog.setHeaderBackground(R.drawable.dialog_header_background);
            dialog.setHeaderIcon(R.drawable.dialog_header_icon);
        }
    }

    /**
     * Configures a button bar dialog, depending on the app's settings.
     *
     * @param dialog
     *         The builder, which should be configured as an instance of the type {@link
     *         ButtonBarDialog}
     */
    private void configureButtonBarDialog(@NonNull final ButtonBarDialog dialog) {
        if (shouldNegativeButtonBeShown()) {
            dialog.setNegativeButton(getNegativeButtonText(), createNegativeButtonListener());
        }

        if (shouldNeutralButtonBeShown()) {
            dialog.setNeutralButton(getNeutralButtonText(), createNeutralButtonListener());
        }

        if (shouldPositiveButtonBeShown()) {
            dialog.setPositiveButton(getPositiveButtonText(), createPositiveButtonListener());
        }

        dialog.stackButtons(shouldStackButtons());
        dialog.showButtonBarDivider(shouldButtonBarDividerBeShown());
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

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDialogs();
        addPreferencesFromResource(R.xml.preferences);
        initializeThemePreference();
        initializeShowAlertDialogPreference();
        initializeShowListDialogPreference();
        initializeShowSingleChoiceListDialogPreference();
        initializeShowMultipleChoiceListDialogPreference();
        initializeShowCustomDialogPreference();
        initializeShowProgressDialogPreference();
        initializeShowWizardDialogPreference();

        if (savedInstanceState != null) {
            Bundle alertDialogState = savedInstanceState.getBundle(ALERT_DIALOG_STATE_EXTRA);
            Bundle listDialogState = savedInstanceState.getBundle(LIST_DIALOG_STATE_EXTRA);
            Bundle singleChoiceListDialogState =
                    savedInstanceState.getBundle(SINGLE_CHOICE_LIST_DIALOG_STATE_EXTRA);
            Bundle multipleChoiceListDialogState =
                    savedInstanceState.getBundle(MULTIPLE_CHOICE_LIST_DIALOG_STATE_EXTRA);
            Bundle customDialogState = savedInstanceState.getBundle(CUSTOM_DIALOG_STATE_EXTRA);
            Bundle progressDialogState = savedInstanceState.getBundle(PROGRESS_DIALOG_STATE_EXTRA);

            if (alertDialogState != null) {
                alertDialog.onRestoreInstanceState(alertDialogState);
            }

            if (listDialogState != null) {
                listDialog.onRestoreInstanceState(listDialogState);
            }

            if (singleChoiceListDialogState != null) {
                singleChoiceListDialog.onRestoreInstanceState(singleChoiceListDialogState);
            }

            if (multipleChoiceListDialogState != null) {
                multipleChoiceListDialog.onRestoreInstanceState(multipleChoiceListDialogState);
            }

            if (customDialogState != null) {
                customDialog.onRestoreInstanceState(customDialogState);
            }

            if (progressDialogState != null) {
                progressDialog.onRestoreInstanceState(progressDialogState);
            }
        }
    }

    @Override
    public final void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(ALERT_DIALOG_STATE_EXTRA, alertDialog.onSaveInstanceState());
        outState.putBundle(LIST_DIALOG_STATE_EXTRA, listDialog.onSaveInstanceState());
        outState.putBundle(SINGLE_CHOICE_LIST_DIALOG_STATE_EXTRA,
                singleChoiceListDialog.onSaveInstanceState());
        outState.putBundle(MULTIPLE_CHOICE_LIST_DIALOG_STATE_EXTRA,
                multipleChoiceListDialog.onSaveInstanceState());
        outState.putBundle(CUSTOM_DIALOG_STATE_EXTRA, customDialog.onSaveInstanceState());
        outState.putBundle(PROGRESS_DIALOG_STATE_EXTRA, progressDialog.onSaveInstanceState());
    }

}