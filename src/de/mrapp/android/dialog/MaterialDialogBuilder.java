/*
 * AndroidMaterialDialog Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. 
 */
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

/**
 * A builder, which allows to create dialogs, which are designed according to
 * Android 5.0's Material Design guidelines even on pre-Lollipop devices. Such a
 * dialog consists of a title, a message and up to three buttons. Furthermore
 * the dialog can be used to show list items. It is possible to customize the
 * color of the dialog's title and button texts and the title as well as the
 * dialog's content can be replaced by a custom view.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public class MaterialDialogBuilder extends AlertDialog.Builder {

	/**
	 * The context, which is used by the builder.
	 */
	private Context context;

	/**
	 * The title of the dialog, which is created by the builder.
	 */
	private CharSequence title;

	/**
	 * The message of the dialog, which is created by the builder.
	 */
	private CharSequence message;

	/**
	 * The icon of the dialog, which is created by the builder.
	 */
	private Drawable icon;

	/**
	 * The color of the title of the dialog, which is created by the builder.
	 */
	private int titleColor;

	/**
	 * The color of the button texts of the dialog, which is created by the
	 * builder.
	 */
	private int buttonTextColor;

	/**
	 * True, if the buttons of the dialog, which is created by the builder,
	 * should be aligned vertically, false otherwise.
	 */
	private boolean stackButtons;

	/**
	 * The text of the negative button of the dialog, which is created by the
	 * builder.
	 */
	private CharSequence negativeButtonText;

	/**
	 * The text of the neutral button of the dialog, which is created by the
	 * builder.
	 */
	private CharSequence neutralButtonText;

	/**
	 * The text of the positive button of the dialog, which is created by the
	 * builder.
	 */
	private CharSequence positiveButtonText;

	/**
	 * The listener, which is notified, when the negative button of the dialog,
	 * which is created by the builder, is clicked.
	 */
	private OnClickListener negativeButtonListener;

	/**
	 * The listener, which is notified, when the neutral button of the dialog,
	 * which is created by the builder, is clicked.
	 */
	private OnClickListener neutralButtonListener;

	/**
	 * The listener, which is notified, when the positive button of the dialog,
	 * which is created by the builder, is clicked.
	 */
	private OnClickListener positiveButtonListener;

	/**
	 * The adapter, which is used to manage the list items of the dialog, which
	 * is created by the builder.
	 */
	private ListAdapter listAdapter;

	/**
	 * The choice mode of the list view, which is used to show the list items of
	 * the dialog, which is created by the builder.
	 */
	private int listViewChoiceMode;

	/**
	 * The listener, which is notified, when the selection of a list item of the
	 * dialog, which is created by the builder, changes and the list view's
	 * choice mode is <code>ListView.CHOICE_MODE_SINGLE</code>.
	 */
	private OnClickListener listViewSingleChoiceListener;

	/**
	 * The listener, which is notified, when the selection of a list item of the
	 * dialog, which is created by the builder, changes and the list view's
	 * choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
	 */
	private OnMultiChoiceClickListener listViewMultiChoiceListener;

	/**
	 * The listener, which is notified, when a list item of the dialog, which is
	 * created by the builder, becomes selected, independently of the list
	 * view's choice mode.
	 */
	private OnItemSelectedListener listViewItemSelectedListener;

	/**
	 * An array, which is used to identify the list items of the dialog, which
	 * is created by the builder, which are by selected by default.
	 */
	private boolean[] checkedListItems;

	/**
	 * The custom content view of the dialog, which is created by the builder.
	 */
	private View customView;

	/**
	 * The resource id of the custom content view of the dialog, which is
	 * created by the builder.
	 */
	private int customViewId;

	/**
	 * The custom title view of the dialog, which is created by the builder.
	 */
	private View customTitleView;

	/**
	 * Inflates the dialog's layout.
	 * 
	 * @return The root view of the layout, which has been inflated, as an
	 *         instance of the class {@link View}
	 */
	private View inflateLayout() {
		View root = View.inflate(context, R.layout.material_dialog, null);
		super.setView(root);
		return root;
	}

	/**
	 * Inflates the dialog's title view, which may either be the default view or
	 * a custom view, if one has been set before.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @return The parent view of the title view, which has been inflated, as an
	 *         instance of the class {@link ViewGroup}
	 */
	private ViewGroup inflateTitleView(final View root) {
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

	/**
	 * Initializes the dialog's title and icon.
	 * 
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 */
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

	/**
	 * Initializes the dialog's message.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 * @return The text view, which is used to show the dialog's message, as an
	 *         instance of the class {@link TextView}
	 */
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

	/**
	 * Shows the text view, which is used to show the dialog's message.
	 * 
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 * @param messageTextView
	 *            The text view, which is used to show the dialog's message, as
	 *            an instance of the class {@link TextView}
	 */
	private void showMessageTextView(final ViewGroup titleContainer,
			final TextView messageTextView) {
		messageTextView.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams layoutParams = (LayoutParams) titleContainer
				.getLayoutParams();
		layoutParams.bottomMargin = context.getResources()
				.getDimensionPixelSize(R.dimen.dialog_content_spacing);
		titleContainer.setLayoutParams(layoutParams);
	}

	/**
	 * Inflates the dialog's content view, which may either be the default view
	 * or a custom view, if one has been set before.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 * @param messageTextView
	 *            The text view, which is used to show the dialog's message, as
	 *            an instance of the class {@link TextView}
	 * @param dialog
	 *            The dialog, whose content view should be inflated, as an
	 *            instance of the class {@link AlertDialog}
	 */
	private void inflateContentView(final View root,
			final ViewGroup titleContainer, final TextView messageTextView,
			final AlertDialog dialog) {
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

	/**
	 * Initializes the dialog's content. The content view is shown if a custom
	 * content view has been specified or if any list items have been set.
	 * 
	 * @param contentContainer
	 *            The parent view of the the content view as an instance of the
	 *            clas {@link ViewGroup}
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 * @param messageTextView
	 *            The text view, which is used to show the dialog's message, as
	 *            an instance of the class {@link TextView}
	 * @param dialog
	 *            The dialog, whose content should be initialized, as an
	 *            instance of the class {@link AlertDialog}
	 */
	private void initializeContent(final ViewGroup contentContainer,
			final ViewGroup titleContainer, final TextView messageTextView,
			final AlertDialog dialog) {
		ListView listView = (ListView) contentContainer
				.findViewById(android.R.id.list);

		if (listAdapter != null && !listAdapter.isEmpty() && listView != null) {
			showContentContainer(contentContainer, titleContainer,
					messageTextView);
			listView.setChoiceMode(listViewChoiceMode);
			listView.setAdapter(listAdapter);
			initializeListViewListener(dialog, listView);
			initializeListViewCheckedItems(listView);
		}
	}

	/**
	 * Initializes the listener, which should be notified, when the selection of
	 * a list item of the dialog has been changed.
	 * 
	 * @param dialog
	 *            The dialog, the list items belong to, as an instance of the
	 *            class {@link AlertDialog}
	 * @param listView
	 *            The list view, which is used to show the list items, as an
	 *            instance of the class {@link ListView}
	 */
	private void initializeListViewListener(final AlertDialog dialog,
			final ListView listView) {
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

		if (listViewItemSelectedListener != null) {
			listView.setOnItemSelectedListener(listViewItemSelectedListener);
		}
	}

	/**
	 * Initializes the list items, which are selected by default.
	 * 
	 * @param listView
	 *            The list view, which is used to show the list items, as an
	 *            instance of the class {@link ListView}
	 */
	private void initializeListViewCheckedItems(final ListView listView) {
		boolean scrolledToCheckedItem = false;

		if (checkedListItems != null) {
			for (int i = 0; i < checkedListItems.length; i++) {
				listView.setItemChecked(i, checkedListItems[i]);

				if (!scrolledToCheckedItem && checkedListItems[i]) {
					listView.setSelection(i);
				}
			}
		}
	}

	/**
	 * Shows the parent view of the dialog's content view.
	 * 
	 * @param contentContainer
	 *            The parent view of the the content view as an instance of the
	 *            clas {@link ViewGroup}
	 * @param titleContainer
	 *            The parent view of the title view as an instance of the class
	 *            {@link ViewGroup}
	 * @param messageTextView
	 *            The text view, which is used to show the dialog's message, as
	 *            an instance of the class {@link TextView}
	 */
	private void showContentContainer(final ViewGroup contentContainer,
			final ViewGroup titleContainer, final TextView messageTextView) {
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

	/**
	 * Inflates the button bar, which contains the dialog's buttons.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param dialog
	 *            The dialog, the buttons belong to, as an instance of the class
	 *            {@link AlertDialog}
	 */
	private void inflateButtonBar(final View root, final AlertDialog dialog) {
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

	/**
	 * Initializes the button bar, which contains the dialog's buttons.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param buttonBarContainer
	 *            The parent view of the button bar, which contains the dialog's
	 *            buttons, as an instance of the class {@link ViewGroup}
	 * @param dialog
	 *            The dialog, the buttons belong to, as an instance of the class
	 *            {@link AlertDialog}
	 */
	private void initializeButtonBar(final View root,
			final ViewGroup buttonBarContainer, final AlertDialog dialog) {
		Button negativeButton = addNegativeButton(buttonBarContainer, dialog);
		Button neutralButton = addNeutralButton(buttonBarContainer, dialog);
		Button positiveButton = addPositiveButton(buttonBarContainer, dialog);

		if (negativeButton != null || neutralButton != null
				|| positiveButton != null) {
			showButtonBarContainer(root, buttonBarContainer);
		}
	}

	/**
	 * Shows the parent view of the button bar, which contains the dialog's
	 * buttons.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param buttonBarContainer
	 *            The parent view of the button bar, which contains the dialog's
	 *            buttons, as an instance of the class {@link ViewGroup}
	 */
	private void showButtonBarContainer(final View root,
			final View buttonBarContainer) {
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

	/**
	 * Adds a negative button to the dialog, if an appropriate button text has
	 * been set before.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param dialog
	 *            The dialog, the button should belong to, as an instance of the
	 *            class {@link AlertDialog}
	 * @return The button, which has been added to the dialog, as an instance of
	 *         the class {@link Button} or null, if no button has been added
	 */
	private Button addNegativeButton(final View root, final AlertDialog dialog) {
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

	/**
	 * Adds a neutral button to the dialog, if an appropriate button text has
	 * been set before.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param dialog
	 *            The dialog, the button should belong to, as an instance of the
	 *            class {@link AlertDialog}
	 * @return The button, which has been added to the dialog, as an instance of
	 *         the class {@link Button} or null, if no button has been added
	 */
	private Button addNeutralButton(final View root, final AlertDialog dialog) {
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

	/**
	 * Adds a positive button to the dialog, if an appropriate button text has
	 * been set before.
	 * 
	 * @param root
	 *            The root view of the dialog's layout as an instance of the
	 *            class {@link View}
	 * @param dialog
	 *            The dialog, the button should belong to, as an instance of the
	 *            class {@link AlertDialog}
	 * @return The button, which has been added to the dialog, as an instance of
	 *         the class {@link Button} or null, if no button has been added
	 */
	private Button addPositiveButton(final View root, final AlertDialog dialog) {
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

	/**
	 * Creates a new builder, which allows to create dialogs, which are designed
	 * according to Android 5.0's Material Design guidelines even on
	 * pre-Lollipop devices.
	 * 
	 * @param context
	 *            The context, which should be used by the builder, as an
	 *            instance of the class {@link Context}
	 */
	public MaterialDialogBuilder(final Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Sets the color of the title of the dialog, which is created by the
	 * builder.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value
	 * @return The builder, the method has been called upon, as an instance of
	 *         the class {@link MaterialDialogBuilder}
	 */
	public final MaterialDialogBuilder setTitleColor(final int color) {
		this.titleColor = color;
		return this;
	}

	/**
	 * Sets the color of the button texts of the dialog, which is created by the
	 * builder.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value
	 * @return The builder, the method has been called upon, as an instance of
	 *         the class {@link MaterialDialogBuilder}
	 */
	public final MaterialDialogBuilder setButtonTextColor(final int color) {
		this.buttonTextColor = color;
		return this;
	}

	/**
	 * Sets, whether the buttons of the dialog, which is created by the builder,
	 * should be aligned vertically, or not.
	 * 
	 * @param stackButtons
	 *            True, if the buttons of the dialog, which is created by the
	 *            builder, should be aligned vertically, false otherwise
	 * @return The builder, the method has been called upon, as an instance of
	 *         the class {@link MaterialDialogBuilder}
	 */
	public final MaterialDialogBuilder stackButtons(final boolean stackButtons) {
		this.stackButtons = stackButtons;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setTitle(final CharSequence title) {
		this.title = title;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setTitle(final int resourceId) {
		return setTitle(context.getText(resourceId));
	}

	@Override
	public final MaterialDialogBuilder setMessage(final CharSequence message) {
		this.message = message;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setMessage(final int resourceId) {
		return setMessage(context.getText(resourceId));
	}

	@Override
	public final MaterialDialogBuilder setIcon(final Drawable icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setIcon(final int resourceId) {
		return setIcon(context.getResources().getDrawable(resourceId));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public final MaterialDialogBuilder setIconAttribute(final int attributeId) {
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				new int[] { attributeId });
		return setIcon(typedArray.getDrawable(0));
	}

	@Override
	public final MaterialDialogBuilder setNegativeButton(
			final CharSequence text, final OnClickListener listener) {
		negativeButtonText = text;
		negativeButtonListener = listener;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setNegativeButton(final int resourceId,
			final OnClickListener listener) {
		return setNegativeButton(context.getText(resourceId), listener);
	}

	@Override
	public final MaterialDialogBuilder setPositiveButton(
			final CharSequence text, final OnClickListener listener) {
		positiveButtonText = text;
		positiveButtonListener = listener;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setPositiveButton(final int resourceId,
			final OnClickListener listener) {
		return setPositiveButton(context.getText(resourceId), listener);
	}

	@Override
	public final MaterialDialogBuilder setNeutralButton(
			final CharSequence text, final OnClickListener listener) {
		neutralButtonText = text;
		neutralButtonListener = listener;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setNeutralButton(final int resourceId,
			final OnClickListener listener) {
		return setNeutralButton(context.getText(resourceId), listener);
	}

	@Override
	public final MaterialDialogBuilder setItems(final CharSequence[] items,
			final OnClickListener listener) {
		listAdapter = new ArrayAdapter<CharSequence>(context,
				android.R.layout.simple_list_item_1, items);
		listViewSingleChoiceListener = listener;
		listViewChoiceMode = ListView.CHOICE_MODE_NONE;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setItems(final int resourceId,
			final OnClickListener listener) {
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
	public final MaterialDialogBuilder setCursor(final Cursor cursor,
			final OnClickListener listener, final String labelColumn) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public final MaterialDialogBuilder setSingleChoiceItems(
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
	public final MaterialDialogBuilder setSingleChoiceItems(
			final int resourceId, final int checkedItem,
			final OnClickListener listener) {
		return setSingleChoiceItems(
				context.getResources().getTextArray(resourceId), checkedItem,
				listener);
	}

	@Override
	public final MaterialDialogBuilder setSingleChoiceItems(
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
	public final MaterialDialogBuilder setSingleChoiceItems(
			final Cursor cursor, final int checkedItem,
			final String labelColumn, final OnClickListener listener) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public final MaterialDialogBuilder setMultiChoiceItems(
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
	public final MaterialDialogBuilder setMultiChoiceItems(
			final int resourceId, final boolean[] checkedItems,
			final OnMultiChoiceClickListener listener) {
		return setMultiChoiceItems(
				context.getResources().getTextArray(resourceId), checkedItems,
				listener);
	}

	@Override
	public final MaterialDialogBuilder setMultiChoiceItems(final Cursor cursor,
			final String isCheckedColumn, final String labelColumn,
			final OnMultiChoiceClickListener listener) {
		throw new UnsupportedOperationException(
				"This method is not supported yet");
	}

	@Override
	public final MaterialDialogBuilder setOnItemSelectedListener(
			final OnItemSelectedListener listener) {
		listViewItemSelectedListener = listener;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setView(final View view) {
		customView = view;
		customViewId = 0;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setView(final int resourceId) {
		customViewId = resourceId;
		customView = null;
		return this;
	}

	@Override
	public final MaterialDialogBuilder setCustomTitle(final View view) {
		customTitleView = view;
		return this;
	}

	@Override
	public final AlertDialog create() {
		View root = inflateLayout();
		AlertDialog dialog = super.create();
		ViewGroup titleContainer = inflateTitleView(root);
		TextView messageTextView = initializeMessage(root, titleContainer);
		inflateContentView(root, titleContainer, messageTextView, dialog);
		inflateButtonBar(root, dialog);
		return dialog;
	}

}