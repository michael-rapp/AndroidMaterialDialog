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

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation;

/**
 * Defines the interface of a decorator, which allows to modify the view hierarchy of a dialog,
 * which is designed according to Android 5's Material Design guidelines even on pre-Lollipop
 * devices and may contain a header, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface HeaderDialogDecorator extends Dialog {

    /**
     * Returns, whether the dialog's header is shown, or not.
     *
     * @return True, if the dialog's header is shown, false otherwise
     */
    boolean isHeaderShown();

    /**
     * Sets, whether the dialog's header should be shown, or not.
     *
     * @param show
     *         True, if the dialog's header should be shown, false otherwise
     */
    void showHeader(boolean show);

    /**
     * Sets the custom view, which should be used to show the header of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the header
     */
    void setCustomHeader(@Nullable View view);

    /**
     * Returns, whether a custom view is set for showing the header of the dialog, or not.
     *
     * @return True, if a custom view is set for showing the header of the dialog, false otherwise
     */
    boolean isCustomHeaderUsed();

    /**
     * Sets the custom view, which should be used to show the header of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    void setCustomHeader(@LayoutRes int resourceId);

    /**
     * Returns the height of the dialog's header.
     *
     * @return The height of the dialog's header in pixels as an {@link Integer} value
     */
    int getHeaderHeight();

    /**
     * Sets the height of the dialog's header.
     *
     * @param height
     *         The height, which should be set, in pixels as an {@link Integer} value. The height
     *         must be at least 0
     */
    void setHeaderHeight(int height);

    /**
     * Returns the background of the dialog's header.
     *
     * @return The background of the dialog's header as an instance of the class {@link Drawable}
     */
    Drawable getHeaderBackground();

    /**
     * Sets the background of the dialog's header.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no background should be shown
     */
    void setHeaderBackground(@Nullable Bitmap background);

    /**
     * Sets the background of the dialog's header.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no background should be shown
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setHeaderBackground(@Nullable Bitmap background, @Nullable BackgroundAnimation animation);

    /**
     * Sets the background of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    void setHeaderBackground(@DrawableRes int resourceId);

    /**
     * Sets the background of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setHeaderBackground(@DrawableRes int resourceId, @Nullable BackgroundAnimation animation);

    /**
     * Sets the background color of the dialog's header.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     */
    void setHeaderBackgroundColor(@ColorInt int color);

    /**
     * Sets the background color of the dialog's header.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    void setHeaderBackgroundColor(@ColorInt int color, @Nullable BackgroundAnimation animation);

    /**
     * Returns the icon of the dialog's header.
     *
     * @return The icon of the dialog's header as an instance of the class {@link Drawable} or null,
     * if no icon has been set
     */
    Drawable getHeaderIcon();

    /**
     * Sets the icon of the dialog's header.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be set
     */
    void setHeaderIcon(@Nullable Bitmap icon);

    /**
     * Sets the icon of the dialog's header.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be set
     * @param animation
     *         The animation, which should be used to change the icon, as an instance of the class
     *         {@link DrawableAnimation} or null, if no animation should be used
     */
    void setHeaderIcon(@Nullable Bitmap icon, @Nullable DrawableAnimation animation);

    /**
     * Sets the icon of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    void setHeaderIcon(@DrawableRes int resourceId);

    /**
     * Sets the icon of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     * @param animation
     *         The animation, which should be used to change the icon, as an instance of the class
     *         {@link DrawableAnimation} or null, if no animation should be used
     */
    void setHeaderIcon(@DrawableRes int resourceId, @Nullable DrawableAnimation animation);

    /**
     * Returns the color state list, which is used to tint the header icon of the dialog.
     *
     * @return The color state list, which is used to tint the header icon of the dialog, as an
     * instance of the class {@link ColorStateList} or null, if no color state list has been set
     */
    ColorStateList getHeaderIconTintList();

    /**
     * Sets the color, which should be used to tint the header icon of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setHeaderIconTint(@ColorInt int color);

    /**
     * Sets the color state list, which should be used to tint the header icon of the dialog.
     *
     * @param tintList
     *         The color state list, which should be set, as an instance of the class {@link
     *         ColorStateList} or null, if no color state list should be set
     */
    void setHeaderIconTintList(@Nullable ColorStateList tintList);

    /**
     * Returns the mode, which is used to tint the icon of the dialog.
     *
     * @return The mode, which is used to tint the icon of the dialog, as a value of the enum {@link
     * PorterDuff.Mode}. The mode may not be null
     */
    @NonNull
    PorterDuff.Mode getHeaderIconTintMode();

    /**
     * Sets the mode, which should be used to tint the icon of the dialog.
     *
     * @param mode
     *         The mode, which should be set, as a value of the enum {@link PorterDuff.Mode}. The
     *         mode may not be null
     */
    void setHeaderIconTintMode(@NonNull PorterDuff.Mode mode);

    /**
     * Returns the color of the divider of the dialog's header.
     *
     * @return The color of the divider of the dialog's header as an {@link Integer} value
     */
    int getHeaderDividerColor();

    /**
     * Sets the color of the divider of the dialog's header.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setHeaderDividerColor(@ColorInt int color);

    /**
     * Returns, whether the divider of the dialog's header is shown, or not.
     *
     * @return True, if the divider of the dialog's header is shown, false otherwise
     */
    boolean isHeaderDividerShown();

    /**
     * Sets, whether the divider of the dialog's header should be shown, or not.
     *
     * @param show
     *         True, if the divider of the dialog's header should be shown, false otherwise
     */
    void showHeaderDivider(boolean show);

}