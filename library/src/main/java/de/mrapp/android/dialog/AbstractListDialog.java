/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and may contain list items.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractListDialog extends AbstractButtonBarDialog {

    /**
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
     * contain list items.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    protected static abstract class Builder<DialogType extends AbstractListDialog, BuilderType extends Builder<DialogType, ?>>
            extends AbstractButtonBarDialog.Builder<DialogType, BuilderType> {

        /**
         * Obtains the item color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainItemColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemColor(color);
            }
        }

        /**
         * Obtains the item control color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item control color should be obtained from, as
         *         an {@link Integer} value
         */
        private void obtainItemControlColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemControlColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemControlColor(color);
            }
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain list items.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain list items.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Sets the color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setItemColor(@ColorInt final int color) {
            getDialog().setItemColor(color);
            return self();
        }

        /**
         * Sets the control color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public final BuilderType setItemControlColor(@ColorInt final int color) {
            getDialog().setItemControlColor(color);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setItems(@NonNull final CharSequence[] items,
                                          @Nullable final OnClickListener listener) {
            getDialog().setItems(items, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final Builder setItems(@ArrayRes final int resourceId,
                                      @Nullable final OnClickListener listener) {
            getDialog().setItems(resourceId, listener);
            return self();
        }

        /**
         * Sets the adapter, which provides the items, which should be shown by the dialog, which is
         * created by the builder.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setAdapter(@NonNull final ListAdapter adapter,
                                            @Nullable final OnClickListener listener) {
            getDialog().setAdapter(adapter, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(items, checkedItem, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@ArrayRes final int resourceId,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(resourceId, checkedItem, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(adapter, checkedItem, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                     @Nullable final boolean[] checkedItems,
                                                     @Nullable final OnMultiChoiceClickListener listener) {
            getDialog().setMultiChoiceItems(items, checkedItems, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMultiChoiceItems(@ArrayRes final int resourceId,
                                                     @Nullable final boolean[] checkedItems,
                                                     @Nullable final OnMultiChoiceClickListener listener) {
            getDialog().setMultiChoiceItems(resourceId, checkedItems, listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when an item, which is shown by the dialog,
         * which is created by the builder, is selected.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         AdapterView.OnItemSelectedListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnItemSelectedListener(
                @Nullable final AdapterView.OnItemSelectedListener listener) {
            getDialog().setOnItemSelectedListener(listener);
            return self();
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainItemColor(themeResourceId);
            obtainItemControlColor(themeResourceId);
        }

    }

    /**
     * The list view, which is used to show the dialog's list items.
     */
    private ListView listView;

    /**
     * The color of the list items of the dialog.
     */
    private int itemColor = -1;

    /**
     * The control color of the list items of the dialog.
     */
    private int itemControlColor = -1;

    /**
     * The adapter, which is used to manage the list items of the dialog.
     */
    private ListAdapter listAdapter;

    /**
     * The choice mode of the list view, which is used to show the list items of the dialog.
     */
    private int listViewChoiceMode = -1;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_SINGLE</code>.
     */
    private OnClickListener listViewSingleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private OnMultiChoiceClickListener listViewMultiChoiceListener;

    /**
     * The listener, which is notified, when a list item of the dialog becomes selected,
     * irrespective of the list view's choice mode.
     */
    private AdapterView.OnItemSelectedListener listViewItemSelectedListener;

    /**
     * An array, which is used to identify the list items of the dialog, which is created by the
     * builder, which are by selected by default.
     */
    private boolean[] checkedListItems;

    /**
     * Inflates the list view, which is used to show the dialog's list items.
     *
     * @param contentContainer
     *         The parent view of the layout, which is used to show the dialog's content, as an
     *         instance of the class {@link ViewGroup}
     */
    private void inflateListView(@NonNull final ViewGroup contentContainer) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        listView = (ListView) layoutInflater
                .inflate(R.layout.material_dialog_list_view, contentContainer, false);
    }

    /**
     * Shows the list view, which is used to show the dialog's list items.
     *
     * @param contentContainer
     *         The parent view of the layout, which is used to show the dialog's content, as an
     *         instance of the class {@link ViewGroup}
     */
    private void showListView(@NonNull final ViewGroup contentContainer) {
        if (listAdapter != null && !listAdapter.isEmpty() && listView != null) {
            listView.setVisibility(View.VISIBLE);
            listView.setChoiceMode(listViewChoiceMode);
            listView.setAdapter(listAdapter);
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
            initializeListViewSelectionListener();
            initializeListViewCheckedItems();
        }
    }

    /**
     * Initializes the list items, which are selected by default.
     */
    private void initializeListViewCheckedItems() {
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
     * Initializes the listener, which should be notified, when the selection of a list item of the
     * dialog has been changed.
     */
    private void initializeListViewSelectionListener() {
        if (listViewChoiceMode == ListView.CHOICE_MODE_NONE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, this,
                            DialogInterface.BUTTON_POSITIVE));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, this, 0));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(listViewMultiChoiceListener, this, 0));
        }
    }

    /**
     * Adapts the color of the dialog's list items.
     */
    private void adaptItemColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemColor(itemColor);
        }
    }

    /**
     * Adapts the control color of the dialog's list items.
     */
    private void adaptItemControlColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemControlColor(itemControlColor);
        }
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contains list items.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractListDialog(@NonNull final Context context,
                                 @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Returns the list view, which is contained by the dialog.
     *
     * @return The list view, which is contained by the dialog, as an instance of the class {@link
     * ListView} or null, if the dialog does not show any list items or has not been shown yet
     */
    public final ListView getListView() {
        return listView != null && listView.getVisibility() == View.VISIBLE ? listView : null;
    }

    /**
     * Returns the adapter of the list view, which is contained by the dialog.
     *
     * @return The adapter of the list view, which is contained by the dialog, as an instance of the
     * type {@link ListAdapter} or null, if the dialog does not show any list items
     */
    public final ListAdapter getListAdapter() {
        return listAdapter;
    }

    /**
     * Returns the color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getItemColor() {
        return itemColor;
    }

    /**
     * Sets the color of the list items of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setItemColor(@ColorInt final int color) {
        itemColor = color;
        adaptItemColor();
    }

    /**
     * Returns the control color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getItemControlColor() {
        return itemControlColor;
    }

    /**
     * Sets the control color of the list items of the dialog.
     *
     * @param color
     *         The control color, which should be set, as an {@link Integer} value or -1, if no list
     *         items are shown or if the dialog has not been shown yet
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        itemControlColor = color;
        adaptItemControlColor();
    }

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setItems(@NonNull final CharSequence[] items,
                               @Nullable final OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        super.setView(-1);
    }

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final OnClickListener listener) {
        setItems(getContext().getResources().getTextArray(resourceId), listener);
    }

    /**
     * Sets the adapter, which provides the items, which should be shown by the dialog.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setAdapter(@NonNull final ListAdapter adapter,
                                 @Nullable final OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        super.setView(-1);
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@NonNull final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice,
                        items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[items.length];
        checkedListItems[checkedItem] = true;
        super.setView(-1);
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        setSingleChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItem,
                listener);
    }

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog.
     * Only one of the items can be selected at once.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[adapter.getCount()];
        checkedListItems[checkedItem] = true;
        super.setView(-1);
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnMultiChoiceClickListener} or null, if no listener should be
     *         notified
     */
    public final void setMultiChoiceItems(@NonNull final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                        items);
        listViewMultiChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
        checkedListItems = checkedItems;
        super.setView(-1);
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnMultiChoiceClickListener} or null, if no listener should be
     *         notified
     */
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        setMultiChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItems,
                listener);
    }

    /**
     * Sets the listener, which should be notified, when an item, which is shown by the dialog is
     * selected.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         AdapterView.OnItemSelectedListener} or null, if no listener should be notified
     */
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;

        if (listView != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
    }

    @Override
    public final void setView(@Nullable final View view) {
        listAdapter = null;
        listViewSingleChoiceListener = null;
        listViewChoiceMode = -1;
        super.setView(view);
    }

    @Override
    public final void setView(@LayoutRes final int resourceId) {
        listAdapter = null;
        listViewSingleChoiceListener = null;
        listViewChoiceMode = -1;
        super.setView(resourceId);
    }

    @Override
    public final void onStart() {
        super.onStart();
        adaptItemColor();
        adaptItemControlColor();
    }

    @Override
    public final void onStop() {
        super.onStop();
        listView = null;
    }

    @Override
    protected final View onCreateContentView(@NonNull final ViewGroup parent) {
        inflateListView(parent);
        showListView(parent);
        return listView;
    }

}