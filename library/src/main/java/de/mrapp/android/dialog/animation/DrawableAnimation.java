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
import android.support.annotation.Nullable;

import de.mrapp.android.dialog.builder.AbstractBuilder;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all animations, which can be used to change drawables.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class DrawableAnimation {

    /**
     * Defines the interface, a class, which should be notified about a drawable animation's
     * progress, must implement.
     */
    public interface AnimationListener {

        /**
         * The method, which is invoked, when the animation is started.
         */
        void onAnimationStart();

        /**
         * The method, which is invoked, when the animation is finished.
         */
        void onAnimationEnd();

    }

    /**
     * An abstract base class for all builders, which allow to create animations, which can be used
     * to change drawables.
     *
     * @param <AnimationType>
     *         The type of the animations, which are created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    protected static abstract class AbstractDrawableAnimationBuilder<AnimationType extends DrawableAnimation, BuilderType extends AbstractDrawableAnimationBuilder<AnimationType, ?>>
            extends AbstractBuilder<AnimationType, BuilderType> {

        /**
         * Creates a new builder, which allows to create animations, which can be used to change
         * drawables.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        protected AbstractDrawableAnimationBuilder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Sets the duration of the animation, which is created by the builder.
         *
         * @param duration
         *         The duration, which should be set, in milliseconds as an {@link Integer} value.
         *         The duration must be at least 1
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setDuration(final int duration) {
            getProduct().setDuration(duration);
            return self();
        }

        /**
         * Sets the listener, which should be notified about the progress of the animation, which is
         * created by the builder.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         AnimationListener} or null, if no listener should be notified
         * @return The builder, this method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public BuilderType setListener(@Nullable final AnimationListener listener) {
            getProduct().setListener(listener);
            return self();
        }

    }

    /**
     * The duration of the animation in milliseconds.
     */
    private int duration;

    /**
     * The listener, which is notified about the animation's progress.
     */
    private AnimationListener listener;

    /**
     * Sets the duration of the animation.
     *
     * @param duration
     *         The duration, which should be set, in milliseconds as an {@link Integer} value. The
     *         duration must be at least 1
     */
    protected final void setDuration(final int duration) {
        ensureAtLeast(duration, 1, "The duration must be at least 1");
        this.duration = duration;
    }

    /**
     * Sets the listener, which should be notified about the animation's progress.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         AnimationListener} or null, if no listener should be notified
     */
    protected final void setListener(@Nullable final AnimationListener listener) {
        this.listener = listener;
    }

    /**
     * Creates a new animation, which can be used to change drawables.
     *
     * @param context
     *         The context, which should be used by the animation, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    protected DrawableAnimation(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.duration = context.getResources().getInteger(android.R.integer.config_longAnimTime);
        this.listener = null;
    }

    /**
     * Returns the duration of the animation.
     *
     * @return The duration of the animation in milliseconds as an {@link Integer} value
     */
    public final int getDuration() {
        return duration;
    }

    /**
     * Returns the listener, which is notified about the animation's progress.
     *
     * @return The listener, which is notified about the animation's progress, as an instance of the
     * type {@link AnimationListener} or null, if no listener is notified
     */
    @Nullable
    public final AnimationListener getListener() {
        return listener;
    }

}