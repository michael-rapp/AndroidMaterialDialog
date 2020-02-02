/*
 * Copyright 2014 - 2020 Michael Rapp
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

import android.content.DialogInterface.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import de.mrapp.android.dialog.DialogValidator;
import de.mrapp.android.dialog.model.ValidateableDialog;

/**
 * A wrapper, which implements the interface {@link android.view.View.OnClickListener} in order to
 * delegate the method calls to encapsulated listener of the type {@link OnClickListener}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class OnClickListenerWrapper extends AbstractListenerWrapper
        implements android.view.View.OnClickListener {

    /**
     * The listener, which is encapsulated by the wrapper.
     */
    private final OnClickListener wrappedListener;

    /**
     * True, if the dialog, the listener belongs to, is validated when the listener is called, false
     * otherwise.
     */
    private final boolean validate;

    /**
     * Creates a new wrapper, which implements the interface {@link android.view.View.OnClickListener}
     * in order to delegate the method calls to encapsulated listener of the type {@link
     * OnClickListener}.
     *
     * @param listener
     *         The listener, which should be encapsulated by the wrapper, as an instance of the type
     *         {@link OnClickListener} or null, if no listener should be encapsulated
     * @param validate
     *         True, if the dialog, the listener belongs to, should be validated when the listener
     *         is called, false otherwise
     * @param dialog
     *         The dialog, the listener should belong to, as an instance of the type {@link
     *         ValidateableDialog}. The dialog may not be null
     * @param buttonType
     *         The type of the button or list item, the listener belongs to, as an {@link Integer}
     *         value
     */
    public OnClickListenerWrapper(@Nullable final OnClickListener listener, final boolean validate,
                                  @NonNull final ValidateableDialog dialog, final int buttonType) {
        super(dialog, buttonType);
        this.wrappedListener = listener;
        this.validate = validate;
    }

    @Override
    public final void onClick(final View v) {
        if (validate) {
            for (DialogValidator validator : getDialog().getDialogValidators()) {
                if (!validator.validate(getDialog())) {
                    return;
                }
            }
        }

        if (wrappedListener != null) {
            wrappedListener.onClick(getDialog(), getButtonType());
        }

        attemptCloseDialog();
    }

}