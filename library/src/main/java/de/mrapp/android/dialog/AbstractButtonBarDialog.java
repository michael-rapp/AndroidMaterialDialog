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
package de.mrapp.android.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;

import de.mrapp.android.dialog.decorator.ButtonBarDialogDecorator;
import de.mrapp.android.dialog.model.ButtonBarDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and may contain up to three buttons.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractButtonBarDialog extends AbstractValidateableDialog
        implements ButtonBarDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final ButtonBarDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contain up to three buttons.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractButtonBarDialog(@NonNull final Context context,
                                      @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new ButtonBarDialogDecorator(this);
        addDecorator(decorator);
    }

    @Override
    public final Button getButton(final int whichButton) {
        return decorator.getButton(whichButton);
    }

    @Override
    public final void setPositiveButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        decorator.setPositiveButton(text, listener);
    }

    @Override
    public final void setPositiveButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        decorator.setPositiveButton(resourceId, listener);
    }

    @Override
    public final void setNegativeButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        decorator.setNegativeButton(text, listener);
    }

    @Override
    public final void setNegativeButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        decorator.setNegativeButton(resourceId, listener);
    }

    @Override
    public final void setNeutralButton(@Nullable final CharSequence text,
                                       @Nullable final OnClickListener listener) {
        decorator.setNeutralButton(text, listener);
    }

    @Override
    public final void setNeutralButton(@StringRes final int resourceId,
                                       @Nullable final OnClickListener listener) {
        decorator.setNeutralButton(resourceId, listener);
    }

    @Override
    public final boolean areButtonsStacked() {
        return decorator.areButtonsStacked();
    }

    @Override
    public final void stackButtons(final boolean stackButtons) {
        decorator.stackButtons(stackButtons);
    }

    @Override
    public final int getButtonTextColor() {
        return decorator.getButtonTextColor();
    }

    @Override
    public final void setButtonTextColor(@ColorInt final int color) {
        decorator.setButtonTextColor(color);
    }

    @Override
    public final int getDisabledButtonTextColor() {
        return decorator.getDisabledButtonTextColor();
    }

    @Override
    public final void setDisabledButtonTextColor(@ColorInt final int color) {
        decorator.setDisabledButtonTextColor(color);
    }

    @Override
    public final boolean isButtonBarDividerShown() {
        return decorator.isButtonBarDividerShown();
    }

    @Override
    public final void showButtonBarDivider(final boolean show) {
        decorator.showButtonBarDivider(show);
    }

    @Override
    public final boolean isCustomButtonBarUsed() {
        return decorator.isCustomButtonBarUsed();
    }

    @Override
    public final void setCustomButtonBar(@LayoutRes final int resourceId) {
        decorator.setCustomButtonBar(resourceId);
    }

    @Override
    public final void setCustomButtonBar(@Nullable final View view) {
        decorator.setCustomButtonBar(view);
    }

    @NonNull
    @CallSuper
    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        decorator.onSaveInstanceState(outState);
        return outState;
    }

    @CallSuper
    @Override
    public void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

}