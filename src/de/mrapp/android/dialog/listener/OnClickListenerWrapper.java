package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class OnClickListenerWrapper extends AbstractListenerWrapper implements
		android.view.View.OnClickListener {

	private OnClickListener wrappedListener;

	public OnClickListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog, final int button) {
		super(dialog, button);
		this.wrappedListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (wrappedListener != null) {
			wrappedListener.onClick(getDialog(), getButton());
		}

		attemptCloseDialog();
	}

}