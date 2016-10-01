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
package de.mrapp.android.dialog.decorator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;

import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.animation.RectangleRevealAnimation;
import de.mrapp.android.dialog.model.MaterialDialog;

/**
 * A decorator, which allows to modify the view hierarchy of an animateable dialog, which is
 * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and may
 * contain a header.
 *
 * @author Michael Rapp
 * @since 3.7.0
 */
public class AnimateableDialogDecorator extends AbstractDialogDecorator<MaterialDialog>
        implements de.mrapp.android.dialog.model.AnimateableDialogDecorator,
        DialogInterface.OnShowListener {

    /**
     * The animation, which is used to show the dialog.
     */
    private DialogAnimation showAnimation;

    /**
     * The animation, which is used to dismiss the dialog.
     */
    private DialogAnimation dismissAnimation;

    /**
     * The animation, which is used to cancel the dialog.
     */
    private DialogAnimation cancelAnimation;

    /**
     * Shows the dialog is an animated manner using a rectangular reveal animation.
     *
     * @param view
     *         The view, which should be animated, as an instance of the class {@link View}. The
     *         view may not be null
     * @param animation
     *         The rectangular reveal animation, which should be used, as an instance of the class
     *         {@link RectangleRevealAnimation}. The animation may not be null
     */
    private void animateShow(@NonNull final View view,
                             @NonNull final RectangleRevealAnimation animation) {
        if (animation.getX() != null || animation.getY() != null || animation.getWidth() != null ||
                animation.getHeight() != null) {
            ViewPropertyAnimator animator =
                    view.animate().setInterpolator(animation.getInterpolator())
                            .setDuration(animation.getDuration());

            if (animation.getX() != null) {
                view.setTranslationX(animation.getX() - view.getX());
                animator.translationX(0);
            }

            if (animation.getY() != null) {
                view.setTranslationY(animation.getY() - view.getY());
                animator.translationY(0);
            }

            if (animation.getWidth() != null) {
                view.setScaleX((float) animation.getWidth() / (float) view.getWidth());
                animator.scaleX(1);
            }

            if (animation.getHeight() != null) {
                view.setScaleY((float) animation.getHeight() / (float) view.getHeight());
                animator.scaleY(1);
            }

            animator.start();
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of an animateable dialog,
     * which is designed according to Android 5's Material design guidelines even on pre-Lollipop
     * devices.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link MaterialDialog}. The dialog may not be null
     */
    public AnimateableDialogDecorator(@NonNull final MaterialDialog dialog) {
        super(dialog);
    }

    @Override
    public final DialogAnimation getShowAnimation() {
        return showAnimation;
    }

    @Override
    public final void setShowAnimation(@Nullable final DialogAnimation animation) {
        this.showAnimation = animation;
    }

    @Override
    public final DialogAnimation getDismissAnimation() {
        return dismissAnimation;
    }

    @Override
    public final void setDismissAnimation(@Nullable final DialogAnimation animation) {
        this.dismissAnimation = animation;
    }

    @Override
    public final DialogAnimation getCancelAnimation() {
        return cancelAnimation;
    }

    @Override
    public final void setCancelAnimation(@Nullable final DialogAnimation animation) {
        this.cancelAnimation = animation;
    }

    @Override
    public void onShow(final DialogInterface dialog) {
        if (getView() != null && showAnimation != null) {
            if (showAnimation instanceof RectangleRevealAnimation) {
                animateShow(getView(), (RectangleRevealAnimation) showAnimation);
            } else {
                throw new RuntimeException(
                        "Unknown typed of animation: " + showAnimation.getClass().getSimpleName());
            }
        }
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {

    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {

    }

    @Override
    protected final void onAttach(@NonNull final Window window, @NonNull final View view) {

    }

    @Override
    protected final void onDetach() {

    }
}