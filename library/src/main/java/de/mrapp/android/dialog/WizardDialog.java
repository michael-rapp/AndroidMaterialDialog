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
package de.mrapp.android.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.widget.Button;

import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.builder.AbstractHeaderDialogBuilder;
import de.mrapp.android.dialog.model.WizardDialogDecorator;
import de.mrapp.android.util.ThemeUtil;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices and provides a navigation for switching between multiple fragments.
 *
 * For creating or showing such dialogs, the methods <code>WizardDialog#create()</code> or
 * <code>WizardDialog#show()</code> of the builder {@link WizardDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.0.0
 */
public class WizardDialog extends AbstractHeaderDialogFragment implements WizardDialogDecorator {

    /**
     * Defines the interface a class, which should be notified when the user navigates within a
     * {@link WizardDialog}, by using its next-, back- and finish-button. The return values of the
     * interface's methods allow to take influence on the navigation, e.g. if the currently shown
     * fragment should be validated.
     */
    public interface WizardListener {

        /**
         * The method, which is invoked, when the user wants to navigate to the next fragment of the
         * dialog.
         *
         * @param position
         *         The position of the currently shown fragment as an {@link Integer} value
         * @param fragment
         *         The currently shown fragment as an instance of the class Fragment
         * @return True, if navigating to the next fragment should be allowed, false otherwise
         */
        boolean onNext(int position, @NonNull Fragment fragment);

        /**
         * The method, which is invoked, when the user wants to navigate to the previous frament of
         * the dialog.
         *
         * @param position
         *         The position of the currently shown fragment as an {@link Integer} value
         * @param fragment
         *         The currently shown fragment as an instance of the class Fragment
         * @return True, if navigating to the previous fragment should be allowed, false otherwise
         */
        boolean onPrevious(int position, @NonNull Fragment fragment);

        /**
         * The method, which is invoked, when the user wants to finish the last fragment of the
         * dialog.
         *
         * @param position
         *         The position of the currently shown fragment as an {@link Integer} value
         * @param fragment
         *         The currently shown fragment as an instance of the class Fragment
         * @return True, if finishing the dialog should be allowed, false otherwise
         */
        boolean onFinish(int position, @NonNull Fragment fragment);

    }

    /**
     * Contains all possible positions of the dialog's tabs.
     */
    public enum TabPosition {

        /**
         * If the tabs should be shown in the header, if the dialog contains a header and if no
         * title and message are shown.
         */
        USE_HEADER(0),

        /**
         * If the tabs should be shown in the header, if the dialog contains a header, regardless of
         * whether a title or message is shown.
         */
        PREFER_HEADER(1),

        /**
         * If the tabs should never be shown in the header.
         */
        NO_HEADER(2);

        /**
         * The position's value.
         */
        private int value;

        /**
         * Creates a new position.
         *
         * @param value
         *         The position's value as an {@link Integer} value
         */
        TabPosition(final int value) {
            this.value = value;
        }

        /**
         * Returns the position's value.
         *
         * @return The positions value as an {@link Integer} value
         */
        public final int getValue() {
            return value;
        }

        /**
         * Creates and returns the position, which corresponds to a specific value.
         *
         * @param value
         *         The value, the position, which should be returned, corresponds to, as an {@link
         *         Integer} value
         * @return The position, which corresponds to the given value as a value of the enum {@link
         * TabPosition}
         */
        public static TabPosition fromValue(final int value) {
            for (TabPosition position : values()) {
                if (position.getValue() == value) {
                    return position;
                }
            }

            throw new IllegalArgumentException("Invalid enum value: " + value);
        }

    }

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices and provide a navigation for
     * switching between multiple fragments.
     */
    public static class Builder extends AbstractHeaderDialogBuilder<WizardDialog, Builder> {

