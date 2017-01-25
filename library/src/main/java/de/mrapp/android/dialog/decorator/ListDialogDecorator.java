/*
 * Copyright 2014 - 2017 Michael Rapp
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
package de.mrapp.android.dialog.decorator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.util.ArrayUtil;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and may contain list
 * items.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ListDialogDecorator extends AbstractDialogDecorator<ButtonBarDialog>
        implements de.mrapp.android.dialog.model.ListDialogDecorator {

    /**
     * The name of the extra, which is used to store the color of the list items of the dialog
     * within a bundle.
     */
    private static final String ITEM_COLOR_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::itemColor";

    /**
     * The name of the extra, which is used to store the dialog's items within a bundle.
     */
    private static final String ITEMS_EXTRA = ListDialogDecorator.class.getSimpleName() + "::items";

    /**
     * The name of the extra, which is used to store the dialog's single choice items within a
     * bundle.
     */
    private static final String SINGLE_CHOICE_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::singleChoiceItems";

    /**
     * The name of the extra, which is used to store the dialog's mnultiple choice items within a
     * bundle.
     */
    private static final String MULTI_CHOICE_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::multiChoiceItems";

    /**
     * The name of the extra, which is used to store the checked items within a bundle.
     */
    private static final String CHECKED_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::checkedItems";

    /**
     * The list view, which is used to show the dialog's list items.
     */
    private ListView listView;

    /**
     * The color of the list items of the dialog.
     */
    private int itemColor;

    /**
     * The adapter, which is used to manage the list items of the dialog.
     */
    private ListAdapter adapter;

    /**
     * The choice mode of the list view, which is used to show the list items of the dialog.
     */
    private int choiceMode = -1;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_SINGLE</code>.
     */
    private DialogInterface.OnClickListener singleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private DialogInterface.OnMultiChoiceClickListener multiChoiceListener;

    /**
     * The listener, which is notified, when a list item of the dialog becomes selected,
     * irrespective of the list view's choice mode.
     */
    private AdapterView.OnItemSelectedListener listViewItemSelectedListener;

    /**
     * An array, which is used to identify the list items of the dialog, which is created by the
     * builder, which are checked by default.
     */
    private boolean[] checkedItems;

    /**
     * The dialog's items.
     */
    private CharSequence[] items;

    /**
     * The dialog's single choice items.
     */
    private CharSequence[] singleChoiceItems;

    /**
     * The dialog's multiple choice items.
     */
    private CharSequence[] multiChoiceItems;

    /**
     * Adapts the list view, which is used to show the dialog's list items.
     */
    private void inflateListView() {
        if (getRootView() != null) {
            if (adapter != null && !adapter.isEmpty()) {
                getDialog().setView(R.layout.material_dialog_list_view);
                View listView = getRootView().findViewById(android.R.id.list);
                this.listView = listView instanceof ListView ? (ListView) listView : null;

                if (this.listView != null) {
                    this.listView.setVisibility(View.VISIBLE);
                    this.listView.setChoiceMode(choiceMode);
                    this.listView.setAdapter(adapter);
                    this.listView.setOnItemSelectedListener(listViewItemSelectedListener);
                    initializeSelectionListener();
                    initializeCheckedItems();
                }
            }
        }
    }

    /**
     * Initializes the list items, which are checked by default.
     */
    private void initializeCheckedItems() {
        if (checkedItems != null) {
            for (int i = 0; i < checkedItems.length; i++) {
                listView.setItemChecked(i, checkedItems[i]);

                if (checkedItems[i]) {
                    listView.setSelection(i);
                }
            }
        }
    }

    /**
     * Returns an array, which identifies the currently checked list items.
     *
     * @return An array, which identifies the currently checked list items, as {@link Boolean} array
     */
    @Nullable
    private boolean[] getCheckedItems() {
        if (listView != null && adapter != null) {
            boolean[] result = new boolean[adapter.getCount()];

            for (int i = 0; i < result.length; i++) {
                result[i] = listView.isItemChecked(i);
            }

            return result;
        } else {
            return checkedItems;
        }
    }

    /**
     * Initializes the listener, which should be notified, when the selection of a list item of the
     * dialog has been changed.
     */
    private void initializeSelectionListener() {
        if (choiceMode == ListView.CHOICE_MODE_NONE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(singleChoiceListener, getDialog(),
                            DialogInterface.BUTTON_POSITIVE));
        } else if (choiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(singleChoiceListener, getDialog(), 0));
        } else if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(multiChoiceListener, getDialog(), 0));
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
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * may contain list items.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the dialog, as an instance of
     *         the type {@link ButtonBarDialog}. The dialog may not be null
     */
    public ListDialogDecorator(@NonNull final ButtonBarDialog dialog) {
        super(dialog);
    }

    @Override
    public final ListView getListView() {
        return listView != null && listView.getVisibility() == View.VISIBLE ? listView : null;
    }

    @Override
    public final ListAdapter getListAdapter() {
        return adapter;
    }

    @Override
    public final int getItemColor() {
        return itemColor;
    }

    @Override
    public final void setItemColor(@ColorInt final int color) {
        itemColor = color;
        adaptItemColor();
    }

    @Override
    public final void setItems(@NonNull final CharSequence[] items,
                               @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        this.items = items;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        this.singleChoiceListener = listener;
        this.multiChoiceListener = null;
        this.choiceMode = ListView.CHOICE_MODE_NONE;
        inflateListView();
    }

    @Override
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final DialogInterface.OnClickListener listener) {
        setItems(getContext().getResources().getTextArray(resourceId), listener);
    }

    @Override
    public final void setAdapter(@NonNull final ListAdapter adapter,
                                 @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = listener;
        this.multiChoiceListener = null;
        this.choiceMode = ListView.CHOICE_MODE_NONE;
        inflateListView();
    }

    @Override
    public final void setSingleChoiceItems(@NonNull final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        this.items = null;
        this.singleChoiceItems = items;
        this.multiChoiceItems = null;
        this.adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice,
                        items);
        this.singleChoiceListener = listener;
        this.multiChoiceListener = null;
        this.choiceMode = ListView.CHOICE_MODE_SINGLE;
        this.checkedItems = new boolean[items.length];
        this.checkedItems[checkedItem] = true;
        inflateListView();
    }

    @Override
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        setSingleChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItem,
                listener);
    }

    @Override
    public final void setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                           final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = listener;
        this.multiChoiceListener = null;
        this.choiceMode = ListView.CHOICE_MODE_SINGLE;
        this.checkedItems = new boolean[adapter.getCount()];
        this.checkedItems[checkedItem] = true;
        inflateListView();
    }

    @Override
    public final void setMultiChoiceItems(@NonNull final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = items;
        this.adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                        items);
        this.singleChoiceListener = null;
        this.multiChoiceListener = listener;
        this.choiceMode = ListView.CHOICE_MODE_MULTIPLE;
        this.checkedItems = checkedItems;
        inflateListView();
    }

    @Override
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        setMultiChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItems,
                listener);
    }

    @Override
    public final void setMultiChoiceItems(@NonNull final ListAdapter adapter,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = null;
        this.multiChoiceListener = listener;
        this.choiceMode = ListView.CHOICE_MODE_MULTIPLE;
        this.checkedItems = checkedItems;
        inflateListView();
    }

    @Override
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;

        if (listView != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putInt(ITEM_COLOR_EXTRA, getItemColor());

        if (items != null) {
            outState.putCharSequenceArray(ITEMS_EXTRA, items);
        } else if (singleChoiceItems != null) {
            outState.putCharSequenceArray(SINGLE_CHOICE_ITEMS_EXTRA, singleChoiceItems);
            outState.putBooleanArray(CHECKED_ITEMS_EXTRA, getCheckedItems());
        } else if (multiChoiceItems != null) {
            outState.putCharSequenceArray(MULTI_CHOICE_ITEMS_EXTRA, multiChoiceItems);
            outState.putBooleanArray(CHECKED_ITEMS_EXTRA, getCheckedItems());
        }
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        setItemColor(savedInstanceState.getInt(ITEM_COLOR_EXTRA));
        CharSequence[] items = savedInstanceState.getCharSequenceArray(ITEMS_EXTRA);

        if (items != null) {
            setItems(items, null);
        } else {
            boolean[] checkedItems = savedInstanceState.getBooleanArray(CHECKED_ITEMS_EXTRA);
            CharSequence[] singleChoiceItems =
                    savedInstanceState.getCharSequenceArray(SINGLE_CHOICE_ITEMS_EXTRA);

            if (singleChoiceItems != null) {
                int checkedItem = checkedItems != null ? ArrayUtil.indexOf(checkedItems, true) : 0;
                setSingleChoiceItems(singleChoiceItems, checkedItem, null);
            } else {
                CharSequence[] multiChoiceItems =
                        savedInstanceState.getCharSequenceArray(MULTI_CHOICE_ITEMS_EXTRA);

                if (multiChoiceItems != null) {
                    setMultiChoiceItems(multiChoiceItems, checkedItems, null);
                }
            }
        }
    }

    @Override
    protected final void onAttach(@NonNull final Window window, @NonNull final View view) {
        inflateListView();
        adaptItemColor();
    }

    @Override
    protected final void onDetach() {
        listView = null;
    }

}