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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.WizardDialog.TabPosition;
import de.mrapp.android.dialog.adapter.ViewPagerAdapter;
import de.mrapp.android.dialog.model.HeaderDialog;
import de.mrapp.android.dialog.view.ViewPager;
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
     * The tabs, which indicate the currently shown fragment.
     */
    private TabLayout tabLayout;

    /**
     * The position of the tabs, which indicate the currently shown fragment.
     */
    private TabPosition tabPosition;

    /**
     * True, if the tabs, which indicate the currently shown fragment, are enabled, false
     * otherwise.
     */
    private boolean tabLayoutEnabled;

    /**
     * True, if the tabs, which indicate the currently shown fragment, are shown, false otherwise.
     */
    private boolean tabLayoutShown;

    /**
     * The height of the indicator, which indicates the currently shown fragment.
     */
    private int tabIndicatorHeight;

    /**
     * The color of the indicator, which indicates the currently shown fragment.
     */
    private int tabIndicatorColor;

    /**
     * The text color of the tabs, which indicate the currently shown fragment.
     */
    private int tabTextColor;

    /**
     * The selected text color of the tabs, which indicate the currently shown fragment.
     */
    private int tabSelectedTextColor;

    /**
     * True, if switching between fragments using swipe gestures is enabled, false otherwise.
     */
    private boolean swipeEnabled;

    /**
     * Inflates the tab layout, which indicates the currently shown fragment.
     */
    private void inflateTabLayout() {
        if (getView() != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            ViewGroup headerContainer = (ViewGroup) getView().findViewById(R.id.header);
            ViewGroup contentContainer = (ViewGroup) getView().findViewById(R.id.content_container);

            if (tabLayout != null) {
                headerContainer.removeViewInLayout(tabLayout);
                contentContainer.removeView(tabLayout);
                tabLayout = null;
            }

            if (getDialog().isHeaderShown() && getTabPosition() != TabPosition.NO_HEADER &&
                    ((TextUtils.isEmpty(getDialog().getTitle()) &&
                            TextUtils.isEmpty(getDialog().getMessage())) ||
                            getTabPosition() == TabPosition.PREFER_HEADER)) {
                tabLayout = (TabLayout) layoutInflater
                        .inflate(R.layout.wizard_dialog_tab_layout, headerContainer, false);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.BOTTOM;
                headerContainer.addView(tabLayout, layoutParams);
            } else {
                tabLayout = (TabLayout) layoutInflater
                        .inflate(R.layout.wizard_dialog_tab_layout, contentContainer, false);
                contentContainer.addView(tabLayout, 0);
            }

            tabLayout.setupWithViewPager(viewPager);
        }
    }

    /**
     * Adapts the tab layout, which indicates the currently shown fragment.
     */
    private void adaptTabLayout() {
        adaptTabLayoutEnableState();
        adaptTabLayoutVisibility();
        adaptTabIndicatorHeight();
        adaptTabIndicatorColor();
        adaptTabTextColor();
    }

    /**
     * Adapts the enable state of the tab layout, which indicates the currently shown fragment.
     */
    private void adaptTabLayoutEnableState() {
        if (tabLayout != null) {
            LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
            tabStrip.setEnabled(tabLayoutEnabled);

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setEnabled(tabLayoutEnabled);
            }
        }
    }

    /**
     * Adapts the visibility of the tab layout, which indicates the currently shown fragment.
     */
    private void adaptTabLayoutVisibility() {
        if (tabLayout != null) {
            tabLayout.setVisibility(tabLayoutShown ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Adapts the height of the indicator, which indicates the currently shown fragment.
     */
    private void adaptTabIndicatorHeight() {
        if (tabLayout != null) {
            tabLayout.setSelectedTabIndicatorHeight(tabIndicatorHeight);
        }
    }

    /**
     * Adapts the color of the indicator, which indicates the currently shown fragment.
     */
    private void adaptTabIndicatorColor() {
        if (tabLayout != null) {
            tabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);
        }
    }

    /**
     * Adapts the text color of the tabs, which indicate the currently shown fragment.
     */
    private void adaptTabTextColor() {
        if (tabLayout != null) {
            tabLayout.setTabTextColors(tabTextColor, tabSelectedTextColor);
        }
    }

    /**
     * Adapts the view pager, which is used to show the dialog's fragments.
     */
    private void adaptViewPager() {
        if (viewPager != null) {
            viewPager.enableSwipe(swipeEnabled);
        }
    }

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
    public final TabPosition getTabPosition() {
        return tabPosition;
    }

    @Override
    public final void setTabPosition(@NonNull final TabPosition tabPosition) {
        this.tabPosition = tabPosition;
        inflateTabLayout();
        adaptTabLayout();
    }

    @Override
    public final boolean isTabLayoutEnabled() {
        return tabLayoutEnabled;
    }

    @Override
    public final void enableTabLayout(final boolean enable) {
        this.tabLayoutEnabled = enable;
        adaptTabLayoutEnableState();
    }

    @Override
    public final boolean isTabLayoutShown() {
        return tabLayoutShown;
    }

    @Override
    public final void showTabLayout(final boolean show) {
        this.tabLayoutShown = show;
        adaptTabLayoutVisibility();
    }

    @Override
    public final int getTabIndicatorHeight() {
        return tabIndicatorHeight;
    }

    @Override
    public final void setTabIndicatorHeight(final int height) {
        this.tabIndicatorHeight = height;
        adaptTabIndicatorHeight();
    }

    @Override
    public final int getTabIndicatorColor() {
        return tabIndicatorColor;
    }

    @Override
    public final void setTabIndicatorColor(@ColorInt final int color) {
        this.tabIndicatorColor = color;
        adaptTabIndicatorColor();
    }

    @Override
    public final int getTabTextColor() {
        return tabTextColor;
    }

    @Override
    public final void setTabTextColor(@ColorInt final int textColor) {
        this.tabTextColor = textColor;
        adaptTabTextColor();
    }

    @Override
    public final int getTabSelectedTextColor() {
        return tabSelectedTextColor;
    }

    @Override
    public final void setTabSelectedTextColor(@ColorInt final int selectedTextColor) {
        this.tabSelectedTextColor = selectedTextColor;
        adaptTabTextColor();
    }

    @Override
    public final boolean isSwipeEnabled() {
        return swipeEnabled;
    }

    @Override
    public final void enableSwipe(final boolean enable) {
        this.swipeEnabled = enable;
        adaptViewPager();
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
            inflateTabLayout();
            adaptTabLayout();
        }
    }

    @Override
    protected final void onDetach() {
        tabLayout = null;
        viewPager = null;
        viewPagerAdapter = null;
    }

}