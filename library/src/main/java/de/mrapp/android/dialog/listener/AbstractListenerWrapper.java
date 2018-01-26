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
package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.support.annotation.NonNull;

import de.mrapp.android.dialog.model.ValidateableDialog;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all wrappers, which implement a specific listener interface of a
 * dialog's buttonType or list item, in order to delegate the method calls to an encapsulated
 * listener of another type. Depending on the type of the buttonType or list item which causes the
 * listener call, the appropriate dialog may be closed by the wrapper.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractListenerWrapper {

    /**
     * The dialog, the listener belongs to.
     */
    private final ValidateableDialog dialog;

    /**
     * The type of the buttonType or list view, the listener belongs to.
     */
    private final int buttonType;

    /**
     * Attempts to close the dialog depending on the type of the buttonType or list view, the
     * listener belongs to.
     */
    protected final void attemptCloseDialog() {
        switch (buttonType) {
            case AlertDialog.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
            case AlertDialog.BUTTON_NEUTRAL:
                dialog.cancel();
                break;
            case AlertDialog.BUTTON_POSITIVE:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * Returns the dialog, the listener belongs to.
     *
     * @return The dialog, the listener belongs to, as an instance of the type {@link
     * ValidateableDialog}
     */
    protected final ValidateableDialog getDialog() {
        return dialog;
    }

    /**
     * Returns the type of the button or list item, the listener belongs to.
     *
     * @return The type of the button or list item, the listener belongs to, as an {@link Integer}
     * value
     */
    protected final int getButtonType() {
        return buttonType;
    }

    /**
     * Creates a new wrapper, which implements a specific listener interface of a dialog's
     * buttonType or list item, in order to delegate the method calls to an encapsulated listener of
     * another type.
     *
     * @param dialog
     *         The dialog, the listener should belong to, as an instance of the type {@link
     *         ValidateableDialog}. The dialog may not be null
     * @param buttonType
     *         The type of the button or list item, the listener belongs to, as an {@link Integer}
     *         value
     */
    public AbstractListenerWrapper(@NonNull final ValidateableDialog dialog, final int buttonType) {
        ensureNotNull(dialog, "The dialog may not be null");
        this.dialog = dialog;
        this.buttonType = buttonType;
    }

}