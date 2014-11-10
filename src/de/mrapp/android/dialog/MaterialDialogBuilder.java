package de.mrapp.android.dialog;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;

public class MaterialDialogBuilder extends AlertDialog.Builder {

	private Context context;

	private ViewGroup contentViewGroup;

	private ViewGroup inputViewGroup;

	/**
	 * The text view, which is used to show the dialog's title.
	 */
	private TextView titleTextView;

	/**
	 * The text view, which is used to show the dialog's message.
	 */
	private TextView messageTextView;

	/**
	 * The list view, which is used to show the dialog's items.
	 */
	private ListView listView;

	private Button negativeButton;

	private Button neutralButton;

	private Button positiveButton;

	private CharSequence negativeButtonText;

	private CharSequence neutralButtonText;

	private CharSequence positiveButtonText;

	private OnClickListener negativeButtonListener;

	private OnClickListener neutralButtonListener;

	private OnClickListener positiveButtonListener;

	private ListAdapter listAdapter;

	private OnClickListener listViewClickListener;

	/**
	 * Inflates the dialog's layout.
	 */
	private void inflateLayout() {
		View view = View.inflate(context, R.layout.material_dialog, null);
		contentViewGroup = (ViewGroup) view.findViewById(android.R.id.content);
		inputViewGroup = (ViewGroup) view.findViewById(android.R.id.input);
		titleTextView = (TextView) view.findViewById(android.R.id.title);
		messageTextView = (TextView) view.findViewById(android.R.id.message);
		listView = (ListView) view.findViewById(android.R.id.list);
		negativeButton = (Button) view.findViewById(android.R.id.button1);
		neutralButton = (Button) view.findViewById(android.R.id.button2);
		positiveButton = (Button) view.findViewById(android.R.id.button3);
		setView(view);
	}

	private void applyTheme(int theme) {
		if (theme != 0) {
			obtainTitleColor(theme);
			obtainButtonTextColor(theme);
		}
	}

