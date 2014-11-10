package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class OnClickListenerWrapper implements
		android.view.View.OnClickListener {

	private OnClickListener wrappedListener;

	private AlertDialog dialog;

	private int button;

	private void closeDialog() {
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

	public OnClickListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog, final int button) {
		this.wrappedListener = listener;
		this.dialog = dialog;
		this.button = button;
	}

	@Override
	public void onClick(View v) {
		if (wrappedListener != null) {
			wrappedListener.onClick(dialog, button);
		}

		closeDialog();
	}

}