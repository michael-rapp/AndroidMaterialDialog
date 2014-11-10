package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

public abstract class AbstractListenerWrapper {

	private OnClickListener wrappedListener;

	private AlertDialog dialog;

	private int button;

	protected final void attemptOnClick() {
		if (wrappedListener != null) {
			wrappedListener.onClick(dialog, button);
		}
	}

	protected final void attemptCloseDialog() {
		switch (button) {
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

	public AbstractListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog, final int button) {
		this.wrappedListener = listener;
		this.dialog = dialog;
		this.button = button;
	}

}