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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.decorator.AnimateableDialogDecorator;
import de.mrapp.android.dialog.model.AnimateableDialog;

/**
 * An abstract base class for all animateable dialogs, which are designed according to Android 5's
 * Material Design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class AbstractAnimateableDialog extends AbstractHeaderDialog
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
                decorator.showAnimated(getShowAnimation(), null);

                if (onShowListener != null) {
                    onShowListener.onShow(dialog);
                }
            }

        };
    }

    /**
     * Creates and returns an animation listener, which allows to dismiss the dialog, once the
     * animation, which is used to hide it, has finished.
     *
     * @return The animation listener, which has been created, as an instance of the type {@link
     * AnimatorListener}
     */
    private AnimatorListener createDismissAnimationListener() {
        return new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AbstractAnimateableDialog.super.dismiss();
            }

        };
    }

    /**
     * Creates and returns an animation listener, which allows to cancel the dialog, once the
     * animation, which is used to hide it, has finished.
     *
     * @return The animation listener, which has been created, as an instance of the type {@link
     * AnimatorListener}
     */
    private AnimatorListener createCancelAnimationListener() {
        return new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);
                AbstractAnimateableDialog.super.cancel();
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
        decorator = new AnimateableDialogDecorator(this);
        addDecorator(decorator);
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

    @Override
    public final void dismiss() {
        if (!decorator.hideAnimated(getDismissAnimation(), createDismissAnimationListener())) {
            super.dismiss();
        }
    }

    @Override
    public final void cancel() {
        if (!decorator.hideAnimated(getCancelAnimation(), createCancelAnimationListener())) {
            super.cancel();
        }
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

    @Override
    protected final boolean onCanceledOnTouchOutside() {
        if (!decorator.hideAnimated(getCancelAnimation(), createCancelAnimationListener())) {
            super.cancel();
            return true;
        }

        return false;
    }

}