        /**
         * Obtains, the position of the dialog's tab layout from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the enable state should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainTabPosition(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTabPosition});
            TabPosition defaultPosition = TabPosition.PREFER_HEADER;
            setTabPosition(TabPosition.fromValue(typedArray.getInt(0, defaultPosition.getValue())));
        }

        /**
         * Obtains, whether the dialog's tab layout should be enabled, or nor, from a specific
         * theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the enable state should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainEnableTabLayout(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogEnableTabLayout});
            enableTabLayout(typedArray.getBoolean(0, true));
        }

        /**
         * Obtains, whether the dialog's tab layout should be shown, or nor, from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the visibility should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainShowTabLayout(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogShowTabLayout});
            showTabLayout(typedArray.getBoolean(0, true));
        }

        /**
         * Obtains the height of the tab indicator from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the height should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainTabIndicatorHeight(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTabIndicatorHeight});
            int defaultHeight = getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_tab_indicator_height);
            setTabIndicatorHeight(typedArray.getDimensionPixelSize(0, defaultHeight));
        }

        /**
         * Obtains the color of the tab indicator from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainTabIndicatorColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTabIndicatorColor});
            int defaultColor =
                    ThemeUtil.getColor(getContext(), themeResourceId, R.attr.colorAccent);
            setTabIndicatorColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Obtains the text color of the tabs from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainTabTextColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTabTextColor});
            int defaultColor = ThemeUtil
                    .getColor(getContext(), themeResourceId, android.R.attr.textColorSecondary);
            setTabTextColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Obtains the selected text color of the tabs from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainTabSelectedTextColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTabSelectedTextColor});
            int defaultColor = ThemeUtil
                    .getColor(getContext(), themeResourceId, android.R.attr.textColorSecondary);
            setTabSelectedTextColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Obtains, whether switching between the dialog's fragment using swipe gestures should be
         * enabled, or not, from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the enable state should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainEnableSwipe(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogEnableSwipe});
            enableSwipe(typedArray.getBoolean(0, true));
        }

        /**
         * Obtains, whether the dialog's button should be shown, or not, from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the visibility should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainShowButtonBar(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogShowButtonBar});
            showButtonBar(typedArray.getBoolean(0, true));
        }

        /**
         * Obtains the button text color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainButtonTextColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogButtonTextColor});
            int defaultColor =
                    ThemeUtil.getColor(getContext(), themeResourceId, R.attr.colorAccent);
            setButtonTextColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Obtains the text color of the dialog's back button from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainBackButtonText(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogBackButtonText});
            CharSequence defaultText = getContext().getText(R.string.back_button_text);
            CharSequence text = typedArray.getText(0);
            setBackButtonText(!TextUtils.isEmpty(text) ? text : defaultText);
        }

        /**
         * Obtains the text color of the dialog's next button from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainNextButtonText(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogNextButtonText});
            CharSequence defaultText = getContext().getText(R.string.next_button_text);
            CharSequence text = typedArray.getText(0);
            setNextButtonText(!TextUtils.isEmpty(text) ? text : defaultText);
        }

        /**
         * Obtains the text color of the dialog's finish button from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the text color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainFinishButtonText(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogFinishButtonText});
            CharSequence defaultText = getContext().getText(R.string.finish_button_text);
            CharSequence text = typedArray.getText(0);
            setFinishButtonText(!TextUtils.isEmpty(text) ? text : defaultText);
        }

        /**
         * Obtains, whether the divider, which is located above the dialog's buttons, should be
         * shown, or not, from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the visibility should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainShowButtonBarDivider(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogShowButtonBarDivider});
            showButtonBarDivider(typedArray.getBoolean(0, false));
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and provide a
         * navigation for switching between multiple fragments.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and provide a
         * navigation for switching between multiple fragments.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
            getProduct().addFragment(fragmentClass);
            return self();
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @param arguments
         *         A bundle, which should be passed to the fragment, when it is shown, as an
         *         instance of the class {@link Bundle} or null, if no arguments should be passed to
         *         the fragment
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@NonNull final Class<? extends Fragment> fragmentClass,
                                         @Nullable final Bundle arguments) {
            getProduct().addFragment(fragmentClass, arguments);
            return self();
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title of the fragment, which should be added, as an {@link
         *         Integer}  value. The resource id must correspond to a valid string resource
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@StringRes final int resourceId,
                                         @NonNull final Class<? extends Fragment> fragmentClass) {
            getProduct().addFragment(resourceId, fragmentClass);
            return self();
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title of the fragment, which should be added, as an {@link
         *         Integer}  value. The resource id must correspond to a valid string resource
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @param arguments
         *         A bundle, which should be passed to the fragment, when it is shown, as an
         *         instance of the class {@link Bundle} or null, if no arguments should be passed to
         *         the fragment
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@StringRes final int resourceId,
                                         @NonNull final Class<? extends Fragment> fragmentClass,
                                         @Nullable final Bundle arguments) {
            getProduct().addFragment(resourceId, fragmentClass, arguments);
            return self();
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param title
         *         The title of the fragment, which should be added, as an instance of the type
         *         {@link CharSequence} or null, if no title should be set
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@Nullable final CharSequence title,
                                         @NonNull final Class<? extends Fragment> fragmentClass) {
            getProduct().addFragment(title, fragmentClass);
            return self();
        }

        /**
         * Adds a new fragment to the dialog, which is created by the builder.
         *
         * @param title
         *         The title of the fragment, which should be added, as an instance of the type
         *         {@link CharSequence} or null, if no title should be set
         * @param fragmentClass
         *         The class of the fragment, which should be added, as an instance of the class
         *         {@link Class}. The class may not be null
         * @param arguments
         *         A bundle, which should be passed to the fragment, when it is shown, as an
         *         instance of the class {@link Bundle} or null, if no arguments should be passed to
         *         the fragment
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addFragment(@Nullable final CharSequence title,
                                         @NonNull final Class<? extends Fragment> fragmentClass,
                                         @Nullable final Bundle arguments) {
            getProduct().addFragment(title, fragmentClass, arguments);
            return self();
        }

        /**
         * Sets the position of the tabs, which indicate the currently shown fragment.
         *
         * @param tabPosition
         *         The position, which should be set, as a value of the enum {@link TabPosition}.
         *         The position may either be <code>USE_HEADER</code>, <code>PREFER_HEADER</code> or
         *         <code>NO_HEADER</code>
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTabPosition(@NonNull final TabPosition tabPosition) {
            getProduct().setTabPosition(tabPosition);
            return self();
        }

        /**
         * Enables or disables the tabs, which indicate the currently shown fragment. When the tabs
         * are enabled, it is possible to switch between the fragments by clicking the tabs.
         *
         * @param enable
         *         True, if the tabs should be enabled, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder enableTabLayout(final boolean enable) {
            getProduct().enableTabLayout(enable);
            return self();
        }

        /**
         * Sets, whether the tabs, which indicate the currently shown fragment, should be shown
         * shown, or not.
         *
         * @param show
         *         True, if the tabs, which indicate the currently shown fragment, should be shown,
         *         false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder showTabLayout(final boolean show) {
            getProduct().showTabLayout(show);
            return self();
        }

        /**
         * Sets the height of the indicator, which indicates the currently shown fragment.
         *
         * @param height
         *         The height, which should be set, in pixels as an {@link Integer} value. The
         *         height must be at least 1
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTabIndicatorHeight(final int height) {
            getProduct().setTabIndicatorHeight(height);
            return self();
        }

        /**
         * Sets the color of the indicator, which indicates the currently shown fragment.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTabIndicatorColor(@ColorInt final int color) {
            getProduct().setTabIndicatorColor(color);
            return self();
        }

        /**
         * Sets the text color of the tabs, which indicates the currently shown fragment.
         *
         * @param textColor
         *         The text color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTabTextColor(@ColorInt final int textColor) {
            getProduct().setTabTextColor(textColor);
            return self();
        }

        /**
         * Sets the selected text color of the tabs, which indicate the currently shown fragment.
         *
         * @param selectedTextColor
         *         The selected text color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTabSelectedTextColor(@ColorInt final int selectedTextColor) {
            getProduct().setTabSelectedTextColor(selectedTextColor);
            return self();
        }

        /**
         * Sets, whether switching fragments using swipe gestures should be enabled, or not.
         *
         * @param enable
         *         True, if switching fragments using swipe gestures should be enabled, false
         *         otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder enableSwipe(final boolean enable) {
            getProduct().enableSwipe(enable);
            return self();
        }

        /**
         * Sets, whether the dialog's buttons should be shown, or not.
         *
         * @param show
         *         True, if the dialog's buttons should be shown, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder showButtonBar(final boolean show) {
            getProduct().showButtonBar(show);
            return self();
        }

        /**
         * Sets the color of the button texts of the dialog.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setButtonTextColor(@ColorInt final int color) {
            getProduct().setButtonTextColor(color);
            return self();
        }

        /**
         * Sets, whether the divider, which is located above the dialog's buttons, should be shown,
         * or not.
         *
         * @param show
         *         True, if the divider, which is located above the dialog's buttons, should be
         *         show, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder showButtonBarDivider(final boolean show) {
            getProduct().showButtonBarDivider(show);
            return self();
        }

        /**
         * Sets the text of the dialog's back button.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackButtonText(@StringRes final int resourceId) {
            getProduct().setBackButtonText(resourceId);
            return self();
        }

        /**
         * Sets the text of the dialog's back button.
         *
         * @param text
         *         The text, which should be set, as an instance of the class {@link CharSequence}.
         *         The text may neither be null, nor empty
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackButtonText(@NonNull final CharSequence text) {
            getProduct().setBackButtonText(text);
            return self();
        }

        /**
         * Sets the text of the dialog's next button.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNextButtonText(@StringRes final int resourceId) {
            getProduct().setNextButtonText(resourceId);
            return self();
        }

        /**
         * Sets the text of the dialog's next button.
         *
         * @param text
         *         The text, which should be set, as an instance of the class {@link CharSequence}.
         *         The text may neither be null, nor empty
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNextButtonText(@NonNull final CharSequence text) {
            getProduct().setNextButtonText(text);
            return self();
        }

        /**
         * Sets the text of the dialog's finish button.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setFinishButtonText(@StringRes final int resourceId) {
            getProduct().setFinishButtonText(resourceId);
            return self();
        }

        /**
         * Sets the text of the dialog's finish button.
         *
         * @param text
         *         The text, which should be set, as an instance of the class {@link CharSequence}.
         *         The text may neither be null, nor empty
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setFinishButtonText(@NonNull final CharSequence text) {
            getProduct().setFinishButtonText(text);
            return self();
        }

        /**
         * Adds a new listener, which should be notified, when the user navigates within the dialog,
         * which is created by the builder.
         *
         * Note, that the attached listeners are not stored using a dialog's
         * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
         * method must be called again after configuration changes, e.g when the orientation of the
         * device has changed, in order to re-register the listeners.
         *
         * @param listener
         *         The listener, which should be added, as an instance of the type {@link
         *         WizardListener}. The listener may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addWizardListener(@NonNull final WizardListener listener) {
            getProduct().addWizardListener(listener);
            return self();
        }

        /**
         * Adds a listener, which should be notified, when the page of the view page of the dialog,
         * which is created by the builder, has been changed.
         *
         * @param listener
         *         The listener, which should be added, as an instance of the type
         *         OnPageChangeListener. The listener may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addOnPageChangeListener(@NonNull final OnPageChangeListener listener) {
            getProduct().addOnPageChangeListener(listener);
            return self();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @param fragmentManager
         *         The fragment manager, which should be used by the dialog, as an instance of the
         *         class FragmentManager. The fragment manager may not be null
         * @param tag
         *         The tag, which should be used to show the dialog, as a {@link String} or null, if
         *         no tag should be used
         * @return The dialog, which has been shown, as an instance of the class {@link
         * WizardDialog}
         */
        public final WizardDialog show(@NonNull final FragmentManager fragmentManager,
                                       @Nullable final String tag) {
            ensureNotNull(fragmentManager, "The fragment manager may not be null");
            WizardDialog dialog = create();
            dialog.show(fragmentManager, tag);
            return dialog;
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @param fragmentTransaction
         *         The fragment transaction, which should be used to show the dialog, as an instance
         *         of the class FragmentTransaction. The fragment transaction may not be null
         * @param tag
         *         The tag, which should be used to show the dialog, as a {@link String} or null, if
         *         no tag should be used
         * @return The dialog, which has been shown, as an instance of the class {@link
         * WizardDialog}
         */
        public final WizardDialog show(@NonNull final FragmentTransaction fragmentTransaction,
                                       @Nullable final String tag) {
            ensureNotNull(fragmentTransaction, "The fragment transaction may not be null");
            WizardDialog dialog = create();
            dialog.show(fragmentTransaction, tag);
            return dialog;
        }

        @NonNull
        @Override
        protected final WizardDialog onCreateProduct() {
            WizardDialog dialog = new WizardDialog();
            dialog.setThemeResourceId(getThemeResourceId());
            dialog.setContext(getContext());
            return dialog;
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainTabPosition(themeResourceId);
            obtainEnableTabLayout(themeResourceId);
            obtainShowTabLayout(themeResourceId);
            obtainTabIndicatorHeight(themeResourceId);
            obtainTabIndicatorColor(themeResourceId);
            obtainTabTextColor(themeResourceId);
            obtainTabSelectedTextColor(themeResourceId);
            obtainEnableSwipe(themeResourceId);
            obtainShowButtonBar(themeResourceId);
            obtainButtonTextColor(themeResourceId);
            obtainBackButtonText(themeResourceId);
            obtainNextButtonText(themeResourceId);
            obtainFinishButtonText(themeResourceId);
            obtainShowButtonBarDivider(themeResourceId);
        }

    }

