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
package de.mrapp.android.dialog.decorator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.adapter.ViewPagerAdapter;
import de.mrapp.android.dialog.model.HeaderDialog;
import de.mrapp.android.util.datastructure.Triple;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and provides a navigation
 * for switching between multiple fragments.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class WizardDialogDecorator extends AbstractDialogFragmentDecorator<HeaderDialog>
        implements de.mrapp.android.dialog.model.WizardDialogDecorator, OnPageChangeListener {

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
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * provides a navigation for switching between multiple fragments..
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the generic type DialogType. The dialog may not be null
     */
    public WizardDialogDecorator(@NonNull final HeaderDialog dialog) {
        super(dialog);
        fragments = new ArrayList<>();
    }

    @Override
    public final ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public final TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(fragmentClass, null);
    }

    @Override
    public final void addFragment(@NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        addFragment(null, fragmentClass, arguments);
    }

    @Override
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(resourceId, fragmentClass, null);
    }

    @Override
    public final void addFragment(@StringRes final int resourceId,
                                  @NonNull final Class<? extends Fragment> fragmentClass,
                                  @Nullable final Bundle arguments) {
        addFragment(getContext().getText(resourceId), fragmentClass, arguments);
    }

    @Override
    public final void addFragment(@Nullable final CharSequence title,
                                  @NonNull final Class<? extends Fragment> fragmentClass) {
        addFragment(title, fragmentClass, null);
    }

    @Override
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

    @Override
    public final void removeFragment(final int index) {
        fragments.remove(index);

        if (viewPagerAdapter != null) {
            viewPagerAdapter.removeItem(index);
        }
    }

    @Override
    public final void clearFragments() {
        fragments.clear();

        if (viewPagerAdapter != null) {
            viewPagerAdapter.clear();
        }
    }

    @Override
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

    @Override
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

    @Override
    protected final void onAttach(@NonNull final View view,
                                  @NonNull final FragmentManager fragmentManager) {
        View viewPagerView = view.findViewById(R.id.view_pager);

        if (viewPagerView instanceof ViewPager) {
            viewPagerAdapter = new ViewPagerAdapter(getContext(), fragmentManager, fragments);
            viewPager = (ViewPager) viewPagerView;
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    protected final void onDetach() {
        tabLayout = null;
        viewPager = null;
        viewPagerAdapter = null;
    }

}