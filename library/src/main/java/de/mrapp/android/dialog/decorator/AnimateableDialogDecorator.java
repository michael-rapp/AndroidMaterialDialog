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
package de.mrapp.android.dialog.decorator;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.view.Window;

import java.util.Collections;
import java.util.Map;

import de.mrapp.android.dialog.animation.CircleRevealAnimation;
import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.animation.FadeAnimation;
import de.mrapp.android.dialog.animation.RectangleRevealAnimation;
import de.mrapp.android.dialog.model.HeaderDialog;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;

/**
 * A decorator, which allows to modify the view hierarchy of an animateable dialog, which is
 * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and may
 * contain a header.
 *
 * @author Michael Rapp
 * @since 4.0.0
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
     * True, if the dialog has already been hidden, false otherwise.
     */
    private boolean hidden;

    /**
     * Creates an animator, which should be used for a fade animation.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param animation
     *         The animation as an instance of the class {@link FadeAnimation}. The animation may
     *         not be null
     * @param listener
     *         The listener, which should be notified about the animation's events, as an instance
     *         of the type {@link AnimatorListener} or null, if no listener should be notified
     * @param show
     *         True, if the animation should be used to show the dialog, false otherwise
     * @return The animator, which has been created, as an instance of the class {@link
     * ViewPropertyAnimator} or null, if no animation should be used
     */
    private ViewPropertyAnimator createAnimator(@NonNull final View animatedView,
                                                @NonNull final FadeAnimation animation,
                                                @Nullable final AnimatorListener listener,
                                                final boolean show) {
        if (animation.getAlpha() != null) {
            ViewPropertyAnimator animator =
                    animatedView.animate().setInterpolator(animation.getInterpolator())
                            .setDuration(getDuration(animatedView, animation))
                            .setStartDelay(animation.getStartDelay()).setListener(listener);

            if (show) {
                animatedView.setAlpha(animation.getAlpha());
                animator.alpha(1);
            } else {
                animatedView.setAlpha(1f);
                animator.alpha(animation.getAlpha());
            }

            return animator;
        }

        return null;
    }

    /**
     * Creates an animator, which should be used for a rectangular reveal animation.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param animation
     *         The animation as an instance of the class {@link RectangleRevealAnimation}. The
     *         animation may not be null
     * @param listener
     *         The listener, which should be notified about the animation's events, as an instance
     *         of the type {@link AnimatorListener} or null, if no listener should be notified
     * @return The animator, which has been created, as an instance of the class {@link
     * ViewPropertyAnimator} or null, if no animation should be used
     */
    @Nullable
    private ViewPropertyAnimator createAnimator(@NonNull final View animatedView,
                                                @NonNull final RectangleRevealAnimation animation,
                                                @Nullable final AnimatorListener listener) {
        if (animation.getX() != null || animation.getY() != null || animation.getWidth() != null ||
                animation.getHeight() != null || animation.getAlpha() != null) {
            return animatedView.animate().setInterpolator(animation.getInterpolator())
                    .setDuration(getDuration(animatedView, animation))
                    .setStartDelay(animation.getStartDelay()).setListener(listener);
        }

        return null;
    }

    /**
     * Configures an animator, which should be used to show the dialog using a rectangular reveal
     * animation.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param animation
     *         The animation as an instance of the class {@link RectangleRevealAnimation}. The
     *         animation may not be null
     * @param animator
     *         The animator, which should be configured, as an instance of the class {@link
     *         ViewPropertyAnimator}. The animator may not be null
     */
    private void configureShowAnimator(@NonNull final View animatedView,
                                       @NonNull final RectangleRevealAnimation animation,
                                       @NonNull final ViewPropertyAnimator animator) {
        int horizontalWindowInset =
                getDialog().getWindowInsetLeft() + getDialog().getWindowInsetRight();
        int verticalWindowInset =
                getDialog().getWindowInsetTop() + getDialog().getWindowInsetBottom();
        float translationX = 0;
        float translationY = 0;

        if (animation.getX() != null) {
            translationX = animation.getX() - animatedView.getLeft() - horizontalWindowInset;
        }

        if (animation.getY() != null) {
            translationY = animation.getY() - animatedView.getTop() - verticalWindowInset;
        }

        if (animation.getWidth() != null) {
            int viewWidth = animatedView.getWidth() - horizontalWindowInset;
            translationX -= (float) (viewWidth - animation.getWidth()) / 2f;
            animatedView.setScaleX((float) animation.getWidth() / (float) viewWidth);
            animator.scaleX(1);
        }

        if (animation.getHeight() != null) {
            int viewHeight = animatedView.getHeight() - verticalWindowInset;
            translationY -= (float) (viewHeight - animation.getHeight()) / 2f;
            animatedView.setScaleY((float) animation.getHeight() / (float) viewHeight);
            animator.scaleY(1);
        }

        if (animation.getAlpha() != null) {
            animatedView.setAlpha(animation.getAlpha());
            animator.alpha(1);
        }

        if (translationX != 0) {
            animatedView.setTranslationX(translationX);
            animator.translationX(0);
        }

        if (translationY != 0) {
            animatedView.setTranslationY(translationY);
            animator.translationY(0);
        }
    }

    /**
     * Configures an animator, which should be used to hide the dialog using a rectangular reveal
     * animation.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param animation
     *         The animation as an instance of the class {@link RectangleRevealAnimation}. The
     *         animation may not be null
     * @param animator
     *         The animator, which should be configured, as an instance of the class {@link
     *         ViewPropertyAnimator}. The animator may not be null
     */
    private void configureHideAnimator(@NonNull final View animatedView,
                                       @NonNull final RectangleRevealAnimation animation,
                                       @NonNull final ViewPropertyAnimator animator) {
        int horizontalWindowInset =
                getDialog().getWindowInsetLeft() + getDialog().getWindowInsetRight();
        int verticalWindowInset =
                getDialog().getWindowInsetTop() + getDialog().getWindowInsetBottom();
        float translationX = 0;
        float translationY = 0;

        if (animation.getX() != null) {
            translationX = animation.getX() - animatedView.getLeft() - horizontalWindowInset;
        }

        if (animation.getY() != null) {
            translationY = animation.getY() - animatedView.getTop() - verticalWindowInset;
        }

        if (animation.getWidth() != null) {
            int viewWidth = animatedView.getWidth() - horizontalWindowInset;
            translationX -= (float) (viewWidth - animation.getWidth()) / 2f;
            animator.scaleX((float) animation.getWidth() / (float) viewWidth);
        }

        if (animation.getHeight() != null) {
            int viewHeight = animatedView.getHeight() - verticalWindowInset;
            translationY -= (float) (viewHeight - animation.getHeight()) / 2f;
            animator.scaleY((float) animation.getHeight() / (float) viewHeight);
        }

        if (animation.getAlpha() != null) {
            animator.alpha(animation.getAlpha());
        }

        if (translationX != 0) {
            animator.translationX(translationX);
        }

        if (translationY != 0) {
            animator.translationY(translationY);
        }
    }

    /**
     * Creates an animator, which should be used for a circle reveal animation.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param rootView
     *         The root view of the dialog as an instance of the class {@link View}. The view may
     *         not be null
     * @param animation
     *         The animation as an instance of the class {@link CircleRevealAnimation}. The
     *         animation may not be null
     * @param listener
     *         The listener, which should be notified about the animation's events, as an instance
     *         of type {@link AnimatorListener} or null, if no listener should be notified
     * @param show
     *         True, if the animation should be used for showing the dialog, false otherwise
     * @return The animator, which has been created, as an instance of the class {@link Animator} or
     * null, if no animation should be used
     */
    @Nullable
    private Animator createAnimator(@NonNull final View animatedView, @NonNull final View rootView,
                                    @NonNull final CircleRevealAnimation animation,
                                    @Nullable final AnimatorListener listener, final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            long duration = getDuration(animatedView, animation);
            int horizontalDistance = Math.max(Math.abs(rootView.getLeft() - animation.getX()),
                    Math.abs(rootView.getRight() - animation.getX()));
            int verticalDistance = Math.max(Math.abs(rootView.getTop() - animation.getY()),
                    Math.abs(rootView.getBottom() - animation.getY()));
            float maxRadius = (float) Math
                    .sqrt(Math.pow(horizontalDistance, 2) + Math.pow(verticalDistance, 2));
            Animator animator = ViewAnimationUtils
                    .createCircularReveal(animatedView, animation.getX(), animation.getY(),
                            show ? animation.getRadius() : maxRadius,
                            show ? maxRadius : animation.getRadius());
            animator.setInterpolator(animation.getInterpolator());
            animator.setStartDelay(animation.getStartDelay());
            animator.setDuration(duration);

            if (listener != null) {
                animator.addListener(listener);
            }

            if (animation.getAlpha() != null) {
                ObjectAnimator alphaAnimator = ObjectAnimator
                        .ofFloat(animatedView, "alpha", show ? animation.getAlpha() : 1,
                                show ? 1 : animation.getAlpha());
                alphaAnimator.setInterpolator(animation.getInterpolator());
                alphaAnimator.setStartDelay(animation.getStartDelay());
                alphaAnimator.setDuration(duration);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator, alphaAnimator);
                return animatorSet;
            }

            return animator;
        }

        return null;
    }

    /**
     * Returns the duration, which should be used for an animation, depending on whether a previous
     * animation is still running, or not.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param animation
     *         The animation as an instance of the class {@link DialogAnimation}. The animation may
     *         not be null
     * @return The duration, which used for the given animation, in milliseconds as a {@link Long}
     * value
     */
    private long getDuration(@NonNull final View animatedView,
                             @NonNull final DialogAnimation animation) {
        double scale = 1;

        if (animatedView.getAnimation() != null) {
            scale = (double) (System.currentTimeMillis() -
                    animatedView.getAnimation().getStartTime()) /
                    (double) animatedView.getAnimation().getDuration();
            animatedView.getAnimation().cancel();
        }

        return Math.round(animation.getDuration() * scale);
    }

    /**
     * Creates and returns an animation listener, which allows to hide the animated view once the
     * animation is finished.
     *
     * @param animatedView
     *         The animated view as an instance of the class {@link View}. The view may not be null
     * @param listener
     *         The listener, which should be notified, as an instance of the type {@link
     *         AnimatorListener} or null, if no listener should be notified
     * @return The animation listener, which has been created, as an instance of the type {@link
     * AnimatorListener}
     */
    private AnimatorListener createHideAnimationListener(@NonNull final View animatedView,
                                                         @Nullable final AnimatorListener listener) {
        return new AnimatorListener() {

            @Override
            public void onAnimationStart(final Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                animatedView.setVisibility(View.GONE);

                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(final Animator animation) {
                if (listener != null) {
                    listener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }

        };
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
     *         The listener, which should be notified about the animation's events, as an instance
     *         of the type {@link AnimatorListener} or null, if no listener should be notified
     * @return True, if the dialog has been shown in an animated manner, false otherwise
     */
    public final boolean showAnimated(@Nullable final DialogAnimation animation,
                                      @Nullable final AnimatorListener listener) {
        hidden = false;

        if (animation != null) {
            Window window = getWindow();
            View view = getRootView();

            if (view != null && window != null) {
                View animatedView = getDialog().isFullscreen() ? window.getDecorView() : view;

                if (animation instanceof FadeAnimation) {
                    FadeAnimation fadeAnimation = (FadeAnimation) animation;
                    ViewPropertyAnimator animator =
                            createAnimator(animatedView, fadeAnimation, listener, true);

                    if (animator != null) {
                        animator.start();
                        return true;
                    }
                } else if (animation instanceof RectangleRevealAnimation) {
                    RectangleRevealAnimation rectangleRevealAnimation =
                            (RectangleRevealAnimation) animation;
                    ViewPropertyAnimator animator =
                            createAnimator(animatedView, rectangleRevealAnimation, listener);

                    if (animator != null) {
                        configureShowAnimator(animatedView, rectangleRevealAnimation, animator);
                        animator.start();
                        return true;
                    }
                } else if (animation instanceof CircleRevealAnimation) {
                    CircleRevealAnimation circleRevealAnimation = (CircleRevealAnimation) animation;
                    Animator animator = createAnimator(window.getDecorView(), animatedView,
                            circleRevealAnimation, listener, true);

                    if (animator != null) {
                        animator.start();
                        return true;
                    }
                } else {
                    throw new RuntimeException(
                            "Unknown type of animation: " + animation.getClass().getSimpleName());
                }
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
     *         The listener, which should be notified about the animation's events, as an instance
     *         of the type {@link AnimatorListener} or null, if no listener should be notified
     * @return True, if the dialog has been hidden in an animated manner, false otherwise
     */
    public final boolean hideAnimated(@Nullable final DialogAnimation animation,
                                      @Nullable final AnimatorListener listener) {
        if (!hidden) {
            hidden = true;

            if (animation != null) {
                View view = getRootView();
                Window window = getWindow();

                if (view != null && window != null) {
                    View animatedView = getDialog().isFullscreen() ? window.getDecorView() : view;

                    if (animation instanceof FadeAnimation) {
                        FadeAnimation fadeAnimation = (FadeAnimation) animation;
                        ViewPropertyAnimator animator =
                                createAnimator(animatedView, fadeAnimation, listener, false);

                        if (animator != null) {
                            animator.start();
                            return true;
                        }
                    } else if (animation instanceof RectangleRevealAnimation) {
                        RectangleRevealAnimation rectangleRevealAnimation =
                                (RectangleRevealAnimation) animation;
                        ViewPropertyAnimator animator =
                                createAnimator(animatedView, rectangleRevealAnimation,
                                        createHideAnimationListener(animatedView, listener));

                        if (animator != null) {
                            configureHideAnimator(animatedView, rectangleRevealAnimation, animator);
                            animator.start();
                            return true;
                        }
                    } else if (animation instanceof CircleRevealAnimation) {
                        CircleRevealAnimation circleRevealAnimation =
                                (CircleRevealAnimation) animation;
                        Animator animator = createAnimator(window.getDecorView(), animatedView,
                                circleRevealAnimation, listener, false);

                        if (animator != null) {
                            animator.start();
                            return true;
                        }
                    } else {
                        throw new RuntimeException("Unknown type of animation: " +
                                animation.getClass().getSimpleName());
                    }
                }
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

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {

    }

}