    /**
     * The decorator, which is used by the dialog.
     */
    private final de.mrapp.android.dialog.decorator.WizardDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and provides a navigation for switching between multiple fragments.
     */
    public WizardDialog() {
        decorator = new de.mrapp.android.dialog.decorator.WizardDialogDecorator(this);
        addDecorator(decorator);
        setCancelable(false);
        setView(R.layout.wizard_dialog_view_pager);
    }

    @Override
    public final void setScrollableArea(final Area area) {
        if (area == Area.CONTENT) {
            // The view pager expands to the dialog's height. It doesn't make sense to make it
            // scrollable
            super.setScrollableArea(null);
        } else {
            super.setScrollableArea(area);
        }
    }

    @Override
    public final void setScrollableArea(final Area top, final Area bottom) {
        if (top == Area.CONTENT && bottom == Area.CONTENT) {
            // The view pager expands to the dialog's height. It doesn't make sense to make it
            // scrollable
            super.setScrollableArea(null, null);
        } else {
            super.setScrollableArea(top, bottom);
        }
    }

    @Override
    public final ViewPager getViewPager() {
        return decorator.getViewPager();
    }

    @Override
    public final TabLayout getTabLayout() {
        return decorator.getTabLayout();
    }

    @Override
    public final Button getBackButton() {
        return decorator.getBackButton();
    }

