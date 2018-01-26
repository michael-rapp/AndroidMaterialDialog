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

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import de.mrapp.android.dialog.ProgressDialog.ProgressBarPosition;

/**
 * Defines the interface, a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
 * allows to indicate a running progress.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface ProgressDialogDecorator extends Dialog {

    /**
     * Returns the color of the dialog's progress bar.
     *
     * @return The color of the dialog's progress bar, as an {@link Integer} value
     */
    int getProgressBarColor();

    /**
     * Sets the color of the dialog's progress bar.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setProgressBarColor(@ColorInt int color);

    /**
     * Returns the size of the dialog's progress bar.
     *
     * @return The size of the dialog's progress bar in pixels as an {@link Integer} value
     */
    int getProgressBarSize();

    /**
     * Sets the size of the dialog's progress bar.
     *
     * @param size
     *         The size, which should be set, in pixels as an {@link Integer} value. The size must
     *         be at least 0
     */
    void setProgressBarSize(int size);

    /**
     * Returns the thickness of the dialog's progress bar.
     *
     * @return The thickness of the dialog's progress bar in pixels as an {@link Integer} value
     */
    int getProgressBarThickness();

    /**
     * Sets the thickness of the dialog's progress bar.
     *
     * @param thickness
     *         The thickness, which should be set, in pixels as an {@link Integer} value. The
     *         thickness must be at least 1
     */
    void setProgressBarThickness(int thickness);

    /**
     * Returns the position of the dialog's progress bar.
     *
     * @return The position of the dialog's progress bar as a value of the enum {@link
     * ProgressBarPosition}. The position may either be <code>LEFT</code>, <code>TOP</code>,
     * <code>RIGHT</code> or <code>BOTTOM</code>
     */
    ProgressBarPosition getProgressBarPosition();

    /**
     * Sets the position of the dialog's progress bar.
     *
     * @param position
     *         The position, which should be set, as a value of the enum {@link
     *         ProgressBarPosition}. The position may either be <code>LEFT</code>, <code>TOP</code>,
     *         <code>RIGHT</code> or <code>BOTTOM</code>
     */
    void setProgressBarPosition(@NonNull ProgressBarPosition position);

}