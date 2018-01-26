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

import android.content.Context;

/**
 * Defines the interface, all dialogs must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface Dialog {

    /**
     * Contains all flags, which can be used to specify a dialog's gravity.
     */
    interface Gravity {

        /**
         * The flag, which specifies, that the dialog should be centered horizontally.
         */
        int CENTER_HORIZONTAL = 0x000001;

        /**
         * The flag, which specifies, that the dialog should be centered vertically
         */
        int CENTER_VERTICAL = 0x000010;

        /**
         * The flag, which specifies, that the dialog should be located at the top.
         */
        int TOP = 0x000100;

        /**
         * The flag, which specifies, that the dialog should be located at the bottom.
         */
        int BOTTOM = 0x001000;

        /**
         * The flag, which specifies, that the dialog should be located at the left.
         */
        int LEFT = 0x010000;

        /**
         * The flag, which specifies, that the dialog should be located at the right.
         */
        int RIGHT = 0x100000;

        /**
         * The flag, which specifies, that the dialog should be centered both horizontally and
         * vertically.
         */
        int CENTER = CENTER_HORIZONTAL | CENTER_VERTICAL;

    }

    /**
     * Special value for the height or width requested by a dialog. MATCH_PARENT means that the
     * dialog wants to be as big as the window, minus the dialog's margin, if any.
     */
    int MATCH_PARENT = -1;

    /**
     * Special value for the height or width requested by a dialog. WRAP_CONTENT means that the
     * dialog wants to be just large enough to fit its own internal content, taking its own padding
     * into account.
     */
    int WRAP_CONTENT = -2;

    /**
     * Returns the context, which is used by the dialog.
     *
     * @return The context, which is used by the dialog, as an instance of the class {@link Context}
     */
    Context getContext();

}