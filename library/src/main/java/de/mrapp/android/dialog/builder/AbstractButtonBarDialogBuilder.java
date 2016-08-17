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
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.util.ThemeUtil;

/**
 * An abstract base class for all builders, which allow to create and show dialogs, which are
 * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
 * contain up to three buttons.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 */
public abstract class AbstractButtonBarDialogBuilder<DialogType extends ButtonBarDialog, BuilderType extends AbstractButtonBarDialogBuilder<DialogType, ?>>
        extends AbstractValidateableDialogBuilder<DialogType, BuilderType> {

    /**
     * Obtains the button text color from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the button text color should be obtained from, as an
     *         {@link Integer} value
     */
    private void obtainButtonTextColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogButtonTextColor});
        int defaultColor = ThemeUtil.getColor(getContext(), themeResourceId, R.attr.colorAccent);
        setButtonTextColor(typedArray.getColor(0, defaultColor));
    }

    /**
     * Obtains, whether the divider, which is located above the dialog's buttons, should be shown,
     * or not, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the visibility should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainShowButtonBarDivider(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogShowButtonBarDivider});
        showButtonBarDivider(typedArray.getBoolean(0, false));
    }

    /**
     * Obtains the color of the divider, which is located above the dialog's buttons, from a
     * specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainButtonBarDividerColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogButtonBarDividerColor});
        int defaultColor =
                ContextCompat.getColor(getContext(), R.color.button_bar_divider_color_light);
        setButtonBarDividerColor(typedArray.getColor(0, defaultColor));
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain up to three buttons.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractButtonBarDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain up to three buttons.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractButtonBarDialogBuilder(@NonNull final Context context,
                                          @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Sets the color of the button texts of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom
     *         button color should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setButtonTextColor(@ColorInt final int color) {
        getDialog().setButtonTextColor(color);
        return self();
    }

    /**
     * Sets, whether the buttons of the dialog, which is created by the builder, should be aligned
     * vertically, or not.
     *
     * @param stackButtons
     *         True, if the buttons of the dialog, which is created by the builder, should be
     *         aligned vertically, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType stackButtons(final boolean stackButtons) {
        getDialog().stackButtons(stackButtons);
        return self();
    }

    /**
     * Sets the text of the negative button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no negative button should be shown
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setNegativeButton(@Nullable final CharSequence text,
                                               @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setNegativeButton(text, listener);
        return self();
    }

    /**
     * Sets the text of the negative button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setNegativeButton(@StringRes final int resourceId,
                                               @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setNegativeButton(resourceId, listener);
        return self();
    }

    /**
     * Sets the text of the positive button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no positive button should be shown
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setPositiveButton(@Nullable final CharSequence text,
                                               @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setPositiveButton(text, listener);
        return self();
    }

    /**
     * Sets the text of the positive button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setPositiveButton(@StringRes final int resourceId,
                                               @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setPositiveButton(resourceId, listener);
        return self();
    }

    /**
     * Sets the text of the neutral button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no neutral button should be shown
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setNeutralButton(@Nullable final CharSequence text,
                                              @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setNeutralButton(text, listener);
        return self();
    }

    /**
     * Sets the text of the neutral button of the dialog, which is created by the builder.
     *
     * Note, that the attached listener is not restored using a dialog's
     * <code>onRestoreInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setNeutralButton(@StringRes final int resourceId,
                                              @Nullable final DialogInterface.OnClickListener listener) {
        getDialog().setNeutralButton(resourceId, listener);
        return self();
    }

    /**
     * Sets, whether the divider, which is located above the buttons of the dialog, which is created
     * by the builder, should be shown, or not.
     *
     * @param show
     *         True, if the divider, which is located above the dialog's buttons, should be show,
     *         false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType showButtonBarDivider(final boolean show) {
        getDialog().showButtonBarDivider(show);
        return self();
    }

    /**
     * Sets the color of the divider, which is located above the buttons of the dialog, which is
     * created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setButtonBarDividerColor(final int color) {
        getDialog().setButtonBarDividerColor(color);
        return self();
    }

    @CallSuper
    @Override
    protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
        super.obtainStyledAttributes(themeResourceId);
        obtainButtonTextColor(themeResourceId);
        obtainShowButtonBarDivider(themeResourceId);
        obtainButtonBarDividerColor(themeResourceId);
    }

}