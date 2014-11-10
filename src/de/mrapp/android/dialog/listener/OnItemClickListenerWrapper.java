package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickListenerWrapper extends AbstractListenerWrapper
		implements OnItemClickListener {

	private OnClickListener wrappedListener;

	public OnItemClickListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog, final int button) {
		super(dialog, button);
		this.wrappedListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (wrappedListener != null) {
			wrappedListener.onClick(getDialog(), position);
		}

		attemptCloseDialog();
	}

};