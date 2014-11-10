package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickListenerWrapper implements OnItemClickListener {

	private OnClickListener wrappedListener;

	private AlertDialog dialog;

	public OnItemClickListenerWrapper(final OnClickListener listener,
			final AlertDialog dialog) {
		this.wrappedListener = listener;
		this.dialog = dialog;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		wrappedListener.onClick(dialog, 0);
	}

};