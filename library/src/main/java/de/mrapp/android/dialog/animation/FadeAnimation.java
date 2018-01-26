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

/**
 * An animation, which can be used to show or hide a dialog by animating its alpha property.
 *
 * @author Michael Rapp
 * @since 4.1.0
 */
public class FadeAnimation extends DialogAnimation {

    /**
     * A builder, which allows to create animations, which can be used to show or hide a dialog by
     * animating its position and size.
     */
    public static class Builder extends AbstractDialogAnimationBuilder<FadeAnimation, Builder> {

        /**
         * Creates a new builder, which allows to create animations, which can be used to show or
         * hide a dialog by animating its position and size.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        @NonNull
        @Override
        protected final FadeAnimation onCreateProduct() {
            return new FadeAnimation(getContext());
        }

    }

    /**
     * Creates a new animation, which allows to show or hide a dialog by animating its alpha
     * property.
     *
     * @param context
     *         The context, which should be used by the animation, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    protected FadeAnimation(@NonNull final Context context) {
        super(context);
    }

}