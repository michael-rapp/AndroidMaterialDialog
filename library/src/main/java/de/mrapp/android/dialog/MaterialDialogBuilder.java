/*
 * AndroidMaterialDialog Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A builder, which allows to create dialogs, which are designed according to Android 5.0's Material
 * Design guidelines even on pre-Lollipop devices. Such a dialog consists of a title, a message and
 * up to three buttons. Furthermore the dialog can be used to show list items. It is possible to
 * customize the color of the dialog's title and button texts and the title as well as the dialog's
 * content can be replaced by a custom view.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class MaterialDialogBuilder extends AlertDialog.Builder {

    /**
     * Defines the interface, a class, which should be able to validate the content of a dialog,
     * must implement.
     */
    public interface Validator {

        /**
         * Validates the content of a dialog.
         *
         * @return True, if the content of the dialog is valid, false otherwise
         */
        boolean validate();

    }

    /**
     * The context, which is used by the builder.
     */
    private final Context context;

    /**
     * A set, which contains the validators of the dialog.
     */
    private final Set<Validator> validators;

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
    private int titleColor = -1;

    /**
     * The color of the message of the dialog, which is created by the builder.
     */
    private int messageColor = -1;

    /**
     * The color of the list items of the dialog, which is created by the builder.
     */
    private int itemColor = -1;

    /**
     * The control color of the list items of the dialog, which is created by the builder.
     */
    private int itemControlColor = -1;

    /**
     * The background of the dialog, which is created by the builder.
     */
    private Drawable background;

    /**
     * The color of the button texts of the dialog, which is created by the builder.
     */
    private int buttonTextColor = -1;

    /**
     * True, if the buttons of the dialog, which is created by the builder, should be aligned
     * vertically, false otherwise.
     */
    private boolean stackButtons;

    /**
     * The text of the negative button of the dialog, which is created by the builder.
     */
    private CharSequence negativeButtonText;

    /**
     * The text of the neutral button of the dialog, which is created by the builder.
     */
    private CharSequence neutralButtonText;

    /**
     * The text of the positive button of the dialog, which is created by the builder.
     */
    private CharSequence positiveButtonText;

    /**
     * The listener, which is notified, when the negative button of the dialog, which is created by
     * the builder, is clicked.
     */
    private OnClickListener negativeButtonListener;

    /**
     * The listener, which is notified, when the neutral button of the dialog, which is created by
     * the builder, is clicked.
     */
    private OnClickListener neutralButtonListener;

    /**
     * The listener, which is notified, when the positive button of the dialog, which is created by
     * the builder, is clicked.
     */
    private OnClickListener positiveButtonListener;

    /**
     * The adapter, which is used to manage the list items of the dialog, which is created by the
     * builder.
     */
    private ListAdapter listAdapter;

    /**
     * The choice mode of the list view, which is used to show the list items of the dialog, which
     * is created by the builder.
     */
    private int listViewChoiceMode;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog, which is
     * created by the builder, changes and the list view's choice mode is
     * <code>ListView.CHOICE_MODE_SINGLE</code>.
     */
    private OnClickListener listViewSingleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog, which is
     * created by the builder, changes and the list view's choice mode is
     * <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private OnMultiChoiceClickListener listViewMultiChoiceListener;

    /**
     * The listener, which is notified, when a list item of the dialog, which is created by the
     * builder, becomes selected, independently of the list view's choice mode.
     */
    private OnItemSelectedListener listViewItemSelectedListener;

    /**
     * An array, which is used to identify the list items of the dialog, which is created by the
     * builder, which are by selected by default.
     */
    private boolean[] checkedListItems;

    /**
     * The custom content view of the dialog, which is created by the builder.
     */
    private View customView;

    /**
     * The resource id of the custom content view of the dialog, which is created by the builder.
     */
    private int customViewId = -1;

    /**
     * The custom title view of the dialog, which is created by the builder.
     */
    private View customTitleView;

    /**
     * Inflates the dialog's layout.
     *
     * @return The root view of the layout, which has been inflated, as an instance of the class
     * {@link View}
     */
    @SuppressWarnings("deprecation")
    private View inflateLayout() {
        View root = View.inflate(context, R.layout.material_dialog, null);

        if (background != null) {
            root.setBackgroundDrawable(background);
        }

        super.setView(root);
        return root;
    }

    /**
     * Inflates the dialog's title view, which may either be the default view or a custom view, if
     * one has been set before.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @return The parent view of the title view, which has been inflated, as an instance of the
     * class {@link ViewGroup}
     */
    private ViewGroup inflateTitleView(@NonNull final View root) {
        ViewGroup titleContainer = (ViewGroup) root.findViewById(R.id.title_container);

        if (customTitleView != null) {
            titleContainer.setVisibility(View.VISIBLE);
            titleContainer.addView(customTitleView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            View.inflate(context, R.layout.material_dialog_title, titleContainer);
        }

        initializeTitle(titleContainer);
        return titleContainer;
    }

    /**
     * Initializes the dialog's title and icon.
     *
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     */
    private void initializeTitle(@NonNull final ViewGroup titleContainer) {
        TextView titleTextView = (TextView) titleContainer.findViewById(android.R.id.title);

        if (titleTextView != null) {
            if (!TextUtils.isEmpty(title) || icon != null) {
                titleContainer.setVisibility(View.VISIBLE);
                titleTextView.setText(title);

                if (titleColor != -1) {
                    titleTextView.setTextColor(titleColor);
                }

                if (icon != null) {
                    titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                }

            }
        }
    }

    /**
     * Initializes the dialog's message.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     * @return The text view, which is used to show the dialog's message, as an instance of the
     * class {@link TextView}
     */
    private TextView initializeMessage(@NonNull final View root,
                                       @NonNull final ViewGroup titleContainer) {
        TextView messageTextView = (TextView) root.findViewById(android.R.id.message);

        if (!TextUtils.isEmpty(message)) {
            showMessageTextView(titleContainer, messageTextView);
            messageTextView.setText(message);

            if (messageColor != -1) {
                messageTextView.setTextColor(messageColor);
            }
        }

        return messageTextView;
    }

    /**
     * Shows the text view, which is used to show the dialog's message.
     *
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     * @param messageTextView
     *         The text view, which is used to show the dialog's message, as an instance of the
     *         class {@link TextView}
     */
    private void showMessageTextView(@NonNull final ViewGroup titleContainer,
                                     @NonNull final TextView messageTextView) {
        messageTextView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) titleContainer.getLayoutParams();
        layoutParams.bottomMargin =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_spacing);
        titleContainer.setLayoutParams(layoutParams);
    }

    /**
     * Inflates the dialog's content view, which may either be the default view or a custom view, if
     * one has been set before.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     * @param messageTextView
     *         The text view, which is used to show the dialog's message, as an instance of the
     *         class {@link TextView}
     * @param dialog
     *         The dialog, whose content view should be inflated, as an instance of the class {@link
     *         AlertDialog}
     */
    private void inflateContentView(@NonNull final View root,
                                    @NonNull final ViewGroup titleContainer,
                                    @NonNull final TextView messageTextView,
                                    @NonNull final AlertDialog dialog) {
        ViewGroup contentContainer = (ViewGroup) root.findViewById(R.id.content_container);

        if (customView != null) {
            showContentContainer(contentContainer, titleContainer, messageTextView);
            contentContainer.addView(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (customViewId != -1) {
            showContentContainer(contentContainer, titleContainer, messageTextView);
            View.inflate(context, customViewId, contentContainer);
        } else {
            View.inflate(context, R.layout.material_dialog_list_view, contentContainer);
        }

        initializeContent(contentContainer, titleContainer, messageTextView, dialog);
    }

    /**
     * Initializes the dialog's content. The content view is shown if a custom content view has been
     * specified or if any list items have been set.
     *
     * @param contentContainer
     *         The parent view of the the content view as an instance of the clas {@link ViewGroup}
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     * @param messageTextView
     *         The text view, which is used to show the dialog's message, as an instance of the
     *         class {@link TextView}
     * @param dialog
     *         The dialog, whose content should be initialized, as an instance of the class {@link
     *         AlertDialog}
     */
    private void initializeContent(@NonNull final ViewGroup contentContainer,
                                   @NonNull final ViewGroup titleContainer,
                                   @NonNull final TextView messageTextView,
                                   @NonNull final AlertDialog dialog) {
        ListView listView = (ListView) contentContainer.findViewById(android.R.id.list);

        if (listAdapter != null && !listAdapter.isEmpty() && listView != null) {
            if (listAdapter instanceof ArrayAdapter<?>) {
                if (itemColor != -1) {
                    ((ArrayAdapter<?>) listAdapter).setItemColor(itemColor);
                }

                if (itemControlColor != -1) {
                    ((ArrayAdapter<?>) listAdapter).setItemControlColor(itemControlColor);
                }
            }

            showContentContainer(contentContainer, titleContainer, messageTextView);
            listView.setChoiceMode(listViewChoiceMode);
            listView.setAdapter(listAdapter);
            initializeListViewListener(dialog, listView);
            initializeListViewCheckedItems(listView);
        }
    }

    /**
     * Initializes the listener, which should be notified, when the selection of a list item of the
     * dialog has been changed.
     *
     * @param dialog
     *         The dialog, the list items belong to, as an instance of the class {@link
     *         AlertDialog}
     * @param listView
     *         The list view, which is used to show the list items, as an instance of the class
     *         {@link ListView}
     */
    private void initializeListViewListener(@NonNull final AlertDialog dialog,
                                            @NonNull final ListView listView) {
        if (listViewChoiceMode == ListView.CHOICE_MODE_NONE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, dialog,
                            AlertDialog.BUTTON_POSITIVE));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, dialog, 0));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(listViewMultiChoiceListener, dialog, 0));
        }

        if (listViewItemSelectedListener != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
    }

    /**
     * Initializes the list items, which are selected by default.
     *
     * @param listView
     *         The list view, which is used to show the list items, as an instance of the class
     *         {@link ListView}
     */
    private void initializeListViewCheckedItems(@NonNull final ListView listView) {
        if (checkedListItems != null) {
            for (int i = 0; i < checkedListItems.length; i++) {
                listView.setItemChecked(i, checkedListItems[i]);

                if (checkedListItems[i]) {
                    listView.setSelection(i);
                }
            }
        }
    }

    /**
     * Shows the parent view of the dialog's content view.
     *
     * @param contentContainer
     *         The parent view of the the content view as an instance of the clas {@link ViewGroup}
     * @param titleContainer
     *         The parent view of the title view as an instance of the class {@link ViewGroup}
     * @param messageTextView
     *         The text view, which is used to show the dialog's message, as an instance of the
     *         class {@link TextView}
     */
    private void showContentContainer(@NonNull final ViewGroup contentContainer,
                                      @NonNull final ViewGroup titleContainer,
                                      @NonNull final TextView messageTextView) {
        contentContainer.setVisibility(View.VISIBLE);
        int contentSpacing =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_spacing);
        LinearLayout.LayoutParams titleLayoutParams =
                (LayoutParams) titleContainer.getLayoutParams();
        titleLayoutParams.bottomMargin = contentSpacing;
        titleContainer.setLayoutParams(titleLayoutParams);
        LinearLayout.LayoutParams messageLayoutParams =
                (LayoutParams) messageTextView.getLayoutParams();
        messageLayoutParams.bottomMargin = contentSpacing;
        messageTextView.setLayoutParams(messageLayoutParams);
    }

    /**
     * Inflates the button bar, which contains the dialog's buttons.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param dialog
     *         The dialog, the buttons belong to, as an instance of the class {@link AlertDialog}
     */
    private void inflateButtonBar(@NonNull final View root, @NonNull final AlertDialog dialog) {
        ViewGroup buttonBarContainer = (ViewGroup) root.findViewById(R.id.button_bar_container);

        if (stackButtons) {
            View.inflate(context, R.layout.stacked_button_bar, buttonBarContainer);
        } else {
            View.inflate(context, R.layout.horizontal_button_bar, buttonBarContainer);
        }

        initializeButtonBar(root, buttonBarContainer, dialog);
    }

    /**
     * Initializes the button bar, which contains the dialog's buttons.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param buttonBarContainer
     *         The parent view of the button bar, which contains the dialog's buttons, as an
     *         instance of the class {@link ViewGroup}
     * @param dialog
     *         The dialog, the buttons belong to, as an instance of the class {@link AlertDialog}
     */
    private void initializeButtonBar(@NonNull final View root,
                                     @NonNull final ViewGroup buttonBarContainer,
                                     @NonNull final AlertDialog dialog) {
        Button negativeButton = addNegativeButton(buttonBarContainer, dialog);
        Button neutralButton = addNeutralButton(buttonBarContainer, dialog);
        Button positiveButton = addPositiveButton(buttonBarContainer, dialog);

        if (negativeButton != null || neutralButton != null || positiveButton != null) {
            showButtonBarContainer(root, buttonBarContainer);
        }
    }

    /**
     * Shows the parent view of the button bar, which contains the dialog's buttons.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param buttonBarContainer
     *         The parent view of the button bar, which contains the dialog's buttons, as an
     *         instance of the class {@link ViewGroup}
     */
    private void showButtonBarContainer(@NonNull final View root,
                                        @NonNull final View buttonBarContainer) {
        View contentRoot = root.findViewById(R.id.content_root);
        buttonBarContainer.setVisibility(View.VISIBLE);
        int paddingLeft =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_padding_left);
        int paddingTop =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_padding_top);
        int paddingRight =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_padding_right);
        int paddingBottom =
                context.getResources().getDimensionPixelSize(R.dimen.dialog_content_padding_bottom);
        contentRoot.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * Adds a negative button to the dialog, if an appropriate button text has been set before.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param dialog
     *         The dialog, the button should belong to, as an instance of the class {@link
     *         AlertDialog}
     * @return The button, which has been added to the dialog, as an instance of the class {@link
     * Button} or null, if no button has been added
     */
    private Button addNegativeButton(@NonNull final View root, @NonNull final AlertDialog dialog) {
        if (!TextUtils.isEmpty(negativeButtonText)) {
            Button negativeButton = (Button) root.findViewById(android.R.id.button2);
            negativeButton.setText(negativeButtonText.toString().toUpperCase(Locale.getDefault()));
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(negativeButtonListener, null, dialog,
                            AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(onClickListener);
            negativeButton.setVisibility(View.VISIBLE);

            if (buttonTextColor != -1) {
                negativeButton.setTextColor(buttonTextColor);
            }

            return negativeButton;
        }

        return null;
    }

    /**
     * Adds a neutral button to the dialog, if an appropriate button text has been set before.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param dialog
     *         The dialog, the button should belong to, as an instance of the class {@link
     *         AlertDialog}
     * @return The button, which has been added to the dialog, as an instance of the class {@link
     * Button} or null, if no button has been added
     */
    private Button addNeutralButton(@NonNull final View root, @NonNull final AlertDialog dialog) {
        if (!TextUtils.isEmpty(neutralButtonText)) {
            Button neutralButton = (Button) root.findViewById(android.R.id.button3);
            neutralButton.setText(neutralButtonText.toString().toUpperCase(Locale.getDefault()));
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(neutralButtonListener, null, dialog,
                            AlertDialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(onClickListener);
            neutralButton.setVisibility(View.VISIBLE);

            if (buttonTextColor != -1) {
                neutralButton.setTextColor(buttonTextColor);
            }

            return neutralButton;
        }

        return null;
    }

    /**
     * Adds a positive button to the dialog, if an appropriate button text has been set before.
     *
     * @param root
     *         The root view of the dialog's layout as an instance of the class {@link View}
     * @param dialog
     *         The dialog, the button should belong to, as an instance of the class {@link
     *         AlertDialog}
     * @return The button, which has been added to the dialog, as an instance of the class {@link
     * Button} or null, if no button has been added
     */
    private Button addPositiveButton(@NonNull final View root, @NonNull final AlertDialog dialog) {
        if (!TextUtils.isEmpty(positiveButtonText)) {
            Button positiveButton = (Button) root.findViewById(android.R.id.button1);
            positiveButton.setText(positiveButtonText.toString().toUpperCase(Locale.getDefault()));
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(positiveButtonListener, validators, dialog,
                            AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(onClickListener);
            positiveButton.setVisibility(View.VISIBLE);

            if (buttonTextColor != -1) {
                positiveButton.setTextColor(buttonTextColor);
            }

            return positiveButton;
        }

        return null;
    }

    /**
     * Creates a new builder, which allows to create dialogs, which are designed according to
     * Android 5.0's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public MaterialDialogBuilder(@NonNull final Context context) {
        super(context);
        this.context = context;
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Creates a new builder, which alows to create dialogs, which are designed according to Android
     * 5.0's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog. The resource id
     *         must correspond to a valid theme
     */
    public MaterialDialogBuilder(@NonNull final Context context, final int themeResourceId) {
        super(context, themeResourceId);
        this.context = context;
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Sets the color of the title of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setTitleColor(@ColorInt final int color) {
        this.titleColor = color;
        return this;
    }

    /**
     * Sets the color of the message of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setMessageColor(@ColorInt final int color) {
        this.messageColor = color;
        return this;
    }

    /**
     * Sets the color of the list items of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setItemColor(@ColorInt final int color) {
        this.itemColor = color;
        return this;
    }

    /**
     * Sets the control color of the list itemsof the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final MaterialDialogBuilder setItemControlColor(@ColorInt final int color) {
        this.itemControlColor = color;
        return this;
    }

    /**
     * Sets the background of the dialog, which is created by the builder.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no custom background should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setBackground(@Nullable final Drawable background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the background of the dialog, which is created by the builder.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    @SuppressWarnings("deprecation")
    public final MaterialDialogBuilder setBackground(@DrawableRes final int resourceId) {
        this.background = getContext().getResources().getDrawable(resourceId);
        return this;
    }

    /**
     * Sets the background color of the dialog, which is created by the builder.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value or -1, if no
     *         custom background color should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setBackgroundColor(@ColorInt final int color) {
        this.background = color != -1 ? new ColorDrawable(color) : null;
        return this;
    }

    /**
     * Sets the color of the button texts of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom
     *         button color should be set
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder setButtonTextColor(@ColorInt final int color) {
        this.buttonTextColor = color;
        return this;
    }

    /**
     * Sets, whether the buttons of the dialog, which is created by the builder, should be aligned
     * vertically, or not.
     *
     * @param stackButtons
     *         True, if the buttons of the dialog, which is created by the builder, should be
     *         aligned vertically, false otherwise
     * @return The builder, the method has been called upon, as an instance of the class {@link
     * MaterialDialogBuilder}
     */
    public final MaterialDialogBuilder stackButtons(final boolean stackButtons) {
        this.stackButtons = stackButtons;
        return this;
    }

    /**
     * Adds a new validator to the dialog, which should be executed when the positive button is
     * clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link Validator}
     */
    public final void addValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    /**
     * Removes a specific validator from the dialog, which should not be executed when the positive
     * button is clicked, anymore.
     *
     * @param validator
     *         The validator, which should be removed
     */
    public final void removeValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    @Override
    public final MaterialDialogBuilder setTitle(@Nullable final CharSequence title) {
        this.title = title;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setTitle(@StringRes final int resourceId) {
        return setTitle(context.getText(resourceId));
    }

    @Override
    public final MaterialDialogBuilder setMessage(@Nullable final CharSequence message) {
        this.message = message;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setMessage(@StringRes final int resourceId) {
        return setMessage(context.getText(resourceId));
    }

    @Override
    public final MaterialDialogBuilder setIcon(@Nullable final Drawable icon) {
        this.icon = icon;
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public final MaterialDialogBuilder setIcon(@DrawableRes final int resourceId) {
        return setIcon(context.getResources().getDrawable(resourceId));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public final MaterialDialogBuilder setIconAttribute(@AttrRes final int attributeId) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{attributeId});
        return setIcon(typedArray.getDrawable(0));
    }

    @Override
    public final MaterialDialogBuilder setNegativeButton(@Nullable final CharSequence text,
                                                         @Nullable final OnClickListener listener) {
        negativeButtonText = text;
        negativeButtonListener = listener;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setNegativeButton(@StringRes final int resourceId,
                                                         @Nullable final OnClickListener listener) {
        return setNegativeButton(context.getText(resourceId), listener);
    }

    @Override
    public final MaterialDialogBuilder setPositiveButton(@Nullable final CharSequence text,
                                                         @Nullable final OnClickListener listener) {
        positiveButtonText = text;
        positiveButtonListener = listener;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setPositiveButton(@StringRes final int resourceId,
                                                         @Nullable final OnClickListener listener) {
        return setPositiveButton(context.getText(resourceId), listener);
    }

    @Override
    public final MaterialDialogBuilder setNeutralButton(@Nullable final CharSequence text,
                                                        @Nullable final OnClickListener listener) {
        neutralButtonText = text;
        neutralButtonListener = listener;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setNeutralButton(@StringRes final int resourceId,
                                                        @Nullable final OnClickListener listener) {
        return setNeutralButton(context.getText(resourceId), listener);
    }

    @Override
    public final MaterialDialogBuilder setItems(@NonNull final CharSequence[] items,
                                                @Nullable final OnClickListener listener) {
        listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setItems(final int resourceId,
                                                @Nullable final OnClickListener listener) {
        return setItems(context.getResources().getTextArray(resourceId), listener);
    }

    @Override
    public final MaterialDialogBuilder setAdapter(@Nullable final ListAdapter adapter,
                                                  @Nullable final OnClickListener listener) {
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setCursor(@Nullable final Cursor cursor,
                                                 @Nullable final OnClickListener listener,
                                                 @Nullable final String labelColumn) {
        throw new UnsupportedOperationException("This method is not supported yet");
    }

    @Override
    public final MaterialDialogBuilder setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                            final int checkedItem,
                                                            @Nullable final OnClickListener listener) {
        listAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[items.length];

        for (int i = 0; i < checkedListItems.length; i++) {
            checkedListItems[i] = (i == checkedItem);
        }

        return this;
    }

    @Override
    public final MaterialDialogBuilder setSingleChoiceItems(final int resourceId,
                                                            final int checkedItem,
                                                            @Nullable final OnClickListener listener) {
        return setSingleChoiceItems(context.getResources().getTextArray(resourceId), checkedItem,
                listener);
    }

    @Override
    public final MaterialDialogBuilder setSingleChoiceItems(@Nullable final ListAdapter adapter,
                                                            final int checkedItem,
                                                            @Nullable final OnClickListener listener) {
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[adapter != null ? adapter.getCount() : 0];

        for (int i = 0; i < checkedListItems.length; i++) {
            checkedListItems[i] = (i == checkedItem);
        }

        return this;
    }

    @Override
    public final MaterialDialogBuilder setSingleChoiceItems(@Nullable final Cursor cursor,
                                                            final int checkedItem,
                                                            @Nullable final String labelColumn,
                                                            @Nullable final OnClickListener listener) {
        throw new UnsupportedOperationException("This method is not supported yet");
    }

    @Override
    public final MaterialDialogBuilder setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                           @Nullable final boolean[] checkedItems,
                                                           @Nullable final OnMultiChoiceClickListener listener) {
        listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice,
                items);
        listViewMultiChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
        checkedListItems = checkedItems;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setMultiChoiceItems(final int resourceId,
                                                           @Nullable final boolean[] checkedItems,
                                                           @Nullable final OnMultiChoiceClickListener listener) {
        return setMultiChoiceItems(context.getResources().getTextArray(resourceId), checkedItems,
                listener);
    }

    @Override
    public final MaterialDialogBuilder setMultiChoiceItems(@Nullable final Cursor cursor,
                                                           @Nullable final String isCheckedColumn,
                                                           @Nullable final String labelColumn,
                                                           @Nullable final OnMultiChoiceClickListener listener) {
        throw new UnsupportedOperationException("This method is not supported yet");
    }

    @Override
    public final MaterialDialogBuilder setOnItemSelectedListener(
            @Nullable final OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;
        return this;
    }

    @Override
    public final MaterialDialogBuilder setView(@Nullable final View view) {
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
    public final MaterialDialogBuilder setCustomTitle(@Nullable final View view) {
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
        return new MaterialDialog(dialog);
    }

}