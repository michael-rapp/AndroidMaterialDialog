/*
 * AndroidMaterialDialog Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.dialog.listener;

import java.util.Set;

import de.mrapp.android.dialog.MaterialDialogBuilder.Validator;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

/**
 * A wrapper, which implements the interface
 * {@link android.view.View.OnClickListener} in order to delegate the method
 * calls to encapsulated listener of the type {@link OnClickListener}.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class OnClickListenerWrapper extends AbstractListenerWrapper implements
		android.view.View.OnClickListener {

	/**
	 * The listener, which is encapsulated by the wrapper.
	 */
	private final OnClickListener wrappedListener;

	/**
	 * A set, which contains the validators, which should be executed when the
	 * listener is called.
	 */
	private final Set<Validator> validators;

	/**
	 * Creates a new wrapper, which implements the interface
	 * {@link android.view.View.OnClickListener} in order to delegate the method
	 * calls to encapsulated listener of the type {@link OnClickListener}.
	 * 
	 * @param listener
	 *            The listener, which should be encapsulated by the wrapper, as
	 *            an instance of the type {@link OnClickListener}
	 * @param validators
	 *            A set, which contains the validators, which should be executed
	 *            when the listener is called, as an instance of the type
	 *            {@link Set} or null, if no validators should be executed
	 * @param dialog
	 *            The dialog, the listener should belong to, as an instance of
	 *            the class {@link AlertDialog}
	 * @param buttonType
	 *            The type of the button or list item, the listener belongs to,
	 *            as an {@link Integer} value
	 */
	public OnClickListenerWrapper(final OnClickListener listener,
			final Set<Validator> validators, final AlertDialog dialog,
			final int buttonType) {
		super(dialog, buttonType);
		this.wrappedListener = listener;
		this.validators = validators;
	}

	@Override
	public final void onClick(final View v) {
		if (wrappedListener != null) {
			wrappedListener.onClick(getDialog(), getButtonType());
		}

		if (validators != null) {
			for (Validator validator : validators) {
				if (!validator.validate()) {
					return;
				}
			}
		}

		attemptCloseDialog();
	}

}