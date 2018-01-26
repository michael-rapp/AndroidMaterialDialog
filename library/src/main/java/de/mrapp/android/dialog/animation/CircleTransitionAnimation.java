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

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * An animation, which can be used to change backgrounds by animating the new background as a
 * circle, which grows from a specific position.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public class CircleTransitionAnimation extends BackgroundAnimation {

    /**
     * A builder, which allows to create animations, which can be used to change backgrounds by
     * animating the new background as a circle, which grows from a specific position.
     */
    public static class Builder
            extends AbstractBackgroundAnimationBuilder<CircleTransitionAnimation, Builder> {

        /**
         * Creates a new builder, which allows to create animations, which can be used to change
         * backgrounds by animating the new background as a circle, which grows from a specific
         * position.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Sets the horizontal position, which should be used by the animation, which is created by
         * the builder.
         *
         * @param x
         *         The horizontal position, which should be set, in pixels as an {@link Integer}
         *         value. The horizontal position must be at least 0
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setX(final int x) {
            getProduct().setX(x);
            return self();
        }

        /**
         * Sets the vertical position, which should be used by the animation.
         *
         * @param y
         *         The vertical position, which should be set, in pixels as an {@link Integer}
         *         value. The vertical position must be at least 0
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setY(final int y) {
            getProduct().setY(y);
            return self();
        }

        /**
         * Sets the radius, which should be used by the animation.
         *
         * @param radius
         *         The radius, which should be set, in pixels as an {@link Integer} value. The
         *         radius must be at least 0
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setRadius(final int radius) {
            getProduct().setRadius(radius);
            return self();
        }

        @NonNull
        @Override
        protected final CircleTransitionAnimation onCreateProduct() {
            return new CircleTransitionAnimation(getContext());
        }

    }

    /**
     * The horizontal position, which is used by the animation.
     */
    private Integer x;

    /**
     * The vertical position, which is used by the animation.
     */
    private Integer y;

    /**
     * The radius, which is used by the animation.
     */
    private int radius;

    /**
     * Sets the horizontal position, which should be used by the animation.
     *
     * @param x
     *         The horizontal position, which should be set, in pixels as an {@link Integer} value.
     *         The horizontal position must be at least 0
     */
    protected final void setX(final int x) {
        ensureAtLeast(x, 0, "The horizontal position must be at least 0");
        this.x = x;
    }

    /**
     * Sets the vertical position, which should be used by the animation.
     *
     * @param y
     *         The vertical position, which should be set, in pixels as an {@link Integer} value.
     *         The vertical position must be at least 0
     */
    protected final void setY(final int y) {
        ensureAtLeast(y, 0, "The vertical position must be at least 0");
        this.y = y;
    }

    /**
     * Sets the radius, which should be used by the animation.
     *
     * @param radius
     *         The radius, which should be set, in pixels as an {@link Integer} value. The radius
     *         must be at least 0
     */
    protected final void setRadius(final int radius) {
        ensureAtLeast(radius, 0, "The radius must be at least 0");
        this.radius = radius;
    }

    /**
     * Creates a new animation, which can be used to change backgrounds by animating the new
     * background as a circle, which grows from a specific position.
     *
     * @param context
     *         The context, which should be used by the animation, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    protected CircleTransitionAnimation(@NonNull final Context context) {
        super(context);
        this.radius = 0;
        this.x = null;
        this.y = null;
    }

    /**
     * Returns the horizontal position, which is used by the animation.
     *
     * @return The horizontal position, which is used by the animation, in pixels as an {@link
     * Integer} value or null, if the animation should start at the center
     */
    public final Integer getX() {
        return x;
    }

    /**
     * Returns the vertical position, which is used by the animation.
     *
     * @return The vertical position, which is used by the animation, in pixels as an {@link
     * Integer} value or null, if the animation should start at the center
     */
    public final Integer getY() {
        return y;
    }

    /**
     * Returns the radius, which is used by the animation.
     *
     * @return The radius, which is used by the animation, in pixels as an {@link Integer} value
     */
    public final int getRadius() {
        return radius;
    }

}