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
package de.mrapp.android.dialog.drawable;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.mrapp.android.dialog.animation.DrawableAnimation.AnimationListener;

/**
 * An abstract base class for all layer drawables, which allow to translate between the first and
 * second layer in an animated manner.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class AbstractTransitionDrawable extends LayerDrawable {

    /**
     * The state when a transition is about to start.
     */
    private static final int TRANSITION_STARTING = 0;

    /**
     * The state when a transition is in progress.
     */
    private static final int TRANSITION_RUNNING = 1;

    /**
     * The state when no transition is running.
     */
    private static final int TRANSITION_NONE = 2;

    /**
     * The listener, which is notified about the transition's progress.
     */
    private AnimationListener listener;

    /**
     * The duration, which has been used by the last transition.
     */
    private int duration;

    /**
     * The time, the last transition has been started.
     */
    private long startTime;

    /**
     * The current state of the drawable.
     */
    private int state;

    /**
     * Notifies the listener, which is notified about the transition's progress, that the transition
     * is started.
     */
    private void notifyOnAnimationStart() {
        if (getListener() != null) {
            getListener().onAnimationStart();
        }
    }

    /**
     * Notifies the listener, which is notified about the transition's progress, that the transition
     * is started.
     */
    private void notifyOnAnimationEnd() {
        if (getListener() != null) {
            getListener().onAnimationEnd();
        }
    }

    /**
     * The method, which is invoked on subclasses when the transition is about to start.
     */
    protected abstract void onStartTransition();

    /**
     * The method, which is invoked on subclasses in order to draw the drawable during a transition
     * is in progress.
     *
     * @param interpolatedTime
     *         The interpolated time as a {@link Float} value. This is a value between 0.0 and 1.0
     * @param canvas
     *         The canvas, which should be used for drawing, as an instance of the class {@link
     *         Canvas}. The canvas may not be null
     */
    protected abstract void onDraw(final float interpolatedTime, @NonNull final Canvas canvas);

    /**
     * The method, which is invoked on subclasses when the transition is about to end.
     */
    protected abstract void onEndTransition();

    /**
     * Creates a new layer drawable, which allows to translate between the first and second layer in
     * an animated manner.
     *
     * @param layers
     *         The layers of this drawable as a {@link Drawable} array. The array may not be null
     */
    public AbstractTransitionDrawable(@NonNull final Drawable[] layers) {
        super(layers);
        this.listener = null;
        this.state = TRANSITION_NONE;
    }

    /**
     * Returns the listener, which is notified about the transition's progress.
     *
     * @return The listener, which is notified about the transition's progress, as an instance of
     * the type {@link AnimationListener} or null, if no listener is notified
     */
    @Nullable
    public final AnimationListener getListener() {
        return listener;
    }

    /**
     * Sets the listener, which should be notified about the transition's progress.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         AnimationListener} or null, if no listener should be notified
     */
    public final void setListener(@Nullable final AnimationListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the transition, which shows the second layer in front of the first layer.
     *
     * @param duration
     *         The duration of the transition in milliseconds as an {@link Integer} value
     */
    public final void startTransition(final int duration) {
        this.duration = duration;
        this.state = TRANSITION_STARTING;
        onStartTransition();
        notifyOnAnimationStart();
        invalidateSelf();
    }

    @Override
    public final void draw(final Canvas canvas) {
        boolean done = true;
        float interpolatedTime = 0;

        if (state == TRANSITION_STARTING) {
            startTime = SystemClock.uptimeMillis();
            done = false;
            state = TRANSITION_RUNNING;
        } else if (state == TRANSITION_RUNNING) {
            if (startTime >= 0) {
                interpolatedTime = (float) (SystemClock.uptimeMillis() - startTime) / duration;
                done = interpolatedTime >= 1f;
                interpolatedTime = Math.min(interpolatedTime, 1f);
            }
        }

        if (done) {
            onEndTransition();
            Drawable first = getDrawable(0);
            Drawable second = getDrawable(1);

            if (state != TRANSITION_NONE) {
                second.draw(canvas);
            } else {
                first.draw(canvas);
            }

            notifyOnAnimationEnd();
            return;
        }

        onDraw(interpolatedTime, canvas);
        invalidateSelf();
    }

}