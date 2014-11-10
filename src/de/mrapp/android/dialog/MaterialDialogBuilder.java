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

	private CharSequence title;

	private CharSequence message;

	private Drawable icon;

	private int titleColor;

	private int buttonTextColor;

	private boolean stackButtons;

	private CharSequence negativeButtonText;

	private CharSequence neutralButtonText;

	private CharSequence positiveButtonText;

	private OnClickListener negativeButtonListener;

	private OnClickListener neutralButtonListener;

	private OnClickListener positiveButtonListener;

	private ListAdapter listAdapter;

	private int listViewChoiceMode;

	private OnClickListener listViewSingleChoiceListener;

	private OnMultiChoiceClickListener listViewMultiChoiceListener;

	private OnItemSelectedListener listViewItemSelectedListener;

	private boolean[] checkedListItems;

	private View customView;

	private int customViewId;

	private View customTitleView;

	/**
	 * Inflates the dialog's layout.
	 */
	private View inflateLayout() {
		View root = View.inflate(context, R.layout.material_dialog, null);
		super.setView(root);
		return root;
	}

	private ViewGroup inflateTitleView(View root) {
		ViewGroup titleContainer = (ViewGroup) root
				.findViewById(R.id.title_container);

		if (customTitleView != null) {
			titleContainer.setVisibility(View.VISIBLE);
			titleContainer.addView(customTitleView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		} else {
			View.inflate(context, R.layout.material_dialog_title,
					titleContainer);
		}

		initializeTitle(titleContainer);
		return titleContainer;
	}

	private void initializeTitle(final ViewGroup titleContainer) {
		TextView titleTextView = (TextView) titleContainer
				.findViewById(android.R.id.title);

		if (titleTextView != null) {
			if (!TextUtils.isEmpty(title) || icon != null) {
				titleContainer.setVisibility(View.VISIBLE);
				titleTextView.setText(title);

				if (titleColor != 0) {
					titleTextView.setTextColor(titleColor);
				}

				if (icon != null) {
					titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon,
							null, null, null);
				}

			}
		}
	}

	private TextView initializeMessage(final View root,
			final ViewGroup titleContainer) {
		TextView messageTextView = (TextView) root
				.findViewById(android.R.id.message);

		if (!TextUtils.isEmpty(message)) {
			showMessageTextView(titleContainer, messageTextView);
			messageTextView.setText(message);
		}

		return messageTextView;
	}

	private void showMessageTextView(final ViewGroup titleContainer,
			TextView messageTextView) {
		messageTextView.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams layoutParams = (LayoutParams) titleContainer
				.getLayoutParams();
		layoutParams.bottomMargin = context.getResources()
				.getDimensionPixelSize(R.dimen.dialog_content_spacing);
		titleContainer.setLayoutParams(layoutParams);
	}

	private void inflateContentView(View root, ViewGroup titleContainer,
			TextView messageTextView, final AlertDialog dialog) {
		ViewGroup contentContainer = (ViewGroup) root
				.findViewById(R.id.content_container);

		if (customView != null) {
			showContentContainer(contentContainer, titleContainer,
					messageTextView);
			contentContainer.addView(customView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		} else if (customViewId != 0) {
			showContentContainer(contentContainer, titleContainer,
					messageTextView);
			View.inflate(context, customViewId, contentContainer);
		} else {
			View.inflate(context, R.layout.material_dialog_list_view,
					contentContainer);
		}

		initializeContent(contentContainer, titleContainer, messageTextView,
				dialog);
	}

	private void initializeContent(final ViewGroup contentContainer,
			final ViewGroup titleContainer, final TextView messageTextView,
			final AlertDialog dialog) {
		ListView listView = (ListView) contentContainer
				.findViewById(android.R.id.list);

		if (listAdapter != null && !listAdapter.isEmpty() && listView != null) {
			showContentContainer(contentContainer, titleContainer,
					messageTextView);
			listView.setAdapter(listAdapter);
			listView.setVisibility(View.VISIBLE);
			initializeListViewListener(dialog, listView);
			initializeListViewCheckedItems(listView);

			if (listViewItemSelectedListener != null) {
				listView.setOnItemSelectedListener(listViewItemSelectedListener);
			}
		}
	}

	private void initializeListViewListener(final AlertDialog dialog,
			ListView listView) {
		if (listViewChoiceMode == ListView.CHOICE_MODE_NONE) {
			listView.setOnItemClickListener(new OnItemClickListenerWrapper(
					listViewSingleChoiceListener, dialog,
					AlertDialog.BUTTON_POSITIVE));
		} else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
			listView.setOnItemClickListener(new OnItemClickListenerWrapper(
					listViewSingleChoiceListener, dialog, 0));
		} else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
			listView.setOnItemClickListener(new OnMultiChoiceClickListenerWrapper(
					listViewMultiChoiceListener, dialog, 0));
		}
	}

	private void initializeListViewCheckedItems(ListView listView) {
		if (checkedListItems != null) {
			for (int i = 0; i < checkedListItems.length; i++) {
				listView.setItemChecked(i, checkedListItems[i]);
			}
		}
	}

	private void showContentContainer(ViewGroup contentContainer,
			ViewGroup titleContainer, TextView messageTextView) {
		contentContainer.setVisibility(View.VISIBLE);
		int contentSpacing = context.getResources().getDimensionPixelSize(
				R.dimen.dialog_content_spacing);
		LinearLayout.LayoutParams titleLayoutParams = (LayoutParams) titleContainer
				.getLayoutParams();
		titleLayoutParams.bottomMargin = contentSpacing;
		titleContainer.setLayoutParams(titleLayoutParams);
		LinearLayout.LayoutParams messageLayoutParams = (LayoutParams) messageTextView
				.getLayoutParams();
		messageLayoutParams.bottomMargin = contentSpacing;
		messageTextView.setLayoutParams(messageLayoutParams);
	}

	private void inflateButtonBar(View root, AlertDialog dialog) {
		ViewGroup buttonBarContainer = (ViewGroup) root
				.findViewById(R.id.button_bar_container);

		if (stackButtons) {
			View.inflate(context, R.layout.stacked_button_bar,
					buttonBarContainer);
		} else {
			View.inflate(context, R.layout.horizontal_button_bar,
					buttonBarContainer);
		}

		initializeButtonBar(root, buttonBarContainer, dialog);
	}

	private void initializeButtonBar(View root, ViewGroup buttonBarContainer,
			AlertDialog dialog) {
		Button negativeButton = addNegativeButton(buttonBarContainer, dialog);
		Button neutralButton = addNeutralButton(buttonBarContainer, dialog);
		Button positiveButton = addPositiveButton(buttonBarContainer, dialog);

		if (negativeButton != null || neutralButton != null
				|| positiveButton != null) {
			showButtonBarContainer(root, buttonBarContainer);
		}
	}

	private void showButtonBarContainer(View root, View buttonBarContainer) {
		View contentRoot = root.findViewById(R.id.content_root);
		buttonBarContainer.setVisibility(View.VISIBLE);
		int paddingLeft = context.getResources().getDimensionPixelSize(
				R.dimen.dialog_content_padding_left);
		int paddingTop = context.getResources().getDimensionPixelSize(
				R.dimen.dialog_content_padding_top);
		int paddingRight = context.getResources().getDimensionPixelSize(
				R.dimen.dialog_content_padding_right);
		int paddingBottom = context.getResources().getDimensionPixelSize(
				R.dimen.dialog_content_padding_bottom);
		contentRoot.setPadding(paddingLeft, paddingTop, paddingRight,
				paddingBottom);
	}

	private Button addNegativeButton(View root, AlertDialog dialog) {
		if (!TextUtils.isEmpty(negativeButtonText)) {
			Button negativeButton = (Button) root
					.findViewById(android.R.id.button1);
			negativeButton.setText(negativeButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					negativeButtonListener, dialog, AlertDialog.BUTTON_NEGATIVE);
			negativeButton.setOnClickListener(onClickListener);
			negativeButton.setVisibility(View.VISIBLE);

			if (buttonTextColor != 0) {
				negativeButton.setTextColor(buttonTextColor);
			}

			return negativeButton;
		}

		return null;
	}

	private Button addNeutralButton(View root, AlertDialog dialog) {
		if (!TextUtils.isEmpty(neutralButtonText)) {
			Button neutralButton = (Button) root
					.findViewById(android.R.id.button2);
			neutralButton.setText(neutralButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					neutralButtonListener, dialog, AlertDialog.BUTTON_NEUTRAL);
			neutralButton.setOnClickListener(onClickListener);
			neutralButton.setVisibility(View.VISIBLE);

			if (buttonTextColor != 0) {
				neutralButton.setTextColor(buttonTextColor);
			}

			return neutralButton;
		}

		return null;
	}

	private Button addPositiveButton(View root, AlertDialog dialog) {
		if (!TextUtils.isEmpty(positiveButtonText)) {
			Button positiveButton = (Button) root
					.findViewById(android.R.id.button3);
			positiveButton.setText(positiveButtonText.toString().toUpperCase(
					Locale.getDefault()));
			OnClickListenerWrapper onClickListener = new OnClickListenerWrapper(
					positiveButtonListener, dialog, AlertDialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(onClickListener);
			positiveButton.setVisibility(View.VISIBLE);

			if (buttonTextColor != 0) {
				positiveButton.setTextColor(buttonTextColor);
			}

			return positiveButton;
		}

		return null;
	}

	public MaterialDialogBuilder(Context context) {
		this(context, 0);
		this.context = context;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MaterialDialogBuilder(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public MaterialDialogBuilder setTitleColor(final int color) {
		this.titleColor = color;
		return this;
	}

	public MaterialDialogBuilder setButtonTextColor(final int color) {
		this.buttonTextColor = color;
		return this;
	}

	public MaterialDialogBuilder stackButtons(final boolean stackButtons) {
		this.stackButtons = stackButtons;
		return this;
	}

	@Override
	public MaterialDialogBuilder setTitle(CharSequence title) {
		this.title = title;
		return this;
	}

	@Override
	public MaterialDialogBuilder setTitle(int resourceId) {
		return setTitle(context.getText(resourceId));
	}

	@Override
	public MaterialDialogBuilder setMessage(CharSequence message) {
		this.message = message;
		return this;
	}

	@Override
	public MaterialDialogBuilder setMessage(int resourceId) {
		return setMessage(context.getText(resourceId));
	}

	@Override
	public MaterialDialogBuilder setIcon(Drawable icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public MaterialDialogBuilder setIcon(int resourceId) {
		return setIcon(context.getResources().getDrawable(resourceId));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public final MaterialDialogBuilder setIconAttribute(final int attributeId) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				new int[] { attributeId });
		return setIcon(typedArray.getDrawable(0));
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
		listViewChoiceMode = ListView.CHOICE_MODE_NONE;
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
		listViewChoiceMode = ListView.CHOICE_MODE_NONE;
		return this;
	}

	@Override
	public MaterialDialogBuilder setSingleChoiceItems(
			final CharSequence[] items, final int checkedItem,
			final OnClickListener listener) {
		listAdapter = new ArrayAdapter<CharSequence>(context,
				android.R.layout.simple_list_item_single_choice, items);
		listViewSingleChoiceListener = listener;
		listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
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
		listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
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
		listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
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
		listViewItemSelectedListener = listener;
		return this;
	}

	@Override
	public MaterialDialogBuilder setView(final View view) {
		customView = view;
		customViewId = 0;
		return this;
	}

	@Override
	public MaterialDialogBuilder setView(final int resourceId) {
		customViewId = resourceId;
		customView = null;
		return this;
	}

	@Override
	public MaterialDialogBuilder setCustomTitle(final View view) {
		customTitleView = view;
		return this;
	}

	@Override
	public AlertDialog create() {
		View root = inflateLayout();
		AlertDialog dialog = super.create();
		ViewGroup titleContainer = inflateTitleView(root);
		TextView messageTextView = initializeMessage(root, titleContainer);
		inflateContentView(root, titleContainer, messageTextView, dialog);
		inflateButtonBar(root, dialog);
		return dialog;
	}

}