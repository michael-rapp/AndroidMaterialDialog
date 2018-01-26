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

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.KeyEvent;
import android.view.View;

import de.mrapp.android.dialog.ScrollableArea;
import de.mrapp.android.dialog.animation.BackgroundAnimation;

/**
 * Defines the interface a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material Design guidelines even on pre-Lollipop devices,
 * must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface MaterialDialogDecorator extends Dialog {

    /**
     * Returns the background of the dialog's window.
     *
     * @return The background of the dialog's window as an instance of the class {@link Drawable} or
     * null, if no window background is set
     */
    Drawable getWindowBackground();

    /**
     * Sets the background of the dialog's window. Preferably, it should be a 9-patch drawable using
     * the color white for visible areas and transparency for invisible ones. This allows to
     * customize the shape of the dialog as well as its shadow. The window background is only used
     * if the dialog is not shown fullscreen. The drawable's padding is taken into account for
     * insetting the dialog's content (see {@link #getWindowInsetLeft()}, {@link
     * #getWindowInsetTop()}, {@link #getWindowInsetRight()} and {@link #getWindowInsetBottom()}).
     *
     * @param resourceId
     *         The resource id of the window background, which should be set, as an {@link Integer}
     *         value. The resource id must correspond to a valid drawable resource
     */
    void setWindowBackground(@DrawableRes final int resourceId);

    /**
     * Sets the background of the dialog's window. The given bitmap is stretched to fit the dialog's
     * width and height. Therefore, if the width and height is not explicitly given, the method
     * {@link #setWindowBackground(int)} should be preferred. If the given bitmap is null, a
     * rectangle, which matches the width and height of the dialog, is used as the window
     * background.
     *
     * @param windowBackground
     *         The window background, which should be set, as an instance of the class {@link
     *         Bitmap} or null, if no window background should be set
     */
    void setWindowBackground(@Nullable final Bitmap windowBackground);

    /**
     * Returns the left inset of the dialog's content, depending on the padding of the dialog's
     * window background. If the dialog is shown fullscreen, the inset is always 0.
     *
     * @return The left inset of the dialog's content in pixels as an {@link Integer} value
     */
    int getWindowInsetLeft();

    /**
     * Returns the top inset of the dialog's content, depending on the padding of the dialog's
     * window background. If the dialog is shown fullscreen, the inset is always 0.
     *
     * @return The top inset of the dialog's content in pixels as an {@link Integer} value
     */
    int getWindowInsetTop();

    /**
     * Returns the right inset of the dialog's content, depending on the padding of the dialog's
     * window background. If the dialog is shown fullscreen, the inset is always 0.
     *
     * @return The right inset of the dialog's content in pixels as an {@link Integer} value
     */
    int getWindowInsetRight();

    /**
     * Returns the bottom inset of the dialog's content, depending on the padding of the dialog's
     * window background. If the dialog is shown fullscreen, the inset is always 0.
     *
     * @return The bottom inset of the dialog's content in pixels as an {@link Integer} value
     */
    int getWindowInsetBottom();

    /**
     * Returns, whether the dialog is canceled, when touched outside the window's bounds, or not.
     *
     * @return True, if the dialog is canceled, when touched outside the window, false otherwise
     */
    boolean isCanceledOnTouchOutside();

    /**
     * Sets, whether the dialog is canceled, when touched outside the window's bounds. If set to
     * true, the dialog is set to be cancelable, if not already set.
     *
     * @param canceledOnTouchOutside
     *         True, if the dialog should be canceled, when touched outside the window, false
     *         otherwise
     */
    void setCanceledOnTouchOutside(boolean canceledOnTouchOutside);

    /**
     * Returns whether the dialog is cancelable with the {@link KeyEvent#KEYCODE_BACK BACK} key, or
     * not.
     *
     * @return True, if the dialog is cancelable, false otherwise
     */
    boolean isCancelable();

    /**
     * Sets whether the dialog is cancelable with the {@link KeyEvent#KEYCODE_BACK BACK} key, or
     * not.
     *
     * @param cancelable
     *         True, if the dialog should be cancelable, false otherwise
     */
    void setCancelable(boolean cancelable);

    /**
     * Returns, whether the dialog is shown fullscreen, or not.
     *
     * @return True, if the dialog is shown fullscreen, false otherwise
     */
    boolean isFullscreen();

    /**
     * Sets, whether the dialog should be shown fullscreen, or not.
     *
     * @param fullscreen
     *         True, if the dialog should be shown fullscreen, false otherwise
     */
    void setFullscreen(boolean fullscreen);

    /**
     * Returns the gravity of the dialog.
     *
     * @return The gravity of the dialog as an {@link Integer} values. The gravity consists of the
     * flags given in {@link Dialog.Gravity}
     */
    int getGravity();

    /**
     * Sets the gravity of the dialog.
     *
     * @param gravity
     *         The gravity, which should be set, as an {@link Integer} value. The gravity must
     *         consist of the flags given in {@link Dialog.Gravity}
     */
    void setGravity(int gravity);

    /**
     * Returns the width of the dialog.
     *
     * @return The width of the dialog in pixels as an {@link Integer} value or {@link
     * Dialog#MATCH_PARENT}, respectively {@link Dialog#WRAP_CONTENT}
     */
    int getWidth();

    /**
     * Sets the width of the dialog.
     *
     * @param width
     *         The width, which should be set, in pixels as an {@link Integer} value. The width must
     *         be at least 1 or {@link Dialog#MATCH_PARENT}, respectively {@link
     *         Dialog#WRAP_CONTENT}
     */
    void setWidth(int width);

    /**
     * Returns the height of the dialog.
     *
     * @return The height of the dialog in pixels as an {@link Integer} value or {@link
     * Dialog#MATCH_PARENT}, respectively {@link Dialog#WRAP_CONTENT}
     */
    int getHeight();

    /**
     * Sets the height of the dialog.
     *
     * @param height
     *         The height, which should be set, in pixels as an {@link Integer} value. The width
     *         must be at least 1 or {@link Dialog#MATCH_PARENT}, respectively {@link
     *         Dialog#WRAP_CONTENT}
     */
    void setHeight(int height);

    /**
     * Returns the maximum width of the dialog.
     *
     * @return The maximum width of the dialog in pixels as an {@link Integer} value or -1, if no
     * maximum width is set
     */
    int getMaxWidth();

    /**
     * Sets the maximum width of the dialog.
     *
     * @param maxWidth
     *         The maximum width, which should be set, in pixels as an {@link Integer} value. The
     *         maximum width must be at least 1, or -1, if no maximum width should be set
     */
    void setMaxWidth(int maxWidth);

    /**
     * Returns the maximum height of the dialog.
     *
     * @return The maximum height of the dialog in pixels as an {@link Integer} value or -1, if no
     * maximum height is set
     */
    int getMaxHeight();

    /**
     * Sets the maximum height of the dialog.
     *
     * @param maxHeight
     *         The maximum height, which should be set, in pixels as an {@link Integer} value. The
     *         maximum height must be at least 1, or -1, if no maximum height should be set
     */
    void setMaxHeight(int maxHeight);

    /**
     * Returns the left margin of the dialog.
     *
     * @return The left margin of the dialog in pixels as an {@link Integer} value
     */
    int getLeftMargin();

    /**
     * Returns the top margin of the dialog.
     *
     * @return The top margin of the dialog in pixels as an {@link Integer} value
     */
    int getTopMargin();

    /**
     * Returns the right margin of the dialog.
     *
     * @return The right margin of the dialog in pixels as an {@link Integer} value
     */
    int getRightMargin();

    /**
     * Returns the bottom margin of the dialog.
     *
     * @return The bottom margin of the dialog in pixels as an {@link Integer} value
     */
    int getBottomMargin();

    /**
     * Sets the margin of the dialog.
     *
     * @param left
     *         The left margin, which should be set, in pixels as an {@link Integer} value. The left
     *         margin must be at least 0
     * @param top
     *         The top margin, which should be set, in pixels as an {@link Integer} value. The top
     *         margin must be at least 0
     * @param right
     *         The right margin, which should be set, in pixels as an {@link Integer} value. The
     *         right margin must be at least 0
     * @param bottom
     *         The bottom margin, which should be set, in pixels as an {@link Integer} value. The
     *         bottom margin must be at least 0
     */
    void setMargin(int left, int top, int right, int bottom);

    /**
     * Returns the left padding of the dialog.
     *
     * @return The left padding of the dialog in pixels as an {@link Integer} value
     */
    int getPaddingLeft();

    /**
     * Returns the top padding of the dialog.
     *
     * @return The top padding of the dialog in pixels as an {@link Integer} value
     */
    int getPaddingTop();

    /**
     * Returns the right padding of the dialog.
     *
     * @return The right padding of the dialog in pixels as an {@link Integer} value
     */
    int getPaddingRight();

    /**
     * Returns the bottom padding of the dialog.
     *
     * @return The bottom padding of the dialog in pixels as an {@link Integer} value
     */
    int getPaddingBottom();

    /**
     * Sets the padding of the dialog. The padding does not apply to the dialog's header and
     * buttons.
     *
     * @param left
     *         The left padding, which should be set, in pixels as an {@link Integer} value. The
     *         left padding must be at least 0
     * @param top
     *         The top padding, which should be set, in pixels as an {@link Integer} value. The top
     *         padding must be at least 0
     * @param right
     *         The right padding, which should be set, in pixels as an {@link Integer} value. The
     *         right padding must be at least 0
     * @param bottom
     *         The bottom padding, which should be set, in pixels as an {@link Integer} value. The
     *         bottom padding must be at least 0
     */
    void setPadding(int left, int top, int right, int bottom);

    /**
     * Returns, whether the dialog accounts for system screen decorations such as the status bar and
     * insets its content at the left edge.
     *
     * @return True, if the dialog insets its content at the left edge, false otherwise
     */
    boolean isFitsSystemWindowsLeft();

    /**
     * Returns, whether the dialog accounts for system screen decorations such as the status bar and
     * insets its content at the top edge.
     *
     * @return True, if the dialog insets its content at the top edge, false otherwise
     */
    boolean isFitsSystemWindowsTop();

    /**
     * Returns, whether the dialog accounts for system screen decorations such as the status bar and
     * insets its content at the right edge.
     *
     * @return True, if the dialog insets its content at the right edge, false otherwise
     */
    boolean isFitsSystemWindowsRight();

    /**
     * Returns, whether the dialog accounts for system screen decorations such as the status bar and
     * insets its content at the bottom edge.
     *
     * @return True, if the dialog insets its content at the bottom edge, false otherwise
     */
    boolean isFitsSystemWindowsBottom();

    /**
     * Sets, whether the dialog should account for system screen decorations such as the status bar
     * and inset its content, or not.
     *
     * @param fitsSystemWindows
     *         True, if the dialog should inset its content, false otherwise
     */
    void setFitsSystemWindows(boolean fitsSystemWindows);

    /**
     * Sets, whether the dialog should account for system screen decorations such as the status bar
     * and inset its content, or not.
     *
     * @param left
     *         True, if the dialog should inset its content at the left edge, false otherwise
     * @param top
     *         True, if the dialog should inset its content at the top edge, false otherwise
     * @param right
     *         True, if the dialog should inset its content at the right edge, false otherwise
     * @param bottom
     *         True, if the dialog should inset its content at the bottom edge, false otherwise
     */
    void setFitsSystemWindows(boolean left, boolean top, boolean right, boolean bottom);

    /**
     * Returns the scrollable area of the dialog.
     *
     * @return The scrollable area of the dialog as an instance of the class {@link ScrollableArea}.
     * The scrollable area may not be null
     */
    @NonNull
    ScrollableArea getScrollableArea();

    /**
     * Sets the area of the dialog, which should be scrollable.
     *
     * @param area
     *         The area, which should be set, as a value of the enum {@link ScrollableArea.Area} or
     *         null, if no area should be scrollable
     */
    void setScrollableArea(@Nullable ScrollableArea.Area area);

    /**
     * Sets the areas of the dialog, which should be scrollable.
     *
     * @param top
     *         The top-most area, which should be scrollable, as a value of the enum {@link
     *         ScrollableArea.Area} or null, if no area should be scrollable
     * @param bottom
     *         The bottom-most area, which should be scrollable, as a value of the enum {@link
     *         ScrollableArea.Area}. If the top-most area is null, the bottom-most are must be null
     *         as well. The index of the bottom-most area must be at least the index of the top-most
     *         area
     */
    void setScrollableArea(@Nullable ScrollableArea.Area top, @Nullable ScrollableArea.Area bottom);

    /**
     * Returns, whether dividers, which are located above and below the dialog's scrollable areas,
     * are shown, when being scrolled, or not.
     *
     * @return True, if the dividers, which are located above and below the dialog's scrollable
     * areas, are shown, when scrolling, false otherwise
     */
    boolean areDividersShownOnScroll();

    /**
     * Sets, whether dividers, which are located above and below the dialog's scrollable areas,
     * should be shown, when scrolled, or not.
     *
     * @param show
     *         True, if the dividers, which are located above and below the dialog's scrollable
     *         areas, should be shown, when scrolling, false otherwise
     */
    void showDividersOnScroll(boolean show);

    /**
     * Returns the color of dividers.
     *
     * @return The color of dividers as an {@link Integer} value
     */
    int getDividerColor();

    /**
     * Sets the color of dividers.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setDividerColor(@ColorInt int color);

    /**
     * Returns the left and right margin of dividers.
     *
     * @return The left and right margin of dividers in pixels as an {@link Integer} value
     */
    int getDividerMargin();

    /**
     * Sets the left and right margin of the divider, which is located above the dialog's button.
     *
     * @param margin
     *         The left and right margin, which should be set, in pixels as an {@link Integer}
     *         value. The margin must be at least 0
     */
    void setDividerMargin(int margin);

    /**
     * Returns the icon of the dialog.
     *
     * @return The icon of the dialog, as an instance of the class {@link Drawable} or null, if no
     * icon has been set
     */
    Drawable getIcon();

    /**
     * Sets the icon of the dialog.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be shown
     */
    void setIcon(@Nullable Bitmap icon);

    /**
     * Sets the icon of the dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    void setIcon(@DrawableRes int resourceId);

    /**
     * Set the icon of the dialog.
     *
     * @param attributeId
     *         The id of the theme attribute, which supplies the icon, which should be set, as an
     *         {@link Integer} value. The id must point to a valid drawable resource
     */
    void setIconAttribute(@AttrRes int attributeId);

    /**
     * Returns the color of the title of the dialog.
     *
     * @return The color of the title of the dialog as an {@link Integer} value
     */
    int getTitleColor();

    /**
     * Sets the color of the title of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setTitleColor(@ColorInt int color);

    /**
     * Returns the color of the message of the dialog.
     *
     * @return The color of the message of the dialog as an {@link Integer} value
     */
    int getMessageColor();

    /**
     * Sets the color of the message of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setMessageColor(@ColorInt int color);

    /**
     * Returns the background of the dialog.
     *
     * @return The background of the dialog as an instance of the class {@link Drawable} or null, if
     * no background has been set
     */
    Drawable getBackground();

    /**
     * Sets the background of the dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no background should be set
     */
    void setBackground(@Nullable Bitmap background);

    /**
     * Sets the background of the dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no background should be set
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setBackground(@Nullable Bitmap background, @Nullable BackgroundAnimation animation);

    /**
     * Sets the background of the dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    void setBackground(@DrawableRes int resourceId);

    /**
     * Sets the background of the dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setBackground(@DrawableRes int resourceId, @Nullable BackgroundAnimation animation);

    /**
     * Sets the background color of the dialog.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     */
    void setBackgroundColor(@ColorInt int color);

    /**
     * Sets the background color of the dialog.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setBackgroundColor(@ColorInt int color, @Nullable final BackgroundAnimation animation);

    /**
     * Returns, whether a custom view is set for showing the title of the dialog, or not.
     *
     * @return True, if a custom view is set for showing the title of the dialog, false otherwise
     */
    boolean isCustomTitleUsed();

    /**
     * Sets the custom view, which should be used to show the title of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    void setCustomTitle(@Nullable View view);

    /**
     * Sets the custom view, which should be used to show the title of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    void setCustomTitle(@LayoutRes int resourceId);

    /**
     * Returns, whether a custom view is set for showing the dialog's message, or not.
     *
     * @return True, if a custom view is set for showing the dialog's message, false otherwise
     */
    boolean isCustomMessageUsed();

    /**
     * Sets the custom view, which should be used to show the message of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    void setCustomMessage(@Nullable View view);

    /**
     * Sets the custom view, which should be used to show the message of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    void setCustomMessage(@LayoutRes int resourceId);

    /**
     * Returns, whether a custom view is set for showing the content of the dialog, or not.
     *
     * @return True, if a custom view is set for showing the content of the dialog, false otherwise
     */
    boolean isCustomViewUsed();

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be shown
     */
    void setView(@Nullable View view);

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    void setView(@LayoutRes int resourceId);

    /**
     * Returns the message of the dialog.
     *
     * @return The message of the dialog as an instance of the type {@link CharSequence} or null, if
     * no message has been set
     */
    CharSequence getMessage();

    /**
     * Sets the message of the dialog.
     *
     * @param message
     *         The message, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no message should be shown
     */
    void setMessage(@Nullable CharSequence message);

    /**
     * Sets the message of the dialog.
     *
     * @param resourceId
     *         The resource id of the message, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    void setMessage(@StringRes int resourceId);

    /**
     * Returns the title of the dialog.
     *
     * @return The title of the dialog as an instance of the type {@link CharSequence} or null, if
     * no title has been set
     */
    CharSequence getTitle();

    /**
     * Sets the title of the dialog.
     *
     * @param title
     *         The title, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no title should be set
     */
    void setTitle(@Nullable CharSequence title);

    /**
     * Sets the title of the dialog.
     *
     * @param resourceId
     *         The resource id of the title, which should be set, as an {@link Integer}. The
     *         resource id must correspond to a valid string resource
     */
    void setTitle(@StringRes int resourceId);

}