    @Override
    public final Button getNextButton() {
        return decorator.getNextButton();
    }

    @Override
    public final Button getFinishButton() {
        return decorator.getFinishButton();
    }

    @Override
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
        decorator.addFragment(fragmentClass);
    }

    @Override
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        decorator.addFragment(fragmentClass, arguments);
    }

    @Override
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        decorator.addFragment(resourceId, fragmentClass);
    }

    @Override
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        decorator.addFragment(resourceId, fragmentClass, arguments);
    }

    @Override
    public final void addFragment(@Nullable final CharSequence title,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        decorator.addFragment(title, fragmentClass);
    }

    @Override
    public final void addFragment(@Nullable final CharSequence title,
                                  @NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        decorator.addFragment(title, fragmentClass, arguments);
    }

    @Override
    public final void removeFragment(final int index) {
        decorator.removeFragment(index);
    }

    @Override
    public final void clearFragments() {
        decorator.clearFragments();
    }

    @Override
    public final int indexOfFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
        return decorator.indexOfFragment(fragmentClass);
    }

    @Override
    public final int getFragmentCount() {
        return decorator.getFragmentCount();
    }

    @Override
    public final TabPosition getTabPosition() {
        return decorator.getTabPosition();
    }

    @Override
    public void setTabPosition(@NonNull final TabPosition tabPosition) {
        decorator.setTabPosition(tabPosition);
    }

    @Override
    public final boolean isTabLayoutEnabled() {
        return decorator.isTabLayoutEnabled();
    }

    @Override
    public final void enableTabLayout(final boolean enable) {
        decorator.enableTabLayout(enable);
    }

    @Override
    public final boolean isTabLayoutShown() {
        return decorator.isTabLayoutShown();
    }

    @Override
    public final void showTabLayout(final boolean show) {
        decorator.showTabLayout(show);
    }

    @Override
    public final int getTabIndicatorHeight() {
        return decorator.getTabIndicatorHeight();
    }

    @Override
    public final void setTabIndicatorHeight(final int height) {
        decorator.setTabIndicatorHeight(height);
    }

    @Override
    public final int getTabIndicatorColor() {
        return decorator.getTabIndicatorColor();
    }

    @Override
    public final void setTabIndicatorColor(@ColorInt final int color) {
        decorator.setTabIndicatorColor(color);
    }

    @Override
    public final int getTabTextColor() {
        return decorator.getTabTextColor();
    }

    @Override
    public final void setTabTextColor(@ColorInt final int textColor) {
        decorator.setTabTextColor(textColor);
    }

    @Override
    public final int getTabSelectedTextColor() {
        return decorator.getTabSelectedTextColor();
    }

    @Override
    public final void setTabSelectedTextColor(@ColorInt final int selectedTextColor) {
        decorator.setTabSelectedTextColor(selectedTextColor);
    }

    @Override
    public final boolean isSwipeEnabled() {
        return decorator.isSwipeEnabled();
    }

    @Override
    public final void enableSwipe(final boolean enable) {
        decorator.enableSwipe(enable);
    }

    @Override
    public final boolean isButtonBarShown() {
        return decorator.isButtonBarShown();
    }

    @Override
    public final void showButtonBar(final boolean show) {
        decorator.showButtonBar(show);
    }

    @Override
    public final int getButtonTextColor() {
        return decorator.getButtonTextColor();
    }

    @Override
    public final void setButtonTextColor(@ColorInt final int color) {
        decorator.setButtonTextColor(color);
    }

    @Override
    public final boolean isButtonBarDividerShown() {
        return decorator.isButtonBarDividerShown();
    }

    @Override
    public final void showButtonBarDivider(final boolean show) {
        decorator.showButtonBarDivider(show);
    }

    @Override
    public final int getButtonBarDividerColor() {
        return decorator.getButtonBarDividerColor();
    }

    @Override
    public final void setButtonBarDividerColor(final int color) {
        decorator.setButtonBarDividerColor(color);
    }

    @Override
    public final int getButtonBarDividerMargin() {
        return decorator.getButtonBarDividerMargin();
    }

    @Override
    public final void setButtonBarDividerMargin(final int margin) {
        decorator.setButtonBarDividerMargin(margin);
    }

    @Override
    public final CharSequence getBackButtonText() {
        return decorator.getBackButtonText();
    }

    @Override
    public final void setBackButtonText(@StringRes final int resourceId) {
        decorator.setBackButtonText(resourceId);
    }

    @Override
    public final void setBackButtonText(@NonNull final CharSequence text) {
        decorator.setBackButtonText(text);
    }

    @Override
    public final CharSequence getNextButtonText() {
        return decorator.getNextButtonText();
    }

    @Override
    public final void setNextButtonText(@StringRes final int resourceId) {
        decorator.setNextButtonText(resourceId);
    }

    @Override
    public final void setNextButtonText(@NonNull final CharSequence text) {
        decorator.setNextButtonText(text);
    }

    @Override
    public final CharSequence getFinishButtonText() {
        return decorator.getFinishButtonText();
    }

    @Override
    public final void setFinishButtonText(@StringRes final int resourceId) {
        decorator.setFinishButtonText(resourceId);
    }

    @Override
    public final void setFinishButtonText(@NonNull final CharSequence text) {
        decorator.setFinishButtonText(text);
    }

    @Override
    public final void addWizardListener(@NonNull final WizardListener listener) {
        decorator.addWizardListener(listener);
    }

    @Override
    public final void removeWizardListener(@NonNull final WizardListener listener) {
        decorator.addWizardListener(listener);
    }

    @Override
    public final void addOnPageChangeListener(@NonNull final OnPageChangeListener listener) {
        decorator.addOnPageChangeListener(listener);
    }

    @Override
    public final void removeOnPageChangeListener(@NonNull final OnPageChangeListener listener) {
        decorator.removeOnPageChangeListener(listener);
    }

    @Override
    public final void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        decorator.onSaveInstanceState(outState);
    }

    @Override
    protected final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        decorator.onRestoreInstanceState(savedInstanceState);
    }

}