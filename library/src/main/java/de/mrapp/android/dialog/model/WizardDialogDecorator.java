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
package de.mrapp.android.dialog.model;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Button;

import de.mrapp.android.dialog.WizardDialog.TabPosition;
import de.mrapp.android.dialog.WizardDialog.WizardListener;

/**
 * Defines the interface a decorator, which allows to modify the view hierary of a dialog, which is
 * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
 * provides a navigation for switching between multiple fragments, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface WizardDialogDecorator {

    /**
     * Returns the view pager, which is contained by the dialog.
     *
     * @return The view pager, which is contained by the dialog, as an instance of the class
     * ViewPager or null, if the dialog does not show any fragments or has not been shown yet
     */
    ViewPager getViewPager();

    /**
     * Returns the tab layout, which is contained by the dialog.
     *
     * @return The tab layout, which is contained by the dialog, as an instance of the class
     * TabLayout or null, if the dialog does not show any fragments or has not been shown yet
     */
    TabLayout getTabLayout();

    /**
     * Returns the button, which allows to show the previous fragment of the dialog.
     *
     * @return The button, which allows to show the previous fragment of the dialog, as an instance
     * of the class {@link Button} or null, if the dialog does not show any fragments or has not
     * been shown yet
     */
    Button getBackButton();

    /**
     * Returns the button, which allows to show the next fragment of the dialog.
     *
     * @return The button, which allows to show the next fragment of the dialog, as an instance of
     * the class {@link Button} or null, if the dialog does not show any fragments or has not been
     * shown yet
     */
    Button getNextButton();

    /**
     * Returns the button, which allows to close dialog when the last fragment is shown.
     *
     * @return The button, which allows to close dialog when the last fragment is shown, as an
     * instance of the class {@link Button} or null, if the dialog does not show any fragments or
     * has not been shown yet
     */
    Button getFinishButton();

    /**
     * Adds a new fragment to the dialog.
     *
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     */
    void addFragment(@NonNull Class<? extends Fragment> fragmentClass);

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
    void addFragment(@NonNull Class<? extends Fragment> fragmentClass, @Nullable Bundle arguments);

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
    void addFragment(@StringRes int resourceId, @NonNull Class<? extends Fragment> fragmentClass);

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
    void addFragment(@StringRes int resourceId, @NonNull Class<? extends Fragment> fragmentClass,
                     @Nullable Bundle arguments);

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
    void addFragment(@Nullable CharSequence title,
                     @NonNull Class<? extends Fragment> fragmentClass);

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
    void addFragment(@Nullable CharSequence title, @NonNull Class<? extends Fragment> fragmentClass,
                     @Nullable Bundle arguments);

    /**
     * Removes the fragment at a specific index from the dialog.
     *
     * @param index
     *         The index of the fragment, which should be removed, as an {@link Integer value}
     */
    void removeFragment(int index);

    /**
     * Removes all fragments from the dialog.
     */
    void clearFragments();

    /**
     * Returns the index of a specific fragment.
     *
     * @param fragmentClass
     *         The class of the fragment, whose index should be returned, as an instance of the
     *         class {@link Class}. The class may not be null
     * @return The index of the given fragment as an {@link Integer} value or -1, if the adapter
     * does not contain the given fragment
     */
    int indexOfFragment(@NonNull Class<? extends Fragment> fragmentClass);

    /**
     * Returns the number of fragments, which are contained by the dialog.
     *
     * @return The number of fragments, which are contained by the dialog, as an {@link Integer}
     * value
     */
    int getFragmentCount();

    /**
     * Returns the position of the tabs, which indicate the currently shown fragment.
     *
     * @return The position of the tabs, which indicate the currently shown fragment, as a value of
     * the enum {@link TabPosition}
     */
    TabPosition getTabPosition();

    /**
     * Sets the position of the tabs, which indicate the currently shown fragment.
     *
     * @param tabPosition
     *         The position, which should be set, as a value of the enum {@link TabPosition}. The
     *         position may either be <code>USE_HEADER</code>, <code>PREFER_HEADER</code> or
     *         <code>NO_HEADER</code>
     */
    void setTabPosition(@NonNull TabPosition tabPosition);

    /**
     * Returns, whether the tabs, which indicate the currently shown fragment, are enabled, or not.
     *
     * @return True, if the tabs, which indicate the currently shown fragment, are enabled, false
     * otherwise
     */
    boolean isTabLayoutEnabled();

    /**
     * Enables or disables the tabs, which indicate the currently shown fragment. When the tabs are
     * enabled, it is possible to switch between the fragments by clicking the tabs.
     *
     * @param enable
     *         True, if the tabs should be enabled, false otherwise
     */
    void enableTabLayout(boolean enable);

    /**
     * Returns, whether the tabs, which indicate the currently shown fragment, are shown, or not.
     *
     * @return True, if the tabs, which indicate the currently shown fragment, are shown, false
     * otherwise
     */
    boolean isTabLayoutShown();

    /**
     * Sets, whether the tabs, which indicate the currently shown fragment, should be shown shown,
     * or not.
     *
     * @param show
     *         True, if the tabs, which indicate the currently shown fragment, should be shown,
     *         false otherwise
     */
    void showTabLayout(boolean show);

    /**
     * Returns the height of the indicator, which indicates the currently shown fragment.
     *
     * @return The height of the indicator, which indicates the currently shown fragment, in pixels
     * as an {@link Integer} value
     */
    int getTabIndicatorHeight();

    /**
     * Sets the height of the indicator, which indicates the currently shown fragment.
     *
     * @param height
     *         The height, which should be set, in pixels as an {@link Integer} value. The height
     *         must be at least 1
     */
    void setTabIndicatorHeight(int height);

    /**
     * Returns the color of the indicator, which indicates the currently shown fragment.
     *
     * @return The color of the indicator, which indicates the currently shown fragment, as an
     * {@link Integer} value
     */
    int getTabIndicatorColor();

    /**
     * Sets the color of the indicator, which indicates the currently shown fragment.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setTabIndicatorColor(@ColorInt int color);

    /**
     * Returns the text color of the tabs, which indicate the currently shown fragment.
     *
     * @return The text color of the tabs, which indicate the currently shown fragment, as an {@link
     * Integer} value
     */
    int getTabTextColor();

    /**
     * Sets the text color of the tabs, which indicates the currently shown fragment.
     *
     * @param textColor
     *         The text color, which should be set, as an {@link Integer} value
     */
    void setTabTextColor(@ColorInt int textColor);

    /**
     * Returns the selected text color of the tabs, which indicate the currently shown fragment.
     *
     * @return The selected text color of the tabs, which indicate the currently shown fragment, as
     * an {@link Integer} value
     */
    int getTabSelectedTextColor();

    /**
     * Sets the selected text color of the tabs, which indicate the currently shown fragment.
     *
     * @param selectedTextColor
     *         The selected text color, which should be set, as an {@link Integer} value
     */
    void setTabSelectedTextColor(@ColorInt int selectedTextColor);

    /**
     * Returns, whether switching fragments using swipe gestures is enabled, or not.
     *
     * @return True, if switching fragments using swipe gestures is enabled, false otherwise
     */
    boolean isSwipeEnabled();

    /**
     * Sets, whether switching fragments using swipe gestures should be enabled, or not.
     *
     * @param enable
     *         True, if switching fragments using swipe gestures should be enabled, false otherwise
     */
    void enableSwipe(boolean enable);

    /**
     * Returns, whether the dialog's buttons are shown, or not.
     *
     * @return True, if the dialog's buttons are shown, false otherwise
     */
    boolean isButtonBarShown();

    /**
     * Sets, whether the dialog's buttons should be shown, or not.
     *
     * @param show
     *         True, if the dialog's buttons should be shown, false otherwise
     */
    void showButtonBar(boolean show);

    /**
     * Returns the color of the button texts of the dialog.
     *
     * @return The color of the button texts of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    int getButtonTextColor();

    /**
     * Sets the color of the button texts of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setButtonTextColor(@ColorInt int color);

    /**
     * Returns, whether the divider, which is located above the dialog's buttons, is shown, or not.
     *
     * @return True, if the divider, which is located above the dialog's buttons, is shown, false
     * otherwise
     */
    boolean isButtonBarDividerShown();

    /**
     * Sets, whether the divider, which is located above the dialog's buttons, should be shown, or
     * not.
     *
     * @param show
     *         True, if the divider, which is located above the dialog's buttons, should be show,
     *         false otherwise
     */
    void showButtonBarDivider(boolean show);

    /**
     * Returns the color of the divider, which is located above the dialog's buttons.
     *
     * @return The color of the divider, which is located above the dialog's buttons, as an {@link
     * Integer} value
     */
    int getButtonBarDividerColor();

    /**
     * Sets the color of the divider, which is located above the dialog's buttons.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setButtonBarDividerColor(int color);

    /**
     * Returns the left and right margin of the divider, which is located above the dialog's
     * buttons.
     *
     * @return The left and right margin of the divider, which is located above the dialog's
     * buttons, in pixels as an {@link Integer} value
     */
    int getButtonBarDividerMargin();

    /**
     * Sets the left and right margin of the divider, which is located above the dialog's buttons.
     *
     * @param margin
     *         The left and right margin, which should be set, in pixels as an {@link Integer}
     *         value. The margin must be at least 0
     */
    void setButtonBarDividerMargin(int margin);

    /**
     * Returns the text of the dialog's back button.
     *
     * @return The text of the dialog's back button as an instance of the type {@link CharSequence}
     */
    CharSequence getBackButtonText();

    /**
     * Sets the text of the dialog's back button.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    void setBackButtonText(@StringRes int resourceId);

    /**
     * Sets the text of the dialog's back button.
     *
     * @param text
     *         The text, which should be set, as an instance of the class {@link CharSequence}. The
     *         text may neither be null, nor empty
     */
    void setBackButtonText(@NonNull CharSequence text);

    /**
     * Returns the text of the dialog's next button.
     *
     * @return The text of the dialog's next button as an instance of the type {@link CharSequence}
     */
    CharSequence getNextButtonText();

    /**
     * Sets the text of the dialog's next button.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    void setNextButtonText(@StringRes int resourceId);

    /**
     * Sets the text of the dialog's next button.
     *
     * @param text
     *         The text, which should be set, as an instance of the class {@link CharSequence}. The
     *         text may neither be null, nor empty
     */
    void setNextButtonText(@NonNull CharSequence text);

    /**
     * Returns the text of the dialog's finish button.
     *
     * @return The text of the dialog's finish button as an instance of the type {@link
     * CharSequence}
     */
    CharSequence getFinishButtonText();

    /**
     * Sets the text of the dialog's finish button.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    void setFinishButtonText(@StringRes int resourceId);

    /**
     * Sets the text of the dialog's finish button.
     *
     * @param text
     *         The text, which should be set, as an instance of the class {@link CharSequence}. The
     *         text may neither be null, nor empty
     */
    void setFinishButtonText(@NonNull CharSequence text);

    /**
     * Adds a new listener, which should be notified, when the user navigates within the dialog.
     *
     * Note, that the attached listeners are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listeners.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link
     *         WizardListener}. The listener may not be null
     */
    void addWizardListener(@NonNull WizardListener listener);

    /**
     * Removes a specific listener, which should not be notified, when the user navigates within the
     * dialog.
     *
     * @param listener
     *         The listener, which should be removed, as an instance of the type {@link
     *         WizardListener}. The listener may not be null
     */
    void removeWizardListener(@NonNull WizardListener listener);

    /**
     * Adds a listener, which should be notified, when the page of the dialog's view pager has been
     * changed.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type OnPageChangeListener.
     *         The listener may not be null
     */
    void addOnPageChangeListener(@NonNull OnPageChangeListener listener);

    /**
     * Removes a listener, which should not be notified, when the page of the dialog's view pager
     * has been changed, anymore.
     *
     * @param listener
     *         The listener, which should be removed, as an instance of the type
     *         OnPageChangeListener. The listener may not be null
     */
    void removeOnPageChangeListener(@NonNull OnPageChangeListener listener);

}