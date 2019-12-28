/*
 * Copyright 2014 - 2019 Michael Rapp
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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import de.mrapp.android.dialog.EditTextDialog;
import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.dialog.ProgressDialog;
import de.mrapp.android.dialog.WizardDialog;
import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.CircleRevealAnimation;
import de.mrapp.android.dialog.animation.CircleTransitionAnimation;
import de.mrapp.android.dialog.animation.CrossFadeTransitionAnimation;
import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation.AnimationListener;
import de.mrapp.android.dialog.animation.RectangleRevealAnimation;
import de.mrapp.android.dialog.animation.ScaleTransitionAnimation;
import de.mrapp.android.dialog.builder.AbstractButtonBarDialogBuilder;
import de.mrapp.android.dialog.builder.AbstractHeaderDialogBuilder;
import de.mrapp.android.validation.Validators;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends PreferenceFragmentCompat {

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
     * The name of the extra, which is used to store the state of the edit text dialog within a
     * bundle.
     */
    private static final String EDIT_TEXT_DIALOG_STATE_EXTRA =
            PreferenceFragment.class.getSimpleName() + "::editTextDialogState";

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
     * The edit text dialog.
     */
    private EditTextDialog editTextDialog;

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
        initializeEditTextDialog();
    }

    /**
     * Initializes the alert dialog.
     */
    private void initializeAlertDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);
        alertDialog = builder.create();
    }

    /**
     * Initializes the list dialog.
     */
    private void initializeListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);

        if (shouldListItemIconsBeShown()) {
            int[] iconResourceIds = new int[]{R.drawable.ic_info_24dp, R.drawable.ic_info_24dp,
                    R.drawable.ic_info_24dp};
            builder.setItems(R.array.list_items, iconResourceIds, createSingleChoiceListener());
            builder.setItemIconTint(ContextCompat.getColor(getActivity(), R.color.color_accent));
        } else {
            builder.setItems(R.array.list_items, createSingleChoiceListener());
        }

        listDialog = builder.create();
    }

    /**
     * Initializes the single choice list dialog.
     */
    private void initializeSingleChoiceListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);

        if (shouldListItemIconsBeShown()) {
            int[] iconResourceIds = new int[]{R.drawable.ic_info_24dp, R.drawable.ic_info_24dp,
                    R.drawable.ic_info_24dp};
            builder.setSingleChoiceItems(R.array.list_items, iconResourceIds, 0,
                    createSingleChoiceListener());
            builder.setItemIconTint(ContextCompat.getColor(getActivity(), R.color.color_accent));
        } else {
            builder.setSingleChoiceItems(R.array.list_items, 0, createSingleChoiceListener());
        }

        singleChoiceListDialog = builder.create();
    }

    /**
     * Initializes the multiple choice list dialog.
     */
    private void initializeMultipleChoiceListDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);

        if (shouldListItemIconsBeShown()) {
            int[] iconResourceIds = new int[]{R.drawable.ic_info_24dp, R.drawable.ic_info_24dp,
                    R.drawable.ic_info_24dp};
            builder.setMultiChoiceItems(R.array.list_items, iconResourceIds,
                    new boolean[]{true, false, false}, createMultiChoiceListener());
            builder.setItemIconTint(ContextCompat.getColor(getActivity(), R.color.color_accent));
        } else {
            builder.setMultiChoiceItems(R.array.list_items, new boolean[]{true, false, false},
                    createMultiChoiceListener());
        }

        multipleChoiceListDialog = builder.create();
    }

    /**
     * Initializes the custom dialog.
     */
    private void initializeCustomDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);
        builder.setView(R.layout.custom_dialog_content);
        builder.setCustomTitle(R.layout.custom_dialog_title);
        builder.setCustomMessage(R.layout.custom_dialog_message);
        builder.setCustomButtonBar(R.layout.custom_dialog_button_bar);
        builder.setCustomHeader(R.layout.custom_dialog_header);
        customDialog = builder.create();
    }

    /**
     * Initializes the progress dialog.
     */
    private void initializeProgressDialog() {
        ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);
        progressDialog = builder.create();
    }

    /**
     * Initializes the edit text dialog.
     */
    private void initializeEditTextDialog() {
        EditTextDialog.Builder builder =
                new EditTextDialog.Builder(getActivity()).setHint(R.string.edit_text_dialog_hint)
                        .setText(getContext().getString(R.string.edit_text_dialog_text))
                        .addValidator(Validators.notEmpty(getContext()));
        configureHeaderDialogBuilder(builder);
        configureButtonBarDialogBuilder(builder);
        editTextDialog = builder.create();
    }

    /**
     * Initializes the preference, which allows to change the app's theme.
     */
    private void initializeThemePreference() {
        Preference themePreference = findPreference(getString(R.string.theme_preference_key));
        themePreference.setOnPreferenceChangeListener(createThemeChangeListener());
    }

    /**
     * Initializes the preference, which allows to specfify, whether dialogs should be shown
     * fullscreen, or not.
     */
    private void initializeFullscreenPreference() {
        Preference fullscreenPreference =
                findPreference(getString(R.string.fullscreen_preference_key));
        fullscreenPreference.setOnPreferenceChangeListener(createThemeChangeListener());
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
                alertDialog.setShowAnimation(createRectangularRevealAnimation(preference));
                alertDialog.setDismissAnimation(createRectangularRevealAnimation(preference));
                alertDialog.setCancelAnimation(createRectangularRevealAnimation(preference));
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
                listDialog.setShowAnimation(createRectangularRevealAnimation(preference));
                listDialog.setDismissAnimation(createRectangularRevealAnimation(preference));
                listDialog.setCancelAnimation(createRectangularRevealAnimation(preference));
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
                singleChoiceListDialog
                        .setShowAnimation(createRectangularRevealAnimation(preference));
                singleChoiceListDialog
                        .setDismissAnimation(createRectangularRevealAnimation(preference));
                singleChoiceListDialog
                        .setCancelAnimation(createRectangularRevealAnimation(preference));
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
                multipleChoiceListDialog
                        .setShowAnimation(createRectangularRevealAnimation(preference));
                multipleChoiceListDialog
                        .setDismissAnimation(createRectangularRevealAnimation(preference));
                multipleChoiceListDialog
                        .setCancelAnimation(createRectangularRevealAnimation(preference));
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
                customDialog.setShowAnimation(createRectangularRevealAnimation(preference));
                customDialog.setDismissAnimation(createRectangularRevealAnimation(preference));
                customDialog.setCancelAnimation(createRectangularRevealAnimation(preference));
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
                progressDialog.setShowAnimation(createRectangularRevealAnimation(preference));
                progressDialog.setDismissAnimation(createRectangularRevealAnimation(preference));
                progressDialog.setCancelAnimation(createRectangularRevealAnimation(preference));
                progressDialog.show();
                return true;
            }

        });
    }

    /**
     * Initializes the preference, which allows to show the edit text dialog.
     */
    private void initializeShowEditTextDialogPreference() {
        Preference preference =
                findPreference(getString(R.string.show_edit_text_dialog_preference_key));
        preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(final Preference preference) {
                initializeEditTextDialog();
                editTextDialog.setShowAnimation(createRectangularRevealAnimation(preference));
                editTextDialog.setDismissAnimation(createRectangularRevealAnimation(preference));
                editTextDialog.setCancelAnimation(createRectangularRevealAnimation(preference));
                editTextDialog.show();
                return true;
            }

        });
    }

    /**
     * Creates and returns the rectangular reveal animation, which should be used to show or hide
     * dialogs.
     *
     * @param preference The preference, which is used to show the dialog, as an instance of the class {@link
     *                   Preference}
     * @return The animation, which has been created, as an instance of the type {@link
     * DialogAnimation}
     */
    private DialogAnimation createRectangularRevealAnimation(@NonNull final Preference preference) {
        if (shouldUseAnimations()) {
            RecyclerView recyclerView = getListView();
            View view = recyclerView.getChildAt(preference.getOrder() + 1);
            int[] viewLocation = new int[2];
            view.getLocationOnScreen(viewLocation);
            View rootView = getActivity().findViewById(android.R.id.content);
            int[] rootViewLocation = new int[2];
            rootView.getLocationOnScreen(rootViewLocation);
            ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            int y = viewLocation[1];

            if (!shouldUseFullscreen()) {
                y -= (rootViewLocation[1] - (toolbar != null ? toolbar.getHeight() : 0));
            }

            return new RectangleRevealAnimation.Builder(getActivity()).setWidth(view.getWidth())
                    .setHeight(view.getHeight()).setX(viewLocation[0]).setY(y).create();
        }

        return null;
    }

    /**
     * Creates and returns the circle reveal animation, which should be used to show or hide
     * dialogs.
     *
     * @param view The view, which is used to show the dialog, as an instance of the class {@link View}
     * @return The animation, which has been created, as an instance of the type {@link
     * DialogAnimation}
     */
    private DialogAnimation createCircleRevealAnimation(@NonNull final View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int margin = getResources().getDimensionPixelSize(R.dimen.floating_action_button_margin);
        int width = view.getWidth();
        int height = view.getHeight();
        float radius = width - margin * 2;
        int x = location[0] + width / 2;
        int y = location[1] + height / 2;
        return new CircleRevealAnimation.Builder(getActivity()).setX(x).setY(y).setRadius(radius)
                .create();
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
                WizardDialog.Builder builder = new WizardDialog.Builder(getActivity(),
                        shouldUseFullscreen() ? R.style.DarkFullscreenDialogTheme : 0);
                configureHeaderDialogBuilder(builder);
                builder.enableTabLayout(!shouldHeaderBeShown());

                if (shouldButtonBarDividerBeShown()) {
                    builder.showButtonBarDivider(true);
                }

                addFragment(builder, 1);
                addFragment(builder, 2);
                addFragment(builder, 3);
                WizardDialog wizardDialog = builder.create();

                if (shouldUseAnimations()) {
                    builder.setBackgroundColor(
                            getResources().getIntArray(R.array.wizard_dialog_background_colors)[0]);
                    builder.addOnPageChangeListener(
                            createWizardDialogPageChangeListener(wizardDialog));
                }

                wizardDialog.show(getActivity().getSupportFragmentManager(), null);
                return true;
            }

        });
    }

    /**
     * Creates and returns a listener, which allows to change the background color of the wizard
     * dialog, when its view pager is scrolled.
     *
     * @param wizardDialog The wizard dialog as an instance of the class {@link WizardDialog}. The wizard dialog
     *                     may not be null
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPageChangeListener}
     */
    private OnPageChangeListener createWizardDialogPageChangeListener(
            @NonNull final WizardDialog wizardDialog) {
        return new OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, final float positionOffset,
                                       final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                int[] colors = getResources().getIntArray(R.array.wizard_dialog_background_colors);
                BackgroundAnimation backgroundAnimation =
                        new CrossFadeTransitionAnimation.Builder(getActivity()).create();
                wizardDialog.setBackgroundColor(colors[position], backgroundAnimation);
                TypedArray backgroundIds =
                        getResources().obtainTypedArray(R.array.wizard_dialog_header_backgrounds);
                BackgroundAnimation headerBackgroundAnimation =
                        new CircleTransitionAnimation.Builder(getActivity())
                                .setListener(createHeaderAnimationListener(wizardDialog)).create();
                wizardDialog.setHeaderBackground(backgroundIds.getResourceId(position, 0),
                        headerBackgroundAnimation);
                backgroundIds.recycle();
                TypedArray iconIds =
                        getResources().obtainTypedArray(R.array.wizard_dialog_header_icons);
                DrawableAnimation headerIconAnimation =
                        new ScaleTransitionAnimation.Builder(getActivity()).create();
                wizardDialog.setHeaderIcon(iconIds.getResourceId(position, 0), headerIconAnimation);
                iconIds.recycle();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }

        };
    }

    /**
     * Creates and returns a listener, which allows to reset a wizard dialog's header background to
     * default, when its animation is finished.
     *
     * @param wizardDialog The wizard dialog as an instance of the class {@link WizardDialog}
     * @return The listener, which has been created, as an instance of the type {@link
     * AnimationListener}
     */
    private AnimationListener createHeaderAnimationListener(
            @NonNull final WizardDialog wizardDialog) {
        return new AnimationListener() {

            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
                BackgroundAnimation animation =
                        new CrossFadeTransitionAnimation.Builder(getActivity())
                                .setDuration(duration).create();
                wizardDialog.setHeaderBackground(R.drawable.dialog_header_background, animation);
            }

        };
    }

    /**
     * Adds a new fragment to a builder, which allows to create wizard dialogs.
     *
     * @param builder The builder, the fragment should be added to, as an instance of the class {@link
     *                WizardDialog.Builder}
     * @param index   The index of the fragment, which should be added
     */
    private void addFragment(@NonNull final WizardDialog.Builder builder, final int index) {
        Bundle arguments = new Bundle();
        arguments.putInt(DialogFragment.INDEX_EXTRA, index);
        CharSequence title = shouldHeaderBeShown() ? null :
                String.format(getString(R.string.dialog_tab_text), index);
        builder.addFragment(title, DialogFragment.class, arguments);
    }

    /**
     * Shows a specific toast and cancels a previous one, if existing.
     *
     * @param text The text of the toast, which should be shown, as a {@link String}, or null, if no
     *             toast should be shown
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
     * @param builder The builder, which should be configured, as an instance of the class {@link
     *                AbstractHeaderDialogBuilder}
     */
    private void configureHeaderDialogBuilder(@NonNull final AbstractHeaderDialogBuilder builder) {
        builder.setFullscreen(shouldUseFullscreen());

        if (shouldTitleBeShown()) {
            builder.setTitle(getDialogTitle());
        }

        if (shouldMessageBeShown()) {
            builder.setMessage(getDialogMessage());
        }

        if (shouldIconBeShown()) {
            builder.setIcon(R.drawable.ic_info_72dp);
            builder.setIconTint(ContextCompat.getColor(getActivity(), R.color.color_accent));
        }

        if (shouldHeaderBeShown()) {
            builder.showHeader(true);
            builder.setHeaderBackground(R.drawable.dialog_header_background);
            builder.setHeaderIcon(R.drawable.ic_message_alert_72dp);
            builder.setHeaderIconTint(Color.WHITE);
        }
    }

    /**
     * Configures a builder, which allows to create button bar dialogs, depending on the app's
     * settings.
     *
     * @param builder The builder, which should be configured as an instance of the class {@link
     *                AbstractButtonBarDialogBuilder}
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
     * Returns, whether the list items of the example dialogs should have an icon, or not.
     *
     * @return True, if the list items should have an icon, false otherwise
     */
    private boolean shouldListItemIconsBeShown() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.show_list_item_icon_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.show_list_item_icon_preference_default_value);
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
     * Returns, whether animations should be used to show or hide dialogs, or not.
     *
     * @return True, if animations should be used, false otherwise
     */
    private boolean shouldUseAnimations() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.animation_preference_key);
        boolean defaultValue = getResources().getBoolean(R.bool.animation_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Returns, whether dialogs should be shown fullscreen, or not.
     *
     * @return True, if dialogs should be shown fullscreen, false otherwise
     */
    private boolean shouldUseFullscreen() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String key = getString(R.string.fullscreen_preference_key);
        boolean defaultValue =
                getResources().getBoolean(R.bool.fullscreen_preference_default_value);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Shows an alert dialog using a circle reveal animation.
     *
     * @param view The view, which is used to show the dialog, as an instance of the class {@link View}
     */
    public final void showAlertDialog(@NonNull final View view) {
        initializeAlertDialog();
        alertDialog.setShowAnimation(createCircleRevealAnimation(view));
        alertDialog.setDismissAnimation(createCircleRevealAnimation(view));
        alertDialog.setCancelAnimation(createCircleRevealAnimation(view));
        alertDialog.show();
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDialogs();
        initializeThemePreference();
        initializeFullscreenPreference();
        initializeShowAlertDialogPreference();
        initializeShowListDialogPreference();
        initializeShowSingleChoiceListDialogPreference();
        initializeShowMultipleChoiceListDialogPreference();
        initializeShowCustomDialogPreference();
        initializeShowProgressDialogPreference();
        initializeShowEditTextDialogPreference();
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
            Bundle editTextDialogState = savedInstanceState.getBundle(EDIT_TEXT_DIALOG_STATE_EXTRA);

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

            if (editTextDialogState != null) {
                editTextDialog.onRestoreInstanceState(editTextDialogState);
            }
        }
    }

    @Override
    public final void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
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
        outState.putBundle(EDIT_TEXT_DIALOG_STATE_EXTRA, editTextDialog.onSaveInstanceState());
    }

}