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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices. Such a dialog consists of a title, a message and up to three buttons.
 * Furthermore, the dialog can be used to show list items. It is possible to customize the color of
 * the dialog's title and button texts and the title as well as the dialog's content can be replaced
 * with a custom view.
 *
 * For creating or showing such dialogs, the methods {@link MaterialDialog#create()} or {@link
 * MaterialDialog#show()} of the builder {@link MaterialDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.0.0
 */
public class MaterialDialog extends Dialog implements DialogInterface {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices. Such a dialog consists of a
     * title, a message and up to three buttons. Furthermore, the dialog can be used to show list
     * items. It is possible to customize the color of the dialog's title and button texts and the
     * title as well as the dialog's content can be replaced with a custom view.
     */
    public static class Builder {

        /**
         * The context, which is used by the builder.
         */
        private final Context context;

        /**
         * The resource id of the theme, which should be used by the dialog, which is created by the
         * builder.
         */
        private final int themeResourceId;

        /**
         * A set, which contains the validators of the dialog, which is created by the builder.
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
         * The listener, which is notified, when the negative button of the dialog, which is created
         * by the builder, is clicked.
         */
        private OnClickListener negativeButtonListener;

        /**
         * The listener, which is notified, when the neutral button of the dialog, which is created
         * by the builder, is clicked.
         */
        private OnClickListener neutralButtonListener;

        /**
         * The listener, which is notified, when the positive button of the dialog, which is created
         * by the builder, is clicked.
         */
        private OnClickListener positiveButtonListener;

        /**
         * The adapter, which is used to manage the list items of the dialog, which is created by
         * the builder.
         */
        private ListAdapter listAdapter;

        /**
         * The choice mode of the list view, which is used to show the list items of the dialog,
         * which is created by the builder.
         */
        private int listViewChoiceMode;

        /**
         * The listener, which is notified, when the selection of a list item of the dialog, which
         * is created by the builder, changes and the list view's choice mode is
         * <code>ListView.CHOICE_MODE_SINGLE</code>.
         */
        private OnClickListener listViewSingleChoiceListener;

        /**
         * The listener, which is notified, when the selection of a list item of the dialog, which
         * is created by the builder, changes and the list view's choice mode is
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
         * The resource id of the custom content view of the dialog, which is created by the
         * builder.
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

            return root;
        }

        /**
         * Inflates the dialog's title view, which may either be the default view or a custom view,
         * if one has been set before.
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
            View titleView = titleContainer.findViewById(android.R.id.title);

            if (titleView != null && titleView instanceof TextView) {
                TextView titleTextView = (TextView) titleView;

                if (titleColor != -1) {
                    titleTextView.setTextColor(titleColor);
                }

                if (!TextUtils.isEmpty(title) || icon != null) {
                    titleContainer.setVisibility(View.VISIBLE);
                    titleTextView.setText(title);

                    if (icon != null) {
                        titleTextView
                                .setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
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
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
            layoutParams.bottomMargin =
                    context.getResources().getDimensionPixelSize(R.dimen.dialog_content_spacing);
            titleContainer.setLayoutParams(layoutParams);
        }

        /**
         * Inflates the dialog's content view, which may either be the default view or a custom
         * view, if one has been set before.
         *
         * @param root
         *         The root view of the dialog's layout as an instance of the class {@link View}
         * @param titleContainer
         *         The parent view of the title view as an instance of the class {@link ViewGroup}
         * @param messageTextView
         *         The text view, which is used to show the dialog's message, as an instance of the
         *         class {@link TextView}
         * @param dialog
         *         The dialog, whose content view should be inflated, as an instance of the class
         *         {@link MaterialDialog}
         */
        private void inflateContentView(@NonNull final View root,
                                        @NonNull final ViewGroup titleContainer,
                                        @NonNull final TextView messageTextView,
                                        @NonNull final MaterialDialog dialog) {
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
         * Shows the parent view of the dialog's content view.
         *
         * @param contentContainer
         *         The parent view of the the content view as an instance of the clas {@link
         *         ViewGroup}
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
                    (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
            titleLayoutParams.bottomMargin = contentSpacing;
            titleContainer.setLayoutParams(titleLayoutParams);
            LinearLayout.LayoutParams messageLayoutParams =
                    (LinearLayout.LayoutParams) messageTextView.getLayoutParams();
            messageLayoutParams.bottomMargin = contentSpacing;
            messageTextView.setLayoutParams(messageLayoutParams);
        }

        /**
         * Initializes the dialog's content. The content view is shown if a custom content view has
         * been specified or if any list items have been set.
         *
         * @param contentContainer
         *         The parent view of the the content view as an instance of the clas {@link
         *         ViewGroup}
         * @param titleContainer
         *         The parent view of the title view as an instance of the class {@link ViewGroup}
         * @param messageTextView
         *         The text view, which is used to show the dialog's message, as an instance of the
         *         class {@link TextView}
         * @param dialog
         *         The dialog, whose content should be initialized, as an instance of the class
         *         {@link MaterialDialog}
         */
        private void initializeContent(@NonNull final ViewGroup contentContainer,
                                       @NonNull final ViewGroup titleContainer,
                                       @NonNull final TextView messageTextView,
                                       @NonNull final MaterialDialog dialog) {
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
                listView.setVisibility(View.VISIBLE);
                listView.setChoiceMode(listViewChoiceMode);
                listView.setAdapter(listAdapter);
                initializeListViewListener(dialog, listView);
                initializeListViewCheckedItems(listView);
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
         * Initializes the listener, which should be notified, when the selection of a list item of
         * the dialog has been changed.
         *
         * @param dialog
         *         The dialog, the list items belong to, as an instance of the class {@link
         *         MaterialDialog}
         * @param listView
         *         The list view, which is used to show the list items, as an instance of the class
         *         {@link ListView}
         */
        private void initializeListViewListener(@NonNull final MaterialDialog dialog,
                                                @NonNull final ListView listView) {
            if (listViewChoiceMode == ListView.CHOICE_MODE_NONE) {
                listView.setOnItemClickListener(
                        new OnItemClickListenerWrapper(listViewSingleChoiceListener, dialog,
                                DialogInterface.BUTTON_POSITIVE));
            } else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
                listView.setOnItemClickListener(
                        new OnItemClickListenerWrapper(listViewSingleChoiceListener, dialog, 0));
            } else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
                listView.setOnItemClickListener(
                        new OnMultiChoiceClickListenerWrapper(listViewMultiChoiceListener, dialog,
                                0));
            }

            if (listViewItemSelectedListener != null) {
                listView.setOnItemSelectedListener(listViewItemSelectedListener);
            }
        }

        /**
         * Inflates the button bar, which contains the dialog's buttons.
         *
         * @param root
         *         The root view of the dialog's layout as an instance of the class {@link View}
         * @param dialog
         *         The dialog, the buttons belong to, as an instance of the class {@link
         *         MaterialDialog}
         */
        private void inflateButtonBar(@NonNull final View root,
                                      @NonNull final MaterialDialog dialog) {
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
         *         The dialog, the buttons belong to, as an instance of the class {@link
         *         MaterialDialog}
         */
        private void initializeButtonBar(@NonNull final View root,
                                         @NonNull final ViewGroup buttonBarContainer,
                                         @NonNull final MaterialDialog dialog) {
            Button negativeButton = addNegativeButton(buttonBarContainer, dialog);
            Button neutralButton = addNeutralButton(buttonBarContainer, dialog);
            Button positiveButton = addPositiveButton(buttonBarContainer, dialog);

            if (negativeButton != null || neutralButton != null || positiveButton != null) {
                showButtonBarContainer(root, buttonBarContainer);
            }
        }

        /**
         * Adds a negative button to the dialog, if an appropriate button text has been set before.
         *
         * @param root
         *         The root view of the dialog's layout as an instance of the class {@link View}
         * @param dialog
         *         The dialog, the button should belong to, as an instance of the class {@link
         *         MaterialDialog}
         * @return The button, which has been added to the dialog, as an instance of the class
         * {@link Button} or null, if no button has been added
         */
        private Button addNegativeButton(@NonNull final View root,
                                         @NonNull final MaterialDialog dialog) {
            if (!TextUtils.isEmpty(negativeButtonText)) {
                Button negativeButton = (Button) root.findViewById(android.R.id.button2);
                negativeButton
                        .setText(negativeButtonText.toString().toUpperCase(Locale.getDefault()));
                OnClickListenerWrapper onClickListener =
                        new OnClickListenerWrapper(negativeButtonListener, false, dialog,
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
         *         MaterialDialog}
         * @return The button, which has been added to the dialog, as an instance of the class
         * {@link Button} or null, if no button has been added
         */
        private Button addNeutralButton(@NonNull final View root,
                                        @NonNull final MaterialDialog dialog) {
            if (!TextUtils.isEmpty(neutralButtonText)) {
                Button neutralButton = (Button) root.findViewById(android.R.id.button3);
                neutralButton
                        .setText(neutralButtonText.toString().toUpperCase(Locale.getDefault()));
                OnClickListenerWrapper onClickListener =
                        new OnClickListenerWrapper(neutralButtonListener, false, dialog,
                                DialogInterface.BUTTON_NEUTRAL);
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
         *         MaterialDialog}
         * @return The button, which has been added to the dialog, as an instance of the class
         * {@link Button} or null, if no button has been added
         */
        private Button addPositiveButton(@NonNull final View root,
                                         @NonNull final MaterialDialog dialog) {
            if (!TextUtils.isEmpty(positiveButtonText)) {
                Button positiveButton = (Button) root.findViewById(android.R.id.button1);
                positiveButton
                        .setText(positiveButtonText.toString().toUpperCase(Locale.getDefault()));
                dialog.addAllValidators(validators);
                OnClickListenerWrapper onClickListener =
                        new OnClickListenerWrapper(positiveButtonListener, true, dialog,
                                DialogInterface.BUTTON_POSITIVE);
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
            int paddingLeft = context.getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_left);
            int paddingTop = context.getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_top);
            int paddingRight = context.getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_right);
            int paddingBottom = context.getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_bottom);
            contentRoot.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            this.context = context;
            this.themeResourceId = -1;
            this.validators = new LinkedHashSet<>();
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            this.context = context;
            this.themeResourceId = themeResourceId;
            this.validators = new LinkedHashSet<>();
        }

        /**
         * Returns the context, which is used by the builder.
         *
         * @return The context, which is used by the builder, as an instance of the class {@link
         * Context}
         */
        public final Context getContext() {
            return context;
        }

        /**
         * Sets the color of the title of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitleColor(@ColorInt final int color) {
            this.titleColor = color;
            return this;
        }

        /**
         * Sets the color of the message of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessageColor(@ColorInt final int color) {
            this.messageColor = color;
            return this;
        }

        /**
         * Sets the color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItemColor(@ColorInt final int color) {
            this.itemColor = color;
            return this;
        }

        /**
         * Sets the control color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public final Builder setItemControlColor(@ColorInt final int color) {
            this.itemControlColor = color;
            return this;
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param background
         *         The background, which should be set, as an instance of the class {@link Drawable}
         *         or null, if no custom background should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackground(@Nullable final Drawable background) {
            this.background = background;
            return this;
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the background, which should be set, as an {@link Integer}
         *         value. The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        @SuppressWarnings("deprecation")
        public final Builder setBackground(@DrawableRes final int resourceId) {
            this.background = context.getResources().getDrawable(resourceId);
            return this;
        }

        /**
         * Sets the background color of the dialog, which is created by the builder.
         *
         * @param color
         *         The background color, which should be set, as an {@link Integer} value or -1, if
         *         no custom background color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackgroundColor(@ColorInt final int color) {
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
         * Builder}
         */
        public final Builder setButtonTextColor(@ColorInt final int color) {
            this.buttonTextColor = color;
            return this;
        }

        /**
         * Sets, whether the buttons of the dialog, which is created by the builder, should be
         * aligned vertically, or not.
         *
         * @param stackButtons
         *         True, if the buttons of the dialog, which is created by the builder, should be
         *         aligned vertically, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder stackButtons(final boolean stackButtons) {
            this.stackButtons = stackButtons;
            return this;
        }

        /**
         * Adds a new validator, which should be executed when the positive button of the dialog,
         * which is created by the builder, is clicked.
         *
         * @param validator
         *         The validator, which should be added, as an instance of the type {@link
         *         Validator}. The validator may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addValidator(@NonNull final Validator validator) {
            ensureNotNull(validator, "The validator may not be null");
            validators.add(validator);
            return this;
        }

        /**
         * Adds all validators, which are contained by a specific collection and should be executed
         * when the positive button of the dialog, which is created by the builder, is clicked.
         *
         * @param validators
         *         A collection, which contains all validators, which should be added, as an
         *         instance of the type {@link Collection} or an empty collection, if no validators
         *         should be added
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addAllValidators(@NonNull final Collection<Validator> validators) {
            ensureNotNull(validators, "The collection may not be null");
            this.validators.addAll(validators);
            return this;
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param title
         *         The title, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no title should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitle(@Nullable final CharSequence title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitle(@StringRes final int resourceId) {
            return setTitle(context.getText(resourceId));
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param message
         *         The message, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no message should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessage(@Nullable final CharSequence message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the message, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessage(@StringRes final int resourceId) {
            return setMessage(context.getText(resourceId));
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param icon
         *         The icon, which should be set, as an instance of the class {@link Drawable} or
         *         null, if no icon should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setIcon(@Nullable final Drawable icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the icon, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        @SuppressWarnings("deprecation")
        public final Builder setIcon(@DrawableRes final int resourceId) {
            return setIcon(context.getResources().getDrawable(resourceId));
        }

        /**
         * Set the icon of the dialog, which is created by the builder.
         *
         * @param attributeId
         *         The id of the theme attribute, which supplies the icon, which should be set, as
         *         an {@link Integer} value. The id must point to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setIconAttribute(@AttrRes final int attributeId) {
            TypedArray typedArray =
                    context.getTheme().obtainStyledAttributes(new int[]{attributeId});
            return setIcon(typedArray.getDrawable(0));
        }

        /**
         * Sets the text of the negative button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no negative button should be shown
         * @param listener
         *         The listener, which should be notified, when the negative button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNegativeButton(@Nullable final CharSequence text,
                                               @Nullable final OnClickListener listener) {
            negativeButtonText = text;
            negativeButtonListener = listener;
            return this;
        }

        /**
         * Sets the text of the negative button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the negative button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNegativeButton(@StringRes final int resourceId,
                                               @Nullable final OnClickListener listener) {
            return setNegativeButton(context.getText(resourceId), listener);
        }

        /**
         * Sets the text of the positive button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no positive button should be shown
         * @param listener
         *         The listener, which should be notified, when the positive button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setPositiveButton(@Nullable final CharSequence text,
                                               @Nullable final OnClickListener listener) {
            positiveButtonText = text;
            positiveButtonListener = listener;
            return this;
        }

        /**
         * Sets the text of the positive button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the positive button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setPositiveButton(@StringRes final int resourceId,
                                               @Nullable final OnClickListener listener) {
            return setPositiveButton(context.getText(resourceId), listener);
        }

        /**
         * Sets the text of the neutral button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no neutral button should be shown
         * @param listener
         *         The listener, which should be notified, when the neutral button is clicked, as an
         *         instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNeutralButton(@Nullable final CharSequence text,
                                              @Nullable final OnClickListener listener) {
            neutralButtonText = text;
            neutralButtonListener = listener;
            return this;
        }

        /**
         * Sets the text of the neutral button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the neutral button is clicked, as an
         *         instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNeutralButton(@StringRes final int resourceId,
                                              @Nullable final OnClickListener listener) {
            return setNeutralButton(context.getText(resourceId), listener);
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItems(@NonNull final CharSequence[] items,
                                      @Nullable final OnClickListener listener) {
            listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items);
            listViewSingleChoiceListener = listener;
            listViewChoiceMode = ListView.CHOICE_MODE_NONE;
            return this;
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItems(final int resourceId,
                                      @Nullable final OnClickListener listener) {
            return setItems(context.getResources().getTextArray(resourceId), listener);
        }

        /**
         * Sets the adapter, which provides the items, which should be shown by the dialog, which is
         * created by the builder.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}
         *         or null, if no items should be shown by the dialog
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setAdapter(@Nullable final ListAdapter adapter,
                                        @Nullable final OnClickListener listener) {
            listAdapter = adapter;
            listViewSingleChoiceListener = listener;
            listViewChoiceMode = ListView.CHOICE_MODE_NONE;
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                  final int checkedItem,
                                                  @Nullable final OnClickListener listener) {
            listAdapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice,
                            items);
            listViewSingleChoiceListener = listener;
            listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
            checkedListItems = new boolean[items.length];

            for (int i = 0; i < checkedListItems.length; i++) {
                checkedListItems[i] = (i == checkedItem);
            }

            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(final int resourceId, final int checkedItem,
                                                  @Nullable final OnClickListener listener) {
            return setSingleChoiceItems(context.getResources().getTextArray(resourceId),
                    checkedItem, listener);
        }

        /**
         * Sets the adapter, which provides the selectable items, which should be shown by the
         * dialog, which is created by the builder. Only one of the items can be selected at once.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(@Nullable final ListAdapter adapter,
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

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final OnMultiChoiceClickListener listener) {
            listAdapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice,
                            items);
            listViewMultiChoiceListener = listener;
            listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
            checkedListItems = checkedItems;
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMultiChoiceItems(final int resourceId,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final OnMultiChoiceClickListener listener) {
            return setMultiChoiceItems(context.getResources().getTextArray(resourceId),
                    checkedItems, listener);
        }

        /**
         * Sets the listener, which should be notified, when an item, which is shown by the dialog,
         * which is created by the builder, is selected.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnItemSelectedListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setOnItemSelectedListener(
                @Nullable final OnItemSelectedListener listener) {
            listViewItemSelectedListener = listener;
            return this;
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setView(@Nullable final View view) {
            customView = view;
            customViewId = 0;
            return this;
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setView(@LayoutRes final int resourceId) {
            customViewId = resourceId;
            customView = null;
            return this;
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the title
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setCustomTitle(@Nullable final View view) {
            customTitleView = view;
            return this;
        }

        /**
         * Creates a dialog with the arguments, which have been suppplied to the builder. Calling
         * this method does not display the dialog.
         *
         * @return The dialog, which has been create as an instance of the class {@link
         * MaterialDialog}
         */
        public final MaterialDialog create() {
            View root = inflateLayout();
            int themeId = themeResourceId;

            if (themeResourceId == -1) {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
                themeId = typedValue.resourceId;
            }

            MaterialDialog dialog = new MaterialDialog(context, themeId);
            ViewGroup titleContainer = inflateTitleView(root);
            TextView messageTextView = initializeMessage(root, titleContainer);
            inflateContentView(root, titleContainer, messageTextView, dialog);
            inflateButtonBar(root, dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(root);
            return dialog;
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the class {@link
         * MaterialDialog}
         */
        public final MaterialDialog show() {
            MaterialDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }

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
     * A set, which contains the validators of the dialog.
     */
    private final Set<Validator> validators;

    /**
     * The root view of the dialog.
     */
    private ViewGroup rootView;

    /**
     * The positive button of the dialog.
     */
    private Button positiveButton;

    /**
     * The negative button of the dialog.
     */
    private Button negativeButton;

    /**
     * The neutral button of the dialog.
     */
    private Button neutralButton;

    /**
     * The list view of the dialog.
     */
    private ListView listView;

    /**
     * The parent view of the text view, which is used to show the title of the dialog.
     */
    private ViewGroup titleContainer;

    /**
     * The text view, which is used to show the title of the dialog.
     */
    private TextView titleTextView;

    /**
     * The text view, which is used to show the message of the dialog.
     */
    private TextView messageTextView;

    /**
     * The parent view of the custom view, which is shown by the dialog.
     */
    private ViewGroup contentContainer;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public MaterialDialog(@NonNull final Context context) {
        super(context);
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected MaterialDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Returns one of the buttons, which is shown by the dialog.
     *
     * @param whichButton
     *         The identifier of the button, which should be returned, as an {@link Integer} value.
     *         The identifier must either be {@link DialogInterface#BUTTON_POSITIVE}, {@link
     *         DialogInterface#BUTTON_NEGATIVE} or {@link DialogInterface#BUTTON_NEUTRAL}
     * @return The button, which corresponds to the given identifier, as an instance of the class
     * {@link Button} or null, if the button is not shown or if the dialog has not been shown yet
     */
    public final Button getButton(final int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return (positiveButton != null && positiveButton.getVisibility() == View.VISIBLE) ?
                        positiveButton : null;
            case DialogInterface.BUTTON_NEGATIVE:
                return (negativeButton != null && negativeButton.getVisibility() == View.VISIBLE) ?
                        negativeButton : null;
            case DialogInterface.BUTTON_NEUTRAL:
                return (neutralButton != null && neutralButton.getVisibility() == View.VISIBLE) ?
                        neutralButton : null;
            default:
                return null;
        }
    }

    /**
     * Sets the text of the positive button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no positive button should be shown
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setPositiveButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        if (positiveButton != null) {
            positiveButton.setText(
                    text != null ? text.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(listener, true, this,
                            DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(onClickListener);
            positiveButton.setVisibility(!TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Sets the text of the positive button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setPositiveButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        setPositiveButton(getContext().getText(resourceId), listener);
    }

    /**
     * Sets the text of the negative button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no negative button should be shown
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNegativeButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        if (negativeButton != null) {
            negativeButton.setText(
                    text != null ? text.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(listener, false, this,
                            DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(onClickListener);
            negativeButton.setVisibility(!TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Sets the text of the negative button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNegativeButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        setNegativeButton(getContext().getText(resourceId), listener);
    }

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no neutral button should be shown
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNeutralButton(@Nullable final CharSequence text,
                                       @Nullable final OnClickListener listener) {
        if (neutralButton != null) {
            neutralButton.setText(
                    text != null ? text.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(listener, false, this,
                            DialogInterface.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(onClickListener);
            neutralButton.setVisibility(!TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNeutralButton(@StringRes final int resourceId,
                                       @Nullable final OnClickListener listener) {
        setNeutralButton(getContext().getText(resourceId), listener);
    }

    /**
     * Returns a set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked.
     *
     * @return A set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked, as an instance of the type {@link Set} or an empty set, if no
     * validators are executed
     */
    public final Set<Validator> getValidators() {
        return validators;
    }

    /**
     * Adds a new validator, which should be executed when the positive button of the dialog is
     * clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link Validator}.
     *         The validator may not be null
     */
    public final void addValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    /**
     * Adds all validators, which are contained by a specific collection and should be executed when
     * the positive button of the dialog is clicked.
     *
     * @param validators
     *         A collection, which contains all validators, which should be added, as an instance of
     *         the type {@link Collection} or an empty collection, if no validators should be added
     */
    public final void addAllValidators(@NonNull final Collection<Validator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.addAll(validators);
    }

    /**
     * Removes a specific validator, which should not be executed, when the positive button of the
     * dialog is clicked, anymore.
     *
     * @param validator
     *         The validator, which should be removed, as an instance of the type {@link Validator}.
     *         The validator may not be null
     */
    public final void removeValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    /**
     * Removes all validators, which are contained by a specific collection and should not be
     * executed, when the positive button of the dialog is clicked, anymore.
     *
     * @param validators
     *         A collection, which contains the validators, which should be removed, as an instance
     *         of the type {@link Collection} or an empty collection, if no validators should be
     *         removed
     */
    public final void removeAllValidators(@NonNull final Collection<Validator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.removeAll(validators);
    }

    /**
     * Returns the list view, which is contained by the dialog.
     *
     * @return The list view, which is contained by the dialog, as an instance of the class {@link
     * ListView} or null, if the dialog does not show any list items or has not been shown yet
     */
    public final ListView getListView() {
        return (listView != null && listView.getVisibility() == View.VISIBLE) ? listView : null;
    }

    /**
     * Returns the adapter of the list view, which is contained by the dialog.
     *
     * @return The adapter of the list view, which is contained by the dialog, as an instance of the
     * type {@link ListAdapter} or null, if the dialog does not show any list items or has not been
     * shown yet
     */
    public final ListAdapter getListAdapter() {
        return getListView() != null ? getListView().getAdapter() : null;
    }

    /**
     * Returns the icon of the dialog.
     *
     * @return The icon of the dialog, as an instance of the class {@link Drawable} or null, if no
     * icon is shown or if the dialog has not been shown yet
     */
    public final Drawable getIcon() {
        return titleTextView != null ? titleTextView.getCompoundDrawables()[0] : null;
    }

    /**
     * Sets the icon of the dialog.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be shown
     */
    public final void setIcon(final Drawable icon) {
        if (titleTextView != null) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        }

        if (titleContainer != null) {
            titleContainer.setVisibility(
                    (!TextUtils.isEmpty(getTitle()) || getIcon() != null) ? View.VISIBLE :
                            View.GONE);
        }
    }

    /**
     * Sets the icon of the dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    @SuppressWarnings("deprecation")
    public final void setIcon(@DrawableRes final int resourceId) {
        setIcon(getContext().getResources().getDrawable(resourceId));
    }

    /**
     * Set the icon of the dialog.
     *
     * @param attributeId
     *         The id of the theme attribute, which supplies the icon, which should be set, as an
     *         {@link Integer} value. The id must point to a valid drawable resource
     */
    public final void setIconAttribute(@AttrRes final int attributeId) {
        TypedArray typedArray =
                getContext().getTheme().obtainStyledAttributes(new int[]{attributeId});
        setIcon(typedArray.getDrawable(0));
    }

    /**
     * Returns the color of the title of the dialog.
     *
     * @return The color of the title of the dialog as an {@link Integer} value or -1, if no title
     * is shown or if the dialog has not been shown yet
     */
    public final int getTitleColor() {
        return !TextUtils.isEmpty(getTitle()) ? titleTextView.getCurrentTextColor() : -1;
    }

    /**
     * Sets the color of the title of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setTitleColor(@ColorInt final int color) {
        if (titleTextView != null) {
            titleTextView.setTextColor(color);
        }
    }

    /**
     * Returns the color of the message of the dialog.
     *
     * @return The color of the message of the dialog as an {@link Integer} value or -1, if no
     * message is shown or if the dialog has not been shown yet
     */
    public final int getMessageColor() {
        return !TextUtils.isEmpty(getMessage()) ? messageTextView.getCurrentTextColor() : -1;
    }

    /**
     * Sets the color of the message of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setMessageColor(@ColorInt final int color) {
        if (messageTextView != null) {
            messageTextView.setTextColor(color);
        }
    }

    /**
     * Returns the color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * list items are shown or if the dialog has not been shown yet
     */
    public final int getItemColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter != null && listAdapter instanceof ArrayAdapter<?>) {
            return ((ArrayAdapter<?>) listAdapter).getItemColor();
        }

        return -1;
    }

    /**
     * Sets the color of the list items of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setItemColor(@ColorInt final int color) {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter != null && listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemColor(color);
        }
    }

    /**
     * Returns the control color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value
     */
    public final int getItemControlColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter != null && listAdapter instanceof ArrayAdapter<?>) {
            return ((ArrayAdapter<?>) listAdapter).getItemControlColor();
        }

        return -1;
    }

    /**
     * Sets the control color of the list items of the dialog, which is created by the builder.
     *
     * @param color
     *         The control color, which should be set, as an {@link Integer} value or -1, if no list
     *         items are shown or if the dialog has not been shown yet
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter != null && listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemControlColor(color);
        }
    }

    /**
     * Returns the background of the dialog.
     *
     * @return The background of the dialog as an instance of the class {@link Drawable} or null, if
     * no custom background is set or if the dialog has not been shown yet
     */
    public final Drawable getBackground() {
        return rootView != null ? rootView.getBackground() : null;
    }

    /**
     * Sets the background of the dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no custom background should be set
     */
    @SuppressWarnings("deprecation")
    public final void setBackground(@Nullable final Drawable background) {
        if (rootView != null) {
            rootView.setBackgroundDrawable(background);
        }
    }

    /**
     * Sets the background of the dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    @SuppressWarnings("deprecation")
    public final void setBackground(@DrawableRes final int resourceId) {
        setBackground(getContext().getResources().getDrawable(resourceId));
    }

    /**
     * Sets the background color of the dialog.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value or -1, if no
     *         custom background color should be set
     */
    public final void setBackgroundColor(@ColorInt final int color) {
        setBackground(color != -1 ? new ColorDrawable(color) : null);
    }

    /**
     * Returns the color of the button texts of the dialog.
     *
     * @return The color of the button texts of the dialog as an {@link Integer} value or -1, if no
     * button is shown or if the dialog has not been shown yet
     */
    public final int getButtonTextColor() {
        return (getButton(DialogInterface.BUTTON_POSITIVE) != null ||
                getButton(DialogInterface.BUTTON_NEUTRAL) != null ||
                getButton(DialogInterface.BUTTON_NEGATIVE) != null) ?
                positiveButton.getCurrentTextColor() : -1;
    }

    /**
     * Sets the color of the button texts of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setButtonTextColor(@ColorInt final int color) {
        if (positiveButton != null) {
            positiveButton.setTextColor(color);
        }

        if (neutralButton != null) {
            neutralButton.setTextColor(color);
        }

        if (negativeButton != null) {
            negativeButton.setTextColor(color);
        }
    }

    /**
     * Sets the custom view, which should be used to show the title of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    public final void setCustomTitle(@Nullable final View view) {
        if (titleContainer != null) {
            CharSequence title = getTitle();
            Drawable icon = getIcon();
            int titleColor = getTitleColor();
            titleContainer.removeAllViews();

            if (view != null) {
                titleContainer.setVisibility(View.VISIBLE);
                titleContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                View.inflate(getContext(), R.layout.material_dialog_title, titleContainer);
            }

            View titleView = titleContainer.findViewById(android.R.id.title);

            if (titleView != null && titleView instanceof TextView) {
                titleTextView = (TextView) titleView;
                titleTextView.setTextColor(titleColor);

                if (!TextUtils.isEmpty(title) || icon != null) {
                    titleContainer.setVisibility(View.VISIBLE);
                    titleTextView.setText(title);

                    if (icon != null) {
                        titleTextView
                                .setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                    }

                }
            }
        }
    }

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be shown
     */
    public final void setView(@Nullable final View view) {
        if (contentContainer != null) {
            contentContainer.removeAllViews();
            LinearLayout.LayoutParams titleLayoutParams =
                    (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
            LinearLayout.LayoutParams messageLayoutParams =
                    (LinearLayout.LayoutParams) messageTextView.getLayoutParams();
            int contentSpacing = getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_spacing);

            if (view != null) {
                contentContainer.setVisibility(View.VISIBLE);
                titleLayoutParams.bottomMargin = contentSpacing;
                messageLayoutParams.bottomMargin = contentSpacing;
                contentContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                contentContainer.setVisibility(View.GONE);
                titleLayoutParams.bottomMargin =
                        !TextUtils.isEmpty(getMessage()) ? contentSpacing : 0;
                messageLayoutParams.bottomMargin = 0;
            }

            titleContainer.setLayoutParams(titleLayoutParams);
            messageTextView.setLayoutParams(messageLayoutParams);
        }
    }

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    public final void setView(@LayoutRes final int resourceId) {
        setView(View.inflate(getContext(), resourceId, null));
    }

    /**
     * Returns the message of the dialog.
     *
     * @return The message of the dialog as an instance of the type {@link CharSequence} or null, if
     * no message is shown or if the dialog has not been shown yet
     */
    public final CharSequence getMessage() {
        return (messageTextView != null && messageTextView.getVisibility() == View.VISIBLE) ?
                messageTextView.getText() : null;
    }

    /**
     * Sets the message of the dialog.
     *
     * @param message
     *         The message, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no message should be shown
     */
    public final void setMessage(@Nullable final CharSequence message) {
        if (messageTextView != null) {
            messageTextView.setText(message);
            messageTextView
                    .setVisibility(!TextUtils.isEmpty(getMessage()) ? View.VISIBLE : View.GONE);
        }

        if (titleContainer != null) {
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
            layoutParams.bottomMargin = !TextUtils.isEmpty(getMessage()) ?
                    getContext().getResources()
                            .getDimensionPixelSize(R.dimen.dialog_content_spacing) : 0;
            titleContainer.setLayoutParams(layoutParams);
        }
    }

    /**
     * Sets the message of the dialog.
     *
     * @param resourceId
     *         The resource id of the message, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setMessage(@StringRes final int resourceId) {
        setMessage(getContext().getText(resourceId));
    }

    /**
     * Returns the title of the dialog.
     *
     * @return The title of the dialog as an instance of the type {@link CharSequence} or null, if
     * no title is shown or if the dialog has not been shown yet
     */
    public final CharSequence getTitle() {
        return titleTextView != null ? titleTextView.getText() : null;
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        super.setTitle(title);

        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        if (titleContainer != null) {
            titleContainer.setVisibility(
                    (!TextUtils.isEmpty(getTitle()) || getIcon() != null) ? View.VISIBLE :
                            View.GONE);
        }
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup) findViewById(R.id.root);
        positiveButton = (Button) findViewById(android.R.id.button1);
        negativeButton = (Button) findViewById(android.R.id.button2);
        neutralButton = (Button) findViewById(android.R.id.button3);
        listView = (ListView) findViewById(android.R.id.list);
        titleContainer = (ViewGroup) findViewById(R.id.title_container);
        View titleView = findViewById(android.R.id.title);
        titleTextView =
                (titleView != null && titleView instanceof TextView) ? (TextView) titleView : null;
        messageTextView = (TextView) findViewById(android.R.id.message);
        contentContainer = (ViewGroup) findViewById(R.id.content_container);
    }

}