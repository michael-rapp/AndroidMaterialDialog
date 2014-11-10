package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class OnClickListenerWrapper extends AbstractListenerWrapper implements
		android.view.View.OnClickListener {

	public OnClickListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog, final int button) {
		super(listener, dialog, button);
	}

	@Override
	public void onClick(View v) {
		attemptOnClick();
		attemptCloseDialog();
	}

}