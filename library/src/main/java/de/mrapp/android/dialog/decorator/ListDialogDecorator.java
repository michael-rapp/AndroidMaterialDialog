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
package de.mrapp.android.dialog.decorator;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;
import de.mrapp.android.dialog.model.ButtonBarDialog;

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
    private DialogInterface.OnClickListener listViewSingleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private DialogInterface.OnMultiChoiceClickListener listViewMultiChoiceListener;

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
     * Adapts the list view, which is used to show the dialog's list items.
     */
    private void inflateListView() {
        if (getView() != null) {
            View listView = getView().findViewById(android.R.id.list);
            this.listView = listView instanceof ListView ? (ListView) listView : null;

            if (this.listView != null && listAdapter != null && !listAdapter.isEmpty()) {
                getDialog().setView(R.layout.material_dialog_list_view);
                this.listView.setVisibility(View.VISIBLE);
                this.listView.setChoiceMode(listViewChoiceMode);
                this.listView.setAdapter(listAdapter);
                this.listView.setOnItemSelectedListener(listViewItemSelectedListener);
                initializeListViewSelectionListener();
                initializeListViewCheckedItems();
            }
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
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, getDialog(),
                            DialogInterface.BUTTON_POSITIVE));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, getDialog(), 0));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(listViewMultiChoiceListener, getDialog(),
                            0));
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
        return listAdapter;
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
    public final int getItemControlColor() {
        return itemControlColor;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        itemControlColor = color;
        adaptItemControlColor();
    }

    @Override
    public final void setItems(@NonNull final CharSequence[] items,
                               @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
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
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        inflateListView();
    }

    @Override
    public final void setSingleChoiceItems(@NonNull final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice,
                        items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[items.length];
        checkedListItems[checkedItem] = true;
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
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[adapter.getCount()];
        checkedListItems[checkedItem] = true;
        inflateListView();
    }

    @Override
    public final void setMultiChoiceItems(@NonNull final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                        items);
        listViewMultiChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
        checkedListItems = checkedItems;
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
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;

        if (listView != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
    }

    @Override
    protected final void onAttach(@NonNull final View view) {
        inflateListView();
        adaptItemColor();
        adaptItemControlColor();
    }

    @Override
    protected final void onDetach() {
        listView = null;
    }

}