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
package de.mrapp.android.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;

import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.decorator.AnimateableDialogDecorator;
import de.mrapp.android.dialog.model.AnimateableDialog;

/**
 * An abstract base class for all animateable dialogs, which are designed according to Android 5's
 * Material Design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 3.7.0
 */
public abstract class AbstractAnimateableDialog extends AbstractMaterialDialog
        implements AnimateableDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final AnimateableDialogDecorator decorator;

    /**
     * The listener, which should be notified, when the dialog has been shown.
     */
    private OnShowListener onShowListener;

    /**
     * Creates and returns a listener, which allows to observe, when the dialog has been shown.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnShowListener}
     */
    private OnShowListener createOnShowListener() {
        return new OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                decorator.showAnimated();

                if (onShowListener != null) {
                    onShowListener.onShow(dialog);
                }
            }

        };
    }

    /**
     * Creates an animateable dialog, which is designed according to Android 5's Material Design
     * guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractAnimateableDialog(@NonNull final Context context,
                                        @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.decorator = new AnimateableDialogDecorator(this);
        super.setOnShowListener(createOnShowListener());
    }

    @Override
    public final DialogAnimation getShowAnimation() {
        return decorator.getShowAnimation();
    }

    @Override
    public final void setShowAnimation(@Nullable final DialogAnimation animation) {
        decorator.setShowAnimation(animation);
    }

    @Override
    public final DialogAnimation getDismissAnimation() {
        return decorator.getDismissAnimation();
    }

    @Override
    public final void setDismissAnimation(@Nullable final DialogAnimation animation) {
        decorator.setDismissAnimation(animation);
    }

    @Override
    public final DialogAnimation getCancelAnimation() {
        return decorator.getCancelAnimation();
    }

    @Override
    public final void setCancelAnimation(@Nullable final DialogAnimation animation) {
        decorator.setCancelAnimation(animation);
    }

    @Override
    public final void setOnShowListener(@Nullable final OnShowListener listener) {
        this.onShowListener = listener;
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

    @CallSuper
    @Override
    protected void onAttachDecorators(@NonNull final Window window, @NonNull final View view) {
        super.onAttachDecorators(window, view);
        decorator.attach(window, view);

    }

    @CallSuper
    @Override
    protected void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}