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
package de.mrapp.android.dialog.model;

import android.support.annotation.Nullable;

import de.mrapp.android.dialog.animation.DialogAnimation;

/**
 * Defines the interface a decorator, which allows to modify the view hierarchy of an animateable
 * dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices, must implement.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public interface AnimateableDialogDecorator extends Dialog {

    /**
     * Returns the animation, which is used to show the dialog.
     *
     * @return The animation, which is used to show the dialog, as an instance of the class {@link
     * DialogAnimation} or null, if no animation is used
     */
    DialogAnimation getShowAnimation();

    /**
     * Sets the animation, which should be used to show the dialog.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     */
    void setShowAnimation(@Nullable DialogAnimation animation);

    /**
     * Returns the animation, which is used to dismiss the dialog.
     *
     * @return The animation, which is used to dismiss the dialog, as an instance of the class
     * {@link DialogAnimation} or null, if no animation is used
     */
    DialogAnimation getDismissAnimation();

    /**
     * Sets the animation, which should be used to dismiss the dialog.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     */
    void setDismissAnimation(@Nullable DialogAnimation animation);

    /**
     * Returns the animation, which is used to cancel the dialog.
     *
     * @return The animation, which is used to dismiss the dialog, as an instance of the class
     * {@link DialogAnimation} or null, if no animation is used
     */
    DialogAnimation getCancelAnimation();

    /**
     * Sets the animation, which should be used to cancel the dialog.
     *
     * @param animation
     *         The animation, which should be set, as an instance of the class {@link
     *         DialogAnimation} or null, if no animation should be used
     */
    void setCancelAnimation(@Nullable DialogAnimation animation);

}