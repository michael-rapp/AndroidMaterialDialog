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
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A wrapper, which implements the interface {@link OnItemClickListener} in
 * order to delegate the method calls to encapsulated listener of the type
 * {@link OnMultiChoiceClickListener}.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class OnMultiChoiceClickListenerWrapper extends AbstractListenerWrapper
		implements OnItemClickListener {

	/**
	 * The listener, which is encapsulated by the wrapper.
	 */
	private OnMultiChoiceClickListener wrappedListener;

	/**
	 * Creates a new wrapper, which implements the interface
	 * {@link OnItemClickListener} in order to delegate the method calls to
	 * encapsulated listener of the type {@link OnMultiChoiceClickListener}.
	 * 
	 * @param listener
	 *            The listener, which should be encapsulated by the wrapper, as
	 *            an instance of the type {@link OnMultiChoiceClickListener}
	 * @param dialog
	 *            The dialog, the listener should belong to, as an instance of
	 *            the class {@link AlertDialog}
	 * @param buttonType
	 *            The type of the button or list item, the listener belongs to,
	 *            as an {@link Integer} value
	 */

	public OnMultiChoiceClickListenerWrapper(
			final OnMultiChoiceClickListener listener,
			final AlertDialog dialog, final int buttonType) {
		super(dialog, buttonType);
		this.wrappedListener = listener;
	}

	@Override
	public final void onItemClick(final AdapterView<?> parent, final View view,
			final int position, final long id) {
		if (wrappedListener != null) {
			ListView listView = (ListView) parent;
			wrappedListener.onClick(getDialog(), position,
					listView.isItemChecked(position));
		}

		attemptCloseDialog();
	}

}