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
public abstract class AbstractMaterialDialogBuilder<DialogType extends MaterialDialog, BuilderType extends AbstractMaterialDialogBuilder<DialogType, ?>> {

    /**
     * The context, which is used by the builder.
     */
    private Context context;

    /**
     * The dialog, which is configured by the builder.
     */
    private DialogType dialog;

    /**
     * Initializes the builder.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value or 0, if the default theme should be used
     */
    private void initialize(@NonNull final Context context, @StyleRes final int themeResourceId) {
        int themeId = themeResourceId;

        if (themeId == 0) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.materialDialogTheme, typedValue, true);
            themeId = typedValue.resourceId;
            themeId = themeId != 0 ? themeId : R.style.MaterialDialog_Light;
        }

        this.context = new ContextThemeWrapper(context, themeId);
        this.dialog = onCreateDialog(context, themeId);
        obtainStyledAttributes(themeId);
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
     * Returns the builder itself, casted to the generic type BuilderType.
     *
     * @return The builder itself as an instance of the generic type BuilderType
     */
    @SuppressWarnings("unchecked")
    protected final BuilderType self() {
        return (BuilderType) this;
    }

    /**
     * Returns the dialog, which is configured by the builder.
     *
     * @return The dialog, which is configured by the builder, as an instance of the generic type
     * BuilderType
     */
    protected final DialogType getDialog() {
        return dialog;
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
        obtainGravity(themeResourceId);
        obtainWidth(themeResourceId);
        obtainHeight(themeResourceId);
        obtainMaxWidth(themeResourceId);
        obtainMaxHeight(themeResourceId);
        obtainMargin(themeResourceId);
        obtainBackground(themeResourceId);
        obtainMessageColor(themeResourceId);
        obtainTitleColor(themeResourceId);
    }

    /**
     * The method, which is invoked on subclasses in order to create the dialog, which is configured
     * by the builder.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value
     * @return The dialog, which has been created, as an instance of the generic type DialogType
     */
    protected abstract DialogType onCreateDialog(@NonNull final Context context,
                                                 @StyleRes final int themeResourceId);

    /**
     * Creates a new builder, which allows to create and show dialogs, which are designed according
     * to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractMaterialDialogBuilder(@NonNull final Context context) {
        this(context, 0);
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
        initialize(context, themeResourceId);
    }

    /**
     * Returns the context, which is used by the builder.
     *
     * @return The context, which is used by the builder, as an instance of the class {@link
     * Context}
     */
    public final Context getContext() {
        return context;
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
        getDialog().setCancelable(cancelable);
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
        getDialog().setOnShowListener(listener);
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
        getDialog().setOnCancelListener(listener);
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
        getDialog().setOnDismissListener(listener);
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
        getDialog().setGravity(gravity);
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
        getDialog().setWidth(width);
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
        getDialog().setHeight(height);
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
        getDialog().setMaxWidth(maxWidth);
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
        getDialog().setMaxHeight(maxHeight);
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
        getDialog().setMargin(left, top, right, bottom);
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
        getDialog().setTitleColor(color);
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
        getDialog().setMessageColor(color);
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
        getDialog().setBackground(background);
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
        getDialog().setBackground(resourceId);
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
        getDialog().setBackgroundColor(color);
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
        getDialog().setTitle(title);
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
        getDialog().setTitle(resourceId);
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
        getDialog().setMessage(message);
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
        getDialog().setMessage(resourceId);
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
        getDialog().setIcon(icon);
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
        getDialog().setIcon(resourceId);
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
        getDialog().setIconAttribute(attributeId);
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
        getDialog().setView(view);
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
        getDialog().setView(resourceId);
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
        getDialog().setCustomTitle(view);
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
        getDialog().setCustomTitle(resourceId);
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
        getDialog().setCustomMessage(view);
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
        getDialog().setCustomMessage(resourceId);
        return self();
    }

    /**
     * Creates a dialog with the arguments, which have been supplied to the builder. Calling this
     * method does not display the dialog.
     *
     * @return The dialog, which has been created as an instance of the generic type DialogType
     */
    public final DialogType create() {
        return getDialog();
    }

}