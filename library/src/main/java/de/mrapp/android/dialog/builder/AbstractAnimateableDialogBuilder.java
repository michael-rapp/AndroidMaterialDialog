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
package de.mrapp.android.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.model.AnimateableDialog;

/**
 * An abstract base class for all builders, which allow to create and show animateable dialogs,
 * which are designed according to Android 5's Material Design guidelines even on pre-Lollipop
 * devices.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 4.0.0
 */
public abstract class AbstractAnimateableDialogBuilder<DialogType extends AnimateableDialog, BuilderType extends AbstractAnimateableDialogBuilder<DialogType, ?>>
        extends AbstractHeaderDialogBuilder<DialogType, BuilderType> {

    /**
     * Creates a new builder, which allows to create animateable dialogs, which are designed
     * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractAnimateableDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new builder, which allows to create animateable dialogs, which are designed
     * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractAnimateableDialogBuilder(@NonNull final Context context,
                                            @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Sets the animation, which should be used to show the dialog, which is created by the
     * builder.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setShowAnimation(@Nullable final DialogAnimation animation) {
        getProduct().setShowAnimation(animation);
        return self();
    }

    /**
     * Sets the animation, which should be used to dismiss the dialog, which is created by the
     * builder.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setDismissAnimation(@Nullable final DialogAnimation animation) {
        getProduct().setDismissAnimation(animation);
        return self();
    }

    /**
     * Sets the animation, which should be used to cancel the dialog, which is created by the
     * builder.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setCancelAnimation(@Nullable final DialogAnimation animation) {
        getProduct().setCancelAnimation(animation);
        return self();
    }

}