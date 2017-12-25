/*
 * Copyright 2014 - 2017 Michael Rapp
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
package de.mrapp.android.dialog.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.dialog.model.MaterialDialog;
import de.mrapp.android.util.ThemeUtil;

/**
 * An abstract base class for all builders, which allow to create and show dialogs, which are
 * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 3.3.0
 */
public abstract class AbstractMaterialDialogBuilder<DialogType extends MaterialDialog, BuilderType extends AbstractMaterialDialogBuilder<DialogType, ?>>
        extends AbstractBuilder<DialogType, BuilderType> {

    /**
     * The resource if of the theme, which is used by the dialog.
     */
    private int themeResourceId;

    /**
     * Initializes the builder.
     *
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value or 0, if the default theme should be used
     */
    private void initialize(@StyleRes final int themeResourceId) {
        int themeId = themeResourceId;

        if (themeId == 0) {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.materialDialogTheme, typedValue, true);
            themeId = typedValue.resourceId;
            themeId = themeId != 0 ? themeId : R.style.MaterialDialog_Light;
        }

        setContext(new ContextThemeWrapper(getContext(), themeId));
        this.themeResourceId = themeId;
        obtainStyledAttributes(themeId);
    }

    /**
     * Obtains the boolean value, which specified whether the dialog should be shown fullscreen, or
     * not, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the boolean value should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainFullscreen(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogFullscreen});
        setFullscreen(typedArray.getBoolean(0, false));
    }

    /**
     * Obtains the gravity from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the gravity should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainGravity(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogGravity});
        setGravity(typedArray.getInteger(0, Dialog.Gravity.CENTER));
    }

    /**
     * Obtains the width from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the width should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainWidth(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogWidth});
        int defaultValue = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width);

        try {
            setWidth(typedArray.getDimensionPixelSize(0, defaultValue));
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            setWidth(typedArray.getInteger(0, defaultValue));
        }
    }

    /**
     * Obtains the height from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the height should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainHeight(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogHeight});
        int defaultValue = Dialog.WRAP_CONTENT;

        try {
            setHeight(typedArray.getDimensionPixelSize(0, defaultValue));
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            setHeight(typedArray.getInteger(0, defaultValue));
        }
    }

    /**
     * Obtains the maximum width from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the maximum width should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainMaxWidth(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogMaxWidth});
        int defaultValue;

        try {
            defaultValue =
                    getContext().getResources().getDimensionPixelSize(R.dimen.dialog_max_width);
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            defaultValue = -1;
        }

        try {
            setMaxWidth(typedArray.getDimensionPixelSize(0, defaultValue));
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            setMaxWidth(-1);
        }
    }

    /**
     * Obtains the maximum height from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the maximum height should be obtained from, as an
     *         {@link Integer} value
     */
    private void obtainMaxHeight(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogMaxHeight});
        int defaultValue;

        try {
            defaultValue =
                    getContext().getResources().getDimensionPixelSize(R.dimen.dialog_max_height);
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            defaultValue = -1;
        }

        try {
            setMaxHeight(typedArray.getDimensionPixelSize(0, defaultValue));
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            setMaxHeight(-1);
        }
    }

    /**
     * Obtains the margin from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the margin should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainMargin(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogMarginLeft, R.attr.materialDialogMarginTop,
                        R.attr.materialDialogMarginRight, R.attr.materialDialogMarginBottom});
        int defaultHorizontalMargin =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_horizontal_margin);
        int defaultVerticalMargin =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_vertical_margin);
        int left = typedArray.getDimensionPixelSize(0, defaultHorizontalMargin);
        int top = typedArray.getDimensionPixelSize(1, defaultVerticalMargin);
        int right = typedArray.getDimensionPixelSize(2, defaultHorizontalMargin);
        int bottom = typedArray.getDimensionPixelSize(3, defaultVerticalMargin);
        setMargin(left, top, right, bottom);
    }

    /**
     * Obtains the üadding from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the padding should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainPadding(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogPaddingLeft, R.attr.materialDialogPaddingTop,
                        R.attr.materialDialogPaddingRight, R.attr.materialDialogPaddingBottom});
        int defaultLeftPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_left_padding);
        int defaultTopPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_top_padding);
        int defaultRightPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_right_padding);
        int defaultBottomPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_bottom_padding);
        int left = typedArray.getDimensionPixelSize(0, defaultLeftPadding);
        int top = typedArray.getDimensionPixelSize(1, defaultTopPadding);
        int right = typedArray.getDimensionPixelSize(2, defaultRightPadding);
        int bottom = typedArray.getDimensionPixelSize(3, defaultBottomPadding);
        setPadding(left, top, right, bottom);
    }

    /**
     * Obtains, whether the dialog's content should be inset, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the boolean value, which specifies whether the dialog's
     *         content should be inset, should be obtained from, as a {@link Integer} value
     */
    private void obtainFitsSystemWindows(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogFitsSystemWindowsLeft,
                        R.attr.materialDialogFitsSystemWindowsTop,
                        R.attr.materialDialogFitsSystemWindowsRight,
                        R.attr.materialDialogFitsSystemWindowsBottom});
        boolean left = typedArray.getBoolean(0, true);
        boolean top = typedArray.getBoolean(1, true);
        boolean right = typedArray.getBoolean(2, true);
        boolean bottom = typedArray.getBoolean(3, true);
        setFitsSystemWindows(left, top, right, bottom);
    }

    /**
     * Obtains the background from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the background should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainBackground(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogBackground});
        int resourceId = typedArray.getResourceId(0, 0);

        if (resourceId != 0) {
            setBackground(resourceId);
        } else {
            setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.dialog_background_light));
        }
    }

    /**
     * Obtains the message color from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the message color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainMessageColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogMessageColor});
        int defaultColor = ThemeUtil
                .getColor(getContext(), themeResourceId, android.R.attr.textColorSecondary);
        setMessageColor(typedArray.getColor(0, defaultColor));
    }

    /**
     * Obtains the title color from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the title color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainTitleColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogTitleColor});
        int defaultColor =
                ThemeUtil.getColor(getContext(), themeResourceId, android.R.attr.textColorPrimary);
        setTitleColor(typedArray.getColor(0, defaultColor));
    }

    /**
     * Obtains the scrollable area from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the scrollable area should be obtained from, as an
     *         {@link Integer} value
     */
    private void obtainScrollableArea(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogScrollableAreaTop,
                        R.attr.materialDialogScrollableAreaBottom});
        int topIndex = typedArray.getInt(0, -1);
        int bottomIndex = typedArray.getInt(1, -1);

        if (topIndex != -1) {
            Area top = Area.fromIndex(topIndex);

            if (bottomIndex != -1) {
                Area bottom = Area.fromIndex(bottomIndex);
                setScrollableArea(top, bottom);
            } else {
                setScrollableArea(top);
            }
        } else {
            setScrollableArea(null, null);
        }
    }

    /**
     * Obtains, whether the dividers, which are located above and below the dialog's scrollable
     * areas, should be shown when scrolling, or not, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the boolean value should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainShowDividersOnScroll(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogShowDividersOnScroll});
        showDividersOnScroll(typedArray.getBoolean(0, true));
    }

    /**
     * Obtains the color of dividers from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainDividerColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogDividerColor});
        int defaultColor = ContextCompat.getColor(getContext(), R.color.divider_color_light);
        setDividerColor(typedArray.getColor(0, defaultColor));
    }

    /**
     * Obtains the left and right margin of dividers from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the margin should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainDividerMargin(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogDividerMargin});
        setButtonBarDividerMargin(typedArray.getDimensionPixelSize(0, 0));
    }

    /**
     * Obtains all relevant attributes from the current theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid style resource
     */
    @CallSuper
    protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
        obtainFullscreen(themeResourceId);
        obtainGravity(themeResourceId);
        obtainWidth(themeResourceId);
        obtainHeight(themeResourceId);
        obtainMaxWidth(themeResourceId);
        obtainMaxHeight(themeResourceId);
        obtainMargin(themeResourceId);
        obtainPadding(themeResourceId);
        obtainFitsSystemWindows(themeResourceId);
        obtainBackground(themeResourceId);
        obtainMessageColor(themeResourceId);
        obtainTitleColor(themeResourceId);
        obtainScrollableArea(themeResourceId);
        obtainShowDividersOnScroll(themeResourceId);
        obtainDividerColor(themeResourceId);
        obtainDividerMargin(themeResourceId);
    }

    /**
     * Returns the resource id of the theme, which is used by the dialog.
     *
     * @return The resource id of the theme, which is used by the dialog, as an {@link Integer}
     * value
     */
    @StyleRes
    protected final int getThemeResourceId() {
        return themeResourceId;
    }

    /**
     * Creates a new builder, which allows to create and show dialogs, which are designed according
     * to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractMaterialDialogBuilder(@NonNull final Context context) {
        super(context);
        initialize(0);
    }

    /**
     * Creates a new builder, which allows to create and show dialogs, which are designed according
     * to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractMaterialDialogBuilder(@NonNull final Context context,
                                         @StyleRes final int themeResourceId) {
        super(context);
        initialize(themeResourceId);
    }

    /**
     * Sets the background of the window of the dialog, which is created by the builder. Preferably,
     * it should be a 9-patch drawable using the color white for visible areas and transparency for
     * invisible ones. This allows to customize the shape of the dialog as well as its shadow. The
     * drawable's padding is taken into account for insetting the dialog's content (see {@link
     * MaterialDialog#getWindowInsetLeft()}, {@link MaterialDialog#getWindowInsetTop()}, {@link
     * MaterialDialog#getWindowInsetRight()} and {@link MaterialDialog#getWindowInsetBottom()}).
     *
     * @param resourceId
     *         The resource id of the window background, which should be set, as an {@link Integer}
     *         value. The resource id must correspond to a valid drawable resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setWindowBackground(@DrawableRes final int resourceId) {
        getProduct().setWindowBackground(resourceId);
        return self();
    }

    /**
     * Sets, whether the dialog, which is created by the builder, should be cancelable, or not.
     *
     * @param cancelable
     *         True, if the dialog, which is created by the builder, should be cancelable, false
     *         otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCancelable(final boolean cancelable) {
        getProduct().setCancelable(cancelable);
        return self();
    }

    /**
     * Sets, whether the dialog, which is created by the builder, should be canceled, when touched
     * outside the window's bounds. If set to true, the dialog is set to be cancelable, if not
     * already set.
     *
     * @param canceledOnTouchOutside
     *         True, if the dialog should be canceled, when touched outside the window, false
     *         otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        getProduct().setCanceledOnTouchOutside(canceledOnTouchOutside);
        return self();
    }

    /**
     * Sets the listener, which should be notified, when the dialog has been shown.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnShowListener}, or null, if no listener should be set
     */
    public BuilderType setOnShowListener(@Nullable final DialogInterface.OnShowListener listener) {
        getProduct().setOnShowListener(listener);
        return self();
    }

    /**
     * Sets the listener, which should be notified, when the dialog, which is created by the
     * builder, is canceled.
     *
     * Even in a cancelable dialog, the dialog may be dismissed for reasons other than being
     * canceled or one of the supplied choices being selected. If you are interested in listening
     * for all cases where the dialog is dismissed and not just when it is canceled, see {@link
     * #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
     * setOnDismissListener}.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnCancelListener}, or null, if no listener should be set
     * @return The builder, the method has been called upon, as an instance of the class BuilderType
     * @see #setCancelable(boolean)
     * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
     */
    public BuilderType setOnCancelListener(
            @Nullable final DialogInterface.OnCancelListener listener) {
        getProduct().setOnCancelListener(listener);
        return self();
    }

    /**
     * Sets the listener, which should be notified, when the dialog, which is created by the
     * builder, is dismissed for any reason.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnDismissListener}, or null, if no listener should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setOnDismissListener(
            @Nullable final DialogInterface.OnDismissListener listener) {
        getProduct().setOnDismissListener(listener);
        return self();
    }

    /**
     * Sets, whether the dialog, which is created by the builder, should be shown fullscreen, or
     * not.
     *
     * @param fullscreen
     *         True, if the dialog should be shown fullscreen, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setFullscreen(final boolean fullscreen) {
        getProduct().setFullscreen(fullscreen);
        return self();
    }

    /**
     * Sets the gravity of the dialog, which is created by the builder.
     *
     * @param gravity
     *         The gravity, which should be set, as an {@link Integer} value. The gravity must
     *         consist of the flags given in {@link Dialog.Gravity}
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setGravity(final int gravity) {
        getProduct().setGravity(gravity);
        return self();
    }

    /**
     * Sets the width of the dialog, which is created by the builder.
     *
     * @param width
     *         The width, which should be set, in pixels as an {@link Integer} value. The width must
     *         be at least 1 or {@link Dialog#MATCH_PARENT}, respectively {@link
     *         Dialog#WRAP_CONTENT}
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setWidth(final int width) {
        getProduct().setWidth(width);
        return self();
    }

    /**
     * Sets the height of the dialog, which is created by the builder.
     *
     * @param height
     *         The height, which should be set, in pixels as an {@link Integer} value. The width
     *         must be at least 1 or {@link Dialog#MATCH_PARENT}, respectively {@link
     *         Dialog#WRAP_CONTENT}
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setHeight(final int height) {
        getProduct().setHeight(height);
        return self();
    }

    /**
     * Sets the maximum width of the dialog, which is created by the builder.
     *
     * @param maxWidth
     *         The maximum width, which should be set, in pixels as an {@link Integer} value. The
     *         maximum width must be at least 1, or -1, if no maximum width should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMaxWidth(final int maxWidth) {
        getProduct().setMaxWidth(maxWidth);
        return self();
    }

    /**
     * Sets the maximum height of the dialog, which is created by the builder.
     *
     * @param maxHeight
     *         The maximum height, which should be set, in pixels as an {@link Integer} value. The
     *         maximum height must be at least 1, or -1, if no maximum height should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMaxHeight(final int maxHeight) {
        getProduct().setMaxHeight(maxHeight);
        return self();
    }

    /**
     * Sets the margin of the dialog, which is created by the builder.
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
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMargin(final int left, final int top, final int right,
                                       final int bottom) {
        getProduct().setMargin(left, top, right, bottom);
        return self();
    }

    /**
     * Sets the padding of the dialog, which is created by the builder.
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
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setPadding(final int left, final int top, final int right,
                                        final int bottom) {
        getProduct().setPadding(left, top, right, bottom);
        return self();
    }

    /**
     * Sets, whether the dialog, which is created by the builder, should account for system screen
     * decorations such as the status bar and inset its content, or not.
     *
     * @param fitsSystemWindows
     *         True, if the dialog should inset its content, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setFitsSystemWindows(final boolean fitsSystemWindows) {
        getProduct().setFitsSystemWindows(fitsSystemWindows);
        return self();
    }

    /**
     * Sets, whether the dialog, which is created by the builder, should account for system screen
     * decorations such as the status bar and inset its content, or not.
     *
     * @param left
     *         True, if the dialog should inset its content at the left edge, false otherwise
     * @param top
     *         True, if the dialog should inset its content at the top edge, false otherwise
     * @param right
     *         True, if the dialog should inset its content at the right edge, false otherwise
     * @param bottom
     *         True, if the dialog should inset its content at the bottom edge, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setFitsSystemWindows(final boolean left, final boolean top,
                                                  final boolean right, final boolean bottom) {
        getProduct().setFitsSystemWindows(left, top, right, bottom);
        return self();
    }

    /**
     * Sets the color of the title of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setTitleColor(@ColorInt final int color) {
        getProduct().setTitleColor(color);
        return self();
    }

    /**
     * Sets the color of the message of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMessageColor(@ColorInt final int color) {
        getProduct().setMessageColor(color);
        return self();
    }

    /**
     * Sets the background of the dialog, which is created by the builder.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no custom background should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setBackground(@Nullable final Bitmap background) {
        getProduct().setBackground(background);
        return self();
    }

    /**
     * Sets the background of the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setBackground(@DrawableRes final int resourceId) {
        getProduct().setBackground(resourceId);
        return self();
    }

    /**
     * Sets the background color of the dialog, which is created by the builder.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value or -1, if no
     *         custom background color should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setBackgroundColor(@ColorInt final int color) {
        getProduct().setBackgroundColor(color);
        return self();
    }

    /**
     * Sets the title of the dialog, which is created by the builder.
     *
     * @param title
     *         The title, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no title should be shown
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setTitle(@Nullable final CharSequence title) {
        getProduct().setTitle(title);
        return self();
    }

    /**
     * Sets the title of the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the title, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setTitle(@StringRes final int resourceId) {
        getProduct().setTitle(resourceId);
        return self();
    }

    /**
     * Sets the message of the dialog, which is created by the builder.
     *
     * @param message
     *         The message, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no message should be shown
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMessage(@Nullable final CharSequence message) {
        getProduct().setMessage(message);
        return self();
    }

    /**
     * Sets the message of the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the message, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMessage(@StringRes final int resourceId) {
        getProduct().setMessage(resourceId);
        return self();
    }

    /**
     * Sets the icon of the dialog, which is created by the builder.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be shown
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setIcon(@Nullable final Bitmap icon) {
        getProduct().setIcon(icon);
        return self();
    }

    /**
     * Sets the icon of the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setIcon(@DrawableRes final int resourceId) {
        getProduct().setIcon(resourceId);
        return self();
    }

    /**
     * Set the icon of the dialog, which is created by the builder.
     *
     * @param attributeId
     *         The id of the theme attribute, which supplies the icon, which should be set, as an
     *         {@link Integer} value. The id must point to a valid drawable resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setIconAttribute(@AttrRes final int attributeId) {
        getProduct().setIconAttribute(attributeId);
        return self();
    }

    /**
     * Sets the custom view, which should be shown by the dialog, which is created by the builder.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be shown
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setView(@Nullable final View view) {
        getProduct().setView(view);
        return self();
    }

    /**
     * Sets the custom view, which should be shown by the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setView(@LayoutRes final int resourceId) {
        getProduct().setView(resourceId);
        return self();
    }

    /**
     * Sets the custom view, which should be used to show the title of the dialog, which is created
     * by the builder.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCustomTitle(@Nullable final View view) {
        getProduct().setCustomTitle(view);
        return self();
    }

    /**
     * Sets the custom view, which should be used to show the title of the dialog, which is created
     * by the builder.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCustomTitle(@LayoutRes final int resourceId) {
        getProduct().setCustomTitle(resourceId);
        return self();
    }

    /**
     * Sets the custom view, which should be used to show the message of the dialog, which is
     * created by the builder.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the message
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCustomMessage(@Nullable final View view) {
        getProduct().setCustomMessage(view);
        return self();
    }

    /**
     * Sets the custom view, which should be used to show the message of the dialog, which is
     * created by the builder.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCustomMessage(@LayoutRes final int resourceId) {
        getProduct().setCustomMessage(resourceId);
        return self();
    }

    /**
     * Sets the area of the dialog, which is created by the builder, which should be scrollable.
     *
     * @param area
     *         The area, which should be set, as a value of the enum {@link Area} or null, if no
     *         area should be scrollable
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setScrollableArea(@Nullable final Area area) {
        getProduct().setScrollableArea(area);
        return self();
    }

    /**
     * Sets the areas of the dialog, which is created by the builder, which should be scrollable.
     *
     * @param top
     *         The top-most area, which should be scrollable, as a value of the enum {@link Area} or
     *         null, if no area should be scrollable
     * @param bottom
     *         The bottom-most area, which should be scrollable, as a value of the enum {@link
     *         Area}. If the top-most area is null, the bottom-most are must be null as well. The
     *         index of the bottom-most area must be at least the index of the top-most area
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setScrollableArea(@Nullable final Area top,
                                               @Nullable final Area bottom) {
        getProduct().setScrollableArea(top, bottom);
        return self();
    }

    /**
     * Sets, whether dividers, which are located above and below the scrollable areas dialog, which
     * is created by the builder, should be shown when scrolling, or not.
     *
     * @param show
     *         True, if the dividers, which are located above and below the dialog's scrollable
     *         areas, should be shown when scrolling, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType showDividersOnScroll(boolean show) {
        getProduct().showDividersOnScroll(show);
        return self();
    }

    /**
     * Sets the color of dividers, which are contained by the dialog, which is created by the
     * builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setDividerColor(@ColorInt final int color) {
        getProduct().setDividerColor(color);
        return self();
    }

    /**
     * Sets the left and right margin of dividers, which are contained by the dialog, which is
     * created by the builder.
     *
     * @param margin
     *         The left and right margin, which should be set, in pixels as an {@link Integer}
     *         value. The margin must be at least 0
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setButtonBarDividerMargin(final int margin) {
        getProduct().setDividerMargin(margin);
        return self();
    }

}