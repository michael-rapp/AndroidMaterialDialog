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
package de.mrapp.android.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import de.mrapp.android.dialog.builder.AbstractHeaderDialogBuilder;
import de.mrapp.android.dialog.model.WizardDialogDecorator;

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
@SuppressLint("ValidFragment")
public class WizardDialog extends AbstractHeaderDialogFragment implements WizardDialogDecorator {

    /**
     * Contains all possible positions of the dialog's tabs.
     */
    public enum TabPosition {

        /**
         * If the tabs should be shown in the header, if the dialog contains a header and if no
         * title and message are shown.
         */
        USE_HEADER("use_header"),

        /**
         * If the tabs should be shown in the header, if the dialog contains a header, regardless of
         * whether a title or message is shown.
         */
        PREFER_HEADER("prefer_header"),

        /**
         * If the tabs should never be shown in the header.
         */
        NO_HEADER("no_header");

        /**
         * The position's value.
         */
        private String value;

        /**
         * Creates a new position.
         *
         * @param value
         *         The position's value
         */
        TabPosition(final String value) {
            this.value = value;
        }

        /**
         * Returns the position's value.
         *
         * @return The positions value as a {@link String}
         */
        public final String getValue() {
            return value;
        }

        /**
         * Creates and returns the position, which corresponds to a specific value.
         *
         * @param value
         *         The value, the position, which should be returned, corresponds to, as a {@link
         *         String}. The value may not be null
         * @return The position, which corresponds to the given value as a value of the enum {@link
         * TabPosition}
         */
        public static TabPosition fromValue(@NonNull final String value) {
            ensureNotNull(value, "The value may not be null");

            for (TabPosition position : values()) {
                if (position.getValue().equals(value)) {
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
            getDialog().addFragment(fragmentClass);
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
            getDialog().addFragment(fragmentClass, arguments);
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
            getDialog().addFragment(resourceId, fragmentClass);
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
            getDialog().addFragment(resourceId, fragmentClass);
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
            getDialog().addFragment(title, fragmentClass);
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
            getDialog().addFragment(title, fragmentClass, arguments);
            return self();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @param fragmentManager
         *         The fragment manager, which should be used by the dialog, as an instance of the
         *         class {@link FragmentManager}. The fragment manager may not be null
         * @return The dialog, which has been shown, as an instance of the class {@link
         * WizardDialog}
         */
        public final WizardDialog show(@NonNull final FragmentManager fragmentManager) {
            ensureNotNull(fragmentManager, "The fragment manager may not be null");
            WizardDialog dialog = create();
            dialog.show(fragmentManager, null);
            return dialog;
        }

        @Override
        protected final WizardDialog onCreateDialog(@NonNull final Context context,
                                                    @StyleRes final int themeResourceId) {
            return new WizardDialog(context, themeResourceId);
        }

    }

    /**
     * The decorator, which is used by the dialog.
     */
    private final de.mrapp.android.dialog.decorator.WizardDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and provides a navigation for switching between multiple fragments.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected WizardDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new de.mrapp.android.dialog.decorator.WizardDialogDecorator(this);
        setView(R.layout.wizard_dialog_view_pager);
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
        decorator.getButtonBarDividerColor();
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
    protected final void onAttachDecorators(@NonNull final View view,
                                            @NonNull final FragmentManager fragmentManager) {
        super.onAttachDecorators(view, fragmentManager);
        decorator.attach(view, fragmentManager);
    }

    @Override
    protected final void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}