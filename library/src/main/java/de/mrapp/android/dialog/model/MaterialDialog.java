/*
 * Copyright 2014 - 2017 Michael Rapp
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

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.mrapp.android.dialog.ScrollableArea;
import de.mrapp.android.dialog.ScrollableArea.Area;

/**
 * Defines the interface, a dialog, which is designed according to Android 5's Material Design
 * guidelines even on pre-Lollipop devices, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface MaterialDialog extends MaterialDialogDecorator, DialogInterface {

    /**
     * Sets, whether the dialog should be cancelable, or not.
     *
     * @param cancelable
     *         True, if the dialog, which is created by the builder, should be cancelable, false
     *         otherwise
     */
    void setCancelable(boolean cancelable);

    /**
     * Sets the listener, which should be notified, when the dialog has been shown.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnShowListener}, or null, if no listener should be set
     */
    void setOnShowListener(@Nullable DialogInterface.OnShowListener listener);

    /**
     * Sets the listener, which should be notified, when the dialog is canceled.
     *
     * Even in a cancelable dialog, the dialog may be dismissed for reasons other than being
     * canceled or one of the supplied choices being selected. If you are interested in listening
     * for all cases where the dialog is dismissed and not just when it is canceled, see {@link
     * #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
     * setOnDismissListener}.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnCancelListener}, or null, if no listener should be set
     * @see #setCancelable(boolean)
     * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
     */
    void setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener);

    /**
     * Sets the listener, which should be notified, when the dialog is dismissed for any reason.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         DialogInterface.OnDismissListener}, or null, if no listener should be set
     */
    void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener);

    /**
     * Returns the scrollable area of the dialog.
     *
     * @return The scrollable area of the dialog as an instance of the class {@link ScrollableArea}.
     * The scrollable area may not be null
     */
    @NonNull
    ScrollableArea getScrollableArea();

    /**
     * Sets the area of the dialog, which should be scrollable.
     *
     * @param area
     *         The area, which should be set, as a value of the enum {@link Area} or null, if no
     *         area should be scrollable
     */
    void setScrollableArea(@Nullable Area area);

    /**
     * Sets the areas of the dialog, which should be scrollable.
     *
     * @param top
     *         The top-most area, which should be scrollable, as a value of the enum {@link Area} or
     *         null, if no area should be scrollable
     * @param bottom
     *         The bottom-most area, which should be scrollable, as a value of the enum {@link
     *         Area}. If the top-most area is null, the bottom-most are must be null as well. The
     *         index of the bottom-most area must be at least the index of the top-most area
     */
    void setScrollableArea(@Nullable Area top, @Nullable Area bottom);

}