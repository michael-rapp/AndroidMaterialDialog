package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;

public abstract class AbstractListenerWrapper {

	private AlertDialog dialog;

	private int button;

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

	protected final AlertDialog getDialog() {
		return dialog;
	}

	protected final int getButton() {
		return button;
	}

	public AbstractListenerWrapper(final AlertDialog dialog, final int button) {
		this.dialog = dialog;
		this.button = button;
	}

}