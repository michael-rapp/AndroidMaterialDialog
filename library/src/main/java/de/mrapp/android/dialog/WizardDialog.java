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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import de.mrapp.android.dialog.adapter.ViewPagerAdapter;
import de.mrapp.android.dialog.builder.AbstractHeaderDialogBuilder;
import de.mrapp.android.util.datastructure.Triple;

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
public class WizardDialog extends AbstractHeaderDialogFragment implements OnPageChangeListener {

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
            return new WizardDialog(themeResourceId);
        }

    }

    /**
     * A list, which contains the fragments, which are contained by the dialog.
     */
    private final List<Triple<CharSequence, Class<? extends Fragment>, Bundle>> fragments;

    /**
     * The adapter, which is used to manage the dialog's fragments.
     */
    private ViewPagerAdapter viewPagerAdapter;

    /**
     * The view pager, which is used to show the dialog's fragments.
     */
    private ViewPager viewPager;

    /**
     * The tab layout, which allows to switch between the dialog's fragments.
     */
    private TabLayout tabLayout;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and provides a navigation for switching between multiple fragments.
     *
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    @SuppressLint("ValidFragment")
    protected WizardDialog(@StyleRes final int themeResourceId) {
        super(themeResourceId);
        fragments = new ArrayList<>();
        setView(R.layout.wizard_dialog);
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and provides a navigation for switching between multiple fragments.
     */
    public WizardDialog() {
        this(-1);
    }

    /**
     * Returns the view pager, which is contained by the dialog.
     *
     * @return The view pager, which is contained by the dialog, as an instance of the class {@link
     * ViewPager} or null, if the dialog does not show any fragments or has not been shown yet
     */
    public final ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Returns the tab layout, which is contained by the dialog.
     *
     * @return The tab layout, which is contained by the dialog, as an instance of the class {@link
     * TabLayout} or null, if the dialog does not show any fragments or has not been shown yet
     */
    public final TabLayout getTabLayout() {
        return tabLayout;
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     */
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(fragmentClass, null);
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     * @param arguments
     *         A bundle, which should be passed to the fragment, when it is shown, as an instance of
     *         the class {@link Bundle} or null, if no arguments should be passed to the fragment
     */
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        addFragment(null, fragmentClass, arguments);
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param resourceId
     *         The resource id of the title of the fragment, which should be added, as an {@link
     *         Integer}  value. The resource id must correspond to a valid string resource
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     */
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(resourceId, fragmentClass, null);
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param resourceId
     *         The resource id of the title of the fragment, which should be added, as an {@link
     *         Integer}  value. The resource id must correspond to a valid string resource
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     * @param arguments
     *         A bundle, which should be passed to the fragment, when it is shown, as an instance of
     *         the class {@link Bundle} or null, if no arguments should be passed to the fragment
     */
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        addFragment(getContext().getText(resourceId), fragmentClass, arguments);
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param title
     *         The title of the fragment, which should be added, as an instance of the type {@link
     *         CharSequence} or null, if no title should be set
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     */
    public final void addFragment(@Nullable final CharSequence title,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(title, fragmentClass, null);
    }

    /**
     * Adds a new fragment to the dialog.
     *
     * @param title
     *         The title of the fragment, which should be added, as an instance of the type {@link
     *         CharSequence} or null, if no title should be set
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     * @param arguments
     *         A bundle, which should be passed to the fragment, when it is shown, as an instance of
     *         the class {@link Bundle} or null, if no arguments should be passed to the fragment
     */
    public final void addFragment(@Nullable final CharSequence title,
                                  @NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        ensureNotNull(fragmentClass, "The fragment class may not be null");
        fragments.add(new Triple<CharSequence, Class<? extends Fragment>, Bundle>(title,
                fragmentClass, arguments));

        if (viewPagerAdapter != null) {
            viewPagerAdapter.addItem(title, fragmentClass, arguments);
        }
    }

    /**
     * Removes the fragment at a specific index from the dialog>.
     *
     * @param index
     *         The index of the fragment, which should be removed, as an {@link Integer value}
     */
    public final void removeFragment(final int index) {
        fragments.remove(index);

        if (viewPagerAdapter != null) {
            viewPagerAdapter.removeItem(index);
        }
    }

    /**
     * Removes all fragments from the dialog.
     */
    public final void clearFragments() {
        fragments.clear();

        if (viewPagerAdapter != null) {
            viewPagerAdapter.clear();
        }
    }

    /**
     * Returns the index of a specific fragment.
     *
     * @param fragmentClass
     *         The class of the fragment, whose index should be returned, as an instance of the
     *         class {@link Class}. The class may not be null
     * @return The index of the given fragment as an {@link Integer} value or -1, if the adapter
     * does not contain the given fragment
     */
    public final int indexOfFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
        ensureNotNull(fragmentClass, "The fragment class may not be null");

        for (int i = 0; i < fragments.size(); i++) {
            Triple<CharSequence, Class<? extends Fragment>, Bundle> item = fragments.get(i);

            if (item.second.equals(fragmentClass)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the number of fragments, which are contained by the dialog.
     *
     * @return The number of fragments, which are contained by the dialog, as an {@link Integer}
     * value
     */
    public final int getFragmentCount() {
        return fragments.size();
    }

    @Override
    public final void onPageScrolled(final int position, final float positionOffset,
                                     final int positionOffsetPixels) {

    }

    @Override
    public final void onPageSelected(final int position) {

    }

    @Override
    public final void onPageScrollStateChanged(final int state) {

    }

}