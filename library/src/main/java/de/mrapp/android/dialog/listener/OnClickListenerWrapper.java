/*
 * AndroidMaterialDialog Copyright 2014 - 2016 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.dialog.listener;

import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.dialog.MaterialDialog.Validator;

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
     *         The dialog, the listener should belong to, as an instance of the class {@link
     *         MaterialDialog}. The dialog may not be null
     * @param buttonType
     *         The type of the button or list item, the listener belongs to, as an {@link Integer}
     *         value
     */
    public OnClickListenerWrapper(@Nullable final OnClickListener listener, final boolean validate,
                                  @NonNull final MaterialDialog dialog, final int buttonType) {
        super(dialog, buttonType);
        this.wrappedListener = listener;
        this.validate = validate;
    }

    @Override
    public final void onClick(final View v) {
        if (wrappedListener != null) {
            wrappedListener.onClick(getDialog(), getButtonType());
        }

        if (validate) {
            for (Validator validator : getDialog().getValidators()) {
                if (!validator.validate()) {
                    return;
                }
            }
        }

        attemptCloseDialog();
    }

}