	private void obtainTitleColor(int theme) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				theme, new int[] { R.attr.colorPrimary });
		int color = typedArray.getColor(0, 0);

		if (color != 0) {
			titleTextView.setTextColor(color);
		} else {
			int resourceId = typedArray.getResourceId(0, 0);

			if (resourceId != 0) {
				titleTextView.setTextColor(context.getResources().getColor(
						resourceId));
			}
		}
	}

	private void obtainButtonTextColor(int theme) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				theme, new int[] { R.attr.colorAccent });
		int color = typedArray.getColor(0, 0);

		if (color != 0) {
			negativeButton.setTextColor(color);
			neutralButton.setTextColor(color);
			positiveButton.setTextColor(color);
		} else {
			int resourceId = typedArray.getResourceId(0, 0);

			if (resourceId != 0) {
				color = context.getResources().getColor(resourceId);
				negativeButton.setTextColor(color);
				neutralButton.setTextColor(color);
				positiveButton.setTextColor(color);
			}
		}
	}

	private void addNegativeButton(AlertDialog dialog) {
		if (!TextUtils.isEmpty(negativeButtonText)) {
			negativeButton.setText(negativeButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					negativeButtonListener, dialog, AlertDialog.BUTTON_NEGATIVE);
			negativeButton.setOnClickListener(onClickListener);
			negativeButton.setVisibility(View.VISIBLE);
		}
	}

	private void addNeutralButton(AlertDialog dialog) {
		if (!TextUtils.isEmpty(neutralButtonText)) {
			neutralButton.setText(neutralButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					neutralButtonListener, dialog, AlertDialog.BUTTON_NEUTRAL);
			neutralButton.setOnClickListener(onClickListener);
			neutralButton.setVisibility(View.VISIBLE);
		}
	}

	private void addPositiveButton(AlertDialog dialog) {
		if (!TextUtils.isEmpty(positiveButtonText)) {
			positiveButton.setText(positiveButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					positiveButtonListener, dialog, AlertDialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(onClickListener);
			positiveButton.setVisibility(View.VISIBLE);
		}
	}

	private void showListView(final AlertDialog dialog) {
		if (listAdapter != null && !listAdapter.isEmpty()) {
			listView.setAdapter(listAdapter);
			listView.setVisibility(View.VISIBLE);
			listView.setOnItemClickListener(new OnItemClickListenerWrapper(
					listViewClickListener, dialog, 0));

			if (!TextUtils.isEmpty(messageTextView.getText())) {
				LinearLayout.LayoutParams layoutParams = (LayoutParams) messageTextView
						.getLayoutParams();
				layoutParams.bottomMargin = context.getResources()
						.getDimensionPixelSize(R.dimen.dialog_content_spacing);
				messageTextView.setLayoutParams(layoutParams);
			}
		}
	}

	private void hideTitleIfEmpty() {
		if (TextUtils.isEmpty(titleTextView.getText())) {
			titleTextView.setVisibility(View.GONE);
		}
	}

	private void hideMessageIfEmpty() {
		if (TextUtils.isEmpty(messageTextView.getText())) {
			messageTextView.setVisibility(View.GONE);

			if (listAdapter == null || listAdapter.isEmpty()) {
				LinearLayout.LayoutParams layoutParams = (LayoutParams) titleTextView
						.getLayoutParams();
				layoutParams.bottomMargin = 0;
				titleTextView.setLayoutParams(layoutParams);
			}
		}
	}

	private void hideButtonBarIfEmpty() {
		if (TextUtils.isEmpty(negativeButtonText)
				&& TextUtils.isEmpty(neutralButtonText)
				&& TextUtils.isEmpty(positiveButtonText)) {
			int paddingLeft = context.getResources().getDimensionPixelSize(
					R.dimen.dialog_content_padding_left);
			int paddingTop = context.getResources().getDimensionPixelSize(
					R.dimen.dialog_content_padding_top);
			int paddingRight = context.getResources().getDimensionPixelSize(
					R.dimen.dialog_content_padding_right);
			contentViewGroup.setPadding(paddingLeft, paddingTop, paddingRight,
					paddingTop);
			inputViewGroup.setVisibility(View.GONE);
		}
	}

	public MaterialDialogBuilder(Context context) {
		this(context, 0);
	}

	public MaterialDialogBuilder(Context context, int theme) {
		super(context);
		this.context = context;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			inflateLayout();
			applyTheme(theme);
		}
	}

	@Override
	public MaterialDialogBuilder setTitle(CharSequence title) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			titleTextView.setText(title);
		} else {
			super.setTitle(title);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setTitle(int resourceId) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			titleTextView.setText(resourceId);
		} else {
			super.setTitle(resourceId);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setMessage(CharSequence message) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			messageTextView.setText(message);
		} else {
			super.setMessage(message);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setMessage(int resourceId) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			messageTextView.setText(resourceId);
		} else {
			super.setMessage(resourceId);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setIcon(Drawable icon) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null,
					null, null);
		} else {
			super.setIcon(icon);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setIcon(int resourceId) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			Drawable icon = context.getResources().getDrawable(resourceId);
			setIcon(icon);
		} else {
			super.setIcon(resourceId);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setNegativeButton(final CharSequence text,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			negativeButtonText = text;
			negativeButtonListener = listener;
		} else {
			super.setNegativeButton(text, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setNegativeButton(int resourceId,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			setNegativeButton(context.getText(resourceId), listener);
		} else {
			super.setNegativeButton(resourceId, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setPositiveButton(final CharSequence text,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			positiveButtonText = text;
			positiveButtonListener = listener;
		} else {
			super.setPositiveButton(text, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setPositiveButton(int resourceId,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			setPositiveButton(context.getText(resourceId), listener);
		} else {
			setPositiveButton(resourceId, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setNeutralButton(final CharSequence text,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			neutralButtonText = text;
			neutralButtonListener = listener;
		} else {
			super.setNeutralButton(text, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setNeutralButton(final int resourceId,
			final OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			setNeutralButton(context.getText(resourceId), listener);
		} else {
			super.setNeutralButton(resourceId, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setItems(final CharSequence[] items,
			OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			listAdapter = new ArrayAdapter<CharSequence>(context,
					android.R.layout.simple_list_item_1, items);
			listViewClickListener = listener;
		} else {
			super.setItems(items, listener);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setItems(final int resourceId,
			OnClickListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			this.setItems(context.getResources().getTextArray(resourceId),
					listener);
		} else {
			super.setItems(resourceId, listener);
		}

		return this;
	}

	@Override
	public AlertDialog create() {
		AlertDialog dialog = super.create();

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			addNegativeButton(dialog);
			addNeutralButton(dialog);
			addPositiveButton(dialog);
			showListView(dialog);
			hideTitleIfEmpty();
			hideMessageIfEmpty();
			hideButtonBarIfEmpty();
		}

		return dialog;
	}

}