package de.mrapp.android.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OnMultiChoiceClickListenerWrapper extends AbstractListenerWrapper
		implements OnItemClickListener {

	private OnMultiChoiceClickListener wrappedListener;

	public OnMultiChoiceClickListenerWrapper(
			OnMultiChoiceClickListener listener, AlertDialog dialog, int button) {
		super(dialog, button);
		this.wrappedListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (wrappedListener != null) {
			ListView listView = (ListView) parent;
			wrappedListener.onClick(getDialog(), position,
					listView.isItemChecked(position));
		}

		attemptCloseDialog();
	}

}