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

import android.animation.Animator.AnimatorListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.animation.RectangleRevealAnimation;
import de.mrapp.android.dialog.model.HeaderDialog;

/**
 * A decorator, which allows to modify the view hierarchy of an animateable dialog, which is
 * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and may
 * contain a header.
 *
 * @author Michael Rapp
 * @since 3.7.0
 */
public class AnimateableDialogDecorator extends AbstractDialogDecorator<HeaderDialog>
        implements de.mrapp.android.dialog.model.AnimateableDialogDecorator {

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
     * @param animation
     *         The rectangular reveal animation, which should be used, as an instance of the class
     *         {@link RectangleRevealAnimation}. The animation may not be null
     * @param listener
     *         The animation listener, which should be used, as an instance of the type {@link
     *         AnimatorListener} or null, if no animation listener should be used
     * @return True, if the dialog has been shown in an animated manner, false otherwise
     */
    private boolean showAnimated(@NonNull final RectangleRevealAnimation animation,
                                 @Nullable final AnimatorListener listener) {
        if (getView() != null && getWindow() != null) {
            View view = getDialog().isFullscreen() ? getWindow().getDecorView() : getView();

            if (animation.getX() != null || animation.getY() != null ||
                    animation.getWidth() != null || animation.getHeight() != null ||
                    animation.getAlpha() != null) {
                int shadowWidth = getDialog().isFullscreen() ? 0 : getContext().getResources()
                        .getDimensionPixelSize(R.dimen.dialog_shadow_width);
                ViewPropertyAnimator animator =
                        view.animate().setInterpolator(animation.getInterpolator())
                                .setDuration(animation.getDuration())
                                .setStartDelay(animation.getStartDelay()).setListener(listener);
                float translationX = 0;
                float translationY = 0;

                if (animation.getX() != null) {
                    translationX = animation.getX() - view.getLeft() - shadowWidth;
                }

                if (animation.getY() != null) {
                    translationY = animation.getY() - view.getTop() - shadowWidth;
                }

                if (animation.getWidth() != null) {
                    int viewWidth = view.getWidth() - 2 * shadowWidth;
                    translationX -= (float) (viewWidth - animation.getWidth()) / 2f;
                    view.setScaleX((float) animation.getWidth() / (float) viewWidth);
                    animator.scaleX(1);
                }

                if (animation.getHeight() != null) {
                    int viewHeight = view.getHeight() - 2 * shadowWidth;
                    translationY -= (float) (viewHeight - animation.getHeight()) / 2f;
                    view.setScaleY((float) animation.getHeight() / (float) viewHeight);
                    animator.scaleY(1);
                }

                if (animation.getAlpha() != null) {
                    view.setAlpha(animation.getAlpha());
                    animator.alpha(1);
                }

                if (translationX != 0) {
                    view.setTranslationX(translationX);
                    animator.translationX(0);
                }

                if (translationY != 0) {
                    view.setTranslationY(translationY);
                    animator.translationY(0);
                }

                animator.start();
                return true;
            }
        }

        return false;
    }

    /**
     * Hides the dialog is an animated manner using a rectangular reveal animation.
     *
     * @param animation
     *         The rectangular reveal animation, which should be used, as an instance of the class
     *         {@link RectangleRevealAnimation}. The animation may not be null
     * @param listener
     *         The animation listener, which should be used, as an instance of the type {@link
     *         AnimatorListener} or null, if no animation listener should be used
     * @return True, if the dialog has been hidden in an animated manner, false otherwise
     */
    private boolean hideAnimated(final RectangleRevealAnimation animation,
                                 @Nullable AnimatorListener listener) {
        if (getView() != null && getWindow() != null) {
            View view = getDialog().isFullscreen() ? getWindow().getDecorView() : getView();

            if (animation.getX() != null || animation.getY() != null ||
                    animation.getWidth() != null || animation.getHeight() != null ||
                    animation.getAlpha() != null) {
                int shadowWidth = getDialog().isFullscreen() ? 0 : getContext().getResources()
                        .getDimensionPixelSize(R.dimen.dialog_shadow_width);
                ViewPropertyAnimator animator =
                        view.animate().setInterpolator(animation.getInterpolator())
                                .setDuration(animation.getDuration())
                                .setStartDelay(animation.getStartDelay()).setListener(listener);
                float translationX = 0;
                float translationY = 0;

                if (animation.getX() != null) {
                    translationX = animation.getX() - view.getLeft() - shadowWidth;
                }

                if (animation.getY() != null) {
                    translationY = animation.getY() - view.getTop() - shadowWidth;
                }

                if (animation.getWidth() != null) {
                    int viewWidth = view.getWidth() - 2 * shadowWidth;
                    translationX -= (float) (viewWidth - animation.getWidth()) / 2f;
                    animator.scaleX((float) animation.getWidth() / (float) viewWidth);
                }

                if (animation.getHeight() != null) {
                    int viewHeight = view.getHeight() - 2 * shadowWidth;
                    translationY -= (float) (viewHeight - animation.getHeight()) / 2f;
                    animator.scaleY((float) animation.getHeight() / (float) viewHeight);
                }

                if (animation.getAlpha() != null) {
                    animator.alpha(1);
                }

                if (translationX != 0) {
                    animator.translationX(translationX);
                }

                if (translationY != 0) {
                    animator.translationY(translationY);
                }

                animator.start();
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of an animateable dialog,
     * which is designed according to Android 5's Material design guidelines even on pre-Lollipop
     * devices.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link HeaderDialog}. The dialog may not be null
     */
    public AnimateableDialogDecorator(@NonNull final HeaderDialog dialog) {
        super(dialog);
    }

    /**
     * Shows the dialog in an animated manner, according to a specific animation.
     *
     * @param animation
     *         The animation, which should be used, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     * @param listener
     *         The animation listener, which should be used, as an instance of the type {@link
     *         AnimatorListener} or null, if no animation listener should be used
     * @return True, if the dialog has been shown in an animated manner, false otherwise
     */
    public final boolean showAnimated(@Nullable final DialogAnimation animation,
                                      @Nullable final AnimatorListener listener) {
        if (showAnimation != null) {
            if (showAnimation instanceof RectangleRevealAnimation) {
                return showAnimated((RectangleRevealAnimation) showAnimation, listener);
            } else {
                throw new RuntimeException(
                        "Unknown type of animation: " + showAnimation.getClass().getSimpleName());
            }
        }

        return false;
    }

    /**
     * Hides the dialog in an animated manner, according to a specific animation.
     *
     * @param animation
     *         The animation, which should be used, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     * @param listener
     *         The animation listener, which should be used, as an instance of the type {@link
     *         AnimatorListener} or null, if no animation listener should be used
     * @return True, if the dialog has been hidden in an animated manner, false otherwise
     */
    public final boolean hideAnimated(@Nullable final DialogAnimation animation,
                                      @Nullable final AnimatorListener listener) {
        if (animation != null) {
            if (animation instanceof RectangleRevealAnimation) {
                return hideAnimated((RectangleRevealAnimation) animation, listener);
            } else {
                throw new RuntimeException(
                        "Unknown type of animation: " + animation.getClass().getSimpleName());
            }
        }

        return false;
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