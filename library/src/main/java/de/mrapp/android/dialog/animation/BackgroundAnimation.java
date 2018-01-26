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

/**
 * An abstract base class for all animations, which can be used to change backgrounds.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class BackgroundAnimation extends DrawableAnimation {

    /**
     * An abstract base class for all builders, which allow to create animations, which can be used
     * to change backgrounds.
     *
     * @param <AnimationType>
     *         The type of the animations, which are created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    protected static abstract class AbstractBackgroundAnimationBuilder<AnimationType extends BackgroundAnimation, BuilderType extends AbstractBackgroundAnimationBuilder<AnimationType, ?>>
            extends AbstractDrawableAnimationBuilder<AnimationType, BuilderType> {

        /**
         * Creates a new builder, which allows to create animations, which can be used to change
         * backgrounds.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        protected AbstractBackgroundAnimationBuilder(@NonNull final Context context) {
            super(context);
        }

    }

    /**
     * Creates a new animation, which can be used to change drawables.
     *
     * @param context
     *         The context, which should be used by the animation, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    protected BackgroundAnimation(@NonNull final Context context) {
        super(context);
    }

}