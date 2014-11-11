/*
 * AndroidMaterialDialog Copyright 2014 Michael Rapp
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

import android.app.AlertDialog;

/**
 * An abstract base class for all wrappers, which implement a specific listener
 * interface of a dialog's buttonType or list item, in order to delegate the
 * method calls to an encapsulated listener of another type. Depending on the
 * type of the buttonType or list item which causes the listener call, the
 * appropriate dialog may be closed by the wrapper.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public abstract class AbstractListenerWrapper {

	/**
	 * The alert dialog, the listener belongs to.
	 */
	private final AlertDialog dialog;

	/**
	 * The type of the buttonType or list view, the listener belongs to.
	 */
	private final int buttonType;

	/**
	 * Attempts to close the dialog depending on the type of the buttonType or
	 * list view, the listener belongs to.
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
	 * @return The dialog, the listener belongs to, as an instance of the class
	 *         {@link AlertDialog}
	 */
	protected final AlertDialog getDialog() {
		return dialog;
	}

	/**
	 * Returns the type of the button or list item, the listener belongs to.
	 * 
	 * @return The type of the button or list item, the listener belongs to, as
	 *         an {@link Integer} value
	 */
	protected final int getButtonType() {
		return buttonType;
	}

	/**
	 * Creates a new wrapper, which implements a specific listener interface of
	 * a dialog's buttonType or list item, in order to delegate the method calls
	 * to an encapsulated listener of another type.
	 * 
	 * @param dialog
	 *            The dialog, the listener should belong to, as an instance of
	 *            the class {@link AlertDialog}
	 * @param buttonType
	 *            The type of the button or list item, the listener belongs to,
	 *            as an {@link Integer} value
	 */
	public AbstractListenerWrapper(final AlertDialog dialog,
			final int buttonType) {
		this.dialog = dialog;
		this.buttonType = buttonType;
	}

}