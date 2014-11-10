package de.mrapp.android.dialog;

import java.util.Locale;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;

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

	private OnClickListener listViewSingleChoiceListener;

	private OnMultiChoiceClickListener listViewMultiChoiceListener;

	private boolean[] checkedListItems;

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

			if (listView.getChoiceMode() == ListView.CHOICE_MODE_NONE) {
				listView.setOnItemClickListener(new OnItemClickListenerWrapper(
						listViewSingleChoiceListener, dialog,
						AlertDialog.BUTTON_POSITIVE));
			} else if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
				listView.setOnItemClickListener(new OnItemClickListenerWrapper(
						listViewSingleChoiceListener, dialog, 0));
			} else if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
				listView.setOnItemClickListener(new OnMultiChoiceClickListenerWrapper(
						listViewMultiChoiceListener, dialog, 0));
			}

			if (checkedListItems != null) {
				for (int i = 0; i < checkedListItems.length; i++) {
					listView.setItemChecked(i, checkedListItems[i]);
				}
			}

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
		inflateLayout();
		applyTheme(theme);
	}

	@Override
	public MaterialDialogBuilder setTitle(CharSequence title) {
		titleTextView.setText(title);
		return this;
	}

	@Override
	public MaterialDialogBuilder setTitle(int resourceId) {
		titleTextView.setText(resourceId);
		return this;
	}

	@Override
	public MaterialDialogBuilder setMessage(CharSequence message) {
		messageTextView.setText(message);
		return this;
	}

	@Override
	public MaterialDialogBuilder setMessage(int resourceId) {
		messageTextView.setText(resourceId);
		return this;
	}

	@Override
	public MaterialDialogBuilder setIcon(Drawable icon) {
		titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null,
				null);
		return this;
	}

	@Override
	public MaterialDialogBuilder setIcon(int resourceId) {
		Drawable icon = context.getResources().getDrawable(resourceId);
		return setIcon(icon);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public final MaterialDialogBuilder setIconAttribute(final int attributeId) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				new int[] { attributeId });
		Drawable icon = typedArray.getDrawable(0);
		return setIcon(icon);
	}

	@Override
	public MaterialDialogBuilder setNegativeButton(final CharSequence text,
			final OnClickListener listener) {
		negativeButtonText = text;
		negativeButtonListener = listener;
		return this;
	}

	@Override
	public MaterialDialogBuilder setNegativeButton(int resourceId,
			final OnClickListener listener) {
		return setNegativeButton(context.getText(resourceId), listener);
	}

	@Override
	public MaterialDialogBuilder setPositiveButton(final CharSequence text,
			final OnClickListener listener) {
		positiveButtonText = text;
		positiveButtonListener = listener;
		return this;
	}

	@Override
	public MaterialDialogBuilder setPositiveButton(int resourceId,
			final OnClickListener listener) {
		return setPositiveButton(context.getText(resourceId), listener);
	}

	@Override
	public MaterialDialogBuilder setNeutralButton(final CharSequence text,
			final OnClickListener listener) {
		neutralButtonText = text;
		neutralButtonListener = listener;
		return this;
	}

	@Override
	public MaterialDialogBuilder setNeutralButton(final int resourceId,
			final OnClickListener listener) {
		return setNeutralButton(context.getText(resourceId), listener);
	}

	@Override
	public MaterialDialogBuilder setItems(final CharSequence[] items,
			OnClickListener listener) {
		listAdapter = new ArrayAdapter<CharSequence>(context,
				android.R.layout.simple_list_item_1, items);
		listViewSingleChoiceListener = listener;
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		return this;
	}

	@Override
	public MaterialDialogBuilder setItems(final int resourceId,
			OnClickListener listener) {
		return setItems(context.getResources().getTextArray(resourceId),
				listener);
	}

	@Override
	public final MaterialDialogBuilder setAdapter(final ListAdapter adapter,
			final OnClickListener listener) {
		listAdapter = adapter;
		listViewSingleChoiceListener = listener;
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		return this;
	}

	@Override
	public MaterialDialogBuilder setSingleChoiceItems(
			final CharSequence[] items, final int checkedItem,
			final OnClickListener listener) {
		listAdapter = new ArrayAdapter<CharSequence>(context,
				android.R.layout.simple_list_item_single_choice, items);
		listViewSingleChoiceListener = listener;
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		checkedListItems = new boolean[items.length];

		for (int i = 0; i < checkedListItems.length; i++) {
			checkedListItems[i] = (i == checkedItem);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setSingleChoiceItems(final int resourceId,
			final int checkedItem, final OnClickListener listener) {
		return setSingleChoiceItems(
				context.getResources().getTextArray(resourceId), checkedItem,
				listener);
	}

	@Override
	public MaterialDialogBuilder setSingleChoiceItems(
			final ListAdapter adapter, final int checkedItem,
			final OnClickListener listener) {
		listAdapter = adapter;
		listViewSingleChoiceListener = listener;
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		checkedListItems = new boolean[adapter.getCount()];

		for (int i = 0; i < checkedListItems.length; i++) {
			checkedListItems[i] = (i == checkedItem);
		}

		return this;
	}

	@Override
	public MaterialDialogBuilder setSingleChoiceItems(final Cursor cursor,
			final int checkedItem, final String labelColumn,
			final OnClickListener listener) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public MaterialDialogBuilder setMultiChoiceItems(
			final CharSequence[] items, final boolean[] checkedItems,
			final OnMultiChoiceClickListener listener) {
		listAdapter = new ArrayAdapter<CharSequence>(context,
				android.R.layout.simple_list_item_multiple_choice, items);
		listViewMultiChoiceListener = listener;
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		checkedListItems = checkedItems;
		return this;
	}

	@Override
	public MaterialDialogBuilder setMultiChoiceItems(final int resourceId,
			final boolean[] checkedItems,
			final OnMultiChoiceClickListener listener) {
		return setMultiChoiceItems(
				context.getResources().getTextArray(resourceId), checkedItems,
				listener);
	}

	@Override
	public MaterialDialogBuilder setMultiChoiceItems(final Cursor cursor,
			final String isCheckedColumn, final String labelColumn,
			final OnMultiChoiceClickListener listener) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public MaterialDialogBuilder setCursor(final Cursor cursor,
			final OnClickListener listener, final String labelColumn) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public MaterialDialogBuilder setOnItemSelectedListener(
			final OnItemSelectedListener listener) {
		listView.setOnItemSelectedListener(listener);
		return this;
	}

	@Override
	public AlertDialog create() {
		AlertDialog dialog = super.create();
		addNegativeButton(dialog);
		addNeutralButton(dialog);
		addPositiveButton(dialog);
		showListView(dialog);
		hideTitleIfEmpty();
		hideMessageIfEmpty();
		hideButtonBarIfEmpty();
		return dialog;
	}

}