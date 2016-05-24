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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import android.view.View;

import de.mrapp.android.dialog.R;
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
 */
public abstract class AbstractMaterialDialogBuilder<DialogType extends MaterialDialog, BuilderType extends AbstractMaterialDialogBuilder<DialogType, ?>> {

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
     *         Integer} value, or -1, if the default theme should be used
     */
    private void initialize(@NonNull final Context context, @StyleRes final int themeResourceId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.materialDialogTheme, typedValue, true);
        int themeId = typedValue.resourceId;
        themeId = themeId != 0 ? themeId : R.style.MaterialDialog_Light;
        dialog = onCreateDialog(context, themeId);
        obtainStyledAttributes(themeId);
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
        int color = typedArray.getColor(0, -1);

        if (color != -1) {
            setBackgroundColor(color);
        } else {
            int resourceId = typedArray.getResourceId(0, 0);

            if (resourceId != 0) {
                setBackground(resourceId);
            }
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
     * Obtains, whether the divider, which is located above the dialog's custom view, should be
     * shown, or not, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the visibility should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainShowContentDivider(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogShowContentDivider});
        showContentDivider(typedArray.getBoolean(0, false));
    }

    /**
     * Obtains the color of the divider, which is located above the dialog's custom view, from a
     * specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the divider color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainContentDividerColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogContentDividerColor});
        int defaultColor =
                ContextCompat.getColor(getContext(), R.color.content_divider_color_light);
        setContentDividerColor(typedArray.getColor(0, defaultColor));
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
        obtainBackground(themeResourceId);
        obtainMessageColor(themeResourceId);
        obtainTitleColor(themeResourceId);
        obtainShowContentDivider(themeResourceId);
        obtainContentDividerColor(themeResourceId);
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
        this(context, -1);
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
        return getDialog().getContext();
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
     * Sets the listener, which should be notified, if a key is dispatched to the dialog, which is
     * created by the builder.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnKeyListener}, or null, if no listener should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setOnKeyListener(
            @Nullable final DialogInterface.OnKeyListener listener) {
        getDialog().setOnKeyListener(listener);
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
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no custom background should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setBackground(@Nullable final Drawable background) {
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
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be shown
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setIcon(@Nullable final Drawable icon) {
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
     * Sets, whether the divider, which is located above the custom view of the dialog, which is
     * created by the builder, should be shown, or not.
     *
     * @param show
     *         True, if the divider, which is located above the dialog's custom view, should be
     *         shown, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType showContentDivider(final boolean show) {
        getDialog().showContentDivider(show);
        return self();
    }

    /**
     * Sets the color of the divider, which is located above the custom view of the dialog, which is
     * created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setContentDividerColor(@ColorInt final int color) {
        getDialog().setContentDividerColor(color);
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