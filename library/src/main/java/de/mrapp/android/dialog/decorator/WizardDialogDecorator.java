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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.WizardDialog.TabPosition;
import de.mrapp.android.dialog.WizardDialog.WizardListener;
import de.mrapp.android.dialog.adapter.ViewPagerAdapter;
import de.mrapp.android.dialog.model.HeaderDialog;
import de.mrapp.android.dialog.view.ViewPager;
import de.mrapp.android.util.datastructure.Triple;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotEmpty;
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
     * The listeners, which should be notified, when the user navigates within the dialog.
     */
    private final Set<WizardListener> listeners;

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
     * The parent view of the layout, which is used to show the dialog's buttons.
     */
    private ViewGroup buttonBarContainer;

    /**
     * The back button of the dialog.
     */
    private Button backButton;

    /**
     * The next button of the dialog.
     */
    private Button nextButton;

    /**
     * The finish button of the dialog.
     */
    private Button finishButton;

    /**
     * The divider, which is shown above the dialog's buttons.
     */
    private View buttonBarDivider;

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
     * True, if the dialogÄs buttons are shown, false otherwise.
     */
    private boolean buttonBarShown;

    /**
     * The color of the button texts of the dialog.
     */
    private int buttonTextColor;

    /**
     * The text of the back button of the dialog.
     */
    private CharSequence backButtonText;

    /**
     * The text of the next button of the dialog.
     */
    private CharSequence nextButtonText;

    /**
     * The text of the finish button of the dialog.
     */
    private CharSequence finishButtonText;

    /**
     * True, if the divider, which is located above the dialog's buttons, is shown, false
     * otherwise.
     */
    private boolean showButtonBarDivider;

    /**
     * The color of the divider, which is located above the dialog's buttons.
     */
    private int buttonBarDividerColor;

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
     * Inflates the layout, which is used to show the dialog's buttons.
     */
    private void inflateButtonBar() {
        ViewGroup rootView = (ViewGroup) getView();

        if (rootView != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            buttonBarContainer = (ViewGroup) layoutInflater
                    .inflate(R.layout.button_bar_container, rootView, false);
            rootView.addView(buttonBarContainer);
            View view = layoutInflater
                    .inflate(R.layout.horizontal_button_bar, buttonBarContainer, false);
            buttonBarContainer.addView(view);
            nextButton = (Button) view.findViewById(android.R.id.button1);
            finishButton = (Button) view.findViewById(android.R.id.button2);
            backButton = (Button) view.findViewById(android.R.id.button3);
            buttonBarDivider = view.findViewById(R.id.button_bar_divider);
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
     * Adapts the text color of the dialog's buttons.
     */
    private void adaptButtonTextColor() {
        if (backButton != null) {
            backButton.setTextColor(buttonTextColor);
        }

        if (nextButton != null) {
            nextButton.setTextColor(buttonTextColor);
        }

        if (finishButton != null) {
            finishButton.setTextColor(buttonTextColor);
        }
    }

    /**
     * Adapts the dialog's back button.
     */
    private void adaptBackButton() {
        if (backButton != null) {
            backButton.setText(backButtonText.toString().toUpperCase(Locale.getDefault()));
            backButton.setOnClickListener(createBackButtonListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to show the previous fragment, when the
     * corresponding button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createBackButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                int selectedIndex = viewPager.getCurrentItem();

                if (notifyOnPrevious(selectedIndex)) {
                    viewPager.setCurrentItem(selectedIndex - 1);
                }
            }

        };
    }

    /**
     * Adapts the dialog's next button.
     */
    private void adaptNextButton() {
        if (nextButton != null) {
            nextButton.setText(nextButtonText.toString().toUpperCase(Locale.getDefault()));
            nextButton.setOnClickListener(createNextButtonListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to show the next fragment, when the
     * corresponding button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createNextButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                int selectedIndex = viewPager.getCurrentItem();

                if (notifyOnNext(selectedIndex)) {
                    viewPager.setCurrentItem(selectedIndex + 1);
                }
            }

        };
    }

    /**
     * Adapts the dialog's finish button.
     */
    private void adaptFinishButton() {
        if (finishButton != null) {
            finishButton.setText(finishButtonText.toString().toUpperCase(Locale.getDefault()));
            finishButton.setOnClickListener(createFinishButtonListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to close the dialog, when the corresponding
     * button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createFinishButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                int selectedIndex = viewPager.getCurrentItem();

                if (notifyOnFinish(selectedIndex)) {
                    getDialog().dismiss();
                }
            }

        };
    }

    /**
     * Adapts the visibility of the dialog's buttons.
     */
    private void adaptButtonBarVisibility() {
        if (buttonBarContainer != null) {
            buttonBarContainer.setVisibility(buttonBarShown ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Adapts the visibility of the divider, which is shown above the dialog's buttons.
     */
    private void adaptButtonBarDividerVisibility() {
        if (buttonBarDivider != null) {
            buttonBarDivider.setVisibility(showButtonBarDivider ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Adapts the color of the divider, which is shown above the dialog's buttons.
     */
    private void adaptButtonBarDividerColor() {
        if (buttonBarDivider != null) {
            buttonBarDivider.setBackgroundColor(buttonBarDividerColor);
        }
    }

    /**
     * Adapts the visibility of the dialog's buttons, depending on the currently shown fragment.
     */
    private void adaptButtonVisibility() {
        if (viewPager != null && viewPagerAdapter != null && backButton != null &&
                nextButton != null && finishButton != null) {
            int selectedIndex = viewPager.getCurrentItem();
            backButton.setVisibility(selectedIndex > 0 ? View.VISIBLE : View.GONE);
            nextButton.setVisibility(
                    selectedIndex < viewPagerAdapter.getCount() - 1 ? View.VISIBLE : View.GONE);
            finishButton.setVisibility(
                    selectedIndex == viewPagerAdapter.getCount() - 1 ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Notifies all listeners, when the next fragment is about to be shown.
     *
     * @param index
     *         The index of the fragment, which is about to be shown, as an {@link Integer} value
     * @return True, if the fragment is allowed to be shown, false otherwise
     */
    private boolean notifyOnNext(final int index) {
        boolean result = true;

        for (WizardListener listener : listeners) {
            result &= listener.onNext(index, viewPagerAdapter.getItem(index));
        }

        return result;
    }

    /**
     * Notifies all listeners, when the previous fragment is about to be shown.
     *
     * @param index
     *         The index of the fragment, which is about to be shown, as an {@link Integer} value
     * @return True, if the fragment is allowed to be shown, false otherwise
     */
    private boolean notifyOnPrevious(final int index) {
        boolean result = true;

        for (WizardListener listener : listeners) {
            result &= listener.onPrevious(index, viewPagerAdapter.getItem(index));
        }

        return result;
    }

    /**
     * Notifies all listeners, when the last fragment is about to be finished.
     *
     * @param index
     *         The index of the fragment, which is about to be finished, as an {@link Integer}
     *         value
     * @return True, if the last fragment is allowed to be finished, false otherwise
     */
    private boolean notifyOnFinish(final int index) {
        boolean result = true;

        for (WizardListener listener : listeners) {
            result &= listener.onFinish(index, viewPagerAdapter.getItem(index));
        }

        return result;
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
        this.fragments = new ArrayList<>();
        this.listeners = new LinkedHashSet<>();
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
    public final Button getBackButton() {
        return backButton;
    }

    @Override
    public final Button getNextButton() {
        return nextButton;
    }

    @Override
    public final Button getFinishButton() {
        return finishButton;
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
        ensureNotNull(tabPosition, "The tab position may not be null");
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
        ensureAtLeast(height, 1, "The height must be at least 1");
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
    public final boolean isButtonBarShown() {
        return buttonBarShown;
    }

    @Override
    public final void showButtonBar(final boolean show) {
        this.buttonBarShown = show;
        adaptButtonBarVisibility();
    }

    @Override
    public final int getButtonTextColor() {
        return buttonTextColor;
    }

    @Override
    public final void setButtonTextColor(@ColorInt final int color) {
        this.buttonTextColor = color;
        adaptButtonTextColor();
    }

    @Override
    public final boolean isButtonBarDividerShown() {
        return showButtonBarDivider;
    }

    @Override
    public final void showButtonBarDivider(final boolean show) {
        this.showButtonBarDivider = show;
        adaptButtonBarDividerVisibility();
    }

    @Override
    public final int getButtonBarDividerColor() {
        return buttonBarDividerColor;
    }

    @Override
    public final void setButtonBarDividerColor(final int color) {
        this.buttonBarDividerColor = color;
        adaptButtonBarDividerColor();
    }

    @Override
    public final CharSequence getBackButtonText() {
        return backButtonText;
    }

    @Override
    public final void setBackButtonText(@StringRes final int resourceId) {
        setBackButtonText(getContext().getText(resourceId));
    }

    @Override
    public final void setBackButtonText(@NonNull final CharSequence text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        this.backButtonText = text;
        adaptBackButton();
    }

    @Override
    public final CharSequence getNextButtonText() {
        return nextButtonText;
    }

    @Override
    public final void setNextButtonText(@StringRes final int resourceId) {
        setNextButtonText(getContext().getText(resourceId));
    }

    @Override
    public final void setNextButtonText(@NonNull final CharSequence text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        this.nextButtonText = text;
        adaptNextButton();
    }

    @Override
    public final CharSequence getFinishButtonText() {
        return finishButtonText;
    }

    @Override
    public final void setFinishButtonText(@StringRes final int resourceId) {
        setFinishButtonText(getContext().getText(resourceId));
    }

    @Override
    public final void setFinishButtonText(@NonNull final CharSequence text) {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        this.finishButtonText = text;
        adaptFinishButton();
    }

    @Override
    public final void addWizardListener(@NonNull final WizardListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.add(listener);
    }

    @Override
    public final void removeWizardListener(@NonNull final WizardListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.remove(listener);
    }

    @Override
    public final void onPageScrolled(final int position, final float positionOffset,
                                     final int positionOffsetPixels) {

    }

    @Override
    public final void onPageSelected(final int position) {
        adaptButtonVisibility();
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
            adaptViewPager();
            inflateButtonBar();
            adaptButtonTextColor();
            adaptBackButton();
            adaptNextButton();
            adaptFinishButton();
            adaptButtonBarVisibility();
            adaptButtonBarDividerVisibility();
            adaptButtonBarDividerColor();
            adaptButtonVisibility();
        }
    }

    @Override
    protected final void onDetach() {
        tabLayout = null;
        viewPager = null;
        viewPagerAdapter = null;
        buttonBarContainer = null;
        backButton = null;
        nextButton = null;
        finishButton = null;
        buttonBarDivider = null;
    }

}