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
package de.mrapp.android.dialog.animation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import de.mrapp.android.dialog.builder.AbstractBuilder;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all animations, which can be used to show or hide a dialog.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class DialogAnimation {

    /**
     * An abstract base class for all builders, which allow to create animations, which can be used
     * to show or hide a dialog.
     *
     * @param <AnimationType>
     *         The type of the animations, which are created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    protected static abstract class AbstractDialogAnimationBuilder<AnimationType extends DialogAnimation, BuilderType extends AbstractDialogAnimationBuilder<AnimationType, ?>>
            extends AbstractBuilder<AnimationType, BuilderType> {

        /**
         * Creates a new builder, which allows to create animations, which can be used to show or
         * hide a dialog.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        protected AbstractDialogAnimationBuilder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Sets the interpolator, which should be used by the animation, which is created by the
         * builder.
         *
         * @param interpolator
         *         The interpolator, which should be set, as an instance of the type {@link
         *         Interpolator}. The interpolator may not be null
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setInterpolator(@NonNull final Interpolator interpolator) {
            getProduct().setInterpolator(interpolator);
            return self();
        }

        /**
         * Sets the duration of the animation, which is created by the builder.
         *
         * @param duration
         *         The duration, which should be set, in milliseconds as a {@link Long} value. The
         *         duration must be at least 1
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setDuration(final long duration) {
            getProduct().setDuration(duration);
            return self();
        }

        /**
         * Sets the delay until the animation, which is created by the builder, is started.
         *
         * @param startDelay
         *         The delay, which should be set, as a {@link Long} value. The delay must be at
         *         least 0
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setStartDelay(final long startDelay) {
            getProduct().setStartDelay(startDelay);
            return self();
        }

        /**
         * Sets the alpha, which should be used by the animation, which is created by the builder.
         *
         * @param alpha
         *         The alpha, which should be set, as a {@link Float} value. The alpha must be at
         *         least 0 and at maximum 1
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setAlpha(final float alpha) {
            getProduct().setAlpha(alpha);
            return self();
        }

    }

    /**
     * The interpolator, which is used by the animation.
     */
    private Interpolator interpolator;

    /**
     * The duration of the animation in milliseconds.
     */
    private long duration;

    /**
     * The delay until the animation is started.
     */
    private long startDelay;

    /**
     * The alpha, which is used by the animation.
     */
    private Float alpha;

    /**
     * Sets the interpolator, which should be used by the animation.
     *
     * @param interpolator
     *         The interpolator, which should be set, as an instance of the type {@link
     *         Interpolator}. The interpolator may not be null
     */
    protected final void setInterpolator(@NonNull final Interpolator interpolator) {
        ensureNotNull(interpolator, "The interpolator may not be null");
        this.interpolator = interpolator;
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration
     *         The duration, which should be set, in milliseconds as a {@link Long} value. The
     *         duration must be at least 1
     */
    protected final void setDuration(final long duration) {
        ensureAtLeast(duration, 1, "The duration must be at least 1");
        this.duration = duration;
    }

    /**
     * Sets the delay until the animation is started.
     *
     * @param startDelay
     *         The delay, which should be set, in milliseconds as a {@link Long} value. The delay
     *         must be at least 0
     */
    protected final void setStartDelay(final long startDelay) {
        ensureAtLeast(startDelay, 0, "The start delay must be at least 0");
        this.startDelay = startDelay;
    }

    /**
     * Sets the alpha, which should be used by the animation.
     *
     * @param alpha
     *         The alpha, which should be set, as a {@link Float} value. The alpha must be at least
     *         0 and at maximum 1
     */
    protected final void setAlpha(final float alpha) {
        ensureAtLeast(alpha, 0, "The alpha must be at least 0");
        ensureAtMaximum(alpha, 1, "The alpha must be at maximum 1");
        this.alpha = alpha;
    }

    /**
     * Creates a new animation, which can be used to show or hide a dialog.
     *
     * @param context
     *         The context, which should be used by the animation, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    protected DialogAnimation(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.duration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        this.startDelay = 0;
        this.alpha = null;
    }

    /**
     * Returns the interpolator, which is used by the animation.
     *
     * @return The interpolator, which is used by the animation, as an instance of the type {@link
     * Interpolator}. The interpolator may not be null
     */
    @NonNull
    public final Interpolator getInterpolator() {
        return interpolator;
    }

    /**
     * Returns the duration of the animation.
     *
     * @return The duration of the animation in milliseconds as a {@link Long} value
     */
    public final long getDuration() {
        return duration;
    }

    /**
     * Returns the delay until the animation is started.
     *
     * @return The delay until the animation is started in milliseconds as a {@link Long} value
     */
    public final long getStartDelay() {
        return startDelay;
    }

    /**
     * Returns the alpha, which is used by the animation.
     *
     * @return The alpha, which is used by the animation as a {@link Float} value, or null, if no
     * alpha is used
     */
    public final Float getAlpha() {
        return alpha;
    }

}